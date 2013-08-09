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
package com.tymeac.demo;

import java.rmi.RemoteException;

import com.tymeac.base.CancelParm;
import com.tymeac.base.InterruptionReady;

/**
 * Implementation of read() method for server callback.
 *   The constructor saves the reference to the worker thread.
 *   The callback wakes up the thread passing the CancelParm object.
 * 
 */
public class TyDemoClientCancel2Impl implements InterruptionReady {

      // thread that calls back to server      
      private volatile TyDemoClientCancelThread2Thread myThread;

/**
 * Constructor
 * @param myThread
 */      
public TyDemoClientCancel2Impl(TyDemoClientCancelThread2Thread myThread) {
  
  // save parm   
  this.myThread = myThread;  
  
} // end-constructor

/**
 * Callback method from server
 */
public void ready(CancelParm forCancel) throws RemoteException {

  System.out.println("ready() waking up thread");

  try {     
    // wake up thread and pass it the parm from server
    myThread.wakeUp(forCancel);
    
  } catch (Exception e) {
  
      System.out.println("ready()= " + e.toString());
  }        
    
} // end-method

}// end-class
