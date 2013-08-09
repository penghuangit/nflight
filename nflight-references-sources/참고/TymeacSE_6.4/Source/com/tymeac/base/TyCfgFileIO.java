package com.tymeac.base;

/* 
 * Copyright (c) 2004 - 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * The i/o logic for the configuration file 
 */
public final class TyCfgFileIO  {
  
  
/**
 * Static method to read the Configuration file
 * @param dir String - directory
 * @param filename String - file name
 * @param Ty TyCfgFields - data object
 * @return int
 */ 
public static int getFile ( String dir, 
                            String filename, 
                            TyCfgFields Ty) {   
  
  // new xml reader
  TyCfgFileXMLReader xrdr = new TyCfgFileXMLReader(dir, filename, Ty);
  
  // get the fields
  return xrdr.getFile();                             
    
} // end-method

/**
 * Static method to write the Configuration file
 * @param dir String - directory
 * @param filename String - file name
 * @param Ty TyCfgFields - data object
 * @return int
 */ 
public static int putFile ( String dir,
                            String filename,
                            TyCfgFields Ty) {
  
  // write the XML document
  return TyCfgFileXMLWriter.putFile(dir, filename, Ty);
  
} // end-method

/**
 * Static method to write a message
 * @param reason int - reason for message
 * @param dir String - directory
 * @param filename String - file name
 */
public static void putMsg(int reason,
                          String dir, 
                          String filename)  {

  // work
  String msg;

  // When a format error
  if  (reason < 100) {

      // basic msg
      msg = TyCfgStrings.getErrorMsg(reason);
  }
  else {
      // file error
      msg = TyCfgStrings.getErrorMsg(reason) + ", " + dir + ", " + filename;
  
  } // endif

  // out we go
  TyBase.printMsg(msg);

} // end-method
} // end-class
