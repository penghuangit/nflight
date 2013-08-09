package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import javax.swing.*;
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import java.util.*;
import java.util.prefs.*;
import com.tymeac.base.*;
import com.tymeac.client.*;

/**
 *  Backend class to support the TyCfg GUI
 */
public final class TyCfgBean2 {
  
  // security
  private LoginContext loginContext = null;
  
  // file fields
  private String dir, filename;
  private TyCfgFields base = null;

  // get file reason
  private int reason = 0;
        
/**
 * Constructor
 */  
public TyCfgBean2() {
  
  // security
  loginContext = getContext();  
  
} // end-constructor  

/**
 * Read the Configuration File
 * @param dir String - file directory
 * @param filename String - file name
 * @param db1 JTextField - URL
 * @param db2 JTextField - DataManager
 * @param db3 JTextField - User Name
 * @param db4 JTextField - Pass Word
 * @param db5 JTextField - Que Table
 * @param db6 JTextField - Func Table
 * @param db7 JTextField - List Table
 * @param db8 JTextField - Stats Table
 * @param db9 JTextField - Log Table
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param sysexit JCheckBox - sysexit on termination
 * @param notify JTextfield - notification queue
 * @param loginContext JTextfield - security login context
 * @param start_classes JList - startup classes
 * @param start_functions JList - startup functions
 * @param shut1 JList shutdown 1 exit classes
 * @param shut2 JList shutdown 2 exit classes
 * @param statsDir JTextField - stats director
 * @param statsFile JTextField - stats file
 * @param logDir JTextField - log dir
 * @param logFile JTextField - log file
 * @param statsAlt JTextField - alternative stats class
 * @param logAlt JTextField - alternative log class
 */
 
@SuppressWarnings("unchecked")
public void readConfig (String dir,
						String filename, 
						JTextField db1, 
						JTextField db2,
						JTextField db3,
						JTextField db4,
						JTextField db5,
						JTextField db6,
						JTextField db7,
						JTextField db8,
						JTextField db9,
						JTextField monitor,
						JTextField activator,
						JCheckBox sys_exit,
						JTextField notify,
            JTextField login_context,
						JList start_classes,
						JList start_functions,
						JList shut1,
						JList shut2,
						JTextField statsDir,
						JTextField statsFile,
						JTextField logDir,
						JTextField logFile,
						JTextField statsAlt,
						JTextField logAlt) {

	// class of strings used to pass config file lines
	base = new TyCfgFields();
  
  // other fields
  this.dir = dir;
  this.filename = filename;
		
	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doRead();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured access");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doRead();
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);
          System.exit(1);                    
                   
      } // end-catch              
  } // endif
  
	// when no good
	if  (reason > 0) {

		// put the message
		TyCfgFileIO.putMsg(reason, dir, filename);

		// done
		return;

	} // endif

	// set dbms data
	db1.setText(base.dbms_URL);
	db2.setText(base.dbms_DataManager);
	db3.setText(base.dbms_UserName);
	db4.setText(base.dbms_PassWord);
	db5.setText(base.dbms_QueTable);
	db6.setText(base.dbms_FuncTable);
	db7.setText(base.dbms_ListTable);
	db8.setText(base.dbms_Stats);
	db9.setText(base.dbms_Log);

	// set monitor data
	monitor.setText(base.mon_Interval);

	// set activation data
	activator.setText(base.act_Interval);

	// set system exit data
	if  (base.sys_exit.equalsIgnoreCase("yes")) {
			  
		sys_exit.setSelected(true);
	}
	else {
		sys_exit.setSelected(false);

	} // endif

	// set notify function data
	notify.setText(base.func_Notify);
  
  // set login context data
  login_context.setText(base.loginContext);  

	// work
	int i, tot_strings;
	String[] obj = null;

	// When there are start classes
	if  ((base.start_classes != null) &&
		 	 (base.start_classes.length > 0)) {

  		// remove all currently
  		start_classes.removeAll();
  
  		// total strings 
  		tot_strings = base.start_classes.length;  
  
  		// obj[] for list
  		obj = new String[tot_strings]; 
  
  		// load up the strings
  		for  (i = 0; i < tot_strings; i++) { 
  			  
			  // insert string
			  obj[i] = base.start_classes[i];
  
  		} // end-for
  
  		// update the JList
  		start_classes.setListData(obj);
 
	} // endif

	// When there are start functions
	if  ((base.start_functions != null) &&
		 (base.start_functions.length > 0)) {

  		// remove all currently
  		start_functions.removeAll();
  
  		// total strings 
  		tot_strings = base.start_functions.length;  
  
  		// obj[] for list
  		obj = new String[tot_strings]; 
  
  		// load up the strings
  		for  (i = 0; i < tot_strings; i++) { 
  			  
			  // insert string
			  obj[i] = base.start_functions[i];
  
  		} // end-for
  
  		// update the JList
  		start_functions.setListData(obj);
 
	} // endif

	// When there are stage1  classes
	if  ((base.shut1_classes != null) &&
		  (base.shut1_classes.length > 0)) {

  		// remove all currently
  		shut1.removeAll();
  
  		// total strings 
  		tot_strings = base.shut1_classes.length;  
  
  		// obj[] for list
  		obj = new String[tot_strings]; 
  
  		// load up the strings
  		for  (i = 0; i < tot_strings; i++) { 
  			  
			  // insert string
			  obj[i] = base.shut1_classes[i]; 
  
  		} // end-for
  
  		// update the JList
  		shut1.setListData(obj);
 
	} // endif
  
	// When there are shut2 classes
	if  ((base.shut2_classes != null) &&
		  (base.shut2_classes.length > 0)) {

  		// remove all currently
  		shut2.removeAll();
  
  		// total strings 
  		tot_strings = base.shut2_classes.length;  
  
  		// obj[] for list
  		obj = new String[tot_strings]; 
  
  		// load up the strings
  		for  (i = 0; i < tot_strings; i++) { 
  			  
			  // insert string
			  obj[i] = base.shut2_classes[i]; 
  
  		} // end-for
  
  		// update the JList
  		shut2.setListData(obj);
 
	} // endif

	// set file data
	statsDir.setText(base.statsDir);
	statsFile.setText(base.statsFile);
	logDir.setText(base.logDir);
	logFile.setText(base.logFile);

	// set alt data
	logAlt.setText(base.altLogClass);
	statsAlt.setText(base.altStatsClass);

} // end-method

