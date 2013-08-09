package com.tymeac.demo;

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
 *
 *  Tymeac demonstration system, startup exit base class 
 * 
 */
 
public final class TyDemoMsgBase {

  private static TyDemoMsgThread mt = null;
  
/**
 *  constructor called by applications
 */
 
public TyDemoMsgBase() {

} // end-constructor  
/**
 *  constructor called at start up
 * @param msg_thread TyDemoMsgThread - data
 */
 
public TyDemoMsgBase(TyDemoMsgThread msg_thread) {

    // message thread reference
    mt = msg_thread;  

} // end-constructor
/**
 * static accessor
 * @return TyDemoMsgThread
 */
 
public static TyDemoMsgThread getField() {

    // return the thread reference  
    return mt;
    
} // end-method
/**
 * instance accessor
 * @return TyDemoMsgThread
 */
 
public TyDemoMsgThread getThreadRef() {

    // return the thread reference  
    return mt;
    
} // end-method
} // end-class
