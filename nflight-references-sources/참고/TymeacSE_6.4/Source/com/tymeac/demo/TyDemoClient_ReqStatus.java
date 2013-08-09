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
 *    Tymeac request status
 * 
 */
 
public class TyDemoClient_ReqStatus {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    // put the tymeac start up time here
    long ty = 1073857317478L;

    // put the async request id here
    long req = 1L;

    // do a status request 
    int rc =  new TyReqStatusClient().getStatus(ty, req);
    
    System.out.println("status is " + rc);
    
    // do a cancel request 
    rc =  new TyReqStatusClient().cancelReq(ty, req);    
    
    System.out.println("status is " + rc);
   
    return;
   
} // end-method
} // end-class