/**
 * Write the Configuration File
 * @param dir String - file directory
 * @param filename String - file name
 * @param db1 JTextField - URL
 * @param db2 JTextField - DataManager
 * @param db3 JTextField - User Name
 * @param db4 JTextField - Pass Word
 * @param db5 JTextField - Que Table
 * @param db6 JTextField - Func Table
 * @param db7 JTextField - List Table
 * @param db8 JTextField - Stats Table
 * @param db9 JTextField - Log Table
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param sysexit JCheckBox - sysexit on termination
 * @param notify JTextfield - notification queue
 * @param loginContext JTextfield - security login context
 * @param start_classes JList - startup classes
 * @param start_functions JList - startup functions
 * @param shut1 JList shutdown 1 exit classes
 * @param shut2 JList shutdown 2 exit classes
 * @param statsDir JTextField - stats director
 * @param statsFile JTextField - stats file
 * @param logDir JTextField - log dir
 * @param logFile JTextField - log file
 * @param statsAlt JTextField - alternative stats class
 * @param logAlt JTextField - alternative log class
 */ 
public void writeConfig ( String dir,
						  String filename,          
						  JTextField db1,
						  JTextField db2,
						  JTextField db3,
						  JTextField db4,
						  JTextField db5,
						  JTextField db6,
						  JTextField db7,
						  JTextField db8,
						  JTextField db9,
						  JCheckBox sys_exit,
						  JTextField monitor,
						  JTextField activator,
						  JTextField notify,
              JTextField login_context,
						  JList start_classes,
						  JList start_functions,
						  JList stage1,
						  JList stage2,
							JTextField statsDir,
							JTextField statsFile,
							JTextField logDir,
							JTextField logFile,
							JTextField statsAlt,
							JTextField logAlt) {
				
	// work
	int i, count;  

	// class of strings used to pass config file lines
  base = new TyCfgFields();
  
  // other fields
  this.dir = dir;
  this.filename = filename; 

	// *--- put general data ---*

	// When checked
	if  (sys_exit.getAccessibleContext().getAccessibleStateSet().contains(
            javax.accessibility.AccessibleState.CHECKED)) {
  
		// say yes
		base.sys_exit = "yes";
	}
	else {  
		// say no
		base.sys_exit = "no";

	} // endif

	// rest of general data
	base.mon_Interval = monitor.getText();
	base.act_Interval = activator.getText();
	base.func_Notify  = notify.getText();  
  
  // *--- Security Data ---*
  
  // login context data
  base.loginContext = login_context.getText();  

	// *--- Exits Data ---*

	// number in list
	count = start_classes.getModel().getSize(); 

	// When there are strings in the List
	if  (count > 0) {

  		// new array
  		base.start_classes = new String[count];
  
  		// do all the strings
  		for  (i = 0; i < count; i++) {
  
  			// move to a string
  			base.start_classes[i] = (String) start_classes.getModel().getElementAt(i);
  
  		} // end-for
  
  		// When only null, nothing here
  		if  ((count == 1) &&
  				 (base.start_classes[0] == null)) base.start_classes = null;
	
	} // endif
		
	// number in list
	count = start_functions.getModel().getSize(); 

	// When there are strings in the List
	if  (count > 0) {

		// new array
		base.start_functions = new String[count];

		// do all the strings
		for  (i = 0; i < count; i++) {

		  // move to a string
		  base.start_functions[i] = (String) start_functions.getModel().getElementAt(i);

		} // end-for

		// When only null, nothing here
		if  ((count == 1) &&
				 (base.start_functions[0] == null)) base.start_functions = null;
	
	} // endif
		
	// number in list
	count = stage1.getModel().getSize();

	// When there are strings in the List
	if  (count > 0) {

  		// new array
  		base.shut1_classes  = new String[count];
  
  		// do all the strings
  		for  (i = 0; i < count; i++) {
  
  			// move to a string
  			base.shut1_classes[i] = (String) stage1.getModel().getElementAt(i);
  			
  		} // end-for
  
  		// When only null, nothing here
  		if  ((count == 1) &&
  				 (base.shut1_classes[0] == null)) base.shut1_classes = null;
  	
	} // endif

	// number in list
	count = stage2.getModel().getSize();

	// When there are strings in the List
	if  (count > 0) {

  		// new array
  		base.shut2_classes = new String[count];
  
  		// do all the strings
  		for  (i = 0; i < count; i++) {
  
  			// move to a string
  			base.shut2_classes[i] = (String) stage2.getModel().getElementAt(i);
  				
  		} // end-for
  
  		// When only null, nothing here
  		if  ((count == 1) &&
  				 (base.shut2_classes[0] == null)) base.shut2_classes = null;
  	
	} // endif

	// put DBMS data
	base.dbms_URL         = db1.getText();
	base.dbms_DataManager = db2.getText();
	base.dbms_UserName    = db3.getText();
	base.dbms_PassWord    = db4.getText();
	base.dbms_QueTable    = db5.getText();
	base.dbms_FuncTable   = db6.getText();
	base.dbms_ListTable   = db7.getText();
	base.dbms_Stats       = db8.getText();
	base.dbms_Log         = db9.getText();

	// put file data
	base.statsDir 	= statsDir.getText();
	base.statsFile 	= statsFile.getText();
	base.logDir 		= logDir.getText();
	base.logFile 		= logFile.getText();

	// put alt data
	base.altStatsClass 	= statsAlt.getText();
	base.altLogClass	 	= logAlt.getText();
	
	// When not using security     
  if (loginContext == null) {
        
        System.out.println("UNSECURED modification"); 
        
      // non-secure
      doWrite();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured modification");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doWrite();
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
        // say what found
        System.out.println("PrivilegedActionException= " + e);
        System.exit(1);                    
                   
      } // end-catch              
  } // endif
  
	// when no good
	if  (reason > 0) {

		// put the message
		TyCfgFileIO.putMsg(reason, dir, filename);

		// done
		return;

	} // endif
  
} // end-method

