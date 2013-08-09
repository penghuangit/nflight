package com.tymeac.base;

import com.tymeac.serveruser.TymeacUserVariables;

/* 
 * Copyright (c) 1998 - 2012 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * These are the fields used by the start up classes. This is really just a struct with
 *   getters and setters. 
 */
public class StartupFields {
   
  // common
  @SuppressWarnings("unused")
  private final TyBase T; 
 
  // using stand alone
  private boolean stand_alone = false;  

  // external file work
  private String dir = null; // directory
  private String filename = null; // filename
  
  // Notify function
  private String Notify_func = null;
  
  // startup exit array
  private String[] start_array = null;
  
  // *--- JINI ---*
  private String configLocation  = null;
  private String configComponent = null;
  
  // Variables Class for stand-a-lone mode
  private TymeacUserVariables variable_table = null; 
  
  // Strings used to pass config file lines when in DBMS mode
  private TyCfgFields base = null;
  
  // list of queue definitions
  private TymeacIndividualQueue[] tiq = null; 
  
  // list of function definitions
  private TymeacIndividualFunction[] tif = null; 

/**
 * Constructor
 * @param ty
 */    
protected StartupFields(TyBase ty) {
  
  T = ty; // base
  
} // end-constructor

/**
 * @return the dir
 */
protected String getDir() { return dir; } // end-method

/**
 * @param dir the dir to set
 */
protected void setDir(String dir) { this.dir = dir; } // end-method

/**
 * @return the filename
 */
protected String getFilename() { return filename; } // end-method

/**
 * @param filename the filename to set
 */
protected void setFilename(String filename) { this.filename = filename; } // end-method

/**
 * @return the notify_func
 */
protected String getNotify_func() { return Notify_func; } // end-method

/**
 * @param notify_func the notify_func to set
 */
protected void setNotify_func(String notify_func) { Notify_func = notify_func; } // end-method

/**
 * @return the stand_alone
 */
protected boolean isStand_alone() { return stand_alone; } // end-method

/**
 * @param stand_alone the stand_alone to set
 */
protected void setStand_alone(boolean stand_alone) { this.stand_alone = stand_alone; } // end-method

/**
 * @return the start_array
 */
protected String[] getStart_array() { return start_array; } // end-method

/**
 * @param start_array the start_array to set
 */
protected void setStart_array(String[] start_array) { this.start_array = start_array; } // end-method

/**
 * @return the base
 */
protected TyCfgFields getBase() { return base; } // end-method

/**
 * @param base the base to set
 */
protected void setBase(TyCfgFields base) { this.base = base; } // end-method

/**
 * @return the variable_table
 */
protected TymeacUserVariables getVariable_table() { return variable_table; } // end-method

/**
 * @param variable_table the variable_table to set
 */
protected void setVariable_table(TymeacUserVariables variable_table) { this.variable_table = variable_table; } // end-method

/**
 * @return the full_tif
 */
protected TymeacIndividualFunction[] getTif() { return tif; } // end-method

/**
 * @param full_tif the full_tif to set
 */
protected void setTif(TymeacIndividualFunction[] tif) { this.tif = tif; } // end-method

/**
 * @return the full_tiq
 */
protected TymeacIndividualQueue[] getTiq() { return tiq; } // end-method

/**
 * @param full_tiq the full_tiq to set
 */
protected void setTiq(TymeacIndividualQueue[] tiq) { this.tiq = tiq; } // end-method

/**
 * @return the configComponent
 */
public String getConfigComponent() { return configComponent; } // end-method

/**
 * @param configComponent the configComponent to set
 */
public void setConfigComponent(String configComponent) { this.configComponent = configComponent; } // end-method

/**
 * @return the configLocation
 */
public String getConfigLocation() { return configLocation; } // end-method

/**
 * @param configLocation the configLocation to set
 */
public void setConfigLocation(String configLocation) { this.configLocation = configLocation; } // end-method
  
} // end-class
