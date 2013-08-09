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
 *
 *  Tymeac demonstration system, shutdown stage 1 exit entry
 * 
 */
 
public class TyDemoShutdown1 {

  
/**
 *  constructor
 */
 
public TyDemoShutdown1() {
    
    // common storage
    TyDemoMsgThread mt = new TyDemoMsgBase().getThreadRef();

    // When not there
    if  (mt == null) {
              
        // back
        return;
        
    } // endif

    // wake up the thread to shut it down
    mt.shutDown("Shut down the Message Thread");    

    
} // end-constructor
} // end-class
