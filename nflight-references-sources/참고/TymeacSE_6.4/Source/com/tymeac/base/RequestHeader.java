package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

 import java.util.*;
 import java.util.concurrent.*;

/**
 * The object that holds the request details array
 */
public final class RequestHeader {
   
  private final TyBase  T; // Tymeac base storage 
  
  /*
   * The request goes into this array as a new RequestDetail object.
   * 
   * Tymeac returns the RequestDetail pointer to the calling class. The pointer is then put 
   *   into the wait list of all the Tymeac Queues that need to process this request. 
   *   
   * This array is not used as a FIFO Queue. Therefore, poll() and peek() are not used. The
   *   array is used as a repository. The only way elements are removed from the queue is
   *   by remove(). The remove() method does not immediately remove the element, therefore,
   *   we always have some elements herein that have been freed by the Tymeac system but
   *   linger in the array (see the free() method of RequestDetail.) This is why we cannot
   *   use the size() method and why the iterator methods always check for 'freed' elements.  
   */
  private final ConcurrentLinkedQueue<RequestDetail> details; // array of detail entries

/**
 * Constructor
 * @param Ty TyBase - tymeac base storage
 */   
protected RequestHeader(TyBase Ty) {
   
   // Base storage
   T = Ty;    
    
  // get the array of details
  details = new ConcurrentLinkedQueue<RequestDetail>(); 
     
} // end-constructor

/**
 * Create a new detail entry and set it "in use" for Async Request
 * @param name long - generated name
 * @param queues AreaBase[] - array of queues in request
 * @param input Object - input data
 * @param function int - name of function
 * @return RequestDetail
 */ 
protected RequestDetail assignAsync ( 
                    long       name,
                    AreaBase[] queues,
                    Object     input,
                    FuncDetail function) {  
  
  // create 
  RequestDetail him = new RequestDetail(T,
                                        name,
                                        input, 
                                        function, 
                                        queues);
  
  // When put in request queue successful, give back new object
  return (details.offer(him))? him : null;
  
} // end-method

/**
 * Set an entry as 'in use' for sync request
 * @return RequestDetail
 * @param input Object - input object
 * @param uni long - unique name
 * @param cancel long - cancel word
 * @param waitime int - max time to wait in seconds
 * @param lock Lock - waiting request object
 * @parm caller Condition - waiting condition
 */
protected RequestDetail assignSync (
          Object input,
          long   uni,
          long   cancel,
          AreaBase[]  que_names, 
          int    waitime,
          CountDownLatch latch) {
  
  // create 
  RequestDetail him = new RequestDetail(T,
                                        input,
                                        uni, 
                                        cancel, 
                                        que_names, 
                                        waitime,
                                        latch);
  
  // When put in request queue successful, give back new object
  return (details.offer(him))? him : null;  
        
} // end-method

/**
 * Find the request in the detail entries and set the Sync entry to cancelled
 * @param req long - cancel word to check
 * @return boolean
 */
protected boolean cancelWordRequest (long cancel_word) { 
  
  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When a match, say what happended
    if  (him.getCancel() == cancel_word) return him.cancelSyncCancelWord(cancel_word);
    
  } // end-for
  
  // not found
  return false;
   
} // end-method

/**
 * Find an Async request in the detail entries and cancel it
 * @param req long - generated name to check
 * @return int
 */
protected int cancelAsyncRequest (long req) { 
  
  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When a match, say what happended
    if  (him.getUni() == req) return him.cancelAsync(req);
    
  } // end-for
  
  // not found
  return 7;
   
} // end-method

/**
 * Find the request in the detail entries and set the Sync entry to cancelled
 * @param req long - request id to check
 * @return boolean
 */
protected boolean cancelSyncRequest (long req) { 
  
  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When a match, say what happended
    if  (him.getUni() == req)  return him.cancelSync(req);
    
  } // end-for
  
  // not found
  return false;
   
} // end-method

/**
 * Diagnose - print all active entries
 */ 
