package com.tymeac.base;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.activation.*;
import java.io.*; 
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

/**
 * Tymeac Activation Implementation
 * 
 */ 
public class TymeacActivationImpl
  extends    Activatable
  implements TymeacInterface {  
      
    private static final long serialVersionUID = 7074810102029070057L;      

    // base of shared storage
    protected TyBase Ty;
    
    // which constructor was used
    protected int type = 0;
      public static final int BASIC = 1; // base constructor
      public static final int CSF   = 2; // csf  constructor  
  
    // real impl class
    protected TymeacImpl impl; 
    
    // start up class 
    protected Startup su = null;
    
    // local 
    protected boolean goodContinue = false;
    protected boolean goodInit     = false;   
   
/**
 * Constructor 1, for standard activation
 * @param id ActivationID
 * @param data MarshalledObject
 * @exception java.rmi.RemoteException The exception description.
 * @exception Throwable.
 */ 
public TymeacActivationImpl (ActivationID id, MarshalledObject data)
    throws  RemoteException, 
            Throwable {

  // this act-id and default port
  super(id, 0); 
  
  // set type of start up
  type = TymeacActivationImpl.BASIC;
  
  // do the start up stuff
  startUp(data);

} // end-constructor 

/**
 * Constructor 2, for csf activation
 * @param id ActivatinID
 * @param csf RMIClientSocketFactory
 * @param ssf RMIServerSocketFactory
 * @param data MarshalledObject
 * @exception java.rmi.RemoteException The exception description.
 */ 
public TymeacActivationImpl (ActivationID id, 
                             RMIClientSocketFactory csf,
                             RMIServerSocketFactory ssf,             
                             MarshalledObject data)
          throws  RemoteException, 
                  Throwable {

  // act_id, port 0 for any, client sf, server sf
  super(id, 0, csf, ssf); 
  
  // set type of start up
  type = TymeacActivationImpl.CSF;

  // do the start up stuff
  startUp(data);

} // end-constructor

/**
 * Create the start up class. Override for each individual start up type
 * 
 */
private void createStartup(int type) {
  
  // start up class with type
  su = new Startup(Ty, type);  
  
} // end-method 

/**
 * Call the start up determine run mode routine
 * 
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
 * Get the Login Context object
 * @return LoginContext
 */
protected LoginContext getLoginContext() {
  
  // find the login context
  su.determineLoginContext();
    
  // When using security, get the context or null
  if  (Ty.getLoginContext() != null) return ServerSecurityModule.getContext(Ty); 
  
  // none
  return null;
  
} // end-method  

/**
 * Call the start up parse args routine
 * 
 * @param args command line arguments
 * @return ok or ng
 */   
private boolean parseArgs(String[] args) {
  
  int rc = 0;
  
  try {
    // look at the command line args
    rc = su.parseArgs(args);

  } // end-try

  // uncaught in above
  catch (Throwable e) {

    // say what found
    TyBase.printMsg(TyMsg.getMsg(97) + e);
    
    return false;

  } // end-catch
  
  // When something went wrong, ng
  if  (rc != 0) return false;
  
  return true;

} // end-method

/**
 * Override this method for unique requirements
 * 
 * @return ok or ng
 */   
public boolean overrideMe1() { return true; } // end-method

/**
 * Override this method for unique requirements
 * 
 * @return ok or ng
 */   
public boolean overrideMe2() { return true; } // end-method

/**
 * Override this method for unique requirements
 * 
 * @return ok or ng
 */   
public boolean overrideMe3() { return true; } // end-method

/**
 * Start up the server
 * @param data
 * @throws Throwable
 */
private void startUp(MarshalledObject data)
              throws Throwable {

   // the base for all Tymeac processing
  Ty = new TyBase(); 
  
  // save the activation id
  Ty.setTyActivation(this.getID());                

  String[] args = null;
         
  // see if any passed data
  try {
    // When something there, get the string[]
    if  (data != null) args = (String[]) data.get();
    
  } // end-try        
  
  catch (IOException e) {}
  catch (ClassNotFoundException e) {}
  
  // When failed initialize
  if  (!init(args)) { 
  
     // say failed startup
     TyBase.printMsg(TyMsg.getMsg(98));
     
     // throw error
     throw new Throwable(TyMsg.getMsg(98)); 
     
  } // endif
  
  // When failed start up
  if  (!goodContinue) { 
  
     // say failed startup
     TyBase.printMsg(TyMsg.getMsg(98));
     
     // throw error
     throw new Throwable(TyMsg.getMsg(98)); 
     
  } // endif 
  
  // Server is available
  Ty.setEndit(TymeacInfo.AVAILABLE);

} // end-method

/**
 * Initialize the server. OVERRIDE this method for a differenct type
 *   of initialization  
 * @param args command line arguments
 * @return ok or ng
 */  
protected boolean init(String[] args) {
 
  // start up class as activatable
  createStartup(Startup.startup_activatable);
  
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
      if  (!ServerSecurityModule.login(loginContext)) {
        
          // say login failed
          msgOut(TyMsg.getMsg(91), 10);
          
          // bye
          return false;
      } // endif          
        
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
          msgOut(TyMsg.getMsg(86) + e.toString(), 10);
      
          // bye
          return false;
                 
      } // end-catch
    } // endif
    
    // When basic constructor was used
    if  (type == TymeacActivationImpl.BASIC) {
        
        // Server is activation type
        Ty.setStartType(TymeacInfo.ACT_RMI); 

    }
    else {
        // Server is csf activation type
        Ty.setStartType(TymeacInfo.ACT_CSF);       
      
    } // endif        
    
    // good
    return true;   
  
} // end-method

