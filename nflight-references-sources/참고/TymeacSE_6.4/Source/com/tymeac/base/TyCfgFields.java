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
 * For passing string from the config file fetch method to the 
 *   start up main method  
 * 
 */
public final class TyCfgFields {
  
  public String dbms_URL          = null;
  public String dbms_DataManager  = null;
  public String dbms_UserName     = null;
  public String dbms_PassWord     = null; 
  public String dbms_FuncTable    = null;
  public String dbms_ListTable    = null;
  public String dbms_QueTable     = null;
  public String dbms_Log          = null;
  public String dbms_Stats        = null;
  public String func_Notify       = null;
  public String sys_exit          = null;
  public String mon_Interval      = null;
  public String act_Interval      = null;
  public String[] start_classes   = null;
  public String[] start_functions = null;
  public String[] shut1_classes   = null;
  public String[] shut2_classes   = null;
  public String statsDir          = null;
  public String statsFile          = null;
  public String logDir            = null;
  public String logFile           = null;
  public String altStatsClass     = null;
  public String altLogClass       = null;
  public String loginContext      = null;

  public String found = null;

} // end-class
