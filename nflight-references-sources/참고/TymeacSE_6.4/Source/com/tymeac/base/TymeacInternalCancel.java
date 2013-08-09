package com.tymeac.base;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * This is the internal Processing Application Class for a syncRequest()
 *   with a callback field set in the TymeacParm Class.
 * 
 * Call back to the original caller and let them know Tymeac Server is
 *   ready to handle a cancel (cancelSyncReq()) for the prior syncRequest().
 * 
 */
public class TymeacInternalCancel {  

/**
 * Invoked method by java.lang.reflect.Method
 * 
 * @param args Object[]
 * @exception java.lang.Throwable
 */
public static void main(Object args[])
      throws  java.lang.Throwable    {

  // When not proper type, get out 
  if  (!(args[0] instanceof TymeacCancelParm)) return; 
  
  // passed object
  TymeacCancelParm parm = (TymeacCancelParm) args[0];

  try {
    // When using callback 
    if  (parm.getObject() instanceof InterruptionReady) {
              
        // do such
        cancelRMI(parm);
    
    } // endif
  } // end-try
  catch (Exception e) {} // unhandled exception   
  
} // end-method

/**
 * Call back to the client
 * @param parm
 */
private static void cancelRMI (TymeacCancelParm parm) {
  
  // tell the caller we're ready for a cancel request
  
  // get callback object
  InterruptionReady callback = (InterruptionReady)parm.getObject();
      
    try {
      // call back
      callback.ready(new CancelParm(parm.getSession(),
                                    parm.getRequest())); 
    } // end-try

    catch (Exception e) {
    } // end-catch ignore
 
} // end-method  
} // end-class
