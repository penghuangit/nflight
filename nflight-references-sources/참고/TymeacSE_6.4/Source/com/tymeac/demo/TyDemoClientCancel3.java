package com.tymeac.demo;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.base.*;

import java.util.*;

 /**
 * Tymeac demonstration system. This is an example of how to cancel
 *    a syncReq() using a cancel word
 *   - start a new thread to do the cancel  
 *   - call the Tymeac Server with a syncReq()
 *   - new thread calls the Tymeac Server cancelSyncReq() method
 *
 *  In order for a cancel to work, the function on the server must be delayed.
 *    Otherwise, the syncReq() would return before the cancel logic was reached.
 *    When using a Tymeac Function_4, three queues are in use. These queues use
 *    applications com.tymeac.serveruser.Demo1, ...Demo2 and ...Demo3. Put a delay
 *    in Demo3.main() such as Thread.sleep(5000). This gives the cancel logic enough
 *    time to work. You may also use the com.tymeac.demo.JFrame.T3 class to start 
 *    enough thread to load the server. 
 */
 
public class TyDemoClientCancel3 {    
  
/**
 * Request processing of cancel request
 * @param args java.lang.String[]
 */ 
public static void main(String args[]) {   
  
  // new object
  TyDemoClientCancel3 can = new TyDemoClientCancel3();
  
  // do the work
  can.doWork();        
     
  // end the JVM since an RMI Server persists
  System.exit(0);
   
} // end-method

private void doWork() {

    // server
    TySvrComm svr = new TySvrComm();
  
    // new string for Tymeac function
    String x = "112233445566778899";

    // make obj
    Object pass = x;
    
    // cancel req
    Random random = new Random();
    long cancel_word = random.nextLong();
    
    CancelParm cp = new CancelParm(cancel_word); 

    // return array
    Object back[] = null;
       
    // form a parameter for Tymeac   
    TymeacParm TP = new TymeacParm(pass,        // data                                
                                  "Function_4", // function name
                                  10,           // wait time
                                  1,            // priority
                                  cancel_word); // cancel word

    // new thread
    new TyDemoClientCancelThread3Thread(svr, cp).start();
                         
    // do a sync request   
    back = svr.syncRequest(TP);
  
    // reformat the return array
    TymeacReturn ret = TymeacReturn.formatCallReturn(back);
    
    // return code from tymeac
    int rc = ret.getReturnCode();
    
    // When no back
    if  (rc == 9001)  {
          
        // say no good  
        System.out.println("back returned null");
        return;
  
    } // endif  
  
    // When any error
    if  (rc != 0) {
          
        System.out.println(ret.getTyMessage()); // problem
        return;
        
    } // endif 
      
    // say what
    System.out.println(ret.getTyMessage());       
  
} // end-method  

} // end-class
