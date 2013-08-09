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
import java.sql.*;
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import com.tymeac.client.*;
import com.tymeac.base.*;

/**
 * Backend object for the queue maint GUI
 */
public class TyQueMaintBean2 {
  
  // DBMS
  boolean first_time = true;
  
  // connection instance
  Connection con = null;
  
  // statement                                                                     
  Statement stmt = null;
  
  // indicators
  private int import_Result;
  private int update_Result;
  private int delete_Result;
  
  // configuration data 
  private TyCfgFields fields = null;
  
  // old value from the import
  private String O_que_name = ""; 
	
  // new values from the window
  private String  N_que_name    = null;
  private String  N_pa_class    = null;
  
  private int N_pa_timeout  = 0;
  private int N_que_type    = 0;
  private int N_th_start    = 0;
  private int N_nbr_threads = 1;
  private int N_wait_time   = 10;
  private int N_kill_time   = 30;
  private int N_nbr_wl      = 1;
  private int N_nbr_in_wl   = 10;
  private int N_th_nbr_in_wl = 10;
  
  private float N_overall   = 0.0F;
  private float N_individ   = 0.0F;
  private float N_factor    = 0.0F; 
  private float N_average   = 0.0F;
  
  // security
  private LoginContext loginContext = null;

/**
 * Constructor
 * @param cfg TyCfgFields - configuration file fields
 */ 
public TyQueMaintBean2 (TyCfgFields cfg) {
  
  // cfg file fields
  fields = cfg;
  
  // security
  loginContext = getContext();

} // end-constructor 

/**
 * basic SQL stuff
 */ 
private int basic() {          

  // When 1st time
  if  (first_time) {

    try {
      // When there is a mgr, try to load it
      if  (fields.dbms_DataManager != null)  {

        // load
        java.sql.DriverManager.registerDriver((Driver)
            Class.forName(fields.dbms_DataManager).newInstance());
        
      } // endif
    } // end-try
            
    catch (Exception ex) {

      // say what
      System.out.println ("Exception loading DriverManager: " + ex.getMessage());
      
      // done
      return 1;
    
    } // end-catch
    
    // not first_time now
    first_time = false;   
    
  } // endif          
  
  try {
    // When no name, use single parm constructor
    if  ((fields.dbms_UserName == null) || 
         (fields.dbms_UserName.length() == 0)) {

      // single
      con = DriverManager.getConnection(fields.dbms_URL);
      }
    else {      
      // use all three parms
      con = DriverManager.getConnection(fields.dbms_URL,  
                        fields.dbms_UserName,
                        fields.dbms_PassWord);
    } // endif 
      
    // get a stmt                                                                     
    stmt = con.createStatement();            
                    
  } // end-try
  
  catch (SQLException ex) {

    System.out.println ("SQLException:");
      
    while (ex != null) {
        System.out.println ("SQLState: " 
                    + ex.getSQLState());
        System.out.println ("Message:  " 
                    + ex.getMessage());
        System.out.println ("Vendor:   " 
                    + ex.getErrorCode());
        ex = ex.getNextException();
        System.out.println ("");

    } // end-while 
    
    // done
    return 2;
          
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    System.out.println("Exception: " + ex);
    ex.printStackTrace (); 

    // done
    return 3;
          
  } // end-catch 
  
  // good done
  return 0;

} // end-method 

/**
 * Delete button
 * @param S_que_name JTextField - queue name
 */ 
public void deleteButton(JTextField S_que_name) {  

  // When there are no fields data
  if  (fields == null) {

    // say so
    delete_Result = 4;

    // back
    return;

  } // endif

  // set the queue name
  N_que_name = S_que_name.getText();
  
  // When not using security     
    if (loginContext == null) {
          System.out.println("UNSECURED modification"); 
        // non-secure
        doDelete();     
    } 
    else {
        // When NOT a good login
        if  (!ClientSecurityModule.login(loginContext)) {            
            
            System.out.println("Login failed");
            System.exit(1);
            
        } // endif
                  
        try {
          System.out.println("Secured modification");         
          // do as privileged
          Subject.doAsPrivileged(
              loginContext.getSubject(),
              new PrivilegedExceptionAction<Object>() {
                  public Object run() throws Exception {
                      doDelete();
                      return null;
                  } // end-method
              }, // end-PEA
              null);
              
        } // end-try
        
        catch (PrivilegedActionException e) {
          
            // say what found
            System.out.println("PrivilegedActionException= " + e);
            System.exit(1);                    
                     
        } // end-catch              
    } // endif
    
} // end-method

/**
 * delete queue
 * @return int
 */
private int deleteQue() {  
  
  String delete   = "DELETE FROM " 
            + fields.dbms_QueTable
            + " WHERE QUEUE_NAME = '"
            + N_que_name
            + "'  ";
        //+ "'  ;";  
  
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_QueTable == null) {

    delete_Result = 1;
    return 1;

  } // endif 
  
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
    // say db error
    delete_Result = 3;
    
