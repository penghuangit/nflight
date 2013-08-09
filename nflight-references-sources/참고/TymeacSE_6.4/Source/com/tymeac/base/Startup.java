package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2012 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.io.File;
import java.util.*;
import java.util.prefs.*;
import java.net.*;

import com.tymeac.serveruser.*;

/**
 * Tymeac Server Start up base module. 
 */
public final class Startup {
  
// public constants 
    public static final int startup_basic       = 0; // normal rmi including Jini    
    public static final int startup_activatable = 1; // activatable rmi including Jini   
    public static final int startup_iiop        = 2; // iiop including poa   
    public static final int startup_internal    = 3; // non-rmi internal       

// private instance variables
    // The base for all Tymeac processing    
    private final TyBase T;
  
    // Type of startup as above
    private final int ty_type;
    
    // General fields used by the other start up classes
    private final StartupFields suFields;

/**
 * Constructor
 * @param Ty TyBase - tymeac base storage
 * @param type int - type of startup
 */
protected Startup(TyBase Ty, int type) {
    
  T       = Ty;   // save Tymeac Base Storage
  ty_type = type; // save type of startup
  
  // common fields used by other start up classes
  suFields = new StartupFields(Ty);
 
} // end-constructor

/**
 * See if a config file exists
 * @return
 */
private boolean checkForFile() {
  
  File afile;
  String tdir;
  
  // get a pointer to the default filename in the current directory
  String tname = TyMsg.getText(53);
  afile = new File(tname);

  // When it is there
  if  (afile.exists()) {

      // get the full path including the filename                 
      tdir = afile.getAbsolutePath();
      
      // get rid of the filename in the dir string
      suFields.setDir(tdir.substring(0, tdir.length() - (tname.length() + 1))); 
      
      // new file name
      suFields.setFilename(tname);
      
      // got it
      return true;
      
  } // endif  
  
  // no file
  return false; 
   
} // end-method

/**
 * See if a preference node exists 
 * 
 * @return boolean
 */
private boolean checkForPref() {
  
  try {
    // When a node does Not exist
    if  (!Preferences.systemRoot().nodeExists(TyMsg.getText(147))) return false;
    
  } // end-try
  
  catch (Exception e ) {
    
    // not here
    return false;
    
  } // end-catch
  
  // Retrieve the user preference node for the package 
  Preferences prefs = Preferences.systemNodeForPackage(com.tymeac.base.Startup.class);
  
  // When URL exists, got it
  return (prefs.get(com.tymeac.base.TyBase.PREF_URL, null) != null)? true : false; 
  
} // end-method

/**
 * Get the login context from a class or file
 */
public void determineLoginContext() {  
     
  // When in stand alone mode
  if  (suFields.isStand_alone()) {
      
      // from a class
      getFromClass();  
  }
  else {
      // from a file
      getFromFile();
      
  } // endif          
      
} // end-method

/**
 * Select either stand-alone or DBMS mode. The boolean is in the common fields. This routine returns
 *   a false when there was a problem.
 */
protected boolean determineRunMode() 
                   throws Throwable {   
  
  File afile;
  String tdir;
  boolean isPref = false;
  
  String tFilename = suFields.getFilename();
  
  // When not in stand alone mode
  if  (!suFields.isStand_alone()) {
        
      // When no directory    
      if  (suFields.getDir() == null) {
        
          // When no filename
          if  (tFilename == null) {
            
              // When preferences found
              if  (checkForPref()) {
            
                   // set true
                   isPref = true;              
              }
              else { 
                  // When no file found
                  if  (!checkForFile()) {                
                
                      // default to standalone
                      suFields.setStand_alone(true);
                      
                  } // endif  
              } // endif              
          }
          else { 
              // get a pointer to the file in the current directory
              afile = new File(tFilename);
          
              // When it is there
              if  (afile.exists()) {
            
                  // get the full path including the filename                 
                  tdir = afile.getAbsolutePath();
                  
                  // get rid of the filename
                  suFields.setDir(tdir.substring(0, tdir.length() - (tFilename.length() + 1))); 
              }
              else { 
                  // read error without dir
                  TyCfgFileIO.putMsg(100, null, tFilename);   
                  
                  // say so
                  doMsg(TyMsg.getMsg(3));
                  
                  // When activation type
                  if  (isActivatable()) {
                       
                      // done here
                      throw new Throwable(TyMsg.getMsg(3));              
                  }
                  else {
                      // go back
                      return false;
                      
                  } // endif              
              } // endif
          } // endif
      } // endif          
  } // endif
  
  // When in stand alone mode
  if  (suFields.isStand_alone()) {
      
      try {
        // Load the Variables Class
        suFields.setVariable_table(new TymeacUserVariables());

      } // end-try 
    
      catch (NoClassDefFoundError e) {
          
        // error
        doMsg1(TyMsg.getMsg(32));
        
        // When activation type
        if  (isActivatable()) {
             
            // done here
            throw new Throwable(TyMsg.getMsg(32));            
        }
        else {
            // go back
            return false;
            
        } // endif
      } // end-catch
  
      //good
      return true; 
  
  } // endif  
  
  // class of strings used to pass config file lines
  suFields.setBase(new TyCfgFields());
  
  // When using preferences
  if  (isPref) {
    
      // get the pref fields
      fetchPref();
  
      //good
      return true; 
    
  } // endif 
  
  // When no filename, use the default filename
  if  (tFilename == null) tFilename = TyMsg.getText(53);

  // do the file read and load
  int reason = TyCfgFileIO.getFile(suFields.getDir(), tFilename, suFields.getBase());
              
  // When no good
  if  (reason != 0) {

      // See what came back
      switch (reason) {

        case 100: // read error
        
          doMsg(TyMsg.getMsg(19));

          // reason
          TyCfgFileIO.putMsg(reason, suFields.getDir(), tFilename);

          // done 
          break;

        case 200: // io exception
        
          doMsg(TyMsg.getMsg(20));

          // reason
          TyCfgFileIO.putMsg(reason, suFields.getDir(), tFilename);

          // done 
          break;

        case 300: // close error
        
          doMsg(TyMsg.getMsg(21));

          // reason
          TyCfgFileIO.putMsg(reason, suFields.getDir(), tFilename);

          // done
          break;

        default: // structure error
        
          doMsg(TyMsg.getMsg(26));

          // reason
          TyCfgFileIO.putMsg(reason, suFields.getDir(), tFilename);

          // done
          break;

      } // end-switch
      
      // When activation type
      if  (isActivatable()) {
           
          // done here
          throw new Throwable(TyMsg.getMsg(26));              
      }
      else {
          // go back
          return false;
          
      } // endif 
  } // endif      
    
  //good
  return true;  
  
} // end-method

/**
 * do the rest of the set up
 *
 * @return int non zero is a problem
 */
protected int doRest() 
        throws Throwable { 
  
  // new class loader
  T.setLoader(new TyClassLoader());  
      
  // When in stand alone mode
  if  (suFields.isStand_alone()) {
    
      // set in base
      T.setStandALoneMode();
    
      // When a standalone basic set up failed, get out
      if  (!usingStandAlone()) return 5;
  }
  else {      
      // set in base
      T.setDBMSMode();
      
      // When a DBMS mode basic set up failed, get out
      if  (!usingDBMS()) return 5;

  } // endif
  
  /*
   * When building Queues and Functions, the Functions depend on 
   *   the Queues, so build the Queues first.
   */
  
  // *--- Queues ---*
  
  // When any error in final queue setup, initialization failed
  if  (!new StartupQueues(T, this).createQueues()) return 10;   
  
  // *--- Functions ---*
  
  // When any error in final function setup, initialization failed
  if  (!new StartupFunctions(T, this).createFunctions()) return 15; 
  
  /*
  * set up the base tables for Tymeac:
  *
  *     Request - for sync / asynchronous requests
  *     Stall   - for stalled async requests 
  *     Gen     - number generation for sync and async requests
  */ 
  
  // Requests 
  T.setRequest_tbl(new RequestHeader(T));
  
  // Stall Array
  T.setStall_tbl(new StallHeader(T));
      
  // Number Generation 
  T.setGen_tbl(new GenTable(T));
  
  // *--- load the real impl class ---*
  T.setImpl(new TymeacImpl(T));

  // *--- new info class and save base therein ---*
  T.setTyInfo(TymeacInfo.getInstance());  
  T.getTyInfo().setBase(T);
  
  // *--- load the User Singleton ---*  
  try {    
    // When non-zero return, bad
    if  (doSingleton() != 0) return 20;
    
  }// end-try
  
  catch (Throwable e) {
    
    // just re-throw
    throw e;
    
  } // end-catch
  
  // *--- load a new RMI security manager ---*  
  try {    
    // When non-zero return, bad
    if  (doSecurityMgr() != 0) return 25;
    
  } // end-try
  
  catch (Throwable e) {
    
    // just re-throw
    throw e;
    
  } // end-catch 
  
  // *--- load the monitor ---*
  doMonitor();
    
  // *--- Notification ---* 
  // When there is a notify function in use, set up for post set-up processing
  if  (suFields.getNotify_func()!= null) new SetUpNotify(T).initNotify(suFields.getNotify_func()); 

  // When start up exit classes, do the exit classes
  if  ((suFields.getStart_array() != null) &&
       (suFields.getStart_array().length > 0)) startupExit();
  
  // done good
  return 0;
   
} // end-method

/**
 * Try to load the Tymeac Monitor and set defalut timeout values.
 */
private void doMonitor() {
  
  //  When the monitor interval is not zero:
  if  (T.getMonitor_interval() != 0) {
      
      // using a monitor  
      T.setMonitor(new Monitor(T));

      // lowest priority
      T.getMonitor().setPriority(Thread.MIN_PRIORITY); 
      
      // When internal, use as a daemon
      if  (isInternal()) T.getMonitor().setDaemon(true);  
      
      // start the monitor thread
      T.getMonitor().start();    
  }
  else {
      // no monitor msg
      doMsg(TyMsg.getMsg(14));
          
  } // endif 
  
} // end-method

/**
 * General error routine without throwing exception
 * @param error java.lang.String
 * @exception java.lang.Throwable The exception description.
 */
protected void doMsg(String error) {

  // When logging, do so (wait up to 10 seconds)
  if  (T.isLogUsed())  T.getLog_tbl().writeMsg(error, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(error);

} // end-method 

/**
 * General error routine 
 * @param error java.lang.String
 * @exception java.lang.Throwable The exception description.
 */
protected void doMsg1(String error) 
    throws java.lang.Throwable {

  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(error, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1) TyBase.printMsg(error);
  
  // When actavation, throw the error 
  if  (isActivatable()) throw new Throwable(error); 

} // end-method 

/**
 * Try to load the RMI Security manager
 *
 * @return int
 */
private int doSecurityMgr() 
        throws Throwable {
  
  // When NOT internal
  if  (!isInternal()) {   
      
      try {
        // see if one is there
        T.getLoader().loadClass(TyMsg.getText(161)).newInstance(); 
    
      } // end-try  
      
      // class is not there
      catch (java.lang.ClassNotFoundException e) {
     
        // info only message
        doMsg(TyMsg.getMsg(18));
    
      } // end-catch
      
      // unknown exception
      catch (Exception e) {
    
        // log and print
        doMsg(TyMsg.getMsg(17));
        doMsg(TyMsg.getText(2) + e.toString());
        
        // When activation
        if  (isActivatable()) {
            
            // get out
            throw new Throwable(TyMsg.getMsg(17) + e.toString());
        }
        else {       
            // get out  
            return 25;
            
        } // endif
      } // end-catch
  } // endif
  
  return 0;
  
} // end-method

/**
 * Try to load the user Singleton class
 * @return int
 */
private int doSingleton() 
        throws Throwable { 
  
  try {
   // get the instance reference
   T.setTyuser(TymeacUserSingleton.getInstance());          

  } // end-try  
  
  // class is not there, ignore
  catch (java.lang.NoClassDefFoundError e) {} 
  
  // unknown exception
  catch (Exception e) {

    // log and print
    doMsg(TyMsg.getMsg(15) + e.toString());
    
    // When activation
    if  (isActivatable()) {
        
        // get out
        throw new Throwable(TyMsg.getMsg(15) + e.toString());
    }
    else {       
        // get out  
        return 20;
        
    } // endif
  } // end-catch
  
  return 0;
  
} // end-method

/**
 * Get the preference fields
 */
private void fetchPref() {
  
  // public String fields
  TyCfgFields base = suFields.getBase();
  
  // Retrieve the system preference node for the package 
  Preferences prefs = Preferences.systemNodeForPackage(com.tymeac.base.Startup.class);
  
  // get the value of the preference
  base.dbms_URL         = prefs.get(com.tymeac.base.TyBase.PREF_URL, null);
  base.dbms_DataManager = prefs.get(com.tymeac.base.TyBase.PREF_DATA_MANAGER, null);
  base.dbms_UserName    = prefs.get(com.tymeac.base.TyBase.PREF_USER_NAME, null);
  base.dbms_PassWord    = prefs.get(com.tymeac.base.TyBase.PREF_PASSWORD, null);
  base.dbms_FuncTable   = prefs.get(com.tymeac.base.TyBase.PREF_FUNC_TABLE, null);
  base.dbms_ListTable   = prefs.get(com.tymeac.base.TyBase.PREF_LIST_TABLE, null);
  base.dbms_QueTable    = prefs.get(com.tymeac.base.TyBase.PREF_QUEUE_TABLE, null);
  base.dbms_Log         = prefs.get(com.tymeac.base.TyBase.PREF_LOG_TABLE, null);
  base.dbms_Stats       = prefs.get(com.tymeac.base.TyBase.PREF_STATS_TABLE, null);
  base.func_Notify      = prefs.get(com.tymeac.base.TyBase.PREF_NOTIFY, null);
  base.sys_exit         = prefs.get(com.tymeac.base.TyBase.PREF_SYS_EXIT, null);
  base.mon_Interval     = prefs.get(com.tymeac.base.TyBase.PREF_MON_INTERVAL, null);
  base.act_Interval     = prefs.get(com.tymeac.base.TyBase.PREF_ACT_INTERVAL, null);
  base.statsDir         = prefs.get(com.tymeac.base.TyBase.PREF_STATS_DIR, null);
  base.statsFile        = prefs.get(com.tymeac.base.TyBase.PREF_STATS_FILE, null);
  base.logDir           = prefs.get(com.tymeac.base.TyBase.PREF_LOG_DIR, null);
  base.logFile          = prefs.get(com.tymeac.base.TyBase.PREF_LOG_FILE, null);
  base.altStatsClass    = prefs.get(com.tymeac.base.TyBase.PREF_ALT_STATS_CLASS, null);
  base.altLogClass      = prefs.get(com.tymeac.base.TyBase.PREF_ALT_LOG_CLASS, null);
  base.loginContext     = prefs.get(com.tymeac.base.TyBase.PREF_LOGIN_CONTEXT, null);
  
  String myString = "";
  String S = null; 
  int i, max;
  ArrayList<String> myList = new ArrayList<String>();
  
  // name
  S = com.tymeac.base.TyBase.PREF_START_CLASSES;  
  
  // do all the start up classes
  for (i = 0; myString != null; i++) {
  
    // try to get the string
    myString = prefs.get(S + i, null);
    
    // When exists, add to vector
    if  (myString != null)  myList.add(myString);
        
  } // end-for
  
  // how many
  max = myList.size();
  
  // When there are start up classes
  if  (max > 0) {
  
      // new array
      base.start_classes = new String[max];
      
      // move all the classes
      for (i = 0; i < max; i++) {
        
        // each
        base.start_classes[i] = myList.get(i);
        
      } // end-for
  } // endif
  
  myList = new ArrayList<String>();
  myString = "";
  
  // name
  S = com.tymeac.base.TyBase.PREF_START_FUNCTIONS;  
  
  // do all the start up functions
  for (i = 0; myString != null; i++) {
  
    // try to get the string
    myString = prefs.get(S + i, null);
    
    // When exists, add to vector
    if  (myString != null)  myList.add(myString);
        
  } // end-for
  
  // how many
  max = myList.size();
  
  // When there are start up functions
  if  (max > 0) {
  
      // new array
      base.start_functions = new String[max];
      
      // move all the classes
      for (i = 0; i < max; i++) {
        
        // each
        base.start_functions[i] = myList.get(i);
        
      } // end-for
  } // endif
  
  myList = new ArrayList<String>();
  myString = "";
  
  // name
  S = com.tymeac.base.TyBase.PREF_SHUT1_CLASSES;  
  
  // do all the shut1 up classes
  for (i = 0; myString != null; i++) {
  
    // try to get the string
    myString = prefs.get(S + i, null);
    
    // When exists, add to vector
    if  (myString != null)  myList.add(myString);
        
  } // end-for
  
  // how many
  max = myList.size();
  
  // When there are shut1 classes
  if  (max > 0) {
  
      // new array
      base.shut1_classes = new String[max];
      
      // move all the classes
      for (i = 0; i < max; i++) {
        
        // each
        base.shut1_classes[i] = myList.get(i);
        
      } // end-for
  } // endif
  
  myList = new ArrayList<String>();
  myString = "";
  
  // name
  S = com.tymeac.base.TyBase.PREF_SHUT2_CLASSES;  
  
  // do all the shut2 classes
  for (i = 0; myString != null; i++) {
  
    // try to get the string
    myString = prefs.get(S + i, null);
    
    // When exists, add to vector
    if  (myString != null)  myList.add(myString);
        
  } // end-for
  
  // how many
  max = myList.size();
  
  // When there are shut2 classes
  if  (max > 0) {
  
      // new array
      base.shut2_classes = new String[max];
      
      // move all the classes
      for (i = 0; i < max; i++) {
        
        // each
        base.shut2_classes[i] = myList.get(i);
        
      } // end-for
  } // endif
  
} // end-method

/**
 * get the login context from the UserVariables class
 */
private void getFromClass() {    
  
  // security fields
  T.setLoginContext(suFields.getVariable_table().getLoginContext());
  
} // end-method

/**
 * get the login context from the cfg file
 */
private void getFromFile() {        
  
  // When empty
  if  ((suFields.getBase().loginContext == null) ||
       (suFields.getBase().loginContext.length() == 0)) {
      
      // say is null
      T.setLoginContext(null);
   }
   else {
      // set real
      T.setLoginContext(suFields.getBase().loginContext);
      
   } // endif
    
} // end-method    

/**
 * Get the start up strings
 * @return StartupFields
 */
public StartupFields getStartupFields () { return suFields; } // end-method

/**
 * Look for a configuration argument
 * @param what
 * @return boolean
 */
protected boolean isConfig(String what) {

  // When right length & match
  return ((what.length() == TyMsg.getText(60).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(60))))?
          true : false;
  
} // end-method

/**
 * Is this an activatable start up
 * @return boolean
 */
protected boolean isActivatable() {

  // When activatable, say so
  return (ty_type == startup_activatable)? true : false;
  
} // end-method

/**
 * Look for a directory argument
 * @param what
 * @return
 */
protected boolean isDir(String what) {
  
  // When right length & match
  return ((what.length() == TyMsg.getText(58).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(58))))?
          true : false;

} // end-method  

