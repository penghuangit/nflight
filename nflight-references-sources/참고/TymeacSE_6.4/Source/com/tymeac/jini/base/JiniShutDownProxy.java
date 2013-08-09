package com.tymeac.jini.base;

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
 * Just a simple proxy with the only field the remote object. 
 *
 */
public class JiniShutDownProxy 
	implements Serializable {
	
	static final long serialVersionUID = 2861251629877587004L;
	
	// instance field is backend remote object
	JiniFrontEndShutDown backend;   

/**
 * no-arg constructor
 */
public JiniShutDownProxy() {
    
} // end-constructor

/**
 * Constructor - just save the backend remote object
 * @param c_backend JiniFrontEndShutDown - remote object
 */ 
public JiniShutDownProxy(JiniFrontEndShutDown c_backend) {
    
  // save the object
  backend = c_backend;
      
} // end-constructor

/**
 * accessor for backend object
 * @return JiniFrontEndShutDown
 */
public JiniFrontEndShutDown getRemoteObject() {
    
  // return the instance field
  return backend;
      
} // end-method 
} // end-class