    // done
    return 3;
    
  } // endif         
      
  //  
  try {                    
    // delete the queue
    if  (stmt.executeUpdate(delete) != 1) {

      // bad
      delete_Result = 2;
    }
    else {
      // good
      delete_Result = 0;
      
      // no old anymore
      O_que_name = "";

    } // endif
          
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {
    System.out.println ("SQLException:");
      
    while (ex != null) {
      System.out.println ("SQLState: " 
                + ex.getSQLState());
      System.out.println ("Message:  " 
                + ex.getMessage());
      System.out.println ("Vendor:   " 
                + ex.getErrorCode());
      ex = ex.getNextException();
      System.out.println ("");
                    
    } // end-while  

    // error
    delete_Result = 3;
    return 3;                 

  } // end-catch
  
  catch (java.lang.Exception ex)  {

    System.out.println("Exception: " + ex);
    ex.printStackTrace ();
      
    delete_Result = 3;      
    return 3;
          
   } // end-catch 
        
  return 0;
  
} // end-method

/**
 * Finish button
 * @param S_que_name JTextField - que name
 * @param S_pa_class JTextField - appl class name
 * @param S_que_type JCheckBox  - Normal or Output Agent 
 * @param S_th_start JCheckBox  - start threads at start up yes or no
 * @param S_pa_timeout JTextField - timeout seconds
 * @param S_nbr_threads JTextField - number of threads   
 * @param S_wait_time JTextField -  time to wait for new work  
 * @param S_kill_time JTextField -  time to kill thread
 * @param S_nbr_wl    JTextField -  number of wait lists
 * @param S_nbr_in_wl JTextField -  number of slots in a physical wait list
 * @param S_th_nbr_in_wl JTextField -  number of slots in a logical wait list
 * @param S_overall   JTextField -  Overall %
 * @param S_individ   JTextField -  Individual %
 * @param S_factor    JTextField -  Weighted Factor
 * @param S_average   JTextField -  Weighted Average %
 */
