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
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

/**
 * The i/o logic for the XML configuration file 
 */
public final class TyCfgFileXMLReader 
      extends DefaultHandler  {
  
  // file: directory
  private final String dir; 
  
  // file: file name
  private final String filename; 
  
  // data fields
  private final TyCfgFields base;
  
  // used by invoked method
  
  private boolean general   = false;
  private boolean exits     = false;
  private boolean dbms      = false;    
  private boolean alternate = false;
  private boolean security  = false;
  private boolean tymeac    = false;
  
/**
 * Constructor
 * 
 * @param dir
 * @param filename
 * @param fields
 */  
protected TyCfgFileXMLReader (String dir, String filename, TyCfgFields fields) {
  
  this.dir = dir;
  this.filename = filename;
  base = fields;
  
} // end-constructor

/**
 * Method to read the XML Configuration file
 * 
 * @return int
 */ 
protected int getFile () { 
  
  // Use an instance of ourselves as the SAX event handler
  DefaultHandler handler = this;

  // Use the default validating parser
  SAXParserFactory factory = SAXParserFactory.newInstance();
  factory.setValidating(true);
  
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
    // Parse the input
    SAXParser saxParser = factory.newSAXParser();
    saxParser.parse( new File(newDir + filename), handler );

  } // end-try
  
  catch (ParserConfigurationException t) {
    
    // say why
    System.out.println(t.toString());
      
    // get out
    return 100; 
    
  } // end-catch
  
  catch (IOException t) {
    
    // say why
    System.out.println(t.toString());
      
    // get out
    return 200;
    
  } // end-catch
  
  catch (SAXException t) {
    
    // say why
    System.out.println(t.toString());
      
    // get out
    return 300;
    
  } // end-catch 

  // When all done
  if  ((general)   &&
       (dbms)      &&
       (exits)     &&
       (alternate) &&
       (tymeac)    &&
       (security)) {
              
      // good
       return 0;
  }
  else {
      // type of missing
      if  (!general) return 1;
                
      // type of missing
      if  (!dbms) return 2;

      // type of missing
      if  (!exits) return 3;

      // type of missing
      if  (!alternate) return 4;
      
      // type of missing
      if  (!security) return 5;
      
      // type of missing
      if  (!tymeac) return 6;
      
      // invalid
      return 9;

  } // endif                          
    
} // end-method

/**
 * Start of doc, no logic 
 */
public void startDocument()
throws SAXException {
    
} // end-method

/**
 * End of doc, no logic 
 */
public void endDocument()
    throws SAXException {   
        
} // end-method

/**
 * For every Element in the file, we do some processing. 
 */
public void startElement(String namespaceURI,
                         String sName, // simple name (localName)
                         String qName, // qualified name
                         Attributes attrs)
    throws SAXException {
  
  // element name
  String eName = sName;
  
  // When not there, overlay with qualified name (namespace aware)
  if ("".equals(eName)) eName = qName; 
  
  // When a Tymeac Element
  if  (eName.compareTo(TyCfgStrings.getRoot()) == 0) { 
            
      // say found an element
      tymeac = true;
      
  } // endif 

  // When a General Element
  if  (eName.compareTo(TyCfgStrings.getGeneral()) == 0) { 
      
      // pick up the attributes
      generalElement(attrs);
      
      // say found an element
      general = true;
      
  } // endif 
  
  // When a Security Element
  if  (eName.compareTo(TyCfgStrings.getSecurity()) == 0) { 
      
      // pick up the attributes
      securityElement(attrs);
      
      // say found an element
      security = true;
      
  } // endif 
  
  // When an Exits Element
  if  (eName.compareTo(TyCfgStrings.getExits()) == 0) { 
            
      // say found an element
      exits = true;
      
  } // endif 
  
  // When a Start up Classes Element
  if  (eName.compareTo(TyCfgStrings.getStart_class()) == 0) { 
      
      // pick up the attributes
      startupClassesElement(attrs);
      
  } // endif 
  
  // When a Start up Functions Element
  if  (eName.compareTo(TyCfgStrings.getStart_func()) == 0) { 
      
      // pick up the attributes
      startupFunctionsElement(attrs);
      
  } // endif 
  
  // When a Shut down 1 Element
  if  (eName.compareTo(TyCfgStrings.getShut1_class()) == 0) { 
      
      // pick up the attributes
      shut1ClassesElement(attrs);
      
  } // endif 
  
  // When a Shut down 2 Element
  if  (eName.compareTo(TyCfgStrings.getShut2_class()) == 0) { 
      
      // pick up the attributes
      shut2ClassesElement(attrs);
      
  } // endif
  
  // When a DBMS Element
  if  (eName.compareTo(TyCfgStrings.getDbms()) == 0) { 
      
      // pick up the attributes
      dbmsElement(attrs);
      
      // say found an element
      dbms = true;
      
  } // endif 
  
  // When an Alternate Element
  if  (eName.compareTo(TyCfgStrings.getAlt()) == 0) { 
            
      // say found an element
      alternate = true;
      
  } // endif 
  
  // When a Statistics Element
  if  (eName.compareTo(TyCfgStrings.getStats()) == 0) { 
      
      // pick up the attributes
      statsElement(attrs);
      
  } // endif
  
  // When a Log Element
  if  (eName.compareTo(TyCfgStrings.getLog()) == 0) { 
      
      // pick up the attributes
      logElement(attrs);
      
  } // endif    
    
} // end-method

