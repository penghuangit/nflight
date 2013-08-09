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
 * Backend object for Wait Lists GUI
 */
public class TyWlDataBean2 {
	
	// indicators
	private int refresh_Result = 0;
		
	// Que name
	private String O_que_name = null;     

	// where access logic lives
  private TyWlDataClient module = null; 
	
/**
 * Constructor
 */
public TyWlDataBean2 ( ) {

  // new access module
  module = new TyWlDataClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyWlDataBean2(TymeacInterface TyI) {

	// new access module
  module = new TyWlDataClient(TyI);

} // end-constructor

/**
 * accessor for refresh result
 * @return int
 */ 
public int getRefResult () { return refresh_Result; } // end-method

/**
 * Refresh button
 * @param S_que_name JTextField - queue name
 * @param S_in_use   JList - number in use
 * @param S_used     JList - number used
 * @param S_used     JList - high used
 * @param S_reset    JList - number reset
 * @param S_over_p   JList - number over primary overflow
 * @param S_over_s   JList - number over secondary overflow
 */
@SuppressWarnings("unchecked")
public void refreshButton(JTextField S_que_name,
                          JList S_in_use,
                          JList S_used,
                          JList S_hi_used,
                          JList S_reset,
                          JList S_over_p,
                          JList S_over_s) {
    
    
  // set the queue name
  O_que_name = S_que_name.getText();

	TyWLElements[] result = null;
  
	// over to tymeac		
	result = module.refresh(O_que_name);          
      
  // When a commumication failure
  if  (result == null) {

      // error
      refresh_Result = 0;

      // done
      return;

  } // endif      
      
  // When shutting down
  if  (result[0].status == 1) {

      // error
      refresh_Result = 1;

      // done
      return;

  } // endif       
      
  // When the que name was not found
  if  (result[0].status == 2) {

      // error
      refresh_Result = 2;

      // done
      return;

  } // endif             
  
  // number of occurranes
  int nbr_list = result.length;
  
  // arrays for the lists
  String[] in_use  = new String[nbr_list];
  String[] used    = new String[nbr_list];
  String[] hi_used = new String[nbr_list];
  String[] reset   = new String[nbr_list];
  String[] over_p  = new String[nbr_list];
  String[] over_s  = new String[nbr_list];      
  
  // loop thru all the returned data  
  for (int i = 0; i < nbr_list; i++) {
    
    // set into each list          
    in_use[i]  = "" + result[i].in_use;
    used[i]    = "" + result[i].used; 
    hi_used[i] = "" + result[i].hi_used; 
    reset[i]   = "" + result[i].reset; 
    over_p[i]  = "" + result[i].over_p; 
    over_s[i]  = "" + result[i].over_s;
          
  } // end-for
  
  // set into list 
  S_in_use.setListData(in_use);   
  S_used.setListData(used);  
  S_hi_used.setListData(hi_used);  
  S_reset.setListData(reset);    
  S_over_p.setListData(over_p);  
  S_over_s.setListData(over_s);        
  
  // good return
  refresh_Result = 3;
    
} // end-method
} // end-class
