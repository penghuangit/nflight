package com.tymeac.base;

/* 
 * Copyright (c) 2004 - 2006 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */
 
/**
 * Class for encapsulating the fields necessary to to cancel 
 *   a syncReq(). 
 *  long - Tymeac session (start up time)
 *  long - Sync Request number (Tymeac generated number)
 *  long - Cancel word (user generated number)
 */ 
public class CancelParm
         implements java.io.Serializable {

      static final long serialVersionUID = -5865128640566402822L;      
  
  // Tymeac startup time
  private long session = 0; 
  
  // request number
  private long request = 0; 
  
  // cancel word
  private long cancel_word = 0; 
  
  // return values
  public static final int SUCCESSFUL = 0;
  public static final int INVALID_SESSION_ID = 1; 
  public static final int INVALID_REQUEST_ID = 2; 
  public static final int INVALID_CANCEL_WORD = 4; 
  public static final int CONNECTION_FAILURE = 5; 
  public static final int REMOTE_EXCEPTION = 6; 
  public static final int EXCEPTION = 7;   
  
/**
 * only cancel word constructor
 */
public CancelParm(long c_word) {
  
  cancel_word = c_word;

} // end-constructor

/**
 * two arg constructor
 */
public CancelParm(long newSession, long newRequest) {

  session = newSession;
  request = newRequest;

} // end-constructor

/**
 * @return cancel word
 */
public long getCancelWord() { return cancel_word; } // end-method

/**
 * @return request
 */
public long getRequest() { return request; } // end-method

/**
 * @return session
 */
public long getSession() { return session; } // end-method
} // end-class
