package com.tymeac.base;

/*
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import java.lang.reflect.*;
import java.util.concurrent.locks.*;

/**
 * Queue Thread base logic
 */
public abstract class AreaBasicThread 
            extends Thread {
        
  private final TyBase              T;         // base storage 
  private final AreaThreadMgmt      me;        // the mgt struct for this thread 
  private final AreaBase            area;      // the Area Base for this thread
  private final AreaWaitListsAnchor waitlists; // Area Wait Lists for this Queue  
    
  private final Lock lock;      // lock for this thread
  private final Condition cond; // condition for waiting
  
  private int force_stage;  // keep alive until this stage of shutdown
  private int keep_working; // continue working until this stage of shutdown
  
  private RequestDetail  req; // request detail
  
  private Object[] invoke_arg; // invoked arg       
  private Object[] o_args;     // passed object[] to PACs 
  private Method   pap_method; // method to invoke()   
  
  private int instance_nbr; // current instance number
  private boolean  had_problem;  // had a problem in processing 
    
// modified by multiple threads
  private volatile boolean posted;  // been posted
  private volatile boolean get_out; // time to leave
        
/**
 * Constructor
 * @param Ty TyBase
 * @param AB AreaBase
 * @param AT AreaThread
 * @param tname String - thread name
 */ 
protected AreaBasicThread ( TyBase Ty, 
                            AreaBase AB, 
                            AreaThreadMgmt AT,
                            String tname) {
    
  // name of thread
  super(tname); 
  
  // set pointer to base storage
  T = Ty; 

  // set pointer to area base to which this thread belongs  
  area = AB;
  
  // set pointer to area waitlists to which this thread belongs  
  waitlists = area.getWaitlist();

  // set pointer to area thread class to which this thread belongs    
  me = AT;

  // args for P.A.C.
  o_args = new Object[2];

  // put passed obj in 1st position (done in actual thread)
  o_args[0] = null;

  // tymeac interface in second arg
  o_args[1] = T.getTi();
  
  // args for the method invoke
  invoke_arg = new Object[] { o_args };
  
  // method to invoke
  pap_method = area.getMethod();
  
  // processing switches
  had_problem = false; // no problem
  posted      = false; // not posted
  get_out     = false; // not time to leave
  
  // for waiting and signaling
  lock = new ReentrantLock();
  cond = lock.newCondition();
  
  // set exception handler
  this.setUncaughtExceptionHandler(new AreaThreadExceptionHandler(T, me));
   
} // end-constructor 

/**
 * process the asynchronous request
 */
protected abstract void asyncRequest();

/**
 * Caught exceptions in method.invoke()
 */
protected abstract void caughtIllegalAccessException(IllegalAccessException e);
protected abstract void caughtInvocationTargetException(InvocationTargetException e);
protected abstract void caughtIllegalArgumentException(IllegalArgumentException e);
protected abstract void caughtThrowable(Throwable e);

/**
 * Check the wait lists for the next request
 *   When there is work, call [a]syncRequest() 
 */ 
private void checkWork ( ) {
      
  boolean iswork = true;

  // While there is work in the wait lists
  while (iswork) {
    
    //  when time to leave
    if  (get_out) {  
        
        // all done
        setProblem(); 
              
        // finished
        return;

    } // endif 
    
    // check in the waitlists
    req = waitlists.getFirstBusy();
  
    // When no more requests
    if  (req == null) {
    
        // no work
        iswork = false;

        // set status to Waiting, maybe
        me.setWaiting();

    } // endif 
    
    // When work was found:           
    if  (iswork) {    

        // When NOT a sync request (is an async request) 
        if  (!req.isSync()) {      
          
            // do async
            asyncRequest();
        }
        else {            
            // do sync
            syncRequest();
            
        } // endif

        // When something went wrong in request, get out
        if  (isProblem())  return;

    } // endif 
    
    // When in immediate shut down mode, get out
    if  (T.getEndit() > force_stage)  {
          
        // set disabled   
        me.setDisabled(2002);

        // all done here
        return;

    } // endif  
  } // end-while 
  
} // end-method

/**
 * Get the instance number
 * @return int
 */
protected int getInstance() { return instance_nbr; } // end-method

/**
 * Get the request
 * @return RequestDetail
 */
protected RequestDetail getReq() { return req; } // end-method

/**
 * Was there a problem
 * @return boolean
 */
protected boolean isProblem() { return had_problem; } // end-method

/**
 * Invoke the Processing Application Class 
 */ 
protected Object invokeApplication() {
  
  // return object
  Object back = null;
  
  // When status change to "in Link to Appl" Not good
  if  (!me.setLinkAppl(instance_nbr)) {
    
      // say so
      sayExpunged();
      
      return null;
      
  } // endif 
 
  try {
    // invoke the Processing Application Class 
    back = pap_method.invoke(null, invoke_arg);
      
  } // end-try
  
  catch (IllegalAccessException e) {
    
    caughtIllegalAccessException(e);
    
    return null;

   } // end-catch    
   
   catch (java.lang.reflect.InvocationTargetException e) {
     
     caughtInvocationTargetException(e);
    
     return null;
    
   } // end-catch   
       
  catch (IllegalArgumentException e) {
    
    caughtIllegalArgumentException(e);
    
    return null;
        
   } // end-catch        
   
   catch (java.lang.Throwable e) {
     
     caughtThrowable(e);
    
     return null;

  } // end-catch  

  // When Not had any problems
  if  (!isProblem()) {
  
      // When status change to "actively processing" NOT successful, say so
      if  (!me.setBackProcessing(instance_nbr))  sayExpunged();
      
  } // endif
  
  return back;
    
} // end-method 

/**
 * There was a fatel error
 */
protected void setProblem() { had_problem = true; } // end-method

/**
 * Write a log and print
 * @param msg
 * @param time
 */
protected void msgOut(String msg, int time) {
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(msg);
  
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, time);
  
} // end-method

