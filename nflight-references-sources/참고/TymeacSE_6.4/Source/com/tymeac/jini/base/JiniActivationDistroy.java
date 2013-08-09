/*
 * Created on Feb 8, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
package com.tymeac.jini.base;

import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscovery;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.config.NoSuchEntryException;
import java.security.*;
import javax.security.auth.*;
import javax.security.auth.login.*;


import com.tymeac.base.*;

/**
 * Terminate the activatable Tymeac services
 * 
 */
public class JiniActivationDistroy {
  
        // Configuration provider
        private Configuration config = null;

        // configuration component name
        private String configComponent = null;

/**
 * Constructor
 * @param args
 */
public JiniActivationDistroy (String[] args) {

    // When using a Configuration Provider
    if  ((args[0].length() > 6) &&  
         (args[0].substring(0,7).equalsIgnoreCase(TyMsg.getText(60)))) {
  
        // good
        ;
    }
    else {
        // cannot continue
        TyBase.printMsg("Command line does not begin with -config");
        System.exit(1);
        
    } // endif
    
    // length of args minus -config
    int max = args.length -1;    
    
    // When nothing here
    if  (max < 1 ) {
      
        TyBase.printMsg("Command line only has one arg, -config, needs location & component name");
        System.exit(1);
        
    } // endif
    
    // When there are only two args
    if  (max == 1) {
      
        TyBase.printMsg("Command line only has two args, needs -config, location & component name");
        System.exit(1);            
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
      
      TyBase.printMsg("ConfigurationProvider= " + e.toString());
      System.exit(1); 
                
    } // end-catch   
  
} // end-constructor  
          
/**
 * Main start up
 * @param args
 */  
public static void main (String[] args) {
  
  // new myself
  JiniActivationDistroy jad = new JiniActivationDistroy(args);
  
  // do the work
  jad.init();
  
  // good bye
  TyBase.printMsg("Done");  
  
  // may have threads alive
  System.exit(0);
  
} // end-method

/**
 * Login and call the work method
 *
 */
private void init() {

  // security login context
  LoginContext loginContext = null;
      
  try {
    // get the context
    loginContext = (LoginContext) config.getEntry(
         configComponent,          // component from command line or TyMsg.class
         "loginContext",           // name
         LoginContext.class,       // type of class
         null);                    // no default
    
  } // end-try
  
  catch (NoSuchEntryException e) {
  
      // ignore
      ;
    
  } // end-catch 
  
  catch (Exception e) {

      // say what found
      TyBase.printMsg("Config getLoginContext= " + e);
      
     System.exit(1); 
    
  } // end-catch           
      
  // When not using security     
  if (loginContext == null) {
  
      // say what
      TyBase.printMsg("Using INSECURE startup");
      
      // non-secure
      shutDown();     
  } 
  else {
      try  {  
        // try to login
        loginContext.login();
        
      } // end-try 
         
      catch (LoginException e) {
        
        // say what found
        TyBase.printMsg("LoginException= " + e);
          
        System.exit(1);  
             
      } // end-catch       
        
      // say what
      TyBase.printMsg("Using secure startup");
        
      try {
        // do export stuff as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    shutDown();
                    return null;
                } // end-method
            }, // end-PEA
            null);
          
      } // end-try
    
      catch (PrivilegedActionException e) {
    
          // say what found
          TyBase.printMsg("PriilegedAction= " + e);
        
          System.exit(1); 
          
      } // end-catch 
  } // endif 

} // end-method   

/**
 * Shut down the services with a remote call back to the service remote object
 *
 */
public void shutDown() {
     
  // service discovery manager
  ServiceDiscoveryManager serviceDiscovery = null;  
  
  try {
    // get the service discovery manager
    serviceDiscovery = new ServiceDiscoveryManager(
                            new LookupDiscovery(
                                 new String[] { "TymeacServerFrontEnd" }),
                            null); 
  } // end-try 
  
  catch (Exception e) {
    
    // say what
    TyBase.printMsg("Could not get a Service Discovery Manager= " + e.toString());
      
    // done
    return;   
  
  } // end-catch

  // service item is only Tymeac shut down      
  ServiceItem serviceItem = null;
  
  try {    
  // Look up the remote server
  serviceItem = serviceDiscovery.lookup(
      new ServiceTemplate(
        null,
        new Class[] { JiniShutDownProxy.class },
        null),
      null,
      10000L); // 10 seconds
        
  } // end-try 
  
  catch (Exception e) {
    
    // say what
    TyBase.printMsg("Service Discovery returned= " + e.toString());
      
    // done
    return;
       
  } // end-catch           
  
  // When could not find
  if  (serviceItem == null) {
    
      // say what
      TyBase.printMsg("Could not find a JiniSimpleProxy.class on the Service");
      
    // done
    return;
       
  } // endif 
  
  // get the proxy
  JiniShutDownProxy proxy = (JiniShutDownProxy) serviceItem.service; 
  
  // get the remote object from within the proxy
  JiniFrontEndShutDown server = proxy.getRemoteObject(); 
  
  try {      
    // do it
    server.shutDown();
  
  } // end-try 
  
  catch (Exception e) {
  
    // say what
    TyBase.printMsg("Calling server for shutdown caused this= " + e.toString());
      
    // done
    return;
       
  } // end-catch          

} // end-method

} // end-class
