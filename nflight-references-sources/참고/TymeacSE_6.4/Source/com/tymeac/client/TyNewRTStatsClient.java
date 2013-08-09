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
public class TyNewRTStatsClient {
		
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
  private String one, two; 

/**
 * Constructor
 */
public TyNewRTStatsClient() {

  // get the internal svr comm
  svr = new TyIntnlSvr();
  
  // security
  loginContext = getContext();

} // end-constructor

/**
 * Constructor
 * @param Ty com.tymeac.base.TymeacInterface
 */
public TyNewRTStatsClient(TymeacInterface Ty) {

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
        back = ti.newRunTimeFunctions(type, one, two);
      }
      catch (Exception e) {

        // use conn failure
        back = -1;  

      } // end-catch
  }
  else {     
      try {
        // over to remote tymeac
        back = svr.newRunTimeFunctions(type, one, two);
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
        ele = ti.newRTMaint(SetUpStats.StatsMaint);
      }
      catch (Exception e) {

        // use conn failure
        ele = null; 

      } // end-catch
  }
  else {
      try {
        // over to tymeac
        ele = svr.newRTMaint(SetUpStats.StatsMaint);
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
 * @param DBMS
 * @param Dir
 * @param File name
 * @param Alt
 * @return
 */
public int newStats(String DBMS, 
                    String Dir, 
                    String FileName, 
                    String Alt) {
  
  while (true) {
    
    if  ((DBMS != null) &&
         (DBMS.length() > 0)) {
      
        type = SetUpStats.StatsNewDBMS;
        one  = DBMS;
        two  = null;
        break;
      
    } // endif
    
    if  ((Dir != null) &&
         (Dir.length() > 0)) {
      
        type = SetUpStats.StatsNewFile;
        one  = Dir;
        two  = FileName;
        break;
      
    } // endif
    
    if  ((FileName != null) &&
         (FileName.length() > 0)) {
      
        type = SetUpStats.StatsNewFile;
        one  = null;
        two  = FileName;
        break;
      
    } // endif
    
    type = SetUpStats.StatsNewAlt;
    one  = Alt;
    two  = null;
    break;    
    
  } // end-while  
  
  return doRest();                          		
	
} // end-method

/**
 * reset stats
 * @return int what happened
 */
public int resetStats() {

  // passed
  type = SetUpStats.StatsReset;
  one  = null;
  two  = null; 
    
  return doRest();                            
  
} // end-method

/**
 * stop stats
 * @return int what happened
 */
public int stopStats() {

  // passed
  type = SetUpStats.StatsStop;
  one  = null;
  two  = null; 
    
  return doRest();                            
  
} // end-method
} // end-class
