package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Tymeac logging thread
 */ 
public final class TyLogThread 
          extends Thread {
  
  // Tymeac base storage
  private final TyBase T;

  // logTable base storage
  private final TyLogTable tbl;                 
  
  // database stuff
  private String dbms_URL      = null;   
  private String dbms_User     = null;           
  private String dbms_PassWord = null;       
  private String dbms_LogTable = null;

  // connection instance
  private Connection con = null;

  // stmt                                                                     
  Statement stmt = null;

  // local file stuff
  private String local_dir  = null;
  private String local_file = null;

  // separater
  private String sep = null;

  // new line
  private String crlf = null;

  // alt stuff
  private TymeacAlternativesInterface alt = null;
  private String alt_name = null;

  // passed msg
  String msg = null;
  
  // use local or use alternate (neither is DBMS)
  private boolean use_local = false;
  private boolean use_alt = false;
  
  // local problem
  private boolean had_problem = false;

  // verified 
  private boolean table_valid = false;

  // posted indicator
  private volatile boolean posted = false;

  // posted for verification indicator
  private volatile boolean verify = false;   

/**
 * Constructor for an alternate class
 * @param Ty com.tymeac.base.TyBase
 * @param table com.tymeac.base.TyLogTable
 * @param alt com.tymeac.base.TymeacAlternativesInterface
 */
protected TyLogThread(TyBase Ty,
                      TyLogTable table,
                      TymeacAlternativesInterface altClass) {

  // name of this thread
  super("TymeacLogger");

  // base storage
  T = Ty;

  // log table storage
  tbl = table;

  // alt addr
  alt = altClass;
  
  // String name
  alt_name = alt.getClass().getName();

  // use alt method
  use_alt = true;

  // table initially invalid
  table_valid = false;

  // not posted
  posted = false;

} // end-constructor

/**
 * Constructor for local file logging
 * @param Ty TyBase - Tymeac base storage
 * @param table TyLogTable - the base logger class
 * @param c_Dir String - file directory
 * @param C_File String - file name
 */
protected TyLogThread(  TyBase Ty, 
                    TyLogTable table,
                    String c_Dir,
                    String c_File) {

  // name of this thread
  super("TymeacLogger");

  // base storage
  T = Ty;

  // log table storage
  tbl = table;

  // table initially invalid
  table_valid = false;

  // not posted
  posted = false;

  // use a local file
  use_local = true;

  // local dir
  local_dir = c_Dir;

  // local file
  local_file = c_File;

  // When no dir
  if  (local_dir == null) {

      // get a pointer to the file
      File afile = new File(local_dir,local_file);
      
      // get the full path including the filename                 
      String tdir = afile.getAbsolutePath();
            
      // get rid of the filename
      local_dir = tdir.substring(0, tdir.length() - local_file.length());

  } // endif 

  // append cr/lf to the end of each line
  crlf = System.getProperties().getProperty("line.separator");

  // file separator
  sep = System.getProperties().getProperty("file.separator"); 

} // end-constructor

/**
 * Constructor for DBMS logging
 * @param Ty TyBase - Tymeac base storage
 * @param table TyLogTable - base logger class
 * @param c_log String table name
 */
protected TyLogThread(TyBase Ty, 
                      TyLogTable table,
                      String c_Log) {

  // name of this thread
  super("TymeacLogger");

  // base storage
  T = Ty;

  // log table storage
  tbl = table;

  // table initially invalid
  table_valid = false;

  // not posted
  posted = false;

  // dmms stuff
  dbms_URL      = T.getDBURL();
  dbms_User     = T.getDBUserid();
  dbms_PassWord = T.getDBPassword(); 
  dbms_LogTable = c_Log;
  
  /*
   * Get a connection and statement and keep them until the thread dies. 
   */
  
  try {
    // When no name, use single parm constructor
    if  ((dbms_User == null) ||
         (dbms_User.length() == 0)) {

        // single 
        con = DriverManager.getConnection(dbms_URL);
    }
    else {      
        // use all three parms
        con = DriverManager.getConnection(dbms_URL,  
                                          dbms_User,
                                          dbms_PassWord);
    } // endif        
    
    // get a statement                                                                      
    stmt = con.createStatement();
    
  } // end-try
    
  catch (SQLException ex) {

    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(23) 
                        + TyMsg.getText(2) 
                        + ex.getMessage());
    
    } // endif                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(23) 
                          + TyMsg.getText(2) 
                          + ex.getMessage());
    
    } // endif                        
  } // end-catch  

} // end-constructor

