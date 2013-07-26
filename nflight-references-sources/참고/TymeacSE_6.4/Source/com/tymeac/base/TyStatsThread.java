package com.tymeac.base;

/* 
 * Copyright (c) 2002 - 2012 Cooperative Software Systems, Inc. 
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
 * Tymeac stats writter thread. Writing can block or crash, therefore, writing
 *   is done in this thread. Two stats writing methods are supported:
 *   On request and at shut down. 
 *   There is no use for this thread once it completes a request, therefore, it dies. 
 */ 
public final class TyStatsThread 
          extends Thread {
  
  // Tymeac base storage
  private final TyBase T;

  // logTable base storage
  private final TyStatsTable tbl;                 
  
  // database stuff
  private String dbms_URL         = null;   
  private String dbms_User        = null;           
  private String dbms_PassWord    = null;       
  private String dbms_StatsTable  = null;

  // SELECT delimiter
  private String delimit = null;

  // connection instance
  private  Connection con = null;

  // stmt                                                                     
  private Statement stmt = null;

  // DBMS 'insert into etc.'
  private String prefix; 

  // local file stuff
  private String local_dir  = null;
  private String local_file = null;

  // new line
  private String crlf = null;

  // separator
  private String sep = null;

  // alt stuff
  private TymeacAlternativesInterface alt = null;
  private String alt_name = null;
  
  // use local or use DBMS or use alternate
  private boolean use_local = false;
  private boolean use_dbms = false;
  private boolean use_alt = false;

  // what it says
  private DataOutputStream dataOutStream = null;

  // verified 
  private boolean table_valid = false;

  // type of stats
  //  1 - on request
  //  2 - shut
  //  3 - deactivate
  private int type_request = 0;

  // posted indicator
  private volatile boolean posted = false;

  // posted for verification indicator
  private volatile boolean verify = false; 

/**
 * Constructor for an alternate class
 * @param Ty com.tymeac.base.TyBase
 * @param table com.tymeac.base.TyLogTable
 * @param c_type_request - request or shutdown
 * @param alt com.tymeac.base.TymeacAlternativesInterface
 */
protected TyStatsThread(
              TyBase Ty, 
              TyStatsTable table, 
              int c_type_request,
              TymeacAlternativesInterface c_alt) {

  // name of this thread
  super("TymeacStatsWriter");

  // base storage
  T = Ty;

  // log table storage
  tbl = table;

  // table initially invalid
  table_valid = false;

  // not posted
  posted = false;

  // type of request
  type_request = c_type_request;  

  // use a alt class
  use_alt = true;

  // class
  alt = c_alt;
  
  // String name
  alt_name = alt.getClass().getName();

} // end-constructor

/**
 * Constructor for local file 
 * @param Ty TyBase - Tymeac base storage
 * @param table TyLogTable - the base logger class
 * @param c_type_request - request or shutdown
 * @param c_Dir String - file directory
 * @param C_File String - file name
 */
protected TyStatsThread(
            TyBase Ty, 
            TyStatsTable table, 
            int c_type_request,
            String c_dir, 
            String c_file) {

  // name of this thread
  super("TymeacStatsWriter");

  // base storage
  T = Ty;

  // log table storage
  tbl = table;

  // table initially invalid
  table_valid = false;

  // not posted
  posted = false;

  // type of request
  type_request = c_type_request;  

  // use a local file
  use_local = true;

  // local dir
  local_dir = c_dir;

  // local file
  local_file = c_file;

  // append cr/lf to the end of each line
  crlf = System.getProperties().getProperty("line.separator");

  // file separator
  sep = System.getProperties().getProperty("file.separator");     

  // When no dir
  if  (local_dir == null) {

      // get a pointer to the file
      File afile = new File(local_dir,local_file);
      
      // get the full path including the filename                 
      String tdir = afile.getAbsolutePath();
            
      // get rid of the filename
      local_dir = tdir.substring(0, tdir.length() - local_file.length());

  } // endif 

} // end-constructor

/**
 * Constructor for DBMS 
 * @param Ty TyBase - Tymeac base storage
 * @param table TyLogTable - base logger class
 * @param c_type_request - request or shutdown
 * @param c_stats String - table name
 */
protected TyStatsThread(TyBase Ty, 
                        TyStatsTable table,
                        int c_type_request,
                        String c_stats) {

  // name of this thread
  super("TymeacStatsWriter");

  // base storage
  T = Ty;

  // log table storage
  tbl = table;

  // table initially invalid
  table_valid = false;

  // not posted
  posted = false;

  // type of request
  type_request = c_type_request;  

  // use a dbms
  use_dbms = true;    

  // dmms stuff
  dbms_URL        = T.getDBURL();
  dbms_User       = T.getDBUserid();
  dbms_PassWord   = T.getDBPassword(); 
  dbms_StatsTable = c_stats;

  // delimiter
  delimit = "') ";
  //delimit = "') ;";

} // end-constructor

/**
 * Calculate server up time
 * @return
 */
private String calcDuration() {
    
  long diff = (System.currentTimeMillis() - T.getStart_time()) / 1000;
  long hh1 = diff / 36000;
  long mm1 = diff - (hh1 * 36000);
  long mm2 = mm1 / 60;
  long ss1 = mm1 - (mm2 * 60);
  
  // hh:mm:ss
  return hh1 + ":" + mm2 + ":" + ss1;  
  
} // end-method

/**
 * Top level decision maker 
 */
private void doStats ( ) {   
  
  // When a local file
  if  (use_local) {

      // do the write 
      writeLocalStats();

      // done
      return;

  } // endif  

  // When alt
  if  (use_alt) {

      // do the write
      writeAltStats();

      // done
      return;

  } // endif  

  // do the write
  writeDBMSStats();
  
} // end-method

/**
 * Verify the request
 * @return boolean
 */
private boolean doVerify ( ) {
  
  // When using a local file
  if  (use_local) {
    
      // call verify and set the return value       
      table_valid = verifyLocal();    
  } 
  else {
      // When using an alt method
      if  (use_alt) {

          // call verify and set the return value       
          table_valid = verifyAlt();  
      }
      else {
          // When using a dbms
          if  (use_dbms) {

              // call verify and set the return value       
              table_valid = verifyDBMS();

          } // endif
      } // endif
  } // endif

  // done
  return table_valid;
  
} // end-method

/**
 * Form the function message and get all the function data
 * @param ymd String - year, month, day
 * @return boolean 
 */ 
private boolean formFunc (String ymd) {
  
  // Function base
  FuncHeader Functions = T.getFunc_tbl();
  
  // function detail 
  FuncDetail func;  
  
  // number of functions
  int nbr_func = Functions.getNumb(); 
  
  // function name
  String func_name; 
  
  // times this function was used
  long times_used;    

  // loop thru all the functions
  for (int i = 0; i < nbr_func; i++) {
      
    // get next function detail
    func = Functions.getEntry(i);
    
    // When null, something went wrong
    if  (func == null) return false;
    
    // get the name and times used
    func_name  = func.getName();
    times_used = func.getUsed();        
        
    // fomm message
    String msg = "[FUNC]("
          + func_name
          + ")("
          + times_used
          + ")";
   
    // When a local file
    if  (use_local) {

        // When write failed, get out
        if  (!writeLocal(ymd + msg)) return false;
        
    } // endif

    // When alt
    if  (use_alt) {

        // When write failed, get out
        if  (!writeAlt(ymd + msg)) return false;
        
    } // endif

    // When dbms
    if  (use_dbms) {

        // When write failed, get out
        if  (!writeDBMS(ymd + msg)) return false;
        
    } // endif
  } // end-for  

  // good
  return true;      
    
} // end-method

/**
 * Form the list of queues message and get all the queue data
 * @param ymd String - year, month, day
 * @return boolean
 */ 
private boolean formMain (String ymd) {   

  AreaBase  area = null;  // header entry
  
  int nbr_que;      // number of queues
  int tot_average;  // total times average
  int tot_individ;  // total times individual
  int tot_overall;  // total times overall
  int tot_overflow;  // total times overflowed and started new thread
  int tot_no_busy;  // total times no other thread busy and started new thread
  
  String que_name;     // name of this queue
  
  int tot_req;      // total requests
  int tot_waits;    // total wait time-outs
  int tot_posts;    // total times posted
  int tot_starts;   // total times stared
  int tot_new;      // total new instance
  
  int tot_caught;   // total caught problem
  int tot_wrong;    // total wrong instance
  
  int nbr_wl;       // number of waitlists
  int nbr_in_wl;    // number of entries in each waitlist
  int nbr_threads;  // number of threads
  int que_type;     // type of que 0=normal, 1=OA 
  
  AreaThreadMgmt  detail = null; // detail entry

  int tot_used;    // total times used
  int tot_hi_used; // total high used    
  int tot_reset;   // total times reset
  int pri_over;    // total times overflow primary
  int sec_over;    // total times overflow secondary   
  
  int i, j, k, seq, max;
  
  String que1 = "[QUE1](";
  String que2 = "[QUE2](";
  String que3 = "[QUE3](";
  
  String msg; 
  
  // get total number of queues in the system
  nbr_que = T.getMain_tbl().getNbrEntries();
  
  // get each que in the system
  for  (i = 0; i < nbr_que; i++) {
      
    // get next queue
    area = T.getMain_tbl().getArea(i);
    
    // When null, something went wrong
    if  (area == null)  return false;
    
    // do the stats on this queue
    
    // get the que fields
    que_name    = area.getName();         // name of this queue
    nbr_wl      = area.getWaitlist().getNbrWl();        // number of waitlists
    nbr_in_wl   = area.getWaitlist().getNbrPhysicalEntries();  // number in each waitlist
    que_type    = (area.getQueType() == true)? 0 : 2;      // que type
    nbr_threads = area.getThreadsAnchor().getNbrThreads();   // number of threads 
    tot_average = area.getTotAverage();   // average times
    tot_individ = area.getTotIndivid();   // individual times
    tot_overall = area.getTotOverall();   // overall times
    tot_overflow = area.getTotOverflow(); // overflowed times
    tot_no_busy = area.getTotNoBusy();    // none busy times          

    // write the top part
    msg = que1 
      + que_name
      + ")("
      + que_type
      + ")("
      + nbr_threads
      + ")("
      + nbr_wl
      + ")("
      + nbr_in_wl
      + ")("
      + tot_overall
      + ")("
      + tot_individ
      + ")("
      + tot_average
      + ")("
      + tot_overflow
      + ")("
      + tot_no_busy        
      + ")";
    
    // When a local file
    if  (use_local) {

        // When write failed, error
        if  (!writeLocal(ymd + msg)) return false;
        
    } // endif

    // When alt
    if  (use_alt) {

        // When write failed, error
        if  (!writeAlt(ymd + msg)) return false;
        
    } // endif

    // When dbms
    if  (use_dbms) {

        // When write failed, error
        if  (!writeDBMS(ymd + msg)) return false;
        
    } // endif
              
    // do all the thread details
    for  (j = 0; j < nbr_threads; j++) {
        
      // get a detail
      detail = area.getThreadsAnchor().getNextEntry(j);
    
      // When null, something went wrong
      if  (detail == null)   return false;
    
      // get the detail data
      tot_req     = detail.getTotProc();  
      tot_waits   = detail.getTotWaits();     
      tot_posts   = detail.getTotPosted();      
      tot_starts  = detail.getTotStarted();                 
      tot_new     = detail.getTotNew();      
      tot_caught  = detail.getTotCaught();
      tot_wrong   = detail.getTotWrongInstance();
    
      // write the thread part
      msg = que2 
          + que_name
          + ")("
          + j
          + ")("
          + tot_req
          + ")("
          + tot_waits
          + ")("
          + tot_posts
          + ")("
          + tot_starts
          + ")("
          + tot_new
          + ")("
          + tot_caught
          + ")("
          + tot_wrong              
          + ")";
            
      // When a local file
      if  (use_local) {

          // When write failed, error
          if  (!writeLocal(ymd + msg)) return false;
        
      } // endif

      // When alt
      if  (use_alt) {

          // When write failed, error
          if  (!writeAlt(ymd + msg)) return false;
        
      } // endif

      // When dbms
      if  (use_dbms) {

          // When write failed, error
          if  (!writeDBMS(ymd + msg)) return false;
        
      } // endif
    } // end-for 
          
    // returned object from the wait list anchor
    TyWLElements[] twe = area.getWaitlist().getDisplayable();
          
    // do all the waitlists
    for  (k = 0, seq = 1, max = twe.length; k < max; k++, seq++) {   
        
      // get info for this guy
      tot_used    = twe[k].used;
      tot_hi_used = twe[k].hi_used;
      tot_reset   = twe[k].reset;
      pri_over    = twe[k].over_p;
      sec_over    = twe[k].over_s;
    
      // write the waitlist part
      msg = que3 
          + que_name
          + ")("
          + seq
          + ")("
          + tot_used
          + ")("
          + tot_hi_used
          + ")("
          + tot_reset
          + ")("
          + pri_over
          + ")("
          + sec_over
          + ")";
            
      // When a local file
      if  (use_local) {

          // When write failed, error
          if  (!writeLocal(ymd + msg)) return false;
        
      } // endif

      // When alt
      if  (use_alt) {

          // When write failed, error
          if  (!writeAlt(ymd + msg)) return false;
        
      } // endif

      // When dbms
      if  (use_dbms) {

          // When write failed, error
          if  (!writeDBMS(ymd + msg)) return false;
        
      } // endif                    
    } // end-for
  } // end-for

    // good
    return true;
    
} // end-method

/**
 * Form the stall message and get all the stalled requests data
 * @param ymd String - year, month, day
 * @return boolean
 */ 
private boolean formStall (String ymd) {

  int nbr_busy; 
  
  // number busy
  nbr_busy = T.getStall_tbl().getBusy();
  
  // When none are used, bye
  if  (nbr_busy == 0) return true;
  
  long stl_id = 0;
  
  String stl1 = "[STL1]("
            + T.getStart_time()
            + ")(";
            
  String stl2 = "[STL2]("
            + T.getStart_time()
            + ")(";
    
  String msg; 
  
  // write the top message
  msg = stl1
      + nbr_busy
      + ")";
  
    // When a local file
    if  (use_local) {

        // When write failed, get out
        if  (!writeLocal(ymd + msg)) return false;
        
    } // endif

    // When alt
    if  (use_alt) {

        // When write failed, get out
        if  (!writeAlt(ymd + msg)) return false;
        
    } // endif

    // When dbms
    if  (use_dbms) {

        // When write failed, get out
        if  (!writeDBMS(ymd + msg)) return false;
          
    } // endif

  // list
  ArrayList temp_stall;
  
  // stall detail
  StallDetail s_detail;
    
  // get the list of used entries
  temp_stall = T.getStall_tbl().getNextUsed();
  
  nbr_busy = temp_stall.size();
   
  // do all the busy entries
  for  (int i = 0; i < nbr_busy; i++) { 
      
    // get the next busy
    s_detail = ((StallDetail)temp_stall.get(i));              
    
    // get the name
    stl_id = s_detail.getAtName();  
          
    // When not there, ignore
    if  (stl_id >= 0) {
        
        // write the message
        msg = stl2
          + stl_id
          + ")";
                  
        // When a local file
        if  (use_local) {

            // When write failed, get out
            if  (!writeLocal(ymd + msg)) return false;
          
        } // endif

        // When alt
        if  (use_alt) {

            // When write failed, get out
            if  (!writeAlt(ymd + msg))  return false;
                      
        } // endif

        // When dbms
        if  (use_dbms) {

            // When write failed, get out
            if  (!writeDBMS(ymd + msg)) return false;
          
        } // endif                 
    } // endif            
  } // end-for 

    // good
    return true;
  
} // end-method

/**
 * wait for activation, do the stats, and die
 */
public void run() {
    
  // wait for activation
  synchronized (this) {
          
      // wait for a posting 
      while (!posted) {
        
        try {
            // wait for a post
            wait();

        } // end-try

        catch (InterruptedException e) {

        } // end-catch
      } // end-while

      // reset the posted indicator
      posted = false;
  
  } // end-sync

  // When a verify
  if  (verify) {

      // reset
      verify = false;

      // set what came back
      tbl.setValid(doVerify());
  }   
  else {
      // do it
      doStats();

  } // endif

  // say is finished
  tbl.setFinished();
    
  // die

} // end-method

/**
 * Verify that the alt stats class works
 * @return boolean does or not
 */
private boolean verifyAlt () {
  
  try {    
    // call verify       
    return alt.verify();  
    
  } // end-try
  catch (Exception e) {
    
    // When printing
    if  (T.getSysout() == 1) {

        // write                          
        TyBase.printMsg(TyMsg.getMsg(55)
                        + alt_name
                        + TyMsg.getText(85) 
                        + e.getMessage());
    
    } // endif
    
    // no good
    return false;
    
  } // end-catch
  
} // end-method

/**
 * verify the dbms data
 * @return boolean
 */
private boolean verifyDBMS ( ) {
  
  // Select dummy                   
  String rest = "SELECT Y_EAR FROM " 
              + dbms_StatsTable; 
            //  + ";";    

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

    // try to access
    stmt.executeQuery(rest);
  
    // all done
    stmt.close();
    con.close();
    
  } // end-try

  catch (SQLException ex) {

    // write                          
    msgOut(TyMsg.getMsg(25)
            + TyMsg.getText(2) 
            + ex.getMessage(), 10);
  
    // return with error
    return false;
    
  } // end-catch

  catch (java.lang.Exception ex)  {

    // write                          
    msgOut(TyMsg.getMsg(25)
             + TyMsg.getText(2) 
             + ex.getMessage(), 10);
  
    // return with error
    return false;
     
  } // end-catch 

  // good
  return true;
  
} // end-method

