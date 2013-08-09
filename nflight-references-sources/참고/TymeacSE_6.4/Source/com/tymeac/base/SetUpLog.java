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

/**
 * Temporary setup for the logging environment
 * @since 6.2
 */
public final class SetUpLog {
    
  // type of request
  public static final int LogMaint   = 2;
  public static final int LogReset   = 200;
  public static final int LogNewDBMS = 210;
  public static final int LogNewFile = 220;
  public static final int LogNewAlt  = 230;
  public static final int LogStop    = 240;
  
  // return code here
  public static final int LogSuccess      = 0;   // success
  public static final int LogNoCurr       = 20;  // no current log
  public static final int LogNotNec       = 21;  // log doesn't need resetting
  public static final int LogWriteOldFail = 22;  // write to original log failed
  public static final int LogFailVeryOld  = 23;  // failed to verify original
  public static final int LogCreateFail   = 24;  // New log failed to create
  public static final int LogWriteNewFail = 25;  // write to new log failed
  public static final int LogFailVeryNew  = 26;  // failed to verify new  
  public static final int LogNotInUse     = 27;  // logging not in use
  public static final int LogStopped      = 28;  // logging service stopped 
  
  // The base for all Tymeac processing    
  private final TyBase T;
  
  // saved old log object when getting a new object
  private TyLogTable old = null;
  
/**
 * Simple constructor
 * @param Ty
 */
protected SetUpLog (TyBase Ty) {
  
  T = Ty; // save Tymeac Base Storage
  
} // end-constructor 

/**
 * General msg routine 
 * @param msg String
 */
private void doMsg(String msg) {

  // When logging, do so (wait up to 10 seconds)
  if  (T.isLogUsed())  T.getLog_tbl().writeMsg(msg, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(msg);

} // end-method 

/**
 * Initialize the logging object
 * @param log_table when DBMS
 * @param log_dir when directory
 * @param log_file when file
 * @param log_alt when alternate class
 */
protected void initLog( String log_table,
                        String log_dir,
                        String log_file,
                        String log_alt) {
  
  /*
   * Set log not available. This has no affect on start up.
   *   When a new log object is being formed, this MAY prevent new
   *   requests coming in before the new log is verified.   
   */ 
  T.setLogUsed(false);
  
  // save old (when doing a new, in case new doesn't work)
  old = T.getLog_tbl();
  
  // When there is a DBMS log entry
  if  (log_table != null) {
          
      // init log
      T.setLog_tbl(new TyLogTable(log_table, T));
  } 
  else {
      // When there is a File log entry
      if  (log_file != null) { 
          
          // init log
          T.setLog_tbl(new TyLogTable(T, log_dir, log_file));
      }
      else {
          // When there is an Alt log entry 
          if  (log_alt != null) {
              
              // init log
              T.setLog_tbl(new TyLogTable(T, log_alt));
          }
          else {
              // no log table
              T.setLog_tbl(null);

          } // endif
      } // endif
  } // endif    
    
} // end-method 

/**
 * Finalize the logging environment for a start up
 */
protected void finiLogStartup() {
  
  // log object
  TyLogTable log = T.getLog_tbl();
  
  // When not there
  if  (log == null) {
    
      // no log
      doMsg(TyMsg.getMsg(4) + TyMsg.getText(44));
      
      // done
      return;
    
  } // end if
  
  // verify it exists and wait up to 15 seconds 
  if  (log.verify(15)) {  
      
      // temp
      String temp;
      
      /*
       * This is the start up message that may have been written to the console at
       *   the beginning of of this start up. This could not be written to the log 
       *   before it exists so we write it to the log now. 
       */
      // When stand alone
      if  (T.isStandALone()) {
        
          // with stand-a-lone sufix
          temp = TyMsg.getMsg(1) + TyMsg.getText(65);
      }
      else {
          // with dbms sufix
          temp = TyMsg.getMsg(1) + TyMsg.getText(66);
          
      } // endif
      
      // When in use, write the starting up message, wait up to 15 sec
      if  (log.writeMsg(temp, 15)) {

          // log in use       
          T.setLogUsed(true);
          
          // say what happened
          sayWhat(log);
      }
      else {
          // no log
          doMsg(TyMsg.getMsg(5));
      
      } // endif
  }
  else {
      // no log
      doMsg(TyMsg.getMsg(5));
      
  } // endif
  
} // end-method

/**
 * finalize the new logging environment
 * @return int what happened 
 */
protected int finiLogNew() {
  
  // log object
  TyLogTable log = T.getLog_tbl();
  
  // When not there, get out
  if  (log == null)  return LogCreateFail;
  
  // verify it exists and wait up to 15 seconds 
  if  (log.verify(15)) {            
      
      // When in use, write the new log message, wait up to 15 sec
      if  (log.writeMsg(TyMsg.getMsg(8102), 15)) {

          // log in use       
          T.setLogUsed(true);
          
          // say what happened
          sayWhat(log);
          
          // sucessfull
          return LogSuccess;
      }
      else {  
          // restore old, but logging not in use
          T.setLog_tbl(old);
        
          // back bad
          return LogWriteNewFail;
      
      } // endif  
  } // endif 
  
  // restore old, but logging not in use  
  T.setLog_tbl(old);
  
  // no log   
  return LogFailVeryNew;
  
} // end-method

/**
 * Validate the old logging environment
 * @return int what happened 
 */
protected int resetLogOld() {
  
  // log object
  TyLogTable log = T.getLog_tbl();
  
  // When not there, get out
  if  (log == null)  return LogNoCurr;
  
  // When log doesn't need resetting
  if  (T.isLogUsed()) return LogNotNec;
  
  // verify it exists and wait up to 15 seconds 
  if  (log.verify(15)) {            
      
      // When (write the newly verified log message) successful
      if  (log.writeMsg(TyMsg.getMsg(8101), 15)) {

          // log in use       
          T.setLogUsed(true);
          
          // When printing, do so
          if  (T.getSysout() == 1) TyBase.printMsg(TyMsg.getMsg(8101));
          
          // sucessfull
          return LogSuccess;
      }
      else {          
          // back bad
          return LogWriteOldFail;
      
      } // endif  
  } // endif 
  
  // no log      
  return LogFailVeryOld;
  
} // end-method

/**
 * Say what type of log is in use
 * @param log object
 */
private void sayWhat(TyLogTable log) {
  
  // print/log the type of log in use
  switch (log.getType()) {
  
    case 1:
      // DBMS   
      doMsg(TyMsg.getMsg(4)
               + TyMsg.getText(45)
               + T.getLog_tbl().getName());
      
      // done
      break;      
      
    case 2:
      // say local
      doMsg(TyMsg.getMsg(4) 
               + TyMsg.getText(43)
               + T.getLog_tbl().getName());
      
      // done
      break;
      
    case 3:
      // say alt
      doMsg(TyMsg.getMsg(4) 
               + TyMsg.getText(51)
               + T.getLog_tbl().getName());
      // done
      break; 
    
  } // end-switch
  
} // end-method

/**
 * Stop logging
 * @return service stopped
 */
protected int stopService() {
  
  // set to Not use logging
  T.setLogUsed(false);
  
  // null log object
  T.setLog_tbl(null);
  
  // no log msg
  doMsg(TyMsg.getMsg(4) + TyMsg.getText(44));
  
  return LogStopped;
  
} // end-method
} // end-class
