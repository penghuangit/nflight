package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2012 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.util.Locale;
import java.util.concurrent.atomic.*;

/**
 * This is the management storage for a Queue's thread. This class contains the 
 *   variables associated with an individual thread and the methods for manipulating 
 *   the individual thread.
 *   
 * Each Queue Thread keeps a reference to this object and updates its status therein. 
 *   Outsiders may look at this object to determine the status of the Queue Thread 
 *   at any given time.    
 * 
 * The Queue structure:
 *   level one   -- AreaBase
 *   level two   -- AreaThreadsAnchor & AreaWaitListsAnchor
 *   level three -- AreaThreadMgmt (this)  
 */
public final class AreaThreadMgmt {
  
// static fields
  private static final long max_time = Long.MAX_VALUE; //Max value for a Long
      
// instance fields  
  private final TyBase              T;         // Tymeac base storage
  private final AreaBase            area;      // pointer to top level Queue data
  private       AreaWaitListsAnchor waitlists; // pointer to Queue's wait lists anchor
  
  private final String  tname;    // name of this thread                               
  private final int     name;     // name of this entry (as an int)
  private final boolean type;     // normal-true, agent-false
  
  // reference to the actual thread
  private AtomicReference<AreaBasicThread> thread;
  
  private volatile long  time_active;  // time began
  private volatile long  time_start;   // +
  private volatile long  time_link;    // +
  private volatile long  time_post;    // +
  private volatile long  time_death;   // time to kill thread
    
  /*
   * The thread may be marked cancelled (status code below) by the Monitor 
   *   for one of these reasons.    
   * When (if) the thread resumes processing it resets its status to 'processing'
   *   and resets this cancelled reason to not cancelled.    
   * If a new thread replaces this thread, the new thread gets a not cancelled reason. 
   */  
  private volatile int can_reason;  // cancelled or not reason
  
      private static final int NOT_CANCELLED = 0;
      private static final int EXCESSIVE_PROCESSING = 1;
      private static final int EXCESSIVE_APPL_PROCESSING_DEFAULT = 2;
      private static final int EXCESSIVE_APPL_PROCESSING_SPECIFIC = 3;
      private static final int EXCESSIVE_SCHD_INTERVAL = 4;
      private static final int EXCESSIVE_POSTING_INTERVAL = 5;
      private static final int EXCESSIVE_STARTED_INTERVAL = 6;
  
  /*
   * Disabled reason. 
   *   Set by monitor when a timeout
   *   Set by thread when an Exception or Error
   *   Set by thread when shutting down. 
   */
  private volatile int dis_reason;  
  
  /*
   * Only set by the individual thread but read by other threads 
   */
  private volatile int totl_proc;     // total requests processed
  private volatile int totl_waits;    // total waits expired
  private volatile int totl_posted;   // total times posted
  private volatile int totl_started;  // total times started
  private volatile int totl_wrong_instance; // total times in wrong instance  
  private volatile int totl_caught;   // total times caught problem  
  
  /*
   * For each session of Tymeac there are a fixed, number of threads for each Queue.
   * Each possible thread has a management structure (this class).
   * 
   * There may be many actual threads associated with this structure over the Tymeac Session.
   *   A thread may wait for work and timeout with no new work (status set to inactive.) 
   *   In an inactive status it may timeout and be distroyed by the Monitor
   *   (null instancere reference.) When the Queue needs a new thread, Tymeac creates
   *   a new thread to re-use this structure.
   *     
   * There is also the timeout in a processing state. Tymeac times each thread during
   *   its life cycle. If a thread takes an excessive amount of time it may be marked
   *   cancelled and then disabled (as above.) The ENABLE ALL button on the Queue Thread
   *   GUI sets disabled threads to inactive and nulls the instancere reference to the
   *   thread. A new thread may or may not take its place re-using this structure.
   *   
   * The thread may be set "disabled" for a variety of other reasons (see the page, 
   *   "How threads get disabled" in the documentation. As above, the ENABLE ALL button
   *   nulls the instance reference.  
   *     
   * When (if) the expunged thread awakens it both checks its instance reference
   *   for null and checks this field to see if it matches its own instance number.
   *   If it doesn't, then the thread dies (exits the run() method.) 
   *   Unfortunetly, any request the thread had picked up hangs. The syncRequest()
   *   times out and is eventually purged. The asyncRequest() goes into the Stall Array. 
   */
  private AtomicInteger totl_new; // total times instantiated   
  
