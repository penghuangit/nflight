package com.tymeac.jini.client;

/*
 * Created on Jan 28, 2004
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
public class JiniClientF7 extends JiniClientBase {
  
/**
 * Request processing of Tymeac Function 7. 
 *    
 * @param args java.lang.String[]
*/ 
public static void main(String args[]) {
    
//1.    
  // new string for Tymeac function
  String x = "223344556677889911";

  // make obj
  Object pass = x;
   
//2.   
  // form a parameter for Tymeac   
  TP = new TymeacParm(pass,         // data 
                      "Function_7", // function name *--- change this ---*
                      10,            // wait time
                      1);            // priority
                      
// 3.
  // doAsync(args) or
  // doSync(args) or
  // doShut(args) 
  doAsync(args);

// 4.    
  // end
  System.exit(0);
                                      
} // end-method

} // end-class
