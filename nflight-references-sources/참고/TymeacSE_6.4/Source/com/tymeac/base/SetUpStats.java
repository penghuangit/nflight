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
 * Temporary setup for the statistics environment
 * @since 6.2
 */
public final class SetUpStats {
  
  // type of request
  public static final int StatsMaint   = 3;
  public static final int StatsReset   = 300;
  public static final int StatsNewDBMS = 310;
  public static final int StatsNewFile = 320;
  public static final int StatsNewAlt  = 330;
  public static final int StatsStop    = 340;
  
  // return code here
  public static final int StatsSuccess      = 0;   // success
  public static final int StatsNoCurr       = 40;  // no current stats
  public static final int StatsNotNec       = 41;  // stats doesn't need resetting
  public static final int StatsFailVeryOld  = 42;  // failed to verify
  public static final int StatsCreateFail   = 43;  // New stats failed to create
  public static final int StatsFailVeryNew  = 44;  // failed to verify
  public static final int StatsNotInUse     = 45;  // stats not in use
  public static final int StatsStopped      = 46;  // stats service stopped
  
  // The base for all Tymeac processing    
  private final TyBase T;
  
  // old stats object when new create fails
  private TyStatsTable old = null;
  
/**
 * Simple constructor
 * @param Ty
 */
protected SetUpStats (TyBase Ty) {
  
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
 * Initialize the statistics object
 * @param stats_table when DBMS
 * @param stats_dir when directory
 * @param stats_file when file
 * @param stats_alt when alternate class
 */
protected void initStats( String stats_table,
                          String stats_dir,
                          String stats_file,
                          String stats_alt) {
  
  /*
   * Set stats not available. This has no affect on start up.
   *   When a new stats object is being formed, this MAY prevent new
   *   requests coming in before the new stats is verified.   
   */ 
  T.setStatsUsed(false);
  
  // save current (when doing a new, in case new doesn't work)
  old = T.getStats_tbl();
  
  // When there is a DBMS stats entry
  if  (stats_table != null) {   
      
      // the DBMS stats table is stored in Tymeac base storage                                  
      T.setStats_tbl(new TyStatsTable(stats_table, T));
  }
  else {
      // When there is a File stats entry
      if  (stats_file != null) { 

          // the local log is stored in Tymeac base storage
           T.setStats_tbl(new TyStatsTable(T, stats_dir, stats_file));
      }
      else {
          // When there is an Alt stats entry 
          if  (stats_alt != null) {

              // stored in Tymeac base storage
              T.setStats_tbl(new TyStatsTable(T, stats_alt));
          }
          else {
              // no stats table
              T.setStats_tbl(null);
        
          } // endif
      } // endif
  } // endif   
  
} // end-method

/**
 * Finalize the stats environment for a start up
 */
protected void finiStatsStartup() {
  
  // stats object
  TyStatsTable stats = T.getStats_tbl();
  
  // When not there
  if  (stats == null) {
    
      // no stats
      doMsg(TyMsg.getMsg(6) + TyMsg.getText(44));
      
      // done
      return;
    
  } // end if
  
  // verify and wait up to 15 seconds 
  if  (stats.verify(15)) {  

      // stats in use       
      T.setStatsUsed(true);
      
      // say what happened
      sayWhat(stats);
  }
  else {
      // no stats
      doMsg(TyMsg.getMsg(7));

  } // endif   
  
} // end-method

/**
 * Finalize the stats environment other than a start up
 */
protected int finiStatsNew() {
  
  // stats object
  TyStatsTable stats = T.getStats_tbl();
  
  // When not there, get out
  if  (stats == null)  return StatsCreateFail;
  
  // verify it exists and wait up to 15 seconds 
  if  (stats.verify(15)) {            

      // stats in use       
      T.setStatsUsed(true);
      
      // say what happened
      sayWhat(stats);
      
      // sucessfull
      return StatsSuccess;
      
  } // endif 
  
  // restore old, but stats not in use
  T.setStats_tbl(old);
  
  // no stats      
  return StatsFailVeryNew;  
  
} // end-method

/**
 * Validate the old stats environment
 * @return int what happened 
 */
protected int resetStatsOld() {
  
  // stats object
  TyStatsTable stats = T.getStats_tbl();
  
  // When not there, get out
  if  (stats == null)  return StatsNoCurr;
  
  // When log doesn't need resetting
  if  (T.isStatsUsed()) return StatsNotNec;
  
  // verify it exists and wait up to 15 seconds 
  if  (stats.verify(15)) { 

      // log in use       
      T.setStatsUsed(true);
      
      // say
      doMsg(TyMsg.getMsg(8201));
      
      // sucessfull
      return StatsSuccess;
      
  } // endif 
  
  // no log      
  return StatsFailVeryOld;
  
} // end-method

/**
 * Say what type of stats is in use
 * @param stats object
 */
private void sayWhat(TyStatsTable stats) {
  
  // print/log the type of STATS in use
  switch (stats.getType()) {

    case 1:
      // DBMS table name   
      doMsg(TyMsg.getMsg(6) 
               + TyMsg.getText(45) 
               + T.getStats_tbl().getName());
      // done
      break; 

    case 2:
      // say local
      doMsg(TyMsg.getMsg(6) 
               + TyMsg.getText(43) 
               + T.getStats_tbl().getName());
      // done
      break; 

    case 3:
      // say alt
      doMsg(TyMsg.getMsg(6) 
               + TyMsg.getText(51)
               + T.getStats_tbl().getName());
     // done
     break; 
               
  } // end-switch 
  
} // end-method

/**
 * Stop logging
 * @return service stopped
 */
protected int stopService() {
  
  // set to Not use stats
  T.setStatsUsed(false);
  
  // null stats object
  T.setStats_tbl(null);
  
  // no stats msg
  doMsg(TyMsg.getMsg(6) + TyMsg.getText(44));
  
  return StatsStopped;
  
} // end-method
} // end-class
