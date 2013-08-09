package com.tymeac.client;

/* 
 * Copyright (c) 1998 - 2004 Cooperative Software Systems, Inc. 
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
 * This is the internal display of function data
 */
public class TyFuncDataClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;
  
  // passed
  private String func_name = null;
  
  // returned
  private String[] from = null;			
  
  // security
  private LoginContext loginContext = null;

/**
 * Constructor
 */
public TyFuncDataClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor for using the internal tymeac server
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyFuncDataClient(TymeacInterface Ty) {

  ti = Ty;

  // security
  loginContext = getContext();

} // end-constructor

/**
 * Do a refresh of the function data
 * @param f_name String - partial or full function name
 * @return String[]
 */
public String[] refresh(String f_name) {

  // save
  func_name = f_name;
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      pushedRefresh();     
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
                    pushedRefresh();
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
    
    // send back
    return from;                                           		
	
} // end-method

 /**
 * Get the context for logging in. This is user defined
 */
private LoginContext getContext() {
         
 // new logon Context without call back handler
 return ClientSecurityModule.getBasicContext();    

} // end-method 

/**
 * Executed as privileged or not as privileged
 */
private void pushedRefresh() {    

  // When using the internal server
  if  (ti != null) {

      try {
        from = ti.func1Request(func_name);
      }
      catch (java.rmi.RemoteException e) {}
  }
  else {     
      // over to tymeac
      from = (svr.func1Request(func_name)); 

  } // endif

} // end-method
} // end-class
