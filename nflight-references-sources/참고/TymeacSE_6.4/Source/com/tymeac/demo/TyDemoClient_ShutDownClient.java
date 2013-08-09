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
 *    Tymeac shutdown
 * 
 */
 
public class TyDemoClient_ShutDownClient {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
      
    // new client object
    TyShutdownClient client = new TyShutdownClient();

    // use one or the other
    
    // this is a normal shut down
    String back =  client.shut();
    
    // this is a forced shut down
    // String back =  client.kill();
    
    System.out.println("status is " + back);
   
    return;
   
} // end-method
} // end-class
