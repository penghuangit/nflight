package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2011 Cooperative Software Systems, Inc. 
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
 * Backend object Alter Server Options GUI
 */
public class TyAltSvrBean {
	
	// indicators
	private int callResult = 0;	

	// where access logic lives
  private TyAltSvrClient module = null; 
	
/**
 * Constructor
 */
public TyAltSvrBean ( ) {

  // new access module
  module = new TyAltSvrClient();

} // end-constructor

/**
 * Constructor
 * @param TyI com.tymeac.base.TymeacInterface
 */
public TyAltSvrBean(TymeacInterface TyI) {

	// new access module
  module = new TyAltSvrClient(TyI);

} // end-constructor

/**
 * the result of calling tymeac
 * @return
 */
public int getCallResult() { return callResult; } // end-method

/**
 * import button
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param sysexit JCheckBox - sysexit on termination
 */ 
public void importButton (
                    JTextField monitor,
                    JTextField activator,
                    JCheckBox sys_exit) {       
  
	// over to tymeac		
	TyAltSvrElements result = module.importElements(null);          
      
  // When a commumication failure
  if  (result == null) {

      // error
      callResult = 1;

      // done
      return;

  } // endif  
  
  // set system exit data  
  if  (result.getShut()){
      sys_exit.setSelected(true);
  }
  else {
    sys_exit.setSelected(false);
  } // endif
  
  // set monitor data
  monitor.setText("" + result.getMonInterval());

  // set activation data
  activator.setText("" + result.getInactivate());
  
  // good return
  callResult = 0;
    
} // end-method

/**
 * import button
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param sysexit JCheckBox - sysexit on termination
 */ 
public void alterButton (
                    JTextField monitor,
                    JTextField activator,
                    JCheckBox sys_exit) {       
  
  boolean sys;
  int     mon = -1, act = -1;
  
  // When checked
  if  (sys_exit.getAccessibleContext().getAccessibleStateSet().contains(
            javax.accessibility.AccessibleState.CHECKED)) {
  
    // say yes
    sys = true;
  }
  else {  
    // say no
    sys = false;

  } // endif

  // try to convert 
  try {
    mon = Integer.parseInt(monitor.getText());
  } catch (NumberFormatException e) {}
  
  // When invalid monitor interval
  if  (mon < 1) {
    
      callResult = 2;
      return;
      
  } // endif
  
  // try to convert 
  try {
    act = Integer.parseInt(activator.getText());
  } catch (NumberFormatException e) {}
  
  // When invalid act interval
  if  (act < 0) {
    
      callResult = 3;
      return;
      
  } // endif
  
  // over to tymeac   
  TyAltSvrElements result = module.alterElements(new TyAltSvrElements(sys, mon, act));          
      
  // When a commumication failure
  if  (result == null) {

      // error
      callResult = 1;

      // done
      return;

  } // endif  
  
  // set system exit data  
  if  (result.getShut()){
      sys_exit.setSelected(true);
  }
  else {
    sys_exit.setSelected(false);
  } // endif
  
  // set monitor data
  monitor.setText("" + result.getMonInterval());

  // set activation data
  activator.setText("" + result.getInactivate());
  
  // good return
  callResult = 0;
    
} // end-method
} // end-class