public void finishButton( JTextField S_que_name,
                          JTextField S_pa_class,
                          JTextField S_pa_timeout,
                          JCheckBox  S_que_type,
                          JCheckBox  S_th_start,
                          JTextField S_nbr_threads,
                          JTextField S_wait_time,
                          JTextField S_kill_time,
                          JTextField S_nbr_wl,
                          JTextField S_nbr_in_wl,
                          JTextField S_th_nbr_in_wl,
                          JTextField S_overall,
                          JTextField S_individ,
                          JTextField S_factor,
                          JTextField S_average) {
                
  
  // name of this queue
  N_que_name = S_que_name.getText();
    
  // class to invoke
  N_pa_class = S_pa_class.getText();
  
  // must enter something
  if  ((N_pa_class == null) ||
       (N_pa_class.length() < 1)) {
  
    update_Result = 12;
    return;

  } // endif   

  // timeout value
  try {
    N_pa_timeout = Integer.parseInt(S_pa_timeout.getText());
  } // end-try

  catch (NumberFormatException e) {
    N_pa_timeout = 0;
  } // end-catch
    
  // number of wait lists
  try {
    N_nbr_wl = Integer.parseInt(S_nbr_wl.getText());
  } // end-try

  catch (NumberFormatException e) {
    N_nbr_wl = 0;
  } // end-catch
  
  // When not at least 1, no good
  if  (N_nbr_wl > 0) {
    ;
  }
  else {      
    update_Result = 4;
    return;

  } // endif    
  
  // number in a physical wait list
  try {
    N_nbr_in_wl = Integer.parseInt(S_nbr_in_wl.getText());
  } // end-try

  catch (NumberFormatException e) {
    N_nbr_in_wl = 0;
  } // end-catch
  
  // When not at least 1, no good
  if  (N_nbr_in_wl > 0) {
    ;
  }
  else {      
    update_Result = 5;
    return;

  } // endif  
  
  // number in a logical wait list
  try {
    N_th_nbr_in_wl = Integer.parseInt(S_th_nbr_in_wl.getText());
  } // end-try

  catch (NumberFormatException e) {
    
    // set same as physical
    N_th_nbr_in_wl = N_nbr_in_wl;
    
  } // end-catch
  
  // When invalid
  if  ((N_th_nbr_in_wl < 1) ||
       (N_th_nbr_in_wl > N_nbr_in_wl)) {
    
      // set same as physical
      N_th_nbr_in_wl = N_nbr_in_wl;
  
  } // endif  
  
  // wait time
  try {
    N_wait_time = Integer.parseInt(S_wait_time.getText());
  } // end-try

  catch (NumberFormatException e) {
    N_wait_time = 0;
  } // end-catch    

  // kill time
  try {
    N_kill_time = Integer.parseInt(S_kill_time.getText());
  } // end-try

  catch (NumberFormatException e) {
    N_kill_time = 0;
  } // end-catch  
    
  // number of threads
  try {
    N_nbr_threads = Integer.parseInt(S_nbr_threads.getText());
  } // end-try

  catch (NumberFormatException e) {
    N_nbr_threads = 0;
  } // end-catch    
  
  // When not at least 1, no good
  if  (N_nbr_threads > 0) {
    ;
  }
  else {      
    update_Result = 6;
    return;

  } // endif    
  
  // thread start
  if  (S_th_start.getAccessibleContext().getAccessibleStateSet().contains(
                  javax.accessibility.AccessibleState.CHECKED)) {
  
      N_th_start = 1;
  }
  else {  
      N_th_start = 0;

  } // endif
  
  // que type
  if  (S_que_type.getAccessibleContext().getAccessibleStateSet().contains(
                javax.accessibility.AccessibleState.CHECKED)) {
  
      N_que_type = 1;
  }
  else {  
      N_que_type = 0;

  } // endif
  
  String S;
  
  // overall %
  try {
    S = "0." + S_overall.getText();    
    N_overall = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 8;
    return;
  } // end-catch

  // individual %
  try {
    S = "0." + S_individ.getText();    
    N_individ = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 9;
    return;
  } // end-catch
  
  // weighted factor
  try {
    S = "0." + S_factor.getText();    
    N_factor = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 10;
    return;
  } // end-catch

  // weighted average
  try {
    S = "0." + S_average.getText();    
    N_average = Float.valueOf(S).floatValue();
  } // end-try

  catch (NumberFormatException e) {
    update_Result = 11;
    return;
  } // end-catch
      
  // When not using security     
    if (loginContext == null) {
          System.out.println("UNSECURED modification"); 
        // non-secure
        doFinish();     
    } 
    else {
        // When NOT a good login
        if  (!ClientSecurityModule.login(loginContext)) {            
            
            System.out.println("Login failed");
            System.exit(1);
            
        } // endif
                  
        try {
          System.out.println("Secured modification");         
          // do as privileged
          Subject.doAsPrivileged(
              loginContext.getSubject(),
              new PrivilegedExceptionAction<Object>() {
                  public Object run() throws Exception {
                      doFinish();
                      return null;
                  } // end-method
              }, // end-PEA
              null);
              
        } // end-try
        
        catch (PrivilegedActionException e) {
          
            // say what found
            System.out.println("PrivilegedActionException= " + e);
            System.exit(1);                    
                     
        } // end-catch              
    } // endif
          
} // end-method

