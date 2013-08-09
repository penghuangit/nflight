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
 * Tymeac demonstration system. Internal client
 */
 
public class TyDemoInternalClient {  

/**
 * The sequence is the following. -breakiterator
 * 1. start the server
 * 2. send a sync request for Function_1
 * 3. wait 10 seconds
 * 4. shut down the server
 * 5. wait 10 seconds
 * 6. shut down again    
 *
 *
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
// 1.
    // new server instance
    InternalServer impl = new InternalServer();  

    // passed arg is "only stand alone"      
    String[] in = {"-s"};

    // create the server
    TymeacInterface ti = impl.createServer(in);


// 2.
    // new string for Tymeac function
    String x = "112233445566778899";

    // make obj
    Object pass = x;

    // return array
    Object back[] = null;
   
    // form a parameter for Tymeac   
    TymeacParm TP = new TymeacParm(pass,         // data 
                                  "Function_1", // function name
                                  10,            // wait time
                                  1);            // priority
           
    try {                             
        // do a sync request   
        back = ti.syncRequest(TP);
    }
    catch (Exception e) {}
    
    // reformat the return array
    TymeacReturn ret = TymeacReturn.formatCallReturn(back);
    
    // return code from tymeac
    int rc = ret.getReturnCode();
    
    // When no back
    if  (rc == 9001)  {
          
        // say no good  
        System.out.println("Returned object[] is null");
        System.exit(0);
  
    } // endif  
  
    // When any error
    if  (rc != 0) {
          
        System.out.println(ret.getTyMessage()); // problem
        System.exit(0);
        
    } // endif 
    
    Object user_data = ret.getUserData();
    
    // should not be null
    if  ((user_data == null) | (!(user_data instanceof String)))  {
      
        // say no good  
        System.out.println("Did not complete properly");
        System.exit(0);
  
    } // endif  
    
    // say what
    System.out.println(ret.getTyMessage()+ user_data);

// 3.
    // wait 10 seconds
    try {
        Thread.sleep(10000);

    }
    catch (InterruptedException e) {}

// 4.
    // return string from a shut down
    String end = null;

    // shut down the server
    try {
        end = ti.shutRequest();
    }
    catch (Exception e) {}

    // print the result.     
    System.out.println(end);

// 5.
    // wait 10 seconds in case the last shut request was not complete
    try {
      Thread.sleep(10000);

    }
    catch (InterruptedException e) {}

// 6.
    // shut down the server
    try {
        end = ti.shutRequest();
    }
    catch (Exception e) {}

    // print the result.     
    System.out.println(end);

    System.exit(0);
   
} // end-method
} // end-class
