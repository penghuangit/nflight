package com.tymeac.demo.jini2.client;

/*
 * Created on Feb 23, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscovery;
import net.jini.lookup.ServiceDiscoveryManager;
import com.tymeac.demo.jini2.base.*;

/**
 * @author CoopSoft.com
 *
 * 
 */
public class TyDemoJiniT1GetProxy {

        private TyDemoJiniDocInterface server = null;

public TyDemoJiniT1GetProxy() {
  
  doJini();
  
} // end-constructor          

public TyDemoJiniDocInterface getInterface() {

return server;
} // end-method

/**
 * Jini work: service discovery and proxy preparing
 * @return boolean 
 */
private boolean doJini() {
  
  // service discovery manager
  ServiceDiscoveryManager serviceDiscovery = null;  
  
  try {
    // Get the basic discovery manager with only "TymeacDocServer" registered  
    serviceDiscovery = new ServiceDiscoveryManager(
        new LookupDiscovery(
          new String[] { "TymeacDocService" }),
        null);
             
  } // end-try 
  
  catch (Exception e) {
    
    // say what
    System.out.println("Could not get a Service Discovery Manager= " + e.toString());
      
    // done
    return false;   
  
  } // end-catch    

  // service item is only Tymeac      
  ServiceItem serviceItem = null;
  
  try {    
  // Look up the remote server
  serviceItem = serviceDiscovery.lookup(
      new ServiceTemplate(
        null,
        new Class[] { TyDemoJiniDocProxy.class },
        null),
      null,
      10000L); // 10 seconds
        
  } // end-try 
  
  catch (Exception e) {
    
    // say what
    System.out.println("Service Discovery returned= " + e.toString());
      
    // done
    return false;
       
  } // end-catch           
  
  // When could not find
  if  (serviceItem == null) {
    
      // say what
      System.out.println("Could not find a Tymeac Server on the network");
      
    // done
    return false;
       
  } // endif  
        
  // cast to the proxy   
  TyDemoJiniDocProxy proxy = (TyDemoJiniDocProxy) serviceItem.service;
  
  server = proxy.getRemoteObject();
  
   
     
  // good
  return true;        
  
} // end-method 



 



} // end-class
