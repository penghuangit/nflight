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

/**
 * This is the anchor point for the Queue's Wait Lists The pool is simply
 *   an ArrayList. The Queue Area Wait List Anchor contains fields used by the wait lists 
 *   and the Array of Wait Lists. 
 *   
 * The Queue structure:
 *   level one   -- AreaBase
 *   level two   -- this
 *   level three -- AreaWaitlist (individual waitlist)  
 */
public final class AreaWaitListsAnchor {
  
   private final TyBase T;      // pointer to Tymeac base storage
   private final AreaBase area; // pointer to Queue Base this waitlist belongs to
   
   private final int nbr_waitlists;  // number of waitlists (never changes)
    
   private final ArrayList<AreaWaitList> details;  // the wait lists
   
   /*
    * Physical max number of details in the array, below. This is the maximum
    *   number of entries the user wants at any given time. Since the size 
    *   of the ConcurrentLinkedQueue may increase dynamically by other threads 
    *   adding elements, we may end up with more than the limit (but not by much.)
    *   Also, when removing an element from the ConcurrentLinkedQueue, the remove()
    *   does not actually remove the element immediately so we end up with left
    *   over elements for some time. Since remove() should be rarely used, it doesn't
    *   present a problem.
    *   
    *  This field is ONLY changeable by the synchronized alterWlEntries()
    *    Since changes may come from multiple threads, the code is synchronized.
    *  
    *  Changes to this field only affect new adds to the Queue. They have no affect 
    *    on what is currently inside the Queue. The change may be up or down. When it 
    *    goes down for a heavily used system, the results may be interesting. 
    *    
    *      E.G. The number is 100 and the queue is running at 70% full. The number
    *      changes to 20. Now any new adds will see a queue that is over 100% and this
    *      will result in an overflow into the next WaitList or a request rejection.
    *  
    *  Changes affect the logical number of entries when the physical becomes
    *    less than the logical.
    *    
    *  Changes will ALWAYS skew the Tymeac Statistics.
    */   
   private volatile int physical_details; 
   
