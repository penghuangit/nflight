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
import com.tymeac.client.jframe.*;
/** 
 *  Tymeac demonstration system
 *      thread in support of Frame class TyDemoInternalTest
 */

public class TyDemoInternalTestThread3 extends Thread {

		private TymeacInterface ti = null;

		private volatile boolean stopme = false;

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyDemoInternalTestThread3(TymeacInterface TyI) {

		// give it a name		
		super("TyDemoInternalTestThread");

		// passed reference to tymeac 
		ti = TyI;

} // end-constructor
/**
 * Thread run method
 */
public void run() {

		// new object passing the reference to the server
		TyMenu menu = new TyMenu(ti);

		// make visable
		menu.setVisible(true);

		// lock on this monitor
		synchronized (this) {

				// till stopped
				while (!stopme) {

						// wait for a notifyAll
						try {
								wait();

						} // end-try

						catch (InterruptedException e) {}

				} // end while		
		} // end-sync

		// no longer visable
		menu.setVisible(false);

} // end-method
/**
 * stop the server
 */
public synchronized void stopMe() {

		// set to end spin lock
		stopme = true;

		// wake up thread
		notifyAll();

} // end-method
} // end-class
