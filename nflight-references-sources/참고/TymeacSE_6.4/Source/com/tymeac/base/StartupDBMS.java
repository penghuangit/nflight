package com.tymeac.base;

import java.sql.*;
import java.util.*;

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
 * Start up using a DBMS for variables.
 */
public final class StartupDBMS {
  
  // common
  private final TyBase T;
  private final Startup home;
  
  // General fields used by the other start up classes
  private final StartupFields suFields;
  
  // connection instance
  private Connection con = null;
  
  // statement instance
  private Statement stmt;
  
/**
 * Constructor
 * @param Ty TyBase - tymeac base storage
 * @param Startup b - Base class
 */    
protected StartupDBMS(TyBase ty, Startup b) {
  
  T        = ty;
  home     = b;
  suFields = b.getStartupFields();
  
} // end-constructor

/**
 * The startup is to use DBMS mode
 * @return boolean
 */
private boolean getStatement(TyCfgFields base) 
        throws SQLException, 
                Throwable {
    
  try {
    // When there is a mgr, try to load it
    if  ((base.dbms_DataManager != null) && 
         (base.dbms_DataManager.length() > 0)) {
        
        // save driver name in shared storage
        T.setDBDriver(base.dbms_DataManager);
        
        // load the driver
        java.sql.DriverManager.registerDriver((Driver)
          Class.forName(base.dbms_DataManager).newInstance());
        
    } // endif 
      
    // When no name use single parm constructor
    if  ((base.dbms_UserName == null) ||
         (base.dbms_UserName.length() == 0)) {
      
        // no user name/password
        con = DriverManager.getConnection(base.dbms_URL);
    }
    else {      
        // use all three parms
        con = DriverManager.getConnection(base.dbms_URL,  
                                          base.dbms_UserName,
                                          base.dbms_PassWord);
        
        // save Name and Password in shared storage
        T.setDBUserid(base.dbms_UserName);
        T.setDBPassword(base.dbms_PassWord);
        
    } // endif 
    
    // save URL in shared storage
    T.setDBUrl(base.dbms_URL);
                
    // get a stmt                                                                     
    stmt = con.createStatement();
    
  } // end-try
  
  catch (SQLException ex) {

    // send out
    doErrorSQL(TyMsg.getMsg(110) + ex.getMessage(), ex);

    // all done
    return false;
  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    // send out
    doMsg(TyMsg.getMsg(115) + ex.getMessage());    

    // all done
    return false;
 
   } // end-catch     
  
  return true;
  
} // end-method

/**
 * The startup is to use DBMS mode
 * @return boolean
 */
protected boolean doBase() 
        throws Throwable {
  
  // String fields
  TyCfgFields base = suFields.getBase();
  
  // initializing msg
  doMsg(TyMsg.getMsg(1) + TyMsg.getText(66));
  
  // When getting the connection and statement failed, get out
  if  (!getStatement(base)) return false;
          
  // When not using the shutdown thread 
  if  (home.isNo(base.sys_exit)) {
      
      // no shut exit
      T.setSysexit(false);
  }
  else {
      // is shut exit
      T.setSysexit(true);
      
  } // endif
      
  // monitor interval
  try {    
    T.setMonitor_interval(Integer.parseInt(base.mon_Interval));
      
  } // end-try

  catch (NumberFormatException e) {
      
    // default 1 minute
    T.setMonitor_interval(60);

  } // end-catch

  // When an activation startup
  if  (home.isActivatable()) {

      // inactivate minutes
      try {
        T.setInactivateMinutes(Integer.parseInt(base.act_Interval));
      
      } // end-try

      catch (NumberFormatException e) {
        
        // fatal error
        doMsg(TyMsg.getMsg(58) + base.act_Interval);    
    
        // all done
        return false;
  
      } // end-catch
  } // endif
          
  // notify function
  if  ((base.func_Notify == null) ||
       (base.func_Notify.length() < 1)) {

      // no notification function 
      suFields.setNotify_func(null);
  }
  else {       
      // notification function name
      suFields.setNotify_func(base.func_Notify); 
      
  } // endif  

  // start up exit strings
  suFields.setStart_array(base.start_classes);

  // start up function strings
  T.setStart_array(base.start_functions);

  // shutdown exit strings
  T.setShut1_array(base.shut1_classes);
  T.setShut2_array(base.shut2_classes);
  
  // *--- Logging ---*
  
  // create temp class
  SetUpLog log = new SetUpLog(T);
  
  // initialize logging object 
  log.initLog(base.dbms_Log, 
              base.logDir, 
              base.logFile, 
              base.altLogClass);
  
  // finalize startup for logging
  log.finiLogStartup();
  
  // *--- Statistics ---*
  
  // create temp class
  SetUpStats stats = new SetUpStats(T);
  
  // initialize stats object 
  stats.initStats(base.dbms_Stats, 
                  base.statsDir, 
                  base.statsFile, 
                  base.altStatsClass);
  
  // finalize startup for stats
  stats.finiStatsStartup();
  
  return true;
  
} // end-method

/**
 * Create the functions
 */
protected TymeacIndividualFunction[] doFunctions() 
            throws java.lang.Throwable {
  
  // the result of all this
  ResultSet rs;

  int nbr_func = 0;
  String list_tbl = suFields.getBase().dbms_ListTable;
  
  // function's queues
  String[] que_tbl = null;
        
  // user data
  TymeacIndividualFunction lcl_tif;

  // vector
  ArrayList<TymeacIndividualFunction> data = new ArrayList<TymeacIndividualFunction>();
  
  // all the functions                      
  String rest = "SELECT * FROM " 
                + suFields.getBase().dbms_FuncTable; 
            
  try {
    // get the full load
    rs = stmt.executeQuery(rest);
      
    // loop thru all
    while(rs.next()) {

      // new object
      lcl_tif = new TymeacIndividualFunction();

      // func name
      lcl_tif.setName(rs.getString(1));

      // oa name
      lcl_tif.setOa_name(rs.getString(2));

      // nbr queues
      lcl_tif.setNbrQue(rs.getInt(3));

      // add to vector
      data.add(lcl_tif);

    } // end-while

    // total in vector
    nbr_func = data.size();

    // new function array
    TymeacIndividualFunction[] tif = new TymeacIndividualFunction[nbr_func];

    // do all the functions
    for (int i = 0; i < nbr_func; i++) {  

      // new obj
      tif[i] = new TymeacIndividualFunction();

      // load it up

      // func name
      tif[i].setName((data.get(i)).getName());

      // oa name
      tif[i].setOa_name((data.get(i)).getOA());

      // nbr queues
      tif[i].setNbrQue((data.get(i)).getNbrQue());

      // list of queues
      que_tbl = loadList(list_tbl, 
                         tif[i].getName(),
                         tif[i].getNbrQue());

      // When invalid, done
      if  (que_tbl == null) return null;

      // put in tif
      tif[i].setList(que_tbl);  

    } // end-for
    
    // done
    return tif;
 
  } // end-try
  
  catch (SQLException ex) {

    try {
      // send out
      doErrorSQL(TyMsg.getMsg(210) + ex.getMessage(), ex);
      
    } // end-try
    
    catch (Throwable e) { 
      
      throw new Throwable(e);
      
    }// end-catch

    // all done
    return null;

  } // end-catch
  
  catch (java.lang.Exception ex)  {

    // send out
    doMsg(TyMsg.getMsg(215) + ex.getMessage());

    // all done
    return null;

  } // end-catch  
  
} // end-method

/**
 * Create the queues
 */
protected TymeacIndividualQueue[] doQueues() 
          throws java.lang.Throwable {
  
  // the result of all this
  ResultSet rs;
  
  // variables for the Queue
  String  a_name, a_st_threads, a_q_type;
  String  a_full_class;

  int a_pa_timeout, 
      a_waitlists,   
      a_nbr_in, a_nbr_in_th, a_waitime, 
      a_killtime, a_threads, 
      a_type, 
      a_th;      
   
  float a_overall, a_indiv, a_factor, a_average;
  
  // Error string
  String error;
    
  // all queues                       
  String rest = "SELECT * FROM " 
                 + suFields.getBase().dbms_QueTable; 
  
  // vector
  ArrayList<TymeacIndividualQueue> data = new ArrayList<TymeacIndividualQueue>();
    
  try {      
    // get the full load
    rs = stmt.executeQuery(rest);
              
    // loop thru all
    while(rs.next()) {
                
      // get que data 
      a_name       = rs.getString(1);        
      a_full_class = rs.getString(2);
      a_waitlists  = rs.getInt(3);
      a_nbr_in     = rs.getInt(4);
      a_waitime    = rs.getInt(5);
      a_killtime   = rs.getInt(6);
      a_threads    = rs.getInt(7); 
      a_type       = rs.getInt(8);
      a_th         = rs.getInt(9);
      a_overall    = rs.getFloat(10);
      a_indiv      = rs.getFloat(11);
      a_factor     = rs.getFloat(12);
      a_average    = rs.getFloat(13);
      a_pa_timeout = rs.getInt(14);
      a_nbr_in_th  = rs.getInt(15);

      // When queue normal or oa type
      a_q_type = (a_type == 0)? "no" : "yes";

      // When starting threads
      a_st_threads = (a_th == 0)? "no" : "yes";      

      // add to vector
      data.add(new TymeacIndividualQueue(a_name,
                                        a_full_class,
                                        a_q_type,
                                        a_st_threads,
                                        a_pa_timeout,
                                        a_threads,
                                        a_waitlists,
                                        a_nbr_in,
                                        a_nbr_in_th,
                                        a_waitime,
                                        a_killtime,
                                        a_overall,
                                        a_indiv,
                                        a_factor,
                                        a_average));    

    } // end-while
  } // end-try

  catch (SQLException ex) {

    try {    
      // send out
      doErrorSQL(TyMsg.getMsg(110) + ex.getSQLState(), ex);
      
    } // end-try 
    
    catch (Throwable e) { 
      
      throw new Throwable(e);
      
    }// end-catch
    
    // all done
    return null;

  } // end-catch
  
  catch (java.lang.Exception excp)  {

    // send out
    doMsg(TyMsg.getMsg(115) + excp.getMessage());

    // all done
     return null;

   } // end-catch 
  
  // work
  int nbr_que = data.size(), i; 

  // When nogood
  if  (nbr_que == 0) {
    
      // error msg
      error = TyMsg.getMsg(103);

      // send out
      doMsg(error);

      // all done
      return null;

  } // endif
    
  // built an array of indiv queues
  TymeacIndividualQueue[] tiq = new TymeacIndividualQueue[nbr_que];

  // load the array
  for (i = 0; i < nbr_que; i++) {

    // load queue
    tiq[i] = data.get(i);

  } // end-for
  
  // success
  return tiq;
  
} // end-method

/**
 * General message writer
 * @param msg java.lang.String
 * @exception java.lang.Throwable The exception description.
 */
private void doMsg(String msg) {

  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1)  TyBase.printMsg(msg);

} // end-method 

