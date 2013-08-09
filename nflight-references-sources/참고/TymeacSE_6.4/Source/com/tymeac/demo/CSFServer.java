/*
 * Created on Mar 13, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
package com.tymeac.demo;
import com.tymeac.base.*;

/**
 * @author CoopSoft.com
 *
 * 
 */
public class CSFServer 
    extends com.tymeac.base.CSFServer {

  /**
   * 
   */
public CSFServer() {
    super();
    //
} // end-constructor

/**
 * Create the client and server socket factories. This is your code.
 * @return
 */
public boolean createFactory() {
  
  try {
    // new client factory
    client = new TyDemoClientSocketFactory();  
    
    // new server factory
    server = new TyDemoServerSocketFactory();

  } // end-try
  
  catch (Exception e) {

    // say what
    msgOut(TyMsg.getMsg(90) + e. toString(), 10);
      
    // get out      
    return false;   
   
  } // end-catch 

  // good
  return true;

} // end-method  
} // end-class
