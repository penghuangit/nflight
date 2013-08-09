package com.tymeac.client;

/* 
 * Copyright (c) 1998 - 2011 Cooperative Software Systems, Inc. 
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
 * Client access to the wait list display
 */
public class TyAltSvrClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;	
  
  // security
  private LoginContext loginContext = null;
  
  // pass
  private TyAltSvrElements ele;
  
  // back
  private TyAltSvrElements back;
  
/**
 * Constructor
 */
public TyAltSvrClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyAltSvrClient(TymeacInterface Ty) {

	ti = Ty;
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * update the elements
 * @return TyAltSvrElements
 * @param ele TyAltSvrElements
 */
public TyAltSvrElements alterElements(TyAltSvrElements ele) {

  // save
  this.ele = ele;

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doAlter();     
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
                    doAlter();
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
 * import the elements
 * @return TyAltSvrElements
 * @param ele TyAltSvrElements
 */
public TyAltSvrElements importElements(TyAltSvrElements ele) {

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
        // over to tymeac for read only
        back = ti.alterSvrOptions(null);
      }
      catch (Exception e) {

        // use conn failure
        back = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.alterSvrOptions(null);

  } // endif    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doAlter() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac for update
        back = ti.alterSvrOptions(ele);
      }
      catch (Exception e) {

        // use conn failure
        back = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.alterSvrOptions(ele);

  } // endif    

} // end-method
} // end-class
