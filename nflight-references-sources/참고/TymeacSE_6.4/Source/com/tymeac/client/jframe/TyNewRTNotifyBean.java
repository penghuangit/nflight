package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import javax.swing.*;
import com.tymeac.base.*;
import com.tymeac.client.*;

/**
 * Backend object for the New Run Time Notify GUI
 * @since 6.2
 */
public class TyNewRTNotifyBean {
	
	// indicators
  private int resetResult;
  private int stopResult;
  private int newResult;
  private int importResult;

	 // where access logic lives
  private TyNewRTNotifyClient module = null;
  
  // elements
  TyRunTimeElements ele;
	
/**
 * Constructor
 */
public TyNewRTNotifyBean ( ) {

  // new access module
  module = new TyNewRTNotifyClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyNewRTNotifyBean(TymeacInterface TyI) {

  // new access module
  module = new TyNewRTNotifyClient(TyI);

} // end-constructor

public int getResetResult () { return resetResult; } // end-method
public int getStopResult  () { return stopResult; } // end-method
public int getImportResult() { return importResult; } // end-method
public int getNewResult   () { return newResult; }   // end-method

/**
 * Import button on GUI. Get the current usage
 * @param Notify Function
 */
public void importButton (JTextField NotifyFunc) {
  
  // get current environment
  ele = module.getExisting();
  
  // When not in existence
  if  (ele == null) { 
        
      // comm error
      importResult = -1;
      return;
      
  } // endif
  
  // When not in use
  if  (!ele.isInUse()) { 
        
      // something is wrong
      importResult = SetUpNotify.NotifyNotInUse;
      NotifyFunc.setText(""); // blank field
      return;
      
  } // endif
  
  // When logging Not in use, say so
  if  (!ele.isAlive()) {
    
      importResult = SetUpNotify.NotifyNoCurr;      
      NotifyFunc.setText(""); // blank field
      return;
  } 
  
  NotifyFunc.setText((ele.getFuncName() != null)?  "" + ele.getFuncName() : "");
 
  importResult = SetUpNotify.NotifySuccess ;
  
} // end-method

/**
 * New button on GUI
 * @param Notify Function
 */
public void newButton(JTextField NotifyFunc) {
  
  newResult = module.newNotify(NotifyFunc.getText());
  
} // end-method

/**
 * Reset button on the GUI
 */
public void resetButton() {
  
  resetResult = module.resetNotify();
  
} // end-method

/**
 * Stop button on the GUI
 */
public void stopButton() {
  
  stopResult = module.stopNotify();
  
} // end-method
} // end-class
