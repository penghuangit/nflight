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
 *  Tymeac demonstration system
 *      bean in support of Frame class TyDemoInternalTest
 */

public class TyDemoInternalTestBean3 {

	// reference to the server
	private TymeacInterface ti = null;

	// implementation methods
	private InternalServer impl = null;

	// window thread
	private TyDemoInternalTestThread3 it = null;
	
	//
	@SuppressWarnings("unused")
  private TyDemoInternalTest3T3Base tmt;

/**
 * TyDemoInternalTestBean constructor comment.
 */
public TyDemoInternalTestBean3() {
	
} // end-constructor
/**
 * Close the tymeac menu display 
 */
public void closeMenu() {

		// stop thread
		it.stopMe();

} // end-if
/**
 * open the tymeac menu display
 */
public void openMenu() {

		// new window thread
		it = new TyDemoInternalTestThread3(ti);

		// start it working
		it.start();

} // end-if
/**
 * Send a single request to the server
 * @return java.lang.String
 */
public String sendSingle() {
	
	//start it off 
  tmt = new TyDemoInternalTest3T3Base(22, ti);
    return "";

} // end-method
/**
 * Start up the server 
 * @return com.tymeac.base.TymeacInterface
 */
public TymeacInterface startServer() {

		// new object
		impl = new InternalServer();  

		// passed arg is "use stand alone mode"			
		String[] in = {"-s"};

		// create the server and save the reference to it
		ti = impl.createServer(in);

		// give back the reference 
		return ti;

} // end-method
/**
 * stop the server
 * @return java.lang.String
 */
public String stopServer() {
	
		// returned string
		String back = null;
		
		try {                             
		    // do a shut request   
  		  back = ti.shutRequest();
		}
		catch (Exception e) {}
  
    // Should be an array
    if  (back == null)  {
            
        // bye
        return "Did not complete properly";

    } // endif            
    
    // what came back
    return back;

} // end-method
} // end-class
