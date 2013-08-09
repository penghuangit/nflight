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
 * Backend object for the Function Data GUI
 */
public class TyFuncDataBean2 {
	
	// indicators
	private int refresh_Result = 0;
				
	// old value from the import
	private String O_func_name  = null; 
	
  // where access logic lives
  private TyFuncDataClient module = null;	

/**
 * Constructor
 */
public TyFuncDataBean2 ( ) {
    
  // new access module
  module = new TyFuncDataClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyFuncDataBean2(TymeacInterface TyI) {
    
  // new access module
  module = new TyFuncDataClient(TyI);

} // end-constructor

/**
 * accessor for refresh result
 * @return int
 */ 
public int getRefResult () {
  
  return refresh_Result;
    
} // end-method

/**
 * Refresh Button
 * @param S_func_name JTextField - name of function
 * @param S_list java.awt.Jlist - list of queues
 */ 
@SuppressWarnings("unchecked")
public void refreshButton(JTextField S_func_name,
                          javax.swing.JList S_list) {                                
    
  // set the queue name
  O_func_name = S_func_name.getText();
  
  // do access
	String[] from = module.refresh(O_func_name);

  // remove all the data from the list
  S_list.removeAll();  
      
  // When a commumication failure
  if  (from == null) {

      // error
      refresh_Result = 0;

      // done
      return;

  } // endif      
  
  // When shutting down
  if  (from[0].compareTo("N286") == 0) {

      // error
      refresh_Result = 1;

      // done
      return;

  } // endif       
  
  // When not found
  if  (from[0].compareTo("N287") == 0) {

      // error
      refresh_Result = 2;

      // done
      return;

  } // endif  
    
   // update the JList 
  S_list.setListData(from); 
  
  // good return
  refresh_Result = 3;
    
} // end-method
} // end-class
