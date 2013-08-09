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
 * Backend object for the New Run Time Log GUI
 * @since 6.2
 */
public class TyNewRTLogBean {
	
	// indicators
  private int resetResult;
  private int stopResult;
  private int newResult;
  private int importResult;

	 // where access logic lives
  private TyNewRTLogClient module = null;
  
  // elements
  TyRunTimeElements ele;
	
/**
 * Constructor
 */
public TyNewRTLogBean ( ) {

  // new access module
  module = new TyNewRTLogClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyNewRTLogBean(TymeacInterface TyI) {

  // new access module
  module = new TyNewRTLogClient(TyI);

} // end-constructor

public int getResetResult () { return resetResult; } // end-method
public int getStopResult ()  { return stopResult; } // end-method
public int getImportResult() { return importResult; } // end-method
public int getNewResult   () { return newResult; }   // end-method

/**
 * Import button on GUI. Get the current usage
 * @param DBMS
 * @param Dir
 * @param FileName
 * @param Alt
 */
public void importButton (JTextField DBMS,
                          JTextField Dir,
                          JTextField FileName,
                          JTextField Alt) {
  
  // get current environment
  ele = module.getExisting();
  
  // When not in existence
  if  (ele == null) { 
        
      // comm error
      importResult = -1;
      return;
      
  } // endif
  
  // When logging Not available, say so
  if  (!ele.isAlive()) {
    
      importResult = SetUpLog.LogNoCurr;
      return;
  } 
  
  // When logging Not in use, say so
  if  (!ele.isInUse()) {
    
        importResult = SetUpLog.LogNotInUse;
        DBMS.setText("");
        Dir.setText("");
        FileName.setText("");
        Alt.setText("");
        return;
  } 
  
  DBMS.setText(    (ele.getDBMS() != null)?     "" + ele.getDBMS()     : "");
  Dir.setText(     (ele.getDir() != null)?      "" + ele.getDir()      : "");
  FileName.setText((ele.getFileName() != null)? "" + ele.getFileName() : "");
  Alt.setText(     (ele.getAlt() != null)?      "" + ele.getAlt()      : "");
 
  importResult = SetUpLog.LogSuccess ;
  
} // end-method

/**
 * New button on GUI
 * @param DBMS
 * @param Dir
 * @param FileName
 * @param Alt
 */
public void newButton(JTextField DBMS,
                      JTextField Dir,
                      JTextField FileName,
                      JTextField Alt) {
  
  newResult = module.newLog(DBMS.getText(), 
                            Dir.getText(), 
                            FileName.getText(), 
                            Alt.getText());
  
} // end-method

/**
 * Reset button on the GUI
 */
public void resetButton() {
  
  resetResult = module.resetLog();
  
} // end-method

/**
 * Stop button on the GUI
 */
public void stopButton() {
  
  stopResult = module.stopLog();
  
} // end-method
} // end-class
