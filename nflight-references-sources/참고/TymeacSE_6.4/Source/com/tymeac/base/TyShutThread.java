package com.tymeac.base;

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
 * The shutdown thread. Wait 2 seconds then issue System.exit().
 */ 
public final class TyShutThread
    extends Thread {
  
   TyBase T;  // base storage
    
/**
 * Constructor
 * @param Ty TyBase
 */   
public TyShutThread (TyBase Ty) {
  
    super("TymeacShutdown"); // thread name
  
    T = Ty;  // set pointer to base storage
    
} // end-constructor

/**
 * Wait for two seconds then system exit.
 */    
public void run () {
  
  try {
    // wait 2 seconds       
    sleep(2000);
      
  } // end-try
    
  catch (InterruptedException e) {
  } // end-catch      
    
  // Tymeac server ended
  System.exit(0);
  
} // end-method
} // end-class
