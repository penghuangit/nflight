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
 * Client access to the threads display
 */
public class TyQueThdClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;
  
  // security
  private LoginContext loginContext = null; 
  
  // pass
  private String que;
  private int nbr;
  
  // back
  private int back = 0;
  private String[] list = null;  
	
/**
 * Constructor
 */
public TyQueThdClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyQueThdClient(TymeacInterface Ty) {

	ti = Ty;
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Disable a single thread
 * @return int
 * @param que java.lang.String - que name
 * @param nbr int - thread number
 */
public int disable(String que, int nbr) {

  // save
  this.que = que;
  this.nbr = nbr;

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doDisable();     
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
                    doDisable();
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
 * Enable all threads
 * @return int
 * @param que java.lang.String - queue name
 */
public int enable(String que) {

	// save
  this.que = que;
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doEnable();     
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
                    doEnable();
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
 * Enable a single thread
 * @return int
 * @param que java.lang.String - que name
 * @param nbr int - thread number
 */
public int enableSpecific(String que, int nbr) {

  // save
  this.que = que;
  this.nbr = nbr;

  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doEnableSpecific();     
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
                    doEnableSpecific();
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
 * Refresh the display
 * @return java.lang.String[]
 * @param que java.lang.String - queue name
 */
public String[] refresh(String que) {

  // save
  this.que = que;

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doRefresh();     
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
                    doRefresh();
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
  return list;
    
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
private void doDisable() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.que4Request(que, nbr);
      }
      catch (Exception e) {

        // use conn failure
        back = 3; 

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.que4Request(que, nbr);

  } // endif    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doEnable() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.que5Request(que);
      }
      catch (Exception e) {

        // use conn failure
        back = 1; 

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.que5Request(que);

  } // endif    

} // end-method

/**
 * This method re-uses the
 *   disable method on the Server interface by passing
 *   the selected thread number as a negative value. Otherwise,
 *   would require adding a method to the TymeacInterface.
 */
private void doEnableSpecific() {
  
  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.que4Request(que, nbr);
      }
      catch (Exception e) {

        // use conn failure
        back = 1; 

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.que4Request(que, nbr);

  } // endif    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doRefresh() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        list = ti.que3Request(que);
      }
      catch (Exception e) {

        // use conn failure
        list = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      list = svr.que3Request(que);

  } // endif 

} // end-method
} // end-class