   /*
    * Logical max number of details in the array, below. 
    *   These are only used for calculating thresholds.
    */
   private volatile int logical_details;    
   
/**
 * Constructor
 * @param Ty TyBase - tymeac base 
 * @param me AreaHeader to which this belongs
 * @param max int - total wait lists
 * @param entries int - total entries in a waitlist
 * @param th_entries int - total entries in a waitlist for thresholds
 */
protected AreaWaitListsAnchor ( TyBase Ty, 
                                AreaBase me, 
                                int max, 
                                int entries,
                                int th_entries) {
  
  T                = Ty;  // common
  area             = me;  // Area Base
  nbr_waitlists    = max;  // total waitlists
  physical_details = entries;  // total physical entries in each waitlist  
  logical_details  = th_entries; // total logical entries for threshold processing
  
  // new list
  details = new ArrayList<AreaWaitList>(max);
  
  // init the waitlists
  for  (int i = 0; i < max; i++) {
    
    // do each of the wait lists
    details.add(new AreaWaitList(T, this));
    
  } // end-for
  
} // end-constructor

/**
 * Add an entry to the priority list. The priority number was verified early on and
 *   adjusted for Java iteration (i.e. User priority 1 = Java starting number 0)  
 *   
 * @return 0=good, >0 overflow, <0 failed
 * @param req RequestDetail - this entry
 * @param priority the priority of the request
 * @return int 0=added, no overflow; 1=added, with overflow; -1=failed to add
 */
protected int addToQueue (RequestDetail req, int priority) {  
    
  // no overflow yet
  boolean over = false;
  
  /*
   * Try to put the request in the corresponding priority first. When it doesn't go, 
   *   then keep trying to put the request into the next wait list. The first time
   *   it doesn't fit is a primary overflow. If it doesn't go into the next
   *   wait list then its a secondary overflow. Return to the caller what happened. 
   */
  for (int prty = priority; prty < nbr_waitlists; prty++) {
    
    // When successfully added to waitlist, return what happended    
    if  ((details.get(prty)).addToQueue(req))  return (over)? 1 : 0;
         
    // When hasn't overflowed yet
    if  (!over) {

        // accru primary overflow
        details.get(prty).addPriOverflow();

        // set overflowed at least once
        over = true;
    }
    else {
        // accru secondary overflow
        details.get(prty).addSecOverflow();

    } // endif
  } // end-for
  
  // failed to add to any waitlist
  return -1; 
  
} // end-method

/**
 * Set new Wait Lists maximum logical entries for threshold processing during execution.  
 * This only has an affect on new requests.
 * @param int number of entries in wait lists
 */ 
protected synchronized void alterLogicalWlEntries(int nbr) {    
  
  // new number
  logical_details = nbr;
  
  // set new number of wait list entries for thresholds in base
  area.setThresholdWlEntries(nbr);
    
} // end-method

/**
 * Set new Wait Lists maximum physical entries during execution.  
 * This only has an affect on new requests.
 * @param int number of entries in wait lists
 */ 
protected synchronized void alterPhysicalWlEntries(int nbr) {    
  
  // new number
  physical_details = nbr;
  
  // When physical change adversely affects logical
  if  (logical_details > physical_details) {
    
      // force same as
      logical_details = nbr;
      
      // set new number of wait list entries for thresholds in base
      area.setThresholdWlEntries(nbr);
      
  } // endif
    
} // end-method

/**
 * Diagnose - display wait lists
 */ 
protected void dsplyWaitLists () { 
  
  TyBase.printMsg(area.getName() 
               + " Total Wait Lists=" 
               + nbr_waitlists 
               + " in each="
               + physical_details
               + " thresholds max="
               + logical_details);
  
  // wait list number
  int i = 1;
  
  // each waitlist
  for (AreaWaitList WL : details) {
     
    TyBase.printMsg(
        "  Wait List # " + i
                    + ", #busy=" 
                    + WL.getWlBusy()
                    + " HiUsed="
                    + WL.getHiUsed()                    
                    + " OvrPri="
                    + WL.getPriOverflow()
                    + " OvrSec="
                    + WL.getSecOverflow()
                    + " #Reset="
                    + WL.getReset()
                    + " TimesUsed="
                    + WL.getTimesUsed());
      // next wl
      i++;
                                            
      // dispay the active requests
      WL.dsplyRequests();
      
  } // end-for 
  
} // end-method

/**
 * Get all the wait list details for display
 * @return TyWLElements
 */
protected TyWLElements[] getDisplayable() {  
  
  // the return obj
  TyWLElements[] twe = new TyWLElements[nbr_waitlists];
  
  int i;
  
  // initialize all elements
  for (i = 0; i < nbr_waitlists; i++) {
      
    // get a new object
    twe[i] = new TyWLElements();
    
  } // end-for
  
  // start back at first
  i = 0;
  
  // do each wait list
  for (AreaWaitList WL : details) {
    
    // fill in the data
    twe[i].status  = 3;
    twe[i].in_use  = WL.getWlBusy();
    twe[i].used    = WL.getTimesUsed();
    twe[i].hi_used = WL.getHiUsed();
    twe[i].reset   = WL.getReset();
    twe[i].over_p  = WL.getPriOverflow();
    twe[i].over_s  = WL.getSecOverflow();
    
    i++; // bump index
    
  } // end-for 
  
  // done
  return twe;
  
} // end-method

/**
 * Get the first wait list entry that is busy.
 * @return RequestDetail
 */
protected RequestDetail getFirstBusy () {  
  
  // return value
  RequestDetail req = null;
  
  // look in each wait list for the first used
  for (AreaWaitList WL : details) {
    
    // picks up the top of the queue
    req = WL.fetchFirstBusy();
    
    // When found one, give back
    if  (req != null) return req;
    
  } // end-for
  
  // no requests in wait lists
  return null;
  
} // end-method

/**
 * Accessor for the max number of entries for thresholds
 * @return int
 */ 
protected int getNbrLogicalEntries () { return logical_details; } // end-method

/**
 * Accessor for the max number of entries
 * @return int
 */ 
protected int getNbrPhysicalEntries () { return physical_details; } // end-method

/**
 * Accessor for the number of wait lists
 * @return int
 */ 
protected int getNbrWl () { return nbr_waitlists; } // end-method

/**
 * Count of entries busy in a single wait list
 * @return int
 */ 
protected int getPriorityBusy (int priority) {  
  
  // number busy in this wait list
  return details.get(priority).getWlBusy(); 
  
} // end-method

/**
 * Look for an available entry in any wait list
 * @return boolean
 */
protected boolean getWlAvail ( ) {
  
  // find an available entry in any waitlist
  for (AreaWaitList WL : details) {
    
    // When any available, say found one
    if  (WL.getAvail()) return true;
    
  } // end-for
  
  // none found
  return false;  
  
} // end-method

/**
 * Count of entries busy
 * @return int
 */ 
protected int getWLBusy () {
  
  // count
  int count = 0;
    
  // count busy in each wait list
  for (AreaWaitList WL : details) {
    
    count += WL.getWlBusy();
    
  } // end-for
  
  return count;
    
} // end-method

/**
 * Test for a request in any wait list.
 * 
 * This is what can happen when a Scheduling Thread (ST) (this may either be
 *   the thread for the (a)syncRequest() or the AreaQueThread when scheduling
 *   the Output Agent Queue for an asynchronous request) puts a new request
 *   into a wait list and a working Queue Thread (QT) for the Queue 
 *   just finished processing those wait lists. There is no guarantee what
 *   thread will run at any given time so the scenareo below happens:
 *   
 *  ------------------------------------------------------------------------------- 
 *   
 *   ST                           QT
 *   
 *                                (Status is 'Processing')
 *                                Finished looking in Wait Lists, no work pending
 *   Add request to Wait List.
 *   
 *   No QT's are available to
 *   post (status 'Waiting').
 *   A QT is busy 'Processing'
 *   requests so this ST believes
 *   that QT will pick up this 
 *   new request. No further
 *   action necessary.   
 *   End of Thread.
 *                                 Set status to 'Waiting'.
 *                                 When wait time expires with no posting,
 *                                 set status to 'Detached'.
 * 
 * -------------------------------------------------------------------------------
 *                           
 * If there are other QT's working the Queue, then one of them will pick up the request. 
 *   If this is the only QT woring the Queue then the request sits in a wait list until 
 *   a new request comes in making a QT search the wait lists.
 *   
 * The same scenareo can happen when the QT goes from 'waiting' to 'detached'. A 
 *   request can squeak in.  
 * 
 * This method avoids the problem by looking (not taking) in all the waitlists for any work.  
 *   
 * @return boolean
 */
protected boolean isWaitlistEmpty() {   
  
  // find any busy entry in any waitlist
  for (AreaWaitList WL : details) {
    
    // When any list has requests, say not empty
    if  (!WL.isEmpty()) return false;
    
  } // end-for
  
  // none found
  return true;    
  
} // end-method

/**
 * find a matching request in any wait list
 * @param RequestDetail req
 * @return boolean
 */ 
protected boolean matchEntry(RequestDetail req) {  
  
  // find the request
  for (AreaWaitList WL : details) {
    
    // When found it
    if  (WL.matchEntry(req)) return true;
    
  } // end-for
  
  // not found
  return false;  
  
} // end-method

/**
 * Purge any busy entries in the wait lists
 * @param int reason for the purge
 */ 
protected void purgeBusy(int reason) {  
  
  // purge busy entry from each wait list
  for (AreaWaitList WL : details) {
    
    // purge any busy entries
    WL.purgeBusy(reason);
    
  } // end-for
  
} // end-method

/**
 * Reset an entry (for backout)
 * @return boolean did or not done
 * @param req RequestDetail
 */ 
protected boolean resetEntry (RequestDetail req) {  
  
  // find a match
  for (AreaWaitList WL : details) {
    
    // When found, try to reset it
    if  (WL.matchEntry(req)) return WL.resetEntry(req);      
        
  } // end-for
  
  // not reset
  return false;
  
} // end-method
} // end-class