/**
 * End of element, no logic 
 */
public void endElement(String namespaceURI,
                           String sName, // simple name
                           String qName  // qualified name
                          )
    throws SAXException {
      
        
} // end-method

/**
 * Char in doc, no logic 
 */
public void characters(char buf[], int offset, int len)
    throws SAXException {
            
} // end-method

/**
 * Attributes of the General Element 
 * @param attrs
 */
private void generalElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr name
        String aName = attrs.getLocalName(i); 
        
        // When attr name is a qualified name, overlay 
        if ("".equals(aName)) aName = attrs.getQName(i);
        
        // When sys exit, set
        if  (aName.compareTo(TyCfgStrings.getSysExit()) == 0) base.sys_exit = attrs.getValue(i);
        
        // When monitor interval, set
        if  (aName.compareTo(TyCfgStrings.getMonInterval()) == 0) base.mon_Interval = attrs.getValue(i);
        
        // When activation interval, set 
        if  (aName.compareTo(TyCfgStrings.getDeActInterval()) == 0) base.act_Interval = attrs.getValue(i);
        
        // When notify function, set
        if  (aName.compareTo(TyCfgStrings.getNotify()) == 0) base.func_Notify = attrs.getValue(i);
        
      } // end-for
  } // endif
  
} // end-method

/**
 * Attributes of the Security Element 
 * @param attrs
 */
private void securityElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr name
        String aName = attrs.getLocalName(i); 
        
        // When attr name is a qualified name, overlay 
        if ("".equals(aName)) aName = attrs.getQName(i);
        
        // When login, set
        if  (aName.compareTo(TyCfgStrings.getLogin()) == 0) base.loginContext = attrs.getValue(i);
        
      } // end-for
  } // endif
  
} // end-method

/**
 * Attributes of the Startup Classes Element. Attribute names are ignored. All we need is the class names
 *  contained in the values. 
 * @param attrs
 */
private void startupClassesElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
    
      // new array
      String[] suc = new String[attrs.getLength()];
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr valaue
        suc[i] = attrs.getValue(i);
        
      } // end-for
      
      // set in base fields
      base.start_classes = suc;
      
  } // endif
  
} // end-method

/**
 * Attributes of the Startup Functions Element. Attribute names are ignored. All we need is the class names
 *  contained in the values. 
 * @param attrs
 */
private void startupFunctionsElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
    
      // new array
      String[] suf = new String[attrs.getLength()];
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr valaue
        suf[i] = attrs.getValue(i);
        
      } // end-for
      
      // set in base fields
      base.start_functions = suf;
      
  } // endif
  
} // end-method

/**
 * Attributes of the Shutdown 1 Classes Element. Attribute names are ignored. All we need is the class names
 *  contained in the values. 
 * @param attrs
 */