protected void dsplyActiveAsyncDetails () {
  
  int max = 0, j = 0, k = 0;
      
  // all current
  for (RequestDetail him : details) { 
    
    // count
    max++;
        
    // When NOT sync, incr j
    if  (!him.isSync())  j++;
      
  } // end-for 
     
  TyBase.printMsg(
      "Total ALL Slots in use = " + max 
      + ", AR Slots in use at first scan = " + j 
      + ", total requests handled = " + (T.getGen_tbl().getAsync()-1));
  
  if  (j == 0) {
    
     TyBase.printMsg("AR None are in use, finished."); 
     return;     
     
  } // endif  
    
  Object input; 
  
  int nbr_que, remain, status, 
    checked, nxt;
  
  String backout, function;    
  AreaBase[]  queues;     
  long entered, name;  
  AreaBase agent;
  
  // all current
  for (RequestDetail him : details) { 
        
    // When NOT sync
    if  (!him.isSync()) {      
        
        status   = him.getStatus();
        entered  = him.getEntered();
        name     = him.getUni();
        input    = him.getInput();
        agent    = him.getAgent();
        function = him.getFunction().getName();
        nbr_que  = him.getNbr();
        remain   = him.getRemaining();
        checked  = him.getChecked();
        nxt      = him.getNextOut();
        queues   = him.getQueNames();
        //   output   = him.getOutput();
        
        // When backing out
        backout = (him.isBackout())? "true" : "false";
        
        TyBase.printMsg("Next Detail");
        
        TyBase.printMsg(
           "   In=" + input 
           + " Na=" + name
           + " staus=" + status
           + " #Q=" + nbr_que 
           + " Now=" + remain
           + " Agnt=" + agent.getName()
           + " Nxt=" + nxt
           + " Chk=" + checked
           + " Bout=" + backout
           + " Entered=" + entered);
        
        // when alive
        if  (him.isWorking()) {
        
            // not responding queues
            for  (k = 0; k < nbr_que; k++) {
                
              if  (queues[k] != null) {
                 
                  TyBase.printMsg("   NR = " + queues[k].getName());
                  
              } // endif        
            } // end-for 
        } // endif
    } // endif 
  } // end-for
  
} // end-method

/**
 * Diagnose - display in use
 */ 
protected void dsplyActiveSyncDetails () {
  
  int max = 0, j = 0, k;
  AreaBase[]  queues; 
  
  // all current
  for (RequestDetail him : details) { 
    
    // count
    max++;
        
    // When sync, incr j
    if  (him.isSync())  j++;
      
  } // endfor 
  
  TyBase.printMsg(
      "Total ALL Slots in use = " + max 
      + ", SR Slots in use at first scan = " + j 
      + ", total requests handled = " + (T.getGen_tbl().getSync() -1));
  
  if  (j == 0) {
     TyBase.printMsg("SR  None are in use, finished."); 
     return;     
  
  } // endif   

  Object input; 
  int nbr_que, nxt, status, waittime, remain;  
  boolean backed_out;     
  long name;
  
  // all current
  for (RequestDetail him : details) { 
        
    // When sync
    if  (him.isSync()) {    
          
        //    entered  = him.getEntered();
        name       = him.getUni();
        input      = him.getInput();
        nbr_que    = him.getNbr();
        nxt        = him.getNextOut();
        status     = him.getStatus();
        waittime   = him.getWait();
        backed_out = him.isBackout();
        queues     = him.getQueNames();
        
        // When finished there is no latch object
        remain = (him.isWorking())? him.getRemaining() : 0;

        //   output   = current.details[i].getOutput();
        
        TyBase.printMsg("Next Detail");
        
        TyBase.printMsg("   In=" + input + " Na=" + name
           + " #Q=" + nbr_que 
           + " Now=" + remain
           + " Next Output=" + nxt
           + " Backout=" + backed_out
           + " Waittime=" + waittime
           + " Status=" + status);
        //   + " Entered=" + entered);
        
        // when alive
        if  (him.isWorking()) {
        
            // not responding queues
            for  (k = 0; k < nbr_que; k++) {
                
              if  (queues[k] != null) {
                 
                  TyBase.printMsg("   NR = " + queues[k].getName());
                  
              } // endif        
            } // end-for 
        } // endif
        
        
      } // endif 
  } // endfor
  
} // end-method

