package com.tymeac.jini.base;

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

import com.tymeac.base.*;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.config.NoSuchEntryException;
import net.jini.export.Exporter;
import net.jini.jrmp.*;
import net.jini.id.*;
import net.jini.core.lookup.*;
import net.jini.discovery.*;
import net.jini.lookup.*;
import net.jini.lookup.entry.*;
import net.jini.core.entry.Entry;

import java.security.*;
import javax.security.auth.*;

import javax.security.auth.login.*;

/**
 * Continue building the activation and set up the two Jini services.
 *   This process remains alive until shut down by a Tymeac Server shut down
 *   or manually with a com.tymeac.jini.JiniActivationDistroy or by an o/s kill. 
 *
 */
public class JiniActivationCreate2 
          implements JiniFrontEndShutDown {   

        // used for waiting for a shut down
        private Object forever = new Object();
        
        // Configuration provider
        private Configuration config = null;

        // configuration component name
        private String configComponent = null;
        
        private Remote TymeacRemote_obj = null;
        
        // discovery manager
        private DiscoveryManagement discoveryManager = null;
  
        // join managers
        private JoinManager tymeacJoinManager = null,
                            myselfJoinManager = null;        
        
/**
 * Constructor -
 *   Fill in the values from the config
 * 
 */	 
public JiniActivationCreate2(String[] args) {  
  
    // When using a Configuration Provider
    if  ((args[0].length() > 6) &&  
         (args[0].substring(0,7).equalsIgnoreCase(TyMsg.getText(60)))) {
  
        // good
        ;
    }
    else {
        // cannot continue
        TyBase.printMsg(TyMsg.getMsg(1005));
        System.exit(1);
        
    } // endif
    
    // length of args minus -config
    int max = args.length -1;    
    
    // When nothing here
    if  (max < 1 ) {
      
        TyBase.printMsg(TyMsg.getMsg(1010));
        System.exit(1);
        
    } // endif
    
    // When there are only two args
    if  (max == 1) {
      
        // use the default name
        configComponent = TyMsg.getText(31);            
    }
    else {
        // use the last arg
        configComponent = args[max];
        
        // one less to move
        max--;
        
    } // endif             
    
    // get rid of the other parameters
    String[] newArg = new String[max];
    
    // move the old arg: 1 thru n to new arg 0 thru n-1
    for (int i = 0, j = 1; i < max; i++, j++) {
      
        // move all
        newArg[i] = args[j];
        
    } // end-for   
  
    try {
      // try to get a new provider            
      config = ConfigurationProvider.getInstance(newArg);
      
    } // end-try
    
    catch (Exception e) {
      
      TyBase.printMsg(TyMsg.getMsg(1015) + e.toString());
      System.exit(1); 
                
    } // end-catch     
  
} // end-constructor

/**
 * Start the application
 * @param args
 */
public static void main (String[] args) {
  
  // get new myself
  JiniActivationCreate2 jac = new JiniActivationCreate2(args);
  
  // intialize
  jac.init();

  // Clean up the mess
  System.gc();
  
  // wait for a shut down  
  jac.doWait();  
  
  // all terminated
  TyBase.printMsg(TyMsg.getMsg(1095));   
  
  System.exit(0);  
  
} // end-method

/**
 * Initialize the environment with/without security
 *
 */
private void init() {  

  // security login context
  LoginContext loginContext = null;
      
  try {
    // get the context
    loginContext = (LoginContext) config.getEntry(
         configComponent,          // component from command line or TyMsg.class
         TyMsg.getText(121),       // name
         LoginContext.class,       // type of class
         null);                    // no default
    
  } // end-try
  
  catch (NoSuchEntryException e) {
  
      // ignore
      ;
    
  } // end-catch 
  
  catch (Exception e) {

      // say what found
      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      
     System.exit(1); 
    
  } // end-catch           
      
  // When not using security     
  if (loginContext == null) {
  
      // say what
      TyBase.printMsg(TyMsg.getMsg(80));
      
      // non-secure
      doWork();     
  } 
  else {
      try  {  
        // try to login
        loginContext.login();
        
      } // end-try 
         
      catch (LoginException e) {
        
        // say what found
        TyBase.printMsg(TyMsg.getMsg(85) + e);
          
        System.exit(1);  
             
      } // end-catch       
        
      // say what
      TyBase.printMsg(TyMsg.getMsg(81));
        
      try {
        // do export stuff as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doWork();
                    return null;
                } // end-method
            }, // end-PEA
            null);
          
      } // end-try
    
      catch (PrivilegedActionException e) {
    
          // say what found
          TyBase.printMsg(TyMsg.getMsg(86) + e);
        
          System.exit(1); 
          
      } // end-catch 
  } // endif 

} // end-method  

