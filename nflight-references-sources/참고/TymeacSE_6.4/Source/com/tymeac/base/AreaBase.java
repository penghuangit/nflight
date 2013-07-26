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

import java.util.concurrent.atomic.*;
import java.net.URLClassLoader;
import java.lang.reflect.Method;

/**
 * The Queue Area contains 
 *   Queue-wide variables
 *   the reflect.method pointer to the Processing Application Class 
 *   the pointer to the waitlist anchor
 *   the pointer to the queue threads anchor
 *   
 * The Queue structure:
 *   level 1  -- AreaBase (this)
 *   level 2a -- AreaThreadsAnchor
 *   level 3a -- AreaThreadMgmt
 *   level 2b -- AreaWaitListsAnchor
 *   level 3b -- AreaWaitList 
 */
public final class AreaBase {
  
// *--- Basic Queue Data ---*  
  
  private final TyBase  T;        // addr of base storage  
  private final String  que_name; // name of this queue 
  private final int     que_number;   // seq number from zero
  private final boolean que_type;     // type of queue false=normal, true=output agent
  
  // *--- Wait Lists ---*     
  // addr of the wait list anchor point
  private final AreaWaitListsAnchor waitlists;  

// *--- Threads ---*      
  // addr of the threads anchor point
  private final AreaThreadsAnchor threads;
  
  private String  pap_name; // name of Processing Application Class to invoke
  private URLClassLoader queClassLoader = null; // private class loader for the PAC
  
  private volatile int pa_timeout;     // max time for pa exec in seconds (volatile since release 5.6)
  private volatile int wait_time;      // time to wait when no work
  private volatile int kill_time;      // time to kill the thread      
  private volatile boolean use_delay;  // use scan delay for starting new thread / can be a race, but doesn't matter
  private volatile long delay_time;    // scan delay time  / can be a race, but doesn't matter
  
  private AtomicInteger totl_overall;   // total threshold overall times
  private AtomicInteger totl_individ;   // total threshold individual times
  private AtomicInteger totl_average;   // total threshold average times  
  private AtomicInteger totl_overflow;  // total waitlist overflowed times    
  private AtomicInteger totl_no_busy;   // total no other threads busy times 
  
  // method for invoke(). initially here, fetched by the Queue Thread   
  private Method pap_method;    
  
// *--- Thresholds ---* (alterable, but only in sync methods)   
  
  private float overall;     // overall %
  private float overall_amt; // calc overall                       
  private float individ;     // individual %           
  private float factor;      // weighted factor              
  private float average;     // weighted average

  // calculated factor for all waitlists
  private float[] this_factor;

