package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2012 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.util.concurrent.atomic.*; 

/**
 * Generated numbers for both a sync and async request
 *
 */
public final class GenTable {

   @SuppressWarnings("unused")
  private final TyBase T;              // Tymeac base storage
   private AtomicLong next_sync;  // next sync request number   
   private AtomicLong next_async; // next async request number  

/**
 * Constructor
 * @param Ty TyBase - base of Tymeac Storage
 */
protected GenTable(TyBase Ty) {
  
  T           = Ty; // Tymeac base storage   
  next_sync   = new AtomicLong(1);  // initialize starting at 1                    
  next_async  = new AtomicLong(1);  // initialize starting at 1
            
} // end-constructor

/**
 * Accessor for current async number
 * @return long
 */
protected long getAsync() { return next_async.get(); } // end-method

/**
 * Accessor for current sync request number
 * @return long
 */
protected long getSync() { return next_sync.get(); } // end-method 

/**
 * Set the next async number
 * @return long
 */
protected long nextAsync() { return next_async.getAndIncrement(); } // end-method 

/**
 * Set the next sync number
 * @return long
 */    
protected long nextSync() { return next_sync.getAndIncrement(); } // end-method          

} // end-class