/**
 * Wait for a shutdown
 *
 */
private void doWait() {
  
  // say what
  TyBase.printMsg(TyMsg.getMsg(1100));
  
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

private void doWork() {

  // get the tymeac environment remote object
  if  (!doRMI()) { 
          
      System.exit(1);
  
  } // endif   
  
  // create the tymeac jini service
  if  (!doTymeacJini()) {
    
      System.exit(1);
      
  } // endif 
  
  // create my jini service
  if  (!doMyselfJini()) {
    
      System.exit(1);
      
  } // endif 

} // endif  

/**
 * Get a connection to the RMI Registry and pick up the remote object put there
 *   by the preceding process.
 * 
 * @return true or rmi failure
 */
private boolean doRMI() {

  // try to connect this many times
  int count = 10;
   
  // keep trying the conection 
  while  (count > 0) {
  
    try { 
      // rmi naming look up
      TymeacRemote_obj = Naming.lookup(TyMsg.getText(127));
                       
      // got one
      return true;
                              
    } // end-try
   
    catch(ConnectException e) {
      
      // try this many times in case there is a lot of traffic
      count--;   
                    
    } // end-catch
  
    catch(Exception e) {
      
      // say what 
      TyBase.printMsg(TyMsg.getMsg(1105) + e.toString()); 
        
      // no good
      return false;
        
    } // end-catch        
  } // end-while
  
  // no good
  return false;

} // end-method

/**
 * Jini work: discovery and join manager for Tymeac Server
 * @return boolean 
 */
private boolean doTymeacJini() {             
  
  // proxy
  JiniTymeacProxy proxy = JiniTymeacProxy.create((TymeacInterface)TymeacRemote_obj);
        
  try {   
      // get a new disc mgr
      discoveryManager = (DiscoveryManagement) config.getEntry(
            configComponent, 
            TyMsg.getText(74),
            DiscoveryManagement.class);
  
  } // end-try
   
  catch (Exception e) {
    
      TyBase.printMsg(TyMsg.getMsg(1050) + TyMsg.getText(74) 
                                + e.toString());
      
      // get out  
      return false; 
  
  } // end-catch
  
  // name of service
  Name myName = null;
  
  try {
      // get a new name field
      myName = (Name) config.getEntry(
            configComponent,
            TyMsg.getText(142),
            Name.class,
            new Name(TyMsg.getText(128)));
  
  } // end-try   
  
  catch (Exception e) {

      // print msgs
      TyBase.printMsg(TyMsg.getMsg(1050) + e);
      
      // get out  
      return false;  
                 
  } // end-catch
  
  // service info
  ServiceInfo info = null;
  
  try {
      // get a new service info 
      info = (ServiceInfo) config.getEntry(
            configComponent,
            TyMsg.getText(143),
            ServiceInfo.class,           
            new ServiceInfo(TyMsg.getText(129),
                            TyMsg.getText(130),  
                            TyMsg.getText(131),
                            TyMsg.getText(132),
                            TyMsg.getText(133),
                            TyMsg.getText(134)));
  
  } // end-try   
  
  catch (Exception e) {

      // print msgs
      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      
      // get out  
      return false;  
                 
  } // end-catch  
    
  // attributes  
  Entry[] entries = new Entry[] {myName, info}; 
  
  try {
      // Get the join manager
      tymeacJoinManager = new JoinManager(
          proxy,               // smart proxy
          entries,             // attribute sets
          createServiceID(),  // new serice id
          discoveryManager,   // the above discovery manager
          null,               // no leaseMgr
          config);            // config[file]
   
  } // end-try 
  
  catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(1060) + e.toString());
      
      // get out  
      return false;  
                 
  } // end-catch  
   
  // good
  return true;        
  
} // end-method