/**
 * Look for a file argument
 * @param what
 * @return
 */
protected boolean isFile(String what) {

  // When right length & match
  return ((what.length() == TyMsg.getText(57).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(57))))?
         true : false; 
  
} // end-method  

/**
 * Is this an internal start up, not remote
 * @return boolean
 */
protected boolean isInternal() {

  // When activatable, say so
  return (ty_type == startup_internal)? true : false;
    
} // end-method

/**
 * Look for a port argument
 * @param what
 * @return
 */
protected boolean isPort(String what) {

  // When right length & match
  return ((what.length() == TyMsg.getText(54).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(54))))?
         true : false; 
  
} // end-method  

/**
 * Look for a verbose argument
 * @param what
 * @return
 */
protected boolean isVerbose(String what) {

  // When right length & match
  return ((what.length() == TyMsg.getText(56).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(56))))?
         true : false; 
  
} // end-method  

/**
 * Look for a standalone argument
 * @param what
 * @return
 */
protected boolean isStandAlone(String what) {

  // When right length & match
  return ((what.length() == TyMsg.getText(59).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(59))))?
         true : false;
  
} // end-method

/**
 * Look for a no argument
 * @param what
 * @return
 */
protected boolean isNo(String what) {

  // When right length & match
  return ((what.length() == TyMsg.getText(55).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(55))))?
         true : false;
  
} // end-method

