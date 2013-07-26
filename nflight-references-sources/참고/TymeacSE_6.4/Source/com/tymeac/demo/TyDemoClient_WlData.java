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
 *    Tymeac wait list display
 * 
 */
 
public class TyDemoClient_WlData {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    String que = "AAAA";
    TyWlDataClient client = new TyWlDataClient();

    TyWLElements[] back =  client.refresh(que);

    if  (back == null) {
        System.out.println("Reresh returned a null result" );
         return;
    }

    
    // request a status and print the result     
    System.out.println("Did good" );
   
    return;
   
} // end-method
} // end-class
