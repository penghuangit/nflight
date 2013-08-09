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
import com.tymeac.base.*;
import com.tymeac.demo.*;
 /** 
 *  Tymeac demonstration system
 *      bean in support of Frame class T1
 */
  
public final class TyDemoT1Bean2 {
	
	private String f1;  // function 
	
	private String a1;  // input value
	private String a2;  // input value
	private String a3;  // input value
	
	private String b1;  // input value
	private String b2;  // input value
	private String b3;  // input value
	
	private String c1;  // input value
	private String c2;  // input value
	private String c3;  // input value
  
  private String passS = null;
  private Object[] back = null;

	private TySvrComm svr = null;
  
  private LoginContext loginContext = null;	  
  
public TyDemoT1Bean2(String[] args) {
  
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
 * Tymeac demonstration system
 *     
 *    go to Tymeac with a either a synchronous request or an 
 *     asynchronous request, depending on the function.
 *
 *
 *    Initially,  Tymeac Functions 1 - 4 are used as a syncRequest and
 *                  Tymeac Functions 5 - 7 are used as an asyncRequest. 
 *                  Tymeac Function  8 is the recurrsive Function and is
 *                    a syncRequest.
 *
 *    You may wish to add code that checks for certain values and calls
 *      either a sync or async Tymeac method.  
 * 
 * @param args Object[]
 * @return java.lang.String
 */
 
private void goTymeac (String input) {

    // string to object   
    Object data = input;

    // the parmeter passed to Tymeac
    TymeacParm TP = new TymeacParm(data, // a,b,c values
                                    f1,   // function
                                    20,   // wait no more than 20 seconds
                                    1);   // priority 1  
 
    // When none exists
    if  (svr == null) {

        // get a new obj
        svr = new TySvrComm();

    } // endif   

    // check for which function is present
    if  ((f1.compareTo("Function_5") <  0) ||
         (f1.compareTo("Function_8") == 0))   { 
                                              
        // do a sync request       
        back = svr.syncRequest(TP);
        
        // back
        return;
    }
    else {  
				back = svr.asyncRequest(TP);
        
        // back
        return;
    } // endif
       
} // end-method
/** 
 *  Tymeac demonstration system.
 *
 *      Get the input from the window class.  
 *    Form the parameters needed for Tymeac.  
 *    Call the Tymeac function.
 *    Move the results to the TextFields.
 *
 *
 * @param F1 javax.swing.JTextField
 * @param A1 javax.swing.JTextField
 * @param A2 javax.swing.JTextField
 * @param A3 javax.swing.JTextField
 * @param B1 javax.swing.JTextField
 * @param B2 javax.swing.JTextField
 * @param B3 javax.swing.JTextField
 * @param C1 javax.swing.JTextField
 * @param C2 javax.swing.JTextField
 * @param C3 javax.swing.JTextField
 * @param Result javax.swing.JTextField
 *  
 */
 
public void readIn( JTextField F1,
                    JTextField A1,
                    JTextField A2,
                    JTextField A3,
                    JTextField B1,
                    JTextField B2,
                    JTextField B3,
                    JTextField C1,
                    JTextField C2,
                    JTextField C3,
                    JTextField Result) {
    
    // Function   
    f1 = F1.getText();
    
    // When none, error   
    if  (f1.length() < 1) {
    
        // set error msg
        Result.setText("Enter a function name");
        return;
        
    } // endif       
    
    // temporary string
    String temp;
    
    /*
     * Take each of the input values for A1-3, B1-3, C1-3 and
     *   put these into the String used as the input String for
     *   Tymeac.  Only the first two digits are used. 
     *
     */   
    
    // input value to string
    temp = A1.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   a1 = "00";
                  break;
                  
        case 1:   a1 = "0" + temp.substring(0,1);
                  break;
                  
        default:  a1 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = A2.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   a2 = "00";
                  break;
                  
        case 1:   a2 = "0" + temp.substring(0,1);
                  break;
                  
        default:  a2 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = A3.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   a3 = "00";
                  break;
                  
        case 1:   a3 = "0" + temp.substring(0,1);
                  break;
                  
        default:  a3 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = B1.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   b1 = "00";
                  break;
                  
        case 1:   b1 = "0" + temp.substring(0,1);
                  break;
                  
        default:  b1 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = B2.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   b2 = "00";
                  break;
                  
        case 1:   b2 = "0" + temp.substring(0,1);
                  break;
                  
        default:  b2 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = B3.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   b3 = "00";
                  break;
                  
        case 1:   b3 = "0" + temp.substring(0,1);
                  break;
                  
        default:  b3 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = C1.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   c1 = "00";
                  break;
                  
        case 1:   c1 = "0" + temp.substring(0,1);
                  break;
                  
        default:  c1 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = C2.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   c2 = "00";
                  break;
                  
        case 1:   c2 = "0" + temp.substring(0,1);
                  break;
                  
        default:  c2 = temp.substring(0,2);                               
    
    } // end-switch
    
    // input value to string
    temp = C3.getText();
    
    // only use first two bytes
    switch (temp.length()) {
      
        case 0:   c3 = "00";
                  break;
                  
        case 1:   c3 = "0" + temp.substring(0,1);
                  break;
                  
        default:  c3 = temp.substring(0,2);                               
    
    } // end-switch
      
        
    // form into a concatenated stream
    String S =  a1 + a2 + a3 +
                b1 + b2 + b3 +
                c1 + c2 + c3;
    
    // call the Tymeac method 
    back = null;
    passS = S;
    
    // When not using security     
  if (loginContext == null) {
  
      // say what
      System.out.println("Using INSECURE client");
      
      // non-secure
      goTymeac(passS);     
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
                    goTymeac(passS);
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
        
    Object[] R = back;
    
    // When nothing came back say so
    if  (R == null)  {
      
        // say bad return   
        Result.setText("Communication Error");

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
    Result.setText(stuff);
          
    
    // all done 
    return;
  
} // end-method
} // end-class
