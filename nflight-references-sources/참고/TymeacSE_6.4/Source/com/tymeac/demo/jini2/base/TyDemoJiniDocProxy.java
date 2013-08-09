package com.tymeac.demo.jini2.base;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.io.Serializable;

/**
 * Tymeac Jini Demonstration System, document service.
 *
 *  This is the proxy used in the document system.
 *
 */
public class TyDemoJiniDocProxy 
	implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6685113924853263131L;
	// the document service
	TyDemoJiniDocInterface backend;   

/**
 * Constructor save the backend remote object
 * @param c_backend TyDemoJiniDocInterface - remote object
 */
public TyDemoJiniDocProxy(TyDemoJiniDocInterface c_backend) {
    
  // save the object
  backend = c_backend;
      
} // end-constructor

/**
 * accessor for backend object
 * @return TyDemoJiniDocInterface
 */
public TyDemoJiniDocInterface getRemoteObject () {
    
  // return the instance field
  return backend;
      
} // end-method 
} // end-class
