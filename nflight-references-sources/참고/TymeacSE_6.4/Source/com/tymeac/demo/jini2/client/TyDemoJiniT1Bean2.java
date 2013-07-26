package com.tymeac.demo.jini2.client;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.demo.jini2.base.*;
import javax.swing.*;
/** 
 *
 *  Tymeac demonstration system
 *      bean in support of Frame class TyDemoJini1 
 */ 

public final class TyDemoJiniT1Bean2 {
  
  // instance field, the intervface
  private TyDemoJiniDocInterface svr = null;	  

/**
 * Constructor 
 */
 
public TyDemoJiniT1Bean2() {

   // 
  try {                       
            
    // get a new obj and the remote object interface
    svr = new TyDemoJiniT1GetProxy().getInterface();
    
  } // end-try
  
  catch (Exception e) {
  
    System.out.println("Exception: " + e.getMessage());
    return;
    
  } // end-catch 

} // end-constructor
/** 
 *
 *  Tymeac demonstration system. -breakiterator
 *
 *      Get the input from the window class.  
 *    Form the parameters 
 *    Move the results to the TextFields.
 *
 * @param in_doc java.awt.TextField - document
 * @param Result java.awt.TextField - result of processing
 *  
 */
 
public void sendNoWait( JTextField in_doc,
            JTextField Result) {

  // When no service
  if  (svr == null) {
      
      Result.setText("Could not find the service");
      return;
      
  } // endif                
  
  // Function   
  String doc = in_doc.getText();
  
  // When none, error   
  if  (doc.length() < 1) {
    Result.setText("Enter a document name");
    return;
  } // endif       
  
  // new string array
  String[] str = new String[1];
  str[0] = doc;  
  
  // return value
  String R = null;  
            
  // 
  try {                       
            
    // call the method
    R = svr.putDoc(str, false);
    
  } // end-try
  
  catch (java.rmi.RemoteException e) {
  
    System.out.println("Remote Exception: " + e.getMessage());
    return;
    
  } // end-catch
  
  // When nothing came back say so
  if  ((R == null) || (R.length() < 1)) {
    
    // say bad return   
    Result.setText("Communication Error");
  }
  else {
    // say what came back
    Result.setText(R);
  } // endif            
      
  
  // all done 
  return;
  
} // end-method
/** 
 *
 *  Tymeac demonstration system. -breakiterator
 *
 *      Get the input from the window class.  
 *    Form the parameters 
 *    Move the results to the TextFields.
 *
 * @param in_doc java.awt.TextField - document
 * @param Result java.awt.TextField - result of processing
 *  
 */
 
public void sendWait( JTextField in_doc,
            JTextField Result) {
  
  // When no service
  if  (svr == null) {
      
      Result.setText("Could not find the service");
      return;
      
  } // endif      
  
  // Function   
  String doc = in_doc.getText();
  
  // When none, error   
  if  (doc.length() < 1) {
    Result.setText("Enter a document name");
    return;
  } // endif       
  
  // new string array
  String[] str = new String[1];
  str[0] = doc;    
  
  // return value
  String R = null;  
            
  // 
  try {                       
            
    // call the method
    R = svr.putDoc(str, true);
    
  } // end-try
  
  catch (java.rmi.RemoteException e) {
  
    System.out.println("Remote Exception: " + e.getMessage());
    return;
    
  } // end-catch
  
  // When nothing came back say so
  if  ((R == null) || (R.length() < 1)) {
    
    // say bad return   
    Result.setText("Communication Error");
  }
  else {
    // say what came back
    Result.setText(R);
  } // endif            
      
  
  // all done 
  return;
  
} // end-method
} // end-class
