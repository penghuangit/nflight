package com.tymeac.base;


/* 
 * Copyright (c) 2004 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * This Singleton class contains information about the currently executing Tymeac Server.
 * 
 * If you need additional information from the Tymeac base class, TyBase, then add 
 * methods here for your non-standard usage or make a formal request in SourceForge. 
 * 
 *   Some have indicated a desire for the logging objects so that applications may
 *   log messages to the same repository as the Tymeac Server.
 * 
 *   There is system logging and there is application logging. Using the Java logging
 *   API is not difficult and this is where applications should put their log.   
 */
public final class TymeacInfo {

// private instance fields

  // static reference to this Singleton
  private static TymeacInfo me = null;
  
  // Server base storage 
  private TyBase base = null;
      
// public constants
      
  // shutdown initiated from (from TyBase.shutOrigin)
  public final static int DEACTIVATION    = 1; // from inactivation
  public final static int SHUT_REQUEST    = 2; // from request

  // type of Server (from TyBase.startType)
  public final static int NOT_STARTED  = 0;  // initializing      
  public final static int NON_ACT_RMI  = 1;  // non-activatable RMI
  public final static int ACT_RMI      = 2;  // Activation RMI      
  public final static int IIOP         = 3;  // IIOP start up      
  public final static int INTERNAL     = 4;  // Internal Server 
  public final static int NON_ACT_CSF  = 5;  // non-activatable Custom Socket Factory RMI        
  public final static int ACT_CSF      = 6;  // Activatable Custom Socket Factory RMI      
  public final static int NON_ACT_JINI = 7;  // non_activatable Jini
  public final static int ACT_JINI     = 8;  // Activatable Jini
  public final static int ACT_CSF_JINI = 9;  // Activatable CSF Jini
  public final static int IIOP_POA     = 10; // IIOP as a POA
    
  // current status of Server (from TyBase.endit)
  public final static int INITIALIZATION = 0;  // in init
  public final static int AVAILABLE      = 1;  // normal processing
  public final static int SHUT_STAGE1    = 8;  // shutdown stage 1
  public final static int SHUT_STAGE2    = 16; // shutdown stage 2
  public final static int SHUT_FORCE     = 32; // shutdown force
  public final static int SHUT_COMPLETE  = 64; // shutdown complete

/**
 * Private Constructor, can only be called by the class itself.
 */
private TymeacInfo() {

} // end-constructor

/**
 * Public static method to return the object. During Tymeac start up only one thread
 *   may execute this public method therefore, no sync statement is necessary.
 * @return TymeacInfo
 */
public static TymeacInfo getInstance() {

  // When not here, get new instance
  if  (me == null)  me = new TymeacInfo();
  
  // instance of
  return me;

} // end-method

/**
 * get the db url
 * @return String
 */
public String getDBUrl() { return base.getDBURL(); } // end-method

/**
 * get the db Driver
 * @return String
 */
public String getDBDriver() { return base.getDBDriver(); } // end-method 

/**
 * get the db userid
 * @return String
 */
public String getDBUserid() { return base.getDBUserid(); } // end-method 

/**
 * get the db password
 * @return String
 */
public String getDBPassword() { return base.getDBPassword(); } // end-method 

/**
 * get the db shut down class
 * @return String
 */
public String getDBShutdown() { return base.getDBShut(); } // end-method  

/**
 * get the Jini Join manager as an object. You must cast this to a JoinManager.
 * @return Object
 */
public Object getJoinManager() { return base.getJoinManager(); } // end-method  

/**
 * Accessor for startup time
 * @return long
 */
public long getMilliTime() { return base.getStart_time(); } // end-method

/**
 * Accessor for the origin of shutdown
 * @return int
 */
public int getShutOrigin() { return base.getShutOrigin(); } // end-method

/**
 * Accessor for the startup type
 * @return int
 */
public int getStartType() { return base.getStartType(); } // end-method

/**
 * Accessor for the current status of the Tymeac Server taken from the shut down
 *   indicator.
 * @return int
 */
public int getStatus() { return base.getEndit(); } // end-method

/**
 * Accessor for the instance of the Tymeac Server
 * @return TymeacInterface
 */
public TymeacInterface getTymeac() { return base.getTi(); } // end-method

/**
 * Accessor for the version number
 * @return java.lang.String
 */
public String getVersion() { return base.getVersion(); } // end-method

/**
 * Set the base storage, only once allowed
 * @param p_base
 */
public void setBase (TyBase p_base) {

  // When not already loaded, do so
  if  (base == null)  base = p_base;
  
} // end-method 
} // end-class
