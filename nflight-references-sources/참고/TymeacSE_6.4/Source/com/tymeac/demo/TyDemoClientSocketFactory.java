package com.tymeac.demo;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.net.*;
import java.rmi.server.*;
/** 
 *  Tymeac demonstration system, custom client socket factory 
 * 
 */

public class TyDemoClientSocketFactory 
    implements   java.io.Serializable,
                RMIClientSocketFactory {

  /**
   * 
   */
  private static final long serialVersionUID = -6011745411369873681L;
  /* 
   * Get the default RMISocketFactory
   * 
   */
  private static RMISocketFactory defaultFactory =
        RMISocketFactory.getDefaultSocketFactory();

/**
 * DemoClientSocketFactory constructor
 */

public TyDemoClientSocketFactory() {

  // the super constructor
  super();

} // end constructor
/**
 * Create a client socket connected to the specified host and port.
 * @param  host   the host name
 * @param  port   the port number
 * @return a socket connected to the specified host and port.
 * @exception IOException if an I/O error occurs during socket creation
 * @since JDK1.2
 */

public Socket createSocket(String host, int port) 
    throws java.io.IOException {

  // just the default factroy
  return defaultFactory.createSocket(host, port); 

} // end-method
} // end-class