/**
 * Using a local file -- just open it to see if its ok  
 * @return boolean
 */
private boolean verifyLocal () {
    
  // output definition
  FileOutputStream fileOutStream = null;        
    
  try {
    // open and append data
    fileOutStream = new FileOutputStream(local_dir + sep + local_file, true);

  } // end-try
      
  catch (IOException e) {

    // write                          
    msgOut(TyMsg.getMsg(24)
                      + TyMsg.getText(69)
                      + e.getMessage(), 10);

    // failure
    return false;
        
  } // end-catch
    
  try {
    // close
    fileOutStream.close();

  } // end-try          
      
  catch (IOException e) {

    // write                          
    msgOut(TyMsg.getMsg(24)
          + TyMsg.getText(69)
          + TyMsg.getText(2) 
          + e.getMessage(), 10);

    // failure                
    return false;

  } // end-catch

  // good
  return true;          
      
} // end-method

/**
 * Wake up the posted thread for verify
 */
protected synchronized void verifyWakeUp() {

  // for verification
  verify = true;
  
  // set the posted indicator
  posted = true;

  // wake up
  notify();

} // end-method

/**
 * Wake up the posted thread 
 */
protected synchronized void wakeUp() {

  // set the posted indicator
  posted = true;

  // wake up
  notify();

} // end-method

