package com.tymeac.client;

/* 
 * Copyright (c) 2002, 2004 Cooperative Software Systems, Inc. 
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
 * Client access to the Async Request status display
 */
public class TyReqStatusClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;
  
  // security
  private LoginContext loginContext = null;
  
  // pass
  private long tymeac, request; 
  
  // back
  private int back;			

/**
 * Constructor
 */
public TyReqStatusClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyReqStatusClient(TymeacInterface Ty) {

	ti = Ty;
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Cancel an async request
 * @param tymeac long - session id
 * @param request - long request id
 * @return  int
 */
public int cancelReq(long tymeac, long request) {
  
  // save
  this.tymeac = tymeac;
  this.request = request;  

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doCancelReq();     
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
                    doCancelReq();
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
 * Get the status of an async request
 * @param tymeac long - session id
 * @param request - long request id
 * @return  int
 */
public int getStatus(long tymeac, long request) {

	// save
  this.tymeac = tymeac;
  this.request = request;  
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doGetStatus();     
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
                    doGetStatus();
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
    
}// end-method
                                           		
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
private void doCancelReq() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.idStatus1Request(tymeac, request, 2);
      }
      catch (Exception e) {

        // use conn failure
        back = -1;  

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.idStatus1Request(tymeac, request, 2);

  } // endif    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doGetStatus() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.idStatus1Request(tymeac, request, 1);
      }
      catch (Exception e) {

        // use conn failure
        back = -1;  

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.idStatus1Request(tymeac, request, 1);

  } // endif    

} // end-method	
} // end-class
