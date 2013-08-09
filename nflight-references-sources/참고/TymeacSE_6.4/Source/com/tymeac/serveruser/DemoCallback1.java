package com.tymeac.serveruser;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.demo.*;
/**
 * Tymeac Demonstration System. 
 *   Invoked method for processing Tymeac Callback Demonstration Queue, CALA.  
 *
 *
 */

public class DemoCallback1 {  

/**
 * Invoked method by java.lang.reflect.Method
 * 
 * @param args Object[]
 * @return Object
 * @exception java.lang.Throwable Your exception
 */
public static Object main(Object args[])
            throws  java.lang.Throwable    {

/*
  Catch your exception.  Process as needed.  Then reThrow the exception.
  
    The Queue Thread that invoked this method catches exceptions and
    does a clean-up procedure.  The procedure disables this Thread and
    sends messages to the Log and Notification facility.
*/

    // passed values defaults
    String A = "00";
    String B = "00";
    String C = "00";

    // passed parm
    TyDemoCallbackParm parm = null;

    // output parm
    TyDemoCallbackParm out_parm = null;

    // input string
    String input = null;

    try {
          // Should be parm passed
          if  (args[0] instanceof TyDemoCallbackParm) {
            
              // ok
              parm = (TyDemoCallbackParm) args[0];
          }
          else {
              // no good
              return null;

          } // endif

          // Should be a string passed
          if  (parm.getObj() instanceof String) {
            
              // ok
              input = (String) parm.getObj();
          }
          else {
              // no good
              return null;

          } // endif
          
          // see what we have
          if  (input != null) {
             
                // separate digits
                
                int len = input.length();
                
                // When 3 sets of 2 are present
                if  (len > 5) {
                
                      // all are here
                      A = input.substring(0,2);
                      B = input.substring(2,4);
                      C = input.substring(4,6);
                }   
                else {
                      // When only 2 sets of 2 are present
                      if  (len > 3) {
                
                            // do two
                            A = input.substring(0,2);
                            B = input.substring(2,4);     
                      }
                      else {      
                            // When only 1 set of 2 are present
                            if  (len > 1) {
                
                                  // do one
                                  A = input.substring(0,2);
                            } // endif      
                      } // endif
                } // endif            
          } // endif                  
                            
                            
          // initially:
          int a = 0, b = 0, c = 0, r = 0;
    
          // convert digits to integers

          // try to convert A
          try {
              a = Integer.parseInt(A);
          } catch (NumberFormatException e) {}

          // try to convert B
          try {
              b = Integer.parseInt(B);
          } catch (NumberFormatException e) {};

          // try to convert C
          try {
              c = Integer.parseInt(C);
          } catch (NumberFormatException e) {};
  
 
          // sum digits
          r = a + b + c;
    
          // form return value
          String S = "#DemoCallback1: "
                        + "A1(" 
                        + A 
                        + ")+A2(" 
                        + B 
                        + ")+A3(" 
                        + C 
                        + ")=("
                        + r
                        + ")#";

          // new output parm
          out_parm = new TyDemoCallbackParm(parm.getCb(), S);

          // back to thread
          return out_parm;
    
    } // end-try
     
    catch (java.lang.Throwable e) {
          
        //e.printStackTrace(System.out);
        
        throw  e; 
    } // end-catch  
    
} // end-method
} // end-class
