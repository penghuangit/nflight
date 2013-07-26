package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.base.*;
/**
 * This is the Bean for the TyMenu Frame
 */
public class TyMenuBean2 {

	// use internal server
	private TymeacInterface Ti = null;
	
/**
 * Constructor
 */
public TyMenuBean2 ( ) {

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyMenuBean2(TymeacInterface TyI) {

	Ti = TyI;

} // end-constructor

/**
 * Make the GUI visable
 */
public void doAltSvr() {
  
  // When not using the internal server
  if  (Ti == null) { 

      // visual
      new TyAltSvr().setVisible(true);
  }
  else {
      // visual
      new TyAltSvr(Ti).setVisible(true);

  } // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doCfg() {  

	// When not using the internal server, make visable
	if  (Ti == null) new TyCfg().setVisible(true);
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doFuncData() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyFuncData().setVisible(true);
	}
	else {
			// visual
  		new TyFuncData(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doFuncMaint() {

	// When not using the internal server
	if  (Ti == null) new TyFuncMaint().setVisible(true);
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doNewCopy() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
      new TyNewCopy().setVisible(true);
	}
	else {
			// visual
  		new TyNewCopy(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doOverall() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyOverall().setVisible(true);
	}
	else {
			// visual
  		new TyOverall(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doQueData() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyQueData().setVisible(true);
	}
	else {
			// visual
  		new TyQueData(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doQueMaint() {

	// When not using the internal server
	if  (Ti == null) 	new TyQueMaint().setVisible(true);
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doQueThd() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyQueThd().setVisible(true);
	}
	else {
			// visual
  		new TyQueThd(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doReqStatus() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyReqStatus().setVisible(true);
	}
	else {
			// visual
  		new TyReqStatus(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doRTNotify() {
  
  // When not using the internal server
  if  (Ti == null) { 

      // visual
      new TyNewRTNotify().setVisible(true);
  }
  else {
      // visual
      new TyNewRTNotify(Ti).setVisible(true);

  } // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doRTLog() {
  
  // When not using the internal server
  if  (Ti == null) { 

      // visual
      new TyNewRTLog().setVisible(true);
  }
  else {
      // visual
      new TyNewRTLog(Ti).setVisible(true);

  } // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doRTStats() {
  
  // When not using the internal server
  if  (Ti == null) { 

      // visual
      new TyNewRTStats().setVisible(true);
  }
  else {
      // visual
      new TyNewRTStats(Ti).setVisible(true);

  } // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doShutdown() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyShutdown().setVisible(true);
	}
	else {
			// visual
  		new TyShutdown(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doStalled() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyStalled().setVisible(true); 
	}
	else {
			// visual
  		new TyStalled(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doStats() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyStats().setVisible(true);
	}
	else {
			// visual
  		new TyStats(Ti).setVisible(true);

	} // endif
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doTyVariables() {

	// When not using the internal server
	if  (Ti == null) new TyVariables().setVisible(true);
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doUserFunctions() {

	// When not using the internal server
	if  (Ti == null) new TyUserFunctions().setVisible(true);
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doUserQueues() {

	// When not using the internal server
	if  (Ti == null) 	new TyUserQueues().setVisible(true);
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doUserVariables() {

	// When not using the internal server
	if  (Ti == null) 	new TyUserVariables().setVisible(true);
    
} // end-Method

/**
 * Make the GUI visable
 */
public void doWlData() {
  
	// When not using the internal server
	if  (Ti == null) { 

		  // visual
  		new TyWlData().setVisible(true);
	}
	else {
			// visual
  		new TyWlData(Ti).setVisible(true);

	} // endif
    
} // end-Method
} // end-class
