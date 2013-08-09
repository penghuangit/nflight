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
/**
 * Tymeac demonstration system.  Display, (println), the results of
 *    Tymeac Functions 1 - 7. 
 * 
 */
 
public class TyDemoClient_multi {  

/**
 * Request processing of Tymeac Functions 1 - 7. 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    // new string for Tymeac function
    String x1 = "112233445566778899";
    String x2 = "223344556677889911";
    String x3 = "334455667788991122";
    String x4 = "445566778899112233";
    String x5 = "556677889911223344";
    String x6 = "667788991122334455";
    String x7 = "778899112233445566";

    // make obj
    Object pass1 = x1;
    Object pass2 = x2;
    Object pass3 = x3;
    Object pass4 = x4;
    Object pass5 = x5;
    Object pass6 = x6;
    Object pass7 = x7;

    // return array
    Object back[] = null;
    
    // form paramters for each of the functions 
    TymeacParm TP1 = new TymeacParm(pass1, "Function_1", 10, 1);
    TymeacParm TP2 = new TymeacParm(pass2, "Function_2", 10, 2);
    TymeacParm TP3 = new TymeacParm(pass3, "Function_3", 10, 3);
    TymeacParm TP4 = new TymeacParm(pass4, "Function_4", 10, 4);
    TymeacParm TP5 = new TymeacParm(pass5, "Function_5", 10, 5);
    TymeacParm TP6 = new TymeacParm(pass6, "Function_6", 10, 6);
    TymeacParm TP7 = new TymeacParm(pass7, "Function_7", 10, 7);
   
    // get a new RMI methods object
    TySvrComm TSC = new TySvrComm();
         
    // do each function, printing the results 
    back = TSC.syncRequest(TP1);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP1 did not complete properly");
    }
    else {
        printBack(back);

    } // endif    

    back = TSC.syncRequest(TP2);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP2 did not complete properly");
    }
    else {
        printBack(back);

    } // endif
    
    back = TSC.syncRequest(TP3);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP3 did not complete properly");
    }
    else {
        printBack(back);

    } // endif    

    back = TSC.syncRequest(TP4);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP4 did not complete properly");
    }
    else {
        printBack(back);

    } // endif  

    back = TSC.asyncRequest(TP5);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP5 did not complete properly");
    }
    else {
        printBack(back);

    } // endif    

    back = TSC.asyncRequest(TP6);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP6 did not complete properly");
    }
    else {
        printBack(back);

    } // endif    

    back = TSC.asyncRequest(TP7);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP7 did not complete properly");
    }
    else {
        printBack(back);

    } // endif  
   
   return;
  
} // end-method
/**
 * Concatenate the string data
 * @param pass java.lang.Object[]
 */
private static void printBack(Object[] pass) {

    // number of objects in array
    int nbr = pass.length;

    // Display string
    String S = "";

    // concatenate all the strings
    for  (int i = 0; i < nbr; i++) {

          // concat
          S = S.concat((String) pass[i]);

    } // end-for
    
    // say what
    System.out.println(S);

} // end-method
} // end-class
