package com.tymeac.demo.jframe;

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

/**
 * Tymeac Demonstration System.  Multiple test, base storage for all
 *        threads.
 *    This is used by the two classes:
 *      T3bean   class to display the results on the T3 window class.
 *      T3Thread thread class to put the data for the above.  
 *
 */

public final class TyDemoInternalTest3T3Base {


	// *-------- instance fields --------*
	
	// the list of data for all threads.  
	//  1st array is number of threads, (placeholder for the second array.)
	//  2nd array is three integers:
	//      0 - the status, 0 = continuing
	//                       1 = done
	//      1 - return code
	//      2 - number of times used
	
	private int[][]   tmt_list;
		
	// number of threads 
	private int nbr_in_list = 0;
	
	// shut down mode, 0=no   
	private int shutdown    = 0;
	
	// server
	private TymeacInterface ti;

/**
 * Tymeac Demonstration System, multi test constructor:
 *    Initialize the base integer[][] array.
 *    Start a temporary thread that starats each T3Thread thread.
 *
 * @param nbr int number of threads to start
 */
 
public TyDemoInternalTest3T3Base (int nbr, TymeacInterface t) {
    
    // total number of threads
    nbr_in_list = nbr;
    
    ti = t;
    
    // get a new list
    // 1st array is number of threads
    // 2nd is always 3
    tmt_list = new int[nbr][3];

    // initialize the list
    for  (int i = 0; i < nbr; i++) {
            
          // status, rc, times used all zero
          tmt_list[i][0] = 0;
          tmt_list[i][1] = 0;
          tmt_list[i][2] = 0;

    } // end-for

    // start the temporary thread.  This thread then starts the threads
    //   for the processing, T3Thread.
    //   Without this temporary thread, the window would appear to hang. 
    new TyDemoInternalTest3T3Instant(this, nbr, ti).start();
    
} // end-constructor
/**
 * accessor for the list of threads
 *
 * @return int[][] 
 */
public int[][] getList () {    
        
    return tmt_list;
        
} // end-method
/**
 * accessor for the shutdown indicator 
 * @return boolean
 */
public boolean getShutdown ( ) {    
    
    // the shut down status
    if  (shutdown != 0) {

 				// is shuting down
        return true;

    } // endif    

		// not shutting down
    return false;
        
} // end-method
/**
 * mutator for the finished indication
 *
 *  Using volatile on the definition or syncronizing the method is at your discretion. 
 *
 * @param who int 
 */
 
public synchronized void setDone (int who) {    
    
    // When the index is invalid, exit
    if  ((who < 0) ||
         (who >= nbr_in_list)) {
          
        return;    
    } // endif
    
    // status is done
    tmt_list[who][0] = 1;
    
} // end-method
/**
 * mutator for the shutdown indicator
 * 
 */
 
public void setShutdown ( ) {    
    
    // set the shut down status
    shutdown = 1;
                
} // end-method
/**
 * mutator 
 *
 * Update the return code,  (second integer in second array), 
 *   and        times used, (third integer in second array).
 *
 *  Using volatile on the array definition or syncronizing the method is at your discretion. 
 *
 * @param who int
 * @param rc int
 * @param times int
 */
 
public synchronized void setUpdate (int who, int rc, int times) {    
    
    // When the index is invalid, exit
    if  ((who < 0) ||
         (who >= nbr_in_list)) {
          
        return;    
    } // endif
    
    // save the return code
    tmt_list[who][1] = rc;
    
    // save the number of times used
    tmt_list[who][2] = times;
    
} // end-method
} // end-class
