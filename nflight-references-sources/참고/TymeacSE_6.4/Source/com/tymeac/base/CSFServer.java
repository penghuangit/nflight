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
package com.tymeac.base;

import com.tymeac.demo.*;

import java.rmi.*;
import java.rmi.server.*;
import com.tymeac.common.*;

/**
 * This is the non-activatable rmi server using custom socket factories
 */
public class CSFServer extends BaseServer {
        
  // references to socket factory objects
  protected RMIClientSocketFactory client = null;
  protected RMIServerSocketFactory server = null;        

/**
 * Constructor
 */        
protected CSFServer() {

  // sets the TyBase storage
  super();
  
} // end-constructor  
        
/**
 * main entrypoint - starts the application
 * @param args java.lang.String[]
 */  
public static void main(String[] args) {

  // new server object
  CSFServer server = new CSFServer(); 
  
  // When start up failed, kill
  if  (!server.startUp(args)) System.exit(1);
      
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
 * Create the client and server socket factories. This is your code.
 * @return boolean did it or not
 */
private boolean createFactory() {
  
  try {
    // new client factory
    client = new TyDemoClientSocketFactory();  
    
    // new server factory
    server = new TyDemoServerSocketFactory();

  } // end-try
  
  catch (Exception e) {

    // say what
    msgOut(TyMsg.getMsg(90) + e. toString(), 10);
      
    // get out      
    return false;   
   
  } // end-catch 

  // good
  return true;

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
      
  } // end-try
  
  catch (Exception e) {

    // say what
    msgOut(TyMsg.getMsg(9) + TyMsg.getText(67), 10);
    msgOut(TyMsg.getText(2) + e, 10);
      
    // get out      
    return false;   
   
  } // end-catch 
  
  // good 
  return true;
  
} // end-method

/**
 * Start up the server
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
  setReady(TymeacInfo.NON_ACT_CSF);
  
  // good startup
  return true;

} // end-method

/**
 *  Create the socket factories, impl Object and Rebind with the RMIRegistry 
 */   
protected void doExport() {
  
  // When could not create the factories
  if  (!createFactory()) {
    
       // say ending
      TyBase.printMsg(TyMsg.getMsg(98)); 
    
      // no good
      return;
    
  } // end-if             
  
  // new impl obj
  TymeacStandardImpl impl =  new TymeacStandardImpl(T);
  
  // rebind Tymeac 
  try {
    // When using a port
    if  (T.getPort() > -1) {
              
        // set global remote object
        T.setTi((TymeacInterface)
                  UnicastRemoteObject.exportObject(impl, // remote obj
                                                   T.getPort(),   // port
                                                   client, // client
                                                   server)); // server
    }
    else {
      // set global remote object
        T.setTi((TymeacInterface)
                  UnicastRemoteObject.exportObject(impl, // remote obj
                                                   0,   // any port
                                                   client, // client
                                                   server)); // server
      
    } // endif       
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
  if  (rebind())  setGood();
          
} // end-method
} // end-class
