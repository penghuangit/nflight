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
import java.awt.*;

/**
 * This thread waits for a wake up from the called back Tymeac Client. It
 *   then calls the Tymeac Server with a cancel request. 
 */
public class TyDemoAWTCancelThread1 extends Thread {

  // from client
  private long cancel_word;
  private TextField result;
  
  // for calling Tymeac
  private TySvrComm svr;
  
  // wake up switch
  private volatile boolean posted = false; 
  
  // calling Tymeac
  private volatile boolean working = false;

/**
 * Constructor
 */
public TyDemoAWTCancelThread1(TySvrComm p_svr)  {
  
  super("TyDemoAWTCancelThread1");

  svr = p_svr; // comm module

} // end-constructor

private void doWork() {
  
  // the parmeter passed to Tymeac
  TymeacParm TP = new TymeacParm(null, // nothing going over
                                 "CancelDemo", // function
                                 40,   // wait no more than 40 seconds
                                 1,    // priority 1  
                                 cancel_word); // cancel word
   
  // working on request
  working = true;
  
 // do a sync request       
 Object[] R = svr.syncRequest(TP);
   
  // Not working on request
  working = false; 
   
 // When nothing came back say so
  if  (R == null)  {
    
      // say bad return   
      result.setText("Communication Error");

      // all done
      return;

  } // endif
  
  // find out what came back

  // number of objects
  int nbr = R.length;

  // get new string for concatenating
  String stuff = "";

  // concatenate the Strings
  for  (int i = 0; i < nbr; i++) {

    // When good string
    if  (R[i] != null) {

        // each string
        stuff = stuff.concat((String) R[i]);

    } // endif
  } // end-for    

  // say what came back
  result.setText(stuff);

} // end-method

/**
 * Save the parm and notify() the waiting thread
 * @param the cancel word
 */
public synchronized void wakeUp(long cancel_word, TextField result) {

  // When working already, get out
  if  (working) return;
  
  // save object
  this.cancel_word = cancel_word;
  this.result      = result;
  
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
    
    doWork();
    
  } // end-while  
} // end-method 

} // end-class