/**
 * accessor for delete result
 * @return int
 */ 
public int getDelResult () { return delete_Result; } // end-method

/**
 * accessor for import result
 * @return int
 */ 
public int getImpResult () { return import_Result; } // end-method

/**
 * accessor for update result
 * @return int
 */ 
public int getUpdResult () { return update_Result; } // end-method

/**
 * IMPORT BUTTON
 * @param S_que_name JTextField - que name
 * @param S_pa_class JTextField - appl class name
 * @param S_que_type JCheckBox  - Normal or Output Agent 
 * @param S_th_start JCheckBox  - start threads at start up yes or no
 * @param S_pa_timeout JTextField - timeout seconds
 * @param S_nbr_threads JTextField - number of threads   
 * @param S_wait_time JTextField -  time to wait for new work  
 * @param S_kill_time JTextField -  time to kill thread
 * @param S_nbr_wl    JTextField -  number of wait lists
 * @param S_nbr_in_wl JTextField -  number of physical slots in a wait list
 * @param S_th_nbr_in_wl JTextField -  number of logical slots in a wait list
 * @param S_overall   JTextField -  Overall %
 * @param S_individ   JTextField -  Individual %
 * @param S_factor    JTextField -  Weighted Factor
 * @param S_average   JTextField -  Weighted Average %
 */

public void importButton( JTextField S_que_name,
                          JTextField S_pa_class,
                          JTextField S_pa_timeout,
                          JCheckBox  S_que_type,
                          JCheckBox  S_th_start,
                          JTextField S_nbr_threads,
                          JTextField S_wait_time,
                          JTextField S_kill_time,
                          JTextField S_nbr_wl,
                          JTextField S_nbr_in_wl,
                          JTextField S_th_nbr_in_wl,
                          JTextField S_overall,
                          JTextField S_individ,
                          JTextField S_factor,
                          JTextField S_average) {
    
  // set the queue name
  N_que_name = S_que_name.getText();

  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doImport();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured access");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doImport();
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);
          System.exit(1);                    
                   
      } // end-catch              
  } // endif
  
  // When not there, return
  if  (import_Result != 0) {
     
    // done
    return;

  } // endif      
  
  // does exist, can do an update
  O_que_name = N_que_name;
  
  // data from load
  S_pa_class.setText(N_pa_class);
  
  // When que type 0, no, 1, yes
  if  (N_que_type == 0) {
        
    S_que_type.setSelected(false);
  }
  else {
    S_que_type.setSelected(true);

  } // endif    

  // When th_start type 0, no, 1, yes
  if  (N_th_start == 0) {
        
    S_th_start.setSelected(false);
  }
  else {
    S_th_start.setSelected(true);

  } // endif                      
  
  String S;   

  S = "" + N_pa_timeout;
  S_pa_timeout.setText(S);

  S = "" + N_nbr_threads;
  S_nbr_threads.setText(S);
  
  S = "" + N_wait_time;
  S_wait_time.setText(S);

  S = "" + N_kill_time;
  S_kill_time.setText(S);
  
  S = "" + N_nbr_wl;
  S_nbr_wl.setText(S);
  
  S = "" + N_nbr_in_wl;
  S_nbr_in_wl.setText(S);
  
  S = "" + N_th_nbr_in_wl;
  S_th_nbr_in_wl.setText(S);
  
  S = "" + N_overall;
  if  (S.length() == 3) S = S + "0"; // so it shows as 2 digits
  S_overall.setText(S.substring(2));
  
  S = "" + N_individ;
  if  (S.length() == 3) S = S + "0"; // so it shows as 2 digits
  S_individ.setText(S.substring(2));
  
  S = "" + N_factor;
  if  (S.length() == 3) S = S + "0"; // so it shows as 2 digits
  S_factor.setText(S.substring(2));
  
  S = "" + N_average;
  if  (S.length() == 3) S = S + "0"; // so it shows as 2 digits
  S_average.setText(S.substring(2));
                
} // end-method

