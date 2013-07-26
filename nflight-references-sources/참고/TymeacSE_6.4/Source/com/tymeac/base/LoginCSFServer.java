package com.tymeac.base;

/*
 * Copyright (c) 2006 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

import com.tymeac.common.TyVariables;

/**
 * Demonstrate using a login server to prefix the actual server that uses
 *  Custom Socket Factories
 */
public class LoginCSFServer extends CSFServer {
  
  //impl of tymeac login
  private TymeacLoginImpl log_impl; 
  
  // remote object
  private TymeacLogin ro = null;
  
/**
 * main entrypoint - starts the application
 * @param args java.lang.String[]
 */  
public static void main(String[] args) {

  // new server object
  LoginCSFServer server = new LoginCSFServer();
  
  // When start up failed
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
 * Rebind the login remote object
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
  
  // impl of tymeac login
  log_impl = new TymeacLoginImpl(T); 
  
  // export Tymeac Login 
  try {
    // When using a port
    if  (T.getPort() > -1) {
                
        // export remote object with port
        ro = (TymeacLogin)UnicastRemoteObject.exportObject(log_impl, T.getPort());
    }
    else {
        // export remote object, no port
        ro = (TymeacLogin)(TymeacInterface)UnicastRemoteObject.exportObject(log_impl);
      
    }// endif       
  } // end-try
  
  catch (Exception e) {

    // say what
    msgOut(TyMsg.getMsg(9) + TyMsg.getText(162), 10);
    msgOut(TyMsg.getText(2) + e, 10);
      
    // get out      
    return false;   
   
  } // end-catch 
      
  // When using a port
  if  (T.getPort() > -1) {

      // rebinding message with port number
      msgOut(TyMsg.getMsg(74) 
              + tymeac_name
              + TyMsg.getText(30)
              + T.getPort(),
              10);
  }
  else {      
      // rebinding message
      msgOut(TyMsg.getMsg(74) + tymeac_name, 10);
      
 } // endif
  
  try {
    
    // rebind with Registry
    Naming.rebind(tymeac_url + tymeac_name, ro);
      
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
} // end-class
