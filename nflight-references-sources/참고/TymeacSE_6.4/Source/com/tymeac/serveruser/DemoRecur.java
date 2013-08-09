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

import com.tymeac.base.*;
/**
 *  Demonstration of Tymeac recurrsive functionality. 
 *  
 *
 */

public class DemoRecur {  

/**
 * This method forms a return message.  Then it calls Tymeac for
 *    Function_2 and concatenates that return message with the
 *    first.
 *   
 * Invoked method by java.lang.reflect.Method
 * 
 * @param args Object[]
 * @return Object
 * @exception java.lang.Throwable Your exception
 * 
 */
public static Object main(Object args[]) 
            throws  java.lang.Throwable {

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


  try {
      // Should be a string passed
      if  (args[0] instanceof String) {
      
        ;
      }
      else {
        return null;
      } // endif      
  

      // passed string
      String Begin = (String) args[0];
      
      // see what we have
      if  (Begin != null) {
       
        // separate digits
        
        int len = Begin.length();
        
        // When 3 sets of 2 are present
        if  (len > 5) {
        
            // all are here
            A = Begin.substring(0,2);
            B = Begin.substring(2,4);
            C = Begin.substring(4,6);
        }   
        else {
            // When only 2 sets of 2 are present
            if  (len > 3) {
        
              // do two
              A = Begin.substring(0,2);
              B = Begin.substring(2,4);     
            }
            else {      
              // When only 1 set of 2 are present
              if  (len > 1) {
        
                  // do one
                  A = Begin.substring(0,2);
              } // endif      
            } // endif
        } // endif            
      } // endif                  
  
      // initially:
      int a = 0, b = 0, c = 0, r = 0;
  
      // convert passed digits to integer

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
  
  
      // sum the passed digits
      r = a + b + c;
  
      // new string to pass back
      String S = "#DemoRecur: "
            + "A1(" 
            + A 
            + ")+A2(" 
            + B 
            + ")+A3(" 
            + C 
            + ")=("
            + r
            + ")#";   
  
      /*
      * The basic return string is finished.  Now, to demonstrate the
      *   recurrsive ability of Tymeac.  The method acts as a Client and
      *   calls the Tymeac Server for Function_2 with a synchronous
      *   request.  The return string from this call is appended to the
      *   basic return string and all passes back to the original client.
      * 
      * The second arg passed to this invoked method is the Tymeac Server
      *   interface.  The object is cast to: TymeacInterface  and used in the
      *   second constructor for TySvrComm, (signature: TySvrComm(TymeacInterface Ty) ).
      * 
      */
   
      // new string for Tymeac return
      String x = "223344556677889911";
      Object pass = x;
      Object back[] = null;

       
      // form a parameter for Tymeac   
      TymeacParm TP = new TymeacParm(pass,         // data 
                    "Function_2", // function name
                    10,            // wait time
                    1);            // priority
                    
      // second arg is TymeacInterface

      // do a sync request   
      back = ((TymeacInterface)args[1]).syncRequest(TP);
  
      // Should be an array
      if  (back == null)  {
      
        String Z = S + " Recursion attempt did not complete properly";
        return  Z;
      } // endif      
  
      // number of objects in array
      int nbr = back.length;

      // concatenate all the strings
      for  (int i = 0; i < nbr; i++) {

        // When a good string
        if  (back[i] != null) {

          // concat
          S = S.concat((String) back[i]);

        } // endif
      } // end-for
  
      // return all values
      return S;
    
  } // end-try
   
  catch (java.lang.Throwable e) {
      
      //e.printStackTrace(System.out);
    
      throw  e; 
  } // end-catch  
  
} // end-method
} // end-class
