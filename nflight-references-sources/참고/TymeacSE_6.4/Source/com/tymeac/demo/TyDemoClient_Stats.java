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
 *    Tymeac stats request
 * 
 */
 
public class TyDemoClient_Stats {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
      
    
    TyStatsClient client = new TyStatsClient();

    int rc =  client.writeStats();
    
    
    System.out.println("status is " + rc);
   
    return;
   
} // end-method
} // end-class
