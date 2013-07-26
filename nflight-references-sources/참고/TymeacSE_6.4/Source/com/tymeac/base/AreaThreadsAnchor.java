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

import java.util.ArrayList;

/**
 * This is the anchor point for the Queue's thread Classes. The anchor contains the 
 *   methods for iterating over the thread management objects in the pool. 
 *   The pool is simply an ArrayList of AreaThreadMgmt objects. 
 * 
 * The Queue structure:
 *   level one   -- AreaBase
 *   level two   -- this 
 *   level three -- AreaThreadMgmt (the individual thread)  
 */
public final class AreaThreadsAnchor {
  
  private final TyBase  T;     // pointer to base storage
  private final AreaBase area; // pointer to Queue Base this waitlist belongs to
  private       AreaWaitListsAnchor waitlists; // pointer to Queue's wait list anchor
  
  private final boolean type;      // is a normal queue-false, agent queue-true;
  private final int nbr_threads;   // total threads for this queue 
  private final int nbr_threads_1; // total threads for this queue minus one
  
  private final ArrayList<AreaThreadMgmt> details; // the threads

/**
 * Constructor
 * @param Ty TyBase - tymeac base 
 * @param me AreaHeader to which this belongs
 * @param type int Queue type (normal/Output Agent)
 * @param name String base name of the threads
 * @param c_nbr_threads - Total number of threads 
 * @param entries int - total entries in a waitlist
 */
protected AreaThreadsAnchor (TyBase Ty, 
                         AreaBase mother,
                         boolean type,
                         String name,
                         int c_nbr_threads) {

  T          = Ty; // tymeac base
  area       = mother; // Queue area base
  waitlists  = mother.getWaitlist(); // Queue's wait list pointer
  
  // queue thread or agent thread
  this.type = type;
     
  nbr_threads   = c_nbr_threads;   // total number of threads
  nbr_threads_1 = nbr_threads - 1; // total threads for this queue minus one
  
  // the detail array
  details = new ArrayList<AreaThreadMgmt>(nbr_threads);
      
  // initialize the detail entries
  for (int i = 0; i < nbr_threads; i++) {
      
    //  base pointer, this object, seq number, name of thread
    details.add(new AreaThreadMgmt(T, this, type, i, (name + i)));
   
  } // end-for 
  
} // end-constructor

/**
 * accessor for AreaHeader
 * @return AreaHeader
 */
protected AreaBase getArea() { return area; } // end-method

/**
 * Set cancelled status for threads with excessive time in a status.
 *
 *@param time long - Time now, from monitor 
 */
protected void cancelExcessive (long time) {
      
  // loop thru all the threads   
  for (AreaThreadMgmt him : details) {       
    
    // check for excess time
    him.cancelExcessiveTime(time);
            
  } // end-for
    
} // end-method

/**
 * check for non-disabled threads
 *@return boolean
 */ 
protected boolean checkThreads () {
         
  // loop thru the details   
  for (AreaThreadMgmt him : details) {    
         
    // When a thread is not disabled or about to be, found a live one
    if  ((!him.isDisabled()) &&
         (!him.isCancelled()))  return true;
    
  } // end-for  

  // no threads are active    
  return false;
    
} // end-method

/**
 * Set 'disabled' for any 'cancelled' thread
 * @return int
 */ 
protected int disableCancelled () {
    
  int count = 0;
  
  // the status of a detail entry must be cancelled 
  for (AreaThreadMgmt him : details) {   
    
    // When the entry is cancelled
    if  (him.isCancelled()) {

        // When disabled with reason successful
        if  (him.disabledCancelled(3001)) {    
        
            //  form error msg
            String error = TyMsg.getMsg(3019)
                          + area.getName()
                          + TyMsg.getText(160) + "3001"
                          + TyMsg.getText(170)
                          + him.formCancelMsg(him.getCanReason());
    
            // send a notify
            sendNotify(error);
    
            // log 
            writeLog(error);
            
        } // endif
    } // endif          
  } // endfor
  
  // count the entries disabled,  
  for (AreaThreadMgmt him : details) { 

    // When the entry is disabled, accum
    if  (him.isDisabled()) count++;
    
  } // end-for
  
  // When all are not disabled, all done
  if  (count != nbr_threads) return 0;
  
  // free any busy in the waitlists
  waitlists.purgeBusy(3010);

  // all are disabled   
  return 1;
    
} // end-method

/**
 * Que Thread display, disable button
 * @param nbr int - thread number to disable
 * @return int
 */ 
protected int disableThread (int nbr) {
        
  // When Not area detail number, error    
  if  ((nbr < 0) ||
       (nbr > nbr_threads_1)) return 1;  

  // tell to stop   
  details.get(nbr).disableThread();

  // form error msg
  String error = TyMsg.getMsg(9001)
                + nbr 
                + TyMsg.getText(89)
                + area.getName()
                + TyMsg.getText(97);

  // send a notify
  sendNotify(error);

  // log 
  writeLog(error);
            
  // return ok 
  return 4;
    
} // end-method

/**
 * Diagnose - display threads
 */ 
protected void dsplyThreads () {
  
  TyBase.printMsg(
      area.getName() + " Total Threads=" 
               + nbr_threads
               + " Que type="
               + area.getQueType()
               + " Tot average="
               + area.getTotAverage()
               + " Tot individual="
               + area.getTotIndivid()
               + " Tot overall="
               + area.getTotOverall()
               + " Tot overflowed="
               + area.getTotOverflow()
               + " Tot NoneBusy="
               + area.getTotNoBusy());
   
  // thread number
  int i = 0; 
  
  // loop thru the details  
  for (AreaThreadMgmt him : details) { 
         
     TyBase.printMsg(
         "    Thread " + i 
                       + " Status=" 
                       + him.getStatus()
                       + " Cancelled Reason="
                       + him.getCanReason()
                       + " Disabled Reason="
                       + him.getDisReason()
                       + " Total waits= "
                       + him.getTotWaits()
                       + " Total posts= "
                       + him.getTotPosted()
                       + " Total started= "
                       + him.getTotStarted()
                       + " Total new= "
                       + him.getTotNew()                                         
                       + " Total Proc= "
                       + him.getTotProc()
                       + " Total Caught= "
                       + him.getTotCaught()
                       + " Total Wrong Inst= "
                       + him.getTotWrongInstance());
     // next i
     i++;
            
  } // end-for 
    
} // end-method

/**
 * Thread display (TyQueThd) - enable all threads
 */ 
protected void enableThreads () {
    
  boolean isBusy  = false;
     
  // loop thru the details  
  for (AreaThreadMgmt him : details) { 

    // When Not enabled, see if busy
    if  (!him.tryEnable()) isBusy = him.isBusy();
    
  } // end-for 

  // When one is busy, nothing further here
  if  (isBusy) return;
          
  // When any waiting request, wake up a thread           
  if  (waitlists.getWLBusy() > 0) {  
    
      // loop thru the details  
      for (AreaThreadMgmt him : details) { 
        
        // When an available to start
        if  (him.isAvailable()) { 
          
            // new thread
            him.setNewThread();
            
            // all done, only need one
            return;
            
        } // endif
      } // end-for
  } // end-if
            
} // end-method

/**
 * Que Thread display, enable selected button
 * @param nbr int - thread number to enable
 * @return int
 */ 
protected int enableSpecificThread (int nbr) {
        
  // When Not area detail number, error    
  if  ((nbr < 0) ||
       (nbr > nbr_threads_1)) return 1;  

  // try to enable
  details.get(nbr).tryEnable();
            
  // return ok 
  return 4;
    
} // end-method

/**
 * TyQueThd - display the thread info
 * @return String[]
 */
protected String[] fetchThreads () {
    
  String[] th_data = new String[nbr_threads]; 
  int i = 0;
  
  // loop thru the details  
  for (AreaThreadMgmt him : details) {
          
    // get the string detail status
    th_data[i] = i + him.getStatusDetails();
    
    // next i
    i++;
    
  } // end-for  
          
  // give back the list of each thread's status
  return th_data;
    
} // end-method

/**
 * Find the first available detail that can accept a new thread
 * @return boolean
 */ 
protected boolean getAvailable ( ) {     
  
  // look at all the threads
  for (AreaThreadMgmt him : details) {

    // When a thread can be started, return found 
    if  ((him.isInitialized()) ||
         (him.isDetached()))  return true;
  
  } // end-for

  // none available   
  return false;
  
} // end-method

/**
 * Find a thread that is busy with a request
 * @return boolean
 */
protected boolean getBusy () { 
    
  // find a busy thread
  for (AreaThreadMgmt him : details) {
    
    // When a thread is busy, found one
    if  (him.isBusy()) return true;
       
  } // end-for
  
  return false;
    
} // end-method

/**
 * Count threads that are busy with a request
 * @return int
 */
protected int getBusyCount () { 
    
  // busy count
  int count = 0;
  
  // find a busy thread
  for (AreaThreadMgmt him : details) {

    // When a thread is busy, incr count
    if  (him.isBusy())  count++;
       
  } // end-for
  
  // what we found
  return count;
    
} // end-method

/**
 * Accessor for number of threads
 * @return int
 */ 
protected int getNbrThreads ( ) { return nbr_threads; } // end-method

/**
 * return an AreaThreads object
 * @return AreaThreads
 * @param nbr int - detail number
 */
protected AreaThreadMgmt getNextEntry (int nbr ) {
  
  // When nbr is invalid, error
  if  ((nbr < 0) || 
       (nbr > nbr_threads_1)) return null;

  // return this detail area      
  return details.get(nbr);
    
} // end-method

/**
 * Find an available thread for the new request
 * @return int
 */
protected int getThAvail ( ) {
    
  // the status of a detail entry must be other than cancelled or disable 
  for (AreaThreadMgmt him : details) {
    
    // When cancelled or disabled, ignore
    if  ((him.isCancelled()) ||
         (him.isDisabled())) {
      
        // ignore
        ;
    }
    else {
        // found one
        return 0;

      } // endif          
  } // end-for
 
  // none available
  return 1;
    
} // end-method

/**
 * Is this a normal queue or an agent queue?
 * @return boolean normal queue or agent queue
 */
private boolean isNormalQueue () { return type; } // end-method

/**
 * Find a busy thread. Cancelled is considered busy since it is still working
 * @return boolean
 */
protected boolean isWorking() {
        
  // loop thru the details looking for any busy status
  for (AreaThreadMgmt him : details) { 
         
    // When busy or cancelled, found one
    if  ((him.isBusy()) ||
         (him.isCancelled())) return true;
               
  } // endfor   
 
  // not busy
  return false;
    
} // end-method

/**
 * Set a new User Class for thread to invoke in each thread in the pool
 * @param userClass
 */
protected void setNewMethod(java.lang.reflect.Method userClass) { 
     
  // do each thread in the list
  for (AreaThreadMgmt him : details) {
    
    // basic thread 
    AreaBasicThread th = him.getThread();
    
    // When the thread is alive
    if  (th != null) {
      
        try {
          // set the new method reference
          th.setNewMethod(userClass);
          
        }// end-try
        
        catch (NullPointerException e) {} // ignored, could go null at anytime
        
    } // endif    
  } // end-for
  
} // end-method

/**
 * set new physical thread
 * @return boolean
 */
protected boolean setNewThread () {

  // find an available thread
  for (AreaThreadMgmt him : details) {  
    
    // When available for start
    if  (him.isAvailable()) {
        
        // When successful
        if  (him.setNewThread()) {
          
            //new scan delay time (now + 2 seconds) and indicator
            area.setDelayTime(System.currentTimeMillis() + 2000L);
    
            // good
            return true;           
          
        } // endif        
    } // endif      
  } // end-for
  
  //no good      
  return false;
    
} // end-method

/**
 * Start all threads in list
 */
protected void startAllThreads () {

  // find an available thread
  for (AreaThreadMgmt him : details) {  
    
    // When available for start
    if  (him.isAvailable()) {
        
        // When successfully started
        if  (him.setNewThread()) {
          
            // so it can timeout
            him.calcDeathTime();
          
            //new scan delay time (now + 2 seconds) and indicator
            area.setDelayTime(System.currentTimeMillis() + 2000L);           
          
        } // endif        
    } // endif      
  } // end-for
    
} // end-method

/**
 * Find a waiting thread and "post" that thread 
 * @return boolean
 */ 
protected boolean setPosted () {    
    
  // loop thru the details looking for a status of "waiting"
  for (AreaThreadMgmt him : details) {  
         
    // When a waiting thread, post it
    if  (him.isWaiting()) { 

        // When post sucessful, done
        if  (him.setPosted()) return true;
        
     } // endif     
  } // endfor   

  // none posted   
  return false;
    
} // end-method

/**
 * The system is shutting down. Inform all the normal threads to exit.
 * Output Agent threads are ignored unless there are no async requests working,
 *   then they are terminated if alive.
 *   
 * Other then with a cancelled status there is a chance of error since the status is set()
 *  rather than atomically set with a CAS. Since the system is shutting down, this is acceptable.  
 *
 * @param nbr_async int - number of async requests busy
 * @return int
 */  
protected int setShutdown (int nbr_async) {
    
  // number of threads busy
  int r = 0;
  
  // look for busy, wake up
  for (AreaThreadMgmt him : details) { 
    
    // just to eliminate excessive if/else    
    while (true) {
            
      // When the status is cancelled  
      if  (him.isCancelled()) {
           
            // When set to disabled, get out
            if  (him.disabledCancelled(2002)) break;
            
      } // endif
      
      // When in a waiting status, therefore, will
      //   probably not wake up unless notified
      if  (him.isNapping()) {
  
          // When there is no live thread
          if  (!him.isAlive()) {
  
              // set disabled
              him.setDisabled(2002); 
              
              // done
              break;          
          }
          else {                    
              // When this is an OA Queue 
              if  (!isNormalQueue()) {
    
                  // When in force shut down or
                  //  no async requests active 
                  if  ((T.getEndit() > TymeacInfo.SHUT_STAGE1)||
                       (nbr_async == 0)) {
                      
                      // wake up to terminate the thread
                      him.setTerminate();
                                          
                  } // endif
              }
              else {
                  // wake up the normal thread, let it end on its own
                  him.wakeUpThread();  
    
              } // endif                
    
              // say some are busy
              r++; 
              
              // done
              break;
              
          } // endif              
      } // endif
                    
      // Busy processing a request          
      if  (him.isBusy()) {
          
          // When in forced shut down 
          if  (T.getEndit() > TymeacInfo.SHUT_STAGE1) {

              // When this is a normal que 
              if  (isNormalQueue()) {
                  
                  // set to terminate the thread
                  him.setTerminate();  
              }
              else {
                  // When in really forced shut down 
                  if  (T.getEndit() > TymeacInfo.SHUT_STAGE2) {

                      // set to terminate the OA thread
                      him.setTerminate();

                  } // endif
              } // endif
          } // endif
          
          // say some are busy
          r++;
          
          // done
          break;
      
      } // endif
      
      // done
      break;
      
    } // end-while
  } // end-for

  // return number busy   
  return r;
  
} // end-method

/**
 * Set the waitlist anchor here and pass the
 * reference waitlist to each thread
 * @param waitLists
 */
protected void setWaitLists(AreaWaitListsAnchor waitLists) { 
  
  // anchor
  waitlists = waitLists;
  
  // each thread
  for (AreaThreadMgmt him : details) { 
    
    him.setWaitLists(waitLists);
    
  } // end-for
  
} // end-method

/**
 * Error notification
 * @param msg java.lang.String
 */
private void sendNotify (String msg ) {
    
  // When not using a notify, return
  if  (!T.isNotifyUsed()) return;
  
  // When this Queue is the Notification Queue, do not invoke
  //   itself.  to avoid a recurrsive error
  if  (T.getNotifyName().compareTo(area.getName()) == 0) return;
  
  // do a notify
  T.getNotify_tbl().sendMsg(msg);
    
} // end-method

/**
 * Write an error log message
 * @param msg java.lang.String
 */
private void writeLog (String msg ) {
        
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, 20);
    
} // end-method
} // end-class
