package com.tymeac.base;

/*
 * Copyright (c) 2004 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import java.rmi.*;
import java.rmi.server.*;
import com.tymeac.common.*;

/**
 * This is the non-activatable rmi server 
 */
public class RMIServer extends BaseServer {          

/**
 * Constructor
 */        
public RMIServer() {

  // sets the TyBase storage
  super();
  
} // end-constructor  
        
/**
 * main entrypoint - starts the application
 * @param args java.lang.String[]
 */  
public static void main(String[] args) {

  // new server object
  RMIServer server = new RMIServer();
  
  // When start up failed, die
  if  (!server.startUp(args))  System.exit(1);
  
  // Clean up the mess
  System.gc();

  // wait for a shut down    

  // object for monitor
  Object forever = new Object();

  // lock object
  synchronized (forever) {
      
    try {
      // wait forever
      forever.wait();

    } // end-try

    catch (InterruptedException e) {
    } // end-catch
  } // end-sync     

} // end-method

/**
 * rebind with a registry
 * Override this method to add the TymeacLogin 
 */  
protected boolean rebind() {
  
  // variables
  TyVariables TyV = new TyVariables();
  String tymeac_name = TyV.getTymeac();
  String tymeac_url  = TyV.getUrl();

  // When illogical data
  if  ((!TyV.useRMI())  ||
       (tymeac_name == null) || 
       (tymeac_name.length() < 1)) {       
     
      // say what
      msgOut(TyMsg.getMsg(60), 10);          
        
      // get out      
      return false; 
    
  } // endif 
      
  // When using a port
  if  (T.getPort() > -1) {

      // rebinding message with port number
      msgOut(TyMsg.getMsg(70) 
              + TyMsg.getText(30)
              + T.getPort(),
              10);
  }
  else {      
      // rebinding message
      msgOut(TyMsg.getMsg(70), 10);
      
 } // endif
  
  try {    
    // rebind with Registry
    Naming.rebind(tymeac_url + tymeac_name, T.getTi());
    
    // good 
    return true;
      
  } // end-try
  
  catch (Exception e) {

    // say what
    msgOut(TyMsg.getMsg(9) + TyMsg.getText(67), 10);
    msgOut(TyMsg.getText(2) + e, 10);
      
    // get out      
    return false;   
   
  } // end-catch 
  
} // end-method

/**
 * Initialize and rebind with registry
 * @param args
 * @return boolean good or not
 */
protected boolean startUp(String[] args) {

  // When start up failed
  if  (!init(args, Startup.startup_basic)) {
    
      // say ending
      TyBase.printMsg(TyMsg.getMsg(98));
      
      // cancel run
      return false;
      
  } // endif    
 
  // When bind with RMI failed
  if  (!getCompletion()) {

      // say ending
      TyBase.printMsg(TyMsg.getMsg(98));
      
      // cancel run
      return false;

  } // endif 
  
  // set alive with type of server
  setReady(TymeacInfo.NON_ACT_RMI); 
  
  // good startup
  return true;

} // end-method

/**
 *  Create the impl Object and Rebind with the RMIRegistry 
 */   
protected void doExport() {  
  
  // new impl obj
  TymeacStandardImpl impl =  new TymeacStandardImpl(T);
  
  // export Tymeac 
  try {
    // When using a port
    if  (T.getPort() > -1) {
                
        // set global remote object with port
        T.setTi((TymeacInterface)UnicastRemoteObject.exportObject(impl, T.getPort()));
    }
    else {
        // set global remote object, no port
        T.setTi((TymeacInterface)UnicastRemoteObject.exportObject(impl));
      
    }// endif       
  } // end-try
  
  catch (Exception e) {

    // say what
    msgOut(TyMsg.getMsg(9) + TyMsg.getText(67), 10);
    msgOut(TyMsg.getText(2) + e, 10);
      
    // get out      
    return;   
   
  } // end-catch 
  
  // save impl class
  setImpl(impl);
  
  // When rebind worked, good
  if  (rebind()) setGood();
                
} // end-method
} // end-class
