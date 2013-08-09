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
 * Client access to the statistics request display
 */
public class TyStatsClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;	
  
  // security
  private LoginContext loginContext = null;
  
  // back
  private int back;         		

/**
 * Constructor
 */
public TyStatsClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyStatsClient(TymeacInterface Ty) {

   ti = Ty;
 
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Write the statistics
 * @return int
 */
public int writeStats() {

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doWrite();     
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
                    doWrite();
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
private void doWrite() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.stats1Request();
      }
      catch (Exception e) {

        // use conn failure
        back = 0; 

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.stats1Request();

  } // endif

} // end-method
} // end-class