/**
 * Insert into DBMS
 * @return int
 */
private int insertQue() {   
          
  String insert = "INSERT INTO "
          + fields.dbms_QueTable
          + "(QUEUE_NAME, PA_CLASS," 
          + " NBR_WAITLISTS, NBR_IN_WAITLISTS, WAIT_TIME, THREAD_KILL_TIME,"
          + " NBR_THREADS, QUE_TYPE, THREAD_START_DELAY," 
          + " OVERALL_PERCENT, INDIVIDUAL_PERCENT, WEIGHTED_FACTOR, WEIGHTED_AVERAGE,"
          + " PA_TIMEOUT, TH_NBR_IN_WAITLISTS) "
          + " VALUES ('"
          + N_que_name
          + "', '"
          + N_pa_class
          + "',"
          + N_nbr_wl
          + ", "
          + N_nbr_in_wl
          + ", "
          + N_wait_time
          + ", "
          + N_kill_time
          + ", "
          + N_nbr_threads
          + ", "
          + N_que_type
          + ", "
          + N_th_start
          + ", "
          + N_overall
          + ", "
          + N_individ
          + ", "
          + N_factor
          + ","
          + N_average
          + ","
          + N_pa_timeout
          + ", "
          + N_th_nbr_in_wl
          + ") ";  
    //+ ") ;";  
  
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_QueTable == null) {

    // no good
    update_Result = 1;

    // back
    return 1;

  } // endif
  
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
    // say db error
    update_Result = 3;
    
    // done
    return 3;
    
  } // endif            
  
  try {
    // When successful
    if  (stmt.executeUpdate(insert) == 1) {

      // successfull  
      update_Result = 0;
    }
    else {
      // invalid que name
      update_Result = 2;

    } // endif
      
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {
    System.out.println ("SQLException:");
      
    while (ex != null) {
      System.out.println ("SQLState: " 
                + ex.getSQLState());
      System.out.println ("Message:  " 
                + ex.getMessage());
      System.out.println ("Vendor:   " 
                + ex.getErrorCode());
      ex = ex.getNextException();
      System.out.println ("");
      
    } // end-while

    // error
    update_Result = 3;

    // done
    return 3;
          
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    System.out.println("Exception: " + ex);
    ex.printStackTrace ();
      
    update_Result = 3;
    return 3;                 

  } // end-catch 
  
  // ok
  return 0;
  
} // end-method 

/**
 * load queue
 * @return int
 */
private int loadQue() {  
  
  // to get the requested row
  String base  = "SELECT * FROM " 
          + fields.dbms_QueTable
          + " WHERE QUEUE_NAME = '"
          + N_que_name
          + "'  ";
      //+ "'  ;";
  
  // the result of all this
  ResultSet rs;   
          
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_QueTable == null) {

    // no good
    import_Result = 1;

    // back
    return 1;

  } // endif  
  
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
    // say db error
    import_Result = 3;
    
    // done
    return 3;
    
  } // endif       
  
  // Find the requested queue 
  try {          
    // get the queue
    rs = stmt.executeQuery(base);
      
    // When nothing there:
    if  (!rs.next()) {
        
      // no good
      import_Result = 2;

      // back
      return 2;
    } // endif
      
    // get the fields     
    N_pa_class  = rs.getString(2);
      
    N_nbr_wl       = rs.getInt(3);
    N_nbr_in_wl    = rs.getInt(4);
    N_wait_time    = rs.getInt(5);
    N_kill_time    = rs.getInt(6);
    N_nbr_threads  = rs.getInt(7);    
    N_que_type     = rs.getInt(8);
    N_th_start     = rs.getInt(9);
    N_overall      = rs.getFloat(10);
    N_individ      = rs.getFloat(11);
    N_factor       = rs.getFloat(12);
    N_average      = rs.getFloat(13);
    N_pa_timeout   = rs.getInt(14);
    N_th_nbr_in_wl = rs.getInt(15);
          
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {

    System.out.println ("SQLException:");
      
    while (ex != null) {
      System.out.println ("SQLState: " 
                + ex.getSQLState());
      System.out.println ("Message:  " 
                + ex.getMessage());
      System.out.println ("Vendor:   " 
                + ex.getErrorCode());
      ex = ex.getNextException();
      System.out.println ("");
    } // end-while
      
    // return with error
    import_Result = 3;
    return 3;
      
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    System.out.println("Exception: " + ex);
    ex.printStackTrace ();
      
    // return with error
    import_Result = 3;
    return 3;
      
   } // end-catch 
  
  import_Result = 0;    
  return 0;
  
} // end-method

