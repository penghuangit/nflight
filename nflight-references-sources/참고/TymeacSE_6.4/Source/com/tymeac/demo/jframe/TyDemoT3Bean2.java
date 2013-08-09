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
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import javax.swing.*; 
/**
 * Tymeac demonstration system.
 *
 * This class supports the Frame class T3.  Methods here are used
 *      in the Conn?? methods of class T3 which correspond to buttons
 *      pressed on the window.
 * 
 */

public final class TyDemoT3Bean2{	
	
        // the array [nbr of threads] [3 integers, see below] 
      	//  1st is status, 0-working, 1-finished
      	//  2nd is return code from Tymeac
      	//  3rd is number of times used  
      	private int t3[][] = null;
	
      	// number of threads
      	private int nbr_in_list = 0; 
	
      	// the result of the start button
      	private int start_result = 0;
        
        // security
        private LoginContext loginContext = null;
	
	// the base storage for the threads
	private TyDemoT3Base tmt = null;
  
public TyDemoT3Bean2 (String[] args) {
  
  /*
   * There is one command line option
   *   -secure
   * When this option is present, we make a call
   * to get the login context.
   * 
   */   
  // When 1st arg is for security 
  if  ((args.length > 0) &&
       (args[0].equalsIgnoreCase("-secure"))) {
         
      // see if there is a login module
      loginContext = SecurityModule.getContext();
      
  } // endif 
  
  // When good context
  if  (loginContext != null) {
    
      System.out.println("Got Context");
    
      // When NOT a good login
      if  (!SecurityModule.login(loginContext)) {
        
          // no security
          loginContext = null;
          
          System.out.println("Login failed");
          
      } // endif      
  } // endif
  
} // end-constructor    	  

/**
 * The refresh button was pressed on the Window.
 *  This method gets the updated list from the common storage in
 *    T3_Base and fills in the awt.List. 
 *
 * @param S_list java.awt.List 
 */
 
@SuppressWarnings("unchecked")
public void doRefresh (javax.swing.JList S_list) {
    
    // When not started
    if  (tmt == null) {

        // get out of here
        return;

    } // endif    

    // remove all from list   
    S_list.removeAll();
    
    // strings used for status and return code
    String S, R;
                       
    // obj[] for list
    String[] obj = new String[nbr_in_list]; 
    
    // get the updated list   
    t3 = tmt.getList();
    
    // loop thru all in the list
    for  (int i = 0; i < nbr_in_list; i++) {
            
          // status (1st integer) is 0 or 1
          if  (t3[i][0] == 0) {
                  
              S = "Working   ";
          }
          else {
              S = "__Fini_   ";
          } // endif
            
          // return code (2nd integer) is zero or a 4 digit number        
          if  (t3[i][1] == 0) {
                  
              R = "(0000)   ";
          }
          else {
                             
              R = "("
                  + t3[i][1]
                  + ")   ";         
          } // endif  
            
          // one line in the list:
          //  status, return code, times used (3rd integer)
          obj[i] = new String(S + R + t3[i][2]);
                        
    } // end-for    
        
    // update the JList
    S_list.setListData(obj); 
    
} // end-method
/**
 * 
 * The start button was pressed on the Window.
 *    The number of threads must be > 0.
 *    This is also a good place to check for a maximum number.  Starting
 *      too many threads may have an adverse effect on your system.
 *
 *    The base storage is obtained, of Class T3_Base.
 *    The refreshButton() method is used to display the result. 
 * 
 *
 * @param S_number java.awt.TextField
 * @param S_list java.awt.List
 */
@SuppressWarnings("unchecked")
public void doStart (JTextField S_number, javax.swing.JList S_list ) {        
    
    // When there are no passed parms
    if  ((S_number == null) || (S_list == null)) {

        // bad result  
        start_result = 0;
          
        return;
    } // endif    
  
    // number of threads to start
    try {
        nbr_in_list = Integer.parseInt(S_number.getText());
    } // end-try

    catch (NumberFormatException e) {
        nbr_in_list = 0;
    } // end-catch
    
    // When invalid
    if  (nbr_in_list < 1) {
   
        // for list
        String[] obj = new String[1];
              
        // put this message in the list
        obj[0] = new String("Invalid input");   
        S_list.setListData(obj);  
          
        // bad result  
        start_result = 0;
          
        return;
    } // endif 
    
    // When not using security     
    if (loginContext == null) {
  
        // say what
        System.out.println("Using INSECURE client");
      
       // non-secure
       letHerRip(nbr_in_list);     
    } 
    else {    
        try {
          // say what
          System.out.println("Using secure client");
      
          // do as privileged
          Subject.doAsPrivileged(
              loginContext.getSubject(),
              new PrivilegedExceptionAction<Object>() {
                  public Object run() throws Exception {
                      letHerRip(nbr_in_list); 
                      return null;
                  } // end-method
              }, // end-PEA
              null);
            
        } // end-try   
      
        catch (PrivilegedActionException e) {
        
            // say what found
            System.out.println("PrivilegedActionException= " + e);                    
                   
        } // end-catch
      
        catch (Exception e) {
        
            // say what found
            System.out.println("Exception= " + e);                    
                     
        } // end-catch                 
    } // endif 
    
    // display the results using the refresh button method
    doRefresh(S_list);
    
    // the start button is no longer valid
    start_result = 1;
    
} // end-method

/**
 * Start up the new threads
 * @param number
 */
private void letHerRip(int number) {
  
  // start it off 
  tmt = new TyDemoT3Base(number);
  
} // end-method  
/**
 * 
 * The stop button was pressed on the Window.
 *
 * The shut-down indicator, in the base storage, T3_Base,
 *    is set.
 * 
 */
public void doStop () {        
    
    // When not started
    if  (tmt == null) {

        // get out of here
        return;

    } // endif
    
    // set the shut down indicator in the base storage
    tmt.setShutdown();
    
} // end-method
/**
 * accessor for the start result
 * 
 * @return int
 */
 
public int getStartResult ( ) {
  
    return start_result;
    
} // end-method
} // end-class