/**
 * close the DBMS statement and connection
 */
private void closeDB() {
  
  // When using a DBMS
  if  (con != null) {
  
    try {       
      // close the statement and connection
      stmt.close();
      con.close();
      
    } // end-try
    
    catch (SQLException ex) {
  
      // When printing
      if  (T.getSysout() == 1) {
  
        // write                          
        TyBase.printMsg(TyMsg.getMsg(23) 
                          + TyMsg.getText(2) 
                          + ex.getMessage());
      
      } // endif
    } // end-catch
    
    catch (java.lang.Exception ex)  {
  
      // When printing
      if  (T.getSysout() == 1) {
  
        // write                          
        TyBase.printMsg(TyMsg.getMsg(23) 
                            + TyMsg.getText(2) 
                            + ex.getMessage());
      
      } // endif       
    } // end-catch 
  } // endif
  
} // end-method

/**
 * Either initially verify the information or write a message and wait for completion.
 */
public void run() {

  // time out switch
  boolean timed_out = false;

  // until timed out
  while (!timed_out) {
    
    synchronized (this) {
                            
      // wait for a posting or time out
      while (!posted) {
        
        try {
          // wait for a post or 30 seconds
          wait(30000);

          // When timed out   
          if  (!posted) {
  
              // set temp status as inactive        
              timed_out = true;

              // set is posted
              posted = true;  
          
          } // endif
        } // end-try

        catch (InterruptedException e) {

        } // end-catch
      } // end-while          

      // reset the posted indicator
      posted = false;
  
    } // end-sync

    // When NOT timed out
    if  (!timed_out) {

        // When a verify
        if  (verify) {

            // reset
            verify = false;

            // do a verify
            verifyTable();
        }
        else {
            // write the msg
            writeMsg();

        } // endif
        
        // When there was a problem, get out
        if  (had_problem) break;
        
    } // endif
  } // end-while
  
  // close the database when using a DBMS
  closeDB();

  // say is finished
  tbl.setDone();  

} // end-method

/**
 * Verify that the alt logging class works
 * @return boolean does or not
 */
private boolean verifyAlt () {
  
  try {    
    // call verify and set the return value       
    return alt.verify();
    
  } // end-try
  catch (Exception e) {
    
    // When printing
    if  (T.getSysout() == 1) {

        // write                          
        TyBase.printMsg(TyMsg.getMsg(53)
                        + alt_name
                        + TyMsg.getText(85) 
                        + e.getMessage());
    
    } // endif
    
    return false;
    
  } // end-catch
  
} // end-method

/**
 * Using a local file -- just open it to see if its ok  
 * @return opened or not
 */
private boolean verifyLocal () { 
    
  // output definition
  FileOutputStream fileOutStream = null;        
    
  try {
    // open and append data
    fileOutStream = new FileOutputStream(local_dir + sep + local_file, true);

  } // end-try
      
  catch (IOException e) {

    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(22) 
                          + TyMsg.getText(69)
                          + e.getMessage());

    } // endif

    // failure
    return false;
        
  } // end-catch
    
  try {
    // close
    fileOutStream.close();

  } // end-try          
      
  catch (IOException e) {
              
      // When printing
      if  (T.getSysout() == 1) {

          // write                          
          TyBase.printMsg(TyMsg.getMsg(24)
                              + TyMsg.getText(2) 
                              + e.getMessage());

    } // endif

    // failure                
    return false;

  } // end-catch

  // good
  return true;          
      
} // end-method
/**
 * Varify the table data
 * @return verified or not
 */
