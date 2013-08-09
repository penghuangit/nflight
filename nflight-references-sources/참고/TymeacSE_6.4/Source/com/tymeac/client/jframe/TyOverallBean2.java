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

import com.tymeac.base.*;
import com.tymeac.client.*;

/**
 * Backend object for the Overall GUI
 */
public final class TyOverallBean2 {
	
	// active table entries 
	private int refresh_top_result = 0;
	private int refresh_both_result = 0;
	
	 // where access logic lives
  private TyOverallClient module = null; 	  
	
/**
 * Constructor, not internal
 */
public TyOverallBean2 ( ) {

  // new access module
  module = new TyOverallClient();

} // end-constructor

/**
 * Constructor for internal server
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyOverallBean2(TymeacInterface TyI) {

  // new access module
  module = new TyOverallClient(TyI);

} // end-constructor
/**
 * accessor for both results
 * @return int
 */
public int getBothResult() { return refresh_both_result; } // end-method
/**
 * accessor for top only results
 * @return int
 */
public int getTopResult() { return refresh_top_result; } // end-method 

/**
 * Refresh both button
 * @param s_top JList - top part of window
 * @param s_bottom JList - bottom part of window 
 */
@SuppressWarnings("unchecked")
public void refreshBothButton(javax.swing.JList s_top,
                              javax.swing.JList s_bottom) { 

  // return object
  TyOverallObj TyO = module.refreshBoth(); 
      
  // When a commumication failure
  if  (TyO == null) {

      // error
      refresh_both_result = 0;

      // done
      return;

  } // endif   
  
  String[] obj = null;
      
  // When there are no requests possibly stalled
  if  (TyO.getStalled() == 0) {
    
      // only need two strings
      obj = new String[2];
  }
  else {
      // need full list for top list
      obj = new String[3];
      
      // set possible stalled
      obj[2] = TyO.getStalled() + " Async Requests possibly Stalled";
    
  } // endif
  
  // add all to list
  obj[0] = TyO.getSync() + " Sync Requests Active";
  obj[1] = TyO.getAsync() + " Async Requests Active";
  
  // new list
  s_top.setListData(obj); 

  // move the string array
  String myList[] = TyO.getQueues();

  // total number of entries
  int len = myList.length; 
  
  // new obj
  obj = new String[len];

  // add each item to the list
  for  (int i = 0; i < len; i++) {

    // add item
    obj[i] = myList[i]; 

  } // end-for 
  
  // new bottomlist
  s_bottom.setListData(obj);        
      
  // good return
  refresh_both_result = 1;
    
} // end-method

/**
 * Refresh top button
 * @param s_top JList - top part of window
 * @param s_bottom JList - bottom part of window 
 */ 
@SuppressWarnings("unchecked")
public void refreshTopButton( javax.swing.JList s_top,
                              javax.swing.JList s_bottom) {

  // return object
  TyOverallObj TyO = module.refreshTop();  
          
  // When a commumication failure
  if  (TyO == null) {

      // error
      refresh_top_result = 0;

      // done
      return;

  } // endif 
  
  // obj[] for list
  String[] obj = new String[1];
  obj[0] = "";  
  
  // remove all the data from the bottom list
  s_bottom.setListData(obj); 
  
  // When there are no requests possibly stalled
  if  (TyO.getStalled() == 0) {
    
      // only need two strings
      obj = new String[2];
  }
  else {
      // need full list for top list
      obj = new String[3];
      
      // set possible stalled
      obj[2] = TyO.getStalled() + " Async Requests possibly Stalled";
    
  } // endif
  
  // add others to list
  obj[0] = TyO.getSync() + " Sync Requests Active";
  obj[1] = TyO.getAsync() + " Async Requests Active";
  
  // new list
  s_top.setListData(obj); 
 
    // good return
  refresh_top_result = 1;
    
} // end-method
} // end-class
