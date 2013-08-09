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
import com.tymeac.demo.*;
import java.util.*;

/**
 * Simple test thread for multiple clients. 
 * 
 *   Two user switches: full_checking - Set to true it randomly develops a passed input to Tymeac
 *                                    and checks for proper results from Tymeac. A lot of overhead.
 *                                    
 *                                    Set to false, a null input is passed to Tymeac. This eliminates
 *                                    work on the server side and all checking on this side.
 *   
 *                      new_priority - Set this to true to enable this thread to calculate a new priority
 *                                    for each request. The priority is generated randomly. When testing 
 *                                    priority wait lists, this is usefull.
 *                                    
 *                                    Set this to false to disable calculating a new priority for each request. 
 *                                    A value of 1 is passed as the priority to the Server for every request. 
 *      
 */

public final class TyDemoT3Thread
		  extends Thread {			
		
	// base storage used by all threads.
	private TyDemoT3Base base = null;
    
	// the parameter for Tymeac    
	private TymeacParm TP = null;

	// RMI methods for Tymeac
	private TySvrComm TSC = null;
        
	// the index into the base array for this unit
	private int me = -1; // initially invalid
	
  // passed string
  private String toTymeac;
	
	// random number generator
	private Random myRandom = new Random();
	
	// array of numbers for building passed numbers to Tymeac
	private String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	
	/*
   * Set this to true 
   *   to enable this thread to calculate a new input for every request. The input is
   *   generated randomly and the return is checked against that input.
   *   
   *  Set this to false
   *    to disable calculating a new input for every request. A null value is passed as
   *    the input and no return values come back from the Server. Much, much less cpu time
   *    is used with false.  
	 */
	private boolean full_checking = false;
  
  /*
   * Set this to true 
   *   to enable this thread to calculate a new priority for each request. The priority is
   *   generated randomly. When testing priority wait lists, this is usefull.
   *   
   *  Set this to false
   *    to disable calculating a new priority for each request. A value of 1 is passed as
   *    the priority to the Server for every request. 
   */
  private boolean new_priority = false;

/**
 * Tymeac Demonstration System, multi test thread constructor.
 * This constructor loads the private instance fields from the passed
 *    parameters and names the thread: "TyT3-" + param number.
 *
 * @param from T3_Base
 * @param number int
 */
public TyDemoT3Thread (TyDemoT3Base from, int number) {	
	
	// give the thread a name
	super("TyT3-" + number);
	
	// base storage used by all threads in the test 
	base = from;
	
	// the unique index for this unit
	me = number;
	
	// form a parameter for Tymeac   
	TP = new TymeacParm(null,          // data 
						  "Function_1", // function name
						  30,            // wait time
						  1);            // priority
		
	  
	// get the RMI methods for Tymeac
	TSC = new TySvrComm();
	 	  
} // end-constructor

/**
 * Do an asynchronous request on the Tymeac Server.
 *    format the return code into an integer and return to caller
 * 
 * @return int
 */
private int asyncRequest ( ) {	

	// call the remote method   
	return checkRC(TSC.asyncRequest(TP));
	
} // end-method

/**
 * Check the return data from Tymeac
 * 
 * @param back Object[] from Tymeac
 * @return int 
 */
private int checkData(Object[] back) {
	
	/*
	 * The passed data (toTymeac) is a string of 18 digits. Each of the three 
	 *   application classes works on positional parts of that string:
	 *   Demo1, the A digits: 01,02 - 03,04 - 05,06 
	 *   Demo2, the B digits: 07,08 - 09,10 - 11,12 
	 *   Demo3, the C digits: 13,14 - 15,16 - 17,18
	 *   
	 *    Each application adds up the digits and 
	 *    returns the sum so it looks like this:
	 *    
	 *    #Demo1:A1(00)+A2(00)+A3(00)=(000)#
	 *    #Demo2:B1(00)+B2(00)+B3(00)=(000)#
	 * 		#Demo3:C1(00)+C2(00)+C3(00)=(000)#
	 * 
	 * The 18 digits were randomly generated so we must check what was passed
	 *   against that which was recieved. Any error returns 88xx.
	 */
		
	// compare to
  String A1 = toTymeac.substring(0,  2),
  			 A2 = toTymeac.substring(2,  4), 
  			 A3 = toTymeac.substring(4,  6), 
  			 B1 = toTymeac.substring(6,  8), 
  			 B2 = toTymeac.substring(8,  10), 
  			 B3 = toTymeac.substring(10, 12), 
  			 C1 = toTymeac.substring(12, 14), 
  			 C2 = toTymeac.substring(14, 16), 
  			 C3 = toTymeac.substring(16, 18);
  	
	// total parms with data
	int tot_parms = back.length;
	
	// When no data parms (first contains return code, etc), good bye
	if  (tot_parms < 2) return 8810;
	
  // Each string
	String S;
	
	// do all the passed back objects (second to end)
	for (int i = 1; i < tot_parms; i++) {
		
		try {		
			// each string
			S = (String) back[i];
			
			// look for each application signature
			if  (S.substring(1, 6).compareTo("Demo1") == 0) {					
									
					// When a match error, done
					if  ((S.substring(11, 13).compareTo(A1) != 0 ) ||
							 (S.substring(18, 20).compareTo(A2) != 0 ) ||
							 (S.substring(25, 27).compareTo(A3) != 0 )) return 8820;
				
			} 
			else {			
					// look for application signature
					if  (S.substring(1, 6).compareTo("Demo2") == 0) {    
							
							// When a match error, done
							if  ((S.substring(11, 13).compareTo(B1) != 0 ) ||
									 (S.substring(18, 20).compareTo(B2) != 0 ) ||
									 (S.substring(25, 27).compareTo(B3) != 0 )) return 8830;
						
					} else {
							// look for application signature
							if  (S.substring(1, 6).compareTo("Demo3") == 0) {    
									
									// When a match error, done
									if  ((S.substring(11, 13).compareTo(C1) != 0 ) ||
											 (S.substring(18, 20).compareTo(C2) != 0 ) ||
											 (S.substring(25, 27).compareTo(C3) != 0 )) return 8840;
                  
              } else {
                  // oops
                  return 8850;
										
							} // endif
					} // endif
			} // endif			
		} // end-try 
		
		catch (Exception e) {
			
			System.out.println(e.toString());
			
			// get out
			return 8860;
			
		} // end-catch		
	} // end-for			
	
	// done
	return 0;
	
} // end-method

/**
 * Check the return code from Tymeac
 * 
 * @param back Object[] from Tymeac
 * @return int 
 */
private int checkRC(Object[] back) {
	
	// return code
	int rc;
	
	//right parenthesis
	char paren = ')'; 

	// When nothing, cannot continue
	if  (back == null)  return 9000;
		
	// 1st string
	String S = (String) back[0];
		
	// When valid return message    
	if  ((S != null) &&         // null from registry
			 (S.length() > 13)) {   // minimum message is "Tymeac Sx(nnnn)" 

			// start after the fourteenth digit
			int i = 14; 
	
			// find the right paren
			while (i < S.length() && (S.charAt(i) != paren)) i++;
			
				try {
					// convert to integer
					rc = Integer.parseInt(S.substring(10, i));
					
				} // end-try
	 
				catch (NumberFormatException e) {
					
					// not an integer
					rc = 1; 
					
				} // end-catch
	}
	else {
			// null or invalid length
			rc = 9000;
			
	} // endif

	// done
	return rc;
	
} // end-method

/**
 * Generate the 18 digit string for Tymeac input
 * 
 * @return String formed string of digits
 */
private String genPass () {
	
	// formed string
	String back = 
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)] +
		numbers[myRandom.nextInt(9)];
	
	// give back
	return back; 
	
} // end-method
 
