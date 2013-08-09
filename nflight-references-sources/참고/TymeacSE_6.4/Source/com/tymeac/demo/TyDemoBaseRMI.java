package com.tymeac.demo;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.io.IOException;
import java.rmi.*;
import com.tymeac.base.*;
/**
 * Tymeac Demonstration System
 *
 *  This is the base activatable remote object definition to RMI Regisrty 
 *
 */

public class TyDemoBaseRMI {

/**
 * Create the service
 * @param args java.lang.String[]
 */
  
public static void main(String args[]) throws IOException {
    
    // When not already set
    if  (System.getSecurityManager() == null) { 
        
        // get a new rmi security manager
        System.setSecurityManager(new RMISecurityManager());
            
    } // endif 
        
    // construct base definitions
    TyDemoActivationBase tb = new TyDemoActivationBase(); 
        
    // create the backend object
    TymeacInterface backend = tb.createBackend();
        
    // When no good, get out
    if  (backend == null) {
        
        // back to o/s
        System.exit(0);
            
    } // endif
        
     // register the backend object
     if  (tb.registerWithRMI(backend) == 0) {
        
        // good
        System.out.println("Service registered with RMI Registry "); 
    }
    else {
        // good
        System.out.println("RMI errors"); 
            
    } // endif 
        
    // fini
    System.exit(0);
       
} // end-method
} // end-class
