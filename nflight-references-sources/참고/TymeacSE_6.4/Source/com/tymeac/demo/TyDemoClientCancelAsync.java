package com.tymeac.demo;

/* 
 * Copyright (c) 2006 Cooperative Software Systems, Inc. 
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
 * Tymeac demonstration system. This is an example of how to cancel
 *    an asyncReq() Tymeac Function.
 *
 *  In order for a cancel to work, the function on the server must be delayed.
 *    Otherwise, the syncReq() would return before the cancel logic was reached.
 *    When using a Tymeac Function_4, three queues are in use. These queues use
 *    applications com.tymeac.serveruser.Demo1, ...Demo2 and ...Demo3. Put a delay
 *    in Demo3.main() such as Thread.sleep(5000). This gives the cancel logic enough
 *    time to work.
 *    
 *  {"case__1__Msg_text", " Connection error"},
    {"case_1__Msg_text", " Tymeac MilliTime is not a valid number"},
    {"case_2__Msg_text", " Request Id is not a valid number"},
    {"case_3__Msg_text", " Request id is invalid this session"},
    {"case_4__Msg_text", " Tymeac shutting down"},
    {"case_5__Msg_text", " Tymeac Millitime mismatched with current session"},
    {"case_6__Msg_text", " Request is Stalled"},
    {"case_7__Msg_text", " Request is not in the system"},
    {"case_8__Msg_text", " Request is awaiting execution"},
    {"case_9__Msg_text", " Request Executing at Output Agent Stage"},
    {"case_10__Msg_text", " Request Executing"},
    {"case_11__Msg_text", "Request cancelled. No processing took place."},
    {"case_12__Msg_text", "Request cancelled. Was in progress."},
 *    
 *    
 *    
 *    
 */
 
public class TyDemoClientCancelAsync {

  // for calling Tymeac sync/async methods
  private static TySvrComm svr;
  
  private static TyIntnlSvr intrnl;
  
  private static Long session;
  private static Long reqid;
  
  private static Object obj = new Object();
  
  private static boolean posted = false;
  
  private static int ret_reason;  
    
    /**
     * New thread for invoking the remote cancelSyncReq() method
     * on the Tymeac Server.
     */
    private static class InvokeRunnable implements Runnable {
      
      private volatile boolean my_posted = false;
              
      /**
        * Constructor
       */

      public InvokeRunnable() {

      }   // end-constructor

      // cancel the prior asyncReq()
      public void run() {

        System.out.println("Cancel Thread waiting");
        
        while (true) { 
        
          //      wait for return
          // sync on this
          synchronized (this) {
                                  
              // wait for a posting
              while (!my_posted) {
                
                  try {
                      // wait for a post
                      wait();
                      
                  } // end-try
  
                  catch (InterruptedException e) {
  
                  } // end-catch
              } // end-while          
  
              // reset the posted indicator
              my_posted = false;
          
          } // end-sync      
  
          // call Server with cancel request
          ret_reason = intrnl.idStatus1Request(session, reqid, 2);
                    
          synchronized (obj) {
            
            posted = true;
            
            obj.notify();
            
          } // end-sync
        } // end-while  
    } // end method      
      
    private synchronized void wakeUp () {
      
      my_posted = true;
      
      // hello
      notify();      
      
    } // end-method
  } // end-inner class
    

/**
 * Request processing of cancel request
 * @param args java.lang.String[]
 */ 
@SuppressWarnings("null")
public static void main(String args[]) {

  int nbr_times = 1000, i, j;
  
  // When no number
  if  ((args == null) ||
       (args.length < 1)) {
      
      System.out.println("Enter number of times to loop and try again");
      System.exit(1); 
      
  } // endif   
      
  // try to convert 
  try {
    nbr_times = Integer.parseInt(args[0]);
    
  } catch (NumberFormatException e) {
  
      System.out.println("Enter a numeric number and try again");
      System.exit(1); 
      
  } // end-catch       

  // new string for Tymeac function
  String x = "999999999999999999"; 
  
  // make obj
  Object pass = x;

  // return array
  Object back[] = null;    
     
  // form a parameter for Tymeac   
  TymeacParm TP = new TymeacParm(pass,        // data                                 
                                "Function_7", // function name
                                0,           // wait time
                                1);    // priority
                 
  // find the server remote object from the registry
  svr = new TySvrComm();
  
  // find the server remote object from the registry
  intrnl = new TyIntnlSvr();
  
  InvokeRunnable ir = new InvokeRunnable();
  
  // do the cancel in a new thread
  Thread th = new Thread(ir);
  
  // start the thread (it waits until notified)
  th.start();
  
  int minus  = 0;
  int zero   = 0;
  int one    = 0;
  int two    = 0;
  int three  = 0;
  int four   = 0;
  int five   = 0;
  int six    = 0;
  int seven  = 0;
  int eight  = 0;
  int nine   = 0;
  int ten    = 0;
  int eleven = 0;
  int twelve = 0;
  
  // reformat the return array
  TymeacReturn ret = null;

  
  // do this many times
  for (; nbr_times > 0; nbr_times--) {
    
    // do an async request   
    back = svr.asyncRequest(TP); 
    
    // reformat the return array
    ret = TymeacReturn.formatCallReturn(back);
    
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
    
    session = ret.getSessionId();
    reqid  =  ret.getRequestId();
    
    long my_s = session;
    long my_r = reqid;
    
    // wake up thread
    ir.wakeUp();
    
    //  wait for return
    // sync on this
    synchronized (obj) {
                            
        // wait for a posting
        while (!posted) {
          
            try {
                // wait for a post
                obj.wait();
                
            } // end-try

            catch (InterruptedException e) {

            } // end-catch
        } // end-while          

        // reset the posted indicator
        posted = false;
    
    } // end-sync
    
    // what came back
    switch (ret_reason) {
    
      case -1:  minus++; break;
      case  0:  zero++;  break;
      case  1:  one++;   break; 
      case  2:  two++;   break;
      case  3:  three++; break; 
      case  4:  four++;  break;
      case  5:  five++;  break; 
      case  6:  six++;   break;
      case  7:  seven++; break; 
      case  8:  eight++; break;
      case  9:  nine++;  break; 
      case  10:  ten++;   break;
      case  11:  eleven++; break; 
      case  12:  twelve++; break;
      
      default: System.out.println("Invalid switch/case=" + ret_reason );
                System.exit(2);      
    
    } // end-switch
  } // end-for
    
  System.out.println(minus + " Connection error");
  System.out.println(zero + " Successful");
  System.out.println(one + " Tymeac MilliTime is not a valid number");
  System.out.println(two + " Request Id is not a valid number");
  System.out.println(three + " Request id is invalid this session");
  System.out.println(four + " Tymeac shutting down");
  System.out.println(five + " Tymeac Millitime mismatched with current session");
  System.out.println(six + " Request is Stalled");
  System.out.println(seven + " Request is not in the system");
  System.out.println(eight + " Request is awaiting execution");
  System.out.println(nine + " Request Executing at Output Agent Stage");
  System.out.println(ten + " Request Executing");
  System.out.println(eleven + " Request cancelled. No processing took place");
  System.out.println(twelve + " Request cancelled. Was in progress");
     
  // end the JVM since 
  System.exit(0);
   
} // end-method
} // end-class
