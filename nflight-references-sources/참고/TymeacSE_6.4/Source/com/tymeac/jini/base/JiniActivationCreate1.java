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

import java.io.IOException;
import java.util.Properties;
import java.rmi.*;
import java.rmi.activation.*;

import com.tymeac.base.*;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.config.NoSuchEntryException;
import net.jini.security.ProxyPreparer;
import net.jini.security.BasicProxyPreparer;

import java.security.*;
import javax.security.auth.*;
import javax.security.auth.login.*;

/**
 * The Jini activatable tymeac server is a two stage process.
 *   1 - Create the activatable backend description, register it with the
 *         activation system and save the remote object in the RMI Registry.
 *       End this process. 
 *
 *   2 - The next process will retrieve the above, remote object, and register it
 *         with a Jini Lookup Service.
 *   
 *   Without ending this process, the activation system will activate the Tymeac
 *      Server in the same JVM as this service. When the Server de-activates, goodbye
 *      Jini service.
 * 
 */
public class JiniActivationCreate1 {   
        
        // Configuration provider
        private Configuration config = null;

        // configuration component name
        private String configComponent = null;
        
        // System properties to start act server
        private Properties props = null;
        
        // application to launch
        private String applProgram = null;
  
        // location of the Tymeac Activation Class
        private String location;
  
        // Marshalled Object
        private MarshalledObject data = null;
        
        private Remote remote_obj = null;
        
        private boolean createSuccessful = false;        
        
/**
 * Constructor -
 *   Fill in the values from the config file
 * 
 */	 
public JiniActivationCreate1(String[] args) {  
  
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
    
    try {    
      // see if an appl program
      applProgram = (String) config.getEntry(
            configComponent, 
            TyMsg.getText(122),
            String.class, 
            null);
    } // end-try
    
    catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      System.exit(1); 
                 
    } // end-catch
    
    // When in use
    if  (applProgram == null) {
      
        TyBase.printMsg(TyMsg.getMsg(1065) + TyMsg.getText(122));
        System.exit(1);      
        
    } // endif
    
    try {    
      // see if a location
      location = (String) config.getEntry(
            configComponent, 
            TyMsg.getText(123),
            String.class, 
            null);
    } // end-try
    
    catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      System.exit(1); 
                 
    } // end-catch
    
    // When in use
    if  (location == null) {
      
        TyBase.printMsg(TyMsg.getMsg(1065) + TyMsg.getText(123));
        System.exit(1);      
        
    } // endif
    
    // system properties in string format
    String[] sysProps = null;    
    
    try {    
      // see if system properties
      sysProps = (String[]) config.getEntry(
            configComponent, 
            TyMsg.getText(124),
            String[].class, 
            null);
    } // end-try
    
    catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      System.exit(1); 
                 
    } // end-catch
    
    // When in use
    if  (sysProps == null) {
      
        TyBase.printMsg(TyMsg.getMsg(1065) + TyMsg.getText(124));
        System.exit(1);      
        
    } // endif
    
    // number of properties
    if  (sysProps != null) max = sysProps.length;
    
    // When not even number
    if  (max % 2 != 0) {
      
        TyBase.printMsg(TyMsg.getMsg(1070));
        System.exit(1);      
        
    } // endif
    
    // new properties object             
    props = new Properties();
      
    // load all the properties
    for (int i = 0; i < max; i += 2) {
      
        // each pair
        if  (sysProps != null) props.put(sysProps[i], sysProps[i + 1]);                
      
    } // end-for      
        
    String[] options = null;
    
    try {    
      // see if options for appl program
      options = (String[]) config.getEntry(
            configComponent, 
            TyMsg.getText(125),
            String[].class, 
            new String[] { TyMsg.getText(59) });
    } // end-try
    
    catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      System.exit(1); 
                 
    } // end-catch 
        
    // marshalled object for passing data to the Tymeac Server constructor
    try {        
    	// set a marshalled Object
    	data = new MarshalledObject<Object>(options);
      
    } // end-try 
  	
    catch (IOException e) {

    	TyBase.printMsg(TyMsg.getMsg(1075) + e.toString());
  	
    	System.exit(1);
					
  } // end-catch 
  
} // end-constructor