/**
 * Get the Configuration Preferences
 * @param dir String - file directory
 * @param filename String - file name
 * @param db1 JTextField - URL
 * @param db2 JTextField - DataManager
 * @param db3 JTextField - User Name
 * @param db4 JTextField - Pass Word
 * @param db5 JTextField - Que Table
 * @param db6 JTextField - Func Table
 * @param db7 JTextField - List Table
 * @param db8 JTextField - Stats Table
 * @param db9 JTextField - Log Table
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param sysexit JCheckBox - sysexit on termination
 * @param notify JTextfield - notification queue
 * @param loginContext JTextfield - security login context
 * @param start_classes JList - startup classes
 * @param start_functions JList - startup functions
 * @param shut1 JList shutdown 1 exit classes
 * @param shut2 JList shutdown 2 exit classes
 * @param statsDir JTextField - stats director
 * @param statsFile JTextField - stats file
 * @param logDir JTextField - log dir
 * @param logFile JTextField - log file
 * @param statsAlt JTextField - alternative stats class
 * @param logAlt JTextField - alternative log class
 */ 
@SuppressWarnings("unchecked")
public void GetConfig (
            JTextField db1, 
            JTextField db2,
            JTextField db3,
            JTextField db4,
            JTextField db5,
            JTextField db6,
            JTextField db7,
            JTextField db8,
            JTextField db9,
            JTextField monitor,
            JTextField activator,
            JCheckBox sys_exit,
            JTextField notify,
            JTextField login_context,
            JList start_classes,
            JList start_functions,
            JList shut1,
            JList shut2,
            JTextField statsDir,
            JTextField statsFile,
            JTextField logDir,
            JTextField logFile,
            JTextField statsAlt,
            JTextField logAlt) {

  // class of strings used to pass config file lines
  base = new TyCfgFields();
      
  // When not using security     
  if (loginContext == null) {
    
        System.out.println("UNSECURED access"); 
        
      // non-secure
      doGet();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured access");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doGet();
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
        // say what found
        System.out.println("PrivilegedActionException= " + e);
        System.exit(1);                    
                   
      } // end-catch              
  } // endif

  // set dbms data
  db1.setText(base.dbms_URL);
  db2.setText(base.dbms_DataManager);
  db3.setText(base.dbms_UserName);
  db4.setText(base.dbms_PassWord);
  db5.setText(base.dbms_QueTable);
  db6.setText(base.dbms_FuncTable);
  db7.setText(base.dbms_ListTable);
  db8.setText(base.dbms_Stats);
  db9.setText(base.dbms_Log);

  // set monitor data
  monitor.setText(base.mon_Interval);

  // set activation data
  activator.setText(base.act_Interval);

  // set system exit data
  if  (base.sys_exit.equalsIgnoreCase("yes")) {
        
    sys_exit.setSelected(true);
  }
  else {
    sys_exit.setSelected(false);

  } // endif

  // set notify function data
  notify.setText(base.func_Notify);
  
  // set login context data
  login_context.setText(base.loginContext);  

  // work
  int i, tot_strings;
  String[] obj = null;

  // When there are start classes
  if  ((base.start_classes != null) &&
       (base.start_classes.length > 0)) {

      // remove all currently
      start_classes.removeAll();
  
      // total strings 
      tot_strings = base.start_classes.length;  
  
      // obj[] for list
      obj = new String[tot_strings]; 
  
      // load up the strings
      for  (i = 0; i < tot_strings; i++) { 
          
        // insert string
        obj[i] = base.start_classes[i];
  
      } // end-for
  
      // update the JList
      start_classes.setListData(obj);
 
  } // endif

  // When there are start functions
  if  ((base.start_functions != null) &&
       (base.start_functions.length > 0)) {

      // remove all currently
      start_functions.removeAll();
  
      // total strings 
      tot_strings = base.start_functions.length;  
  
      // obj[] for list
      obj = new String[tot_strings]; 
  
      // load up the strings
      for  (i = 0; i < tot_strings; i++) { 
          
        // insert string
        obj[i] = base.start_functions[i];
  
      } // end-for
  
      // update the JList
      start_functions.setListData(obj);
 
  } // endif

  // When there are stage1  classes
  if  ((base.shut1_classes != null) &&
       (base.shut1_classes.length > 0)) {

      // remove all currently
      shut1.removeAll();
  
      // total strings 
      tot_strings = base.shut1_classes.length;  
  
      // obj[] for list
      obj = new String[tot_strings]; 
  
      // load up the strings
      for  (i = 0; i < tot_strings; i++) { 
          
        // insert string
        obj[i] = base.shut1_classes[i]; 
  
      } // end-for
  
      // update the JList
      shut1.setListData(obj);
 
  } // endif
  
  // When there are shut2 classes
  if  ((base.shut2_classes != null) &&
       (base.shut2_classes.length > 0)) {

      // remove all currently
      shut2.removeAll();
  
      // total strings 
      tot_strings = base.shut2_classes.length;  
  
      // obj[] for list
      obj = new String[tot_strings]; 
  
      // load up the strings
      for  (i = 0; i < tot_strings; i++) { 
          
        // insert string
        obj[i] = base.shut2_classes[i]; 
  
      } // end-for
  
      // update the JList
      shut2.setListData(obj);
 
  } // endif

  // set file data
  statsDir.setText(base.statsDir);
  statsFile.setText(base.statsFile);
  logDir.setText(base.logDir);
  logFile.setText(base.logFile);

  // set alt data
  logAlt.setText(base.altLogClass);
  statsAlt.setText(base.altStatsClass);

} // end-method