/**
 * This thread no longer belongs here so log the message.
 */
protected abstract void sayExpunged();

/**
 * This is the Queue Thread main processing method. Stay here until shutting down.   
 */
public void run() {

  // spin until time to force shutdown         
  while (T.getEndit() < force_stage) {
            
    // when 'interrupted', get out
    if  (get_out) return;  

    // When 'posted' or 'started' or still 'processing'     
    if  (me.setReProcessing()) {

        /*
         * check for work to do. This usually returns with a status of 'waiting'
         *   since there are no more requests in waitlist. However, the
         *   status may not have been changed because new work came in before
         *   the status could be set to 'waiting'. 
         *   
         *   The queue thread finished with the wait lists. [it wants to change its
         *   status to 'waiting']
         *   
         *   A new request comes in.
         *   
         *   The queue thread sets its status to 'waiting' but a request comes in
         *   before this thread can change it's status. The scheduling thread 
         *   saw the status of this thread as 'processing' and believes a working
         *   Queue Thread will process the new request in the waitlist. (If it's
         *   status had been 'waiting' the scheduling thread would have changed 
         *   the status to 'posted'.) Therefore, we have work in the waitlists 
         *   and cannot issued a wait. The status reverts back to 'processing'.
         *   
         *   The status after checkWork() is either 'waiting' or 'processing' (if
         *   the above happended.)
         *   
         *   The status after waitWithTimeout() is either 'posted', 'processing' or
         *   'detached (timed-out). 
         *   
         *   The reason for all this checking is that when only one thread is processing 
         *   the Queue, a request can slip in as above and might never be processed. 
         *      
         */
        checkWork();

        // When something went wrong or stopping, get out
        if (isProblem()) return;
        
        // When in shut down mode:
        if (T.getEndit() > keep_working) {

            // status is disabled
            me.setDisabled(2002);

            // all done
            return;
            
        } // endif
        
        // we're in a 'waiting' or 'processing' status
        // Wait until posted or timed out
        waitWithTimeout();           
    } 
    else {
        // status is 'detached' (timed-out in the other wait) 
      
        // When in shut down mode:
        if  (T.getEndit() > keep_working) {
          
            // force status to processing
            me.setProcessing();

            // check for work to do, just in case
            checkWork();

            // status is disabled
            me.setDisabled(2002);

            // all done
            return;

        } // endif

        // *--- wait for activation by Threshold or overflow or Monitor ---*

        // Wait until someone needs me or thread death time
        waitWithoutTimeout();
        
    } // endif
  } // end-while 

  // shutting down. status is disabled
  me.setDisabled(2002);

} // end-method

/**
 * Send a notification message
 * @param msg java.lang.String
 */
protected void sendNotify (String msg ) {
    
  // When not using a notify, return
  if  (!T.isNotifyUsed()) return;
  
  // When this Queue is the Notification Queue, do not invoke
  //   itself.  Avoids a recurrsive error.
  if  (T.getNotifyName().compareTo(area.getName()) == 0) return;
  
  // do a notify
  T.getNotify_tbl().sendMsg(msg);
    
} // end-method

/**
 * Set the passed arg for the thread
 * @param arg
 */
protected void setArg(Object arg) { o_args[0] = arg; } // end-method

