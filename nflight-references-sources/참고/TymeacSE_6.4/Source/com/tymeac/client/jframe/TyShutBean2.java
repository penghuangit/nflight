package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.base.*;
import com.tymeac.client.*;

/**
 * Backend object for shutdown GUI
 */
public class TyShutBean2 {
	
	// result
	private String shut_Data  = "Failure";

	// where access logic lives
  private TyShutdownClient module = null; 
	
/**
 * Constructor 
 */
public TyShutBean2 ( ) {

  // new access module
  module = new TyShutdownClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyShutBean2(TymeacInterface TyI) {

  // new access module
  module = new TyShutdownClient(TyI);

} // end-constructor

/**
 * accessor for shutdown message
 * @return int
 */ 
public String getShutData () { return shut_Data; } // end-method

/**
 * Kill Button 
 */
public void killButton() {
        
  // do a shut down
  String R = module.kill(); 
  
  // When nothing, leave alone
  if  (R == null || 
       R.length() < 2) {

      // error         
      shut_Data = "Communication failure";
  }
  else {
      // good
      shut_Data = R;

  } // endif  
    
} // end-method

/**
 * Shut Down Button
 */
public void shutButton() {
        
  // do a shut down
	String R = module.shut(); 
  
  // When nothing, leave alone
  if  (R == null || 
       R.length() < 2) {

      // error         
      shut_Data = "Communication failure";
  }
  else {
      // good
      shut_Data = R;

  } // endif  
    
} // end-method
} // end-class
