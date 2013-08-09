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
 * Backend object for statistics GUI
 */
public class TyStatsBean {
	
	// indicators
	private int get_Result;

	// where access logic lives
  private TyStatsClient module = null; 		  

/**
 * Constructor
 */
public TyStatsBean ( ) {

  // new access module
  module = new TyStatsClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyStatsBean(TymeacInterface TyI) {

	  
  // new access module
  module = new TyStatsClient(TyI);

} // end-constructor

/**
 * accessor for get result
 * @return int
 */ 
public int getGetResult () { return get_Result; } // end-method

/**
 * Write statistics button
 */
public void writeButton() { 
  
  // do write  
  get_Result = module.writeStats();  
    
} // end-method
} // end-class
