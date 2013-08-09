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


import java.io.*;
/**
 * The i/o logic for the configuration file 
 */
public final class TyCfgFileXMLWriter  {

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

  // output definition
  FileOutputStream fileOutStream = null;
  DataOutputStream dataOutStream = null;
  
  // append "end-of-line" to the end of each line
  String crlf = System.getProperties().getProperty("line.separator");

  // file sep
  String sep = System.getProperties().getProperty("file.separator"); 

  // file sep stuff
  int len = 0;
  String newDir = null;

  // When no dir
  if  (dir == null) {

      //basic
      newDir = sep;
  }
  else {
      // dir length
      len = dir.length();

      // When last char is not a sep
      if  (dir.substring(0, len).compareTo(sep) != 0) {

          // new dir with a sep
          newDir = dir + sep;
      }
      else {
          // leave alone
          newDir = dir;

      } // endif
  } // endif    
    
  try {
      // open
      fileOutStream = new FileOutputStream(newDir + filename);

  } // end-try
      
  catch (IOException e) {
              
      // to string
      TyBase.printMsg(e.toString());

      // bad open 
      return 1001;
        
  } // end-catch  
  
  // new out stream   
  dataOutStream = new DataOutputStream(fileOutStream);

  // work
  int i, count;
  
  // indent four spaces
  String indent = "    "; 
  
  // appended to keyword, such as name="
  String append = "=\"";
  
  // end quote
  String end = "\"";
      
  try {
    
    // *** - Version - ***
    
    // put version
    dataOutStream.writeBytes(TyCfgStrings.getVersion() + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // *** - Comment - ***
          
    // put comment
    dataOutStream.writeBytes(TyCfgStrings.getComment() + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // *** - Root - ***
    
    // put root element
    dataOutStream.writeBytes("<" + TyCfgStrings.getRoot() + ">" + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf); 
    
    // *** - General - ***
    
    // put General element
    dataOutStream.writeBytes("<" + TyCfgStrings.getGeneral() + crlf);

    // put general attribute data 
    dataOutStream.writeBytes(indent + TyCfgStrings.getSysExit()       + append + Ty.sys_exit     + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getMonInterval()   + append + Ty.mon_Interval + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getDeActInterval() + append + Ty.act_Interval + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getNotify()        + append + Ty.func_Notify  + end + crlf);

    // end of element bracket
    dataOutStream.writeBytes(indent + ">" + crlf);      
    
    // put General element end
    dataOutStream.writeBytes("</" + TyCfgStrings.getGeneral() + ">" + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // *** - Security - ***
    
    // put Security element
    dataOutStream.writeBytes("<" + TyCfgStrings.getSecurity() + crlf);

    // put security attribute data 
    dataOutStream.writeBytes(indent + TyCfgStrings.getLogin()  + append + Ty.loginContext + end + crlf);

    // end of element bracket
    dataOutStream.writeBytes(indent + ">" + crlf);
    
    // put Security element end
    dataOutStream.writeBytes("</" + TyCfgStrings.getSecurity() + ">" + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf);      
    
    // *** - Exits - ***
    
    // put exits element
    dataOutStream.writeBytes("<" + TyCfgStrings.getExits() + ">" + crlf);
    
    // *** - Start up - ***

    // When there are start up exits
    if  (Ty.start_classes != null) {    

        // number in list
        count = Ty.start_classes.length;

        // When there are strings in the List
        if  (count > 0) {
            
            // put start classes element
            dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getStart_class() + crlf);
            
            // do all the strings
            for  (i = 0; i < count; i++) {

              // When something really there
              if  (Ty.start_classes[i].length() > 0) {

                  // write each line
                  dataOutStream.writeBytes(indent + 
                                           indent + 
                                           "SuC-" + i +
                                           "=\"" +
                                           Ty.start_classes[i] + 
                                           "\"" +                 
                                           crlf);

              } // endif
            } // end-for
            
            // end of element bracket
            dataOutStream.writeBytes(indent + indent + ">" + crlf);
            
            // put Start up element end
            dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getStart_class() + ">" + crlf);      
            
        } // endif
    } 
    else {
        // no start up exits
        // put start classes element
        dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getStart_class() +">" + crlf);
        
        // put start up element end
        dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getStart_class() + ">" + crlf); 
      
    } // endif

    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // *** - Start up Functions - ***
    
