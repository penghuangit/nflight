package com.tymeac.base;

/* 
 * Copyright (c) 2002 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.net.*;

/**
 * Hold a complex name as its component URL and Class
 */
public final class ClsUrl {

  // the result  0, bad full name, 1, only class, 2, class and url, 3 bad class name 4, bad url name
  private int result;

  // separated class name
  private String className;

  // separated url names
  private URL[] urlName;

/**
 * Constructor 
 */
protected ClsUrl() {

  result     = 0;
  className = null;
  urlName   = null;

} // end-constructor

/**
 * Accessor for the class name
 * @return java.lang.String
 */
protected String getClassName() { return className; } // end-method

/**
 * Accessor for the result
 * @return int
 */
protected int getResult() { return result; } // end-method

/**
 * Accessor for the url name
 * @return java.net.URL[] 
 */
protected URL[] getUrlName() { return urlName; } // end-method

/**
 * Set a new class name
 * @param newClassName java.lang.String
 */
protected void setClassName(String newClassName) { className = newClassName; } // end-method

/**
 * Set a new result
 * @param newResult int
 */
protected void setResult(int newResult) { result = newResult; } // end-method

/**
 * Set a new url name
 * @param newUrlName java.net.URL[]
 */
protected void setUrlName(URL[] newUrlName) { urlName = newUrlName; } // end-method

} // end-class
