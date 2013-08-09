/*
 * Created on Feb 7, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
package com.tymeac.serveruser;

import java.rmi.RemoteException;
import net.jini.core.lookup.*;
import net.jini.discovery.*;
import net.jini.lookup.*;
import com.tymeac.jini.base.*;
import com.tymeac.base.*;

/**
 * Shutdown module for Tymeac Jini environment
 */
public class JiniShutDownExit
         implements JiniFrontEndShutDown {

  // the info class
  TymeacInfo info = null; 

public JiniShutDownExit() {
  
  // the info class
  info = TymeacInfo.getInstance();
  
  // When a jini activation type
  if  ((info.getStartType() == TymeacInfo.ACT_JINI)||
       (info.getStartType() == TymeacInfo.ACT_CSF_JINI)) {
    
      // When only a deactivation
      if  (info.getShutOrigin() == TymeacInfo.DEACTIVATION) {
    
          // not yet
          return;
      }
      else {
          try {
            // do a remote Jini service shut down
            shutDown();
            
          } catch (Exception e) {} // unhandled     
          
          // done
          return;
      
      } // endif             
  }
  else {    
      // When Jini non-activation
      if  (info.getStartType() == TymeacInfo.NON_ACT_JINI) {
        
          // use local 
          shutDownLocal();
          
          // done
          return;            
      }
      else {
          // not used
          return; 

      } // endif                                  
  } // endif               
  
} // end-constructor

/**
 * local shut down
 */
private void shutDownLocal() {
  
  try {
    // get base storage
    JoinManager jm = (JoinManager)info.getJoinManager();
    
    // When a valid join manager
    if  (jm != null) {
      
        // kill service
        jm.terminate();
        
        // done
        return;
    
    } // endif
    
  } catch (Exception e) {} // unhandled         
  
} // end-method  

/**
 * Find the activatable service on the network and shut it down
 */  
public void shutDown() 
          throws RemoteException {
     
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
    System.out.println("TymeacServer Stage2 shut down exit. Could not get a Service Discovery Manager= " + e.toString());
      
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
    System.out.println("TymeacServer Stage2 shut down exit. Service Discovery returned= " + e.toString());
      
    // done
    return;
       
  } // end-catch           
  
  // When could not find
  if  (serviceItem == null) {
    
      // say what
      System.out.println("TymeacServer Stage2 shut down exit. Could not find a JiniSimpleProxy.class on the Service");
      
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
    System.out.println("TymeacServer Stage2 shut down exit. Calling server for shutdown caused this= " + e.toString());
      
    // done
    return;
       
  } // end-catch          

} // end-method
} // end-class
