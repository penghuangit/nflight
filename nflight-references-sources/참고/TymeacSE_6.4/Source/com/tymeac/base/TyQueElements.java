package com.tymeac.base;

/* 
 * Copyright (c) 2002 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Used for the Que Data GUI
 * 
 */ 
public final class TyQueElements 
      implements java.io.Serializable  {
  
   static final long serialVersionUID = 6929217929340504999L;
  
  public String que_name   = null;   // name of the queue
  public int    wait_time  = 0;      // max wait time
  public int    kill_time  = 0;      // max thread life
  public int    entries    = 0;      // number of WL entries
  public int    th_entries = 0;      // number of WL entries for thresholds
  public float  overall    = 0.0F;   // overall %
  public float  individ    = 0.0F;   // individual %
  public float  factor     = 0.0F;   // weighted factor
  public float  average    = 0.0F;   // weighted average
  public int    timeout    = 0;      // application timeout
  
} // end-class
