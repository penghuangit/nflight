package com.tymeac.base;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * This class is the return object for an overall status request.  
 */
public final class TyOverallObj 
      implements java.io.Serializable  {
  
  static final long serialVersionUID = 5669040491873986634L;
  
  private int nbr_sync    = 0;  // number of sync requsts busy
  private int nbr_async   = 0;  // number of async requsts busy
  private int nbr_stalled = 0;  // number of async requests stalled
  private String[] queues = null; // queues with attendant threads

/**
 * Constructor
 * @param sync int
 * @param async int
 * @param stalled int
 * @param the_queues java.lang.String[]
 */
public TyOverallObj(int sync, int async, int stalled, String[] the_queues) {

  // load classs
  nbr_sync    = sync;
  nbr_async   = async;
  nbr_stalled = stalled;

  // When there are queues, load the queues field
  if  (the_queues != null)  queues = the_queues;

} // end-constructor

/**
 * Return the number of active async requests.
 * @return int
 */
public int getAsync() { return nbr_async; } // end-method

/**
 * Return the number of async requests stalled.
 * @return int
 */
public String[] getQueues() { return queues; } // end-method

/**
 * Return the number of async requests stalled.
 * @return int
 */
public int getStalled() { return nbr_stalled; } // end-method

/**
 * Return the number of active sync requests.
 * @return int
 */
public int getSync() { return nbr_sync; } // end-method
} // end-class
