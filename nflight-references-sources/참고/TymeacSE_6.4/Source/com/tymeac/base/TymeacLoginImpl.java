package com.tymeac.base;

/* 
 * Copyright (c) 2006 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.rmi.*;

/**
 * RMI implementation of Tymeac Login interface
 * 
 */ 
public class TymeacLoginImpl
    implements TymeacLogin {
  
  // base of tymeac shared storage
  private TyBase T = null;
  
/**
 * Constructor
 * @param TyBase T 
 */
public TymeacLoginImpl (TyBase ty) {
  
  // save pointer
  T = ty;
      
} // end-constructor

/**
 * Login to the service
 * 
 * *--- add your code here ---*
 * 
 * 
 * @return TymeacInterface the real remote object
 * @param obj Object[] login parms
 * @exception java.rmi.RemoteException The exception description.
 */
public TymeacInterface login (Object[] obj )
           throws RemoteException {

  // Say we got here
  msgOut("Did a user login", 10); 
  
  // add logic before
  return T.getTi();
    
} // end-method

/**
 * Send the message to print and log 
 * @param msg
 * @param time
 */
private void msgOut(String msg, int time) {
  
  // When printing
  if  (T.getSysout() == 1)  TyBase.printMsg(msg);
  
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, time);
  
} // end-method  
} // end-class
