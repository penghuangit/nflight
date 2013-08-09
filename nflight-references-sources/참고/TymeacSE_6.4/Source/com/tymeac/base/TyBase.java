package com.tymeac.base;

/* 
 * Copyright (c) 2002 - 2013 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.rmi.activation.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.atomic.*; 

/**
 * The base storage for all Tymeac processing. This is the COMMON STORAGE reference
 *   (better known as pointer) anchor-point Class.
 * 
 *   Java does not permit memory management by applications. However, application 
 *   threads need to reference storage (classes) created by other threads. The
 *   purpose of this class is the anchor point for all those references and fields.
 * 
 *   Every Tymeac Class gets a reference to this class. The Tymeac start up primes
 *   the instance fields and any thread, whether a Tymeac management thread, endpoint
 *   connection thread or application thread, has a reference to all the objects.
 * 
 *   If you are familiar with sub-pool memory management then this should be easy for
 *   you. Common Memory is that which is in a shared sub-pool. Try to think of all 
 *   memory as partitioned into three sections:
 * 
 *   1. EndPoint connection threads (from TymeacImpl.class)
 *   2. Tymeac management objects (this TyBase.class (the common objects))
 *   3. Application threads (Queue Threads)
 *   
 *   By sections 1 and 3 having a reference to TyBase.class (section 2), all sections
 *   have references to each others shared objects. 
 * 
 *   The second purpose is to keep a "live" reference to all persistent objects and  
 *   prevent the gargage collector freeing them.
 *   
 *   The third purpose is common methods used by Tymeac objects.
 *
 *   Field "endit", the shutdown indicator. Every [a]syncRequest(), Queue
 *   Threads and many other modules check this field for being shut down.
 *   The field is volitile. Reads are not synchronized. Writes may be synchronized 
 *   using this class as the monitor when necessary.
 * 
 * *--- Manually Maintained ---*
 * 
 *   Tymeac version is manually maintained. It needs to go
 *   somewhere and for now this is the place.
 *     
 *   The timeout fields are manually maintained in setter methods.
 * 
 *   If any user application (like an exit class) needs access to any field herein,
 *   then add a method to the TymeacInfo() class.  
 */
public final class TyBase {

// *--- server status see TymeacInfo for constants ---*
    private volatile int endit = 0; // shut down indicator (set multiple times in sync blocks)

// *--- base Tymeac objects ---*     
    private TymeacImpl    impl        = null;  // Impl of tymeac interface 
    private AreaList      main_tbl    = null;  // Main table of Queues
    private FuncHeader    func_tbl    = null;  // Function table 
    private RequestHeader request_tbl = null;  // Array of requests    
    private StallHeader   stall_tbl   = null;  // Stall table
    private GenTable      gen_tbl     = null;  // Number generation table
    private Monitor       monitor     = null;  // The Monitor
    private TyLogTable    log_tbl     = null;  // msg log class
    private TyNotifyTable notify_tbl  = null;  // notification class
    private TyStatsTable  stats_tbl   = null;  // end-of-day stats
    private TymeacInfo    tyInfo      = null;  // info about me
    @SuppressWarnings("unused")
    private Object        tyuser      = null;  // user (named) singleton as an Object

// base class loader for Queue PAC's when not using URL's
    private TyClassLoader loader = null; 
    
// [Remote] Object myself
    private TymeacInterface Ti = null;
    
// *--- Strings arrays for exits ---* 
    // start up classes are used in start up only, no need to keep here
    private String[] start_array = null; // start up functions (once used, set to null)
    private String[] shut1_array = null; // shutdown stage 1 classes
    private String[] shut2_array = null; // shutdown stage 2 classes 
    
/* *--- List of start-up/shut-down URL Class Loaders, if used ---*   
 * 
 *   If an exit class is loaded by a URL Class Loader, then
 *     a reference to that loader is saved here so the loader does not
 *     get garbage collected. The first save creates this list. There is no 
 *     reason for Tymeac to reference it, so there is no getter.
 *     
 *   Only one thread executes the start up exit Classes. Although multiple requests 
 *     for shut down are supported, only one thread at a time may execute a shut
 *     down exit therefore synchronization is not required. 
 */   
    private ArrayList<URLClassLoader> exit_list = null;
    
/*  *--- Shut down Stage 1 complete indicator ---*   
 * 
 *   As said above, "Although multiple request for shut down are supported, 
 *     only one thread at a time may execute a shut down exit ..."
 *   
 *   The purpose of this boolean is to restrict multiple threads from trying
 *     to execute the same stage of shut down. Once in Stage 1, no other requests
 *     for shut down are honorred until this boolean is set to true. 
 *     
 *   The main purpose is to restrict access to the stage 1 classes. We
 *     wouldn't want a thread to start executing the stage 2 exit classes until
 *     the stage 1 exit classes finish. Restricting access to the stage 2 exit
 *     classes is done below.     
 */    
    private boolean done1 = false;
    
/* *--- Server is finished indicator ---*   
 * 
 *   As said above, "Although multiple request for shut down are supported, 
 *     only one thread at a time may execute a shut down exit ..."
 *     
 *   The purpose is to restrict access to the stage 2 shut down procedure.
 *     Stage 2 shut down executes the Stage 2 user exit Classes (if present),
 *     writes statistics etc. These are the final steps before the Server process
 *     ends. Only one thread may execute these steps since they must be done in
 *     order and only once. Hence, atomic.     
 */    
    private AtomicBoolean done2 = new AtomicBoolean(false);
   
// *--- long for start up time ---*
    private long start_time = 0;  
   
// *--- the monitor interval in seconds, zero don't use ---*
    private int monitor_interval = 0; 
    
// *--- minutes to inactivate when in activation mode, zero don't use ---*
    private int inactivateMinutes = 0;     

// *--- activation ID ---*
    private ActivationID TyActivation = null;
   
// *--- printing or not ---*
    private int sysout = 1; // 0-no, 1-yes 
    
// *--- running in stand a lone mode or DBMS mode    
    private boolean stand_a_lone = true; 
    
/* *--- Timeout values ---*
 * 
 * These are set in the setters and are driven by the monitor_interval.
 *   Since the monitor_interval may be zero during testing, we force
 *   the max value here as a default. See the Tymeac Documentation,
 *   "How threads become disabled" for info about these timeouts.
 */    
    // Processing Application Class default timeout. May be overridden by
    //  individual PAC in Queue creation and dynamically on the QueData screen.
    private int pac_timeout = Integer.MAX_VALUE; 
    