    // When there are start up functions
    if  (Ty.start_functions != null) {    

        // number in list
        count = Ty.start_functions.length;

        // When there are strings in the List
        if  (count > 0) {
            
            // put start functions element
            dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getStart_func() + crlf);
            
            // do all the strings
            for  (i = 0; i < count; i++) {

              // When something really there
              if  (Ty.start_functions[i].length() > 0) {

                  // write each line
                  dataOutStream.writeBytes(indent + 
                                           indent + 
                                           "SuF-" + i +
                                           "=\"" +
                                           Ty.start_functions[i] + 
                                           "\"" +                 
                                           crlf);

              } // endif
            } // end-for
            
            // end of element bracket
            dataOutStream.writeBytes(indent + indent +  ">" + crlf);
            
            // put Start up element end
            dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getStart_func() + ">" + crlf);      
            
        } // endif
    } 
    else {
        // no start up exits
        // put start classes element
        dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getStart_func() +">" + crlf);
        
        // put start up element end
        dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getStart_func() + ">" + crlf); 
      
    } // endif

    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // *** - Shut down 1  - ***
    
    // When there are shut 1 classes
    if  (Ty.shut1_classes != null) {    

        // number in list
        count = Ty.shut1_classes.length;

        // When there are strings in the List
        if  (count > 0) {
            
            // put start functions element
            dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getShut1_class() + crlf);
            
            // do all the strings
            for  (i = 0; i < count; i++) {

              // When something really there
              if  (Ty.shut1_classes[i].length() > 0) {

                  // write each line
                  dataOutStream.writeBytes(indent + 
                                           indent + 
                                           "Sh1-" + i +
                                           "=\"" +
                                           Ty.shut1_classes[i] + 
                                           "\"" +                 
                                           crlf);

              } // endif
            } // end-for
            
            // end of element bracket
            dataOutStream.writeBytes(indent + indent + ">" + crlf);
            
            // put Start up element end
            dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getShut1_class() + ">" + crlf);      
            
        } // endif
    } 
    else {
        // no start up exits
        // put start classes element
        dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getShut1_class() +">" + crlf);
        
        // put start up element end
        dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getShut1_class() + ">" + crlf); 
      
    } // endif

    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // *** - Shut down 2 - ***
    
    // When there are shut 2 classes
    if  (Ty.shut2_classes != null) {    

        // number in list
        count = Ty.shut2_classes.length;

        // When there are strings in the List
        if  (count > 0) {
            
            // put start functions element
            dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getShut2_class() + crlf);
            
            // do all the strings
            for  (i = 0; i < count; i++) {

              // When something really there
              if  (Ty.shut2_classes[i].length() > 0) {

                  // write each line
                  dataOutStream.writeBytes(indent + 
                                           indent + 
                                           "Sh2-" + i +
                                           "=\"" +
                                           Ty.shut2_classes[i] + 
                                           "\"" +                 
                                           crlf);

              } // endif
            } // end-for
            
            // end of element bracket
            dataOutStream.writeBytes(indent + indent + ">" + crlf);
            
            // put Start up element end
            dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getShut2_class() + ">" + crlf);      
            
        } // endif
    } 
    else {
        // no start up exits
        // put start classes element
        dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getShut2_class() +">" + crlf);
        
        // put start up element end
        dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getShut2_class() + ">" + crlf); 
      
    } // endif

    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // put exits element end
    dataOutStream.writeBytes("</" + TyCfgStrings.getExits() + ">" + crlf);
    
    // *** - DBMS - ***
    
    // put dbms element
    dataOutStream.writeBytes("<" + TyCfgStrings.getDbms() + crlf);

    // put dbms attribute data 
    dataOutStream.writeBytes(indent + TyCfgStrings.getURL()        + append + Ty.dbms_URL         + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getMgr()        + append + Ty.dbms_DataManager + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getName()       + append + Ty.dbms_UserName    + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getPassWord()   + append + Ty.dbms_PassWord    + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getQueTable()   + append + Ty.dbms_QueTable    + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getFuncTable()  + append + Ty.dbms_FuncTable   + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getListTable()  + append + Ty.dbms_ListTable   + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getStatsTable() + append + Ty.dbms_Stats       + end + crlf);
    dataOutStream.writeBytes(indent + TyCfgStrings.getLogTable()   + append + Ty.dbms_Log         + end + crlf);
    
    // end of element bracket
    dataOutStream.writeBytes(indent + ">" + crlf);      
    
    // put dbms element end
    dataOutStream.writeBytes("</" + TyCfgStrings.getDbms() + ">" + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf);
    
    // *** - Alternate - ***
    
    // put alternate element
    dataOutStream.writeBytes("<" + TyCfgStrings.getAlt() + ">" + crlf);
    
       // *** - Stats - ***
    
    // put stats element
    dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getStats() + crlf);

    // put stats attribute data 
    dataOutStream.writeBytes(indent + indent + TyCfgStrings.getStatsDir()  + append + Ty.statsDir      + end + crlf);
    dataOutStream.writeBytes(indent + indent + TyCfgStrings.getStatsFile() + append + Ty.statsFile     + end + crlf);
    dataOutStream.writeBytes(indent + indent + TyCfgStrings.getStatsAlt()  + append + Ty.altStatsClass + end + crlf);

    // end of element bracket
    dataOutStream.writeBytes(indent + indent + ">" + crlf);      
    
    // put stats element end
    dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getStats() + ">" + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf);
    
       // *** - Log - ***
    
    // put log element
    dataOutStream.writeBytes(indent + "<" + TyCfgStrings.getLog() + crlf);

    // put log attribute data 
    dataOutStream.writeBytes(indent + indent + TyCfgStrings.getLogDir()  + append + Ty.logDir      + end + crlf);
    dataOutStream.writeBytes(indent + indent + TyCfgStrings.getLogFile() + append + Ty.logFile     + end + crlf);
    dataOutStream.writeBytes(indent + indent + TyCfgStrings.getLogAlt()  + append + Ty.altLogClass + end + crlf);

    // end of element bracket
    dataOutStream.writeBytes(indent + indent + ">" + crlf);      
    
    // put stats element end
    dataOutStream.writeBytes(indent + "</" + TyCfgStrings.getLog() + ">" + crlf);
    
    // skip a line
    dataOutStream.writeBytes(crlf);    
    
    // put alternate element end
    dataOutStream.writeBytes("</" + TyCfgStrings.getAlt() + ">" + crlf);
    
    // put root element end
    dataOutStream.writeBytes("</" + TyCfgStrings.getRoot() + ">" + crlf);  
    
  } // end-try
      
  catch (IOException e) {
              
      TyBase.printMsg(e.toString());
        
      return 1002;

  } // end-catch                                
    
  try {
      // close
      fileOutStream.close();
      dataOutStream.close();

  } // end-try          
      
  catch (IOException e) {
              
      TyBase.printMsg(e.toString());
        
      return 1003;

  } // end-catch

  // good 
  return 0;
  
} // end-method
} // end-class
