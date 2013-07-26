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

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;


/**
 * A single Queue Wait List.
 * 
 * Each object contains information about each WaitList and 
 * the actual list itself (ConcurrentLinkedQueue.) 
 */
public final class AreaWaitList {
     
  private final TyBase T;                   // pointer to Tymeac base storage
  private final AreaWaitListsAnchor anchor; // pointer to Wait List anchor for all waitlists
  
  private final ConcurrentLinkedQueue<RequestDetail> details; // array of detail entries
  private final AtomicInteger count;      // current count of entries within
   
  private AtomicInteger hi_used;      // high water mark
  private AtomicInteger overflow_pri; // primary overflows
  private AtomicInteger overflow_sec; // secondary overflows
  private AtomicInteger nbr_reset;    // times any entry was reset for backout
  private AtomicInteger times_used;   // times this list was used 
  
/**
 * Constructor
 * @param Ty
 * @param mother
 */
protected AreaWaitList(TyBase Ty, AreaWaitListsAnchor mother) {
  
  T      = Ty; // Tymeac base storage
  anchor = mother; // Anchor point
  
  hi_used      = new AtomicInteger(0); // base variables
  overflow_pri = new AtomicInteger(0); // +
  overflow_sec = new AtomicInteger(0); // +
  nbr_reset    = new AtomicInteger(0); // +
  times_used   = new AtomicInteger(0); // +
  
  details = new ConcurrentLinkedQueue<RequestDetail>(); // get queue for details
  count   = new AtomicInteger(0); // current count of entries within
  
} // end-constructor
   
/**
 * Increment primary overflow
 */
protected void addPriOverflow ( ) { overflow_pri.incrementAndGet(); } // end-method

/**
 * Add an entry to the list when not greater than the limit. Since the size may 
 *   increase dynamically by other threads adding elements, we may add more than the limit. 
 *   
 * @return boolean
 * @param req RequestDetail - this entry
 */
protected boolean addToQueue (RequestDetail req) {  
  
  // number in use now
  int nbr_busy = count.get();  
  
  // When Not over limit (max permitted > those already in there), try to add
  if  (anchor.getNbrPhysicalEntries() > nbr_busy) {
  
      // When successful
      if  (details.offer(req)) {
        
          // bump current count
          count.incrementAndGet();
                    
          // try to set a new high water mark
          setHiUsed(++nbr_busy);
          
          // bump total number times added
          addTimesUsed();
          
          // good
          return true;
          
      } // endif
  } // endif
   
  // Not added
  return false;
   
} // end-method

/**
 * Increment times reset
 */ 
private void addReset ( ) { nbr_reset.incrementAndGet(); } // end-method

/**
 * Increment secondary overflow
 */ 
protected void addSecOverflow ( ) { overflow_sec.incrementAndGet(); } // end-method

/**
 * Increment times used
 */ 
private void addTimesUsed ( ) { times_used.incrementAndGet(); } // end-method

/**
 * Diagnose - print for debugging. Using this on a Queue that is heavily in use may
 *   be worthless. Since there is no locking, the size at the beginning may not reflect
 *   what is physically printed. The requests, picked up here, may finish before we can
 *   print them (status=64)
 */ 
protected void dsplyRequests ( ) {
    
  int j = 1;
  String type;
  
  // print the busy in the waitlist
  for (RequestDetail him: details) {
    
    type = (him.isSync())? "Sync" : "Async";
  
    TyBase.printMsg(
        "       #-" + j
        + " " + type  
        + " Req=" 
        + him.getUni()
        + " Status=" 
        + him.getStatus());  
    
    // next j
    j++;
      
  } // end-for 
    
} // end-method

/**
 * Remove first in queue and return it for processing.
 * @return RequestDetail
 */ 
protected RequestDetail fetchFirstBusy ( ) {
  
  // remove first entry in queue
  RequestDetail request = details.poll();
  
  // When something came out, decrement count
  if  (request != null) count.decrementAndGet();
  
  // what came out
  return request;
    
} // end-method

/**
 * See if an available entry
 * @return boolean
 */
protected boolean getAvail ( ) {    
  
  // When not at max
  return (getWlBusy() < anchor.getNbrPhysicalEntries())? true : false;
    
} // end-method

/**
 * Accessor for the primary overflow
 * @return int
 */
protected int getPriOverflow ( ) { return overflow_pri.get(); } // end-method

/**
 * Accessor for the high water mark
 * @return int
 */
protected int getHiUsed ( ) { return hi_used.get(); } // end-method

/**
 * Accessor for number reset
 * @return int
 */ 
protected int getReset ( ) { return nbr_reset.get(); } // end-method

/**
 * Accessor for the secondary overflow
 * @return int
 */ 
protected int getSecOverflow ( ) { return overflow_sec.get(); } // end-method

/**
 * Accessor for times used
 * @return int
 */ 
protected int getTimesUsed ( ) { return times_used.get(); } // end-method

/**
 * Number in use
 * @return int
 */
protected int getWlBusy () { 
  
  int nbr = count.get();
  
  // never < 0 goes back
 return (nbr > 0)? nbr : 0;
  
} // end-method

/**
 * Test if queue is empty. 
 *   When there is a request in the list, then the list is NOT empty -- return false. 
 *   When there is NO request in the list, then the list IS empty -- return true. 
 *   This may seem trivial, but reversing true/false here will put the Queue Threads into
 *   a never ending loop.
 * @return boolean
 */ 
protected boolean isEmpty ( ) {
  
  // When empty, say IS empty
  return details.isEmpty();
    
} // end-method

/**
 * Match a request to an entry
 * @return boolean
 * @param req RequestDetail - this request 
 */ 
protected boolean matchEntry (RequestDetail req) {    
    
  // for each detail in the array
  for (RequestDetail him : details) {
    
    // When a match, say found    
    if  (him == req) return true;                    
      
  } // end-for
  
  // not found
  return false;
  
} // end-method

/**
 * Purge all busy entries
 * @param why int - the reason for the purge
 * @return int
 */ 
protected int purgeBusy (int why ) {
        
  int count = 0;
  RequestDetail request;
  
  /*
   * This method should only be called when the Queue is stalled; i.e. the
   *   threads are not working. That would mean that the list is stable. In such a
   *   case using for-each would be easier.
   *   
   * Since anything can happend at any time, we assume nothing and get a picture
   *   at the time of the call. The purge happends using the Object array as a base
   *   rather than the list directly. 
   *   
   * get all the details into an array
   */
  Object[] obj = details.toArray();
  
  // do all
  for (int i = 0, max = obj.length; i < max; i++) {
    
    // get detail
    request = (RequestDetail) obj[i];
    
    // When Async  
    if  (!request.isSync()) { 
        
        // add an entry to the stall table, cannot purge 
        T.getStall_tbl().addEntry(request.getUni(), why);
    }
    else {
        // When sync request removed successfully
        if  (removeDetail(request)) {
            
            // increment count, add to number reset
            count++;
            addReset();
            
        } // endif
    } // endif
  } // end-for         
          
  // back with count
  return count;
    
} // end-method

/**
 * Remove from the list.
 * remove() doesn't physically remove the element, it just nulls the Node reference to the entry. 
 * @param request RequestDetail
 * @return boolean
 */ 
private boolean removeDetail (RequestDetail request ) {        
  
  // try to remove
  boolean result = details.remove(request);
  
  // When item removed, decrement count of number in there
  if  (result) count.decrementAndGet();
  
  return result;
  
} // end-method

/**
 * Reset an entry (for backout)
 * @return boolean did or not done
 * @param req RequestDetail
 */ 
protected boolean resetEntry (RequestDetail req) {  
  
  /*
   * Since anything can happend at any time, we assume nothing and get a picture
   *   at the time of the call. The reset happends using the Object array as a base
   *   rather than the list directly. 
   *   
   * get all the details into an array
   */
  Object[] obj = details.toArray();
  
  // find the request therein
  for (int i = 0, max = obj.length; i < max; i++) {
    
    // When a match, try to remove  
    if  ((RequestDetail) obj[i] == req) { 
      
      // When removed
      if  (removeDetail(req)) {  
          
          //  bump number reset
          addReset();
          
          // done
          return true;
        
      } // endif      
    } // endif      
  } // end-for
  
  // not found
  return false;
           
} // end-method

/**
 * Set a new high water mark, maybe
 * @parm number busy before last add
 */
private void setHiUsed(int nbr_busy) {
  
  // current high water mark
  int hi = hi_used.get();
  
  // When a new high water mark, try to set the new high water mark
  if  (nbr_busy > hi)  hi_used.compareAndSet(hi, nbr_busy);
  
} // end-method
} // end-class
