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
 * Backend object for the New Run Time Statistics GUI
 * @since 6.2
 */
public class TyNewRTStatsBean {
	
	// indicators
  private int resetResult;
  private int stopResult;
  private int newResult;
  private int importResult;

	 // where access stats lives
  private TyNewRTStatsClient module = null;
  
  // elements
  TyRunTimeElements ele;
	
/**
 * Constructor
 */
public TyNewRTStatsBean ( ) {

  // new access module
  module = new TyNewRTStatsClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyNewRTStatsBean(TymeacInterface TyI) {

  // new access module
  module = new TyNewRTStatsClient(TyI);

} // end-constructor

public int getResetResult () { return resetResult; } // end-method
public int getStopResult  () { return stopResult; } // end-method
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
  
  // When stats Not in use, say so
  if  (!ele.isAlive()) {
    
      importResult = SetUpStats.StatsNoCurr;
      return;
  } 
  
  // When not in use
  if  (!ele.isInUse()) { 
        
      // something is wrong
      importResult = SetUpStats.StatsNotInUse;
      DBMS.setText("");
      Dir.setText("");
      FileName.setText("");
      Alt.setText("");
      return;
      
  } // endif
  
  DBMS.setText(    (ele.getDBMS() != null)?     "" + ele.getDBMS()     : "");
  Dir.setText(     (ele.getDir() != null)?      "" + ele.getDir()      : "");
  FileName.setText((ele.getFileName() != null)? "" + ele.getFileName() : "");
  Alt.setText(     (ele.getAlt() != null)?      "" + ele.getAlt()      : "");
 
  importResult = SetUpStats.StatsSuccess ;
  
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
  
  newResult = module.newStats(DBMS.getText(), 
                              Dir.getText(), 
                              FileName.getText(), 
                              Alt.getText());
  
} // end-method

/**
 * Reset button on the GUI
 */
public void resetButton() {
  
  resetResult = module.resetStats();
  
} // end-method

/**
 * Stop button on the GUI
 */
public void stopButton() {
  
  stopResult = module.stopStats();
  
} // end-method
} // end-class
