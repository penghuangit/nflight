package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2013 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * This class reformates the return objects from both a
 *   syncRequest() and asyncRequest()
 *   into the components internal to the return Object[]. 
 *   This should have been done in the beginning 
 *   but better late than never.
 */
public class TymeacReturn {
  
  private int return_code  = 0;
  private long session_id  = 0;
  private long uid         = 0;
  private String ty_msg    = null;
  private Object user_data = null;

/**
 * Format the return message from a Tymeac [a]syncRequest()
 * @param passed back from Tymeac async call
 * @return the formatted return
 */
public static TymeacReturn formatCallReturn (Object[] back) {
  
  // When no back
  if  (back == null)         
      return new TymeacReturn(9001); 

  // When first object is not a string
  if  ((back[0] == null) ||
       (!(back[0] instanceof String)))     
      return new TymeacReturn(9002); 

  String r = (String) back[0];

  // When invalid return value 
  if  (r.length() < 14)     
      return new TymeacReturn(9003, r); 
  
  // When returned value not 0000
  if  (r.substring(10, 14).compareTo("0000") != 0) { 
    
      int val = -1;
      
      try {
        val = Integer.parseInt(r.substring(10, 14));
      }
      catch (NumberFormatException ignore) {}       
    
      // return code other than zero
      return new TymeacReturn(val, r);  
      
  } // endif
  
  // When an async request, go there
  if  (r.substring(7, 9).compareTo("AR") == 0) return checkBackAsync(r);
    
  // When invalid number of Object[]
  if  (back.length < 2)     
      return new TymeacReturn(9004, r);  
  
  // zero return code + original string message and user data for sync
  return new TymeacReturn(r, back[1]);
  
} // end-method

/**
 * Check the results of calling the Tymeac Server with an asyncRequest().
 * The return is an array of objects.
 * only one object is the result of tymeac async scheduling:
 *   Tymeac AR(nnnn)[session id, request id]
 *   Tymeac is a constant
 *   AR is for Async Request 
 *   the nnnn is the return code. See /TymeacImpl.java for non-zero. 
 *   in brackets are the session/request id's
 *   
 * @param back came back from tymeac call
 */
private static TymeacReturn checkBackAsync (String one) {  
   
  // getting the session and uid from the returned String:
  //   Tymeac AR(nnnn)[session id, request id]
  String left = "[";
  String comma = ",";
  String right = "]";
  
  int found1 = 0, found2 = 0, found3 = 0, found4 = 0;
  int i, max = one.length();
  
  // find left bracket
  for (i = 15; i < max; i++)   {  
    
    if  (one.substring(i, i + 1).compareTo(left) == 0) { 
      
        found1 = i;
        break;
        
    } // endif
  } // end-for
  
  // When not found, format error
  if  (found1 == 0) return new TymeacReturn(9011, one);    
  
  // find comma
  for (i = found1 + 1; i < max; i++) {    
  
    if  (one.substring(i, i + 1).compareTo(comma) == 0) { 
      
      found2 = i;
      break;
      
    } // endif    
  } // end-for
  
  // When not found, format error
  if  (found2 == 0) return new TymeacReturn(9012, one);     
  
  long session;
  
  try {
    session = Long.parseLong(one.substring(found1 + 1, found2));
  }
  catch (NumberFormatException e) {
        
    // error
     return new TymeacReturn(9013, one);       
        
  } // end-catch
    
  // find non-space
  for (i = found2 + 1; i < max; i++)  {  
    
    if  (one.substring(i, i + 1).compareTo(" ") != 0) {      
      
        found3 = i;
        break;

    } // endif
  } // end-for
  
  // When not found, format error
  if  (found3 == 0) return new TymeacReturn(9014, one);    
  
  // find right bracket
  for (i = found3 + 1; i < max; i++) {     
    
    if  (one.substring(i, i + 1).compareTo(right) == 0) { 
      
        found4 = i;
        break;
        
    } // endif    
  } // end-for
  
  // When not found, format error
  if  (found4 == 0) return new TymeacReturn(9015, one); 
  
  long uid;
  
  try {
    uid = Long.parseLong(one.substring(found3, found4));
  }
  catch (NumberFormatException e) {
        
    // error
    return new TymeacReturn(9016, one);       
        
  } // end-catch
    
  // zero return code + original string message + session and async unique id 
  return new TymeacReturn(one, session, uid);  
  
} // end-method

/**
 * Contructor for Async return
 * @param rc
 * @param session
 * @param uid
 */
private TymeacReturn (String msg, long session, long uid) {
  
  session_id  = session;
  this.uid    = uid;  
  this.ty_msg = msg;
  
} // end-constructor
  
/**
 * Contructor error return
 * @param rc
 */
private TymeacReturn (int rc) {
  
  return_code = rc;
  
} // end-constructor

/**
 * Contructor2 error return
 * @param rc
 */
private TymeacReturn (int rc, String msg) {
  
  return_code = rc;
  this.ty_msg = msg;
  
} // end-constructor

/**
 * Contructor for Sync return
 * @param rc
 * @param Object, back from call
 */
private TymeacReturn (String msg, Object user_data ) {
  
  this.user_data = user_data;
  this.ty_msg = msg;
    
} // end-constructor

public int    getReturnCode() { return return_code;  }
public long   getSessionId()  { return session_id;   }
public long   getRequestId()  { return uid;  }
public String getTyMessage()  { return ty_msg; }
public Object getUserData()   { return user_data; }

} // end-class