/**
 * Continue startup in secure or unsecure state
 */
protected void continueStartup() {
  
  int rc = -1;
  
  try {
    // finish startup
    rc = su.doRest();

  } // end-try

  // uncaught in startup
  catch (Throwable e) {

    // say what found
    TyBase.printMsg(TyMsg.getMsg(97) + e.toString());

  } // end-catch

  // When something went wrong, get out
  if  (rc != 0) return;  

  // all done with this
  su = null; 
  
  // set myself as the remote object
  Ty.setTi(this);

  // set the real impl class here
  impl = Ty.getImpl();
  
  // When a notification or startup functions necessary
  if  ((Ty.getNotify_tbl()  != null) ||
       (Ty.getStart_array() != null)) { 

      // start the verification thread
      new TyPostProc(Ty).start();

  } // endif
  
  // all done message 
  msgOut(TyMsg.getMsg(99) + Ty.getStart_time(), 10);
  
  // good start up
  goodContinue = true; 
  
} // end-method

/**
 * Write a log and print
 * @param msg
 * @param time
 */
public void msgOut(String msg, int time) {
  
  // When printing
  if  (Ty.getSysout() == 1)  TyBase.printMsg(msg);
  
  // When logging, do so
  if  (Ty.isLogUsed()) Ty.getLog_tbl().writeMsg(msg, time);
  
} // end-method 

/** 
 * *--- Implementation of Tymeac Interface ---* 
 */

/**
 * Alter server options
 * @param opts
 * @return
 */
public TyAltSvrElements alterSvrOptions( TyAltSvrElements opts) 
        throws RemoteException {
  
  // off to real impl
  return impl.alterSvrOptions(opts);
  
} // end-method

/**
 * @return Object[]
 * @param Req TymeacParm
 * @exception java.rmi.RemoteException The exception description.
 */ 
public Object[] asyncRequest (TymeacParm Req )
       throws RemoteException {
                    
  // off to real impl
  return impl.asyncRequest(Req);                                      
  
} // end-method

/** 
 * @return int
 * @param forCancel com.tymeac.base.CancelParm
 * @exception java.rmi.RemoteException The exception description.
 */
public int cancelSyncReq(CancelParm forCancel) 
        throws java.rmi.RemoteException {

  // off to real impl
  return impl.cancelSyncReq(forCancel);                          
  
} // end - method 

/**
 * @param req TyParm
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */    
public String  diagnoseRequest (TyParm req) 
    throws RemoteException {  
  
  // off to real impl
  return impl.diagnoseRequest(req);   
  
} // end-method

