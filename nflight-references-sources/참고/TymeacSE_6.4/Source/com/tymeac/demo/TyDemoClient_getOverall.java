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

import com.tymeac.base.*;
import com.tymeac.client.*;
/**
 * Tymeac demonstration system.  Display, (println), the result of
 *    Tymeac Overall display
 * 
 */
 
public class TyDemoClient_getOverall {
  
/**
 * Request an overall status from the Tymeac Server. 
 * @param args java.lang.String[]
 */
 
public static void main(String[] args) {
   
   
    
    // This is the obj returned, see the doc on the TyOverStatus Class
    //  three integers = nbr_sync requests active,
    //                   nbr_async requests active,
    //                   nbr_stalled number of async requests stalled
    //  String[] = Queues with attendant threads
    TyOverallObj TyO = null;
    
    // go get the top display or both (the top three int and Queues )
    //TyO = new TyOverallClient().refreshTop();
    TyO = new TyOverallClient().refreshBoth();
    
    // When null return, communication error, (usually shut down)
    if  (TyO == null) {

        // say something went wrong
        System.out.println("Nothing returned");

        // all done
        return;
    } // endif

    // *--- show what came back ---*  

    // number of sync requests
    System.out.println(TyO.getSync() + " Sync requests active");

    // number of async requests
    System.out.println(TyO.getAsync() + " Async requests active");

    // number of stalled requests
    System.out.println(TyO.getStalled() + " Async requests stalled");
    
    // When no returned strings, all done
    if  (TyO.getQueues() == null) {

        // bye
        return;
    } // endif

    // the returned string array
    String[] att_queues = TyO.getQueues();

    // working
    int i, nbr_queues = att_queues.length;

    // print all the queues with attendant threads
    for  (i = 0; i < nbr_queues; i++) {

          // Queue 
          System.out.println(att_queues[i]);
    } // end-for
   

    // all done 
    return;
   
} // end-method
} // end-class
