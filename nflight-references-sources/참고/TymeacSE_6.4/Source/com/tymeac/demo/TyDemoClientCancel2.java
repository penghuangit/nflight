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

import java.rmi.server.*;

 /**
 * Tymeac demonstration system. This is an example of how to cancel
 *    a syncReq() Tymeac Function multiple times.
 *
 *  In order to use a separate thread the ready() method must be able to
 *    find that thread. Having this class save the reference to the thread,
 *    implement InterruptionReady and exporting itself as a remote object does
 *    not work. The callback executes as a separate RMI Connection Thread and
 *    does not have access to this object's fields. The simple solution is to
 *    make a separate Impl class with the reference to the worker thread therein.
 * 
 * 
 *   - create the TySvrComm object
 *   - create the new thread with a reference to TySvrComm
 *   - create the impl object with the new thread reference
 *   - export the impl as a remote object
 *   - call the Tymeac Server with a syncReq()
 *   - the Tymeac Server calls back to method ready()in the impl object
 *   - ready() starts a new thread passing the parameters from the Tymeac Server
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
 
public class TyDemoClientCancel2 {    
  
/**
 * Request processing of cancel request
 * @param args java.lang.String[]
 */ 
public static void main(String args[]) {   
  
  // new object
  TyDemoClientCancel2 can = new TyDemoClientCancel2();
  
  // do the work
  can.doWork();        
     
  // end the JVM since an RMI Server persists
  System.exit(0);
   
} // end-method

private void doWork() {

    // server
    TySvrComm svr = new TySvrComm();
    
    // new thread
    TyDemoClientCancelThread2Thread myThread =  
      new TyDemoClientCancelThread2Thread(svr);
    
    // get it moving
    myThread.start();    
    
    // new impl object that contains the callback method
    TyDemoClientCancel2Impl impl = new TyDemoClientCancel2Impl(myThread);

    // callback remote object
    InterruptionReady cb = null;    

    // may fail
    try {
      // make me an RMI Server
      cb = (InterruptionReady)UnicastRemoteObject.exportObject(impl);

    } // end-try
    catch (java.rmi.RemoteException e) {

      // say error
      System.out.println("caught RemoteException from exportObject");
      e.printStackTrace();

      // done
      return;

    } // end-catch
  
    // new string for Tymeac function
    String x = "112233445566778899";

    // make obj
    Object pass = x;

    // return array
    Object back[] = null;

       
    // form a parameter for Tymeac   
    TymeacParm TP = new TymeacParm(pass,        // data                                
                                  "Function_4", // function name
                                  10,           // wait time
                                  1,            // priority
                                  cb);          // callback
    
    // do this ten times
    for (int i = 0; i < 10; i++) {
                         
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
        
    } // end-for  
  
} // end-method  

} // end-class
