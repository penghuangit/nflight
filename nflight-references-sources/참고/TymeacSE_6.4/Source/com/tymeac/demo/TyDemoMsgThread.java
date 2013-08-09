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

/** 
 *
 *  Tymeac demonstration system startup exit message writter thread
 * 
 */
 
public final class TyDemoMsgThread 
          extends Thread {

  // base storage
  @SuppressWarnings("unused")
  private TyDemoMsgBase base = null;
  
  // timeout interval
  private long wait_time = 0; 
    
  // passed msg
  private String msg = null;

  // posted indicator
  private volatile boolean posted = false;
  

/**
 * Constructor, called at startup
 * @param timeout long - max time
 */

public TyDemoMsgThread(long timeout) {

    // name of this thread
    super("TyDemoMsgThread");

    // base storage initialized
    // this reference assures the storage is never garbage collected
    base = new TyDemoMsgBase(this);

    // timeout interval
    wait_time = timeout;

} // end-constructor
/**
 * write the message
 */

private void doWork() {

    // print
    System.out.println(msg);    

} // end-method
/**
 * implemented run method
 *
 * Wait until a client wakes up the thread then do the work or a timeout and end the thread.
 *   
 */

public void run() {

    // time out switch
    boolean timed_out = false;

    // until timed out
    while (!timed_out) {
        
        // wait for activation or timeout
        //   sync on myself
        synchronized (this) {
                      
            // until posted or time out
            while (!posted) {
                    
                try {
                    // wait for a posting or time out
                    wait(wait_time);

                    // When timed out   
                    if  (!posted) {
                
                        // set temp status as inactive        
                        timed_out = true;

                        // set is posted
                        posted = true;  
                              
                    } // endif
                } // end-try

                catch (InterruptedException e) {

                } // end-catch
            } // end-while

            // reset the posted indicator
            posted = false;
        
        } // end-sync

        // When NOT timed out
        if  (!timed_out) {

            // do the work
            doWork();

        } // endif
    } // end-while

    // finished
  
} // end-method
/**
 * Wake up the posted thread and shut it down
 * @param message String - the message
 */

public synchronized void shutDown(String message) {

    // msg
    msg = message;

    // new wait time of 1 millisecond
    wait_time = 1L;

    // set the posted indicator
    posted = true;

    // wake up
    notify();

} // end-method
/**
 * Wake up the posted thread
 * @param message String - the message
 */

public synchronized void wakeUp(String message) {

    // msg
    msg = message;

    // set the posted indicator
    posted = true;

    // wake up
    notify();

} // end-method
} // end-class
