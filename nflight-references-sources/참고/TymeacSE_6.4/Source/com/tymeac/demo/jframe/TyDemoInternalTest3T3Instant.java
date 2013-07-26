package com.tymeac.demo.jframe;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.base.*;

/**
 * Tymeac Demonstration System.  Multiple test, thread iniatiator for all
 *        threads. 
 *
 */

public final class TyDemoInternalTest3T3Instant 
		extends Thread {

	// the base storage
	TyDemoInternalTest3T3Base base;

	// nbr of threads
	int nbr;
	
	// server
	TymeacInterface ti;

/**
 * Tymeac Demonstration System, multi test thread initiator constructor:
 *
 * @param T3Base base storage   
 * @param int number of threads to start
 *
 */
 
public TyDemoInternalTest3T3Instant (
					TyDemoInternalTest3T3Base p_base, 
					int p_nbr, 
					TymeacInterface t) {

    // fields
    base = p_base;
    nbr  = p_nbr;
    ti   = t;
    
} // end-constructor
/**
 * This is the thread run method.
 */

public void run() {


    // instantiate a new thread
    // wait one half second
    for  (int i = 0; i < nbr; i++) {

          // instantiate a new thread: (TyDemoT3Base storage, sequence nbr.) & start it
          new TyDemoInternalTest3T3Thread(base, i, ti).start();

          // wait a half second
          try {
              Thread.sleep(500);
          } // end-try

          catch (InterruptedException e) {
          } // end-catch  

    } // end-for  

} // end-method
} // end-class
