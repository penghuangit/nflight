package com.tymeac.serveruser;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.rmi.*;
 /**
 *
 * This is the RMI Security Manager used by Tymeac at server startup.
 *   Make changes to the constructor for your installation.
 *
 * The server creates a new instance of this class during startup,
 *   TyServerRMISecMgr tsm = new TyServerRMISecMgr();
 *     to establish a security manager      
 *
 * 
 */

public class TyServerRMISecMgr {  

/**
 *
 * Make changes here for your installation needs.
 *
 * 
 */
public TyServerRMISecMgr ( ) {
  
    
    // new security manager for RMI
    try {
        // When not already set
        if  (System.getSecurityManager() == null) {

            // set a new security manager
            System.setSecurityManager( new RMISecurityManager());

        } // endif
    } // end-try
          
    catch (SecurityException e) {
          
          // put your code here
              
    } // end-catch          
  
    return;
      
} // end-constructor
} // end-class
