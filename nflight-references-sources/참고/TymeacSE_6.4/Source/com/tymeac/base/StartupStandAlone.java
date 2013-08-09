package com.tymeac.base;

import java.util.HashSet;
import com.tymeac.serveruser.*;

/* 
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Start up using Classes for the variables
 */
public final class StartupStandAlone {
  
  // common
  private final TyBase T;
  private final Startup base;
  
  // General fields used by the other start up classes
  private final StartupFields suFields;    
  
/**
 * Constructor
 * @param Ty TyBase - tymeac base storage
 * @param Startup b - Base class
 */    
protected StartupStandAlone(TyBase ty, Startup b) {
  
  T        = ty;
  base     = b;
  suFields = b.getStartupFields();
  
} // end-constructor 

/**
 * The startup is to use the StandAlone (UserClasses) mode
 * @return boolean
 */
protected boolean doBase() 
        throws Throwable {  
  
  TymeacUserVariables variable_table = suFields.getVariable_table();
    
  // say what
  doMsg(TyMsg.getMsg(1) + TyMsg.getText(65));

  // log and stats stuff
  String log_dir    = null;
  String log_file   = null;
  String log_alt    = null;
  String stats_dir  = null;
  String stats_file = null;
  String stats_alt  = null;
  
  boolean use_log   = false;
  boolean use_stats = false;

  try {
    // monitor interval
    T.setMonitor_interval(variable_table.getMonInterval());

    // Notification Function
    suFields.setNotify_func(variable_table.getNotify());

    // when in activation mode, inactivate minutes
    if  (base.isActivatable())  T.setInactivateMinutes(variable_table.getInactivate());

    // When Not using the system exit thread
    if  (variable_table.getSysExit() == 0) {
    
        // no shut exit
        T.setSysexit(false);
    }
    else {
        // is shut exit
        T.setSysexit(true);
        
    } // endif

    // log stuff
    use_log  = variable_table.getLogUse();
    log_dir  = variable_table.getLogDir();
    log_file = variable_table.getLogFile();
    log_alt  = variable_table.getLogAlt();

    // stats stuff
    use_stats  = variable_table.getStatsUse();
    stats_dir  = variable_table.getStatsDir();
    stats_file = variable_table.getStatsFile();
    stats_alt  = variable_table.getStatsAlt();

    // start up function strings
    T.setStart_array(variable_table.getStartUpFunctions());

    // start up exit strings
    suFields.setStart_array(variable_table.getStartUpClasses());

    // shutdown exit strings
    T.setShut1_array(variable_table.getShut1Classes());
    T.setShut2_array(variable_table.getShut2Classes());      

  } // end-try
  
  catch (NoSuchMethodError e) {

    // error
    doMsg(TyMsg.getMsg(45) + e);

    // all done
    return false;

  } // end-catch

  catch (Exception e) {

    // error
    doMsg(TyMsg.getMsg(45) + e);

    // all done
    return false;

  } // end-catch

  // *--- Logging ---*
  
  // create temp class
  SetUpLog log = new SetUpLog(T);

  // initialize logging object 
  log.initLog(null, // no dbms 
              log_dir, 
              log_file, 
              log_alt);
  
  // finalize startup for logging
  log.finiLogStartup();
  
  // *--- Statistics ---*
  
  // create temp class
  SetUpStats stats = new SetUpStats(T);
  
  // initialize stats object 
  stats.initStats(null, // no dbms
                  stats_dir, 
                  stats_file, 
                  stats_alt);
  
  // finalize startup for stats
  stats.finiStatsStartup();
  
  // ok 
  return true;
   
} // end-method

/**
 * Create the functions
 */
protected TymeacIndividualFunction[] doFunctions() {
  
  int nbr_func =0;
  
  // the Function Class
  TymeacUserFunctions func_table = null;
  
  // Function definition
  TymeacIndividualFunction[] tif = null;  

  try {
    // Load the Function Class
    func_table = new TymeacUserFunctions();

  } // end-try

  catch (NoClassDefFoundError e) {

    // not found 
    doMsg(TyMsg.getMsg(35)
              + TyMsg.getText(107)
              + TyMsg.getText(106));

    // all done
    return null; 

  } // end-catch

  try { 
    // get number of functions
    nbr_func = func_table.getNbrFunctions();

    // < 1 is no good
    if  (nbr_func < 1) {
      
        // not found 
        doMsg(TyMsg.getMsg(36)
              + TyMsg.getText(107)
              + TyMsg.getText(109));

        // all done
        return null;  

    } // endif

    // Function definition
    tif = func_table.getFunctions();

  } // end-try 

  catch (NoSuchMethodError e) {

    // not found 
    doMsg(TyMsg.getMsg(47)
            + TyMsg.getText(116)
            + e.toString());

    // all done
    return null; 

  } // end-catch

  catch (Exception e) {

    // not found
    doMsg(TyMsg.getMsg(47)
             + TyMsg.getText(116)
             + e.toString());

    // all done
    return null; 

  } // end-catch

  // *--- check for nulls & dups ---*
  
  // temp set for checking. HashSet rejects duplicates
  HashSet<String> hs = new HashSet<String>(nbr_func);
  
  // for error msg
  int i = 0;
  
  // do each detail
  for (TymeacIndividualFunction func : tif) {

    // When null, no good
    if  ((func == null) ||
         (func.getName() == null)) {

        // send out
        doMsg(TyMsg.getMsg(44)
            + TyMsg.getText(112)
            + i
            + TyMsg.getText(18));

        // all done
        return null;  

    } // endif
    
    // When Not added, is a duplicate
    if  (!hs.add(func.getName())) {

        // send out
        doMsg(TyMsg.getMsg(43)
            + TyMsg.getText(110)
            + func.getName()
            + TyMsg.getText(16));

        // all done
        return null; 

    } // endif
    
    // bump marker
    i++;
    
  } // end-for 
  
  return tif;  
  
} // end-method

/**
 * Create the queues
 */
protected TymeacIndividualQueue[] doQueues() {
  
  // variables
  int nbr_que   = 0;
  
  // Queue Class
  TymeacUserQueues que_table = null;
  
  // load Queue definitions
  TymeacIndividualQueue[] tiq = null;

  try {     
    // Load the Queue Class
    que_table = new TymeacUserQueues();

  } // end-try

  catch (NoClassDefFoundError e) {

    // not found 
    doMsg(TyMsg.getMsg(33)
              + TyMsg.getText(103)
              + TyMsg.getText(106));

    // all done
    return null; 

  } // end-catch

  try { 
    // get number of queues
    nbr_que = que_table.getNbrQueues();

    // When < 1 user, no good
    if  (nbr_que < 1) {

        // send out
        doMsg(TyMsg.getMsg(34)
              + TyMsg.getText(103)
              + TyMsg.getText(105));
  
        // all done
        return null;  

    } // endif   

    // load Queue definitions
    tiq = que_table.getQueues();

  } // end-try

  catch (NoSuchMethodError e) {

    // not found 
    doMsg(TyMsg.getMsg(46) 
              + TyMsg.getText(114)
              + e.toString());

    // all done
    return null;  

  } // end-catch

  catch (Exception e) {

    // not found 
    doMsg(TyMsg.getMsg(46)
             + TyMsg.getText(114)
             + e.toString());

    // all done
    return null;  

  } // end-catch
  
  // *--- check for nulls & dups ---*
  
  // temp set for checking. HashSet rejects duplicates
  HashSet<String> hs = new HashSet<String>(nbr_que);
  
  // for error msg
  int i = 0;
  
  // do each detail
  for (TymeacIndividualQueue que : tiq) {
    
    // When null, no good
    if  ((que == null) ||
         (que.getName() == null)) {
           
        // send out
        doMsg(TyMsg.getMsg(29)
            + i
            + TyMsg.getText(3));

        // all done
        return null;  

    } // endif
    
    // When Not added, is a duplicate
    if (!hs.add(que.getName())) {

        // send out
        doMsg(TyMsg.getMsg(29)
            + i
            + TyMsg.getText(4));

        // all done
        return null;  

    } // endif
    
    // bump marker
    i++;
    
  } // end-for 
  
  // ok
  return tiq;  
  
} // end-method


/**
 * General message writer
 * @param msg java.lang.String
 */
private void doMsg(String msg) {

  // When logging, do so
  if  (T.isLogUsed())  T.getLog_tbl().writeMsg(msg, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(msg);

} // end-method 

} // end-class