/**
 * Write to an alternate facility
 * @param msg String - the message
 * @return boolean
 */ 
private boolean writeAlt (String msg) {
      
  try {
    // When came back error
    if  (!alt.write(msg)) {

        // When printing
        if  (T.getSysout() == 1) {

            // write                          
            msgOut(TyMsg.getMsg(24) + TyMsg.getText(70), 10);

        } // endif

        // no good
        tbl.setValid(false);

        // error                
        return false;

    } // endif
  } // end-try
  
  catch (Exception e) {

    // write                          
    msgOut(TyMsg.getMsg(24)
             + TyMsg.getText(70)
             + TyMsg.getText(2) 
             + e.getMessage(), 10);

    // no good
    tbl.setValid(false);

    // error                
    return false;

  } // end-catch 

  // good
  return true;
    
} // end-method

/**
 * Top line stats writer for an alt faciltiy:
 *   Get the year, month day 
 *   Put out the header
 *   Call the individual stats elements
 *   Put out the trailer 
 */
private void writeAltStats () { 
  
  // todays date    
  GregorianCalendar D = new GregorianCalendar();  
  
  int i_year  = D.get(Calendar.YEAR);
  int i_month = D.get(Calendar.MONTH);

  // month begins at 0
  i_month++;    

  int i_day = D.get(Calendar.DATE);  
  
  int i_hours   = D.get(Calendar.HOUR_OF_DAY);
  int i_minutes = D.get(Calendar.MINUTE);
  int i_seconds = D.get(Calendar.SECOND);

  String ymd = "" + i_year + "/" + i_month + "/" + i_day + " "  
            + i_hours + ":" + i_minutes + ":" + i_seconds + " ";

  String msg1 = null, msg2 = null;
  
  // What type of requesrt
  switch (type_request) {

    case 1: // is on request
        msg1 = "[ONRS](" 
            + T.getStart_time()
            + ")";
          
        msg2 = "[ONRE](" 
            + T.getStart_time()
            + ")"
            + "("
            + calcDuration()
            + ")";

        // done
        break;

    case 2: // shut down
        msg1 = "[FINS](" 
            + T.getStart_time()
            + ")";
          
        msg2 = "[FINE](" 
            + T.getStart_time()
            + ")"
            + "("
            + calcDuration()
            + ")";

        // done
        break;

    case 3: // deactivation
        msg1 = "[ACTS](" 
            + T.getStart_time()
            + ")";
          
        msg2 = "[ACTE](" 
            + T.getStart_time()
            + ")"
            + "("
            + calcDuration()
            + ")";

        // done
        break;  
  
  } // end-switch       
    
  // When write top failed, get out
  if  (!writeAlt(ymd + msg1)) return;

  // When write the functions failed, get out  
  if  (!formFunc(ymd)) return;

  // When write the queues failed, get out   
  if  (!formMain(ymd)) return;

  // When write the stats failed, get out   
  if  (!formStall(ymd)) return;

  // When write bottom failed, get out
  if  (!writeAlt(ymd + msg2)) return;
               
} // end-method

