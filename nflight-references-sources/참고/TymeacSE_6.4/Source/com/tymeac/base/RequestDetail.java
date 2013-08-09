package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2013 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * A request detail object. Contains fields necessary to track the request.
 * There is a basic part that is common to both async and sync. There are two additional parts
 * for async and for sync.  
 */
public final class RequestDetail {
    
// common part
    private final TyBase T;   // Tymeac base storage
    
    private final long   uni_name; // unique name of this entry (from GenTable class)
    private final long   entered;  // time entered
    private final Object input;    // input area pointer, if any
    private final int    nbr_que;  // total queue's in function  
    private final byte   type;     // Type of request 0=sync 1=async
    
    /*
     * This is where the output of each Processing Application Classes goes. Tymeac concatenates
     *   this array when returning to the caller or going on to an Output Agent Queue. This array
     *   doesn't need locking. Only one thread puts into a specific bucket at a time.
     *   
     * See below - next_output   
     */
    private final Object[] output; 
    
    /*
     * next output in list
     * 
     *  This is the subscript for the above output array. It starts off at zero.
     *    This is the next subscript available for an output pointer. The thread
     *    getAndIncrements this field and it now points to the next available slot.
     *    The output array doesn't need to be atomic since no two threads will put
     *    in the same element. 
     */
    private AtomicInteger next_output; 
    
    /*
     * Used for backing out Queues that did not execute; purging wait lists, etc.
     * 
     * The array is loaded with the reference to the Queue (AreaBase) in both buckets [area1][area1] etc.
     *   When a Queue Thread finishes the request, it nulls the second bucket corresponding to its
     *   AreaBase reference. The first bucket is never changed as to do so would cause a null pointer.
     *   Now, when there is a stall or time-out, the buckets in the second array that are not null are the 
     *   AreaBase Queues that did not finish.
     *   
     * When an async request stalls, what Queue's completed? This duelie makes that answer possible. The
     *   Stall Array GUI uses the second bucket for it's detail display.   
     *   
     * Currently this has only limited usefullness for sync requests. Purging wait lists, etc. For now, 
     *   only the diagnose() method makes use of it.
     *   
     * This array doesn't need locking. Only one thread nulls a specific bucket at a time.  
     */
    private final AreaBase[][] que_names;  
    
    /*
     * Status of this entry:
     *      
     *  Busy -- in use, 
     *      active
     *      
     *  Reset -- in use, 
     *      sync request timed out or cancelled. Ready to be freed by Monitor.
     *      Atomically set from Busy to Reset for timed out requests.
     *      Atomically set from Cancelled to Reset for cancelled requests when all work finishes.
     *      
     *  Cancelled -- in use, 
     *      client requested a cancel.
     *      Atomically set from busy to cancelled or reset to cancelled. 
     *      
     *  Freed -- transient, 
     *      Atomically set. 
     *      When a request finishes, it frees this object for the garbage collector.
     *      1. It removes the object from the ConcurrentLinkedQueue in the
     *         RequestHeader Class.
     *      2. It atomically sets this status.
     *      This status is just a marker. The remove doesn't actually remove the
     *      element right away (see source code for ConcurrentLinkedQueue.remove().)          
     *          
     */
    private AtomicInteger status;  
    
        private static final int BUSY      = 1;
        private static final int RESET     = 2;
        private static final int CANCELLED = 3;
        private static final int FREED     = 64;
  
