package com.tymeac.jini.base;

/*
 * Created on Jan 25, 2004
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

import net.jini.config.Configuration;

import com.tymeac.base.*;

/**
 * Basic Tymeac security work
 * 
 */
public class JiniSecurityModule {
  
    // Tymeac base
    static TyBase T = null;
        
public static LoginContext getLoginContext(TyBase ty) {

  // save base
  T = ty;
    
  // security login context
  LoginContext loginContext = null;
      
  try {
    // get the context
    loginContext = (LoginContext) ((Configuration)T.getConfigProvider()).getEntry(
         T.getConfigComponent(),  // component from base storage
         TyMsg.getText(121), // name
         LoginContext.class, // type of class
         null);
    
  } // end-try
  
  catch (java.lang.SecurityException e) {
  
      // say what found
      msgOut(TyMsg.getMsg(83) + e, 10);
    
  } // end-catch 
  
  catch (Exception e) {

      // say what found
      msgOut(TyMsg.getMsg(84) + e, 10);
    
  } // end-catch 

  // give back what was found
  return loginContext;

} // end-method          

/**
 * Do a login
 * @param context
 * @return
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
           
  } // end-catch
    
  catch (Exception e) {
      
    // say what found
    msgOut(TyMsg.getMsg(85) + e, 10);                   
                 
  } // end-catch                   

  // not logged in
  return false;
  
} // end-method

/**
 * Write a message to the system.out and tymeac log
 * 
 * @param msg
 * @param time
 */
private static void msgOut(String msg, int time) {
  
  // When printing
  if  (T.getSysout() == 1) {

      // do so
      TyBase.printMsg(msg);

  } // endif  
  
  // When logging, do so
  if  (T.isLogUsed()) {

      // wait up to 10 sec for completion 
      T.getLog_tbl().writeMsg(msg, time);

  } // endif  
  
} // end-method 

} // end-class