/**
 * Write to a DBMS facility
 * @param msg String - the message
 * @return boolean
 */ 
private boolean writeDBMS (String msg) {
  
  try {
    // write it           
    stmt.executeUpdate(prefix + msg+ delimit);
  
  } // end-try

  catch (SQLException ex) {

    // write                          
    msgOut(TyMsg.getMsg(25)
                      + TyMsg.getText(2) 
                      + ex.getMessage(), 10);

    // no good
    tbl.setValid(false);
  
    // return 
    return false;
    
  } // end-catch

  catch (java.lang.Exception ex)  {

    // write                          
    msgOut(TyMsg.getMsg(25)
                      + TyMsg.getText(2) 
                      + ex.getMessage(), 10);

    // no good
    tbl.setValid(false);

    // return 
    return false;
    
  } // end-catch 
    
  // good 
  return true;          
    
} // end-method

/**
 * Top line stats writer for a DBMS faciltiy:
 *   Get the year, month day 
 *   Put out the header
 *   Call the individual stats elements
 *   Put out the trailer
 */
private void writeDBMSStats ( ) { 
  
  // todays date    
  GregorianCalendar D = new GregorianCalendar();
    
  int i_year  = D.get(Calendar.YEAR);
  int i_month = D.get(Calendar.MONTH);

  // month begins at 0
  i_month++;    

  int i_day = D.get(Calendar.DATE);  
  
  int i_hours   = D.get(Calendar.HOUR_OF_DAY);
  int i_minutes = D.get(Calendar.MINUTE);
  int i_seconds = D.get(Calendar.SECOND);
  
  String msg1 = null, msg2 = null;
  
  // What type of requesrt
  switch (type_request) {

    case 1: // is on request
        msg1 = "[ONRS](" 
            + T.getStart_time()
            + ")')";        
          
        msg2 = "[ONRE](" 
            + T.getStart_time()
            + ")("
            + calcDuration() 
            + ")')";        

        // done
        break;

    case 2: // shut down
        msg1 = "[FINS](" 
            + T.getStart_time()
            + ")')";
          
        msg2 = "[FINE](" 
            + T.getStart_time()
            + ")("
            + calcDuration() 
            + ")')";    

        // done
        break;

    case 3: // deactivation
        msg1 = "[ACTS](" 
            + T.getStart_time()
            + ")')";
          
        msg2 = "[ACTE](" 
            + T.getStart_time()
            + ")("
            + calcDuration() 
            + ")')";    

        // done
        break;  
  
  } // end-switch    
    
  String base1 = 
    "INSERT INTO ";
    
  String base2 = " (Y_EAR, M_ONTH, D_AY, H_OURS, M_INUTES, S_ECONDS, M_ESSAGE) ";

  String base3 = " VALUES ("
            + i_year + ", "
            + i_month + ", "
            + i_day + ", "
            + i_hours + ", "
            + i_minutes + ", "
            + i_seconds + ", "  
            + "'";  

  prefix = base1 + dbms_StatsTable + base2 + base3;
    
  String Start = prefix + msg1;
  String End   = prefix + msg2;   
  
  try {      
    // When no name use single parm constructor
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
      
    // get a stmt                                                                     
    stmt = con.createStatement();
      
    // write the start message
    stmt.executeUpdate(Start);
      
    // no ymd needed here
    String none = "";

    // write the system stats   
    if  (formFunc(none)) {

        // next one
        if  (formMain(none)) {

            // last one
            formStall(none);

        } // endif
    } // endif
      
    // write the end message
    stmt.executeUpdate(End);
      
    // all done
    stmt.close();
    con.close();
        
  } // end-try
  
  catch (SQLException ex) {

    // write                          
    msgOut(TyMsg.getMsg(25)
                    + TyMsg.getText(2) 
                    + ex.getMessage(), 10);

    // no good
    tbl.setValid(false);
      
    // return 
    return;
      
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    // write                          
    msgOut(TyMsg.getMsg(25)
                      + TyMsg.getText(2) 
                      + ex.getMessage(), 10);

    // no good
    tbl.setValid(false);

    // return 
    return;
      
   } // end-catch 
  
} // end-method

