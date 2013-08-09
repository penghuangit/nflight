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

public class DemoAgent1 {  

/**
 * Invoked method by java.lang.reflect.Method
 *
 * This is an example of an Output Agent method.  The single String
 *    passed to this method is a concatenation of the "@return String's"
 *    of all the other Queue's in the Tymeac Function.
 *    What this method does is your need.  There is no return from this
 *    method.   
 *
 * 
 * @param args Object[]
 * @exception java.lang.Throwable Your exception
 */
public static void main(Object[] args)  
                        throws  java.lang.Throwable {

/*
  
  Catch your exception.  Process as needed.  Then reThrow the exception.
  
    The Queue Thread that invoked this method catches exceptions and
    does a clean-up procedure.  The procedure disables this Thread and
    sends messages to the Log and Notification facility. 
    
   
*/

  // put your code here
  try { 

   /* This is dummied          

      // when nothing, get out 
      if  (args[0] == null) {
       
          System.out.println("DemoAgent1 ==> args null");
          
        return;
        
      }
      
      // number of objects passed
      int nbr = 0;

      // the passed object array
      Object[] what = null;

      // arg is really an object[]        
      what = (Object[]) args[0];  

      // length of array
      nbr = what.length;

      // When none, bye
      if  (nbr < 1)  return;

      // base data          
      String out = "DemoAgent1 ==>";

      // concat all the passed strings  
      for  (int i = 0; i < nbr; i++) {

        if  (what[i] instanceof String) {
      
            // each string
            out = out.concat((String) what[i]);
          
        } // endif
      } // end-for
  
      // put out
      System.out.println(out);    


      */ // the above was dummied

        
  } // end-try
   
  catch (java.lang.Throwable e) {
        
      //e.printStackTrace(System.out);
      
      throw  e; 
  } // end-catch  
    
} // end-method
} // end-class
