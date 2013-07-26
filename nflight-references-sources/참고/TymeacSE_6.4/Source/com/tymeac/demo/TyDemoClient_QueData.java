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
import com.tymeac.client.*;
/**
 * Tymeac demonstration system. Display, (println), the result of
 *    Tymeac queue data
 * 
 */
 
public class TyDemoClient_QueData {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    int rc;
    String que = "AAAA";
    TyQueDataClient client = new TyQueDataClient();

    TyQueElements back =  client.importElements(que);

    if  (back == null) {
        System.out.println("Import returned a null result" );
         return;
    }

    rc =  client.updateElements(back);
    if  (rc != 4) {
        System.out.println("Update returned a " + rc);
         return;
    }
    
    // request a status and print the result     
    System.out.println("Did good" );
   
    return;
   
} // end-method
} // end-class
