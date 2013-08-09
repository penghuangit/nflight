package com.tymeac.jini.base;

/*
 * Created on Feb 18, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
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
import java.rmi.activation.*;

import com.tymeac.base.*;

import javax.security.auth.login.*;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;

/**
 * This is the Jini version of activation for both
 *   standard and custom socket factory.
 * 
 */
public class JiniTymeacActivationImpl 
              extends TymeacActivationImpl {
  
   static final long serialVersionUID = -6853116751034403221L;

/**
 * Constructor 1 standard
 * @param id ActivationID
 * @param data MarshalledObject
 * @exception java.rmi.RemoteException The exception description.
 * @exception Throwable.
 */ 
public JiniTymeacActivationImpl (ActivationID id, MarshalledObject data)
    throws  RemoteException, 
            Throwable {

  // this act-id data
  super(id, data);    
  
  // Server is jini activation type
  Ty.setStartType(TymeacInfo.ACT_JINI); 

} // end-constructor
            
/**
 * Constructor 2, csf
 * @param id ActivatinID
 * @param csf RMIClientSocketFactory
 * @param ssf RMIServerSocketFactory
 * @param data MarshalledObject
 * @exception java.rmi.RemoteException The exception description.
 */ 
public JiniTymeacActivationImpl (ActivationID id, 
                             RMIClientSocketFactory csf,
                             RMIServerSocketFactory ssf,             
                             MarshalledObject data)
          throws  RemoteException, 
                  Throwable {

  super(id, csf, ssf, data); 
  
  // Server is jini activation type
  Ty.setStartType(TymeacInfo.ACT_CSF_JINI); 
  
} // end-constructor

/**
 * 
 * @return LoginContext
 */
protected LoginContext getLoginContext() {
  
  // security login context from configuration
  LoginContext loginContext = JiniSecurityModule.getLoginContext(Ty);  
        
  // When no login context there   
  if  (loginContext == null) {
      
      // see what comes back from the startup module
      su.determineLoginContext(); 
      
      // When not a problem and there is something there
      if  (Ty.getLoginContext() != null) {
          
          try {
            // try getting from base
            loginContext = new LoginContext(Ty.getLoginContext());
            
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
 * Overridden method
 * 
 * @return ok or ng
 */   
public boolean overrideMe1() {
  
  // When getting a new configuration provider fails, get out
  if  (!setConfigProvider()) return false; 
  
  return true;
  
} // end-method

/** 
 * Creates a new congig provider
 */
private boolean setConfigProvider() {
  
  // array for config location
  String[] configArg = {su.getStartupFields().getConfigLocation()};
  
  // set config component in base storage
  Ty.setConfigComponent(su.getStartupFields().getConfigComponent()); 
  
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
  Ty.setConfigProvider(config); 
  
  return true;
  
} // end-method
} // end-class