/**
 * Put the Configuration Preferences
 * @param db1 JTextField - URL
 * @param db2 JTextField - DataManager
 * @param db3 JTextField - User Name
 * @param db4 JTextField - Pass Word
 * @param db5 JTextField - Que Table
 * @param db6 JTextField - Func Table
 * @param db7 JTextField - List Table
 * @param db8 JTextField - Stats Table
 * @param db9 JTextField - Log Table
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param sysexit JCheckBox - sysexit on termination
 * @param notify JTextfield - notification queue
 * @param loginContext JTextfield - security login context
 * @param start_classes JList - startup classes
 * @param start_functions JList - startup functions
 * @param shut1 JList shutdown 1 exit classes
 * @param shut2 JList shutdown 2 exit classes
 * @param statsDir JTextField - stats director
 * @param statsFile JTextField - stats file
 * @param logDir JTextField - log dir
 * @param logFile JTextField - log file
 * @param statsAlt JTextField - alternative stats class
 * @param logAlt JTextField - alternative log class
 */ 
public void PutConfig (        
              JTextField db1,
              JTextField db2,
              JTextField db3,
              JTextField db4,
              JTextField db5,
              JTextField db6,
              JTextField db7,
              JTextField db8,
              JTextField db9,
              JCheckBox sys_exit,
              JTextField monitor,
              JTextField activator,
              JTextField notify,
              JTextField login_context,
              JList start_classes,
              JList start_functions,
              JList stage1,
              JList stage2,
              JTextField statsDir,
              JTextField statsFile,
              JTextField logDir,
              JTextField logFile,
              JTextField statsAlt,
              JTextField logAlt) {
        
  // work
  int i, count;  

  // class of strings used to pass config file lines
  base = new TyCfgFields(); 

  // *--- put general data ---*

  // When checked
  if  (sys_exit.getAccessibleContext().getAccessibleStateSet().contains(
            javax.accessibility.AccessibleState.CHECKED)) {
  
    // say yes
    base.sys_exit = "yes";
  }
  else {  
    // say no
    base.sys_exit = "no";

  } // endif

  // rest of general data
  base.mon_Interval = monitor.getText();
  base.act_Interval = activator.getText();
  base.func_Notify  = notify.getText();  
  
  // *--- Security Data ---*
  
  // login context data
  base.loginContext = login_context.getText();  

  // *--- Exits Data ---*

  // number in list
  count = start_classes.getModel().getSize(); 

  // When there are strings in the List
  if  (count > 0) {

      // new array
      base.start_classes = new String[count];
  
      // do all the strings
      for  (i = 0; i < count; i++) {
  
        // move to a string
        base.start_classes[i] = (String) start_classes.getModel().getElementAt(i);
  
      } // end-for
  
      // When only null, nothing here
      if  ((count == 1) &&
           (base.start_classes[0] == null))  base.start_classes = null;
  
  } // endif
    
  // number in list
  count = start_functions.getModel().getSize(); 

  // When there are strings in the List
  if  (count > 0) {

      // new array
      base.start_functions = new String[count];
  
      // do all the strings
      for  (i = 0; i < count; i++) {
  
        // move to a string
        base.start_functions[i] = (String) start_functions.getModel().getElementAt(i);
  
      } // end-for
  
      // When only null, nothing here
      if  ((count == 1) &&
           (base.start_functions[0] == null))  base.start_functions = null;
  
  } // endif
    
  // number in list
  count = stage1.getModel().getSize();

  // When there are strings in the List
  if  (count > 0) {

      // new array
      base.shut1_classes  = new String[count];
  
      // do all the strings
      for  (i = 0; i < count; i++) {
  
        // move to a string
        base.shut1_classes[i] = (String) stage1.getModel().getElementAt(i);
        
      } // end-for
  
      // When only null, nothing here
      if  ((count == 1) &&
           (base.shut1_classes[0] == null))  base.shut1_classes = null;
    
  } // endif

  // number in list
  count = stage2.getModel().getSize();

  // When there are strings in the List
  if  (count > 0) {

      // new array
      base.shut2_classes = new String[count];
  
      // do all the strings
      for  (i = 0; i < count; i++) {
  
        // move to a string
        base.shut2_classes[i] = (String) stage2.getModel().getElementAt(i);
          
      } // end-for
  
      // When only null, nothing here
      if  ((count == 1) &&
           (base.shut2_classes[0] == null))  base.shut2_classes = null;
    
  } // endif

  // put DBMS data
  base.dbms_URL         = db1.getText();
  base.dbms_DataManager = db2.getText();
  base.dbms_UserName    = db3.getText();
  base.dbms_PassWord    = db4.getText();
  base.dbms_QueTable    = db5.getText();
  base.dbms_FuncTable   = db6.getText();
  base.dbms_ListTable   = db7.getText();
  base.dbms_Stats       = db8.getText();
  base.dbms_Log         = db9.getText();  
  
  // put file data
  base.statsDir   = statsDir.getText();
  base.statsFile  = statsFile.getText();
  base.logDir     = logDir.getText();
  base.logFile    = logFile.getText();

  // put alt data
  base.altStatsClass  = statsAlt.getText();
  base.altLogClass    = logAlt.getText();
  
  // When not using security     
  if (loginContext == null) {
    
    System.out.println("UNSECURED modification"); 
    
      // non-secure
      doPut();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured modification");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doPut();
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
        // say what found
        System.out.println("PrivilegedActionException= " + e);
        System.exit(1);                    
                   
      } // end-catch              
  } // endif  
  
} // end-method

 /**
 * Get the context for logging in. This is user defined.
 *  
 */