/**
 * Find a request in the detail entries and return its progress. This is only valid
 * for requests that are stable. The progress may change dynamically.
 * @param req long - generated name to check
 * @return int
 */
protected int findRequest (long req) { 
  
  // for each detail in the array
  for (RequestDetail him : details) {
        
    // When a match and Not sync and alive
    if  ((him.getUni() == req) &&
         (!him.isSync())       &&
         (him.isWorking())) {
        
        // nbr of queues : remaining to process
        if  (him.getNbr() == him.getRemaining()) {
              
            // hasn't started yet
            return 8;
            
        } // endif              
        
        // is executing
        if  ((him.getRemaining() == 0) &&
             (him.getAgent() != null)) {
                                            
            // at OA stage
            return 9;
        }
        else {
            // not at OA stage yet        
            return 10;

        } // endif      
    } // endif
  } // end-for
         
   // not found
   return 7;
   
} // end-method

/**
 * Count all in use
 * @return int
 */
protected int getAllAsyncUsed() { 
      
  int j = 0;
  
  // for each detail in the array
  for (RequestDetail him : details) {
        
    // When Not a sync and alive, increment
    if  ((!him.isSync()) &&
         (him.isWorking()))  j++;
    
  } // end-for 
  
  // what we found
  return j;
    
} // end-method

/**
 * Count all in use
 * @return int
 */
protected int getAllSyncUsed() {   
    
  int j = 0;
  
  // for each detail in the array
  for (RequestDetail him : details) {
        
    // When a Sync and alive, increment
    if  ((him.isSync()) &&
         (him.isWorking()))  j++;
    
  } // end-for 
  
  // what we found
  return j;
    
} // end-method

/**
 * Count all in use 
 * @return int
 */
protected int getAllUsed() { 
  
  int j = 0;
  
  // for each detail in the array
  for (RequestDetail him : details) {
        
    // When alive, increment
    if  (him.isWorking())  j++;
    
  } // end-for 
  
  // what we found
  return j;
    
} // end-method

/**
 * Get the detail object
 * @return Detail
 * @param uni long
 */
protected RequestDetail getAsyncDetail (long uni) { 
  
  // for each detail in the array
  for (RequestDetail him : details) {
        
    // When a match and is busy (not cancelled) and an async, return the object
    if  ((him.getUni() == uni) && 
         (him.isBusy()) && 
         (!him.isSync()) )  return him;
    
  } // end-for 
  
  // not found
  return null;
    
} // end-method

/**
 * Return a list of all in-use async entries.
 * @return ArrayList of used entries
 */
protected ArrayList getInUseAsync () {
      
  // new list
  ArrayList<RequestDetail> temp_ar = new ArrayList<RequestDetail>();
  
  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When the entry is NOT a sync and is alive, add the reference
    if  ((!him.isSync()) && (him.isAlive()))  temp_ar.add(him);

  } // end-for    
    
  // used
  return temp_ar;      
    
} // end-method

/**
 * Return a list of all in-use sync entries.
 * @return ArrayList of used entries
 */
protected ArrayList getInUseSync () {
      
  // new list
  ArrayList<RequestDetail> temp_sr = new ArrayList<RequestDetail>();
  
  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When the entry is a sync and is alive, add the reference
    if  ((him.isSync()) && (him.isAlive()))  temp_sr.add(him);

  } // end-for    
    
  // used
  return temp_sr;      
    
} // end-method

/**
 * Find the cancel word in the detail entries
 * @param req long - cancel word to check
 * @return boolean
 */
protected boolean matchCancelWord (long cancel_word) {

  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When a match and not finished, good
    if  ((him.getCancel() == cancel_word) && 
         (him.isWorking()))  return true; 

  } // end-for
         
  // not found
  return false;
   
} // end-method

/**
 * Purge all details with a 'reset' status code 
 */
protected void purgeReset () { 
  
  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When a match, purge
    if  (him.isReset()) him.setFree();
    
  } // end-for 
   
} // end-method

/**
 * Remove a detail entry. 
 */
protected void removeDetail (RequestDetail him) {              
                    
  // remove this entry
  details.remove(him);
   
} // end-method
} // end-class
