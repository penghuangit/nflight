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

import com.tymeac.client.*;
/**
 * Tymeac demonstration system. Display, (println), the result of
 *    Tymeac Function Data.  
 * 
 */
 
public class TyDemoClient_FuncData {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
        
    TyFuncDataClient client = new TyFuncDataClient();

    String[] back =  client.refresh("F");

    if  (back == null) {
        System.out.println("Refresh returned a null result" );
         return;
    }

     // number of objects in array
    int nbr = back.length;

    // concatenate all the strings
    for  (int i = 0; i < nbr; i++) {

          // must be a string
        if  (back[i] != null) {

              // concat
              System.out.println(back[i]);

        } // endif
    } // end-for
    
    System.out.println("Good ");
   
    return;
   
} // end-method
} // end-class