/**
 * General error routine caused by SQL exception 
 * @param error java.lang.String
 * @param ex java.lang.Exception
 * @exception java.lang.Throwable The exception description.
 */
private void doErrorSQL(String error, SQLException ex) 
    throws java.lang.Throwable {
  
  SQLException excp = ex;

  // When logging, do so
  if  (T.isLogUsed())  T.getLog_tbl().writeMsg(error, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1) {

      // print error                 
      TyBase.printMsg (error);

      while (excp != null) {
        TyBase.printMsg (TyMsg.getText(47)
                            + excp.getSQLState());
        TyBase.printMsg (TyMsg.getText(48)
                            + excp.getMessage());
        TyBase.printMsg (TyMsg.getText(49)
                            + excp.getErrorCode());
        
        // next exception
        excp = excp.getNextException();
        
        // dummy line
        TyBase.printMsg ("");

      } // end-while
  } // endif
              
  // When an activation startup, throw the error
  if  (home.isActivatable()) throw new Throwable(error); 

} // end-method

/**
 * DataBase is finished. Close all
 */
protected void finiDataBase() {
  
  try {    
    // all done
    stmt.close();
    con.close();
    
  } // end-try
  
  catch (SQLException ex) {
    
    // When printing, say what happended
    if  (T.getSysout() == 1) TyBase.printMsg(TyMsg.getText(175) + ex.getMessage());
    
  } // end-catch
  
} // end-method

