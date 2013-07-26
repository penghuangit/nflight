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

/**
 * Temporary setup for the notificattion environment
 * @since 6.2
 */
public final class SetUpNotify {
  
  // type of request
  public static final int NotifyMaint  = 1;
  public static final int NotifyReset  = 100;
  public static final int NotifyNew    = 110;
  public static final int NotifyStop   = 120;
  
  // return code here
  public static final int NotifySuccess  = 0;  // success
  public static final int NotifyNoCurr   = 1;  // no current notify function
  public static final int NotifyNotNec   = 2;  // notify doesn't need resetting
  public static final int NotifyFailVery = 3;  // failed to verify
  public static final int NotifyMissNew  = 4;  // missing new notify name
  public static final int NotifyInvFunc  = 5;  // missing new notify name
  public static final int NotifyNotInUse = 6;  // notification not in use
  public static final int NotifyStopped  = 7;  // notification service stopped
    
  // The base for all Tymeac processing    
  private final TyBase T;
  
/**
 * Simple constructor
 * @param Ty
 */
protected SetUpNotify (TyBase Ty) {
  
  T = Ty; // save Tymeac Base Storage
  
} // end-constructor

/**
 * General msg routine 
 * @param msg String
 */
private void doMsg(String msg) {

  // When logging, do so (wait up to 10 seconds)
  if  (T.isLogUsed())  T.getLog_tbl().writeMsg(msg, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(msg);

} // end-method 

/**
 * Initialize the notification environment
 * @param notify_name
 */
protected void initNotify( String notify_name) {
  
  /*
   * Set notify not available. This has no affect on start up.
   *   When a new notify object is being formed, this MAY prevent new
   *   requests coming in before the new notify is verified.   
   */ 
  T.setNotifyUsed(false); 
  
  // set name in base storage
  T.setNotifyName(notify_name);

  // get a new notify table object
  T.setNotify_tbl(new TyNotifyTable(T));   
  
} // end-method

/**
 * Finalize the notify environment for a start up
 */
protected void finiNotifyStartup() {
  
  // notify object
  TyNotifyTable notify = T.getNotify_tbl();
  
  // When not there
  if  (notify == null) {
    
      // no notification function
      doMsg(TyMsg.getMsg(101));
      
      // done
      return;
    
  } // end if
  
  // When good, we have a notify function
  if  (notify.verifyTable()) {

      // set base to use notify               
      T.setNotifyUsed(true); 
      
      // log
      doMsg(TyMsg.getMsg(11) + T.getNotifyName());
    }
    else {
      // log
      doMsg(TyMsg.getMsg(12));
      
  } // endif
  
} // end-method

/**
 * New Notify function
 * @param String function name
 */
protected int newNotify(String one) {
  
  // When missing new notify name, failed
  if  (one == null) return NotifyMissNew; 
  
  // save current in case new failure
  String temp = T.getNotifyName();
  
  // set base to Not notify               
  T.setNotifyUsed(false);
  
  // When no current notify object, get a new notify table object
  if  (T.getNotify_tbl() == null) T.setNotify_tbl(new TyNotifyTable(T)); 
  
  // set name in base class
  T.setNotifyName(one);
  
  // When good, we have a new notify function
  if  (T.getNotify_tbl().verifyTable()) {

      // set base to use notify               
      T.setNotifyUsed(true);
      
      // say what happened
      doMsg(TyMsg.getMsg(59) + T.getNotifyName());
      
      // good new
      return NotifySuccess;
      
  } // endif
      
  // *--- failed ---*
  
  // restore prior notify function name (may be null), but notification not in use
  T.setNotifyName(temp);
  
  // invalid function
  return NotifyInvFunc;  
  
} // end-method

/**
 * Reset a notification that went sour.
 * @return successful or not
 */
protected int resetNotify() {
  
  // When no current notify function, oops
  if  ((T.getNotifyName() == null) ||
       (T.getNotify_tbl() == null))  return NotifyNoCurr;
  
  // When notify doesn't need resetting
  if  (T.isNotifyUsed()) return NotifyNotNec;
  
  // When good, we reset a notify function
  if  (T.getNotify_tbl().verifyTable()) {
        
      // set to use notify               
      T.setNotifyUsed(true);
      
      // say what
      doMsg(TyMsg.getMsg(8301));
      
      // good reset
      return NotifySuccess;
      
  } // endif
      
  // failed to verify
  return NotifyFailVery;
  
} // end-method

/**
 * Stop notification
 * @return service stopped
 */
protected int stopService() {
  
  // set to Not use notify               
  T.setNotifyUsed(false);
  
  // null name in base storage
  T.setNotifyName(null);

  // null notify object
  T.setNotify_tbl(null); 
  
  // no notification function
  doMsg(TyMsg.getMsg(101));
  
  return NotifyStopped;
  
} // end-method
} // end-class
