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

import com.tymeac.base.*;
import com.tymeac.client.*;

/**
 * Backend object for the stall GUI
 */
public class TyStalledBean2 {
	
	// indicators
	private int refresh_Result = 0;
	private int purge_Result   = 0;
	private int detail_Result  = 0;
	private int reschd_Result  = 0;
	private boolean last_read  = false;
	
	// list of stalled requests
	private String[] stalled_list = null;   

	// where access logic lives
  private TyStalledClient module = null; 

/**
 * Constructor
 */
public TyStalledBean2 ( ) {

  // new access module
  module = new TyStalledClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyStalledBean2(TymeacInterface TyI) {

	// new access module
  module = new TyStalledClient(TyI);

} // end-constructor

/**
 * Detail button
 * @param S_id_list JList - list of stalled requests
 * @param S_selected String - selected request 
 * @param S_detail_list JList list of detail queues
 */
@SuppressWarnings("unchecked")
public void detailButton( String            S_selected,
                          javax.swing.JList S_detail_list) {                              
    
  // When nothing selected
  if  (S_selected == null) {

      // error
      detail_Result = 1;

      // done
      return;

  } // endif

  // int
  int i, len, stl_nbr, found = -1, end = -1;                 
  
  // length of the string array
  len = stalled_list.length;
  
  // thread number
  stl_nbr = -1;
  
  // loop thru all the strings looking for that which was seleected
  for  (i = 0; i < len; i++) {
          
    // When a match
    if  (stalled_list[i].compareTo(S_selected) == 0)  {
            
        // set the number and get out
        stl_nbr = i;

        // get out
        break;

    } // endif              
  } // end-for        
          
  // When not found, something happened
  if  (stl_nbr < 0) {

      // error
      detail_Result = 2;

      // done
      return;

  } // endif
  
  // length of selected string
  len = S_selected.length();
  
  // id is (xxx).  find the '('.
  for  (i = 1; i < len; i++) {
    
    // When left paren
    if  (S_selected.substring(i, (i + 1)).compareTo("(") == 0) {

        // next is position
        found = i + 1;

        // get out
        break;

    } // endif
  } // end-for
  
  // id is (xxx).  find the ')'.
  for  (; i < len; i++) {
    
    // When right paren
    if  (S_selected.substring(i, (i + 1)).compareTo(")") == 0) {
            
        // save found
        end = i;

        // get out
        break;

    } // endif
  } // end-for

  // When not found, something went wrong 
  if  (end < 0) { 
          
      // error
      detail_Result = 2;

      // dnoe
      return;

  } // endif
  
  // convert to long
  long id = 0;

  try {
      id = Long.parseLong(S_selected.substring(found, end));
  } // end-try

  catch (NumberFormatException e) {      
  } // end-catch  
      
  // When not > zero, invalid number
  if  (id == 0) {

      // error
      detail_Result = 2;

      // done
      return;

  } // endif  
  
  // When no prior read
  if  (!last_read) {

      // error
      detail_Result = 4;

      // error
      return;

  } // endif
  
  // string is returned
  String[] q_list = module.detail(id);     
  
  // When null, comm error
  if  (q_list == null) {

      // error
      detail_Result = 0;

      // done
      return;

  } // endif
  
  // When shutting down
  if  (q_list[0].compareTo("N286") == 0) {

      // say so 
      detail_Result = 1;

      // done
      return;

  } // endif       
      
  // When the nothing was not found
  if  (q_list[0].compareTo("N287") == 0) {

      // say so
      detail_Result = 2;

      // done
      return;

  } // endif         
      
  // update the screen
  S_detail_list.setListData(q_list);        
 
  // good return  
  detail_Result = 3;
    
} // end-method

/**
 * accessor for delete result
 * @return int
 */ 
public int getDetResult () { return detail_Result; } // end-method

/**
 * accessor for purge result
 * @return int
 */ 
public int getPurResult () { return purge_Result; } // end-method

/**
 * accessor for refresh result
 * @return int
 */ 
public int getRefResult () { return refresh_Result; } // end-method

/**
 * accessor for re-schedule result
 * @return int
 */ 
public int getReSchdResult () { return reschd_Result; } // end-method

/**
 * Purge button
 * @param S_id_list JList - list of stalled requests
 * @param selected int[] - list of selected requests
 * @return S_detail_list JList - list of detail queues
 */
@SuppressWarnings("unchecked")
public void purgeButton(javax.swing.JList S_id_list,
                        int[]       selected,
                        javax.swing.JList S_detail_list) {                              

  // int
  int i, j, sel_len, string_len, temp_purge = 0, found = -1, end = -1;

  // long
  long id = 0;

  // result of purging
  boolean purged = false;

  // When no prior read
  if  (!last_read) {

      // say so
      purge_Result = 5; 
      
      // go back
      return;

  } // endif

  // number of selected items
  sel_len = selected.length;

  // When nothing selected
  if  (sel_len < 1) {

      // say so
      purge_Result = 2;

      // get out  
      return;

  } // endif 

  // loop thru all the items 
  for  (i = 0; i < sel_len; i++) {
          
    // get the string length
    string_len = stalled_list[selected[i]].length(); 

    // initially not found
    found = -1;

    // id is (xxx).  find the '('.
    for  (j = 1; j < string_len; j++) {

      // When a "(" 
      if  (stalled_list[selected[i]].substring(j, (j + 1)).compareTo("(") == 0) {
        
          // save position  
          found = j + 1;

          // get out of loop  
          break;

      } // endif
    } // end-for

    // id is (xxx).  find the ')'.
    for  (; j < string_len; j++) {

      // When a ")" 
      if  (stalled_list[selected[i]].substring(j, (j + 1)).compareTo(")") == 0) {
        
          // save position  
          end = j;

          // get out of loop  
          break;

      } // endif
    } // end-for

    // When found 
    if  (found >= 0) {

        // convert to long
        try {
          id = Long.parseLong(stalled_list[selected[i]].substring(found, end));
        } // end-try

        catch (NumberFormatException e) {  
        } // end-catch
  
        // When > zero
        if  (id  > 0) {

						// try purge
			 			temp_purge = module.purge(id);                       

            // When purged 
            if  (temp_purge == 2) {

                // say we did one
                purged = true;
                
            } // endif
        } // endif          
    } // endif      
  } // end-for

  // When purged
  purge_Result = (purged)? 2 : 3;

  // refresh now
  stalled_list = module.refresh();      
      
  // When a commumication failure
  if  (stalled_list == null) {

      // say so
      purge_Result = 0;

      // done
      return;

  } // endif  
  
  // wipe out the detail list
  // for list
  String[] obj = new String[1];
            
  // put this message in the list
  obj[0] = "";   
  
  S_detail_list.setListData(obj);      
      
  // When nothing was found
  if  (stalled_list[0].compareTo("N287") == 0) {

      // say so
      purge_Result = 4;
      
      // wipe out the list
      S_id_list.setListData(obj);  

      // done
      return;

  } // endif
  
  // new list
  S_id_list.setListData(stalled_list);
    
} // end-method 

/**
 * Refresh button
 * @param S_id_list JList - list of stalled requests
 * @return S_detail_list JList - list of detail queues
 */
@SuppressWarnings("unchecked")
public void refreshButton(javax.swing.JList S_id_list,
                          javax.swing.JList S_detail_list) {    
    
  // did a read
  last_read = true; 

	// refresh
	stalled_list = module.refresh(); 
  
  // wipe out the detail list
  String[] obj = new String[1];
            
  // put this message in the lists
  obj[0] = "";   
  S_detail_list.setListData(obj);                                   

  // When a commumication failure
  if  (stalled_list == null) {

      // say so
      refresh_Result = 0;

      // done
      return;

  } // endif  
      
  // When nothing was not found
  if  (stalled_list[0].compareTo("N287") == 0) {

      // say so
      refresh_Result = 2;
      
      // null list
      S_id_list.setListData(obj);  

      // done
      return;

  } // endif       
  
  // new list    
  S_id_list.setListData(stalled_list); 
  
  // good return
  refresh_Result = 3;
    
} // end-method

/**
 * Reschedule Button
 * @param S_id_list JList - list of stalled requests
 * @param S_selected String - selected entry
 * @return S_detail_list JList - list of detail queues
 */
@SuppressWarnings("unchecked")
public void reSchdButton( javax.swing.JList   S_id_list,
                          String              S_selected,
                          javax.swing.JList   S_detail_list) {                              
    
  // When not found, something happened
  if  (S_selected == null) {

      // say so
      reschd_Result = 6;

      // done
      return;

  } // endif

  // int
  int i, len, stl_nbr, found = -1, end = -1;
      
  // length of the string array
  len = stalled_list.length;
  
  // thread number
  stl_nbr = -1;
  
  // loop thru all the strings looking for that which was seleected
  for  (i = 0; i < len; i++) {
          
    // When a match
    if  (stalled_list[i].compareTo(S_selected) == 0)  {
            
        // set the number and get out
        stl_nbr = i;

        // get out
        break;

    } // endif              
  } // end-for        
          
  // When not found, something happened
  if  (stl_nbr < 0) {

      // say so
      reschd_Result = 6;

      // done
      return;

  } // endif
  
  // length of selected string
  len = S_selected.length();
  
  // id is (xxx).  find the '('.
  for  (i = 1; i < len; i++) {
    
    // When left paren
    if  (S_selected.substring(i, (i + 1)).compareTo("(") == 0) {

        // set found and get out                  
        found = i + 1;

        // get out
        break;

    } // endif
  } // end-for

  // id is (xxx).  find the ')'.
  for  (; i < len; i++) {
    
    // When right paren
    if  (S_selected.substring(i, (i + 1)).compareTo(")") == 0) {
  
        // set end and get out
        end = i;

        // get out
        break;

    } // endif
  } // end-for
  
  // When not found, something went wrong 
  if  (end < 0) { 

      // say so           
      reschd_Result = 7;

      // done
      return;

  } // endif
  
  // convert to long
  long id = 0;

  try {
      id = Long.parseLong(S_selected.substring(found, end));
  } // end-try

  catch (NumberFormatException e) {      
  } // end-catch    
      
  // When not > zero, invalid number
  if  (id == 0) {

      // say so
      reschd_Result = 8;

      // done
      return;

  } // endif    
  
  // When no prior read
  if  (!last_read) {

      // say so
      reschd_Result = 9;

      // done
      return;

  } // endif

	// try reschedule
  reschd_Result = module.reSchd(id);  

  // When not a good reschedule, done
  if  (reschd_Result > 0)  return;

	// refresh list
  stalled_list = module.refresh();     

  // When a commumication failure
  if  (stalled_list == null) {

      // say so
      reschd_Result = 10;

      // done
      return;

  } // endif  
             
  // wipe out the detail list
  String[] obj = new String[1];
            
  // put this message in the lists
  obj[0] = "";   
  S_detail_list.setListData(obj);     
  
  // When nothing was found
  if  (stalled_list[0].compareTo("N287") == 0) {

      // say so
      reschd_Result = 11;
      
      // wipe out top list
      S_id_list.setListData(obj); 

      // done
      return;

  } // endif
  
  // new list
  S_id_list.setListData(stalled_list);   
    
} // end-method
} // end-class