  /* 
   * Tymeac status of this thread (not to be confused with 'state' in java.lang.Thread)
   *   The status may be set() or atomically set (CAS.) When both an end-point connection 
   *   thread and a Queue Thread may access this field simultaneously then atomic is the choice.
   *     E.G. 
   *     When a Queue Thread invokes the Processing Application Class, only this Queue Thread
   *     may do so, therefore, set() is the method, not CAS.
   *     When an end-point connection thread 'posts' a waiting Queue Thread, then CAS is the method. 
   *    
   *  Initialized  Thread was never used. 
   *                 Set by startup.
   *                           
   *  Detatched    Timed out after waiting for work.
   *                 CAS from waiting.
   *                      
   *  Waiting      Waiting for work with a time limit.
   *                 CAS from processing.
   *  
   *  Processing   The Thread is executing code outside the application. 
   *                 E.G. fetching a request from a Wait List, 
   *                      preparing to invoke the application method or 
   *                      processing the output from the application.
   *                 CAS after return from In link to appl or sched,
   *                 Set other times.
   *                        
   *  In link
   *   to Appl     Invoking Processing Application Class.
   *                 Set by both Normal and OA Queue Thread.
   *  
   *  In link 
   *   to OA Schd  Calling the scheduler for the Output Agent.
   *                 Set by Normal Queue Thread, never OA Thread. 
   *  
   *  Posted       Awakened from waiting with timeout.              
   *                 CAS by Impl.[a]syncRequest() from waiting.
   *  
   *  Started      New or reactived thread.    
   *                 CAS from detached or initialized only.
   *  
   *  Cancelled    Timed out in a non-waiting status.
   *                 CAS by Monitor after a timeout.
   *  
   *  Disabled     Non functioning thread.
   *                 Both set and CAS. 
   */  
  private AtomicInteger status; 
   