/**
 * Look for a shut down embeded data base argument
 * @param what
 * @return shut down arg or not
 */
protected boolean isShutDown(String what) {

  // When right length & match
  return ((what.length() == TyMsg.getText(171).length()) &&  
          (what.equalsIgnoreCase(TyMsg.getText(171))))?
         true : false; 
  
} // end-method

/**
 * Parse the input args
 * @return int
 * @param args java.lang.String[]
 */
public int parseArgs(String[] args) 
            throws Throwable {
  
  // When no args, done
  if  (args == null) return 0;
    
  // for arguement testing
  int arg_count = args.length, pos = 0;
      
  // When no args, done
  if  (arg_count == 0) return 0;

  // until no more args
  while (arg_count > 0) {
    
    // for break
    while (true) {

      // When verbose
      if  (isVerbose(args[pos])) {
  
          // set not printing
          T.setSysout(0);
          
          // next position, one less arg
          pos++;
          arg_count--;
          
          // done here
          break;
          
      } // endif
      
      // When stand alone
      if  (isStandAlone(args[pos])) {
          
          // set stand alone
          suFields.setStand_alone(true);
          
          // next position, one less arg
          pos++;
          arg_count--;
          
          // done here
          break;
          
      } // endif
      
      // When directory
      if  (isDir(args[pos])) {

          // When there is a next arg
          if  (arg_count > 1) {
          
              // set the directory 
              suFields.setDir(args[pos + 1]);
          }
          else {
              // send error
              pushMsg(TyMsg.getMsg(120));
              
              // get out  
              return 1;
            
          } // endif

          // next position, two less args
          pos += 2;
          arg_count -= 2;
          
          // done here
          break;

      } // endif
      
      // When file
      if  (isFile(args[pos])) {

          // When there is a next arg
          if  (arg_count > 1) {
          
              // set the filename
              suFields.setFilename(args[pos + 1]);
          }
          else {
              // send error
              pushMsg(TyMsg.getMsg(125));
              
              // get out  
              return 1;
            
          } // endif
          
          // next position, two less args
          pos += 2;
          arg_count -= 2;
          
          // done here
          break;

      } // endif
      
      // When port
      if  (isPort(args[pos])) {
          
          // When there is a next arg
          if  (arg_count > 1) { 
           
              try {
                // convert to int
                T.setPort(Integer.parseInt(args[pos + 1]));
                
              } // end-try 
              
              catch (NumberFormatException e) {
                
                // send error
                pushMsg(TyMsg.getMsg(132));
              
                // get out  
                return 1;
                
              } // end-catch
          }
          else {
              // send error
              pushMsg(TyMsg.getMsg(130));
              
              // get out  
              return 1;
            
          } // endif
          
          // next position, two less args
          pos += 2;
          arg_count -= 2;
          
          // done here
          break;

      } // endif
      
      // When shut down db
      if  (isShutDown(args[pos])) {
          
          // When there is a next arg
          if  (arg_count > 1) { 
                        
                // save in base 
                T.setDBShut(args[pos + 1]);
          }
          else {
              // send error
              pushMsg(TyMsg.getMsg(131));
              
              // get out  
              return 1;
            
          } // endif
          
          // next position, two less args
          pos += 2;
          arg_count -= 2;
          
          // done here
          break;

      } // endif
      
      // When Jini configuration location and component
      if  (isConfig(args[pos])) {
        
          // When there are two next args
          if  (arg_count > 2) {
          
              // set the location and config name
              suFields.setConfigLocation(args[pos + 1]);
              suFields.setConfigComponent(args[pos + 2]);
          }
          else {
              // send error
              pushMsg(TyMsg.getMsg(140));
              
              // get out  
              return 1;
            
          } // endif
          
          // next position, three less args
          pos += 3;
          arg_count -= 3;
          
          // done here
          break;

      } // endif
      
      // invalid arg
      pushMsg(TyMsg.getMsg(16) + args[pos]);
      
      // get out  
      return 1;
      
    }// end-while     
  } // end-while
  
  // look for mutex problems
  
  // When standalone isn't alone
  if  (suFields.isStand_alone()) {
    
      // When a directory or file
      if  ((suFields.getDir() != null) || (suFields.getFilename() != null)) {
          
          // mutex error
          pushMsg(TyMsg.getMsg(135));
          
          // get out  
          return 1;          
          
      } // endif
  } // endif        
  
  // good
  return 0;

} // end-method

