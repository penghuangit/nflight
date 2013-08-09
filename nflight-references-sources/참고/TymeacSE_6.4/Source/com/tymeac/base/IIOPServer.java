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

import com.tymeac.common.*;
import javax.naming.*;

/**
 * This is the non-activatable rmi server using iiop 
 */
public class IIOPServer extends BaseServer {

/**
 * Constructor
 */        
private IIOPServer() {

  // sets the TyBase Storage
  super();
  
} // end-constructor  
        
/**
 * main entrypoint - starts the application
 * @param args java.lang.String[]
 */  
public static void main(String[] args) {

  // new server object
  IIOPServer server = new IIOPServer();
  
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
 * rebind with a registry
 * Override this method to add the TymeacLogin 
 */  
protected boolean rebind() {
  
  // variables
  TyVariables TyV = new TyVariables();
  String iiop_name = TyV.getIIOPName();
  
  // When illogical data
  if  ((!TyV.useIIOP())  ||
       (iiop_name == null) || 
       (iiop_name.length() < 1)) {           

      // do so
      msgOut(TyMsg.getMsg(61), 10);
              
      // get out      
      return false; 
      
  } // endif
  
  // rebinding message     
  // wait up to 10 sec for completion 
  msgOut(TyMsg.getMsg(71), 10);
  
  // rebind Tymeac 
  try {
    // provides an initial CosNaming server context 
    Context initialNamingContext = new InitialContext();
    
    // register with JNDI Registry
    initialNamingContext.rebind(iiop_name, T.getTi());
    
    // good
    return true;
        
  } // end-try

  catch (Exception e) {
                               
    // wait up to 10 sec for completion 
    msgOut(TyMsg.getMsg(9) + TyMsg.getText(68), 10);
    msgOut(TyMsg.getText(2) + e, 10);
            
    // get out      
    return false;   
   
  } // end-catch 
  
} // end-method

/**
 * Start up the server
 * @param args
 * @return boolean good or not
 */
private boolean startUp(String[] args) {

  // When start up failed
  if  (!init(args, Startup.startup_iiop)) {
    
      // say ending
      TyBase.printMsg(TyMsg.getMsg(98));
      
      // cancel run
      return false;
      
  } // endif    
 
  // When bind failed
  if  (!getCompletion()) {

      // say ending
      TyBase.printMsg(TyMsg.getMsg(98));
      
      // cancel run
      return false;

  } // endif 
  
  // set alive with type of server
  setReady(TymeacInfo.IIOP);
  
  // good startup
  return true;

} // end-method

/**
 *  Create the impl Object and rebind, there is no separate
 *    export/rebind 
 */   
protected void doExport() {
    
  // *--- remote object extends PortableRemoteObject ---*
  TymeacIIOPImpl TyIIOP = null; 
  
  try {
    // new remote object
    TyIIOP = new TymeacIIOPImpl(T); 
        
  } // end-try

  catch (Exception e) {
                               
    // wait up to 10 sec for completion 
    msgOut(TyMsg.getMsg(9) + TyMsg.getText(68), 10);
    msgOut(TyMsg.getText(2) + e.toString(), 10);
            
    // get out      
    return;   
   
  } // end-catch 
    
  // set global remote object in base storage
  T.setTi(TyIIOP); 
  
  // save impl class
  setImpl(TyIIOP);

  // When rebind worked, good 
  if  (rebind()) setGood();
          
} // end-method
} // end-class
