package com.tymeac.base;

/*
 * Copyright (c) 2004 - 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

/**
 * Tymeac Internal Server
 */
public class InternalServer 
        extends BaseServer {

/**
 * Constructor
 */
public InternalServer() {

} // end-constructor

/**
 * starts the application
 * @param args java.lang.String[]
 */
public TymeacInterface createServer(String[] args) { 
  
  // When start up failed
  if  (!init(args, Startup.startup_internal)) {
    
      // say ending
      TyBase.printMsg(TyMsg.getMsg(98));
      
      // cancel run
      return null;
      
  } // endif    
  
  // set alive
  setReady(TymeacInfo.INTERNAL); 
  
  // OK
  return getImpl();
  
} // end-method

/**
 * Export is only local
 */  
protected void doExport() {

  // new interface
  TymeacStandardImpl impl = new TymeacStandardImpl(T);

  // load base
  T.setTi(impl);

  // save impl class
  setImpl(impl);

  // good bind
  setGood();

} // end-method
} // end-class
