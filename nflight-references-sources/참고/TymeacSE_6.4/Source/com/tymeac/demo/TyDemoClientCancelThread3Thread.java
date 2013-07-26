package com.tymeac.demo;
/*
 * Created on Feb 15, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
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
 * This thread waits for a half second. It
 *   then calls the Tymeac Server with a cancel request. 
 */
public class TyDemoClientCancelThread3Thread extends Thread {

        // from server
        private CancelParm passed;
        
        // for calling Tymeac
        private TySvrComm svr;

/**
 * Constructor
 */
public TyDemoClientCancelThread3Thread(TySvrComm p_svr,
                                       CancelParm p_passed)  {

  svr = p_svr;
  passed = p_passed;

} // end-constructor

/**
 * Wait and call the server with a cancel request
 */
public void run() {  
  
  // this should be enough to make sure the original request schedules completely       
  try {
    // wait
    sleep(250);
    
  } catch (InterruptedException e) {}   
  
      
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
  
    case CancelParm.INVALID_CANCEL_WORD: 
            System.out.println("Cancel failed with invalid cancel word");
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
  
} // end-method-run
} // end-class
