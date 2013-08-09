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
 *    Tymeac stalled display
 * 
 */
 
public class TyDemoClient_Stalled {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    int rc;
    
    TyStalledClient client = new TyStalledClient();

    String[] stalled = client.refresh();

    if  (stalled == null) {
        System.out.println("Refresh returned a null result" );
         return;
    }
    
    stalled = client.detail(1L);
    
    if  (stalled == null) {
        System.out.println("Detail returned a null result" );
        return;
    }

    rc = client.purge(1L);

    if  (rc != 2) {
        System.out.println("Purge returned a " + rc);
    }

    rc = client.reSchd(1L);

    if  (rc != 0) {
        System.out.println("ReSchd returned a " + rc);
    }    
    
    
    // request a status and print the result     
    System.out.println("Did good" );
   
    return;
   
} // end-method
} // end-class
