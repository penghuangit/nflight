/* 
 * Copyright (c) 2002 - 2011 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

package com.tymeac.base;

/**
 * String data for the configuration file
 */
public final class TyCfgStrings {
  
/**
 * Get the alternate keyword
 * @return java.lang.String
 */
public static java.lang.String getAlt() { return "ALTERNATE"; } // end-method

/**
 * Get the document comment
 * @return java.lang.String
 */
public static java.lang.String getComment() { return "<!-- Tymeac Configuration --> "; } // end-method

/**
 * Get the DBMS keyword
 * @return java.lang.String
 */
public static java.lang.String getDbms() { return "DBMS"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getURL() { return "DataBaseURL"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getMgr() { return "DriverManager"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getName() { return "UserName"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getPassWord() { return "PassWord"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getQueTable() { return "QueueTable"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getFuncTable() { return "FunctionTable"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getListTable() { return "ListTable"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getStatsTable() { return "StatisticsTable"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getLogTable() { return "LogTable"; } // end-method

/**
 * Get an error message
 * @return java.lang.String
 * @param numb int
 */
public static String getErrorMsg(int numb) { 
  
  // nbr is the msg number
  switch (numb) {

    case 1: return  
      "file format exception: no <GENERAL> Element";

    case 2: return  
      "file format exception: no <DBMS> Element";

    case 3: return  
      "file format exception: no <EXITS> Element";

    case 4: return  
      "file format exception: no <ALTERNATE> Element";

    case 5: return  
      "file format exception: no <SECURITY> Element";
    
    case 6: return  
      "file format exception: no <TYMEAC> Element";

  

    case 100: return  
      "Error parsing file at dir, filename:";
  
    case 200: return  
      "file IOException reading file at dir, filename:";
  
    case 300: return  
      "SAX Exception on file at dir, filename:";
  
    case 1001: return  
      "file creation error at dir + filename";
  
    case 1002: return  
      "file IOException writing";
  
    case 1003: return  
      "file IOException closing";
  
    default: return 
      "Unhandled error in TyCfgStrings class";

  } // end-switch

} // end-method

/**
 * Get the exits keyword
 * @return java.lang.String
 */
public static java.lang.String getExits() { return "EXITS"; } // end-method

/**
 * Get the general keyword
 * @return java.lang.String
 */
public static java.lang.String getGeneral() { return "GENERAL"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getSysExit() { return "SystemExit"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getMonInterval() { return "MonitorInterval"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getDeActInterval() { return "DeActivationInterval"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getNotify() { return "NotificationFunction"; } // end-method

/**
 * Get the log keyword
 * @return java.lang.String
 */
public static java.lang.String getLog() { return "LOG"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getLogDir() { return "LogDirectory"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getLogFile() { return "LogFileName"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getLogAlt() { return "LogAlternativeClass"; } // end-method

/**
 * Get the root keyword
 * @return java.lang.String
 */
public static java.lang.String getRoot() { return "TYMEAC"; } // end-method

/**
 * Get the security word
 * @return java.lang.String
 */
public static java.lang.String getSecurity() { return "SECURITY"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getLogin() { return "LoginContext"; } // end-method

/**
 * Get the shut 1 classes keyword
 * @return java.lang.String
 */
public static java.lang.String getShut1_class() { return "Shut1Classes"; } // end-method

/**
 * Get the shut 2 classesf keyword
 * @return java.lang.String
 */
public static java.lang.String getShut2_class() { return "Shut2Classes"; } // end-method

/**
 * Get the start up classes keyword
 * @return java.lang.String
 */
public static java.lang.String getStart_class() { return "StartUpClasses"; } // end-method

/**
 * Get the start up functions keyword
 * @return java.lang.String
 */
public static java.lang.String getStart_func() { return "StartUpFunctions"; } // end-method

/**
 * Get the statistics keyword
 * @return java.lang.String
 */
public static java.lang.String getStats() { return "STATISTICS"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getStatsDir() { return "StatsDirectory"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getStatsFile() { return "StatsFileName"; } // end-method

/**
 * Get the attribute
 * @return java.lang.String
 */
public static java.lang.String getStatsAlt() { return "StatsAlternativeClass"; } // end-method

/**
 * Get the xml version
 * @return java.lang.String
 */
public static java.lang.String getVersion() { return "<?xml version='1.0' encoding='utf-8'?> "; } // end-method

} // end-class
