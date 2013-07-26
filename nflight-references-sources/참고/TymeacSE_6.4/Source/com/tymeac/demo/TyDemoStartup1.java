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
 *  Tymeac demonstration system, startup exit entry
 * 
 */
 
public class TyDemoStartup1 {

  
/**
 *  constructor
 */
 
public TyDemoStartup1() {

  // new message thread with interval of 1 hour
  TyDemoMsgThread mt = new TyDemoMsgThread(3600000);

  // is a daemon
  mt.setDaemon(true);

  // start it up
  mt.start(); 
  
} // end-constructor
} // end-class