    /*
     * Back out indicator. There was a problem with a Queue in the Function.
     * Now we try to back out all the other Queues in the Function or at least
     * let the executing Queues know not to save any output.
     * 
     * boolean returned to caller
     * 
     * Values:
     *     0 -- not in use
     *       returns false
     *       
     *     same as uni_name -- set in initializing.
     *       returns false  
     * 
     *     -1 -- in backout (atomically set)
     *       returns true
     */  
    private AtomicLong backout;

// async request
    private final FuncDetail function;      // function 
    private final AreaBase   out_agent;     // output agent queue
    private volatile int     times_checked; // times checked by monitor (added by monitor)
    private AtomicInteger    nbr_remaining; // queues remaining to be processed

// sync request  
    private final int      wait_time; // max wait time in milliseconds
    private final long     cancel;    // used for canceling request (cancel word)  
    private volatile int   comp;      // completion code of a single thread (only set when error)    
    private CountDownLatch latch;     // for posting caller

/**
 * Constructor Async
 */
protected RequestDetail(
                     TyBase     Ty,
                     long       u_name,
                     Object     u_input,
                     FuncDetail u_function,
                     AreaBase[] u_queues) {
   
  T             = Ty;     // base storage 
  input         = u_input;   // input reference
  uni_name      = u_name;    // generated name
  entered       = System.currentTimeMillis();  // time entered 
  status        = new AtomicInteger(BUSY);    // is busy              
  nbr_que       = u_queues.length;   // number of queues
  next_output   = new AtomicInteger(0); // no next output
  backout       = new AtomicLong(uni_name);  // initial, not being backed out
  type          = 1; // type is async
  
  function      = u_function;  // function 
  out_agent     = function.getAgent(); // output agent queue name pointer
  times_checked = 0;  // async hasn't been checked by monitor
  nbr_remaining = new AtomicInteger(nbr_que); // all remain to be processed
  
  // new array of queues
  que_names = new AreaBase[nbr_que][2];
  
  // When agent
  if  (out_agent != null) {
    
    // get new output array
    output = new Object[nbr_que];  
  }
  else {    
    // pointer is null, no agent: we don't keep any output
    output = null;  
    
  } // end if
  
  // move all the queue names to the queue array, null output elements
  for (int i = 0; i < nbr_que; i++) {

    // copy twice; to each bucket
    que_names[i][0] = u_queues[i];
    que_names[i][1] = u_queues[i];
    
    // When agent, null slot
    if  (out_agent != null) output[i] = null;
  
  } // end-for 
  
  // only used in sync request
  wait_time = 0;
  cancel    = 0;
       
} // end-constructor

/**
 * Constructor Sync
 */
protected RequestDetail(
                     TyBase     Ty,
                     Object     u_input, 
                     long       u_uni,
                     long       u_cancel,
                     AreaBase[] u_que_names,  
                     int        u_wait,
                     CountDownLatch u_latch) {  
   
  T           = Ty;     // base storage 
  input       = u_input;     // input reference
  uni_name    = u_uni;      // generated name
  entered     = System.currentTimeMillis();  // time entered 
  status      = new AtomicInteger(BUSY);    // is busy              
  nbr_que     = u_que_names.length;   // number of queues
  next_output = new AtomicInteger(0); // no next output
  backout     = new AtomicLong(uni_name);  // initial, not being backed out
  type        = 0; // type is sync  
  wait_time   = u_wait;     // max milliseconds to wait
  comp        =  0;          // completion code of zero
  latch       = u_latch;    // lock obj
  cancel      = u_cancel; // cancel word
  
  // new array of queues
  que_names = new AreaBase[nbr_que][2];
    
  // get new output array
  output = new Object[nbr_que]; 
  
  // move all the queue names to the queue array, null output elements
  for (int i = 0; i < nbr_que; i++) {

    // copy twice; to each bucket
    que_names[i][0] = u_que_names[i];
    que_names[i][1] = u_que_names[i];
    
    // null slot
    output[i] = null;
  
  } // end-for 
  
  // only used in async request
  function  = null;
  out_agent = null;
  
       
} // end-constructor

/**
 * Increment number of times this entry was checked for being here
 * @return int
 */        
protected int addChecked() { return ++times_checked; } // end-method 

/**
 * For an active entry: purge any request that may be in a wait list
 *    When none ever started, free the entry
 * @param ar_nbr int - the reqeust number
 */ 
protected int cancelAsync (long uni) {
  
  // When sync, not same
  if  (isSync()) return 7; 
  
  // When uni numbers don't match, not same
  if  (getUni() != uni) return 7; 
  
  // When NOT able to cancel, not same
  if  (!status.compareAndSet(BUSY, CANCELLED)) return 7; 
  
  // mark for backout
  setBackout(uni);

  // number remaining now
  int now = getRemaining();
  
  // When in OA stage, can't purge
  if  ((now == 0) && (getAgent() != null))  return 10;

  // total queues in list
  int max = getNbr(); 

  // purge the waitlist entries
  int freed = purgeWaitLists();

  // When none had finished
  if  (now == max) {

      // When none now remain
      if  (getRemaining() == 0) {

          // When every request was removed before it could start
          if  (freed == max) {

              // safe to remove entry
              setReset();

              // never began
              return 11;
          
          } // end-if
      } // endif
  } // endif

  // some were exec or still are
  return 12;
     
} // end-method

/**
 * Try to set the status code to cancelled, try to purge waiting request and
 *   wake up the endpoint connection thread
 *   
 * @return boolean  
 */
protected boolean cancelSync(long request) {
  
  //When Not sync, not same
  if  (!isSync()) return false; 
  
  // When the same request id
  if  (request == uni_name) {
  
      // When able to cancel
      if  ((status.compareAndSet(BUSY, CANCELLED)) ||
           (status.compareAndSet(RESET, CANCELLED))) {  
    
          // say in backout
          setBackout(uni_name);
          
          // When some threads did not finish, try to purge the waitlist entries
          if  (getRemaining() > 0) purgeWaitLists();
    
          // get the latch count to zero
          setLatchZero();
          
          // did cancel
          return true;
          
      } // endif
  } // endif  
  
  // did not cancel
  return false;
    
} // end-method

/**
 * When the cancel word matches, its the same as cancelling with a request id.
 *   
 * @return boolean 
 */
protected boolean cancelSyncCancelWord(long cancel_id) {
  
  // When Not the same cancel word, not valid
  if  (cancel_id != cancel) return false;
  
  // same as using a request id
  return cancelSync(uni_name);
    
} // end-method

/**
 * Check for excessive time in Async processing
 * @return boolean
 */    
protected boolean checkExcessive() {
  
  // When already over time
  if  (times_checked > 0) return true;

  // When here over 5 minutes
  if  (System.currentTimeMillis() > (entered + T.getAsync_timeout())) return true;

  // not over max
  return false;
   
} // end-method 

/**
 * Accessor for output agent
 * @return AreaBase
 */
protected AreaBase getAgent ( ) { return out_agent; } // end-method
   
/**
 * get cancel word
 * @return
 */
protected long getCancel() { return cancel; } // end-method

/**
 * get checked times
 * @return int
 */
protected int getChecked() { return times_checked; } // end-method

/**
 * Get the completion code
 * @return int
 */
protected int getComp() { return comp; } // end-method

/**
 * Get the time entered
 * @return long
 */         
protected long getEntered() { return entered; } // end-method 

/**
 * Accessor for the function number
 * @return FuncDetail
 */        
protected FuncDetail getFunction() { return function; } // end-method  

/**
 * Get the input object
 * @return Object
 */         
protected Object getInput() { return input; } // end-method 
    
/**
 * Get the count of remaining queues for sync request
 * @return int
 */                 
protected int getLatchCount() { return (int)latch.getCount(); } // end-method

/**
 * Get the number of queues
 * @return int
 */                 
protected int getNbr() { return nbr_que; } // end-method

/**
 * Get the next output position
 * @return int
 */                 
protected int getNextOut() { return next_output.get(); } // end-method 

/**
 * Get the output array
 * @return object[]
 */
protected Object[] getOutput() { return output; } // end-method  

/**
 * Get the array of queue names that finished/did not finish. Only need the second
 *   array in the AreaBase[][].
 * @return AreaBase[]
 */
protected AreaBase[] getQueNames() {
  
  // new array
  AreaBase[] queues = new AreaBase[nbr_que];
  
  // move only the second array buckets
  for (int i = 0; i < nbr_que; i++) {
    
      queues[i] = que_names[i][1];    
    
  } // end-for  
    
   return queues;  
    
} // end-method     
  
/**
 * Get the number of queues remaining to be processed
 * @return int
 */ 
protected int getRemaining() { 
  
  // When a sync request/async request
  return (isSync()) ? getLatchCount(): nbr_remaining.get();  
    
} // end-method 

/**
 * Get the status code
 * @return int
 */ 
protected int getStatus() { return status.get(); } // end-method  

/** 
 * Get the unique name of this request 
 * @return long  
 */
protected long getUni() { return uni_name; } // end-method  

/**
 * Get the max time to wait for completion
 * @return int
 */
protected int getWait() { return wait_time; } // end-method  

/**
 * Check to see if this entry is actually in use. If the request is in backout
 *   and all the queues have finished, then set the entry to reset.
 * @return boolean
 */
protected boolean isAlive() {

  // When all queues have finished and in backout and not finished
  if  ((getRemaining() == 0) &&  
       (isBackout()) &&
       (isWorking())) {

      // When set to reset worked
      if (setReset()) return false;
        
  } // endif

  // alive
  return true;

} // end-method

/**
 * Get the backout indicator 
 * @return boolean
 */
protected boolean isBackout() {
    
  // When -1 is backing out
  return (backout.get() == -1)? true : false;
    
} // end-method

/**
 * Check to see if this entry is busy
 * @return boolean
 */
protected boolean isBusy() {

  // When status is busy
  return (status.get() == BUSY)? true : false;    

} // end-method

/**
 * Check to see if this entry is cancelled
 * @return boolean
 */
protected boolean isCancelled() {

  // When status is cancelled
  return (status.get() == CANCELLED)? true : false;   

} // end-method

/**
 * Check to see if this entry is reset
 * @return boolean
 */
protected boolean isReset() {

  // When status is busy
  return (status.get() == RESET)? true : false; 

} // end-method

/**
 * Get the type
 * @return int
 */ 
protected boolean isSync() {
    
  // When 0 is sync
  return (type == 0)? true : false;
    
} // end-method  

/**
 * Check to see if this entry is Not finished
 * @return boolean
 */
protected boolean isWorking() {
  
  int stat = status.get();

  // When status is busy, reset or cancelled, say working
  if  ((stat == BUSY)   ||
       (stat == RESET)  ||
       (stat == CANCELLED))  return true;   

  // not busy
  return false;

} // end-method

/**
 * For an active entry: purge any Async request that may be in a wait list then set this entry
 *    free.
 * @param ar_nbr int - the reqeust number
 */ 
protected void purgeEntry (long uni) {
    
  // When this is not here, get out
  if  (uni != uni_name) return;

  // free the waitlist entries
  purgeWaitLists(); 

  // free the entry
  setFree();
   
} // end-method

/**
 * purge all the wait list entries for this Async request
 * @param ar_nbr int - the request number
 */
private int purgeWaitLists() {

  // When finished
  if  (que_names == null) return 0;

  // max queues in request
  int max = que_names.length;

  // number reset
  int count = 0;

  // loop thru all the queues
  for (int i = 0; i < max; i++) {
    
    // queue in second array
    AreaBase area = que_names[i][1];
    
    // When not finished
    if  (area != null) { 

        // purge waiting request
        if  (area.getWaitlist().resetEntry(this)) {

            // decrement remaining
            setDecrement(area);

            // incr count
            count++;

        } // endif
    } // endif
  } // end-for

  // When no output agent queue, done here
  if  (out_agent == null) return count;

  // purge waiting request
  out_agent.getWaitlist().resetEntry(this);

  // done
  return count;

} // end-method

/**
 * When excessive time has elapsed, set the status of this entry to "reset". It will be
 *   purged the next time the monitor runs. 
 */
protected void resetExcessive() {
  
  // calc max time to purge

  // quad request wait time
  long max_time = (wait_time * 4); 

  // When < 3 minutes
  if  (max_time < T.getReset_timeout()) {

      // max result is 3 minutes from now
      max_time = entered + T.getReset_timeout();
  }
  else {
      // max result is quad the wait time from now 
      max_time += entered; 

  } // endif

  // When over max, set to reset
  if  (System.currentTimeMillis() > max_time) setReset();

} // end-method

/**
 * Set the backout indicator   
 */
protected void setBackout(long uni) {
    
  // When the same id, say is backing out
  backout.compareAndSet(uni, -1L);
    
} // end-method

/**
 * Set the completion code. It may look like a race condition but only one thread will update this
 * field and only when there is a problem. If two threads have a problem at the same time,
 * so what. Only one status can go back so it really doesn't matter.
 * @param code int - completion code from queue thread
 */
protected void setComp(int code) { comp = code; } // end-method 

/**
 * Decrement number of queues remaining to be processed and null the queue
 *   in the array of queue names that did the processing.
 * @return int
 */ 
protected int setDecrement(AreaBase que) { 
  
  //  null que in array of que names
  setQueNull(que);  
  
  // When a sync request
  if  (isSync()) {
    
      // say a thread completed
      setPosted(); 
      
      // doesn't matter what goes back
      return 0;
  }
  else { 
      // decrement number remaining, give back how many remain
      return nbr_remaining.decrementAndGet();
  
} // endif 
    
} // end-method

/**
 * Remove the entry from the details list and mark as freed
 */
protected void setFree () {   
  
  // remove from array
  T.getRequest_tbl().removeDetail(this);
  
  // say freed
  status.set(FREED);
        
} // end-method

/**
 * Set the latch count to zero since we don't interrupt the waiting thread 
 */
private void setLatchZero () {
  
  // set the latch count to zero
  for (; latch.getCount() > 0; latch.countDown()) {        
  } // end-for
  
} // end-method

/**
 * Set the output object into the output object array
 * @param obj Object
 */
protected void setOutput (Object obj) {     
  
  // add this entry 
  output[next_output.getAndIncrement()] = obj;
  
} // end-method

/**
 * Set the awaiting (maybe) request as finished. The request may not
 * have reached the await yet. Doing a countDown() when no thread is waiting
 * has no affect.
 */
private void setPosted() { latch.countDown(); } // end-method

/**
 * Null the second queue reference in the array of queue names. 
 * We need a duelie since we cannot null a reference that another thread may pick up
 *   at any time.
 * It may look like a race condition but each thread only modifies one element in the list.
 * @param que AreaBase
 */
private void setQueNull(AreaBase que) {
  
  // get queue number
  int number = que.getQueNumber();
  
  // find que in array
  for (int i = 0; i < nbr_que; i++) {
    
    // When a match on 1st array 
    if (que_names[i][0].getQueNumber() == number) {

        // say finished on second
        que_names[i][1] = null;

        // all done
        return;

    } // endif
  } // end-for       
} // end-method  

/**
 * Set the status code to 'reset'
 * (the monitor will flush this request when next it runs)  
 */
private boolean setReset() {
    
  // When set to reset worked
  if  ((status.compareAndSet(BUSY, RESET)) || 
       (status.compareAndSet(CANCELLED, RESET))) {
    
      // successful
      return true;
    
  } // endif
  
  // no good
  return false;
    
} // end-method
} // end-class