    // Thread in "thread processing" stage timeout
    private int processing_timeout = Integer.MAX_VALUE;  
    
    // In link to scheduler timeout
    private int scheduling_timeout = Integer.MAX_VALUE;  
    
    // Thread posted timeout (notify() or signal())
    private int posted_timeout = Integer.MAX_VALUE;  
    
    // Thread started timeout
    private int started_timeout = Integer.MAX_VALUE;     
    
    // minutes in stall array before new message
    private int time_stalled = Integer.MAX_VALUE;
    
/* *--- Timeout values for internal processes ---* */ 
    
    // excessive time in async processing, 5 minutes seems excessive
    private long async_timeout = 300000L; 
    
    // excessive time to set reset status, 3 minutes seems excessive
    private long reset_timeout = 180000L;    
   
// *--- local switches ---*
    private boolean logUsed    = false;  // use logging (may be reset)
    private boolean sysexit    = false;  // use shut thread 
    private boolean statsUsed  = false;  // statistics in effect (may be reset)    
    private boolean notifyUsed = false;  // notification in effect      

// *--- reference stuff  see TymeacInfo for constants ---*
    private final String version = "6.4"; // current version *-- manually maintained --*
    
    private int shutOrigin = 0; // shutdown initiated by (set once)
    private int startType  = 0; // type of Server (set once) 
    
// Port for binding rmi, -1 is don't use    
    private int port = -1;
    
// *--- Strings for names ---*
    private String notify_name   = null; // notification function name
    private String login_context = null; // security login context name     
    
// *--- Strings for DBMS names ---* 
    private String DBURL      = null; // URL
    private String DBDriver   = null; // Driver Manager
    private String DBUserid   = null; // User Id
    private String DBPassword = null; // Password (not encrypted)
    private String DBESD      = null; // data base embedded shut down class name
    
/*     
 * *--- JINI FIELDS ---*
 * These fields are saved as Class Object since not all implementations use
 *   Jini and may not have the net.jini packages.
 */     
    // *--- JINI ---* cast this to a net.jini.lookup.JoinManager
    private Object joinManager = null;   // JoinManager for jini service
    
    // *--- JINI ---* cast this to a net.jini.config.Configuration
    private Object config = null;   // Configuration provider for jini
    
