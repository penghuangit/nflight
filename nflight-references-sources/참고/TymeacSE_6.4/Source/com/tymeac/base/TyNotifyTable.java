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

import java.text.*;

/**
 * Notification logic 
 */ 
public final class TyNotifyTable {
  
  // Tymeac base storage
  private final TyBase T;

/**
 * Constructor
 * @param Ty TyBase - base of public fields
 */
public TyNotifyTable (TyBase Ty) {
    
  // Tymeac base storage
  T = Ty;
        
} // end-constructor

/**
 * Used for the new notify maint.
 * @return TyRunTimeElements
 */
protected TyRunTimeElements getElements() {
  
  TyRunTimeElements ele = new TyRunTimeElements();
  
  // When Not in effect
  if  (!T.isNotifyUsed()) {
      
      // say is not in use
      ele.setInUse(false);
      
      return ele;
    
  } // endif
  
  // the notify name or null
  ele.setFuncName(T.getNotifyName());
  
  // whatever
  return ele;
  
} // end-method

/**
 * Send the message to print and log 
 * @param msg
 * @param time
 */
private void msgOut(String msg, int time) {
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(msg);
  
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, time);
  
} // end-method 

/**
 * Send a message
 * @param msg String - the message  
 */ 
public void sendMsg (String msg) {

  // date object    
  java.util.Date D  = new java.util.Date();
  
  // the date format of date: dd Month yy, time: xx:xx:xx PM zone   
  DateFormat DF =
    DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                     DateFormat.LONG);
  
  // the zone is wrong so get the zone at this location
  java.util.TimeZone zone = java.util.TimeZone.getDefault();
  
  // adjust the date format zone to: here
  DF.setTimeZone(zone);                                           
  
  // format the date/time in a string
  String S = DF.format(D);    
  
  // add the msg to the date/time
  String out_msg  = S + " " + msg;

  // into obj
  Object obj = out_msg;  
  
  // the parm passed to TymeacServer   
  TymeacParm TP = new TymeacParm( obj,        // input msg       
                                  T.getNotifyName(), // function name
                                  0,             // no wait
                                  1);            // priority 1
      
  // return obj
  Object[] R = null;
  
  // do an async request
  try {
    R = T.getTi().asyncRequest(TP);

  } catch (Exception e) {   
    
    e.printStackTrace(System.out); 
    
  } // end-catch
  
  // When came back not null And
  //      is a String        And 
  //      String length > 13 And (minimum message is Tymeac xx(nnnn))  
  //      return code = 0000, good
  if  ((R != null)  &&
       (R[0] instanceof String) &&
       (((String) R[0]).length() > 13) &&
       (((String) R[0]).substring(10, 14).compareTo("0000") == 0)) return;
       
  // notify not in use anymore, something is wrong          
  T.setNotifyUsed(false);
  
  // When came back is a Not String
  if  ((R == null)  ||
       (!(R[0] instanceof String))) {
      
      // don't know what came back
      msgOut(TyMsg.getMsg(8001) + "?", 10);
  
      // done here
      return;
  
  } // endif
  
  // try to say what
  msgOut(TyMsg.getMsg(8001) + (String) R[0], 10);
  
} // end-method

/**
 * Verify the queue exists
 * @return boolean
 */
public boolean verifyTable ( ) {
  
  // no message
  String s = "";
  
  // passed obj
  Object o = s;    

  // the parm passed to TymeacServer   
  TymeacParm TP = new TymeacParm( o,  // input msg       
                                  T.getNotifyName(),  // function name
                                  10,             // wait this long
                                  1);             // priority 1
   
  Object[] R = null;

  // do a sync request
  try { 
    R = T.getTi().syncRequest(TP);

  } catch (Exception e) {
    
    // not valid
    return false;
    
  } // end-catch
  
  // back with results
  return validateSync(R);
      
} // end-method

/**
 * Validate a Tymeac return Object array.
 * @param R return object[]
 * @return boolean good or bad
 */
private boolean validateSync(Object[] R) {
  
  // When nothing came back or invalid format, not valid
  if  (R == null) return false;

  // When not a string, not valid
  if  (!(R[0] instanceof String))  return false;
    
  String S = (String) R[0];
    
  // When invalid return message (minimum message is Tymeac xx(nnnn))   
  if  ((S == null) ||         
       (S.length() < 14))  return false;
  
  // When nnnn = 0000, valid
  if  (S.substring(10, 14).compareTo("0000") == 0) return true;
  
  // bad return
  return false;
  
} // end-method
} // end-class