      private static final int INITIALIZED = 0;
      private static final int DETACHED    = 1;
      private static final int WAITING     = 2;
      private static final int PROCESSING  = 3;
      private static final int INLINK_APPL = 4;
      private static final int INLINK_SCHD = 5;
      private static final int POSTED      = 6;
      private static final int STARTED     = 7;
      private static final int CANCELLED   = 8;
      private static final int DISABLED    = 9;  
      
/**
 * Constructor
 *
 * @param Ty Tybase - Tymeac base storage
 * @param Ah AreaHeader - pointer to the header base
 * @param c_name int - sequence number
 * @param th_name String - Thread name
 */ 
protected  AreaThreadMgmt(
              TyBase Ty, 
              AreaThreadsAnchor mother, 
              boolean c_type,
              int seq, 
              String baseName) {  
  
  T         = Ty;                 // Tymeac Base Storage
  area      = mother.getArea();   // level 1
  
  name  = seq;      // sequence number
  tname = baseName; // thread name 

  time_active = max_time; // time began processing
  time_start  = max_time; // time new thread started
  time_link   = max_time; // time in link to pap or sched 
  time_post   = max_time; // time thread posted
  time_death  = max_time; // time to kill a thread

  can_reason  = NOT_CANCELLED; // no cancelled reason 
  dis_reason  = 0; // no disabled reason 
  
  status = new AtomicInteger(INITIALIZED); // initialized, never used

  totl_proc    = 0;  // total requests processed
  totl_waits   = 0;  // total waits expired
  totl_posted  = 0;  // total times posted
  totl_started = 0;  // total times started
  totl_new     = new AtomicInteger(0);  // total times instantiated  
  
  totl_wrong_instance = 0; // total in wrong instance
  totl_caught         = 0; // total caught a problem

  // initially null
  thread = new AtomicReference<AreaBasicThread>();
  
  // normal/oa queue thread
  type = c_type;
  
} // end-constructor

/**
 * Increment number of times caught problem
 */ 
protected void addCaught ( ) { totl_caught++; } // end-method

/**
 * Increment number of thread instantiations AND return that number
 * @return int  
 */ 
protected int addNew ( ) { return totl_new.incrementAndGet(); } // end-method

/**
 * Increment number of times posted 
 */ 
private void addPosted ( ) { totl_posted++; } // end-method

/**
 * Increment number of requests processed 
 */ 
private void addProcessed ( ) { totl_proc++; } // end-method

/**
 * Increment number of threads started 
 */ 
private void addStarted ( ) { totl_started++; } // end-method

/**
 * Increment number of times a thread waited for work 
 */ 
private void addWaits ( ) { totl_waits++; } // end-method

/**
 * Increment number of times in wrong instance 
 */ 
protected void addWrongInstance ( ) { totl_wrong_instance++; } // end-method

/**
 * How long to wait in a DETACHED state before killing thread
 * @return long
 */
protected long calcDeathTime () {        
  
  // max time before kill
  int max = area.getKillTime(); 

  // When used, calc death time or not used
  return (max > 0)? (System.currentTimeMillis() + (max * 1000)) : max_time ;
    
} // end-method

/**
 * Set cancelled status for threads with excessive time in a status.
 * The status of threads changes dynamically. Each thread changes its
 * status and sets the time of the new status to the current time. Therefore,
 * only when a thread is in a status for an excessivly long time will it be
 * flagged here.   
 *
 *@param time long - Time now, from monitor 
 */
protected void cancelExcessiveTime (long time) {

  int diff; 
  
   // Now, for which the interest lies --
  switch (getStatus()) {
                                                
    case  PROCESSING:  
      
      // time started processing from now, (time in milliseconds) 
      diff = (int)((time - getTimeActive()) / 1000);
  
      // When over max time, set to cancelled
      if  (diff > T.getProcessing_timeout()) setCancelled(PROCESSING, EXCESSIVE_PROCESSING); 

      // get out                          
      break;                    
                                              
    case  INLINK_APPL: 
      
      // time started in link to appl. class from now, (time in milliseconds) 
      diff = (int)((time - getTimeLink()) / 1000);

      // When using the default value
      if  (area.getPaTimeout() == 0) {
          
          // When over default time, set to cancelled
          if  (diff > T.getPac_timeout()) setCancelled(INLINK_APPL, EXCESSIVE_APPL_PROCESSING_DEFAULT);  
      }
      else {
          // When over max specific seconds, set to cancelled
          if  (diff > area.getPaTimeout()) setCancelled(INLINK_APPL, EXCESSIVE_APPL_PROCESSING_SPECIFIC);              

      } // endif              
        
      // get out                          
      break;              
        
    case  INLINK_SCHD:
      
      // time started in link to Schduler from now
      diff = (int)((time - getTimeLink()) / 1000);
          
      // When over a monitor interval, set to cancelled
      if  (diff > T.getScheduling_timeout()) setCancelled(INLINK_SCHD, EXCESSIVE_SCHD_INTERVAL);
        
      // get out                          
      break;              
        
    case  POSTED:
      
      // time of posting from now, (time in milliseconds)
      diff = (int)((time - getTimePost()) / 1000);
  
      // When over a monitor interval, (times 2), set to cancelled
      if  (diff > T.getPosted_timeout()) setCancelled(POSTED, EXCESSIVE_POSTING_INTERVAL);
                 
      // get out                          
      break;                  
                      
    case  STARTED: 
      
      // time of start from now, (time in milliseconds)
      diff = (int)((time - getTimeStart()) / 1000);
  
      // When over a monitor interval, (times 4), set to cancelled
      if  (diff > T.getStarted_timeout()) setCancelled(STARTED, EXCESSIVE_STARTED_INTERVAL); 
  
      // get out                          
      break;

    case  DETACHED:
    case  INITIALIZED:

      // When using a time of death (get rid of thread)
      if  (area.getKillTime() > 0) {

          // When over max life span, kill the que thread
          if  (time > getTimeKill()) killThread();  

      } // endif                      
  
      // get out                          
      break;  
        
  } // end-switch  
    
} // end-method

/**
 * Create either a new normal or Output Agent thread.
 * 
 * Once the CAS succeeds then we only have one new thread reference. Any other
 *   thread object created when attempting the CAS should be garbage collected.
 *   
 * A CAS failure simply means another thread got here first. There was a new
 *   thread created previously so failure here is insignificant.
 *   
 * Could there be a long delay and the reference get nulled? Seems unlikely, but
 * we put this code in a try block just in case.
 * @return boolean new thread or not
 */
private boolean createThread() {
    
  try {  
    // When Normal Thread
    if  (isNormalQueue()) {
          
        // When new normal fails, get out
        if  (!thread.compareAndSet(null, new AreaQueThread(T, area, this, tname))) return false;    
    }
    else {
        // When new OA fails, get out
        if  (!thread.compareAndSet(null, new AreaAgentThread(T, area, this, tname))) return false;
        
    } // endif
    
    // newly created thread
    AreaBasicThread th = getThread();
    
    // set the new instance number
    th.setInstance(addNew());    
    
    // not a daemon
    th.setDaemon(false);
    
    // start it
    th.start();
    
    // success
    return true;
    
  } // end-try
  
  catch (Exception e) {
    
    // not created
    return false;
    
  } // end-catch
  
} // end-method

/**
 * Set status of Disabled when CAS works
 * @param reason
 * @return boolean disabled or not
 */
protected boolean disabledCancelled (int reason) {

  // When set disabled fails, get out
  if  (!setStatus(CANCELLED, DISABLED)) return false;
  
  // other details
  setDisabledDetails(reason); 
  
  // was disabled
  return true;
    
} // end-method

/**
 * Tell the queue thread to stop by user request. The status is set here
 *   in case the thread does not wake up. 
 */ 
protected void disableThread () {
  
  // que thread
  AreaBasicThread qt = getThread();
  
  // When is alive
  if  (qt != null) {

      try{
        // tell the thread to die
        qt.stopThread();

      } // end-try

      catch (Exception e) {
      } // end-catch    
  } // endif
  
  // say disabled by user
  setDisabled(9001);
    
} // end-method

/**
* Form the reason for the thread cancel status 
* @param reason int
* @return String
*/
protected String formCancelMsg (int reason) {
  
  // cancelled reason
  switch (reason) {
  
    case EXCESSIVE_PROCESSING:
    
        return TyMsg.getText(163);
        
    case EXCESSIVE_APPL_PROCESSING_DEFAULT:
      
        return TyMsg.getText(164);
      
    case EXCESSIVE_APPL_PROCESSING_SPECIFIC:
      
        return TyMsg.getText(165);
      
    case EXCESSIVE_SCHD_INTERVAL:
      
        return TyMsg.getText(166);
      
    case EXCESSIVE_POSTING_INTERVAL:
      
        return TyMsg.getText(167);
      
    case EXCESSIVE_STARTED_INTERVAL:
      
        return TyMsg.getText(168);
      
    default:
      
        return TyMsg.getText(169);  
  
  } // end-switch  
} // end-method

/**
 * Accessor for the area base
 * @return AreaBase to which this struct belongs
 */  
protected AreaBase getArea ( ) { return area; } // end-method

/**
 * Accessor for the agent thread instance
 * @return int reason for cancel
 */  
protected int getCanReason ( ) { return can_reason; } // end-method

/**
 * Accesor for the disabled reason
 * @return int
 */        
protected int getDisReason() { return dis_reason; } // end-method

/**
 * Accesor for the times instantiated
 * @return int
 */        
protected int getInstance() { return totl_new.get(); } // end-method

/**
 * Accessor for the thread name(as an integer)
 * @return int
 */        
protected int getName() { return name; } // end-method

/**
 * Accessor for the thread status
 * @return int*
 */      
protected int getStatus() { return status.get(); } // end-method

/**
 * Accessor for the thread status
 * @return String
 */      
protected String getStatusDetails() {
  
  // get the status of the thread 
  switch (getStatus()) {
    
    case INITIALIZED :
              
      // When alive
      if  (isAlive()) {          

          // say in text, done
          return ",  ("
                     + String.format(Locale.getDefault(), "%,d", getTotProc())  
                     + TyMsg.getText(148);
      } // endif
      
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(149);    
      
              
    case DETACHED :

      // When alive
      if  (isAlive()) {  
        
          // say in text, done
          return ",  ("
                     + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                     + TyMsg.getText(150);
      } // endif
      
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(151);    
        
        
    case WAITING :
            
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(152);
        
    case PROCESSING :  
            
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(153);
    
    case INLINK_APPL : 
            
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(154);
        
    case INLINK_SCHD : 
            
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(155);
        
    case POSTED :      
            
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(156);
        
    case STARTED :     
            
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(157);
    
    case CANCELLED :               
            
      // say in text, done
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(158)
                 + formCancelMsg(getCanReason());
    
    case DISABLED :      

      // reason may be zero
      int reason = getDisReason();

      // When never cancelled
      if  (getCanReason() == 0) {
        
          // When zero
          if  (reason == 0) {

              // make sure all zeros print
              return ",  ("
                         + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                         + TyMsg.getText(159);
          } // endif
                     
          // use whatever non-zero reason
          return ",  ("
                     + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                     + TyMsg.getText(160)
                     + reason;          
      } // endif
      
      // When zero
      if  (reason == 0) {

          // make sure all zeros print
          return ",  ("
                     + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                     + TyMsg.getText(159)
                     + TyMsg.getText(170)
                     + formCancelMsg(getCanReason());
      } // endif
                   
      // use whatever non-zero reason
      return ",  ("
                 + String.format(Locale.getDefault(), "%,d", getTotProc()) 
                 + TyMsg.getText(160)
                 + reason
                 + TyMsg.getText(170)
                 + formCancelMsg(getCanReason());               
                                        
  } // end-switch 
  
  // can't get here
  return "AreaDetail.getStatusDetails() is broken";
                     
} // end-method

/**
 * Accessor for the thread instance
 * @return QueThread
 */ 
protected AreaBasicThread getThread ( ) { return thread.get(); } // end-method

/**
 * Accessor for the time in an active state
 * @return long
 */ 
private long getTimeActive () { return time_active; } // end-method

/**
 * Accessor for the time to end the thread
 * @return long
 */ 
private long getTimeKill () { return time_death; } // end-method

/**
 * Accessor for the time in a staus of Link
 * @return long
 */ 
private long getTimeLink () { return time_link; } // end-method

/**
 * Accessor for the time in a status of Posted
 * @return long
 */ 
private long getTimePost () { return time_post; } // end-method

/**
 * Accessor for the time in a status of Started
 * @return long
 */ 
private long getTimeStart () { return time_start; } // end-method

/**
 * Accessor for total caught
 * @return int
 */        
protected int getTotCaught() { return totl_caught; } // end-method

/**
 * Accessor for total new threads
 * @return int
 */        
protected int getTotNew() { return totl_new.get(); } // end-method

/**
 * Accessor for total times posted
 * @return int
 */ 
protected int getTotPosted ( ) { return totl_posted; } // end-method

/**
 * Accessor for total requests processed
 * @return int
 */
protected int getTotProc ( ) { return totl_proc; } // end-method

/**
 * Accessor for total times started
 * @return int
 */ 
protected int getTotStarted ( ) { return totl_started; } // end-method

/**
 *  Accessor for total waits issued 
 * @return int
 */
protected int getTotWaits ( ) { return totl_waits; } // end-method

/**
 * Accessor for total wrong instance
 * @return int
 */        
protected int getTotWrongInstance() { return totl_wrong_instance; } // end-method

/**
 * Is a thread instance valid? 
 * @return boolean
 */ 
protected boolean isAlive () {
  
  // When No instance is alive, say not alive
  return (getThread() == null)? false : true;
    
} // end-method

/**
 * Is the thread available for a start? Initialized only happens once.
 * @return boolean
 */ 
protected boolean isAvailable () {
  
  int status = getStatus();
  
  // When anyone of these, is avail
  return (status == DETACHED || status == INITIALIZED)?
         true : false;
    
} // end-method

/**
 * Is the thread busy doing something?
 * @return boolean
 */ 
protected boolean isBusy () {
  
  int status = getStatus();
  
  // When anyone of these, is busy
  return ((status == PROCESSING)  ||
          (status == INLINK_APPL) ||
          (status == INLINK_SCHD) ||
          (status == POSTED)      ||
          (status == STARTED))?
         true : false;
    
} // end-method

/**
 * Is cancelled?
 * @return boolean
 */ 
protected boolean isCancelled () {
  
  // When cancelled
  return (getStatus() == CANCELLED)? true : false;
    
} // end-method

/**
 * Was this thread replaced by another thread or is the instance null
 *   (meaning it timed out)?
 * @parm int instance number
 * @return boolean
 */ 
protected boolean isDead (int int_nbr) {
  
  // When instance numbers dont match
  if  (int_nbr != getTotNew()) {
    
      // increment times wrong instance
      addWrongInstance();

      // thread dead
      return true;
  
  }  // endif
  
  // When the thread instance itself is null,
  return (!isAlive())? true : false;
            
} // end-method

/**
 * Is detached?
 * @return boolean
 */ 
protected boolean isDetached () {
  
  // When detached
  return (getStatus() == DETACHED)? true : false;
    
} // end-method

/**
 * Is disabled?
 * @return boolean
 */ 
protected boolean isDisabled () {
  
  // When disabled
  return (getStatus() == DISABLED)? true : false;
            
} // end-method

/**
 * Is initializied?
 * @return boolean
 */ 
protected boolean isInitialized () {
  
  // When init
  return (getStatus() == INITIALIZED)? true : false;
    
} // end-method

/**
 * Is in a wait state?
 * @return boolean
 */ 
protected boolean isNapping () {
  
  int status = getStatus();
  
  // When init, detached or waiting, is napping
  return ((status == INITIALIZED)  ||
          (status == DETACHED)     ||
          (status == WAITING))? 
          true : false;
    
} // end-method

/**
 * Is a normal queue?
 * @return boolean
 */ 
protected boolean isNormalQueue () { return type; } // end-method

/**
 * Is waiting?
 * @return boolean
 */ 
protected boolean isWaiting () {
  
  // When waiting
  return (getStatus() == WAITING)? true : false;
    
} // end-method

/**
 * Tell a queue thread to stop, from monitor 
 */ 
private void killThread () {
  
  // que thread
  AreaBasicThread th = getThread();
  
  // When is alive
  if  (th != null) {

      try{
        // tell the thread to die
        th.stopThread();
        
        // null it 
        nullThread();

      } // end-try

      catch (Exception e) {
      } // end-catch
          
  } // endif
  
  // no kill time
  time_death = max_time;
    
} // end-method

/**
 * set the instance to null  
 */ 
private void nullThread () { thread.set(null); } // end-method

/**
 * Set status of Cancelled to never cancelled
 * @param reason int 
 */ 
private void resetCancelled () { can_reason = NOT_CANCELLED; } // end-method

/**
 * Set thread status to Processing after being in the application code. 
 * A check is necessary since the thread may have timed out 
 *  and the thread was destroyed (instance null) 
 *  and now a new instance exists (instance numbers don't match.)
 * @return boolean successful or not 
 */
protected boolean setBackProcessing (int int_nbr) {

  // When timed out, done  here
  if  (isDead(int_nbr)) return false;

  // do base function
  setProcessing();
  
  // accum number processed
  addProcessed();

  // good
  return true;
    
} // end-method

/**
 * Set status of Cancelled
 * @param from this status to cancelled status
 * @param reason being cancelled 
 */ 
private void setCancelled (int from, int reason) {

  // When status is set to cancelled, set reason 
  if  (setStatus(from, CANCELLED)) can_reason = reason;
    
} // end-method

/**
 * Detached status details
 */
private void setDetachedDetails () {  
        
  // invalidate the time
  time_post   = max_time;
  time_start  = max_time;
  time_active = max_time;
  time_link   = max_time;

  // no disabled reason
  setDisReason(0); 

  // calc death time
  time_death = calcDeathTime();
    
} // end-method

/**
 * Set status of Detached without checking prior status
 */
private void setDetachedForced () {
  
  // change status
  setStatus(0, DETACHED);
      
  // other detail
  setDetachedDetails();
    
} // end-method

/**
 * Exception in the thread means we cannot continue
 * @param reason
 */
protected void setDead(int reason) {
  
  // say is disabled
  setDisabled(reason);
  
  // thread does not exist
  nullThread();
  
} // end-method

/**
 * Force status of Disabled
 */ 
protected void setDisabled (int reason) {

  // force disabled
  setStatus(0, DISABLED);
  
  // the other details
  setDisabledDetails(reason);     
    
} // end-method

/**
 * Set Disabled details.
 * @param reason
 */
private void setDisabledDetails (int reason) {

  // When not zero, say why
  if  (reason > 0) setDisReason(reason);
  
  // invalidate the times
  time_start  = max_time;
  time_link   = max_time;
  time_active = max_time;
  time_post   = max_time;  
  time_death  = max_time;     
    
} // end-method

/**
 * Set the status 'disabled' reason
 * @param int reason 
 */ 
private void setDisReason (int reason) { dis_reason = reason; } // end-method

/**
 * Set 'in link to application' status when still the same instance
 * @param reason int
 * @return boolean same instance or not
 */ 
protected boolean setLinkAppl (int instance) {
  
  // When timed out, done  here
  if  (isDead(instance)) return false;

  //invalidate the time
  time_post   = max_time;
  time_start  = max_time;
  time_active = max_time;

  // set the time of this  
  time_link = System.currentTimeMillis();

  // status is in link to appl
  setStatus(0, INLINK_APPL); 
  
  // ok
  return true;
    
} // end-method

/**
 * Set 'In Link to Scheduler' status
 * @param reason int
 * @return boolean same instance or not
 */ 
protected boolean setLinkSchd (int instance) {
  
  // When timed out, done  here
  if  (isDead(instance)) return false;

  // invalidate the time
  time_post   = max_time;
  time_start  = max_time;
  time_active = max_time;

  // set the time of this  
  time_link = System.currentTimeMillis();

  // status is in link to scheduling
  setStatus(0, INLINK_SCHD); 
  
  // good
  return true;
    
} // end-method

/**
 * Set a new queue thread. 
 * @return boolean
 */ 
protected boolean setNewThread () {
   
  // When invalid status change, get out
  if  (!setStarted()) return false;  
  
  // When doesn't exist
  if  (getThread() == null) { 
    
      // When Not created, get out
      if  (!createThread()) return false;
      
  } // endif
  
  // no time to kill
  time_death = max_time;
      
  // no disabled and cancelled reasons
  setDisReason(0);
  resetCancelled();  

  // wake up the thread
  getThread().wakeUp();    
  
  // good
  return true;
  
} // end-method

/**
 * Set actively processing when waiting. 
 */
protected void setNotWaiting () {    
  
  // When status is "waiting for work" set to processing
  if  (setStatus(WAITING, PROCESSING)) { 

      // set time of active
      time_active = System.currentTimeMillis();
    
      // invalidate the other times
      time_start  = max_time;
      time_link   = max_time;
      time_post   = max_time;

  } // endif
    
} // end-method

/**
 * Post the thread 
 * @return boolean successful or not
 */ 
protected boolean setPosted () {
  
  // When successful
  if  (setStatus(WAITING, POSTED)) {
      
      // set the time of this posting
      time_post = System.currentTimeMillis();
  
      // invalidate the time
      time_start  = max_time;
      time_active = max_time;
      time_link   = max_time;
      
      // add to counter
      addPosted();
              
      // wake up the thread
      getThread().wakeUp();
      
      // good
      return true;
      
  } // endif
  
  // nogood
  return false;
    
} // end-method

/**
 * Set actively processing 
 */
protected void setProcessing () {    

  // set status
  setStatus(0, PROCESSING); 
  
  // detail fields
  setProcessingDetails();
    
} // end-method

/**
 * Set actively processing detail fields
 */
private void setProcessingDetails () { 
  
  // set time of active
  time_active = System.currentTimeMillis();

  // invalidate the other times
  time_start = max_time;
  time_link  = max_time;
  time_post  = max_time;
  
  // not canceled
  resetCancelled();
  
} // end-method

/**
 * Set actively processing when prior condition is true
 * Why check processing? This can happen when a setWaiting
 * failed because a connection thread put a request in a waitlist in 
 * that tiny interval between when there were no requests pending and
 * before this thread could set its status to waiting. 
 */
protected boolean setReProcessing () {  
  
  // When already processing or status change works
  if  ((getStatus() == PROCESSING) ||
       (setStatus(POSTED, PROCESSING)) ||
       (setStatus(STARTED, PROCESSING))) {
    
      // detail fields
      setProcessingDetails();
      
      // did it
      return true;
    
  } // endif
  
  // no good
  return false;
    
} // end-method

/**
 * Tell the thread to stop 
 */ 
private void setRestore () {
  
  // que thread
  AreaBasicThread qt = getThread();

  // When a thread is present
  if  (qt != null) {
  
      try {
        // try to kill the sucker
        qt.stopThread();

      } // end-try 

      catch (Exception e) {
      } // end-catch

      // null the instance
      nullThread(); 

  } // endif 

  // set to available for a start 
  setDetachedForced();
   
} // end-method

/**
 * Set status of Started 
 */
private boolean setStarted () {
  
  // When currently detached, or initialized
  if  ((setStatus(DETACHED, STARTED)) ||
       (setStatus(INITIALIZED, STARTED))) {
    
      // good
  } 
  else {        
      // No good   
      return false;
      
  } // endif
  
  // set the time of this posting 
  time_start = System.currentTimeMillis();
  
  // invalidate the time
  time_post   = max_time;
  time_active = max_time;
  time_link   = max_time;
  
  // add to counter
  addStarted(); 
  
  // good
  return true;
    
} // end-method

/**
 * Set the atomic status indicator 
 * @param oldStatus int
 * @param newStatus int  
 */
private boolean setStatus (int oldStatus, int newStatus) {
  
  // When not passing in the old
  if  (oldStatus == 0) {
            
      // set the new status
      status.set(newStatus);
      
      // good
      return true;      
        
  } // endif
  
  // return what happened. 
  return (status.compareAndSet(oldStatus, newStatus));  
  
} // end-method

/**
 * Tell the thread to die, forced shut down   
 */ 
protected void setTerminate () {
  
  // que thread
  AreaBasicThread qt = getThread();

  // When dead, wake up the thread
  if  (qt != null) qt.wakeUp();

  // reason is terminate
  setDisReason(2002);
    
} // end-method

/**
 * Wait time exceeded, say is detached, maybe
 */
protected void setTimedOut () {
  
  // When we could change status
  if  (setStatus(WAITING, DETACHED)) {
    
      // And there is still No waiting work
      if  (waitlists.isWaitlistEmpty()) {     
      
          // other detail
          setDetachedDetails();
          
          // number of time outs  
          addWaits();
      }
      else {
          // set the status back. a new request squeaked in.
          //  if this fails, it means the status is now 'posted' (irrelevant here) 
          setStatus(DETACHED, PROCESSING);
          
      } // endif         
  } // endif  

} // end-method

/**
 * Set status of waiting for work, when in processing state. If a connection
 * thread puts a request into a waitlist before the atomic C&S it sets 
 * work pending. So, if we change the status and there is really work pending
 * then reset the status. 
 */
protected void setWaiting () {  
  
  // When status changed to "waiting for work" successful
  if  (setStatus(PROCESSING, WAITING)) { 
      
      // And there is still No waiting work
      if  (waitlists.isWaitlistEmpty()) {  
        
          // invalidate the time
          time_post   = max_time;
          time_start  = max_time;
          time_active = max_time;
          time_link   = max_time;
      }
      else {
          // set the status back. a new request squeaked in.
          //  if this fails, it means the status is now 'posted' (irrelevant here) 
          setStatus(WAITING, PROCESSING);
          
      } // endif
  } // endif  
    
} // end-method

/**
 * Set the waitlist anchor here 
 * @param waitLists
 */
protected void setWaitLists(AreaWaitListsAnchor waitLists) { waitlists = waitLists; } // end-method

/**
 * Try to enable a disabled thread 
 */ 
protected boolean tryEnable ( ) {    
    
  // When disabled  
  if  (isDisabled()) {
    
      // restore que thread
      setRestore();
      
      // was enabled
      return true;
      
  } // endif
  
  // was not enabled
  return false;
    
} // end-method

/**
 * Wake up a thread if it is waiting 
 */
protected void wakeUpThread () {    

  // wake up
  try {
    getThread().wakeUp();
  
  } catch (Exception e) {}
    
} // end-method     
} // end-class
