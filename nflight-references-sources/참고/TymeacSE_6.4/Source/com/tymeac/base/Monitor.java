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

import java.rmi.activation.ActivationException;
import java.util.*; 

/**
 * The Tymeac Monitor 
 */ 
public final class Monitor
        extends Thread {  
  
  // base storage   
  private final TyBase  T;  
  
  // Stall table positioning
  private final StallHeader stall_start;
  
  // Request table positioning
  private final RequestHeader request_tbl;
  
  // Number Generation Class
  private final GenTable gen;
   
  // time at each dispatch
  private long time;

  // last no-activity time
  private long last_time;
   
  // for checking activity now
  private long last_sync  = 0, last_async = 0;
  
  // for checking activity in prior cycle
  private long my_last_sync = 0, my_last_async = 0;

  // time to notify admin
  private long time_to_notify = 0; 
  
  /**
   * This field is for shut down processing within the inactivation logic. Non activation
   *   is easier; somebody requested it and that somebody probably will monitor the
   *   shut down progress. 
   *   
   *   What can happen is that a shut down request may enter the system not
   *     followed by secondary requests for shutdown (stage 2, 3). (If the application
   *     threading environment is hanging and cannot end normally then it may require
   *     manual intervention. The inactivation logic is only entered when there is 
   *     no activity.)
   * 
   *     The logic for the shutdown_count is that:
   *        When at the time of inactivation, if the system is already in shut down mode
   *        (stage 1, 2, 3), then let normal shutdown continue but decrement the count
   *        of times we've been in this state. (Each time the Monitor runs,
   *        it decrements the count.) If normal shut down continues, then we never
   *        get back here.
   * 
   *        When the count is less than 1, then assume the system is either broken beyond repair,
   *        Stage 1 shut down exits are hanging or someone forgot to complete the
   *        shutdown procedure. Proceed with the inactivation logic which ends in
   *        an unregister (shut down) rather than inactivate.
   * 
   *        Since shut down mode prevents new (a)syncRequest()'s and the inactivation
   *        logic checks for NO ACTIVITY WITHIN THE PERIOD, this is a reasonable
   *        assumption.
   * 
   *        Set the value of shutdown_count to the number of cycles of the Monitor
   *        you feel is good for you. The default is 3. 
   * 
   *        E.G. If the shutdown_count is 3, (meaning we need 3 cycles of the Monitor) 
   *             the Monitor interval is 60 seconds (1 minute) and
   *             the inactivation interval is 2 minutes:
   * 
   *             When there is no activity for 2 minutes, during Monitor execution, the
   *             Monitor enters the inactivation logic. Within that logic it finds
   *             that the system is in shut down mode. The Monitor now must decrement
   *             shutdown_count three times (during 3 minutes time) before continuing
   *             with an inactivation.
   */
  private int shutdown_count = 3;
  
  /**
   * This field is similar to the above. It is for stage 2 shutdown when shut down
   *   was requested elsewhere.
   * 
   *   After the stage 1 user exit Classes, only one thread may proceed to the
   *   stage 2 shut down proceedure. Stage 2 should end with a system.exit(). 
   *     If the shut down thread is not in use, then this process will hang forever.
   *     No new requests may process since stage 1 stops that. The server is of no use
   *     to anyone and should be shut down.
   *     
   *   When another thread has passed this point, we decrement the count of times we've been here. 
   *     (Each time the Monitor runs, it decrements the count.) 
   *     If normal shut down continues (system.exit()), then we never get back here.
   *     
   * When the count reaches zero, a message is sent to the log and notify.    
   */
  private int shutdown_stage2_count = 3;

/**
 * Constructor
 * @param Ty TyBase
 */   
protected Monitor (TyBase Ty)  {       
  
  super("TymeacMonitor");   
  
  // base storage
  T = Ty;
  
  // the array's starting position
  stall_start = T.getStall_tbl(); 
  request_tbl = T.getRequest_tbl(); 
  
  // number generation class
  gen = T.getGen_tbl(); 
  
  // last activity is now
  last_time = System.currentTimeMillis();
  
  // set exception handler
  this.setUncaughtExceptionHandler(new MonitorExceptionHandler(T));

} // end-constructor

/**
 * Check the Async table entry for over-time-to-flag. 
 * When over the limit, cancelled entries are purged, busy entries create stall array entry.  
 */
private void checkAsync() {
    
  int max, i, checked;
  long ar_name = 0L;
  
  // detail
  RequestDetail req;
    
  // new temp list
  ArrayList ar_list;   

  // get the used requests from the table
  ar_list = request_tbl.getInUseAsync(); 
    
  // total 
  max = ar_list.size();
    
  // When none, done 
  if  (max == 0) return;      
  
  // do all
  for (i = 0; i < max; i++) {
      
    // next entry
    req = (RequestDetail)ar_list.get(i);
    
    // start
    checked = 0;
    
    // When still active
    if  ((req != null) &&
         (req.isWorking())) {
      
        // name of entry
        ar_name = req.getUni();
    
        // When over the max time
        if  (req.checkExcessive()) { 
          
            // When was cancelled
            if  (req.isCancelled()) {
              
                // purge the entry
                req.setFree();
            }
            else {
                // increment the times checked
                checked = req.addChecked();
              
            } // endif              
        } // endif 
      
        // When it's been around too long 
        if  (checked > 1) { 
    
            // When the entry was added
            if  (stall_start.addEntry(ar_name, 3030)) {                                                            
                        
                // form a log the message 
                String L_msg = TyMsg.getMsg(3015) + ar_name;
        
                // send notify msg
                sendNotify(L_msg);
          
                // write a log msg
                writeLog(L_msg);
    
            } // endif                         
        } // endif 
    } // endif
  } // end-for 

  // done
   return;

} // end-method

/**
 * Check the Sync table entry for over-time-to-flag 
 */
private void checkSync() {
  
  // detail
  RequestDetail req;
  
  // new temp list
  ArrayList sr_list = request_tbl.getInUseSync(); 
    
  int  i, max = sr_list.size();
  
  // When none stalled, done 
  if  (max == 0) return;      
  
  // do all
  for (i = 0; i < max; i++) {
      
    // next entry
    req = (RequestDetail)sr_list.get(i);
    
    // When still active
    if  (req.isWorking()) {
  
        // check for over limit 
        req.resetExcessive(); 
        
    } // endif        
  } // end-for 

} // end-method

/**
 * Check the stall table entry for over-time-to-flag
 */
private void checkStall() {    

  // work
  int i, max, remain, checked = 0, rc = 0;
  long stall_ar_name = 0;
  
  // request detail
  RequestDetail req; 
  
  // stall detail
  StallDetail s_detail;
  
  // new temp list
  ArrayList temp_stall;
  
  // get the used requests from the table
  temp_stall = stall_start.getNextUsed();    

  // total stalled
  max = temp_stall.size();
  
  // When none stalled
  if  (max == 0) {
    
      // last notified reset
      time_to_notify = 0L;
      
      // back
      return;
      
  } // endif
  
  // do all in list
  for (i = 0; i < max; i++) {
    
    // at beginning
    checked = 0;
      
    // next stalled entry
    s_detail = ((StallDetail)temp_stall.get(i));       
      
    // get the async request name
    stall_ar_name = s_detail.getAtName();
    
    // get the detail using the stall uni name
    req = request_tbl.getAsyncDetail(stall_ar_name);
            
    // When was purged or been freed or not same as stall
    if  ((req == null) ||
         (!req.isWorking()) ||  
         (stall_ar_name != req.getUni())) {                 
        
        // say no longer here
        remain = -1;    
    }
    else { 
        // get the number of remaining queues from the ar
        remain = req.getRemaining(); 
        
        // When none remain and being backed out
        if  ((remain == 0) && 
             (req.isBackout())) {
      
            // purge the async table entry
            req.setFree(); 
            
            // say no longer here
            remain = -1;             
       
        } // endif 
    } // endif 
    
      // When ar doesn't exist
      if  (remain == -1) {
                    
          // purge the stall table entry 
          s_detail.setFree();              
      }
      else {                 
          // increment the times checked
          checked = s_detail.addChecked();            
            
      } // endif
          
    // When here more than this many minutes
    if  ((checked * 60) > T.getTime_stalled()) {
      
        // When time to notity
        if  (time_to_notify < time) { 
          
            // set new time to notify
            time_to_notify = time + (T.getTime_stalled() * 1000); 
    
            // need new message
            rc = 1; 
                       
        } // endif  
    } // endif
  } // end-for
  
  // When there were stalled entries
  if  (rc > 0) {

      // Message
      String N_msg = TyMsg.getMsg(3005);
  
      // send notify msg
      sendNotify(N_msg);
  
      // write a log msg
      writeLog(N_msg);
      
      N_msg = null;
  
  } // endif   

} // end-method

/**
 * Check each queue area
 */
private void checkArea() {
  
  // Level 1 definition
  AreaBase area;
  
  // Level 2 (threads) definition
  AreaThreadsAnchor anchor;
  
  // errors
  String N_msg = null;
  
  // work
  int i, nbr_entries, rc;
  
  // number of queue's in system
  nbr_entries = T.getMain_tbl().getNbrEntries();
    
  // loop thru all the Queue Areas
  for  (i = 0; i < nbr_entries; i++) {
      
    // get the next area addresses        
    area   = T.getMain_tbl().getArea(i);
    anchor = area.getThreadsAnchor();
      
    // no problem
    rc = 0; 
                        
    // set to disabled those previously cancelled
    if  (anchor.disableCancelled() > 0) {
  
        // When not in shut down mode, do msg
        if  (T.getEndit() == TymeacInfo.AVAILABLE) { 
  
            // All are disabled. build notification and log
            N_msg = TyMsg.getMsg(3020)  + area.getName();

            // say is pending
            rc = 1;

        } // endif            
    } 
    else {
        // When none are alive
        if  (!anchor.checkThreads()) {

            // When not in shut down mode, do msg
            if  (T.getEndit() == TymeacInfo.AVAILABLE) { 

                // All are disabled. build notification and log
                N_msg = TyMsg.getMsg(3025)  + area.getName();  

              // say is pending
              rc = 1;

           } // endif                          
        } // endif            
    } // endif      
    
    // set to detached or cancelled those with excessive time  
    anchor.cancelExcessive(time);
  
    // When any pending msg
    if  (rc == 1) {
  
        // When this is not the notify Queue
        if  (T.getNotifyName().compareTo(area.getName()) != 0) {
    
            // send a notify
            sendNotify(N_msg);
          
        } // endif
        
        // log
        writeLog(N_msg);  
    
        // reset pending
        rc = 0;
      
    } // endif 
  } // end-for
  
} // end-method

/**
 * Check for time to inactivate
 */
private void checkInactivate() {
  
  // last sync request in prior cycle
  my_last_sync = last_sync;
  
  // last sync request processed 
  last_sync = gen.getSync();
  
  // last async request in prior cycle
  my_last_async = last_async;
  
  // last async request processed
  last_sync = gen.getAsync();
  
  // When there was no activity during last cycle
  if  ((my_last_sync  == last_sync)  && 
       (my_last_async == last_async) &&
       
       // and there is nothing active now 
       (request_tbl.getAllUsed() == 0))  { 
       
      // When we're past time to wait
      if  ((((time - last_time) / (1000 * 60)) > T.getInactivateMinutes())){
        
          // do the inactivation (this may not return)
          doInactivate();
      }
      else {
          // wait some more
          return;
          
      } // endif       
  }
  else {
      // set last activity time as now
      last_time = time;
 
      // was activity, not inactivating
      return; 
      
  } // endif 
  
} // end-method

/**
 *The main processing method
 */   
private void doWork( )  {    
  
  // get the current time in milliseconds
  time = System.currentTimeMillis();
 
  // Stall Array Processing 
  checkStall();                
    
  // give up control
  Thread.yield();   
   
  // Async Table Processing
  checkAsync();
    
  // give up control
  Thread.yield();   
   
  /*
   * Sync Table Processing is in two stages. 
   *   First -- find all the entries that are marked "reset" and purge those.
   *   Next  -- find all the entries that are over the time limit for keeping
   *            and mark those "reset".
   */  
    
  // Reset processing
  request_tbl.purgeReset();
  
  // give up control
  Thread.yield();         
  
  // Time out processing
  checkSync();
  
  // give up control
  Thread.yield();
  
  // Queue Area Processing
  checkArea();
        
  // When using the activation system, check time for inactivation
  if  (T.getInactivateMinutes() > 0)  checkInactivate();
 
} // end-method

/**
 * Try to inactivate this process. When successful, this method does not return, it
 *   ends with System.exit().
 *  
 * There is no checking for activity as in a normal shutRequest() (requiring
 *   multiple requests for shutdown) since we checked that before geting here. 
 *   If some request squeaks in between then and now, it really is past
 *   the time to inactivate, so we just finish the inactivation. In any case, once
 *   the shut down sequence begins, there is no way to reverse it.
 * 
 * If the system is already in shut down mode, then we continue with that request
 *   and end with an unregister rather than inactivate. Now, that's when things get
 *   dicey. Each stage of shut down can only be executed by one thread at a time.
 *   Stage 1 and 2 may use user exits which must execute in order. Stage 2 writes
 *   statistics etc, which can only be done once. There are checks at each stage to
 *   force single-threading.
 */   
private void doInactivate () {
  
  // shut down was Not requested
  boolean shutdown_requested = false;
  
  // lock on top level 
  synchronized (T) {
    
    // shuting down indicator
    switch (T.getEndit()) {
      
      // currently not in shut down     
      case TymeacInfo.AVAILABLE:
       
        // now in stage 1        
        T.setEndit(TymeacInfo.SHUT_STAGE1);
    
        // origin of shutdown is by inactivation
        T.setShutOrigin(TymeacInfo.DEACTIVATION);
    
        // done here      
        break;
      
      // currently in another stage of shutdown 
      //  (the Server received a shutRequest() before we got here)       
      default: 
       
        // When not over time to force inactivation, let normal shutdown continue
        if  ((--shutdown_count) > 0) return;
      
        // When Not finished with the shut down stage 1, can't start next stage yet 
        if  (!T.isStage1Done()) { 

            // this shut down may be stalled
            String N_msg = TyMsg.getMsg(3201);
  
            // print locally
            printMsg(N_msg);
        
            // write a log msg
            writeLog(N_msg);
          
            // wait some more
            return;  
          
        } // endif
        
        // someone did a shut request
        shutdown_requested = true;
        
        // proceed with shut down
        break;
                  
    } // end-switch            
  } // end-sync
  
  /*
   * Inactivation or unregister will progress until System.exit().
   *  If any code in a shutdown exit hangs the system, then manual intervention
   *  will be necessary. 
   */
  
  // When in stage 1 and shut down from here
  if  ((T.getEndit() == TymeacInfo.SHUT_STAGE1) &&
       (!shutdown_requested))  {
  
      // When there is NO stage1 exit array of classes
      if  ((T.getShut1_array() == null) ||
           (T.getShut1_array().length < 1)) {
    
          // ignore
          ;
      }
      else {
          // do the exit routine
          T.getImpl().shutExit(T.getShut1_array());
    
      } // endif
      
      // finished exit 1 classes
      T.setShut1_done();
      
  } // endif
  
  /*
   * *--- Server is finished ---*  
   * 
   *  Only one thread can continue past here 
   */
  
  // When Not first thread to enter here
  if  (!T.setServerDone()) {
    
      // When not over time
      if  ((--shutdown_stage2_count) > 0) {
          
          // let normal shutdown continue
          return;      
      } 
      else {
          // this shut down may be stalled
          String N_msg = TyMsg.getMsg(3202);
      
          // print locally
          printMsg(N_msg);
          
          // write a log msg
          writeLog(N_msg);
        
          // wait some more
          return; 
        
      } // endif    
  } // endif

  // When there is NO stage2 exit array of classes
  if  ((T.getShut2_array() == null) ||
       (T.getShut2_array().length < 1)) {

      // ignore
      ;
  }
  else {
      // do the exit routine
      T.getImpl().shutExit(T.getShut2_array());

  } // endif
 
  // When using a stats table, write the stats
  if  (T.isStatsUsed()) T.getStats_tbl().writeActStats();
  
  // When shut request done elsewhere
  String msg = (shutdown_requested)?
              TyMsg.getMsg(3098) + T.getStart_time()
              :
              TyMsg.getMsg(3099) + T.getStart_time();      
  
  // log msg
  writeLog(msg);
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(msg);
 
  // try to inactivate / unregister myself
  try {
    // When shut request done elsewhere
    if  (shutdown_requested) {
      
        // try to unregister myself
        java.rmi.activation.Activatable.unregister(T.getTyActivation());
    }
    else {
        // inactivate
        java.rmi.activation.Activatable.inactive(T.getTyActivation());
        
    } // endif 
  } // end-try 

  catch (java.rmi.activation.UnknownObjectException e) {

    writeLog(TyMsg.getMsg(3100) + e.getMessage());

  } // end-catch

  catch (java.rmi.RemoteException e) {

    writeLog(TyMsg.getMsg(3105) + e.getMessage()); 

  } // end-catch 

  catch (ActivationException e) {

    writeLog(TyMsg.getMsg(3110) + e.getMessage());
 
  } // end-catch
  
  // shut down complete (only one thread can get here)
  T.setEndit(TymeacInfo.SHUT_COMPLETE);
  
  // When there is an embedded data base, try to shut it
  if  (T.getDBShut() != null) T.getImpl().shutEmbeddedDataBase();
   
  // all done charlie
  System.exit(0);   
  
} // end-method

/**
 * Sleep for an interval, then do some work.
 */   
public void run () {
  
  // start up message
  String msg;
  
  // When using the Activation system, inactivate
  if  (T.getInactivateMinutes() > 0) {

      // full message 
      msg = TyMsg.getMsg(3013)
            + T.getMonitor_interval() 
            + TyMsg.getText(86)
            + TyMsg.getText(87) 
            + T.getInactivateMinutes()
            + TyMsg.getText(88); 
  }
  else {    
      // no inactivation
      msg = TyMsg.getMsg(3013) 
            + T.getMonitor_interval() 
            + TyMsg.getText(86);
  
  } // endif
  
  // log start up message
  writeLog(msg);  
               
  // When printing, print
  if  (T.getSysout() == 1)  TyBase.printMsg(msg);
  
  msg = null;
  
  // spin until final shut down
  while  (T.getEndit() != TymeacInfo.SHUT_COMPLETE) {   
    
    try {       
      // wait this long
      sleep((long)T.getMonitor_interval() * 1000);
    
    } // end-try
  
    catch (InterruptedException e) {
    
      // interrupt() not supported in Tymeac, this should not happen    
      return;
      
    } // end-catch  
    
    // do the monitor function 
    doWork();
   
  } // end-while
             
} // end-run

/**
 * Print the message locally
 * @param msg to print
 */
private void printMsg (String msg)  {
  
  // When printing, print msg
  if  (T.getSysout() == 1)  TyBase.printMsg(msg);
  
} // end-method

/**
 * Send a message to the notification queue
 * @param msg java.lang.String
 */
private void sendNotify (String msg )  {
  
  // When using a notify, pass to the notify class
  if  (T.isNotifyUsed()) T.getNotify_tbl().sendMsg(msg);
  
} // end-method

/**
 * Write a message to the log
 * @param msg java.lang.String
 */
private void writeLog (String msg ) {
  
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, 20);  
  
} // end-method
} // end-class
