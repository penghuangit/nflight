package com.tymeac.client;

/* 
 * Copyright (c) 1998 - 2008 Cooperative Software Systems, Inc. 
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
import com.tymeac.base.*;

/**
 * Get a new/reset Runtime stats from a client
 * @since 6.2
 */
public class TyNewRTNotifyClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;	
  
  // security
  private LoginContext loginContext = null;	
  
  // returned value, parm
  private int back = 0, type;
  
  // return elements
  private TyRunTimeElements ele = null;
  
  // passed value
  private String one; 

/**
 * Constructor
 */
public TyNewRTNotifyClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyNewRTNotifyClient(TymeacInterface Ty) {

  ti = Ty;
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Determine security or not
 * @return int what happened
 */
private int doRest() {
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doTymeac();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured access");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doTymeac();
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);
          System.exit(1);                    
                   
      } // end-catch              
  } // endif
    
  // back
  return back; 
  
} // end-method

/**
 * Determine security or not
 * @return TyRunTimeElements what happened
 */
private TyRunTimeElements doRestEle() {
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doTymeacEle();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured access");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doTymeacEle();
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);
          System.exit(1);                    
                   
      } // end-catch              
  } // endif
    
  // back
  return ele; 
  
} // end-method

/**
 * Over to Tymeac
 */
private void doTymeac() {
  
  // When using the internal server
  if  (ti != null) {

      try {
        // over to embedded tymeac
        back = ti.newRunTimeFunctions(type, one, null);
      }
      catch (Exception e) {

        // use conn failure
        back = -1;  

      } // end-catch
  }
  else {     
      try {
        // over to remote tymeac
        back = svr.newRunTimeFunctions(type, one, null);
      }
      catch (Exception e) {

        // use conn failure
        back = -1;  

      } // end-catch
  } // endif   
  
} // end-method

/**
 * Over to Tymeac for elements
 */
private void doTymeacEle() {
  
  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        ele = ti.newRTMaint(SetUpNotify.NotifyMaint);
      }
      catch (Exception e) {

        // use conn failure
        ele = null; 

      } // end-catch
  }
  else {
      try {
        // over to tymeac
        ele = svr.newRTMaint(SetUpNotify.NotifyMaint);
      }
      catch (Exception e) {

        // use conn failure
        ele = null; 

      } // end-catch
  } // endif   
  
} // end-method

/**
 * Get the context for logging in. This is user defined  
 */
private LoginContext getContext() {
         
 // new logon Context without call back handler
 return ClientSecurityModule.getBasicContext();    

} // end-method 

/**
 * get the stats elements
 * @return TyRunTimeElements elements
 */
public TyRunTimeElements getExisting() {
    
  return doRestEle();
  
} // end-method

/**
 * New stats function
 * @param Function Name
 * @return
 */
public int newNotify(String FuncName) {  
    
  if  ((FuncName != null) &&
       (FuncName.length() > 0)) {
            
      one  = FuncName;
  }
  else {
      one = null;
  }
  
  type = SetUpNotify.NotifyNew;
    
  return doRest();                          		
	
} // end-method

/**
 * reset notification
 * @return int what happened
 */
public int resetNotify() {

  // passed
  type = SetUpNotify.NotifyReset;
  one  = null;
    
  return doRest();                            
  
} // end-method

/**
 * stop notification
 * @return int what happened
 */
public int stopNotify() {

  // passed
  type = SetUpNotify.NotifyStop;
  one  = null;
    
  return doRest();                            
  
} // end-method
} // end-class
