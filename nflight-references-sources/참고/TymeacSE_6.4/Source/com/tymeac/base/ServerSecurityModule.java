package com.tymeac.base;

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

/**
 * This is a basic security module for constructing a LoginContext and
 *   logging in.
 */
public class ServerSecurityModule {
  
  // Tymeac base
  static TyBase T = null;

/**
 * Construct a LoginContext with the String from the base storage field. This value
 *   comes from the TymeacStartup.class. The start up gets it either from the Tymeac
 *   configuration file when using a DBMS or the TyVariables.class when in stand alone mode. 
 * 
 * @return a basic LoginContext without a call back handler
 */
protected static LoginContext getContext(TyBase Ty) {
      
  // base
  T = Ty;
        
  try {
      // new logon Context
      return new LoginContext(T.getLoginContext());  
              
    } // end-try
      
  catch (LoginException e) {

      // say what found
      msgOut(TyMsg.getMsg(82) + e, 10);
  
  } // end-catch 

  catch (java.lang.SecurityException e) {

      // say what found
      msgOut(TyMsg.getMsg(83) + e, 10);
  
  } // end-catch 

  catch (Exception e) {

      // say what found
      msgOut(TyMsg.getMsg(84) + e, 10);
  
  } // end-catch 
    
  // no login context
  return null;
  
} // end-method

/**
 * Try to log in using the LoginContext formed above. 
 * @param context
 * @return successful or not
 */
protected static boolean login(LoginContext context) {
  
  try {
    // login
    context.login();
    
    // good
    return true;
      
  } // end-try
  
  catch (LoginException e) {
      
    // say what found
    msgOut(TyMsg.getMsg(85) + e, 10);
      
    // bye
    return false;   
           
  } // end-catch
    
  catch (Exception e) {
      
    // say what found
    msgOut(TyMsg.getMsg(85) + e, 10);                   
                 
  } // end-catch                   

  // not logged in
  return false;
  
} // end-method

/**
 * Write a message to the output stream and log.  
 * @param msg
 * @param time
 */
private static void msgOut(String msg, int time) {
  
  // When printing
  if  (T.getSysout() == 1) TyBase.printMsg(msg);
  
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, time);
  
} // end-method 
} // end-class
