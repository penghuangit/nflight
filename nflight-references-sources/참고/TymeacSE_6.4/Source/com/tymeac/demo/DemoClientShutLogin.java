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

/**
 * @author CoopSoft.com
 *
 * 
 */
public class DemoClientShutLogin extends DemoClientBaseLogin {

/**
 * Request processing of Tymeac Function. You must override this method
 *    and change the function name for [a]syncRequest() methods.
 *    For a shut request you may ignore section 1. and 2.
 *    
 * @param args java.lang.String[]
*/ 
public static void main(String args[]) {
      
                       
  // do a shut down reqeust
  doShut(args, false);
    
  // end
  System.exit(0);
                                      
} // end-method

} // end-class
