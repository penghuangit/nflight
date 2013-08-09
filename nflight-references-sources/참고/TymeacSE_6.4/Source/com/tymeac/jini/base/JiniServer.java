package com.tymeac.jini.base;

/*
 * 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import com.tymeac.base.*;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.config.NoSuchEntryException;
import net.jini.export.Exporter;
import net.jini.discovery.*;
import net.jini.id.*;
import net.jini.core.lookup.*;
import net.jini.lookup.*;
import net.jini.lookup.entry.*;
import net.jini.core.entry.Entry;

import javax.security.auth.login.*;

/**
 * Jini Tymeac Server
 * 
 */
public class JiniServer 
          extends BaseServer {
        
  // smart proxy
  private JiniTymeacProxy proxy = null; 
        
/**
 * Constructor
 *
 */        
public JiniServer() {

  super();
    
} // end-constructor  
        
/**
 * main entrypoint - starts the application
 * @param args java.lang.String[]
 */ 
public static void main(String[] args) {

  // new server object
  JiniServer server = new JiniServer();
  
  // When start up failed
  if  (!server.startUp(args)) {
    
      // kill
      System.exit(1);
    
  } // end-if      
  
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
 * The login context may come from either a config file or normal startup places.
 * @return LoginContext
 */
public LoginContext getLoginContext() {
  
  int rc = 0;
  
  // security login context from configuration
  LoginContext loginContext = JiniSecurityModule.getLoginContext(T);  
        
  // When no login context there   
  if  (loginContext == null) {
            
      // see what comes back from the startup module
      getStartUp().determineLoginContext();
      
      // When not a problem and there is something there
      if  (T.getLoginContext() != null) {
          
          try {
            // try getting from base
            loginContext = new LoginContext(T.getLoginContext());
            
          } // end-try
          
          catch (LoginException e) {

            // say what found
            msgOut(TyMsg.getMsg(82) + e, 10);
            
          } // end-catch           
      } // endif    
  } // endif
  
  return loginContext;
    
} // end-method  

/**
 * Override for unique configuration provider requirements
 * 
 * @return ok or ng
 */   
public boolean overrideMe1() {
  
  // return result of getting a new configuration provider
  return setConfigProvider();
  
} // end-method

/**
 * Start up
 * @param args
 * @return
 */ 
private boolean startUp(String[] args) {
  
  // When start up failed
  if  (!init(args, Startup.startup_basic)) {
    
      // kill
      return false;
    
  } // end-if 
  
  // When export failed
  if  (!getCompletion()) {

      // say ending
      TyBase.printMsg(TyMsg.getMsg(98));
      
      // kill
      return false;

  } // endif  
  
  // set alive with type of server
  setReady(TymeacInfo.NON_ACT_JINI); 
  
  // good 
  return true;

} // end-method  

/**
 * Overridden method to export the impl object
 * 
 */	
public void doExport() {
		
  // exporter object
  Exporter exporter = null;  
    
  try { 
     // get the exporter
     exporter = (Exporter) ((Configuration)T.getConfigProvider()).getEntry(
         T.getConfigComponent(),  // component from command line or TyMsg.class
         TyMsg.getText(120), // name
         Exporter.class,   // type of class
         null);            // no default
            
  } // end-try
  
  catch (Exception e) {
    
      // say what
      msgOut(TyMsg.getMsg(88) + e.toString(), 10);          
       
      // get out      
      return; 
    
  } // end-catch 
  
   // impl class
   JiniTymeacStandardImpl impl = new JiniTymeacStandardImpl(T);  

  try {    
    // export the impl class
    T.setTi((TymeacInterface) exporter.export(impl));
  
  } // end-try
  
  catch (Exception e) {
    
    // say what
     msgOut(TyMsg.getMsg(89) + e, 10);          
        
    // get out      
    return; 
    
  } // end-catch
  
  // smart proxy
  proxy = JiniTymeacProxy.create(T.getTi());
    
  // do jini discovery and join stuff
  if  (!doJini()) {
    
      // no good
      return;
  
  } // endif  
  
  // save impl class
  setImpl(impl);

  // good export
  setGood();
   
  // done
  return;      
    
} // end-method

/**
 * Jini work: discovery and join managers
 * @return boolean 
 */
private boolean doJini() {
    
  // discovery manager
  DiscoveryManagement discoveryManager = null;
  
  // join manager
  JoinManager joinManager = null;
        
  try {
      // get a new disc mgr
      discoveryManager = (DiscoveryManagement) ((Configuration)T.getConfigProvider()).getEntry(
            T.getConfigComponent(), 
            TyMsg.getText(74),
            DiscoveryManagement.class);
  
  } // end-try
   
  catch (NoSuchEntryException e) {
    
      // print msgs
      msgOut(TyMsg.getMsg(1050) + TyMsg.getText(74) 
                                + e.toString(), 10);
      
      // get out  
      return false; 
  
  } // end-catch
  
  catch (Exception e) {

      // print msgs
      msgOut(TyMsg.getMsg(1055) + e.toString(), 10);
      
      // get out  
      return false;  
                 
  } // end-catch
  
  // name of service
  Name myName = null;
  
  try {
      // get a new name field
      myName = (Name) ((Configuration)T.getConfigProvider()).getEntry(
            T.getConfigComponent(), 
            TyMsg.getText(75),
            Name.class,
            new Name(TyMsg.getText(76)));
  
  } // end-try   
  
  catch (Exception e) {

      // print msgs
      msgOut(TyMsg.getMsg(1050) + e, 10);
      
      // get out  
      return false;  
                 
  } // end-catch
  
  // service info
  ServiceInfo info = null;
  
  try {
      // get a new service info 
      info = (ServiceInfo) ((Configuration)T.getConfigProvider()).getEntry(
            T.getConfigComponent(), 
            TyMsg.getText(77),
            ServiceInfo.class,           
            new ServiceInfo(TyMsg.getText(78),
                            TyMsg.getText(79),  
                            TyMsg.getText(80),
                            TyMsg.getText(81),
                            TyMsg.getText(82),
                            TyMsg.getText(83)));
  
  } // end-try
   
  
  catch (Exception e) {

      // print msgs
      msgOut(TyMsg.getMsg(1050) + e.toString(), 10);
      
      // get out  
      return false;  
                 
  } // end-catch
  
  // attributes
  Entry[] entries = new Entry[] {myName, info};
  
  try {
      // Get the join manager
      joinManager = new JoinManager(
          proxy,               // smart proxy
          entries,             // attribute sets
          createServiceID(),  // new serice id
          discoveryManager,   // the above discovery manager
          null,               // no leaseMgr
          ((Configuration)T.getConfigProvider()));  // config[file]
  
      // save in base storage for shut down
      T.setJoinManager(joinManager);
   
  } // end-try 
  
  catch (Exception e) {

      // print msgs
      msgOut(TyMsg.getMsg(1060) + e.toString(), 10);
      
      // get out  
      return false;  
                 
  } // end-catch  
   
  // good
  return true;        
  
} // end-method 

/** 
 * Creates a new service ID. 
 */
private ServiceID createServiceID() {
  
  // get base id
  Uuid uuid = UuidFactory.generate();
  
  // new Service ID
  return new ServiceID(
      uuid.getMostSignificantBits(),
      uuid.getLeastSignificantBits());
      
} // end-method

/** 
 * Creates a new configuration provider
 */
private boolean setConfigProvider() {
  
  // array for config location
  String[] configArg = {getStartUp().getStartupFields().getConfigLocation()};
  
  // set config component in base storage
  T.setConfigComponent(getStartUp().getStartupFields().getConfigComponent()); 
  
  // Configuration Provider
  Configuration config = null;   
  
  try {
    // try to get a new provider            
    config = ConfigurationProvider.getInstance(configArg);
    
  } // end-try
  catch (Exception e) {
    
    // print msgs
    TyBase.printMsg(TyMsg.getMsg(1015) + e.toString());
    
    // get out  
    return false;  
              
  } // end-catch
  
  // When no config provider
  if  (config == null) {
      
      // print msgs
     TyBase.printMsg(TyMsg.getMsg(1045));
    
      // get out  
      return false;  
      
  } // endif 
  
  // set in base storage using an object for now
  T.setConfigProvider(config); 
  
  return true;
  
} // end-method

} // end-class
