package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import javax.swing.*;
import com.tymeac.base.*;
import com.tymeac.client.*;

/**
 * Backend object for the aysnc request status GUI
 */
public class TyReqStatusBean2 {
	
	// indicators
	private int get_Result;

	 // where access logic lives
  private TyReqStatusClient module = null; 
	
/**
 * Constructor 
 */
public TyReqStatusBean2 ( ) {

  // new access module
  module = new TyReqStatusClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyReqStatusBean2(TymeacInterface TyI) {
    
  // new access module
  module = new TyReqStatusClient(TyI);
    
} // end-constructor

/**
 * Cancel Button
 * @param S_TyId JTextField - session id
 * @param S_request JTextField - request id
 */
public void cancelButton(JTextField S_TyId,
                      	 JTextField S_request) {
    
  // parms
  long id  = 0;
  long req = 0;  
  
  // convert to long
  try {
      id = Long.parseLong(S_TyId.getText());
  } // end-try

  catch (NumberFormatException e) {
  } // end-catch
  
  // When invalid
  if  (id < 1) {

      // error
      get_Result = 1;

      // done
      return;

  } // endif      
  
  // convert to long
  try {
      req = Long.parseLong(S_request.getText());
  } // end-try

  catch (NumberFormatException e) {
  } // end-catch

  // When invalid
  if  (req < 1) {

      // error
      get_Result = 2;

      // done
      return;

  } // endif
  
	// try to cancel the request
	get_Result = module.cancelReq(id, req); 
        
} // end-method

/**
 * Get Button
 * @param S_TyId JTextField - session id
 * @param S_request JTextField - request id
 */
public void getButton(JTextField S_TyId,
                      JTextField S_request) {
    
  // parms
  long id  = 0;
  long req = 0;  
  
  // convert to long
  try {
      id = Long.parseLong(S_TyId.getText());
  } // end-try

  catch (NumberFormatException e) {
  } // end-catch
  
  // When invalid
  if  (id < 1) {

      // error
      get_Result = 1;

      // done
      return;

  } // endif      
  
  // convert to long
  try {
      req = Long.parseLong(S_request.getText());
  } // end-try

  catch (NumberFormatException e) {
  } // end-catch

  // When invalid
  if  (req < 1) {

      // error
      get_Result = 2;

      // done
      return;

  } // endif
  
	// get the result
  get_Result = module.getStatus(id, req); 
        
} // end-method

/**
 * accessor for get result
 * @return int
 */ 
public int getGetResult () { return get_Result; } // end-method
} // end-class
