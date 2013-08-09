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
 * Tymeac demonstration system.  Display, (println), the result of a
 *    Tymeac purge request
 * 
 */
 
public class TyDemoClient_PurgeRequest {  

/**
 * Request a status of an async request from the Tymeac Server. 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    // put the Tymeac Millitime here
    long id = 945447907321L;
    
    // put the Request id here
    long req = 2L; 
    
    // request a status and print the result     
    System.out.println("Returned=" + new TyReqStatusClient().cancelReq(id, req));
   
    return;
   
} // end-method
} // end-class
