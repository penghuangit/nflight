package com.tymeac.base;

/*
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

/**
 * This structure holds the Server sync request info so that the Server
 *   Internal Processing Application Class may pass a CancelParm Object
 *   back to the Client.
 *  
 */
public final class TymeacCancelParm {

  private final Object callback; // callback object
  private final long session_id; // session id
  private final long request_id;  // request id

/**
 * Constructor
 * @param obj
 * @param request
 * @param session
 */        
public TymeacCancelParm(Object obj,
                        long request,
                        long session) {
  
  // save parms
  callback   = obj;
  request_id = request;
  session_id = session;
  
} // end-constructor

/**
 * getter
 * @return callback
 */
public Object getObject() { return callback; } // end-method  

/**
 * getter
 * @return req id
 */
public long getRequest() { return request_id; } // end-method  

/**
 * getter
 * @return session
 */
public long getSession() { return session_id; } // end-method
} // end-class
