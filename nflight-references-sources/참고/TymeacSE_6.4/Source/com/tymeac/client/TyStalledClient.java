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
 * Client access to the stalled display
 */
public class TyStalledClient {
		
	// internal comm
	private TyIntnlSvr svr = null; 

	// for internal server
	private TymeacInterface ti = null;
  
  // security
  private LoginContext loginContext = null;
  
  // pass
  private long id;
  
  // back
  private String[] list;
  private int back;   			

/**
 * Constructor
 */
public TyStalledClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyStalledClient(TymeacInterface Ty) {

  ti = Ty;
   
  // security
loginContext = getContext();

} // end-constructor

/**
 * Get a stalled detail entry
 * @param id long - async request id
 * @return String[]
 */
public String[] detail(long id) {
  
  // save
  this.id = id;

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doDetail();     
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
                    doDetail();
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
 * Purge a single async request
 * @param id long - async request id
 * @return int
 */
public int purge(long id) {
  
  // save
  this.id = id;  

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doPurge();     
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
                    doPurge();
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
 */
public String[] refresh() {

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
 * Re-schedule an async request output agent
 * @param id long - async request
 * @return int
 */
public int reSchd(long id) {
  
  // save
  this.id = id;

	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doReSchd();     
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
                    doReSchd();
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
private void doDetail() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        list = ti.stall3Request(id);
      }
      catch (Exception e) {

        // use conn failure
        list = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      list = svr.stall3Request(id);

  } // endif    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doPurge() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.stall2Request(id);
      }
      catch (Exception e) {

        // use conn failure
        back = 0; 

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.stall2Request(id);

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
        list = ti.stall1Request();
      }
      catch (Exception e) {

        // use conn failure
        list = null;  

      } // end-catch
  }
  else {     
      // over to tymeac
      list = svr.stall1Request();

  } // endif    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doReSchd() {    

  // When using the internal server
  if  (ti != null) {

      try {
        // over to tymeac
        back = ti.stall4Request(id);
      }
      catch (Exception e) {

        // use conn failure
        back = 10;  

      } // end-catch
  }
  else {     
      // over to tymeac
      back = svr.stall4Request(id);

  } // endif    

} // end-method
} // end-class
