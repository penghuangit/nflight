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

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * This is the catch for exceptions from Queue Threads 
 * @since 6.1
 */
public final class AreaThreadExceptionHandler 
          implements UncaughtExceptionHandler {
  
  private final TyBase T;          // Tymeac base 
  private final AreaThreadMgmt me; // thread management structure

/**
 * Constructor
 * @param tb Tymeac Base
 * @param mgmt Thread mgmt struct
 */  
protected AreaThreadExceptionHandler(TyBase tb, AreaThreadMgmt mgmt) {
  
  T  = tb;   // save base
  me = mgmt; // save mgmt struct
  
} // end-constructor

/**
 * Disable the thread and add a stall array entry for the agent thread. 
 *   Normal queue threads will time out, nothing more we can do here.
 * @param t Thread that had the exception
 * @param e Throwable the exception
 */  
public void uncaughtException(Thread t, Throwable e) {  
    
  // up the thread priority so this (maybe) runs quickly
  t.setPriority(Thread.MAX_PRIORITY);
  
  // thread reference in mgt struct
  AreaBasicThread bThread = me.getThread();
  
  // When no thread reference, probably expunged
  if  (bThread == null) return;
  
  // When thread object in mgt struct Not same as this thread, probably expunged
  if  (!((Thread)bThread).equals(t)) return; 
  
  // When instance numbers don't match, probably expunged
  if  (bThread.getInstance() != me.getInstance()) {
    
      // increment times wrong instance
      me.addWrongInstance();
      
      // done here
      return;
      
  } // endif
  
  // reason for termination
  int reason;
  
  // error msg 
  String msg;
  
  // When a normal queue
  if  (me.isNormalQueue()) {
    
      reason = 6030;
      msg    = TyMsg.getMsg(6901)
                 + t.getName()
                 + TyMsg.getText(179)
                 + e.toString();
  }
  else {
      // Output agent queue    
      reason = 7030;
      msg    = TyMsg.getMsg(7901)
                 + t.getName()
                 + TyMsg.getText(179)
                 + e.toString();
      
      // add a stall table entry (unique number within request in the base thread)
      T.getStall_tbl().addEntry(bThread.getReq().getUni(), reason);
      
  } // endif
  
  // notify adm
  sendNotify(msg);
  
  // print
  printMsg(msg, e);
  
  // log
  writeLog(msg, e);
  
  // thread is no longer alive
  me.setDead(reason);
  
  // increment times caught
  me.addCaught();
      
} // end-method

/**
 * Send a message to the notification queue
 * @param caught error msg
 */
private void sendNotify (String msg )  {
  
  // When not using a notify, return
  if  (!T.isNotifyUsed()) return;
  
  // When this Queue is the Notification Queue, do not invoke
  //   itself.  to avoid a recurrsive error
  if  (T.getNotifyName().compareTo(me.getArea().getName()) == 0) return;
  
  // pass to the notify class
  T.getNotify_tbl().sendMsg(msg);
  
} // end-method

/**
 * Print the message locally
 * @param shut down msg
 * @param Exception
 */
private void printMsg (String msg, Throwable e )  {
  
  // When printing
  if  (T.getSysout() == 1) { 
    
      // print shut down msg
      TyBase.printMsg(msg);
    
      // print the stack trace
      e.printStackTrace();
      
  } // endif  
  
} // end-method

/**
 * Write a message and the stack trace to the log
 * @param String message
 * @param Exception
 */
private void writeLog (String msg, Throwable e  ) {
  
  // When logging, do so
  if  (T.isLogUsed()) {
    
      // log class
      TyLogTable log = T.getLog_tbl();
    
      // write shut down msg, wait 20 seconds
      log.writeMsg(msg, 20);  
      
      // all those stack trace lines
      StackTraceElement[] ele = e.getStackTrace();
  
      // log the stack trace
      for (int i = 0, max = ele.length; i < max; i++) {
        
        log.writeMsg(ele[i].toString(), 20);
        
      } // end-for   
  } // endif
  
} // end-method
} // end-class
