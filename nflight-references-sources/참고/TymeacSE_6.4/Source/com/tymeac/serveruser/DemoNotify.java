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

/**
 * Demonstration of Tymeac functionality. 
 *
 * 
 */
 
public class DemoNotify {  

/**
 * Invoked method by java.lang.reflect.Method
 *
 * This is an example of a Notification Method.
 *    The passed single String is a message developed by Tymeac Server
 *    modules during execution.  Where to send this String.  What to do
 *    with this String is a customer decision. 
 * 
 * @param args Object[]
 * @exception java.lang.Throwable Your exception
 * 
 */
public static void main(Object args[]) 
                        throws  java.lang.Throwable {


/*
  
  Catch your exception.  Process as needed.  Then reThrow the exception.
  
    The Queue Thread that invoked this method catches exceptions and
    does a clean-up procedure.  The procedure disables this Thread and
    sends a message to the Log. 
    
   
*/

  try { 
    // Should be a string passed
    if  (args[0] instanceof String) {
      
        ;
    }
    else {
        return;
    } // endif      

    // passed string
    String S = (String) args[0];

    // When no message was passed, ignore
    //  This is used by the Tymeac Start Up procedure to verify that
    //  the function is working.  
    if  ((S == null) ||
          (S.length() == 0)) {
          ;
    }
    else {  
          // put your code here
               
          System.out.println("Notification: " + S);
          
          return;
          
    } // endif      
  
  } // end-try
   
  catch (java.lang.Throwable e) {
        
    //e.printStackTrace(System.out);
    
    throw  e;
    
  } // end-catch  
    
} // end-method
} // end-class
