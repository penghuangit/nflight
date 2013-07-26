package com.tymeac.demo;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.base.*;
import java.rmi.server.*;
/**
 * Tymeac demonstration system.  Display, (println), the result of
 *    Tymeac Function Callback.
 * 
 */

public class TyDemoClient_Callback {

/**
 * Request processing of Tymeac Function Callback
 *
 * This requires becoming an RMI Server.  
 *
 *  If you need a security manager for the exportObject(), then add that here.
 *    The TySvrComm Class sets a security manager.
 *   
 * @param args java.lang.String[]
 */ 
public static void main(String args[]) {    
  
  // new string for Tymeac function
  String x = "778899112233445566";

  // make obj
  Object pass = x;

  // new object of the Callback implementation Class
  TyDemoCallbackImpl impl = new TyDemoCallbackImpl();

  // may fail
  try {
    // make me an RMI Server
    UnicastRemoteObject.exportObject(impl);

  } // end-try
  catch (java.rmi.RemoteException e) {

    // say error
    System.out.println("caught RemoteException from exportObject");
    e.printStackTrace();

    // done
    return;

  } // end-catch

  // new pass parm
  TyDemoCallbackParm parm = new TyDemoCallbackParm(impl, // the remote object
                           pass); // the passed object

  // return array from Tymeac
  Object back[] = null;
   
  // form a parameter for Tymeac   
  TymeacParm TP = new TymeacParm(parm,      // data 
                  "Function_Callback", // function name
                  10,            // wait time, ignored
                  1);            // priority
                    
  // do an async request   
  back = new TySvrComm().asyncRequest(TP);
  
  // Should be an array
  if  (back == null)  {
      
      // say no good  
      System.out.println("Did not complete properly");
  
      // kill the RMI Server
      System.exit(0);

  } // endif      
    
  int nbr = 0;
  // number of objects in array
  if  (back != null)
      nbr = back.length;

  // Display string
  String S = "";
  
    /*
     * The returned array contains objects from both Tymeac and the Processing
     *   Application Classes.
     *
     *  The first object is a String cast to an object. It contains the Tymeac
     *    result of the processing: Tymeac SR (0000) 
     *      Tymeac is the prefix. 
     *      SR stands for Synchronous Request, AR stands for an Asynchronous Request. 
     *      (nnnn) Return code, should be 0000.  
     *           When the number in parentheses is not zero, 
     *           then check the return code document for the failure reason.
     *
     *  The other objects in the array are Strings cast to an object. Naturally,
     *    when you develop your own systems, these objects will be other classes.
     * 
     */

  // concatenate all the strings
  for (int i = 0; i < nbr; i++) {

      // must be a string
    if  ((back != null) && 
         (back[i] != null) &&  
         (back[i] instanceof String)) {

        // concat
        S = S.concat((String) back[i]);

    } // endif
  } // end-for
  
  // say what was returned from Tymeac
  System.out.println(S);

  /*
    The Tymeac Server processes the Function.  It is an asynchronous request with
    an output agent queue, (CALD).  The Processing Application Class in Queue, CALD,
    uses the reference to the remote object, (above field, impl), to invoke the
    giveBack() method on this RMI Server, (see class, TyDemoCallbackImpl). That method
    prints the passed String.  A Mickey Mouse demonstration, but it shows that
    callbacks work.

    This Client/RMI Server waits for the callback for 10 seconds.  If it comes back within
    that time then the String prints.  If not, then the CALD Processing Application
    Class may get a remote exception since this JVM no longer exists.

    You may make the wait longer or shorter, or, anything else.
  */
int tt = 0;
while (tt == 0) {
  // wait for the callback, 10 seconds
  try {
  Thread.sleep(10000);
  } 
  catch (java.lang.InterruptedException e) {
  }
}  
  // this kills the RMI Server
  System.exit(0);

   
} // end-method
} // end-class
