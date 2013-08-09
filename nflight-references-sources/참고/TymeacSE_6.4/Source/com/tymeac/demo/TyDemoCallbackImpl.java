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

/**
 * Tymeac Demonstration System. 
 *   Implementation of the Interface for call back demonstration.
 */

class TyDemoCallbackImpl 
    implements TyDemoCallback {   

/**
 * TyDemoCallbackImpl constructor.
 */

public TyDemoCallbackImpl() {
  
} // end-constructor
/**
 * This is as simple as it gets.  Add your own code to do that which is needed.
 * @param back Object - returned object
 */

public void giveBack(java.lang.Object back) 
              throws java.rmi.RemoteException {
    
    // Should be a string object
    if  (back == null)  {
            
        // say no good  
        System.out.println("Did not complete properly");

        // bye
        return;

    } // endif
    
    // say what came back
    System.out.println((String) back);

} // end-method
} // end-class