/**
 * Write to a local facility
 * @param msg String - the message
 * @return boolean
 */ 
private boolean writeLocal (String msg) {
  
  try {
    // put local
    dataOutStream.writeBytes(msg + crlf);

  } // end-try
  
  catch (IOException e) {

      // write                          
      msgOut(TyMsg.getMsg(24)
                        + TyMsg.getText(69)
                        + TyMsg.getText(2) 
                        + e.getMessage(), 10);

    // no good
    tbl.setValid(false);

    // error                
    return false;

  } // end-catch

  // good
  return true;
    
} // end-method

/**
 * Top line stats writer for a local faciltiy:
 *   Get the year, month day 
 *   Put out the header
 *   Call the individual stats elements
 *   Put out the trailer
 */
private void writeLocalStats () { 
  
  // todays date    
  GregorianCalendar D = new GregorianCalendar();  
  
  int i_year  = D.get(Calendar.YEAR);
  int i_month = D.get(Calendar.MONTH);

  // month begins at 0
  i_month++;    

  int i_day = D.get(Calendar.DATE);  
  
  int i_hours   = D.get(Calendar.HOUR_OF_DAY);
  int i_minutes = D.get(Calendar.MINUTE);
  int i_seconds = D.get(Calendar.SECOND);

  String ymd = "" + i_year + "/" + i_month + "/" + i_day + " "  
            + i_hours + ":" + i_minutes + ":" + i_seconds + " ";

  String msg1 = null, msg2 = null;
  
  // What type of requesrt
  switch (type_request) {

    case 1: // is on request
        msg1 = "[ONRS](" 
            + T.getStart_time()
            + ")";
          
        msg2 = "[ONRE](" 
            + T.getStart_time()
            + ")"
            + "("
            + calcDuration()
            + ")";

        // done
        break;

    case 2: // shut down
        msg1 = "[FINS](" 
            + T.getStart_time()
            + ")";
          
        msg2 = "[FINE](" 
            + T.getStart_time()
            + ")"
            + "("
            + calcDuration()
            + ")";

        // done
        break;

    case 3: // deactivation
        msg1 = "[ACTS](" 
            + T.getStart_time()
            + ")";
          
        msg2 = "[ACTE](" 
            + T.getStart_time()
            + ")"
            + "("
            + calcDuration()
            + ")";

        // done
        break;  
  
  } // end-switch       
    
  // output definition
  FileOutputStream fileOutStream = null;  
    
  try {
    // open and append data
    fileOutStream = new FileOutputStream(local_dir + sep + local_file, true);

  } // end-try
      
  catch (IOException e) {

    // write                          
    msgOut(TyMsg.getMsg(24)
              + TyMsg.getText(69)
              + TyMsg.getText(2) 
              + e.getMessage(), 10);

    // no good
    tbl.setValid(false);

    // bad
    return;
        
  } // end-catch  
  
  // what it says
  dataOutStream = new DataOutputStream(fileOutStream);  

  // When write fails, get out
  if  (!writeLocal(ymd + msg1)) return;

  // When write fails, get out
  if  (!formFunc(ymd)) return;

  // When write fails, get out
  if  (!formMain(ymd))  return;

  // When write fails, get out
  if  (!formStall(ymd))  return;

  // When write fails, get out
  if  (!writeLocal(ymd + msg2)) return;
  
  try {
    fileOutStream.close();
    dataOutStream.close();

  } // end-try          
    
  catch (IOException e) {

    // write                          
    msgOut(TyMsg.getMsg(24)
            + TyMsg.getText(69)
            + TyMsg.getText(2) 
            + e.getMessage(), 10);
    
    // no good
    tbl.setValid(false);    
    
    // bad
    return;

  } // end-catch
      
} // end-method

/**
 * Write a log and print
 * @param msg
 * @param time
 */
private void msgOut(String msg, int time) {
  
  // When printing
  if  (T.getSysout() == 1) TyBase.printMsg(msg);
  
  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(msg, time);
  
} // end-method 
} // end-class
