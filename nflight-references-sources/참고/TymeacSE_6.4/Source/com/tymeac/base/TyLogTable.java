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

import java.net.*;

/**
 * Tymeac logging may be thru a DBMS, a local file, or a user method
 */
public final class TyLogTable {
  
  // Tymeac base storage
  private final TyBase T;

  // thread obj
  private volatile TyLogThread logger = null;                
  
  // database stuff      
  private String dbms_LogTable = null;

  // local file stuff
  private String local_dir  = null;
  private String local_file = null;

  // alternatives stuff
  private String alt_name = null;
  private TymeacAlternativesInterface alt = null;
  
  // use local DBMS or alternate
  private boolean use_local = false;
  private boolean use_dbms  = false;
  private boolean use_alt   = false;

  // verified 
  private volatile boolean table_valid = false;

  // working  
  private volatile boolean  busy = false;

/**
 * Constructor for an alternate facility
 * @param Ty com.tymeac.base.TyBase - tymeac base storage
 * @param AltName String - alternate class
 */
protected TyLogTable(TyBase Ty, String AltName) {

  // Tymeac base storage
  T = Ty;

  // using a alt
  use_alt = true;
  
  // save name
  alt_name = AltName;

  // class and url
  ClsUrl  cls_url = null;

  // work
  boolean isUrl = false;
  
  String error = null;
    
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
             error = TyMsg.getMsg(72)
                  + AltName
                  + TyMsg.getText(28);                            

            // When printing, do so
            if  (T.getSysout() == 1) {

                // print error
                TyBase.printMsg(error);  

            } // endif

            // done 
            return;

        case 4:
            // bad url format
             error = TyMsg.getMsg(72)
                  + AltName
                  + TyMsg.getText(29);

            // When printing, do so
            if  (T.getSysout() == 1) {

                // print error
                TyBase.printMsg(error);  

            } // endif

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

          // When printing
          if  (T.getSysout() == 1) { 

              // print
              TyBase.printMsg(TyMsg.getMsg(53)
                                + AltName
                                + TyMsg.getText(98));
          } // endif
      } // end-catch 

      catch (InstantiationException e) {

          // When printing
          if  (T.getSysout() == 1) { 

              // print
              TyBase.printMsg(TyMsg.getMsg(53)
                                + AltName
                                + TyMsg.getText(99));

          } // endif
      } // end-catch  

      catch (IllegalAccessException e) {  

          // When printing
          if  (T.getSysout() == 1) { 

              // print
              TyBase.printMsg(TyMsg.getMsg(53)
                                + AltName
                                + TyMsg.getText(100));

          } // endif
      } // end-catch  

      catch (Exception  e) {

          // When printing
          if  (T.getSysout() == 1) { 

              // print
              TyBase.printMsg(TyMsg.getMsg(53)
                                + AltName
                                + TyMsg.getText(85)
                                + e.getMessage());

          } // endif
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

      // When printing
      if  (T.getSysout() == 1) { 

          // print
          TyBase.printMsg(TyMsg.getMsg(53)
                    + AltName
                    + TyMsg.getText(98));
                    
      } // endif
  } // end-catch 

  catch (InstantiationException e) {

      // When printing
      if  (T.getSysout() == 1) { 

          // print
          TyBase.printMsg(TyMsg.getMsg(53)
                  + AltName
                  + TyMsg.getText(99));

      } // endif
  } // end-catch  

  catch (IllegalAccessException e) {  

      // When printing
      if  (T.getSysout() == 1) { 

          // print
          TyBase.printMsg(TyMsg.getMsg(53)
                  + AltName
                  + TyMsg.getText(100));

      } // endif
  } // end-catch  

  catch (Exception  e) {

      // When printing
      if  (T.getSysout() == 1) { 

          // print
          TyBase.printMsg(TyMsg.getMsg(53)
                    + AltName
                    + TyMsg.getText(85)
                    + e.getMessage());

      } // endif
  } // end-catch    

} // end-constructor

/**
 * Constructor for local file logging
 * @param Ty TyBase - Tymeac base storage
 * @param dir java.lang.String - file directory
 * @param file java.lang.String - file name
 */
protected TyLogTable(TyBase Ty, String dir, String file) {

  // Tymeac base storage
  T = Ty;

  // using a local file
  use_local = true;

  // local file name
  local_file = file;
                          
  // local dir name
  local_dir = dir;

} // end-constructor

/**
 * Constructor for DBMS logging
 * @param Ty TyBase - Tymeac base storage
 * @param tbl String - table name
 */ 
