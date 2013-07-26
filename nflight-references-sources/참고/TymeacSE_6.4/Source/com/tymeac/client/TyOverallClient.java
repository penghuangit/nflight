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
 * Client access to the Overall display
 */
public class TyOverallClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;
  
  // login
  private LoginContext loginContext = null;
  
  // return data
  private TyOverallObj back = null;			

/**
 * Constructor
 */
public TyOverallClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyOverallClient(TymeacInterface Ty) {

	ti = Ty;
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Refresh both the top and bottom displays
 * @return TyOverObj 
 */
public TyOverallObj  refreshBoth() {

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doBoth();     
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
                    doBoth();
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
 * Refresh just the top display 
 * @return TyOverallObj 
 */
public TyOverallObj  refreshTop() {

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doTop();     
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
                    doTop();
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
 * Get the context for logging in. This is user defined  
 */
private LoginContext getContext() {
         
  // new logon Context without call back handler
  return ClientSecurityModule.getBasicContext();    

} // end-method 

/**
 * Executed as privileged or not as privileged
 */
private void doBoth() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.overallRequest(2);
      }
      catch (Exception e) {

        // use conn failure
        back = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.overallRequest(2);

  } // endif    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doTop() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.overallRequest(1);
      }
      catch (Exception e) {

        // use conn failure
        back = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.overallRequest(1);

  } // endif      

} // end-method
} // end-class