/**
 * Set this thread's instance number at creation
 * @param instance
 */
protected void setInstance(int instance) { instance_nbr = instance; } // end-method

/**
 * Set the new user class to invoke
 * @param userClass
 */
protected void setNewMethod(Method userClass) { pap_method = userClass; } // end-method

/**
 * final stages to keep processing
 *   1st -  Continue waiting for work.
 *           Keep looking for work while not in this stage of shutdown.
 *   2nd - Force stage of shut down.
 *           Keep the thread alive while not greater than this stage of shutdown.
 * 
 * @param keep
 * @param stop
 */
protected void setShutStages(int keep, int stop) {
  
  // stage of shutdown to keep working in
  keep_working = keep;

  // final stage to force end of processing
  force_stage = stop;
  
} // end-method 

/**
 * This thread has been asked to stop 
 */
protected void stopThread() {
    
  // time to get out
  get_out = true; 
  
  // wake up
  wakeUp();

}  // end-method

/**
 * Actually process the waiting synchronous request
 */
protected abstract void syncRequest();

/**
 * Wake up this posted thread 
 */
protected void wakeUp() {
  
  // get the lock
  lock.lock();
  
  try {
    // set posted indicator
    posted = true; 
    
    // wake up
    cond.signal();
    
  } finally {
    
    // release lock
    lock.unlock();
    
  } // end-finally

}  // end-method

/**
 * Wait for a post, no timeout. The status coming in is 'detached'. 
 * The status going out is 'posted'. 
 */
private void waitWithoutTimeout() {
  
  // lock 
  lock.lock();
  
    try {
      // spin until set true
      while (!posted) {
  
        try {
          // wait for re-activation
          cond.await();
          
        } // end-try
  
        catch (InterruptedException e) {
        } // end-catch     
      } // end-while

  } finally {
    
    // reset the posted indicator
    posted = false;
    
    // unlock
    lock.unlock();
    
  } // end-finally
  
} // end-method    

/**
 * Wait for a post, with timeout
 */
private void waitWithTimeout() {  
  
  // When not in waiting status, get out
  if  (!me.isWaiting()) return;
  
  // wait time from seconds to nanoseconds (*1k *1k *1k)
  long nanosTimeout = ((long)area.getWaitTime()) * 1000000000;
    
  // lock
  lock.lock();
  
    try {
      // spin until posted
      while (!posted) {
  
        try {
          // When not timed out        
          if (nanosTimeout > 0L) {
            
              // wait for a post or timeout   
              nanosTimeout = cond.awaitNanos(nanosTimeout);
          }
          else {
              // set is posted
              posted = true;
              
          } // endif
        } // end-try
  
        catch (InterruptedException e) {
  
        } // end-catch
      } // end-while

  } finally {
    
    // reset the posted indicator
    posted = false;
    
    // release lock
    lock.unlock();
    
  } // end-finally
  
  /*
   * We got here because the posted indicator was posted. 
   *   This was either because a new request came in or
   *   someone wanted to kill this thread or
   *   the wait time elapsed (time-out).
   *   The posted indicator was reset in the finally block. 
   * 
   * When we got to this method we were in a status of "WAITING" or we would
   * have returned immediately.
   * 
   * We cannot leave here with a status of "WAITING". 
   *   If the time elapsed -- meaning just that -- it doesn't mean that no work is here
   *      (there is a small window between when the work gets here and the wait time elapses.) 
   *      Even if the time elapses, if new work is here then there is no time-out.     
   *   
   *      We try to set the status from 'waiting' to 'detached' (atomically) and we look to see
   *      if new work is here. If there is new work, we change the status back from 'detached' to 'processing'.
   *      Exactly how we do a look-see is outside the scope of this Class. 
   *      
   *      The time may have elapsed even when a new request came in (status is then 'posted')  
   *   
   *   If we're still in a "waiting" status, 
   *     then we try to set the status from 'waiting' to 'processing' (atomically.) 
   *     
   *   If the atomic CAS's fail, we just leave it alone (it's 'posted'.)
   *   
   *   So, when we leave here the status is either 
   *     'posted' -- new work came in and killed the wait 
   *                  or a user wants this thread to die
   *     'processing' -- new work came in before the wait 
   *                       or after the time elasped
   *     'detached' -- no new work and the time elapsed 
   */
    
  // When a time out occurred
  if (nanosTimeout <= 0L) {
          
      // set status to 'detached' (maybe)      
      me.setTimedOut();      
  }
  else {
      // set status to 'processing' (maybe)    
      me.setNotWaiting();  
          
  } // end-if

} // end-method
} // end-class