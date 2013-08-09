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
 * This is the catch for exceptions from the Monitor. 
 *   Tymeac cannot run in a production environment without a monitor.
 *   When an uncaught exception arises in the Monitor, 
 *   this class brings the Server down in an orderly manor. 
 * @since 6.1
 */
public final class MonitorExceptionHandler 
          implements UncaughtExceptionHandler {
  
  private final TyBase T; // Tymeac base 

/**
 * Constructor
 * @param Tymeac Base
 */  
protected MonitorExceptionHandler(TyBase tb) {
  
  T = tb; // save base
  
} // end-constructor

/**
 * Shut down Tymeac Server
 * @param Thread that had the exception
 * @param Throwable the exception
 */  
public void uncaughtException(Thread t, Throwable e) {
  
  // up the thread priority so this runs (maybe) quickly
  t.setPriority(Thread.MAX_PRIORITY);
  
  // shut down msg
  String msg = TyMsg.getMsg(3901) + e.toString();  
  
  // send notify shut down msg
  sendNotify(msg);
  
  // print shut down msg
  printMsg(msg, e); 
  
  // log shut down msg
  writeLog(msg, e);
  
  // implementation of Tymeac Interface class
  TymeacImpl impl = T.getImpl();
  
  // try shutting down normally (may not return)
  shutNormally(impl);   
  
  // shut down with force
  impl.shutRequest(true);

} // end-method

/**
 * Send a message to the notification queue
 * @param caught error msg
 */
private void sendNotify (String msg )  {
  
  // When using a notify, pass to the notify class
  if  (T.isNotifyUsed()) T.getNotify_tbl().sendMsg(msg);
  
} // end-method

/**
 * Shut down normally. This may not return when shut down completes and the session
 *   is using the shut down thread.
 * @param Tymeac Implementation Class
 */
private void shutNormally (TymeacImpl impl )  {
  
  // try shutting down normally, with wait in-between, three times
  for (int i = 0; i < 3; i++) {
    
    // shut down normally
    impl.shutRequest(false);
    
    // wait 30 seconds  
    try { Thread.sleep(30000);      
    } catch (InterruptedException ex) { } // end-catch   
    
  } // end-for
  
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
