package com.tymeac.base;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Class loader for Strings
 * 
 */

public final class TyClassLoader extends ClassLoader {

/**
 * TyClassLoader constructor 
 */

public TyClassLoader() {

  super();

} // end-constructor
/**
 * Tymeac loadClass method
 * @param name String - name of class to load
 * @param resolve boolean - true is to resolve the class
 */

protected synchronized Class<?> loadClass(String name, boolean resolve) 
          throws ClassNotFoundException {
   
    // only system, (local), class
    return findSystemClass(name); 

} // end-method
} // end-class
