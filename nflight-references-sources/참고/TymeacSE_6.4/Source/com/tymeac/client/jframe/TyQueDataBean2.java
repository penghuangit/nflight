package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2008 Cooperative Software Systems, Inc. 
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
 * Backend object for the Queue Data GUI
 */
public class TyQueDataBean2 {
	
	// indicators
	private int import_Result = 0;
	private int update_Result = 0;
	private boolean last_read = false;
		
	// old value from the import
	private String O_que_name   = null;
	private int    nbr_entries  = 0; // nbr wl entries 
		
	// new values from the window
	private String  N_que_name  = null; 
	
	 // where access logic lives
  private TyQueDataClient module = null; 

/**
 * Constructor
 */
public TyQueDataBean2 ( ) {

  // new access module
  module = new TyQueDataClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyQueDataBean2(TymeacInterface TyI) {

	// new access module
   module = new TyQueDataClient(TyI);

} // end-constructor

/**
 * accessor for import result
 * @return int
 */
public int getImpResult () { return import_Result; } // end-method

/**
 * accessor for update result
 * @return int
 */ 
public int getUpdResult () { return update_Result; } // end-method

/**
 * Import button
 * @param S_que_name JTextField - que name
 * @param S_wait_time JTextField - wait time
 * @param S_kill_time JTextField - kill time
 * @param S_overall JTextField - overall %
 * @param S_individ JTextField - individual %
 * @param S_func_name JTextField - factor
 * @param S_average JTextField - average %
 * @param S_entries JTextField - number of entries 
 * @param S_timeout JTextField - appl. timeout
 */

public void importButton( JTextField S_que_name,
                          JTextField S_wait_time,
                          JTextField S_kill_time,
                          JTextField S_overall,
                          JTextField S_individ,
                          JTextField S_factor,
                          JTextField S_average,
                          JTextField S_entries,
                          JTextField S_th_entries,
                          JTextField S_timeout) {    
    
  // set the queue name
  O_que_name = S_que_name.getText();

	// access
  TyQueElements result = module.importElements(O_que_name); 
          
  // When a commumication failure
  if  (result == null) {
      
      // no read
      last_read = false;

      // bad result     
      import_Result = 0;
      return;

  } // endif

  // did a read
  last_read = true;     
      
  // When shutting down
  if  (result.que_name.compareTo("N286") == 0) {

      // cant do
      import_Result = 1;
      
      return;

  } // endif       
      
  // When the que name was not found
  if  (result.que_name.compareTo("N287") == 0) {

      // not here
      import_Result = 2;
      
      return;

  } // endif               
  
  // set for the screen   
  S_wait_time.setText("" + result.wait_time);

  S_kill_time.setText("" + result.kill_time);
  
  String S = "" + result.overall;
  if  (S.length() == 3) S = S + "0"; // so % shows as x0 not x
  S_overall.setText(S.substring(2));   
  
  S = "" + result.individ;
  if  (S.length() == 3) S = S + "0"; // so % shows as x0 not x
  S_individ.setText(S.substring(2));
  
  S = "" + result.factor;
  if  (S.length() == 3) S = S + "0"; // so % shows as x0 not x
  S_factor.setText(S.substring(2));
  
  S = "" + result.average;
  if  (S.length() == 3) S = S + "0"; // so % shows as x0 not x
  S_average.setText(S.substring(2));

  S_entries.setText("" + result.entries);
  
  S_th_entries.setText("" + result.th_entries);
  
  S_timeout.setText("" + result.timeout);
  
  nbr_entries = result.entries;
  
  // good return
  import_Result = 3;
                          
} // end-method

/**
 * Updater button
 * @param S_que_name JTextField - que name
 * @param S_wait_time JTextField - wait time
 * @param S_kill_time JTextField - kill time
 * @param S_overall JTextField - overall %
 * @param S_individ JTextField - individual %
 * @param S_func_name JTextField - factor
 * @param S_average JTextField - average %
 * @param S_entries JTextField - number of entries 
 */
public void updateButton( JTextField S_que_name,
                          JTextField S_wait_time,
                          JTextField S_kill_time,
                          JTextField S_overall,
                          JTextField S_individ,
                          JTextField S_factor,
                          JTextField S_average,
                          JTextField S_entries,
                          JTextField S_th_entries,
                          JTextField S_timeout) {                                
    
  // name of this queue
  N_que_name = S_que_name.getText();
  
  // When the que name doesnt match what was 'read for update'
  if  (O_que_name.compareTo(N_que_name) != 0) {

      // no good
      update_Result = 0;
      
      return;

  } // endif
  
  // When no prior read
  if  (!last_read) {

      // no good
      update_Result = 0;
      
      return;

  } // endif
  
  // get a new parm
  TyQueElements tqe = new TyQueElements();
  
  // que name
  tqe.que_name = O_que_name;
              
  // wait time
  try {
    tqe.wait_time = Integer.parseInt(S_wait_time.getText());
  } // end-try

  catch (NumberFormatException e) {
    tqe.wait_time = 0;
  } // end-catch

  // kill time
  try {
    tqe.kill_time = Integer.parseInt(S_kill_time.getText());
  } // end-try

  catch (NumberFormatException e) {
    tqe.kill_time = 0;
  } // end-catch

  // Number of Wait List entries
  try {
    tqe.entries = Integer.parseInt(S_entries.getText());
  } // end-try

  catch (NumberFormatException e) {
    tqe.entries = 0;
  } // end-catch

  // When Wl Entries bad
  if  (tqe.entries < 1) {

      // bad 
      update_Result = 5;
      
      return;

  } // endif
  
  // Number of Wait List entries for thresholds
  try {
    tqe.th_entries = Integer.parseInt(S_th_entries.getText());
  } // end-try

  catch (NumberFormatException e) {
    
    // force same as physical
    tqe.th_entries = tqe.entries;
      
  } // end-catch

  // When threshold Wl Entries bad
  if  ((tqe.th_entries < 1) ||
       (tqe.th_entries > tqe.entries)) {

      // bad 
      update_Result = 12;
      
      return;

  } // endif
  
  String S;
  
  // overall %
  try {
    S = "0." + S_overall.getText(); 
    tqe.overall = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 7;
    return;
  } // end-catch

  // individual %
  try {
    S = "0." + S_individ.getText();
    tqe.individ = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 8;
    return;
  } // end-catch
  
  // weighted factor
  try {
    S = "0." + S_factor.getText();
    tqe.factor = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 9;
    return;
  } // end-catch
  
  // weighted average
  try {
    S = "0." + S_average.getText();
    tqe.average = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 10;
    return;
  } // end-catch
  
  // timeout
  try {
    tqe.timeout = Integer.parseInt(S_timeout.getText());
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 11;
    return;
  } // end-catch
  
  // over to tymeac
  update_Result = module.updateElements(tqe); 
                                      
  // When the new WL entries failed, resore the original
  if  (update_Result == 6) {

      // back to old one
      S_entries.setText("" + nbr_entries);
  }
  else {
      // set new one
      nbr_entries = tqe.entries;
  }  // endif
    
} // end-method
} // end-class
