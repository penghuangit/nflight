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
 *    Tymeac queue threads
 * 
 */
 
public class TyDemoClient_QueThd { 

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    int rc;
    String que = "AAAA";
    TyQueThdClient client = new TyQueThdClient();

    String[] back =  client.refresh(que);

    if  (back == null) {
        System.out.println("Reresh returned a null result" );
         return;
    }

    rc =  client.disable(que, 0);
    if  (rc != 4) {
        System.out.println("Disable returned a " + rc);
         return;
    }

    rc =  client.enable(que);
    if  (rc != 0) {
        System.out.println("Enable returned a " + rc);
         return;
    }
    
    // request a status and print the result     
    System.out.println("Did good" );
   
    return;
   
} // end-method
} // end-class
