package com.tymeac.demo;

/* 
 * Copyright (c) 2002 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import java.awt.*;
import java.util.*;

import com.tymeac.base.*;

 /** 
 *  Tymeac demonstration system
 *    bean in support of Frame class TyDemoAWTCancel
 */  
public final class TyDemoAWTCancelBean {
  
  private Random random; // to gen cancel word
  private long cancel_word; // cancel word for request
	private TySvrComm svr = null; // comm module 
  private LoginContext loginContext = null;	// for security check  
  private TextField Result; // reference to AWT GUI result field
  
  private TyDemoAWTCancelThread1 send_thread   = null;
  private TyDemoAWTCancelThread2 cancel_thread = null;

/**
 * Constructor
 * @param args
 */
public TyDemoAWTCancelBean(String[] args) {
  
  /*
   * There is one command line option
   *   -secure
   * When this option is present, we make a call
   * to get the login context. 
   */   
  
  // When 1st arg is for security 
  if  ((args.length > 0) &&
       (args[0].equalsIgnoreCase("-secure"))) {
         
      // see if there is a login module
      loginContext = SecurityModule.getBasicContext();
      
  } // endif 
  
  // When good context
  if  (loginContext != null) {
        
      // When NOT a good login
      if  (!SecurityModule.login(loginContext)) {
        
          // no security
          loginContext = null;
          
          System.out.println("Login failed");
          
          System.exit(1);
          
      } // endif      
  } // endif    
  
  // get a new comm obj
  svr = new TySvrComm();
  
  // When failed,
  if  (svr == null) {
      
       System.out.println("TySvrComm failure");          
       System.exit(1);
    
  } // endif
  
  // for generating the cancel word 
  random = new Random();
  
  // start the threads
  send_thread = new TyDemoAWTCancelThread1(svr);
  send_thread.start();
  
  cancel_thread = new TyDemoAWTCancelThread2(svr);
  cancel_thread.start();
  
} // end-constructor 

/** 
 *  Cancel button on GUI
 */ 
public void cancelButton() {    
 
  // wake up the cancel thread
  cancel_thread.wakeUp(new CancelParm(cancel_word));  
  
} // end-method

/** 
 * Tymeac demonstration system     
 *   Pass a new request to the Tymeac thread 
 * 
 * @parm TextField on the AWT GUI
 */ 
private void goTymeac (TextField result) {
  
  send_thread.wakeUp(cancel_word, result);
       
} // end-method

/** 
 *  Tymeac demonstration system.
 *    Call the Tymeac function.
 *    Move the results to the TextFields.
 * @param Result javax.swing.JTextField
 */ 
public void sendButton(TextField result) { 
  
  Result = result;
  
  cancel_word = random.nextLong(); // new cancel word
    
    // When not using security     
  if (loginContext == null) {
  
      // say what
      System.out.println("Using INSECURE client");
      
      // non-secure
      goTymeac(Result);     
  } 
  else {    
      try {
        // say what
        System.out.println("Using secure client");
      
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    goTymeac(Result);
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try   
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);                    
                   
      } // end-catch
      
      catch (Exception e) {
        
          // say what found
          System.out.println("Exception= " + e);                    
                   
      } // end-catch                 
    } // endif 
  
} // end-method
} // end-class
