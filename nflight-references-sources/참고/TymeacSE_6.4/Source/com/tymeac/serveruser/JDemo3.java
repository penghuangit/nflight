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
 * Tymeac Demonstration System. 
 *   Invoked method for processing Tymeac Demonstration Queue, JQue3  
 *
 *
 */

public class JDemo3 {  

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
  
  try {
      // Should be a string passed
      if  (args[0] instanceof String) {
      
        ;
      }
      else {
        return null;
      } // endif      
  
      // passed string
      String In = (String) args[0]; 
      
      String back = "(0000)DBMS: " + In + ", Stored in DBMS successfully.";
      
      return back; 
  
  } // end-try
   
  catch (java.lang.Throwable e) {
      
    //e.printStackTrace(System.out);
    
    throw  e; 
  } // end-catch              
  
  
} // end-method
} // end-class