private void verifyTable () {

  // When using a local file
  if  (use_local) {

      // When good
      if  (verifyLocal()) {

          // return good
          tbl.setValid(true);
      }
      else {
          // return bad
          tbl.setValid(false);
          
          // local bad
          had_problem = true;

      } // endif

      // say is finished
      tbl.setFinished();

      // done
      return;

  } // endif

    // When using an alt method
  if  (use_alt) {

      // call verify and set the return value       
      table_valid = verifyAlt();
    
      // return what happended
      tbl.setValid(table_valid);
      
      // When bad, say local bad
      if  (!table_valid)  had_problem = true;

      // say is finished
      tbl.setFinished();

      // done
      return;
  
  } // endif

  // *--- dbms stuff ---*
  
  // the necessary database fields cannot be null
  if  (con == null || stmt == null) {

      // error
      tbl.setValid(false);
      
      // local bad
      had_problem = true;

      // say is finished
      tbl.setFinished();

      // done
      return;

  } // endif

  // Select dummy                   
  String rest = "SELECT Y_EAR FROM " 
              + dbms_LogTable; 
            //  + " ;";    

  try {
    // try to access
    stmt.executeQuery(rest); 
    
    // return good
    tbl.setValid(true);
  
    // say is finished
    tbl.setFinished();

  } // end-try
  
  catch (SQLException ex) {

    // When printing
    if  (T.getSysout() == 1) {

        // write                          
        TyBase.printMsg(TyMsg.getMsg(23) 
                        + TyMsg.getText(2) 
                        + ex.getMessage());
    
    } // endif
      
    // return with error
    tbl.setValid(false);
    
    // local bad
    had_problem = true;

    // say is finished
    tbl.setFinished();
      
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    // When printing
    if  (T.getSysout() == 1) {

        // write                          
        TyBase.printMsg(TyMsg.getMsg(23) 
                        + TyMsg.getText(2) 
                        + ex.getMessage());
    
    } // endif
      
    // return with error
    tbl.setValid(false);
    
    // local bad
    had_problem = true;

    // say is finished
    tbl.setFinished();

  } // end-catch  
  
} // end-method

/**
 * Wake up the posted thread for verification
 */
protected synchronized void wakeUp() {

  // msg
  verify = true;

  // set the posted indicator
  posted = true;

  // wake up
  notify();

} // end-method

/**
 * Wake up the posted thread with a message to write
 * @param the message to write
 */
protected synchronized void wakeUp(String message) {

  // msg
  msg = message;

  // set the posted indicator
  posted = true;

  // wake up
  notify();

} // end-method

/**
 * Write an alternate message
 * @param fullMsg String - message to write
 * @return sucessful or not
 */
private boolean writeAltMsg (String fullMsg) {
  
  boolean temp = false;
    
  try {
    // write 
    temp = alt.write(fullMsg);    

  } // end-try
    
  catch (Exception e) {

    // When printing
    if  (T.getSysout() == 1) {

        // write                          
        TyBase.printMsg(TyMsg.getMsg(22)
                        + TyMsg.getText(70)
                        + TyMsg.getText(2) 
                        + e.getMessage());

    } // endif

    // say is error
    tbl.setValid(false);  
    
    // local bad
    had_problem = true;

    // error
    return false;
      
  } // end-catch  

  // When bad return
  if  (!temp) {

      // say is error
      tbl.setValid(false);  
      
      // local bad
      had_problem = true;

      // error
      return false;

  } //endif
  
  // return good
  return true;
  
} // end-method

/**
 * Write a local file message
 * @param msg String - message to write
 * @return sucessful or not
 */
