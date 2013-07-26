package com.tymeac.base;

/*
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
 
 import com.tymeac.serveruser.*;
 import java.util.*;

/**
 * This class holds the internal queues used by the Tymeac Server
 *   just like the com.tymeac.serveruser.TymeacUserQueues holds
 *   queues for users.
 * 
 * This class is loaded by the start up for the Server after the normal queues. That
 *   may be either user classes or DBMS tables. 
 * 
 */
public class TymeacInternalQueues
            extends TymeacUserQueues {       

public TymeacInternalQueues() {
  
  // vector for temp phase
  ArrayList<TymeacIndividualQueue> temp = new ArrayList<TymeacIndividualQueue>();

// *--- define all the queues here ---*

  // cancel sync request queue
  temp.add(
        new TymeacIndividualQueue( 
            "TymeacInternalCancel",  // name of queue
            "com.tymeac.base.TymeacInternalCancel", // processing application class   
            "no",     // not an output agent queue    
            "no",     // do not initially start all threads                                                 
            0,        // use default PA Class timeout value
            3,        // number of threads
            3,        // number of waitlists
            100,      // number of waitlist entries
            100,      // thresholds wl entries
            10,       // timeout seconds
            20,       // time to kill thread in seconds
            0.00F,    // overall % *-- not used --*
            0.10F,    // individual % is 10%
            0.00F,    // weighted Factor *-- not used --*
            0.00F));  // weighted Average *-- not used --*

// *--- define all the queues above ---*         
            
  // total Queues 
  total_queues = temp.size();   

  // create a new array in super
  queues = new TymeacIndividualQueue[total_queues];

  // fill up the array by taking objects out of the temporary list
  for  (int i = 0; i < total_queues; i++) {

    // add this queue
    queues[i] = temp.get(i);

  } // end-for

} // end-constructor

} // end-class