/**
 * Jini work: discovery and join manager for myself
 * @return boolean 
 */
private boolean doMyselfJini() {
  
  Exporter exporter = null;
  
  try {   
    // get a new exporter
    exporter = (Exporter) config.getEntry(
          configComponent, 
          TyMsg.getText(120),
          Exporter.class,
          new JrmpExporter());
  
  } // end-try   
   
  catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(88) 
                        + e.toString());
      
      // get out  
      return false;  
                 
  } // end-catch
  
  // interface 
  JiniFrontEndShutDown remote_obj = null;
  
  try {
    // export myself since this class implements the interface
    remote_obj = (JiniFrontEndShutDown) exporter.export(this);
    
  } // end-try 
  
  catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(89) 
                        + e.toString());
      
      // get out  
      return false;  
                 
  } // end-catch
  
  // just a simple proxy
  JiniShutDownProxy proxy = new JiniShutDownProxy(remote_obj);
  
  // name of service
  Name myName = null;
  
  try {
      // get a new name field
      myName = (Name) config.getEntry(
            configComponent,
            TyMsg.getText(144),
            Name.class,
            new Name(TyMsg.getText(135)));
  
  } // end-try   
  
  catch (Exception e) {

      // print msgs
      TyBase.printMsg(TyMsg.getMsg(1050) + e);
      
      // get out  
      return false;  
                 
  } // end-catch
  
  // service info
  ServiceInfo info = null;
  
  try {
      // get a new service info 
      info = (ServiceInfo) config.getEntry(
            configComponent,
            TyMsg.getText(145),
            ServiceInfo.class,           
            new ServiceInfo(TyMsg.getText(136),
                            TyMsg.getText(137),  
                            TyMsg.getText(138),
                            TyMsg.getText(139),
                            TyMsg.getText(140),
                            TyMsg.getText(141)));
  
  } // end-try   
  
  catch (Exception e) {

      // print msgs
      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      
      // get out  
      return false;  
                 
  } // end-catch 
  // attributes                                    
  Entry[] entries = new Entry[] {myName, info};                                      
  
  try {
      // Get the join manager
      myselfJoinManager = new JoinManager(
          proxy,               // the impl object
          entries,             // attribute sets
          createServiceID(),  // new serice id
          discoveryManager,   // the above discovery manager
          null,               // no leaseMgr
          config);            // config[file]
   
  } // end-try 
  
  catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(1060) + e.toString());
      
      // get out  
      return false;  
                 
  } // end-catch  
   
  // good
  return true;        
  
} // end-method

/**
 * This is the implementation of the shut down interface.
 *  The cancelling process calls this method to end the service.
 */
public void shutDown() {
  
  // kill tymeac service
  tymeacJoinManager.terminate();
  TyBase.printMsg(TyMsg.getMsg(1110));

  // kill my service
  myselfJoinManager.terminate();
  TyBase.printMsg(TyMsg.getMsg(1115));
  
  // kill wait
  synchronized (forever) {
    
    // wake up the main thread
    forever.notify();
    
  } // end-sync    
  
} // end-method   

/** 
 * Creates a new service ID. 
 */
protected ServiceID createServiceID() {
  
  // get base id
  Uuid uuid = UuidFactory.generate();
  
  // new Service ID
  return new ServiceID(
      uuid.getMostSignificantBits(),
      uuid.getLeastSignificantBits());
      
} // end-method

} // end-class