/**
 * print or throw the error msg
 * @param msg
 * @throws Throwable
 */
private void pushMsg(String msg) 
              throws Throwable {

  // print error
  TyBase.printMsg(msg);
  
  // When an activation start-up, throw the error                            
  if  (isActivatable())  throw new Throwable(msg);
  
} // end-method                 
              
/**
 * Exec the start up classes
 */
private void startupExit() 
    throws Throwable {
  
  // start array
  String[] start_array = suFields.getStart_array();
  
  // number of classes
  int nbr = start_array.length;

  // class and url
  ClsUrl cls_url = null;
      
  // do all here
  for (int i = 0; i < nbr; i++) {
    
    // separate the Class from the URL
    cls_url = TyFormat.doClass(start_array[i]);
                          
    // what came back
    switch (cls_url.getResult()) {
        
      case 1: // good value but not a url      
          try {
            // load the class 
            T.getLoader().loadClass(cls_url.getClassName()).newInstance(); 

          } // end-try  
            
          catch (Exception  e) {

              // send out
              doMsg(TyMsg.getMsg(52)
                    + start_array[i]
                    + TyMsg.getText(85)
                    + e.getMessage());
     
            } // end-catch 
            
            catch (Error  e) {

              // send out
              doMsg(TyMsg.getMsg(52)
                    + start_array[i]
                    + TyMsg.getText(85)
                    + e.getMessage());
     
            } // end-catch 

          // done 
          break;
      
      case 2: // good value, is a URL
          try {   
            // get the url class loader
            URLClassLoader urloader = new URLClassLoader(cls_url.getUrlName()); 
            
            // load the class with a url loader               
            urloader.loadClass(cls_url.getClassName()).newInstance();
            
            // save loader
            T.setExit_list(urloader);
                
          } // end-try        
          
          catch (Exception  e) {

              // send out
              doMsg(TyMsg.getMsg(52)
                    + start_array[i]
                    + TyMsg.getText(85)
                    + e.getMessage());  
    
            } // end-catch
            
            catch (Error  e) {

              // send out
              doMsg(TyMsg.getMsg(52)
                    + start_array[i]
                    + TyMsg.getText(85)
                    + e.getMessage());
     
            } // end-catch

          // done 
          break;

      case 0:
      case 3: // bad class::url format
          doMsg(TyMsg.getMsg(56)
                + start_array[i]
                + TyMsg.getText(28));

          // done 
          break;

      case 4: // bad format
           doMsg(TyMsg.getMsg(57)
              + start_array[i]
              + TyMsg.getText(29));
                
    } // end-switch
  } // end-for              

} // end-method

