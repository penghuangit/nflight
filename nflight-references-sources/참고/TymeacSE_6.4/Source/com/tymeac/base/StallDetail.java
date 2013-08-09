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

import java.util.concurrent.atomic.*;

/**
 * The stall array detail class. 
 *     times_checked -- is only modified by the Monitor.
 *     status -- may be modified by multiple threads
 */   
public final class StallDetail {
  
  private final TyBase T; // Tymeac base
   
  private final long  entered;       // time entered
  private final long  at_name;       // generated name  
  private final int   failed_reason; // why it is here

  private volatile int times_checked; // times checked by monitor 
  
  private AtomicInteger  status;  // as below
  
      private static final int BUSY  = 1;
      private static final int FREED = 64;

/**
 * Constructor
 * @param u_name int - async table sub
 * @param why int - reason code
 */
protected StallDetail(TyBase Ty, long u_at_name, int why) {
  
  T             = Ty; // Tymeac base    
  entered       = System.currentTimeMillis(); // time entered           
  at_name       = u_at_name; // Async Request entry unique name
  status        = new AtomicInteger(BUSY); // status is in use         
  times_checked = 0;        // never checked by the monitor
  failed_reason = why;      // why failed 
        
} // end-constructor

/**
 * Increment the number of times this entry was checked by the Monitor
 * @return int
 */         
protected int addChecked() {  

  // When used, incrment times this entry checked 
  if  (isBusy()) times_checked++;
      
  // give it back
  return times_checked;
     
} // end-method

/**
 * Accessor for async table name
 * @return long
 */         
protected long getAtName() { return at_name; } // end-method  

/**
 * Accessor for number of times checked
 * @return int
 */         
protected int getChecked() { return times_checked; } // end-method

/**
 * Accessor for the time entered
 * @return long
 */         
protected long getEntered() { return entered; } // end-method

/**
 * Accessor for the failed reason
 * @return int
 */         
protected int getFailedReason() { return failed_reason; } // end-method  

/**
 * Accessor for the status code
 * @return int
 */         
protected int getStatus() { return status.get(); } // end-method 

/**
 * Accessor for busy status
 * @return boolean
 */         
protected boolean isBusy() {
  
  // busy or not
  return (getStatus() == BUSY)? true : false;
     
} // end-method 

/**
 * Find a match on the async generated name within the stall array
 * @param a_at_name long - gen name
 * @return boolean
 */
protected boolean matchName(long a_at_name) {   
   
  // When a match, say so
  return (a_at_name == at_name)? true : false;
    
} // end-method  

/**
 * Free an entry
 */       
protected void setFree() { 
    
  // When not already done, remove from array
  if  (status.compareAndSet(BUSY, FREED)) T.getStall_tbl().removeDetail(this);  
   
} // end-method         
} // end-class