protected TyLogTable (String tbl, TyBase Ty) {  
  
  // Tymeac base storage
  T = Ty;

  // using a DBMS
  use_dbms = true;  
            
  // not valid
  table_valid = false;

  // dbms table name  
  dbms_LogTable = tbl; 
      
} // end-constructor

/**
 * return the type of logging
 * @return type of logging
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
 * Used for the new log maint.
 * @return TyRunTimeElements
 */
protected TyRunTimeElements getElements() {
  
  TyRunTimeElements ele = new TyRunTimeElements();
  
  // When Not in effect
  if  (!T.isLogUsed()) {
      
      // say is not in use
      ele.setInUse(false);
      
      return ele;
    
  } // endif
  
  switch (getType()) {
  
    case 1: // dmbs
      
      ele.setDBMS(dbms_LogTable);
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
 * return the name of the log  
 * @return name of the log  
 */
protected String getName() {
  
  // When dbms
  if  (use_dbms) {
      
      // is so
      return dbms_LogTable;
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
 * executed by the log thread to signal completion
 */
protected void setDone() {

  logger = null;

} // end-method

/**
 * executed by the log thread when the logging is complete 
 */
protected synchronized void setFinished() {

  busy = false;
  
  notify();

} // end-method

/**
 * executed by the log thread to signal the passed info is valid 
 */
protected void setValid(boolean what) {

  table_valid = what;

} // end-method

/**
 * create and start the logger thread
 */
private void startLogger() {

  // When a local file
  if  (use_local) {

      // new thread
      logger = new TyLogThread( T,
                                this, 
                                local_dir,
                                local_file);        
  } // endif

  // when dbms
  if  (use_dbms) {

      // new thread
      logger = new TyLogThread( T,    // tymeac base storage
                                this, // this obj
                                dbms_LogTable); // table name
  } // endif

  // when alt
  if  (use_alt) {
    
      // alt class  
      logger = new TyLogThread( T,
                                this,
                                alt);                         
  } // endif

  // start it
  logger.start(); 
 
} // end-method
  
/**
 * Verify the data is correct
 * @param seconds int - number of seconds to wait for completion
 * @return boolean
 */ 
protected boolean verify (int seconds) {
    
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

  // one request at a time
  synchronized (T.getLog_tbl()) {

    // When no thread, create and start it
    if  (logger == null) startLogger(); 
    
    synchronized (this) {
        
      try {
        // is busy
        busy = true;
      
        // wake up the thread for verify 
        logger.wakeUp();
  
        // until finished or timed out
        while (busy) {

          try {
            // until complete or timed out
            wait(seconds * 1000);

          } // end-try
    
          catch (InterruptedException e) {
          } // end-catch 
        } // end-while
      } // end-try
        
      catch (java.lang.Exception ex)  {

        // When printing
        if  (T.getSysout() == 1) {
          
            // no log
            TyBase.printMsg(TyMsg.getMsg(41)
                              + TyMsg.getText(101)
                              + ex.getMessage()); 
            TyBase.printMsg(ex.toString());               
    
        } // endif

        // log not in use       
        table_valid = false;
        
        // return with error
        return false;
      
      } // end-catch 
    } // end-sync      
  } // end-sync

  // When timed out
  if  (busy) return false;
  
  // say what came back
  return table_valid;
  
} // end-method

/**
 * write a message
 * @param message String - the message
 * @param seconds int - number of seconds to wait for completion
 * @return boolean
 */ 
public boolean writeMsg (String message, int seconds) {

  // one request at a time
  synchronized (T.getLog_tbl()) {

    // When no thread, create and start it
    if  (logger == null)  startLogger(); 
    
    synchronized (this) {
    
      try {
        // is busy
        busy = true;
      
        // wake up the thread 
        logger.wakeUp(message);
  
        // until finished or timed out
        while (busy) {
  
          try {
            // until complete or timed out
            wait(seconds * 1000);
  
          } // end-try
      
          catch (InterruptedException e) {
          } // end-catch   
        } // end-while 
      } // end-try
      
      catch (java.lang.Exception ex)  {
  
        // When printing
        if  (T.getSysout() == 1) {
          
          // no log
          TyBase.printMsg(TyMsg.getMsg(3115) + ex.getMessage()); 
          TyBase.printMsg(ex.toString());               
    
        } // endif
  
        // log not in use       
        table_valid = false;
        
        // return with error
        return false;
        
      } // end-catch 
    } // end-sync
  } // end-sync

  // When timed out
  if  (busy) return false;
  
  // say what came back
  return table_valid;
  
} // end-method
} // end-class