private void shut1ClassesElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
    
      // new array
      String[] sd1 = new String[attrs.getLength()];
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr valaue
        sd1[i] = attrs.getValue(i);
        
      } // end-for
      
      // set in base fields
      base.shut1_classes = sd1;
      
  } // endif
  
} // end-method

/**
 * Attributes of the Shutdown 2 Classes Element. Attribute names are ignored. All we need is the class names
 *  contained in the values. 
 * @param attrs
 */
private void shut2ClassesElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
    
      // new array
      String[] sd2 = new String[attrs.getLength()];
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr valaue
        sd2[i] = attrs.getValue(i);
        
      } // end-for
      
      // set in base fields
      base.shut2_classes = sd2;
      
  } // endif
  
} // end-method

/**
 * Attributes of the DBMS Element 
 * @param attrs
 */
private void dbmsElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr name
        String aName = attrs.getLocalName(i); 
        
        // When attr name is a qualified name, overlay 
        if ("".equals(aName)) aName = attrs.getQName(i);
        
        // When url, set
        if  (aName.compareTo(TyCfgStrings.getURL()) == 0) base.dbms_URL = attrs.getValue(i);
        
        // When data manager, set
        if  (aName.compareTo(TyCfgStrings.getMgr()) == 0) base.dbms_DataManager = attrs.getValue(i);
        
        // When user name, set 
        if  (aName.compareTo(TyCfgStrings.getName()) == 0) base.dbms_UserName = attrs.getValue(i);
        
        // When user password, set
        if  (aName.compareTo(TyCfgStrings.getPassWord()) == 0) base.dbms_PassWord = attrs.getValue(i);
        
        // When user queue table, set
        if  (aName.compareTo(TyCfgStrings.getQueTable()) == 0) base.dbms_QueTable = attrs.getValue(i);
        
        // When user function table, set
        if  (aName.compareTo(TyCfgStrings.getFuncTable()) == 0) base.dbms_FuncTable = attrs.getValue(i);
        
        // When user list table, set
        if  (aName.compareTo(TyCfgStrings.getListTable()) == 0) base.dbms_ListTable = attrs.getValue(i);
        
        // When user stats table, set
        if  (aName.compareTo(TyCfgStrings.getStatsTable()) == 0) base.dbms_Stats = attrs.getValue(i);
        
        // When user log table, set
        if  (aName.compareTo(TyCfgStrings.getLogTable()) == 0) base.dbms_Log = attrs.getValue(i);
        
      } // end-for
  } // endif
  
} // end-method

/**
 * Attributes of the Statistics Element 
 * @param attrs
 */
private void statsElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr name
        String aName = attrs.getLocalName(i); 
        
        // When attr name is a qualified name, overlay 
        if ("".equals(aName)) aName = attrs.getQName(i);
        
        // When url, set
        if  (aName.compareTo(TyCfgStrings.getStatsDir()) == 0) base.statsDir = attrs.getValue(i);
        
        // When data manager, set
        if  (aName.compareTo(TyCfgStrings.getStatsFile()) == 0) base.statsFile = attrs.getValue(i);
        
        // When user name, set 
        if  (aName.compareTo(TyCfgStrings.getStatsAlt()) == 0) base.altStatsClass = attrs.getValue(i);
        
      } // end-for
  } // endif
  
} // end-method

/**
 * Attributes of the Log Element 
 * @param attrs
 */
private void logElement( Attributes attrs) { 
  
  // When no attribute, nothing to do
  if  (attrs != null) {
             
      // do each attribute in the list
      for (int i = 0, max = attrs.getLength(); i < max; i++) {
       
        // attr name
        String aName = attrs.getLocalName(i); 
        
        // When attr name is a qualified name, overlay 
        if ("".equals(aName)) aName = attrs.getQName(i);
        
        // When url, set
        if  (aName.compareTo(TyCfgStrings.getLogDir()) == 0) base.logDir = attrs.getValue(i);
        
        // When data manager, set
        if  (aName.compareTo(TyCfgStrings.getLogFile()) == 0) base.logFile = attrs.getValue(i);
        
        // When user name, set 
        if  (aName.compareTo(TyCfgStrings.getLogAlt()) == 0) base.altLogClass = attrs.getValue(i);
        
      } // end-for
  } // endif
  
} // end-method
} // end-class
