package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * This is just the list of all Tymeac Queues. Built at start-up by adding one entry at a time, 
 * it never changes.
 */
public final class AreaList {

  private final int nbr_entries; // number of Area entries  
  private final AreaBase[] details;  // detail entries
  
  private int curr_sub;    // current location for building only   
   
/**
 * Constructor
 * @param numb int - total number of queues
 */
protected AreaList(int numb) {
  
  nbr_entries = numb;   // number of queues in all
  curr_sub    = 0;      // current location is first 
  details = new AreaBase[nbr_entries]; // new details   
  
} // end-constructor

/**
 * Add a new entry to the array
 * @param A AreaHeader The area to add
 */
protected void addArea(AreaBase A) {
    
  // add the new area   
  details[curr_sub] = A;
  
  // next position        
  curr_sub++;       
   
} // end-method

/**
 * Accessor for an AreaBase using a subscript input
 * @return AreaHeader
 * @param que int
 */ 
protected AreaBase getArea (int que) {
  
  // When good que number, return Area  
  if  ((que > -1) && 
       (que < nbr_entries)) return details[que];
  
  // invalid queue number
  return null;
  
} // end-method 

/**
 * Find a Queue by its internal name
 * @param i_name - input name
 * @return AreaBase
 */       
protected AreaBase getAreaBase(String i_name) {   
    
  // loop thru all the details looking for a match
  for (int i = 0; i < nbr_entries; i++) {
    
    // When its the one, return object
    if  (details[i].getName().equals(i_name)) return details[i];
    
  } // end-for      

  // not found
  return null;      
  
} // end-method 

/**
 * Accessor for number of entries
 * @return int
 */ 
protected int getNbrEntries ( ) { return nbr_entries; } // end-method
} // end-class
