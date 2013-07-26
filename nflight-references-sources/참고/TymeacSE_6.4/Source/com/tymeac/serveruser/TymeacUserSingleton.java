package com.tymeac.serveruser;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * This Singleton class contains information for your application. You may put 
 * anything in here you need. Tymeac maintains an instance of this class so all
 * that is here is persistent.
 *   
 */
public final class TymeacUserSingleton {

  // private Class field

      // static reference to this Singleton
      private static TymeacUserSingleton me = null;
      
  // private instance fields for your use

/**
 * Private Constructor, can only be called by the class itself.
 */
private TymeacUserSingleton() {

} // end-constructor

/**
 * Public static method to return the object. During Tymeac start up only one thread
 *   may execute this public method therefore, no sync statement is necessary.
 * @return TymeacUserSingleton
 */
public static TymeacUserSingleton getInstance() {

  // When not here
  if  (me == null) {

      // new instance
      me = new TymeacUserSingleton();

  } // endif  
  
  // instance of
  return me;

} // end-method

} // end-class