/**
 * The run method for the thread.    
 * 
 */
public void run() {  
	
   // times used
   int count = 0;
   
   // return code from Tymeac
   int rc = 0;
         
   // continue until in shut down mode from the base, or, the return
   //   code from the request is not zero.   
	while (true) {
		
		// When in shut down mode:
		if  (base.getShutdown()) {
			  
			  // say finished
			  base.setDone(me);
			  
			  // all done
			  break;
			  
		} // endif  
		
		// set new function name 
		TP.setFuncname("Function_1");
    
    // When changing priority, set new priority
    if (new_priority) TP.setPriority(myRandom.nextInt(11));
		
		// When checking
		if  (full_checking) {
			
				// set new input parm 
				toTymeac = genPass();
				TP.setInput(toTymeac);
				
		} // endif
	
		// do a request
		rc = syncRequest(); 
		
		//  When the request was not successfull:
		if  (rc != 0) {
			
			// update the base: my index, return code, nbr time used
			base.setUpdate(me, rc, count);
			
			// say this thread is finished
			base.setDone(me);

			// get out of the loop  
			break;

		} // endif  
		
		// set new function name 
		TP.setFuncname("Function_2");
    
    // When changing priority, set new priority
    if (new_priority) TP.setPriority(myRandom.nextInt(11));
		
		//	 When checking
		if  (full_checking) {
			
				// set new input parm 
				toTymeac = genPass();
				TP.setInput(toTymeac);
				
		} // endif
	
		// do a request
		rc = syncRequest();
		
		//  When the request was not successfull:
		if  (rc != 0) {
			
				// update the base: my index, return code, nbr time used
				base.setUpdate(me, rc, count);
				
				// say this thread is finished
				base.setDone(me);
				
				// get out of the loop  
				break;

		} // endif  
		
		// set new function name 
		TP.setFuncname("Function_3");
    
    // When changing priority, set new priority
    if (new_priority) TP.setPriority(myRandom.nextInt(11));
		
		//	 When checking
		if  (full_checking) {
			
				// set new input parm 
				toTymeac = genPass();
				TP.setInput(toTymeac);
				
		} // endif
	
		// do a request
		rc = syncRequest();
		
		//  When the request was not successfull, all done
		if  (rc != 0) {
		
				// update the base: my index, return code, nbr time used
				base.setUpdate(me, rc, count);
				
				// say this thread is finished
				base.setDone(me);
				
				// get out of the loop  
				break;

		} // endif  
		
		// set new function name 
		TP.setFuncname("Function_4");
    
    // When changing priority, set new priority
    if (new_priority) TP.setPriority(myRandom.nextInt(11));
		
		//	 When checking
		if  (full_checking) {
			
				// set new input parm 
				toTymeac = genPass();
				TP.setInput(toTymeac);
				
		} // endif
	
		// do a request
		rc = syncRequest();
		
		//  When the request was not successfull, all done
		if  (rc != 0) {
		
				// update the base: my index, return code, nbr time used
				base.setUpdate(me, rc, count);
				
				// say this thread is finished
				base.setDone(me);
				
				// get out of the loop  
				break;

		} // endif  
		
		// set new function name 
		TP.setFuncname("Function_5");
    
    // When changing priority, set new priority
    if (new_priority) TP.setPriority(myRandom.nextInt(11));
	
		// do a request
		rc = asyncRequest();
		
		//  When the request was not successfull, all done
		if  (rc != 0) {
		
				// update the base: my index, return code, nbr time used
				base.setUpdate(me, rc, count);
				
				// say this thread is finished
				base.setDone(me);
				
				// get out of the loop  
				break;

		} // endif  
		
		// set new function name 
		TP.setFuncname("Function_6");
    
    // When changing priority, set new priority
    if (new_priority) TP.setPriority(myRandom.nextInt(11));
	
		// do a request
		rc = asyncRequest();
		
		//  When the request was not successfull, all done
		if  (rc != 0) {
		
				// update the base: my index, return code, nbr time used
				base.setUpdate(me, rc, count);
				
				// say this thread is finished
				base.setDone(me);
				
				// get out of the loop  
				break;

		} // endif  
		
		// set new function name 
		TP.setFuncname("Function_7");
    
    // When changing priority, set new priority
    if (new_priority) TP.setPriority(myRandom.nextInt(11));
	
		// do a request
		rc = asyncRequest();  
		
		//  When the request was not successfull, all done
		if  (rc != 0) {
		
				// update the base: my index, return code, nbr time used
				base.setUpdate(me, rc, count);
				
				// say this thread is finished
				base.setDone(me);
				
				// get out of the loop  
				break;

		} // endif  
  
		// increment times thru here without error      
		count++;
	
		// update the base
		//  my index to the array
		//  the return code
		//  times used
		base.setUpdate(me, rc, count);
						
	} // end-while     
      
} // end-method

/**
 * Do a synchronous request on the Tymeac Server.
 *    format the return code into an integer
 * 
 * @return int
 */
private int syncRequest ( ) {	
	
	// return code
	int rc;

	// return array
	Object back[] = null;

	// call the remote method   
	back = TSC.syncRequest(TP);
	
	// check the tymeac rc
	rc = checkRC(back);
	
	// When not zero, back
	if  (rc != 0 ) return rc;
	
	//When NOT checking
	if  (!full_checking) return rc;
	
	// check the data and return what came back
	return checkData(back);	
	
} // end-method
} // end-class
