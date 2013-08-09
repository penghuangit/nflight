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
 * Client access to the Que Data display
 */
public class TyQueDataClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;
  
  // return data
  private int back = 0;
  private TyQueElements ele = null;	
  
  // pass
  private String que = null;
  
  // security
  private LoginContext loginContext = null;		

/**
 * Constructor
 */
public TyQueDataClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyQueDataClient(TymeacInterface Ty) {

	ti = Ty;
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Get the que data elements
 * @param que String - queue name
 * @return TyQueElements
 */
public TyQueElements importElements(String que) {

		// pass
    this.que = que;
    ele = null;
    
    // When not using security     
    if (loginContext == null) {
          System.out.println("UNSECURED access"); 
        // non-secure
        doImport();     
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
                      doImport();
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
 * Update the queue elements
 * @param data TyQueElements - changed data
 * @return int
 */
public int updateElements(TyQueElements data) {
  
  // pass
  ele = data;

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doUpdate();     
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
                    doUpdate();
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
private void doImport() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        ele = ti.que1Request(que);
      }
      catch (Exception e) {

        // use conn failure
        ele = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      ele = svr.que1Request(que);

  } // endif

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doUpdate() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.que2Request(ele);
      }
      catch (Exception e) {

        // use conn failure
        back = 0; 

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.que2Request(ele);

  } // endif    

} // end-method
} // end-class
