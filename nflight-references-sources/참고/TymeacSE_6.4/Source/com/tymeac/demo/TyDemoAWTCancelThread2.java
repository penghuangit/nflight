package com.tymeac.demo;

/*
 * Copyright (c) 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import com.tymeac.base.*;

/**
 * This thread waits for a wake up from the called back Tymeac Client. It
 *   then calls the Tymeac Server with a cancel request. 
 */
public class TyDemoAWTCancelThread2 extends Thread {

  // from client
  private CancelParm passed;
  
  // for calling Tymeac
  private TySvrComm svr;
  
  // wake up switch
  private volatile boolean posted = false;  

/**
 * Constructor
 */
public TyDemoAWTCancelThread2(TySvrComm p_svr)  {
  
  super("TyDemoAWTCancelThread2");

  svr = p_svr;

} // end-constructor

/**
 * Save the parm and notify() the waiting thread
 * @param forCancel
 */
public synchronized void wakeUp(CancelParm forCancel) {

  // save object
  passed = forCancel;
  
  // from a notify
  posted = true;
  
  // wake up
  notify();

}  // end-method

/**
 * Wait for a wake up and call the server with a cancel request
 */
public void run() {
  
  // stay here until JVM exits
  while (true) {
    
    // my monitor
    synchronized (this) {
      
      // until a wake up
      while (!posted) {
      
        try {
          // waiting for a notify
          wait();
          
        } catch (InterruptedException e) {}  
                
      } // end-while   
  
      // not posted for next time
      posted = false;
      
    } // end-sync  
      
    // work
    int back = 0; 

    // call Server with new request
    back = svr.cancelSyncReq(passed);

    // what came back
    switch (back) {

      case CancelParm.SUCCESSFUL: 
                System.out.println("Cancel completed in Server");
                break;

      case CancelParm.INVALID_CANCEL_WORD: 
              System.out.println("Cancel failed with invalid cancel word");
              break;
              
      case CancelParm.INVALID_REQUEST_ID: 
              System.out.println("Cancel failed with invalid request id");
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
  } // end-while  
} // end-method 

} // end-class
