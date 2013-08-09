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
 *    a syncReq() Tymeac Function.
 *
 *   - export myself as a remote object
 *   - call the Tymeac Server with a syncReq()
 *   - the Tymeac Server calls back to method ready()
 *   - ready() starts a new thread passing the parameters from the Tymeac Server
 *   - new thread calls the Tymeac Server cancelSyncReq() method
 *
 *  In order for a cancel to work, the function on the server must be delayed.
 *    Otherwise, the syncReq() would return before the cancel logic was reached.
 *    When using a Tymeac Function_4, three queues are in use. These queues use
 *    applications com.tymeac.serveruser.Demo1, ...Demo2 and ...Demo3. Put a delay
 *    in Demo3.main() such as Thread.sleep(5000). This gives the cancel logic enough
 *    time to work.
 */
 
public class TyDemoClientCancel
              implements InterruptionReady {

  // for calling Tymeac
  private static TySvrComm svr;
  
  private static CancelParm passed;
    
    /**
     * New thread for invoking the remote cancelSyncReq() method
     * on the Tymeac Server.
     */
    private static class InvokeRunnable implements Runnable {
              
      /**
        * Constructor
       */

      public InvokeRunnable(CancelParm forCancel) {

        // save object
        passed = forCancel;

      }   // end-constructor

      // cancel the prior syncReq()
      public void run() {

        // work
        int back = 0;      

        System.out.println("invoking cancelSyncReq() method");

        // call Server with new request
        back = svr.cancelSyncReq(passed);

        // what came back
        switch (back) {

          case CancelParm.SUCCESSFUL: 
                  System.out.println("Cancel completed in Server");
                  break;

          case CancelParm.INVALID_SESSION_ID: 
                  System.out.println("Cancel failed with invalid session");
                  break;

          case CancelParm.INVALID_REQUEST_ID: 
                  System.out.println("Cancel failed with invalid request-id");
                  break;

          case CancelParm.CONNECTION_FAILURE: 
                  System.out.println("Cancel failed with a connection failured");
                  break;

          case CancelParm.REMOTE_EXCEPTION: 
                  System.out.println("Cancel failed with a Remote Exception");
                  break;

          case CancelParm.EXCEPTION: 
                  System.out.println("Cancel failed with an Exception");
                  break;
            
          default:
                  System.out.println("Cancel failed with invalid return code=" + back);
                  break;

        } // end-switch
    } // end method  
  } // end-inner class
/**
 * Request processing of cancel request
 * @param args java.lang.String[]
 */
 
@SuppressWarnings("null")
public static void main(String args[]) {

    int port = 1000;
    
    // When no port number
    if  ((args == null) ||
         (args.length < 1)) {
        
        System.out.println("Enter a port number and try again");
        System.exit(1);      
    }   
        
    // try to convert 
    try {
      port = Integer.parseInt(args[0]);
    } 
    catch (NumberFormatException e) {
      
      System.out.println("Enter a numeric port number and try again");
      System.exit(1);      
    }   
    
    // new object 
    TyDemoClientCancel impl = new TyDemoClientCancel();

    // callback remote object
    InterruptionReady cb = null;    

    // may fail
    try {
      // make me an RMI Server on passed port 
      cb = (InterruptionReady)UnicastRemoteObject.exportObject(impl, port);

    } // end-try
    catch (java.rmi.RemoteException e) {

      // say error
      System.out.println("caught RemoteException from exportObject= " + e.toString());
      e.printStackTrace();

      // done
      return;

    } // end-catch
  
    // new string for Tymeac function
    String x = "999999999999999999"; 
    // make obj
    Object pass = x;

    // return array
    Object back[] = null;
    
       
    // form a parameter for Tymeac   
    TymeacParm TP = new TymeacParm(pass,        // data                                 
                                  "Function_4", // function name
                                  10,           // wait time
                                  1,            // priority
                                  cb);    // callback
                   
    // find the server remote object from the registry
    svr = new TySvrComm();
    
    System.out.println("Calling Server with a syncRequest() with callback on port=" + port);
                         
    // do a sync request   
    back = svr.syncRequest(TP);
    
    // may fail
    try {
      // unexport the prior 
      UnicastRemoteObject.unexportObject(impl, true);

    } // end-try
    catch (java.rmi.RemoteException e) {}

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
     
  // end the JVM since an RMI Server persists
  System.exit(0);
   
} // end-method
/**
 * 
 */
public void ready(CancelParm forCancel) {
  
  System.out.println("ready() starting new thread");

  // do the cancel in a new thread
  new Thread(new InvokeRunnable(forCancel)).start();

} // end-method
} // end-class
