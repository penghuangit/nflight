package com.tymeac.client;

/*
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import javax.security.auth.login.*;
import com.sun.security.auth.callback.DialogCallbackHandler; 

/**
 * This is a basic security module for constructing a LoginContext and
 *   logging in.
 * Two LoginContext constructors are supported: with/without a call back
 *   handler.
 */
public class ClientSecurityModule {
  
  // put your login context here
  private static final String myContext = null; // "com.tymeac.TymeacClient";     

/**
 * Construct a LoginContext with String
 * 
 * @return a basic LoginContext without a call back handler
 */
public static LoginContext getBasicContext() {
                    
  try {
      // When no logon Context, don't use
      if  (myContext == null)  return null;
          
      // get the loginContext
      return new LoginContext(myContext);  
              
    } // end-try
      
  catch (LoginException e) {

      // say what found
      System.out.println("Login Exception= " + e);
      System.exit(1);
  
  } // end-catch 

  catch (java.lang.SecurityException e) {

      // say what found
      System.out.println("Security Exception= " + e);
      System.exit(1);
  
  } // end-catch 

  catch (Exception e) {

      // say what found
      System.out.println("Exception= " + e);
      System.exit(1);
      
  } // end-catch

  // no login context
  return null;
  
} // end-method

/**
 * Construct a LoginContext with String: "com.tymeac.TymeacClient"
 *  and a Dialog call back handler
 * 
 * @return a basic LoginContext with a call back handler
 */
public static LoginContext getCallbackContext() {
          
  try {
      // When no logon Context, don't use
      if  (myContext == null)  return null;
      
      // new logon Context
      return new LoginContext(myContext,
                              new DialogCallbackHandler()); 
              
    } // end-try
      
  catch (LoginException e) {

      // say what found
      System.out.println("Login Exception= " + e);
      System.exit(1);
  
  } // end-catch 

  catch (java.lang.SecurityException e) {

      // say what found
      System.out.println("Security Exception= " + e);
      System.exit(1);
  
  } // end-catch 

  catch (Exception e) {

      // say what found
      System.out.println("Exception= " + e);
      System.exit(1);
      
  } // end-catch

  // no login context
  return null;
  
} // end-method

/**
 * Try to log in using the LoginContext formed above.
 * 
 * @param context
 * @return
 */
public static boolean login(LoginContext context) {
  
  try {
      // login
      context.login();
      
      // good
      return true;
      
  } // end-try
  
  catch (LoginException e) {
     
      // say what found
      System.out.println("LoginException= " + e);          
        
  } // end-catch
    
  catch (Exception e) {
      
      // say what found
      System.out.println("Exception= " + e);                    
                 
  } // end-catch                   

  // not logged in
  return false;
  
} // end-method

} // end-class
