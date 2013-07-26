package com.tymeac.base;

/* 
 * Copyright (c) 2004 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

/**
 * Tymeac Server base class 
 */
public abstract class BaseServer {
  
// protected  
  // The base for all Tymeac processing    
  protected TyBase T = null;
          
// private  
  // temporary start up class 
  private Startup su = null; 
  
  // local success
  private boolean goodContinue = false;      
  
  // the remote impl
  private Object implClass = null; 
  
  // general return code
  private int rc = -1;
 
/**
 * Constructor
 */  
protected BaseServer () {

  T = new TyBase(); // base storage
    
} // end-constructor

/**
 * Create the start up class.  
 */
private void createStartup(int type) {
  
  // start up class with type
  su = new Startup(T, type);  
  
} // end-method  

/**
 * Return comp code
 * @return
 */
protected boolean getCompletion() { return goodContinue; } // end-method  

/**
 * Return impl class cast to TymeacInterface
 * @return TymeacInterface
 */
protected TymeacInterface getImpl() { return (TymeacInterface)implClass; } // end-method

/**
 * Get the security login context
 * @return LoginContext
 */
protected LoginContext getLoginContext() {
  
  // find the login context string and save in common strg
  su.determineLoginContext();
    
  // When using security, get the context or null
  return (T.getLoginContext() != null)?
        ServerSecurityModule.getContext(T) : null; 
  
} // end-method  

/**
 * Get the Startup object
 * @return Startup object
 */
protected Startup getStartUp() { return su; } // end-method

/**
 * Initialize the server. 
 *  You should not need to override this for non-standard servers. 
 *  Simply override the lesser methods this method calls.
 * 
 * @param args command line arguments
 * @param type type of server
 * @return ok or ng
 */   
protected boolean init(String[] args, int type) {
 
  // start up class as type passed
  createStartup(type);
  
  // When Not successful parsing args, get out
  if  (!parseArgs(args)) return false;
  
  // When Not successful determining run mode, get out
  if  (!determineRunMode()) return false;
  
  // *--- three override methods for individual override needs
  
  // When Not successful doing an override, get out
  if  (!overrideMe1()) return false;
  
  // When Not successful doing an override, get out
  if  (!overrideMe2()) return false;
  
  // When Not successful doing an override, get out
  if  (!overrideMe3()) return false;
      
  // security login context
  LoginContext loginContext = getLoginContext();
      
  // When no login context   
  if (loginContext == null) {
    
      // say UNSECURE
      msgOut(TyMsg.getMsg(80), 10);

      // non-secure
      continueStartup();     
  } 
  else {    
      // try to login
      if  (ServerSecurityModule.login(loginContext)) {
        
          // say secure
          msgOut(TyMsg.getMsg(81), 10);
            
          try {
            // do rmi stuff as privileged
            Subject.doAsPrivileged(
                loginContext.getSubject(),
                new PrivilegedExceptionAction<Object>() {
                    public Object run() throws Exception {
                        continueStartup();
                        return null;
                    } // end-method
                }, // end-PEA
                null);
            
          } // end-try   
      
          catch (PrivilegedActionException e) {
        
            // say what found
            msgOut(TyMsg.getMsg(86) + e, 10);
        
            // bye
            return false;
                     
          } // end-catch
      }
      else {
          // failed login
          msgOut(TyMsg.getMsg(91), 10);
          return false;
                                  
      } // endif
    } // endif
  
  // When something went wrong, get out
  if  (rc != 0) return false;
  
  // good
  return true;   
  
} // end-method

/**
 * contine building server in secure/unsecure mode
 */
private void continueStartup() {
  
  try {
    // do the rest of the start up
    rc = su.doRest();

  } // end-try

  // uncaught in startup
  catch (Throwable e) {

    // say what found
    TyBase.printMsg(TyMsg.getMsg(97) + e.toString());
    
    // say bad rc
    rc = 1;

  } // end-catch

  // When something went wrong, get out
  if  (rc != 0) return;  

  // all done with start up class, can be gc'ed
  su = null;
  
  // abstract method for implementation classes
  doExport();
  
  // When successful, start the Post Start Up thread
  if  (getCompletion()) new TyPostProc(T).start();
  
} // end-method  

/**
 * Call the start up determine run mode routine 
 * @return ok or ng
 */   
private boolean determineRunMode() {
  
  try {
    // determine stand-alone or DBMS
    return su.determineRunMode();

  } // end-try

  // uncaught in above
  catch (Throwable e) {

    // say what found
    TyBase.printMsg(TyMsg.getMsg(97) + e);
    
    return false;

  } // end-catch
  
} // end-method
 
/**
 * Create the impl Object and send the proxy to the whatever.
 * @return boolean 
 */
protected abstract void doExport();

/**
 * Override this method for unique requirements 
 * @return ok or ng
 */   
protected boolean overrideMe1() { return true; } // end-method

/**
 * Override this method for unique requirements 
 * @return ok or ng
 */   
protected boolean overrideMe2() { return true; } // end-method

/**
 * Override this method for unique requirements 
 * @return ok or ng
 */   
protected boolean overrideMe3() { return true; } // end-method

/**
 * Call the start up parse args routine 
 * @param args command line arguments
 * @return ok or ng
 */   
private boolean parseArgs(String[] args) {
  
  try {
    // look at the command line args
    rc = su.parseArgs(args);

  } // end-try

  // uncaught in above
  catch (Throwable e) {

    // say what found
    TyBase.printMsg(TyMsg.getMsg(97) + e.toString());
    
    return false;

  } // end-catch
  
  // return code 
  return (rc != 0)? false : true;  

} // end-method

/**
 * Say a good export
 */
protected void setGood() { goodContinue = true; } // end-method

/**
 * Save the impl class so it does not get gc'ed
 * @param obj
 */
protected void setImpl(Object obj) { implClass = obj; } // end-method  

/**
 * set ready for clients
 * @param type
 */
protected void setReady(int type) {
  
  // Server is available
  T.setEndit(TymeacInfo.AVAILABLE);

  // Server is this type
  T.setStartType(type);
  
  // *--- all done message ---*   
  msgOut(TyMsg.getMsg(99) 
      + "v"
      + T.getVersion()
      + TyMsg.getText(174)
      + T.getStart_time(), 10);      

} // end-method 

/**
 * Write a log and print
 * @param msg
 * @param time
 */
protected void msgOut(String msg, int time) {
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(msg);
  
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, time);
  
} // end-method  
} // end-class