/**
 * Start the application
 * @param args
 */
public static void main (String[] args) {
  
  // get new myself
  JiniActivationCreate1 jac = new JiniActivationCreate1(args);
  
  // do the work
  jac.init();
  
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
      
      return;
    
  } // end-catch           
      
  // When not using security     
  if (loginContext == null) {
  
      // say what
      TyBase.printMsg(TyMsg.getMsg(80));
      
      // non-secure
      createTymeacEnvironment();     
  } 
  else {
      try  {  
        // try to login
        loginContext.login();
        
      } // end-try 
         
      catch (LoginException e) {
        
        // say what found
        TyBase.printMsg(TyMsg.getMsg(85) + e);
          
        // bye
        return;   
             
      } // end-catch       
        
      // say what
      TyBase.printMsg(TyMsg.getMsg(81));
        
      try {
        // do export stuff as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    createTymeacEnvironment();
                    return null;
                } // end-method
            }, // end-PEA
            null);
          
      } // end-try
    
      catch (PrivilegedActionException e) {
    
          // say what found
          TyBase.printMsg(TyMsg.getMsg(86) + e);
        
          // bye
          return;
          
      } // end-catch 
  } // endif  
  
  // When create the tymeac environment
  if  (createSuccessful) { 
    
      TyBase.printMsg(TyMsg.getMsg(1080));
  }
  else {
      TyBase.printMsg(TyMsg.getMsg(1081));
  
  } // endif 

} // end-method  

/**
 * Create the backend Tymeac Server Remote Object
 *    
 */
private void createTymeacEnvironment() {    
        
    // command env not used
    ActivationGroupDesc.CommandEnvironment ace = null;
      
    // new group
    ActivationGroupDesc group = new ActivationGroupDesc(props, ace);
    
    // for the activation system preparer
    ProxyPreparer actSysPreparer = null;
        
    try {    
      // see if proxy preparer
      actSysPreparer = (ProxyPreparer) config.getEntry(
            configComponent, 
            TyMsg.getText(126),
            ProxyPreparer.class, 
            new BasicProxyPreparer());
    } // end-try
    
    catch (Exception e) {

      TyBase.printMsg(TyMsg.getMsg(1050) + e.toString());
      return; 
                 
    } // end-catch 
     
    try {  
      // act system  
      ActivationSystem actSys = (ActivationSystem) 
          actSysPreparer.prepareProxy(ActivationGroup.getSystem());
        
      // Register the group and get the ID.
      ActivationGroupID gid = actSys.registerGroup(group);
      
      // Now create the group
      ActivationGroup.createGroup(gid, group, 0);
      
      // Create an activation descriptor
      ActivationDesc desc = 
        new ActivationDesc(
              gid,
              applProgram,  // name of the class 
              location,     // location of the class
              data);        // parms class needs to initialize         


      // get the remote obj
      remote_obj = Activatable.register(desc);
      
      // When NOT good
      if  (remote_obj == null) {
        
          // done
          return;
          
      } // endif   
      
      try {
       // Naming 
       Naming.rebind(TyMsg.getText(127), // name to use 
                      remote_obj);        // remote object
       
       // successful
       createSuccessful = true;
       
       // back
       return;
         
      } // end-try
              
      catch (Exception e) {
      
          TyBase.printMsg(TyMsg.getMsg(1085) + e.toString());
          e.printStackTrace();

          return;    
                         
      } // end-catch
    } // end-try
    
    catch (Exception e) {
      
        System.err.println(TyMsg.getMsg(1090) + e.toString());
        
        return;
                      
    } // end-catch
} // end-method

} // end-class
