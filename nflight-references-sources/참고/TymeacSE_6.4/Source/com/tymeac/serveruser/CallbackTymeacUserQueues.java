package com.tymeac.serveruser;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.util.*;
import com.tymeac.base.*;
/**
 * This is the Class for User Queues when not using a DBMS.
 *   See also TymeacUserFunctions and TymeacUserVariables  
 * 
 */

public class CallbackTymeacUserQueues {

  // total number of Queues
  private int total_queues;

  // Array of all the Queues
  private TymeacIndividualQueue[] queues;

/**
 * Constructor for the Tymeac User Queues  
 *
 *  First build a Vector of the TymeacIndividualQueue objects
 *
 *  Then load the queues array from the temporary Vector   
 */

public CallbackTymeacUserQueues() {

  
  // vector for temp phase
  Vector<TymeacIndividualQueue> temp = new Vector<TymeacIndividualQueue>();


// *--- define all the queues here ---*

// *--- Demo Queue AAAA ---*
  temp.addElement( new TymeacIndividualQueue("AAAA",  // name of queue
                        "com.tymeac.serveruser.Demo1",  // processing application class   
                        "no",     // not an output agent queue    
                        "no",     // do not initially start all threads
                        0,        // use default PA Class timeout value                                               
                        3,        // number of threads
                        10,       // number of waitlists
                        30,       // number of waitlist entries
                        30,       // number of waitlist entries for thresholds
                        10,       // timeout seconds
                        20,       // time to kill thread in seconds
                        0.00F,    // overall % *-- not used --*
                        0.60F,    // individual %
                        0.00F,    // weighted Factor *-- not used --*
                        0.00F));  // weighted Average *-- not used --*


// *--- Demo Queue BBBB ---*
  temp.addElement( new TymeacIndividualQueue("BBBB",  // name of queue
                        "com.tymeac.serveruser.Demo2",  // processing application class   
                        "no",     // not an output agent queue    
                        "no",     // do not initially start all threads 
                        0,        // use default PA Class timeout value
                        3,        // number of threads
                        10,       // number of waitlists
                        30,       // number of waitlist entries
                        30,       // number of waitlist entries for thresholds
                        10,       // timeout seconds
                        20,       // time to kill thread in seconds
                        0.00F,    // overall % *-- not used --*
                        0.60F,    // individual %
                        0.00F,    // weighted Factor *-- not used --*
                        0.00F));  // weighted Average *-- not used --*

// *--- Demo Queue CCCC ---*
  temp.addElement( new TymeacIndividualQueue("CCCC",  // name of queue
                        "com.tymeac.serveruser.Demo3",  // processing application class   
                        "no",     // not an output agent queue    
                        "no",     // do not initially start all threads 
                        0,        // use default PA Class timeout value
                        3,        // number of threads
                        10,       // number of waitlists
                        30,       // number of waitlist entries
                        30,       // number of waitlist entries for thresholds                        
                        10,       // timeout seconds
                        20,       // time to kill thread in seconds
                        0.00F,    // overall % *-- not used --*
                        0.60F,    // individual %
                        0.00F,    // weighted Factor *-- not used --*
                        0.00F));  // weighted Average *-- not used --*


// *--- Demo Queue DDDD ---*
  temp.addElement( new TymeacIndividualQueue("DDDD",  // name of queue
                        "com.tymeac.serveruser.DemoAgent1", // processing application class   
                        "yes",    // output agent queue   
                        "no",     // do not initially start all threads
                        0,        // use default PA Class timeout value 
                        3,        // number of threads
                        10,       // number of waitlists
                        30,       // number of waitlist entries
                        30,       // number of waitlist entries for thresholds
                        10,       // timeout seconds
                        20,       // time to kill thread in seconds
                        0.00F,    // overall % *-- not used --*
                        0.60F,    // individual %
                        0.00F,    // weighted Factor *-- not used --*
                        0.00F));  // weighted Average *-- not used --*


// *--- Demo Queue EEEE ---*
  temp.addElement( new TymeacIndividualQueue("EEEE",  // name of queue
                        "com.tymeac.serveruser.DemoRecur", // processing application class    
                        "no",     // not an output agent queue    
                        "no",     // do not initially start all threads 
                        0,        // use default PA Class timeout value
                        3,        // number of threads
                        10,       // number of waitlists
                        30,       // number of waitlist entries
                        30,       // number of waitlist entries for thresholds
                        10,       // timeout seconds
                        20,       // time to kill thread in seconds
                        0.00F,    // overall % *-- not used --*
                        0.60F,    // individual %
                        0.00F,    // weighted Factor *-- not used --*
                        0.00F));  // weighted Average *-- not used --*

// *--- Demo Queue Notify ---*
  temp.addElement( new TymeacIndividualQueue("Notify",  // name of queue
                        "com.tymeac.serveruser.DemoNotify", // processing application class   
                        "no",     // not an output agent queue    
                        "no",     // do not initially start all threads
                        0,        // use default PA Class timeout value 
                        1,        // number of threads
                        3,        // number of waitlists
                        30,       // number of waitlist entries
                        30,       // number of waitlist entries for thresholds
                        10,       // timeout seconds
                        20,       // time to kill thread in seconds
                        0.00F,    // overall % *-- not used --*
                        0.00F,    // individual % *-- not used --* 
                        0.00F,    // weighted Factor *-- not used --*
                        0.00F));  // weighted Average *-- not used --*

// *--- Demo Queue Jini 1 ---*
  temp.addElement( new TymeacIndividualQueue("JQue1",  // name of queue
            "com.tymeac.serveruser.JDemo1", // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads 
            0,        // use default PA Class timeout value
            3,        // number of threads
            10,        // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,        // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %  
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*

 // *--- Demo Queue Jini 2 ---*
  temp.addElement( new TymeacIndividualQueue("JQue2",  // name of queue
            "com.tymeac.serveruser.JDemo2", // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads
            0,        // use default PA Class timeout value 
            3,        // number of threads
            10,        // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,        // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %  
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*

 // *--- Demo Queue Jini 3 ---*
  temp.addElement( new TymeacIndividualQueue("JQue3",  // name of queue
            "com.tymeac.serveruser.JDemo3", // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads 
            0,        // use default PA Class timeout value
            3,        // number of threads
            10,        // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,        // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %  
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*

// *--- Demo Queue Jini 4 ---*
  temp.addElement( new TymeacIndividualQueue("JQue4",  // name of queue
            "com.tymeac.serveruser.JDemo4", // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads
            0,        // use default PA Class timeout value 
            3,        // number of threads
            10,        // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,        // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %  
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*

 // *--- Demo Queue Jini Output Agent ---*
  temp.addElement( new TymeacIndividualQueue("JQueAgent",  // name of queue
            "com.tymeac.serveruser.JDemoAgent", // processing application class   
            "yes",     // not an output agent queue   
            "no",     // do not initially start all threads 
            0,        // use default PA Class timeout value
            1,        // number of threads
            3,        // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,        // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %  
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*



// test start up function

// *--- start up ---*
  temp.addElement( new TymeacIndividualQueue("StartUp",  // name of queue
      "com.tymeac.serveruser.DemoStart", // processing application class   
      "no",     // not an output agent queue    
      "no",     // do not initially start all threads
      0,        // use default PA Class timeout value 
      1,        // number of threads
      1,        // number of waitlists
      3,       // number of waitlist entries
      3,       // number of waitlist entries for thresholds
      1,        // timeout seconds
      2,        // time to kill thread in seconds
      0.00F,    // overall % *-- not used --*
      0.00F,    // individual %  
      0.00F,    // weighted Factor *-- not used --*
      0.00F));  // weighted Average *-- not used --*




// *-------------- Callback Queues Start --------------------------*


// *--- Demo Queue CALA ---*
  temp.addElement( new TymeacIndividualQueue("CALA",  // name of queue
            "com.tymeac.serveruser.DemoCallback1",  // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads
            0,        // use default PA Class timeout value                                                 
            3,        // number of threads
            10,       // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,       // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*


// *--- Demo Queue CALB ---*
  temp.addElement( new TymeacIndividualQueue("CALB",  // name of queue
            "com.tymeac.serveruser.DemoCallback2",  // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads 
            0,        // use default PA Class timeout value
            3,        // number of threads
            10,       // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,       // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*

// *--- Demo Queue CALC ---*
  temp.addElement( new TymeacIndividualQueue("CALC",  // name of queue
            "com.tymeac.serveruser.DemoCallback3",  // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads
            0,        // use default PA Class timeout value 
            3,        // number of threads
            10,       // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,       // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*


// *--- Demo Queue CALD ---*
  temp.addElement( new TymeacIndividualQueue("CALD",  // name of queue
            "com.tymeac.serveruser.DemoAgentCallback", // processing application class    
            "yes",    // output agent queue   
            "no",     // do not initially start all threads 
            0,        // use default PA Class timeout value
            3,        // number of threads
            10,       // number of waitlists
            30,       // number of waitlist entries
            30,       // number of waitlist entries for thresholds
            10,       // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.60F,    // individual %
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --* 


// *------------------------- Callback Queues End -------------------------------*


// *--- end of phase one ---*


  // total Queues 
  total_queues = temp.size();   

  // create a new array 
  queues = new TymeacIndividualQueue[total_queues];

  // fill up the array by taking objects out of the temporary Vector
  for  (int i = 0; i < total_queues; i++) {

      // add this queue
      queues[i] = temp.elementAt(i);

  } // end-for

} // end-constructor
/**
 * This accessor method returns the total number of queues.
 * @return int total_queues is the total number of queues.
 */

public int getNbrQueues() {

  // return the instance field
  return total_queues;

} // end-method
/**
 * This accessor method returns the list of Queues     
 *
 * @return TymeacIndividualQueue[]
 */

public TymeacIndividualQueue[] getQueues() {

  // return the private instance field
  return queues;

} // end-method
} // end-class
