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

import java.text.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * The stall array header. This is where all those stalled async request go. This Class
 *   holds the actual array and methods to access the details.
 */  
public final class StallHeader {
  
  // Tymeac base storage 
  private final TyBase  T; 

  // detail array of stalled objects
  private final ConcurrentLinkedQueue<StallDetail> details; 

/**
 * Constructor
 * @param Ty TyBase - base of Tymeac storage
 */    
protected StallHeader( TyBase Ty) {
   
  T = Ty;    // Tymeac base storage
     
  // get the array of details
  details = new ConcurrentLinkedQueue<StallDetail>();          
         
} // end-constructor

/**
 * Create a detail entry
 * @param at_name long - gen name
 * @param why int - reason code
 * @return boolean
 */ 
protected boolean addEntry (long at_name, int why) { 
    
  // create 
  StallDetail him = new StallDetail(T, at_name, why);
  
  // When already there, don't put it in again
  if  (matchRequest(at_name)) return false;
  
  // When put in stalled queue ok, say so
  return (details.offer(him))? true : false; 
      
} // end-method

/**
 * Diagnose - print used entries
 */ 
protected void dsplyUsedEntries () {    
    
  int j = 0, k = 0, failed, checked, status;
  long name;
       
  //  loop thru the all the details
  for (StallDetail him : details) {
      
    // count total
    k++; 
    
    // When busy, increment
    if  (him.isBusy()) j++;
    
  } // end-for           
  
  TyBase.printMsg("Total ALL Slots in use = " + k);
  TyBase.printMsg("Stall Slots in use at first scan = " + j);

  // when none are working
  if  (k == 0) {

      TyBase.printMsg("Stall, none are in use, finished."); 
      return;

  } // endif  
  
  //  loop thru the all the details
  for (StallDetail him : details) {      
                
    checked  = him.getChecked();
    name     = him.getAtName();
    failed   = him.getFailedReason(); 
    status   = him.getStatus();
                     
    TyBase.printMsg("NextDetail");

    TyBase.printMsg("   " 
                    + "Status="
                    + status
                    + " Uni="  
                    + name 
                    + " Failed="  
                    + failed 
                    + " Checked=" 
                    + checked);
    
  } // end-for
    
} // end-method

/**
 * Count all the entries that are busy
 * @return int
 */
protected int getBusy () {
    
  int count = 0;
      
  //  loop thru the all the details
  for (StallDetail him : details) {
      
    // When busy, increment
    if  (him.isBusy()) count++;
    
  } // end-for            
  
  return count;
    
} // end-method

/**
 * Get the list of used entries formatted for the Stall Array display
 * @return String[]
 */ 
protected String[] getList ( ) { 
  
  //  return value
  String[] list  = null;

  // When none are used, return the not here string   
  if  (getBusy() == 0) {

      // new single string
      list = new String[1];

      // say not found
      list[0] = "N287"; 

      // pass back
      return list;

  } // endif
  
  // get all the details
  Object[] obj = details.toArray();
  
  // new array of only busy details
  StallDetail[] s_detail = new StallDetail[obj.length];
  
  // busy details
  int count = 0;
  
  // count busy within new array
  for (int i = 0; i < obj.length; i++) {  
        
    // move each detail
    s_detail[i] = (StallDetail) obj[i];
    
    // When not busy
    if  (!s_detail[i].isBusy()) {
      
        // not used
        s_detail[i] = null;
    }
    else {
        // increment
        count++;
        
    } // endif    
  } // end-for  
    
  long entered = 0, at_name = 0;
  int  why = 0, count2 = count;
  String fId;
  
  // request detail
  RequestDetail r_detail = null; 
      
  // get a string array
  list = new String[count];
  
  // get date format
  DateFormat DF = DateFormat.getDateTimeInstance( DateFormat.MEDIUM,
                                                  DateFormat.LONG);
  
  // the zone is wrong so get the zone at this location
  java.util.TimeZone zone = java.util.TimeZone.getDefault();
  
  // adjust the date format zone to: here
  DF.setTimeZone(zone);                                           
                                                              
  // date instance                                                            
  java.util.Date D;
     
  //loop thru the all the retrieved objects
  for (int i = 0; i < count; i++) {  
    
    // When still in use
    if  (s_detail[i].isBusy()) {
              
        // get the data from the stall detail
        at_name = s_detail[i].getAtName();
        why     = s_detail[i].getFailedReason();
        
        // get the request detail
        r_detail = T.getRequest_tbl().getAsyncDetail(at_name);
          
        // When still alive
        if  ((r_detail != null) &&
             (r_detail.isWorking())) {
            
            // get the data from the async request 
            fId     = r_detail.getFunction().getName();
            entered = r_detail.getEntered();
            
            // get a new date and time obj
            D = new java.util.Date(entered);
          
            // form the string in the list
            list[i] =   + why
                        + "    "
                        + "("
                        + at_name
                        + ")  "
                        + DF.format(D)
                        + ",   "
                        + fId;      
        }
        else {
            // form the dummy string in the list
            list[i] =  "**- deleted -**";
            
            // purge this stall entry 
            s_detail[i].setFree();
            
            // less
            count2--;
            
        } // endif 
    }
    else {        
        // form the dummy string in the list
        list[i] =  "**- deleted -**";
        
        // less
        count2--;
        
    } // endif
  } // end-for 
  
  // When none are used, return the not here string   
  if  (count2 == 0) {

      // new single string
      list = new String[1];

      // say not found
      list[0] = "N287"; 
      
  } // endif
      
  // give it back
  return list;    
          
} // end-method

/**
 * Find a matching request in the detail entries
 * @param req long - async generated name
 * @return matched detail reference
 */
protected StallDetail getMatch (long req) {   
   
  //  loop thru the all the details
  for (StallDetail him : details) {
    
    // When a match, give back reference
    if  ((him.getAtName() == req) &&
         (him.isBusy()))   return him;
    
  } // end-for 
  
 // not here
 return null;
   
} // end-method

/**
 * Get all the used stall details 
 * @return ArrayList
 */
protected ArrayList getNextUsed () {  
    
  // new list
  ArrayList<StallDetail> temp_stall = new ArrayList<StallDetail>();
  
  //  loop thru the all the details
  for (StallDetail him : details) {
    
    // When a match, give back reference
    if  (him.isBusy()) temp_stall.add(him);
    
  } // end-for    
      
  // what we found
  return temp_stall;
    
} // end-method

/**
 * Find a matching detail using the async generated name
 * @param req long - request generated name
 * @return boolean
 */
protected boolean matchRequest (long req) {
  
  // When a match
  return (getMatch(req) != null)? true : false;
   
} // end-method

/**
 * Purge a detail name using the async generated name
 * @param req long - gen name
 */
protected void purgeATname (long req) {
  
  // Request detail 
  RequestDetail r_detail;
  
  // Stall detail
  StallDetail s_detail = getMatch(req);
  
  // When not there or not busy, done
  if  ((s_detail == null) ||
       (!s_detail.isBusy())) return;
                        
  // get the request detail
  r_detail = T.getRequest_tbl().getAsyncDetail(req);
  
  // When there and working, free the request entry
  if  ((r_detail != null) &&
       (r_detail.isWorking())) r_detail.purgeEntry(req);         
                  
  // free the stall entry         
  s_detail.setFree();
    
} // end-method

/**
 * Remove a detail entry 
 * @param him StallDetail
 */
protected void removeDetail (StallDetail him) {              
                    
  // remove this entry
  details.remove(him);
   
} // end-method
} // end-class
