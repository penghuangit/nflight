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
import com.tymeac.client.*;
/**
 * Tymeac demonstration system. Internal client for canceling syncReq()
 *
 *  In order for a cancel to work, the function on the server must be delayed.
 *    Otherwise, the syncReq() would return before the cancel logic was reached.
 *    When using a Tymeac Function_4, three queues are in use. These queues use
 *    applications com.tymeac.serveruser.Demo1, ...Demo2 and ...Demo3. Put a delay
 *    in Demo3.main() such as Thread.sleep(5000). This gives the cancel logic enough
 *    time to work.
 */
 
public class TyDemoInternalClientCancelled
          implements InterruptionReady {

  // for calling Tymeac
  private TymeacInterface ti;
  
    
    /**
     * New thread for invoking the remote cancelSyncReq() method
     * on the Tymeac Server.
     */
    private class InvokeRunnable implements Runnable {

      private CancelParm passed;
              
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
        try { 
           back = ti.cancelSyncReq(passed);
        }
        catch (Exception e) {

            System.out.println("cancelSyncReq Exception= " + e.getMessage());

            back = 7;

        } // end-catch


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
            
          default: System.out.println("Cancel failed with invalid return code=" + back);
                  break;

        } // end-switch
    } // end method  
  } // end-inner class

/**
 * Constructor 
 */
public TyDemoInternalClientCancelled() {

} // end-constructor

/**
 * The sequence is the following. -breakiterator
 * 1. start the server
 * 2. send a sync request for Function_4 with a callback in the parm
 *    2.1 the server calls back to method ready()
 *    2.2 ready() starts a new thread to do the cancel
 *    2.3 the new thread calls the server with a cancelSyncReq()
 *
 * 3. wait 10 seconds
 * 4. shut down the server
 * 5. wait 10 seconds
 * 6. shut down again    
 *
 */
private void init() {

  // 1.
    // new server instance
    InternalServer impl = new InternalServer();  

    // passed arg is "only stand alone"      
    String[] in = {"-s"};

    // create the server
    ti = impl.createServer(in);


// 2.
    // new string for Tymeac function
    String x = "112233445566778899";

    // make obj
    Object pass = x;

    // return array
    Object back[] = null;
   
    // form a parameter for Tymeac   
    TymeacParm TP = new TymeacParm(pass,        // data 
                                  "Function_4", // function name
                                  10,          // wait time
                                  1,            // priority
                                  this);    // callback
           
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

//------------------------------------------------------------------------------

    
    
    // This is the obj returned, see the doc on the TyOverStatus Class
    //  three integers = nbr_sync requests active,
    //                   nbr_async requests active,
    //                   nbr_stalled number of async requests stalled
    //  String[] = Queues with attendant threads
    TyOverallObj TyO = null;
    
    // go get the top or both (three ints and queues)     
    TyO = new TyOverallClient(ti).refreshBoth();
//    TyO = new TyOverallClient(ti).refreshTop();

    // When null return, communication error, (usually shut down)
    if  (TyO == null) {

        // say something went wrong
        System.out.println("Nothing returned");
        
    } 
    else {
    
        // *--- show what came back ---*  

        // number of sync requests
        System.out.println(TyO.getSync() + " Sync requests active");

        // number of async requests
        System.out.println(TyO.getAsync() + " Async requests active");

        // number of stalled requests
        System.out.println(TyO.getStalled() + " Async requests stalled");
            
         // When no returned strings, all done
        if  (TyO.getQueues() != null) {
   

            // the returned string array
            String[] att_queues = TyO.getQueues();

            // working
            int i, nbr_queues = att_queues.length;

            // print all the queues with attendant threads
            for  (i = 0; i < nbr_queues; i++) {

                  // Queue 
                  System.out.println(att_queues[i]);
            } // end-for
        } // endif
    } // Endif

// ------------------------------
      
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

    // done     
     return;


} // end-method

/**
 * Create a new instance and call init()
 *
 * @param args java.lang.String[]
 */ 
public static void main(String args[]) {

  new TyDemoInternalClientCancelled().init();
   
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
