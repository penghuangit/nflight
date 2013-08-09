package com.tymeac.demo;

/*
 * Created on Jan 24, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import com.tymeac.base.*;

/**
 * @author CoopSoft.com
 *
 * 
 */
public class DemoClientF7 extends DemoClientBase {

/**
 * Request processing of Tymeac Function. You must override this method
 *    and change the function name for [a]syncRequest() methods.
 *    For a shut request you may ignore section 1. and 2.
 *    
 * @param args java.lang.String[]
*/ 
public static void main(String args[]) {
      
  // new string for Tymeac function
  String x = "223344556677889911";

  // make obj
  Object pass = x; 
 
  // form a parameter for Tymeac   
  TP = new TymeacParm(pass,         // data 
                      "Function_7", // function name *--- change this ---*
                      10,            // wait time
                      1);            // priority
                      
  // do an async reqeust
  doAsync(args);
    
  // end
  System.exit(0);
                                      
} // end-method

} // end-class