private boolean writeLocalMsg (String msg) {
  
  // output definition
  FileOutputStream fileOutStream = null;
  DataOutputStream dataOutStream = null;
      
  try {
    // open and append data
    fileOutStream = new FileOutputStream(local_dir + sep + local_file, true);

  } // end-try
      
  catch (IOException e) {

    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(22)
                        + TyMsg.getText(69)
                        + TyMsg.getText(2) 
                        + e.getMessage());

    } // endif

    // say is error
    tbl.setValid(false); 
    
    // local bad
    had_problem = true;

    // error
    return false;
        
  } // end-catch  
  
  // new obj    
  dataOutStream = new DataOutputStream(fileOutStream);  
      
  try {
    // put msg
    dataOutStream.writeBytes(msg + crlf);

  } // end-try
      
  catch (IOException e) {
              
    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(22)
                        + TyMsg.getText(69)
                        + TyMsg.getText(2) 
                        + e.getMessage());

    } // endif

    // say is error
    tbl.setValid(false);  
    
    // local bad
    had_problem = true;

    // error                
    return false;

  } // end-catch                                
    
  try {
    // close
    fileOutStream.close();
    dataOutStream.close();

  } // end-try          
      
  catch (IOException e) {
              
    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(22)
                        + TyMsg.getText(69)
                        + TyMsg.getText(2) 
                        + e.getMessage());

    } // endif

    // say is error
    tbl.setValid(false); 
    
    // local bad
    had_problem = true;

    // error                
    return false;

  } // end-catch
  
  // return good
  return true;
  
} // end-method

/**
 * Add the prefix and write the message
 * @return sucessful or not 
 */ 
private boolean writeMsg () {
  
  // return bool
  boolean back = false;   

  GregorianCalendar D = new GregorianCalendar();
    
  int i_year  = D.get(Calendar.YEAR);
  int i_month = D.get(Calendar.MONTH);

  // begins at 0
  i_month++; 

  int i_day = D.get(Calendar.DATE);  
  
  int i_hours   = D.get(Calendar.HOUR_OF_DAY);            
  int i_minutes = D.get(Calendar.MINUTE);
  int i_seconds = D.get(Calendar.SECOND);

  // When using a local file
  if  (use_local) {

    String ymd = "" + i_year + "/" + i_month + "/" + i_day + " "  
            + i_hours + ":" + i_minutes + ":" + i_seconds + " ";

    // do the other write
    back = writeLocalMsg(ymd + msg);
  
    // say is finished
    tbl.setFinished();

    // go back
    return back;  

  } // endif

  // When using an alternate method
  if  (use_alt) {
      
      String ymd = "" + i_year + "/" + i_month + "/" + i_day + " "  
          + i_hours + ":" + i_minutes + ":" + i_seconds + " ";

      // do the other write
      back = writeAltMsg(ymd + msg);

      // say is finished
      tbl.setFinished();

      // go back
      return back;  

  } // endif

  // *--- dbms write ---*
  
  // the necessary database fields cannot be null
  if  (con == null || stmt == null) {
    
      // error
      tbl.setValid(false);
      
      // local bad
      had_problem = true;

      // say is finished
      tbl.setFinished();

    // done
    return false;

  } // endif
        
  String base1 = 
    "INSERT INTO ";
  
  String base2 = 
    " (Y_EAR, M_ONTH, D_AY, H_OURS, M_INUTES, S_ECONDS, M_ESSAGE) ";

  String base = base1 + dbms_LogTable + base2; 
  
  String F1 = base       
          + " VALUES ("
          + i_year + ", "
          + i_month + ", "
          + i_day + ", "
          + i_hours + ", "
          + i_minutes + ", "
          + i_seconds + ", "  
          + "'"
          + msg
          + "')";
         // + "') ;";  
  
  try {      
    // write the message
    stmt.executeUpdate(F1);
    
    // say is finished
    tbl.setFinished();
  
    // good write
    return true;
      
  } // end-try
  
  catch (SQLException ex) {

    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(23) 
                        + TyMsg.getText(2) 
                        + ex.getMessage());
    
    } // endif

    // say is error
    tbl.setValid(false);
    
    // local bad
    had_problem = true;
            
    // say is finished
    tbl.setFinished();
      
    // return with error
    return false;
      
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    // When printing
    if  (T.getSysout() == 1) {

      // write                          
      TyBase.printMsg(TyMsg.getMsg(23) 
                          + TyMsg.getText(2) 
                          + ex.getMessage());
    
    } // endif

    // say is error
    tbl.setValid(false);
    
    // local bad
    had_problem = true;
                        
    // say is finished
    tbl.setFinished();
      
    // return with error
    return false;
      
  } // end-catch 
  
} // end-method
} // end-class
