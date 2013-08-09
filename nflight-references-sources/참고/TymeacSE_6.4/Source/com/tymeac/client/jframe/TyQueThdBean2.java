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
 * Backend object for the queue threads GUI
 */
public class TyQueThdBean2 {
	
	// indicators
	private int refresh_Result = 0;
	private int disable_Result = 0;
	private int enable_Result  = 0;
	private int enableSpecific_Result  = 0;
	
	private boolean last_read  = false;
		
	// old value from the import
	private String O_que_name = ""; 
	private String[] from     = null;
		
	// new values from the window
	private String  N_que_name  = null;
	
	 // where access logic lives
  private TyQueThdClient module = null; 

/**
 * Constructor
 */
public TyQueThdBean2 ( ) {

  // new access module
  module = new TyQueThdClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyQueThdBean2(TymeacInterface TyI) {

	// new access module
  module = new TyQueThdClient(TyI);

} // end-constructor 

/**
 * Disable button
 * @param S_que_name JTextField - queue name
 * @param S_selected String - selected thread
 * @param S_list JList - list of threads
 */
@SuppressWarnings("unchecked")
public void disableButton(JTextField S_que_name,
                          String    S_selected,
                          javax.swing.JList S_list) {                                
    
  // When nothing selected
  if  (S_selected == null) {

      // error
      disable_Result = 4;

      // done
      return;

  } // endif

  // name of this queue
  N_que_name = S_que_name.getText();
  
  // When the que name doesnt match what was last 'refreshed'
  if  (O_que_name.compareTo(N_que_name) != 0) {

      // error
      disable_Result = 0;

      // done
      return;

  } // endif
  
  // length of the string array
  int len = from.length;
  
  // thread number
  int que_thread = -1;
  
  // loop thru all the strings looking for that which was selected
  for  (int i = 0; i < len; i++) {
          
    // When a match
    if  (from[i].compareTo(S_selected) == 0)  {
          
        // set the thread number
        que_thread = i;

        // get out
        break;
            
    } // endif              
  } // end-for        
          
  // When not found, something happened
  if  (que_thread < 0) {

      // error
      disable_Result = 1;

      // done
      return;

  } // endif  
  
  // When no prior read
  if  (!last_read) {

      // error
      disable_Result = 2;

      // done
      return;

  } // endif
      
  // over to tymeac
  disable_Result = module.disable(O_que_name, que_thread); 

  // When not a normal return                                               
  if  (disable_Result != 4)  return;
  
  // new string array for the returned list
  String[] new_data = module.refresh(O_que_name); 
      
  // When a commumication failure
  if  (new_data == null) {

      // error
      disable_Result = 3;

      // done
      return;

  } // endif  
  
  // set new list in class
  from = new_data;    
  
  // update the JList
  S_list.setListData(new_data); 
    
} // end-method

/**
 * enable all button
 * @param S_que_name JTextField - queue name
 * @param S_list JList - list of threads
 */
@SuppressWarnings("unchecked")
public void enableButton( JTextField S_que_name,
                          javax.swing.JList S_list) {
              
  // name of this queue
  O_que_name = S_que_name.getText();

  // When no prior read
  if  (!last_read) {

      // bad result
      enable_Result = 4;

      // done
      return;

  } // endif
  
  // over to tymeac
  enable_Result = module.enable(O_que_name); 
                                                  
  // When not a normal return
  if  (enable_Result != 0)  return;
  
  // new string array for list
  String[] new_data = module.refresh(O_que_name); 
      
  // When a commumication failure
  if  (new_data == null) {

      // error
      enable_Result = 1;

      // done
      return;

  } // endif  
  
  // set new list in class
  from = new_data;    
  
  // update the JList
  S_list.setListData(new_data); 
    
} // end-method

/**
 * Enable specific thread button. This method re-uses the
 *   disable method on the Server interface by passing
 *   the selected thread number as a negative value. Otherwise,
 *   would require adding a method to the TymeacInterface.
 * @param S_que_name JTextField - queue name
 * @param S_selected String - selected thread
 * @param S_list JList - list of threads
 */
@SuppressWarnings("unchecked")
public void enableSpecificButton( JTextField S_que_name,
                                  String    S_selected,
                                  javax.swing.JList S_list) {                                
    
  // When nothing selected
  if  (S_selected == null) {

      // error
      enableSpecific_Result = 4;
      return;

  } // endif

  // name of this queue
  N_que_name = S_que_name.getText();
  
  // When the que name doesnt match what was last 'refreshed'
  if  (O_que_name.compareTo(N_que_name) != 0) {

      // error
      enableSpecific_Result = 0;
      return;

  } // endif
  
  // length of the string array
  int len = from.length;
  
  // thread number
  int que_thread = -1;
  
  // loop thru all the strings looking for that which was selected
  for  (int i = 0; i < len; i++) {
          
    // When a match
    if  (from[i].compareTo(S_selected) == 0)  {
          
        // set the thread number
        que_thread = i;
        break;
            
    } // endif              
  } // end-for        
          
  // When not found, something happened
  if  (que_thread < 0) {

      // error
      enableSpecific_Result = 1;
      return;

  } // endif  
  
  // When no prior read
  if  (!last_read) {

      // error
      enableSpecific_Result = 2;
      return;

  } // endif
  
  // get past zero and make negative
  int my_thread = (que_thread + 1) * -1;
      
  // over to tymeac
  enableSpecific_Result = module.enableSpecific(O_que_name, my_thread); 

  // When not a normal return                                               
  if  (enableSpecific_Result != 4)  return;
  
  // new string array for the returned list
  String[] new_data = module.refresh(O_que_name); 
      
  // When a commumication failure
  if  (new_data == null) {

      // error
      enableSpecific_Result = 3;
      return;

  } // endif  
  
  // set new list in class
  from = new_data;    
  
  // update the JList
  S_list.setListData(new_data); 
    
} // end-method

/**
 * accessor for disable result
 * @return int
 */ 
public int getDisResult () { return disable_Result; } // end-method

/**
 * accessor for enable all result
 * @return int
 */ 
public int getEnaResult () { return enable_Result; } // end-method

/**
 * accessor for enable specific result
 * @return int
 */ 
public int getEnaSpecResult () { return enableSpecific_Result; } // end-method
/**
 * accessor for refresh result
 * @return int
 */ 
public int getRefResult () { return refresh_Result; } // end-method 
/**
 * refresh button
 * @param S_que_name JTextField - queue name
 * @param S_list JList - list of threads
 */
@SuppressWarnings("unchecked")
public void refreshButton(JTextField S_que_name,
                          javax.swing.JList S_list) {                               
    
  // set the queue name
  O_que_name = S_que_name.getText();
  
  // do refresh
	from = module.refresh(O_que_name);     
      
  // When a commumication failure
  if  (from == null) {

      // error
      refresh_Result = 0;

      // done
      return;

  } // endif      
  
  // did a read
  last_read = true; 
  
  // When the que name was not found
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