/**
 * The startup is to use a DBMS
 * @return boolean 
 */
private boolean usingDBMS() 
         throws Throwable {
  
  // new module to process the DBMS
  StartupDBMS dbms = new StartupDBMS(T, this);
  
  // When the basic part fails, get out
  if  (!dbms.doBase()) return false;
  
  // *--- Queues ---*
  
  // get the local queues
  TymeacIndividualQueue[] tiq = dbms.doQueues();
  
  // When the user Queue load fails, get out
  if  (tiq == null) return false;
  
  // save the queue definitions in common class
  suFields.setTiq(tiq);
    
  // * --- Functions ---*
  
  // load the local functions
  TymeacIndividualFunction[] tif = dbms.doFunctions();
  
  // When the Function acquire fails, get out
  if  (tif == null) return false;
  
  // save the function definitions in common class
  suFields.setTif(tif);
  
  // close up
  dbms.finiDataBase();
  
  // done
  return true;
   
} // end-method

/**
 * The startup is to use the StandAlone (UserClasses) mode
 * @return boolean
 */
private boolean usingStandAlone() 
        throws Throwable {  
    
  // stand alone module
  StartupStandAlone sa = new StartupStandAlone(T, this);
  
  // When the basic part fails, get out
  if  (!sa.doBase()) return false;
  
  // *--- Queues ---*
  
  // get the local queues
  TymeacIndividualQueue[] tiq = sa.doQueues();
  
  // When the user Queue load fails, get out
  if  (tiq == null) return false;
  
  // save the queue definitions in common class
  suFields.setTiq(tiq);
  
  // * --- Functions ---*
  
  // load the local functions
  TymeacIndividualFunction[] tif = sa.doFunctions();
  
  // When the Function acquire fails, get out
  if  (tif == null) return false;
  
  // save the function definitions in common class
  suFields.setTif(tif);
  
  // ok 
  return true;
   
} // end-method
} // end-class