/**
 * Load the list of queues for a function
 * @param table String - talbl name
 * @param function String - function name
 * @param nbr_que int - number of queues
 * @return String[]
 */ 
private String[] loadList( String table,
                           String function,
                           int   nbr_que) 
        throws Throwable {
  
  // the result of all this
  ResultSet rs;  
  
  // array for building a Que Table
  String [] que_tbl = new String[nbr_que];
        
  String full;    
    
  String base = "SELECT QUEUE_NAME FROM " 
                + table
                + " WHERE FUNC_NAME = '"
                + function 
                + "' AND SEQ_NBR = ";
            
  String rest = " "; 
              
  try {
    // get every queue in the list
    for (int seq = 1, i = 0; i < nbr_que; i++, seq++) {
          
      // for new string
      full = base + seq + rest;
      
      // get the que name in the list
      rs = stmt.executeQuery(full);
        
      // get next name
      if  (rs.next()) {

          // que name
          que_tbl[i] = rs.getString(1);
      }
      else {
          // no next name
          doMsg(TyMsg.getMsg(305) 
            + TyMsg.getText(36)
            + function
            + TyMsg.getText(37) 
            + nbr_que
            + TyMsg.getText(38)
            + seq
            + TyMsg.getText(39) 
            + table
            + TyMsg.getText(40));

          // all done
          return null;

      } // endif          
    } // end-for            

    } // end-try
      
  catch (SQLException ex) {

    // send out
    doErrorSQL(TyMsg.getMsg(315) + ex.getMessage(), ex);

    // all done
    return null;

  } // end-catch
  
  catch (java.lang.Exception ex)  {

    // send out
    doMsg(TyMsg.getMsg(320) + ex.getMessage());

    // all done
    return null;

   } // end-catch 
  
  // OK
  return que_tbl;
  
} // end-method

}// end-class
