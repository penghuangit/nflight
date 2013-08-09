package com.tymeac.base;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.net.*;

/**
 * Tymeac statistics writing may be thru a DBMS, a local file, or a user method
 */
public final class TyStatsTable {
  
  // Tymeac base storage    
  private final TyBase  T; 

  // thread obj
  private volatile TyStatsThread writer;

  // use local DBMS or alternate
  private boolean use_local  = false;
  private boolean use_dbms   = false;
  private boolean use_alt    = false;
  
  // database table name
  private String dbms_StatsTable  = null;

  // local file stuff
  private String local_dir  = null;
  private String local_file = null;

  // alternatives stuff
  private String alt_name;
  private TymeacAlternativesInterface alt = null;

  // type of stats
  //    0 - verify
  //    1 - on request
  //    2 - shut
  //    3 - deactivate
  private int type_request = 0;

  // max time to wait
  private int sec = 0;
  
  // verified
  private boolean table_valid = false;

  // finished
  private volatile boolean busy = false; 

/**
 * Constructor for an alternate facility
 * @param Ty com.tymeac.base.TyBase - tymeac base storage
 * @param AltName String - alternate class
 */
protected TyStatsTable(TyBase Ty, String AltName) {

  // Tymeac base storage
  T = Ty;

  // using a alt
  use_alt = true;
  
  // save
  alt_name = AltName;

  // class and url
  ClsUrl  cls_url = null;

  // work
  boolean isUrl = false;  

  // separate the Class from the URL
  cls_url = TyFormat.doClass(AltName);
                              
  // what came back
  switch (cls_url.getResult()) {
      
    case 1:
      // done 
      break;
    
    case 2:
      // is a url
      isUrl = true;

      // done 
      break;

    case 0:
    case 3:
      // error msg
      msgOut(TyMsg.getMsg(73)
            + AltName
            + TyMsg.getText(28), 10);  

      // done 
      return;

    case 4:
      // bad url format
      msgOut(TyMsg.getMsg(73)
            + AltName
            + TyMsg.getText(29), 10);

      // done
      return;
          
  } // end-switch

  // When a url
  if  (isUrl) {
  
      // get the url class loader
      URLClassLoader urloader = new URLClassLoader(cls_url.getUrlName());
  
      try {
        // load the sucker
        alt = (TymeacAlternativesInterface) urloader.loadClass(cls_url.getClassName()).newInstance(); 
      
      } // end-try
  
      catch (ClassNotFoundException e) {         
        
        // print
        msgOut(TyMsg.getMsg(55)
              + AltName
              + TyMsg.getText(97), 10);
                  
      } // end-catch 
  
      catch (InstantiationException e) {

        // print
        msgOut(TyMsg.getMsg(55)
            + AltName
            + TyMsg.getText(98), 10);

      } // end-catch  
  
      catch (IllegalAccessException e) {  
  
        // print
        msgOut(TyMsg.getMsg(55)
              + AltName
              + TyMsg.getText(99), 10);
  
      } // end-catch  
      
      catch (Exception  e) {
        
        // print
        msgOut(TyMsg.getMsg(55)
              + AltName
              + TyMsg.getText(100)
              + e.getMessage(), 10);
        
      } // end-catch
  
      // done
      return;
  
  } // endif

  // *--- get the class definition and a new instance thereof ---*
  try {
    // load the class 
    alt = (TymeacAlternativesInterface)Class.forName(AltName).newInstance();
    
  } // end-try
  
  catch (ClassNotFoundException e) { 

    // print
    msgOut(TyMsg.getMsg(55)
              + AltName
              + TyMsg.getText(98), 10);
              
  } // end-catch 
  
  catch (InstantiationException e) {

    // print
    msgOut(TyMsg.getMsg(55)
            + AltName
            + TyMsg.getText(99), 10);

  } // end-catch  
  
  catch (IllegalAccessException e) { 

    // print
    msgOut(TyMsg.getMsg(55)
            + AltName
            + TyMsg.getText(100), 10);

  } // end-catch  
  
  catch (Exception  e) {

    // print
    msgOut(TyMsg.getMsg(55)
              + AltName
              + TyMsg.getText(85)
              + e.getMessage(), 10);

  } // end-catch

} // end-constructor

/**
 * Constructor for local file 
 * @param Ty TyBase - Tymeac base storage
 * @param dir java.lang.String - file directory
 * @param file java.lang.String - file name
 */
protected TyStatsTable(TyBase Ty, String dir, String file) {

  // Tymeac base storage
  T = Ty;

  // not verified
  table_valid = false; 

  // using a local file
  use_local = true;

  // local file name
  local_file = file;
                          
  // local dir name
  local_dir = dir;

} // end-constructor

/**
 * Constructor for DBMS 
 * @param Ty TyBase - Tymeac base storage
 * @param tbl String - table name
 */ 
protected TyStatsTable (String tbl, TyBase Ty) {
  
  // Tymeac base storage
  T = Ty;                     
  
  // not verified
  table_valid = false; 

  // using a dbms
  use_dbms= true;
   
  // DBMS stuff
  dbms_StatsTable = tbl;
    
} // end-constructor

/**
 * Create the thread and wake it up
 * @return int
 */ 
private int doRequest () { 

  // single request at a time
  synchronized (T.getStats_tbl()) {

    // When not there, create and start it
    if  (writer == null) startWriter();
    
    synchronized (this) {
    
      try {
        // is busy
        busy = true;
      
        // wake up the thread 
        writer.wakeUp();
  
        // until finished or timed out
        while (busy) {
    
          try {
            // until complete or timed out
            wait(sec * 1000);
  
          } // end-try
      
          catch (InterruptedException e) {
          } // end-catch
                
        } // end-while 
      } // end-try    
    
      catch (java.lang.Exception ex)  {
  
        // When on-request
        if  (type_request == 1) {
            
            // log
            msgOut(TyMsg.getMsg(3120) + ex.getMessage(), 10);                 
    
            // stats not in use       
            table_valid = false;         
    
        } // endif
        
        // no longer needed
        writer = null;
        
        // return with error
        return 3;
        
      } // end-catch 
    } // end-sync
  } // end-sync

  // no longer needed
  writer = null;

  // When the table is not available, nothing more here.
  if  (!table_valid) return 3;

  // When timed out
  if  (busy) return 3;
  
  // ok
  return 4;
  
} // end-method

/**
 * return the type of stats
 * @return
 */
protected int getType() {
  
  // When dbms
  if  (use_dbms) {
      
      // is so
      return 1;
  }
  else {
      // When local
      if  (use_local) {
        
          // is so
          return 2;
      }
      else {
          // must be alt
          return 3;
      
      } // endif
  } // endif
  
} // end-method 

/**
 * Used for the new stats maint.
 * @return TyRunTimeElements
 */
protected TyRunTimeElements getElements() {
  
  TyRunTimeElements ele = new TyRunTimeElements();
  
  // When Not in effect
  if  (!T.isStatsUsed()) {
      
      // say is not in use
      ele.setInUse(false);
      
      return ele;
    
  } // endif
  
  switch (getType()) {
  
    case 1: // dmbs
      
      ele.setDBMS(dbms_StatsTable);
      return ele;
      
    case 2: // local file
      
      ele.setDir(local_dir);
      ele.setFileName(local_file);
      return ele;
  
    case 3: // alt class
      
      ele.setAlt(alt_name);
      return ele;
      
    default: return ele;
  
  } // end-switch
  
} // end-method

/**
 * return the name of the stats
 * @return
 */
protected String getName() {
  
  // When dbms
  if  (use_dbms) {
      
      // is so
      return dbms_StatsTable;
  }
  else {
      // When local
      if  (use_local) {
        
          // is so
          return local_file;
      }
      else {
          // must be alt
          return alt_name;
      
      } // endif
  } // endif
  
} // end-method

/**
 * stats on-request 
 * @return int
 */ 
protected int onReqStats () { 
    
  // sleep time
  sec = 15;

  // type of req is on-req
  type_request = 1;

  // do request and return with result
  return doRequest();
    
} // end-method

/**
 * executed from the thread 
 */
protected synchronized void setFinished() {

  busy = false;
  
  notify();

} // end-method

/**
 * set what happened during validation (executed from the thread)
 * @param what boolean - reason code
 */
protected void setValid(boolean what) {

  table_valid = what;

} // end-method

/**
 * Create and start the writer thread
 */
private void startWriter() {

  // When local
  if  (use_local) {

      // new thread
      writer = new TyStatsThread( T, 
                                  this,
                                  type_request,
                                  local_dir,
                                  local_file);
  }
  else {
      // When DBMS
      if  (use_dbms) {

          // new thread
          writer = new TyStatsThread( T, 
                                      this,
                                      type_request,
                                      dbms_StatsTable);
      }
      else {
          // is alt
          writer = new TyStatsThread( T, 
                                      this,
                                      type_request,
                                      alt);
      } // endif
  } // endif

  // start it
  writer.start();
  
} // end-method  

/**
 * Verify that the table is valid
 * @return boolean
 * @param sec int - seconds to wait
 */
protected boolean verify (int sec) {

  // when alt
  if  (use_alt) {

      // When the load failed
      if  (alt == null) {

          // log not in use       
          table_valid = false;

          // done
          return false;

      } // endif
  } // endif

  // single request
  synchronized (T.getStats_tbl()) {

    // When not there, create and start it
    if  (writer == null) startWriter();
    
    synchronized (this) {
    
      try {
        // is busy
        busy = true;
      
        // wake up the thread 
        writer.verifyWakeUp();
  
        // until finished or timed out
        while (busy) {
    
          try {
            // until complete or timed out
            wait(sec * 1000);
  
          } // end-try
      
          catch (InterruptedException e) {
          } // end-catch
                
        } // end-while 
      } // end-try    
  
      catch (java.lang.Exception ex)  {
    
        // log not in use       
        table_valid = false;
        
        // no longer needed
        writer = null;
        
        // return with error
        return false;
  
      } // end-catch 
    }// end-sync
  } // end-sync

  // no longer needed
  writer = null;

  // When the table is not available, nothing more here.
  if  (!table_valid) return false;

  // When timed out
  if  (busy) return false;  
  
  // ok
  return true;
  
} // end-method

/**
 * Stats from an Activation de-activate  
 */
protected void writeActStats () { 
      
  // sleep time
  sec = 45;

  // type of req is deactivation
  type_request = 3; 

  // do request and return 
  doRequest();
    
} // end-method

/**
 * Shut down stats 
 * @return boolean
 */
protected boolean writeStats () {  
  
  // sleep time
  sec = 45;
  
  // type of req is shutdown
  type_request = 2; 

  // do request and return 
  if  (doRequest() == 4) {

      // good
      return true;
  }
  else {
      // ng
      return false;

  } // endif    
  
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
