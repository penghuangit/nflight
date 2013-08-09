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

public class TyDemoInternalTestBean {

	// reference to the server
	private TymeacInterface ti = null;

	// implementation methods
	private InternalServer impl = null;

	// window thread
	TyDemoInternalTestThread it = null;

/**
 * TyDemoInternalTestBean constructor comment.
 */
public TyDemoInternalTestBean() {
	
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
		it = new TyDemoInternalTestThread(ti);

		// start it working
		it.start();

} // end-if
/**
 * Send a single request to the server
 * @return java.lang.String
 */
public String sendSingle() {
	
		// new string for Tymeac function
    String x = "112233445566778899";

    // make obj
    Object pass = x;

    // return array
    Object back[] = null;
   
    // form a parameter for Tymeac   
    TymeacParm TP = new TymeacParm(pass,         // data 
                                  "Function_2", // function name
                                  10,            // wait time
                                  1);            // priority
           
		try {                             
		    // do a sync request   
  		  back = ti.syncRequest(TP);
		}
		catch (Exception e) {}
  
    // Should be an array
    if  (back == null)  {
            
        // bye
        return "Did not complete properly";

    } // endif      
        
    // number of objects in array
    int nbr = back.length;

    // Display string
    String S = "";

    // concatenate all the strings
    for  (int i = 0; i < nbr; i++) {

        // must be a string
        if  ((back[i] != null) &&  
             (back[i] instanceof String)) {

              // concat
              S = S.concat((String) back[i]);

        } // endif
    } // end-for
    
    // what came back
    return S;

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