  // make a float
  private float f_nbr_waitlists;   // float of nbr wait lists
  private float f_nbr_wl_entries;  // float of nbr of entries for thesholds
  private float f_totl_wl_entries; // float of product of above two 
  
/**
 * Constructor
 *@param c_t TyBase - Tymeac Base Pointer
 *@param c_type int - Type of Queue (normal or output agent)
 *@param c_que_name String - The name of this Queue
 *@param c_pap_name String - The Processing Application Name
 *@param c_pa_timeout int - Max time to let the application compute
 *@param c_que_numb int - Queue number starting at zero
 *@param c_nbr_waitlists int Number of Wait Lists
 *@param c_wait_time int - Max time to wait for a timeout
 *@param c_kill_time int - Max time in "inactive" status
 *@param c_nbr_threads - Total number of threads 
 *@param c_nbr_wl_entries - Max entries in a Wait List
 *@param c_nbr_wl_entries - Max entries in a Wait List for thesholds
 *@param c_overall float - Overall %
 *@param c_individ float - Individual %
 *@param c_factor float - Weighted Factor
 *@param c_average float - Weighted Average %
 */
protected AreaBase(TyBase  c_t,
          boolean c_type,
          String  c_que_name, 
          String  c_pap_name,
          int     c_pa_timeout,
          int     c_que_numb,
          int     c_nbr_waitlists,  
          int     c_wait_time,
          int     c_kill_time,    
          int     c_nbr_threads,
          int     c_nbr_wl_entries,
          int     c_nbr_th_wl_entries,
          float   c_overall,        
          float   c_individ,
          float   c_factor,         
          float   c_average) {
  
  // all those instance fields
  T           = c_t;   
  que_name    = c_que_name;   
  pap_name    = c_pap_name;
  que_number  = c_que_numb;
  pa_timeout  = c_pa_timeout;
  wait_time   = c_wait_time;
  kill_time   = c_kill_time;
  que_type    = c_type;
  overall     = c_overall;        
  individ     = c_individ;
  factor      = c_factor;  
  average     = c_average;

  use_delay  = false;
  delay_time  = 0;
  
  totl_average  = new AtomicInteger(0);  // total average times
  totl_individ  = new AtomicInteger(0);  // total individual times
  totl_overall  = new AtomicInteger(0);  // total overall times
  totl_overflow = new AtomicInteger(0);  // total overflowed times
  totl_no_busy  = new AtomicInteger(0);  // total none busy times
  
  // reflect.Method set by the start up after construction
  pap_method = null; 
  
  // length of the que name
  int qlen = que_name.length();
  
  // start of thread name
  String dbase;
  
  // When only 36 char, use all
  if  (qlen <= 36) {
      
      // name is all of que name 
      dbase = "TyQ-"  + que_name.substring(0, qlen)
              + "-";
  } 
  else {
      // name is 1st 32 of que name + last 3
      dbase = "TyQ-"  + que_name.substring(0, 32)
              + "_"
              + que_name.substring(qlen - 3, qlen)
              + "-";
  } // endif  
  
  // *-- Wait Lists --*
  
  // get the Wait Lists object 
  waitlists = new AreaWaitListsAnchor(
                            T, 
                            this, 
                            c_nbr_waitlists, 
                            c_nbr_wl_entries,
                            c_nbr_th_wl_entries); 
  
  // float nbr waitlists (used in threshold processing)
  f_nbr_waitlists = c_nbr_waitlists; 
  
  // other fields that use waitlists (used in threshold processing)
  setThresholdWlEntries(c_nbr_th_wl_entries);
  
  // *-- threads --*
     
  // get the threads object
  threads = new AreaThreadsAnchor(
                          T, 
                          this,
                          que_type, 
                          dbase, 
                          c_nbr_threads);      
  
  // insert wait list anchor in threads anchor
  threads.setWaitLists(waitlists);
  
  /*
   * When starting all threads at start up,
   *   the startup class will call the method
   *   startAllThreads()
   *   after construction is complete 
   */
       
} // end-constructor  

/**
 * Increment number of times average true 
 */ 
private void addAverage ( ) { totl_average.incrementAndGet(); } // end-method

/**
 * Increment number of times individual true 
 */ 
private void addIndivid ( ) { totl_individ.incrementAndGet(); } // end-method

/**
 * Increment number of times none busy true 
 */ 
private void addNoBusy ( ) { totl_no_busy.incrementAndGet(); } // end-method

/**
 * Increment number of times overall true 
 */ 
private void addOverall ( ) { totl_overall.incrementAndGet(); } // end-method

/**
 * Increment number of times waitlist overflowed and we 
 * started a new thread   
 */ 
private void addOverflow ( ) { totl_overflow.incrementAndGet(); } // end-method

/**
 * Delay the wait list scan?
 * @return boolean delaying the scan or not
 */
private boolean delayScan() {
  
  // When using a delay
  if  (use_delay) {

      // When under the limit
      if  (delay_time > System.currentTimeMillis()) {

          // we're delaying
          return true;
      }
      else {
          // not using a delay
          use_delay = false;
          
      } // endif
  } // endif 

  // no delay
  return false;  
  
} // end-method

/**
 * Calc weighted average % to start a new thread
 * @return boolean started a new thread or not
 */
private boolean didWeightedAverage(int i_priority) {
  
  // When a weighted average is not used, done
  if  (average == 0.0F) return false;

  // total %                         
  float percentage = 0.0F;

  // nbr of busy entries in a waitlist
  float f_wl_busy;

  // total participating wait lists
  int participating = 0;

  // current is first
  int curr_wl = 0; 
                              
  // look in all waitlists from the first to the requested
  do {
    // Get nbr busy in a single waitlist
    f_wl_busy = waitlists.getPriorityBusy(curr_wl);
           
    // When at least one busy
    if  (f_wl_busy > 0.0F) {
               
        // cumulative percent
        percentage =+ (f_wl_busy / f_nbr_wl_entries) + this_factor[(curr_wl)];             
                 
        // another wait list is participating in calc
        participating++;
               
    } // endif 

    // up to next wait list
    curr_wl++;

  // finish all wait lists                                                                                                
  } while (curr_wl <= i_priority);         
               
  // When no requests are waiting in participating waitlists, all done
  if  (participating == 0) return false;
         
  // number busy
  int busy_count = threads.getBusyCount();
  
  // Not to exceed that which are participating, force same 
  if  (busy_count > participating) busy_count = participating;
         
  // When not zero, get final percent = % / nbr active threads
  if  (busy_count > 0) percentage /= busy_count;
               
  // When average %, rounded > header amt
  if  ((percentage + 0.005F) > average) {             

      // When starting new thread successful,
      if  (threads.setNewThread()) {
        
        // increment times this happended
        addAverage();
        
        // done
        return true;
        
      } // endif          
  } // endif

  // all done       
  return false;  
  
} // end-method

/**
 * Calc individual % to start a new thread
 * @param int i_priority
 * @return boolean started a new thread or not
 */
private boolean didIndivid(int i_priority) {
  
  // When Individual % is used
  if  (individ > 0.0F) {
            
      // When nbr busy in this wait list / nbr in an entry + weighted factor > individ
      if ((waitlists.getPriorityBusy(i_priority) / f_nbr_wl_entries) 
                  + this_factor[i_priority] > individ) {

          // When starting new thread successful, increment times this happended
          if  (threads.setNewThread()) addIndivid();              
  
          // done           
          return true;

      } // endif
  } // endif 
  
  // not done
  return false;
  
} // end-method

/**
 * Calc overall % to start a new thread
 * @return boolean started a new thread or not
 */
private boolean didOverall() {
  
  // When overall is used
  if  (overall > 0.0F) {
           
      // When nbr all busy > pre-calc overall %
      if  (waitlists.getWLBusy() > overall_amt) {

          // When starting new thread successful, increment times this happended
          if  (threads.setNewThread()) addOverall();
              
          // all done
          return true;

      } // endif
  } // endif 
  
  // not done
  return false;  
  
} // end-method

/**
 * Accessor for Weighted Average
 * @return float
 */  
protected float getAverage () { return average; } // end-method

/**
 * Accessor for weighted factor
 * @return float
 */ 
protected float getFactor ( ) { return factor; } // end-method

/**
 * Accessor for Individual %
 * @return float
 */  
protected float getIndivid ( ) { return individ; } // end-method

/**
 * Accessor for Kill Time
 * @return int
 */  
protected int getKillTime ( ) { return kill_time; } // end-method

/**
 * Accessor for method
 *@return Method
 */
protected Method getMethod() { return pap_method; } // end-method  

/**
 * Accessor for queue name
 *@return String
 */        
protected String getName() { return que_name; } // end-method  

/**
 * Accessor for Overall %
 * @return float
 */ 
protected float getOverall ( ) { return overall; } // end-method

/**
 * Accessor for PaP Class name
 * @return java.lang.String
 */
protected String getPaClass ( ) { return pap_name; } // end-method

/**
 * Accessor for PaP timeout
 * @return int
 */
protected int getPaTimeout ( ) { return pa_timeout; } // end-method

/**
 * Accessor for the Queue Class Loader
 * @return java.net.URLClassLoader
 */
protected URLClassLoader getQueClassLoader() { return queClassLoader; } // end-method

/**
 * Accessor for the Queue number
 * @return int
 */ 
protected int getQueNumber ( ) { return que_number; } // end-method

/**
 * Accessor for the Queue type
 * @return int
 */ 
protected boolean getQueType( ) { return que_type; } // end-method

/**
 * Accessor for total average times
 * @return int
 */        
protected int getTotAverage() { return totl_average.get(); } // end-method

/**
 * Accessor for total individual times
 * @return int
 */        
protected int getTotIndivid() { return totl_individ.get(); } // end-method

/**
 * Accessor for total none busy times
 * @return int
 */        
protected int getTotNoBusy() { return totl_no_busy.get(); } // end-method

/**
 * Accessor for total overall times
 * @return int
 */        
protected int getTotOverall() { return totl_overall.get(); } // end-method

/**
 * Accessor for total overflow times
 * @return int
 */        
protected int getTotOverflow() { return totl_overflow.get(); } // end-method

/**
 * Accessor for the threads anchor pointer
 * @return WLheader
 */ 
protected AreaThreadsAnchor getThreadsAnchor ( ) { return threads; } // end-method

/**
 * Accessor for the wait list anchor pointer
 * @return WLheader
 */ 
protected AreaWaitListsAnchor getWaitlist ( ) { return waitlists; } // end-method

/**
 * Accessor for the wait time
 * @return int
 */  
protected int getWaitTime ( ) { return wait_time; } // end-method

/**
 * Accessor for normal queue
 * @return boolean
 */
protected boolean isNormalQueue( ) {
  
  // When false is a normal queue
  return que_type;
    
} // end-method

/**
 * Schedule the request in this queue
 *@param int req_nbr - request number
 *@param priority int - request priority
 *@param type boolean - normal queue or agent queue
 *@return int
 */
protected int schedule ( RequestDetail req, // request 
                         int priority,      // priority
                         boolean type) {    // type of request normal or not
  
  // When a normal request and not a normal queue  
  //  There is probably system damage. This was checked on start up.
  if  (type) {
    
      // When an output agent, no good, should be normal
      if  (!isNormalQueue()) return 5035;
  }
  else {
      // When a normal queue, no good, should be agent
      if  (isNormalQueue()) return 5040;
          
  } // endif
  
  // When no thread is available to process, cannot continue     
  if  (!threads.checkThreads()) return 5020;
  
  // verify and set request priority for check
  int i_priority = verifyPriority(priority);     
        
  // put the request in a wait list
  int newW = waitlists.addToQueue(req, i_priority);

  // When < 0 failed, no wait list available 
  if  (newW < 0) return 5025;
  
  // when 'posted' (awakened) a waiting thread, all done
  if  (threads.setPosted()) return 0;
  
  // When adding to a waitlist had an overflow
  if  (newW > 0) {
        
      // When start a new thread successful, increment times this happended  
      if  (threads.setNewThread()) addOverflow();
          
      /*
       * We're done whether the above start was successful or not.
       *   When successful, we started a new thread.
       *   When not, there is no thread left to start. 
       *     Since we checked for all threads disabled, above, 
       *     there should be a thread working the queue. If not then
       *     the request will time-out or stall or it already finished. 
       */
      return 0;
        
  } // endif  
  
  // When no thread is busy processing requests
  if  (!threads.getBusy()) {
        
      // try again, could have slipped in between working and waiting.
      // when 'posted' (awakened) a waiting thread, all done
      if  (threads.setPosted()) return 0;
    
      // When start a new thread Successful, 
      if (threads.setNewThread()) {
        
          // increment times this happended 
          addNoBusy();
        
          // all done
          return 0;
          
      } // endif
      
      // try again, could have slipped in between working and waiting.
      // when 'posted' (awakened) a waiting thread, all done
      if  (threads.setPosted()) return 0;        
     
  } // endif
  
  /*
   * When no thread is available for a start, we're done here.
   *   Since we checked for all threads disabled, above, 
   *   there should be a thread working the queue. If not then
   *   the request will time-out or stall or it already finished. 
   */
  if  (!threads.getAvailable()) return 0;
  
  /*
   * Upon reaching here, we know the request went into a wait list. We actually
   *  know very little else.
   * 
   * The request could already have been picked up by a busy thread and have
   *   completely finished so all that follows may be irrelevant.     
   *
   * We know that a least one thread was alive, not cancelled or disabled, the
   * last time we looked. 
   *  
   * We now know that no threads were in a waiting status or one would have
   * been notified. (checked this twice)
   * 
   * We know that if a waitlist overflow occurred, we did not start a new thread. 
   * This may simply be because the Queue only has one thread or all the threads are busy.
   * 
   * We know that if no threads were currently busy processing requests, we
   * did not start a new thread. This may simply mean the request finished already.
   * 
   * The status of Queue Threads may be 
   *   'waiting for work', 
   *   'available for starting'
   *   'busy working' or 
   *   'unavailable'. 
   *   
   * Since this is a lock-free environment and we can only check one status 
   *   at a time while the status is changing dynmamically. We have to assume that 
   *   since the request is in a waitlist it will process. 
   * 
   * We handle the problem of a thread changing its status from 'busy working' to 
   *   'waiting for work' or from 'waiting for work' to 'detached'     
   *   when a new request comes in between our checking this condition 
   *   (see method isWaitlistEmpty() )
   * 
   * If there is a problem beyond what we can know here (like the only thread in
   *   the Queue becoming disabled) then the Monitor will flag async requests 
   *   and sync requests will time out.
   * 
   * If no thread was available to start we returned successfully. 
   * 
   * At the time of last checking, at least one thread was available to start. This
   *   now brings us into Threshold Processing.
   *     
   * When to start a new thread when other threads are working is Threshold Processing. 
   *   The success or failure of the started thread is irrelevant, here.
   *
   * There is a wait list scan delay of two seconds from the time the last
   *   thread was started. When there is a flood of requests, the wait lists can
   *   fill up before a new thread can begin processing. This two second delay
   *   gives the last thread a chance to catch up so that we don't start too many
   *   threads.
   *
   * Overall % is checked first, 
   *   followed by individual %,    
   *     and finally weighted average.
   *
   * Zero % are ignored.     
   */ 

  // When delaying the scan, done here
  if  (delayScan()) return 0; 
         
  // When overall started a new thread, done
  if  (didOverall()) return 0;      
  
  // When individual started a new thread, done
  if  (didIndivid(i_priority)) return 0;
  
  // weighted average started a new thread is irrelevant
  didWeightedAverage(i_priority);
  
  // done here
  return 0;
  
} // end-method

/**
 * set weighted average
 * @param p_average float - weighted average
 */ 
protected synchronized void setAverage (float p_average) { average = p_average; } // end-method

/**
 * set delay scan time and indicator
 * @param int time
 */ 
protected void setDelayTime (long time) {
  
  delay_time = time;
  use_delay = true;
    
} // end-method

/**
 * set weighted factor
 * @param p_factor float - weighted factor
 */ 
protected synchronized void setFactor (float p_factor) {
  
  /*
   * calc the factor for each waitlist. The first is a dummy since the priority on a
   *   request comes in starting at 1, not 0
   *
   * even though sync, we can't have partial results since the end point connection
   *  threads don't use sync. Therefore, we create a new float[] and don't update
   *  anything till the end. 
   */
  
  // number of wait lists
  int nbr_waitlists = waitlists.getNbrWl();
  
  float[] temp_factor = new float[nbr_waitlists + 1];

  // dummy
  temp_factor[0] = 0.0F;

  // do all
  for (int i = 1; i < nbr_waitlists; i++) {
    
    // When a factor
    if (p_factor > 0.0F) {

        // calc amt, rounded
        temp_factor[i] = ((1.0F / i) * p_factor) + 0.005F; 
    }
    else {
        // is zero
        temp_factor[i] = 0.0F;

    } // endif
  } // end-for
  
  // set the new fields
  factor      = p_factor;
  this_factor = temp_factor;
    
} // end-method

/**
 * set individual percent
 * @param p_individ float - individual percent
 */ 
protected synchronized void setIndivid (float p_individ) { individ = p_individ; } // end-method

/**
 * set time to kill thread
 * @param p_kill_time int - time to kill
 * 
 */  
protected synchronized void setKillTime (int p_kill_time) { kill_time = p_kill_time; } // end-method

/**
 * set new proc appl class
 * @param pac java.lang.reflect.Method
 */
protected synchronized void setNewClass (String name ) { pap_name = name; } // end-method 

/**
 * set new proc appl java.lang.reflect.Method
 * @param pac java.lang.reflect.Method
 */
protected synchronized void setNewMethod (Method pac ) { 
  
  pap_method = pac; // locally
  
  // the threads too
  threads.setNewMethod(pac);  
  
} // end-method 

/**
 * set Overall percent
 * @param p_overall float - overall percent
 */ 
protected synchronized void setOverall (float p_overall) {
  
  overall = p_overall;

  // calc the overall %
  overall_amt = overall * f_totl_wl_entries; 
    
} // end-method

/**
 * set application time out
 * @param timeout int in seconds
 */ 
protected synchronized void setPaTimeout (int timeout) { pa_timeout = timeout; } // end-method

/**
 * set a new class loader
 * @param newQueClassLoader java.net.URLClassLoader
 */
protected synchronized void setQueClassLoader(URLClassLoader newQueClassLoader) { queClassLoader = newQueClassLoader; } // end-method

/**
 * Set the number of Wait List entries fields
 * @param entries int - new number of entries
 */ 
protected void setThresholdWlEntries (int entries) {
  
  // number of waitlists
  int nbr_waitlists = waitlists.getNbrWl();

  // new float entries
  f_nbr_wl_entries = entries;

  // total entries = nbr entries in each waitlist for thresholds * nbr waitlists
  f_totl_wl_entries = f_nbr_wl_entries * f_nbr_waitlists;

  // calc the overall %
  overall_amt = overall * f_totl_wl_entries; 

  // calc the factor for each waitlist. The first is a dummy since the priority on a
  //   request comes in starting at 1, not 0
  float my_factor[] = new float[nbr_waitlists + 1];

  // dummy
  my_factor[0] = 0.0F;

  // do all
  for (int i = 1; i < nbr_waitlists; i++) {
      
    // When a factor
    if (factor > 0.0F) {

      // calc amt
      my_factor[i] = ((1.0F / i) * factor) + 0.005F; 
    }
    else {
      // is zero
      my_factor[i] = 0.0F;

    } // endif
  } // end-for
  
  // update field
  this_factor = my_factor;
    
} // end-method

/**
 * set new wait time
 * @param p_wait_time int - max time to wait  
 */  
protected synchronized void setWaitTime (int p_wait_time) { wait_time = p_wait_time; } // end-method

/**
 * Start all threads at start up
 */  
protected  void startAllThreads() {
  
  threads.startAllThreads();
  
} // end-method

/**
 * Verify and set the proper priority for the request. Priority start as 1, 
 *   Java subscript starts at 0 so we always go back 1. 
 * @param priority int from request
 * @return int verified priority
 */
private int verifyPriority(int priority) {
  
  // verify priority
  int i_priority = priority;
    
  // When less than minimum           
  if  (priority < 1) {

      // use one
      i_priority = 1;
  }         
  else {
    // When passed > max  
    if  (priority > waitlists.getNbrWl()) {
     
        // use max 
        i_priority = waitlists.getNbrWl();

    } // endif      
  } // endif
  
  // back minus one
  return --i_priority; 
  
} // end-method
} // end-class