/**
 * Update the Queue DBMS
 * @return int
 */
private int updateQue() {   
          
  String update = "UPDATE "
          + fields.dbms_QueTable
          + " SET "
          + "PA_CLASS = '"
          + N_pa_class
          + "', "
          + "NBR_WAITLISTS = "
          + N_nbr_wl
          + ", "          
          + "NBR_IN_WAITLISTS = "
          + N_nbr_in_wl
          + ", "          
          + "WAIT_TIME = "
          + N_wait_time
          + ", "
          + "THREAD_KILL_TIME = "
          + N_kill_time
          + ", "
          + "NBR_THREADS = "
          + N_nbr_threads
          + ", "
          + "QUE_TYPE = "
          + N_que_type
          + ", "
          + "THREAD_START_DELAY = "
          + N_th_start
          + ", "  
          + "OVERALL_PERCENT = "
          + N_overall
          + ", "
          + "INDIVIDUAL_PERCENT = "
          + N_individ
          + ", "
          + "WEIGHTED_FACTOR = "
          + N_factor
          + ", "
          + "WEIGHTED_AVERAGE = "
          + N_average
          + ", "
          + "PA_TIMEOUT = "
          + N_pa_timeout
          + ", "
          + "TH_NBR_IN_WAITLISTS = "
          + N_th_nbr_in_wl
          + " WHERE "
          + "QUEUE_NAME = '" 
          + N_que_name
          + "' "; 
    //+ "' ; "; 
  
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_QueTable == null) {

    update_Result = 1;
    return 1;

  } // endif
  
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
    // say db error
    update_Result = 3;
    
    // done
    return 3;
    
  } // endif 
            
   //   
  try {            
    // When successful
    if  (stmt.executeUpdate(update) == 1) {

      // good
      update_Result = 0; 
    }
    else {
      // bad
      update_Result = 2;
    
    } // endif                
      
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {

    System.out.println ("SQLException:");
      
    while (ex != null) {
      System.out.println ("SQLState: " 
                + ex.getSQLState());
      System.out.println ("Message:  " 
                + ex.getMessage());
      System.out.println ("Vendor:   " 
                + ex.getErrorCode());
      ex = ex.getNextException();
      System.out.println ("");
      
    } // end-while  

    // error
    update_Result = 3;
    return 3;                 

  } // end-catch
  
  catch (java.lang.Exception ex)  {

    System.out.println("Exception: " + ex);
    ex.printStackTrace ();
      
    update_Result = 3;          
    return 3;   
    
  } // end-catch 
  
  // ok
  return 0;
    
} // end-method  

 /**
 * Get the context for logging in. This is user defined.  
 */
private LoginContext getContext() { return ClientSecurityModule.getBasicContext(); } // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doDelete() {

  // delete the que from the database
  delete_Result = deleteQue();

} // end-method       

/**
 * Executed as privileged or not as privileged
 */
private void doFinish() {

  // When the que name matchs what was 'read for update'
  if  (O_que_name.compareTo(N_que_name) == 0) {

      // update 
      updateQue();
    
      // no old anymore
      O_que_name = "";
  }
  else {
      // insert  
      insertQue();
      
  } // endif

} // end-method   
    
/**
 * Executed as privileged or not as privileged
 */
private void doImport() {

  // get the que data from the database
  loadQue();

} // end-method       
} // end-class
