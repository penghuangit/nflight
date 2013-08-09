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
 * Demonstration of Tymeac Callback Demonstration Queue, CALD.
 *
 *
 */

public class DemoAgentCallback {  

/**
 * Invoked method by java.lang.reflect.Method
 *
 * This is an example of an Output Agent method.  The Object[]
 *    passed to this method is a concatenation of the "@return String's"
 *    of all the other Queue's in the Tymeac Function.
 *    There is no return from this method.   
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

  // the output object
  TyDemoCallbackParm parm = null;

    // the parm contains two fields
    //    1st is the remote object, (TyDemoCallback)
    //    2nd is the String from the non-OA Queue Threads
  /*
    try {
      Thread.sleep(1200000);
    } 
    catch (java.lang.InterruptedException e) {
    }
  */
  
/*  
  try {
    // the passed object array
    Object[] what = (Object[]) args[0];

    // length of array
    int nbr = what.length;

    // When none, bye
    if  (nbr < 1)  return;

    // base data          
    String out = "DemoAgentCallback ==>";

    // concat all the passed strings  
    for (int i = 0; i < nbr; i++) {

      // each object
      parm = (TyDemoCallbackParm) what[i];

      // concatenate the strings
      out = out.concat((String) parm.getObj());

    } // end-for
  
    // callback remote object
    TyDemoCallback impl = parm.getCb();

    // may fail
    try {
      // exec method on remote object
      impl.giveBack((Object) out);

    } // end-try
    catch (java.rmi.RemoteException e) {

      // caller may not be alive
      System.out.println("RemoteException in callback agent");

    } // end-catch
  } // end-try
   
  catch (java.lang.Throwable e) {
      
    //e.printStackTrace(System.out);
    
    throw  e; 
  } // end-catch  
  */
} // end-method
} // end-class