private LoginContext getContext() {
         
   // new logon Context without call back handler
   return ClientSecurityModule.getBasicContext();    

} // end-method 

/**
 * Executed as privileged or not as privileged
 *
 */
private void doRead() {
                 
  // do the file read and load
  reason = TyCfgFileIO.getFile(dir, filename, base);                 

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doWrite() {
                 
  // do the file read and load
  reason = TyCfgFileIO.putFile(dir, filename, base);                 

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doGet() {

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
  
  // reason is return value
  reason = 0; 

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doPut() {
  
  // Retrieve the system preference node for the package 
  Preferences prefs = Preferences.systemNodeForPackage(com.tymeac.base.Startup.class);
    
  try {
    // kill any already set
    prefs.clear();
    
    // make changes
    prefs.flush();
    
  } // end-try
  
  // not handled
  catch (BackingStoreException e) {}
  
  // set the value of the preference
  prefs.put(com.tymeac.base.TyBase.PREF_URL, base.dbms_URL);
  prefs.put(com.tymeac.base.TyBase.PREF_DATA_MANAGER, base.dbms_DataManager);
  prefs.put(com.tymeac.base.TyBase.PREF_USER_NAME, base.dbms_UserName);
  prefs.put(com.tymeac.base.TyBase.PREF_PASSWORD, base.dbms_PassWord);
  prefs.put(com.tymeac.base.TyBase.PREF_FUNC_TABLE, base.dbms_FuncTable);
  prefs.put(com.tymeac.base.TyBase.PREF_LIST_TABLE, base.dbms_ListTable);
  prefs.put(com.tymeac.base.TyBase.PREF_QUEUE_TABLE, base.dbms_QueTable);
  prefs.put(com.tymeac.base.TyBase.PREF_LOG_TABLE, base.dbms_Log);
  prefs.put(com.tymeac.base.TyBase.PREF_STATS_TABLE, base.dbms_Stats);
  prefs.put(com.tymeac.base.TyBase.PREF_NOTIFY, base.func_Notify);
  prefs.put(com.tymeac.base.TyBase.PREF_SYS_EXIT, base.sys_exit);
  prefs.put(com.tymeac.base.TyBase.PREF_MON_INTERVAL, base.mon_Interval);
  prefs.put(com.tymeac.base.TyBase.PREF_ACT_INTERVAL, base.act_Interval);
  prefs.put(com.tymeac.base.TyBase.PREF_STATS_DIR, base.statsDir);
  prefs.put(com.tymeac.base.TyBase.PREF_STATS_FILE, base.statsFile);
  prefs.put(com.tymeac.base.TyBase.PREF_LOG_DIR, base.logDir);
  prefs.put(com.tymeac.base.TyBase.PREF_LOG_FILE, base.logFile);
  prefs.put(com.tymeac.base.TyBase.PREF_ALT_STATS_CLASS, base.altStatsClass);
  prefs.put(com.tymeac.base.TyBase.PREF_ALT_LOG_CLASS, base.altLogClass);
  prefs.put(com.tymeac.base.TyBase.PREF_LOGIN_CONTEXT, base.loginContext);
  
  int i, max;
  String S = null;
    
  // When start up classes
  if  (base.start_classes != null) {
    
      // numb of elements
      max = base.start_classes.length;
    
      // name
      S = com.tymeac.base.TyBase.PREF_START_CLASSES;
      
      // do all
      for (i = 0; i < max; i++) {
    
        // each instance
        prefs.put(S + i, base.start_classes[i]);
    
      } // end-for
  } // endif
     
  // When start up functions
  if  (base.start_functions != null) {
    
      // numb of elements
      max = base.start_functions.length;
    
      // name
      S = com.tymeac.base.TyBase.PREF_START_FUNCTIONS;
      
      // do all
      for (i = 0; i < max; i++) {
    
        // each instance
        prefs.put(S + i, base.start_functions[i]);
    
      } // end-for
  } // endif
   
  // When shut1 classes
  if  (base.shut1_classes != null) {
    
      // numb of elements
      max = base.shut1_classes.length;    
    
      // name
      S = com.tymeac.base.TyBase.PREF_SHUT1_CLASSES;
      
      // do all
      for (i = 0; i < max; i++) {
    
        // each instance
        prefs.put(S + i, base.shut1_classes[i]);
    
      } // end-for
  } // endif
   
  // When shut2 classes
  if  (base.shut2_classes != null) {
    
      // numb of elements
      max = base.shut2_classes.length;
    
      // name
      S = com.tymeac.base.TyBase.PREF_SHUT2_CLASSES;
      
      // do all
      for (i = 0; i < max; i++) {
    
        // each instance
        prefs.put(S + i, base.shut2_classes[i]);
    
      } // end-for
  } // endif
                          
  // reason is return value
  reason = 0;             

} // end-method
} // end-class
