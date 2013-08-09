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

import javax.swing.*;
import com.tymeac.base.*;
import com.tymeac.client.*;
/**
 * Backend object for the New Copy GUI
 */
public class TyNewCopyBean2 {
	
	// indicators
	private int load_Result;
		
	// from the screen
	private String que_name = null;
	private String class_name = null;

	 // where access logic lives
  private TyNewCopyClient module = null; 
	
/**
 * Constructor
 */
public TyNewCopyBean2 ( ) {

  // new access module
  module = new TyNewCopyClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyNewCopyBean2(TymeacInterface TyI) {

  // new access module
  module = new TyNewCopyClient(TyI);

} // end-constructor

/**
 * accessor for load result
 * @return int
 */ 
public int getLoadResult () {
  
  return load_Result;
    
} // end-method

/**
 * Load button
 * @param S_que_name JTextField - name of queue
 * @param S_class_name JTextField - name of new appl class
 */
public void loadButton( JTextField S_que_name,
						JTextField S_class_name) {
	
	// set the queue name, class name
	que_name    = S_que_name.getText();

	// When no que name
	if  ((que_name == null) || (que_name.length() < 1)) {  

			// no name
			load_Result = 5;

			// done
			return;

	} // endif

	// class name
	class_name  = S_class_name.getText();

  // do the access
	load_Result = module.load(que_name, class_name);
	
} // end-method
} // end-class