    // *--- JINI ---* 
    private String configComponent = null; // jini configuration component

/*
 * *--- Preference Keys ---*
 * These constants are for use with the DBMS mode. Tymeac looks for a configuration
 * file for congiguration data. When not found, Tymeac then looks for preference
 * (java.util.prefs.*) data. These are the names of the key fields.  
 * 
 */   
    public final static String PREF_URL             = "dbms_url";
    public final static String PREF_DATA_MANAGER    = "dbms_data_manager";  
    public final static String PREF_USER_NAME       = "dbms_user_name";  
    public final static String PREF_PASSWORD        = "dbms_password";   
    public final static String PREF_FUNC_TABLE      = "dbms_func_table";  
    public final static String PREF_LIST_TABLE      = "dbms_list_table";  
    public final static String PREF_QUEUE_TABLE     = "dbms_queue_table";  
    public final static String PREF_LOG_TABLE       = "dbms_log_table";  
    public final static String PREF_STATS_TABLE     = "dbms_stats_table";  
    public final static String PREF_NOTIFY          = "notify_name";  
    public final static String PREF_SYS_EXIT        = "sys_exit";  
    public final static String PREF_MON_INTERVAL    = "monitor_interval"; 
    public final static String PREF_ACT_INTERVAL    = "activation_interval";  
    public final static String PREF_START_CLASSES   = "start_classes";  
    public final static String PREF_START_FUNCTIONS = "start_functions";  
    public final static String PREF_SHUT1_CLASSES   = "shut1_classes";  
    public final static String PREF_SHUT2_CLASSES   = "shut2_classes";  
    public final static String PREF_STATS_DIR       = "stats_dir";  
    public final static String PREF_STATS_FILE      = "stats_filename";  
    public final static String PREF_LOG_DIR         = "log_dir";  
    public final static String PREF_LOG_FILE        = "log_filename";  
    public final static String PREF_ALT_STATS_CLASS = "alt_stats_class";  
    public final static String PREF_ALT_LOG_CLASS   = "alt_log_class";  
    public final static String PREF_LOGIN_CONTEXT   = "login_context"; 
 
/**
 * Class method to print a message
 * @param msg
 */
public static void printMsg(String msg) {

  // print the message
  System.out.println(msg);

} // end-method     

/**
 * Constructor.
 */
public TyBase ( ) {
            
  // the time of start-up in milliseconds
  start_time = System.currentTimeMillis();
  
} // end-constructor

/**
 * excessive time limit for async processing
 * @return
 */
public long getAsync_timeout() { return async_timeout; }

/**
 * Get the Jini Config Component
 * @return String config component
 */
public String getConfigComponent() { return configComponent; } // end-method

/**
 * Get the Jini Config Provider
 * @return Object config provider cast to object
 */
public Object getConfigProvider() { return config; } // end-method

/**
 *  Get the data base URL 
 * @return
 */
public String getDBURL() { return DBURL; } // end-method

/**
 * Get the data base Driver Manager 
 * @return
 */
public String getDBDriver() { return DBDriver; } // end-method

/**
 * Get the data base Userid 
 * @return
 */
public String getDBUserid() { return DBUserid; } // end-method

/**
 * Get the data base password 
 * @return
 */
public String getDBPassword() { return DBPassword; } // end-method

/**
 * Get the Shut down embedded data base 
 * @return
 */
public String getDBShut() { return DBESD; } // end-method

/**
 * Get the Shut down indicator
 * @return
 */
public int getEndit() { return endit; } // end-method

/**
 * Function array getter
 * @return
 */
public FuncHeader getFunc_tbl() { return func_tbl; } // end-method

/**
 * Number Generation getter 
 * @return
 */
public GenTable getGen_tbl() { return gen_tbl; } // end-method

/**
 * Get the the real implimentation class
 * @return
 */
public TymeacImpl getImpl() { return impl; } // end-method

/**
 * When to inactivate
 * @return
 */
public int getInactivateMinutes() { return inactivateMinutes; } // end-method

/**
 * Get the Jini Join Manager
 * @return Object joinManager
 */
public Object getJoinManager() { return joinManager; } // end-method

/**
 * PAC class loader when not using URL class loader
 * @return
 */
public TyClassLoader getLoader() { return loader; } // end-method

/**
 * Get the Logging class
 * @return
 */
public TyLogTable getLog_tbl() { return log_tbl; } // end-method

/**
 * Get the login context
 * @return String login context
 */
public String getLoginContext() { return login_context; } // end-method

/**
 * Get the Main array of Queues
 * @return
 */
public AreaList getMain_tbl() { return main_tbl; } // end-method

/**
 * Get the The monitor class
 * @return
 */
public Monitor getMonitor() { return monitor; } // end-method

/**
 * Get the Monitor interval
 * @return
 */
public int getMonitor_interval() { return monitor_interval; } // end-method

/**
 * Get the Notification class
 * @return
 */
public TyNotifyTable getNotify_tbl() { return notify_tbl; } // end-method

/**
 * Get the notification name
 * @return String notify
 */
public String getNotifyName() { return notify_name; } // end-method

/**
 * Get the rmi port
 * @return int port
 */
public int getPort() { return port; } // end-method  

/**
 * excessive time limit to set reset status
 * @return
 */
public long getReset_timeout() { return reset_timeout; }

/**
 * Get the Shut down stage 1 classes
 * @return
 */
public String[] getShut1_array() { return shut1_array; } // end-method

/**
 * Get the Shut down stage 2 classes
 * @return
 */
public String[] getShut2_array() { return shut2_array; } // end-method

/**
 * Get the origin of shutdown
 * @return int origin
 */
public int getShutOrigin() { return shutOrigin; } // end-method

/**
 * Get the Stall array
 * @return
 */
public StallHeader getStall_tbl() { return stall_tbl; } // end-method

/**
 * Get the Start up functions
 * @return
 */
public String[] getStart_array() { return start_array; } // end-method 

/**
 * Tymeac start up time getter
 *  @return
 */
public long getStart_time() { return start_time; } // end-method 

/**
 * Get the Type of server
 * @return int start up type
 */
public int getStartType() { return startType; } // end-method

/**
 * Statistics array getter
 * @return
 */
public TyStatsTable getStats_tbl() { return stats_tbl; } // end-method

/**
 * Get the Printing indicator
 * @return
 */
public int getSysout() { return sysout; } // end-method 

/**
 * Get the [remote] object, myself getter
 * @return
 */
public TymeacInterface getTi() { return Ti; } // end-method 

/**
 * time in stall array before new message
 * @return
 */
public int getTime_stalled() { return time_stalled; }

/**
 * Activation id getter
 * @return
 */
public ActivationID getTyActivation() { return TyActivation; } // end-method 

/**
 * Information class getter
 * @return
 */
public TymeacInfo getTyInfo() { return tyInfo; } // end-method

/**
 * Get the current version
 * @return String version
 */
public String getVersion() { return version; } // end-method  
 
/**
 * When using a log repository
 *  @return
 */
public boolean isLogUsed() { return logUsed; } // end-method

/**
 * When using the notify function
 * @return
 */
public boolean isNotifyUsed() { return notifyUsed; } // end-method 

/**
 * Is the server done?
 * @return final shut down status
 */
public boolean isServerDone() { return done2.get(); } // end-method

/**
 * Did stage 1 shut down complete?
 * @return stage 1 shut down status
 */
public boolean isStage1Done() { return done1; } // end-method

/**
 * Stand a lone mode or DBMS mode
 * @return run mode 
 */
public boolean isStandALone() { return stand_a_lone; } // end-method

/**
 * When using a statistics repository
 * @return
 */
public boolean isStatsUsed() { return statsUsed; } // end-method

/**
 * When using the shut down thread
 * @return
 */
public boolean isSysexit() { return sysexit; } // end-method

/**
 * Set the Jini config component
 * @param component
 */
public void setConfigComponent(String component) { configComponent = component; } // end-method

/**
 * Set the Jini config provider
 * @param configProvider
 */
public void setConfigProvider(Object configProvider) { config = configProvider; } // end-method

/**
 * Set run mode DBMS
 */
public void setDBMSMode() { stand_a_lone = false; } // end-method 

/**
 * db URL
 * @param strings
 */
public void setDBUrl(String string) { DBURL = string; } // end-method 

/**
 * db Driver Manager
 * @param strings
 */
public void setDBDriver(String string) { DBDriver = string; } // end-method 

/**
 * db Userid
 * @param strings
 */
public void setDBUserid(String string) { DBUserid = string; } // end-method 

/**
 * db password
 * @param strings
 */
public void setDBPassword(String string) { DBPassword = string; } // end-method 

/**
 * Shutdown embedded db class name
 * @param strings
 */
public void setDBShut(String string) { DBESD = string; } // end-method 

/**
 * Shut down 
 * @param i
 */
public void setEndit(int i) { endit = i; } // end-method 

/**
 * Start up/shut down URL class loaders
 * @param URLClassLoader
 */
public void setExit_list(URLClassLoader loader) { 
  
  // When not there, get one
  if  (exit_list == null) exit_list = new ArrayList<URLClassLoader>();
  
  // add a new entry
  exit_list.add(loader);
  
} // end-method 

/**
 * Function array
 * @param header
 */
public void setFunc_tbl(FuncHeader header) { func_tbl = header; } // end-method 

/**
 * Number generation class
 * @param table
 */
public void setGen_tbl(GenTable table) { gen_tbl = table; } // end-method 

/**
 * Real implementation class
 * @param impl
 */
public void setImpl(TymeacImpl impl) { this.impl = impl; } // end-method 

/**
 * Inactivation time in minutes
 * @param i
 */
public void setInactivateMinutes(int i) { inactivateMinutes = i; } // end-method 

/**
 * Set the Jini Join Manager
 * @param joinManager
 */
public void setJoinManager(Object obj) { joinManager = obj; } // end-method

/**
 * PAC Class loader
 * @param loader
 */
public void setLoader(TyClassLoader loader) { this.loader = loader; } // end-method 

/**
 * Logging class 
 * @param table
 */
public void setLog_tbl(TyLogTable table) { log_tbl = table; } // end-method 

/**
 * Security login context
 * @param string
 */
public void setLogin_context(String string) { login_context = string; } // end-method 

/**
 * Set the login context
 * @param name String
 */
public void setLoginContext(String name) { login_context = name; } // end-method

/**
 * Using a log
 * @param b
 */
public void setLogUsed(boolean b) { logUsed = b; } // end-method 

/**
 * Main array of Queues
 * @param storage
 */
public void setMain_tbl(AreaList storage) { main_tbl = storage; } // end-method 

/**
 * Tymeac Monitor
 * @param monitor
 */
public void setMonitor(Monitor monitor) { this.monitor = monitor; } // end-method 

/**
 * Monitor interval in seconds
 * @param i
 */
public void setMonitor_interval(int i) { 
  
  monitor_interval = i;
  
  // set the default timeout values that depend on the monitor interval
  setPac_timeout();
  setPosted_timeout();
  setProcessing_timeout();
  setScheduling_timeout();
  setStarted_timeout(); 
  setStalled_timeout();
  
} // end-method 

/**
 * Notification class
 * @param table
 */
public void setNotify_tbl(TyNotifyTable table) { notify_tbl = table; } // end-method 
    
/**
 * Set the notify name
 * @param name String
 */
public void setNotifyName(String name) { notify_name = name; } // end-method

/**
 * Using notificaion
 * @param b
 */
public void setNotifyUsed(boolean b) { notifyUsed = b; } // end-method 

/**
 * Set the rmi port
 * @param port int
 */
public void setPort(int rmiPort) { port = rmiPort; } // end-method

/**
 * Set the Server in final stage of shut down
 * @return Server finished or not
 */
public boolean setServerDone() { 
  
  // When CAS failed, someone got here first
  return done2.compareAndSet(false, true);  
  
} // end-method

/**
 * Shutdown stage 1 class array
 * @param strings
 */
public void setShut1_array(String[] strings) { shut1_array = strings; } // end-method 

/**
 * Shutdown stage 2 class array
 * @param strings
 */
public void setShut2_array(String[] strings) { shut2_array = strings; } // end-method 

/**
 * Shutdown stage 1 finished
 */
public void setShut1_done() {  done1 = true; } // end-method 

/**
 * Set the origin of shutdown
 * @param origin int
 */
public void setShutOrigin(int origin) { shutOrigin = origin; } // end-method

/**
 * Stall array
 * @param header
 */
public void setStall_tbl(StallHeader header) { stall_tbl = header; } // end-method 

/**
 * set in stall array time for new message calc
 * time is 10 minutes or 600 seconds, but the actual number depends
 * on the monitor interval
 */
private void setStalled_timeout() {
  
  // When an even minute
  if  (monitor_interval == 60) {
    
      // set 10 times one minute
      time_stalled = 600;
      
      return;      
    
  } // endif
  
  // When > a minute
  if  (monitor_interval > 60) {
    
      // number of minutes more than 1 minute
      //  ignoring not full minutes
      int min = monitor_interval / 60;
      
      // set about 10 minutes
      time_stalled = 600 / min;
      
      return;
    
  } // endif
  
  // number of seconds less than 1 minute
  int min = 60 / monitor_interval;
  
  // set about 10 minutes
  time_stalled = 600 * min;      
  
} // end-method

/**
 * Set run mode stand a lone 
 */
public void setStandALoneMode() { stand_a_lone = true; } // end-method 

/**
 * Start up function array
 * @param strings
 */
public void setStart_array(String[] strings) { start_array = strings; } // end-method 

/**
 * Set the Type of server
 * @param start up type int
 */
public void setStartType(int type) { startType = type; } // end-method

/**
 * Statistics class
 * @param table
 */
public void setStats_tbl(TyStatsTable table) { stats_tbl = table; } // end-method 

/**
 * Using statistics repository
 * @param b
 */
public void setStatsUsed(boolean b) { statsUsed = b; } // end-method 

/**
 * Using shut down thread
 * @param b
 */
public void setSysexit(boolean b) { sysexit = b; } // end-method 

/**
 * Using printing
 * @param i
 */
public void setSysout(int i) { sysout = i; } // end-method 

/**
 * [remote] object myself
 * @param interface1
 */
public void setTi(TymeacInterface interface1) { Ti = interface1; } // end-method 

/**
 * Activation ID
 * @param activationID
 */
public void setTyActivation(ActivationID activationID) { TyActivation = activationID; } // end-method 

/**
 * Information class
 * @param info
 */
public void setTyInfo(TymeacInfo info) { tyInfo = info; } // end-method
 
/**
 * User singleton class
 * @param singleton
 */
public void setTyuser(Object singleton) { tyuser = singleton; } // end-method

/**
 * request array
 * @return header
 */
public RequestHeader getRequest_tbl() { return request_tbl; } // end-method

/**
 * request array
 * @param header
 */
public void setRequest_tbl(RequestHeader request) { request_tbl = request; } // end-method

/**
 * get pac timeout
 * @return int p.a.c. timeout
 */
public int getPac_timeout() { return pac_timeout; } // end-method

/**
 * get posted timeout
 * @return int posted timeout
 */
public int getPosted_timeout() { return posted_timeout; }// end-method

/**
 * get thread processing timeout
 * @return int thread processing timeout
 */
public int getProcessing_timeout() { return processing_timeout; } // end-method

/**
 * get in scheduling timeout
 * @return int in scheduling timeout
 */
public int getScheduling_timeout() { return scheduling_timeout; } // end-method

/**
 * get thread started timeout
 * @return int thread started timeout
 */
public int getStarted_timeout() { return started_timeout; } // end-method

/**
 * set p.a.c. timeout calc
 */
private void setPac_timeout() {
  
  // When monitor interval < 1 minute
  if  (monitor_interval < 60) {
      
      // use 1 minute as base
      pac_timeout = 60 * 4;      
  }
  else {
      // use mon interval as base
      pac_timeout = monitor_interval * 4;
    
  } // endif
  
} // end-method

/**
 * set thread posted timeout calc
 */
private void setPosted_timeout() {
  
  // When monitor interval < 1 minute
  if  (monitor_interval < 60) {
      
      // use 1 minute as base
      posted_timeout = 60 * 2;      
  }
  else {
      // use mon interval
      posted_timeout = monitor_interval * 2;
    
  } // endif
  
} // end--method

/**
 * set thread processing timeout calc
 */
private void setProcessing_timeout() {
  
  // When monitor interval < 1 minute
  if  (monitor_interval < 60) {
      
      // use 1 minute as base
      processing_timeout = 60 * 2;      
  }
  else {
      // use mon interval
      processing_timeout = monitor_interval * 2;
    
  } // endif
  
} // end-method

/**
 * set thread in scheduling timeout calc
 */
private void setScheduling_timeout() {
  
  // When monitor interval < 1 minute
  if  (monitor_interval < 60) {
      
      // use 1 minute
      scheduling_timeout = 60;      
  }
  else {
      // use mon interval
      scheduling_timeout = monitor_interval;
    
  } // endif
  
} // end-method

/**
 * set thread started timeout calc
 */
private void setStarted_timeout() {
  
  // When monitor interval < 1 minute
  if  (monitor_interval < 60) {
      
      // use 1 minute
      started_timeout = 60 * 2;      
  }
  else {
      // use mon interval
      started_timeout = monitor_interval * 2;
    
  } // endif
  
} // end-method
} // end-class