/**
 * @param func String
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String[] func1Request (String func) 
            throws RemoteException {
                          
  // off to real impl
  return impl.func1Request(func);                          
  
} // end-method

/**
 * @param id long
 * @param req long
 * @param action int
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int idStatus1Request (long id, long req, int action) 
   throws RemoteException {
                      
  // off to real impl
  return impl.idStatus1Request(id, req, action);      
  
} // end-method

/**
 * @param que String
 * @param pa_class String
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int newCopyRequest (String que, String pa_class) 
   throws RemoteException {
  
  // off to real impl
  return impl.newCopyRequest(que, pa_class);
   
} // end-method

/**
 * Reset or set a New Runtime Function (notify, log or stats)
 * @param type of request
 * @param one String when DBMS or Alt
 * @param two String when file or directory
 * @return what happened
 */
public int newRunTimeFunctions(int type, String one, String two) {
  
  // off to real impl
  return impl.newRunTimeFunctions(type, one, two);
   
} // end-method 

/**
 * New run time functions maintenance
 * @param type of elements needed
 * @return elements
 */
public TyRunTimeElements newRTMaint(int type) {
  
  // off to real impl
  return impl.newRTMaint(type);
  
} // end-method

/**
 * @return TyOverallObj
 * @param req_type int
 * @exception java.rmi.RemoteException The exception description.
 */
public TyOverallObj overallRequest(int req_type) 
        throws RemoteException {

  // off to real impl
  return impl.overallRequest(req_type);  

} // end-method

/**
 * @param que String
 * @return TyQueElements
 * @exception java.rmi.RemoteException The exception description.
 */  
public TyQueElements que1Request (String que) 
   throws RemoteException {
  
  // off to real impl
  return impl.que1Request(que); 
    
} // end-method

/**
 * @param ele TyQueElements
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */    
public int que2Request (TyQueElements ele) 
   throws RemoteException {
   
   // off to real impl
  return impl.que2Request(ele);
    
} // end-method

/**
 * @param que String
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */   
public String[] que3Request (String que) 
            throws RemoteException {

  // off to real impl
  return impl.que3Request(que);    
  
} // end-method

/**
 * @param que String
 * @param nbr int
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */     
public int que4Request (String que, int nbr ) 
   throws RemoteException {

  // off to real impl
  return impl.que4Request(que, nbr);    
  
} // end-method

/**
 * @param que String
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */    
public int que5Request (String que) 
   throws RemoteException {  

  // off to real impl
  return impl.que5Request(que);
    
} // end-method

/**
 * @param que String
 * @return TyWLElements[]
 * @exception java.rmi.RemoteException The exception description.
 */   
public TyWLElements[] que6Request (String que) 
   throws RemoteException {
 
  // off to real impl
  return impl.que6Request(que);    
       
} // end-method 

/**
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String shutRequest () 
          throws RemoteException {

  // off to real impl
  return impl.shutRequest(false);  
  
} // end-method

/**
 *
 * Shut down Request with force indicator.
 *
 * @param force shutdown
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */
 
public String shutRequest (boolean force) 
                  throws RemoteException {
  

    // off to real impl
    return impl.shutRequest(force);
    
} // end-method   

/**
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */  
public String[] stall1Request () 
   throws RemoteException {

  // off to real impl
  return impl.stall1Request();         
  
} // end-method

/**
 * @param req long
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */       
public int stall2Request (long req) 
   throws RemoteException {

  // off to real impl
  return impl.stall2Request(req); 
  
} // end-method

/**
 * @param req long
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */     
public String[] stall3Request (long req) 
   throws RemoteException {
   
  // off to real impl
  return impl.stall3Request(req);    

} // end-method

/**
 * @param req long
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */ 
public int stall4Request (long req) 
   throws RemoteException {
  
  // off to real impl
  return impl.stall4Request(req);
    
} // end-method

/**
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */   
public int stats1Request () 
          throws RemoteException {

  // off to real impl
  return impl.stats1Request();  
  
} // end-method

/**
 * @param Req TymeacParm
 * @return Object[]
 * @exception java.rmi.RemoteException The exception description.
 */ 
public Object[] syncRequest (TymeacParm Req) 
            throws RemoteException {

  // off to real impl
  return impl.syncRequest(Req);       
    
} // end-method 
} // end-class
