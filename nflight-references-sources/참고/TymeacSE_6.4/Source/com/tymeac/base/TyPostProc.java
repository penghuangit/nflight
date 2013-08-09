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
 * Post startup processing. These functions are done in this separate thread.
 * 1. verify the Notify function
 * 2. do the start up functions
 */ 
public final class TyPostProc
    extends Thread {
  
   private final TyBase T;  // base storage
    
/**
 * Constructor
 * @param Ty TyBase
 */ 
public TyPostProc (TyBase Ty) {
  
  super("TymeacPostProc"); // thread name
  
  T = Ty;  // set pointer to base storage
  
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
 * Verify the notification function then exec the startup functions.
 */  
public void run () {
  
  /*
   * This thread may start running before the Tymeac system is fully
   *   functional. That is, before the user functions are available.
   *
   * Start up is in two levels:
   *   1. The low level where the arrays/objects etc. are built and
   *        this thread is started.
   *   2. The high level which first calls the low level and finishes the
   *        start up according to what type of start up is necessary (RMI,
   *        Jini, Internal.) It then says: Tymeac is available.
   *
   * This thread must wait for the full availability before continuing.
   *
   */

  // max seconds we will wait for Tymeac to finish building. This could be
  //  a problem during testing and may need adjusting therein.
  int max_wait = 5;

  // wait for the server to be available
  for (;;) {

    // When ready to advance, go for it
    if (T.getEndit() == TymeacInfo.AVAILABLE) break;

    // When not being built, get out
    if (T.getEndit() != TymeacInfo.INITIALIZATION) return;

    // could be the system isn't finished building
    try {
      Thread.sleep(1000); // wait one second

    } // end-try
    catch (InterruptedException e) {} // ignored

    // When waited long enough, get out
    if (--max_wait < 1) return;

  } // end-for

  // finalize the notify setup
  new SetUpNotify(T).finiNotifyStartup();
      
  // When no start up functions, done
  if  (T.getStart_array() == null) return;

  // array
  String[] startup = T.getStart_array();

  // null old, helps with gc
  T.setStart_array(null);

  // length of the array
  int nbr = startup.length;
  
  // the parm passed to TymeacServer   
  TymeacParm TP = new TymeacParm( null, // no input        
                                  null, // function name
                                  0,    // no wait
                                  1);   // priority 1

  // return array from Tymeac
  Object[] R = null;
  
  // Tymeac Server
  TymeacInterface ti = T.getTi();
  
  // do all the functions
  for (int i = 0; i < nbr; i++) {

    // set new function name
    TP.setFuncname(startup[i]); 

    try {
      // do an async request for each function
      R = ti.asyncRequest(TP);      

    } catch (Exception e) {} // unhandled exceptions
    
    // validate return message from Tymeac
    validateAsync(startup[i], R);    
    
  } // end-for
  
} // end-method

/**
 * Validate a Tymeac return Object array.
 * @param R return object[]
 */
private void validateAsync(String ty_func, Object[] R) {
  
  // When nothing came back or invalid format, not valid
  if  ((R == null) || (!(R[0] instanceof String))) {
    
      // say bad
      doMsg(TyMsg.getMsg(63) + ty_func);        
    
     return;
     
  } // endif
    
  String S = (String) R[0];
    
  // When invalid return message (minimum message is Tymeac xx(nnnn))   
  if  ((S == null) ||         
       (S.length() < 14))  return;
  
  // When nnnn = 0000, valid
  if  (S.substring(10, 14).compareTo("0000") == 0) return;
  
  // say what found
  doMsg(TyMsg.getMsg(62) 
        + S.substring(10, 14)
        + TyMsg.getText(180)
        + ty_func);
  
  // bad return
  return;
  
} // end-method
} // end-class
