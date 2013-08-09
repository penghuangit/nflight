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
import java.io.*;
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import com.tymeac.client.*;

/**
 *  Backend object for the user variables GUI 
 */
public final class TyUserVariablesBean {

  // work
  private int rc;
  private String dir;
  
  // security
  private LoginContext loginContext = null;
  
  private BufferedReader bufferedReader = null;

	private String found;
  
  private int len = 0, i = 0, j = 0, backBad = 0;
  private String backString = null;
  private String[] backList = null;

	private String F_notify = null;
	private String F_sys_exit = null;
	private String F_mon_interval = null;
	private String F_inact_minutes = null;
  
  private String F_login_context = null;
  
  private String F_log_file = null; 
	private String F_log_dir = null;
	private String F_alt_log_class;  
	private String F_stats_file = null;
	private String F_stats_dir = null;
	private String F_alt_stats_class = null;
  
	private String[] F_startup_classes = null;
	private String[] F_startup_functions = null;
	private String[] F_shutdown_stage1_classes = null;
	private String[] F_shutdown_stage2_classes = null;; 

/**
 * Constructor
 *
 */  
public TyUserVariablesBean() {
  
  // security
  loginContext = getContext();
  
} // end-constructor  
  
/**
 * Finish button
 * @param dir String - file directory
 * @param sysexit JCheckBox - sysexit on termination
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param login_context JTextfield - security login context
 * @param notify JTextfield - notification queue
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
public void finishButton ( String dir,
              JCheckBox sys_exit,
              JTextField monitor,
              JTextField activator,
              JTextField notify,
              JTextField context,
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

  // *--- put general data ---*

  // When checked
  if  (sys_exit.getAccessibleContext().getAccessibleStateSet().contains(
            javax.accessibility.AccessibleState.CHECKED)) {
  
    // say yes
    F_sys_exit = "yes";
  }
  else {  
    // say no
    F_sys_exit = "no";

  } // endif

	// work
	String test;

	test = monitor.getText();

	F_mon_interval = (test.length() == 0)? "0" : test;

	test = activator.getText();

	F_inact_minutes = (test.length() == 0)? "0" : test;

	test = notify.getText();

	F_notify = (test.length() == 0)? null : test;
  
// *--- security ---*
  
  test = context.getText();

  F_login_context = (test.length() == 0)? null : test;

  // *--- Exits Data ---*

  // number in list
  count = start_classes.getModel().getSize(); 

  // When there are strings in the List
  if  (count > 0) {

    	// new array
    	F_startup_classes = new String[count];

	    // do all the strings
			for  (i = 0; i < count; i++) {

	      // move to a string
		    F_startup_classes[i] = (String) start_classes.getModel().getElementAt(i);

			} // end-for
			
	  	// When only null
 	  	if  ((count == 1) &&
           (F_startup_classes[0] == null)) {

	        // nothing here
 					F_startup_classes = null;
  
  	  } // endif
	}
	else {
			F_startup_classes = null;

  } // endif
    
  // number in list
  count = start_functions.getModel().getSize(); 

  // When there are strings in the List
  if  (count > 0) {

    // new array
    F_startup_functions = new String[count];

    // do all the strings
    for  (i = 0; i < count; i++) {

      // move to a string
      F_startup_functions[i] = (String) start_functions.getModel().getElementAt(i);

    } // end-for

    // When only null
    if  ((count == 1) &&
         (F_startup_functions[0] == null)) {

        // nothing here
        F_startup_functions = null;
  
    } // endif
	}
	else {
			F_startup_functions = null;
		
  } // endif
    
  // number in list
  count = stage1.getModel().getSize();

  // When there are strings in the List
  if  (count > 0) {

      // new array
      F_shutdown_stage1_classes  = new String[count];
  
      // do all the strings
      for  (i = 0; i < count; i++) {
  
        // move to a string
        F_shutdown_stage1_classes[i] = (String) stage1.getModel().getElementAt(i);
        
      } // end-for
  
      // When only null
      if  ((count == 1) &&
           (F_shutdown_stage1_classes[0] == null)) {
  
          // nothing here
          F_shutdown_stage1_classes = null;
    
      } // endif
	}
	else {
  			F_shutdown_stage1_classes = null;
		
  } // endif

  // number in list
  count = stage2.getModel().getSize();

  // When there are strings in the List
  if  (count > 0) {

    // new array
    F_shutdown_stage2_classes = new String[count];

    // do all the strings
    for  (i = 0; i < count; i++) {

      // move to a string
      F_shutdown_stage2_classes[i] = (String) stage2.getModel().getElementAt(i);
        
    } // end-for

    // When only null
    if  ((count == 1) &&
         (F_shutdown_stage2_classes[0] == null)) {

        // nothing here
        F_shutdown_stage2_classes = null;
  
    } // endif
	}
	else {
			F_shutdown_stage2_classes = null;
		
  } // endif

	test = statsDir.getText();

	F_stats_dir = (test.length() == 0)? null : test;
  
	test = statsFile.getText();

	F_stats_file = (test.length() == 0)? null : test;
  
	test = logDir.getText();

	F_log_dir = (test.length() == 0)? null : test;
  
	test = logFile.getText();

	F_log_file = (test.length() == 0)? null : test;
  
	test = statsAlt.getText();

	F_alt_stats_class = (test.length() == 0)? null : test;
  
	test = logAlt.getText();

	F_alt_log_class = (test.length() == 0)? null : test;

  // save
  this.dir = dir;
  
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
   
} // end-method

/**
 * read the file
 * @param dir String - dir name
 * @return int
 */ 
public int getFile (String dir) {
		 
  String sep = System.getProperties().getProperty("file.separator"); 

	// file sep stuff
	len = 0;
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
		// new reader
		bufferedReader = new BufferedReader(new FileReader(newDir 
                                                      + "TymeacUserVariables.java"));

  } // end-try
		  
  catch (IOException e) {
			
		// print error              
		System.out.println(e);          

		// get out
		return 100; 
				
  } // end-catch 
  
  // find the constructor
  getConstructor(); 
  
  // When no constructor
  if  (backBad > 0) return backBad;      
										
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  
    
// do Notify
  findString();
  
  // save result  
  F_notify = backString;  
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do sys exit
  getSysExit();     

  // When error
  if  (backBad > 0) return backBad;  
  
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do monitor interval
  findInt(); 
  
  // When error
  if  (backBad > 0) return backBad;  
  
  // save
  F_mon_interval = backString;
  
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do inactivity minutes
  findInt(); 

  // When error
  if  (backBad > 0) return backBad; 
  
  // set 
  F_inact_minutes = backString; 
  
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do login context
  findString();
  
  // set
  F_login_context = backString;
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// skip use log
 // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  
      
// do log file
  findString();
  
  // set 
  F_log_file = backString;
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do log dir
  findString();
  
  // set 
  F_log_dir = backString;
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do log alt
  findString();
  
  // set 
  F_alt_log_class = backString;
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// skip use stats
 // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  
      
// do stats file
  findString();
  
  // set 
  F_stats_file = backString;
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do stats dir
  findString();
  
  // set 
  F_stats_dir = backString;
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;  

// do stats alt
  findString();
  
  // set 
  F_alt_stats_class = backString;
    
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;

// do start up classes
  findList(); 

  // When error
  if  (backBad > 0) return backBad; 
  
  // set 
  F_startup_classes = backList;
  
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;
    
 // do start up functions
  findList(); 

  // When error
  if  (backBad > 0) return backBad; 
  
  // set 
  F_startup_functions = backList;
  
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;
       
// do shut down stage 1 classes
  findList(); 

  // When error
  if  (backBad > 0) return backBad; 
  
  // set 
  F_shutdown_stage1_classes = backList;
  
  // next line
  bumpLine(); 

  // When no next line
  if  (backBad > 0) return backBad;
        
// do shut down stage 2 classes
  findList(); 

  // When error
  if  (backBad > 0) return backBad; 
  
  // set 
  F_shutdown_stage2_classes = backList; 	
	  
  try {
		//close
		bufferedReader.close();

  } // end-try          
		  
  catch (IOException e) {
	  
		// print error            
		System.out.println(e);

		// get out
		return 300; 
		  
  } // end-catch  

  // good
  return 0;                             
  
} // end-method

/**
 * find constructor
 */
private void getConstructor() {
  
  // bypass until the constructor
  while (true) {

    try {
      // read a line
      found = bufferedReader.readLine();

      // When at end
      if  (found == null) {

          // all done
          backBad = 2;
          return;

      } // endif

      // When a constructor
      if  (found.equalsIgnoreCase("public TymeacUserVariables() {"))  return;

    } // end-try
    catch (IOException e) {
    
      // print error            
      System.out.println(e);

      // get out
      backBad = 300;
      return; 
      
    } // end-catch        
  } // end-while      

} // end-method

/**
 * get the next line
 */
private void bumpLine() {

  try {
    // read a line
    found = bufferedReader.readLine();
  
  } // end-try
  
  catch (IOException e) {
    
    // print error            
    System.out.println(e);

    // get out
    backBad = 300;
    return; 
      
   } // end-catch       

  // When at end
  if  (found == null) {

      // all done
      backBad = 3;
      return;

  } // endif

  // line length
  len = found.length();
  
  // start
  i = 0;
  backString = null;
  backList   = null;
  
  // good
  return;

} // end-method  

/**
 * find a string
 */
private void findString() {

  // start at zero
  i = 0;

  while (len > 0) {

    // When a quote
    if  (found.substring(i, i+1).startsWith("\""))  {

        //got it
        break;

    } // endif

    i++;
    len--;

  } // end-while

  // When at end
  if  (len <= 0) {

      // is null
      return;

  } // endif
  
  // save first index
  j = ++i;

  while (len > 0) {

    // When a quote
    if  (found.substring(i, i+1).startsWith("\""))  {

        //got it
        break;

    } // endif

    i++;
    len--;

  } // end-while

  // When not at end
  if  (len > 0) {

      // set notify name
      backString = found.substring(j, i);

  } // endif  

} // end-method

/**
 * get sys exit value
 * @return int
 */
private int getSysExit() {
  
  while (len > 0) {

    // When an equal
    if  (found.substring(i, i+1).startsWith("="))  break;

    i++;
    len--;

  } // end-while

  // When at end
  if  (len <= 0) return 4;
  
  // past equal, space
  i += 2;
 
  // set sys-exit
  String temp = found.substring(i, i+1);

  F_sys_exit = (temp.compareTo("1") == 0)? "yes" : "no";
          
  // good
  return 0;

} // end-method 

/**
 * Find an int
 * @return int
 */
private int findInt() {
  
  while (len > 0) {

    // When an equal
    if  (found.substring(i, i+1).startsWith("="))  break;

    i++;
    len--;

  } // end-while

  // When at end
  if  (len <= 0)  return 4;
  
  // past equal
  i++;

  while (len > 0) {

    // When a space
    if  (found.substring(i, i+1).startsWith(" "))  {

        // bump
        i++;
        len--;
    }
    else {  
        //got it
        break;

    } // endif
  } // end-while

  // When at end
  if  (len <= 0) return 4;

  // save first index
  j = i;
  i++;

  while (len > 0) {

    // When a semicolon
    if  (found.substring(i, i+1).startsWith(";")) break;

    i++;
    len--;

  } // end-while

  // When at end
  if  (len <= 0) return 4;

  // set 
  backString = found.substring(j, i);
          
  // good
  return 0;

} // end-method

/**
 * Find the list
 */
private void findList() {

  int numb = 0, element = 0;
  
  while (len > 0) {

    // When a bracket
    if  (found.substring(i, i+1).startsWith("["))  break;

    // bump
    i++;
    len--;

  } // end-while

  // When at end
  if  (len <= 0) {

      // is null
      return;
  }
  else {                    
      // save first index
      j = ++i;

      while (len > 0) {

        // When a end-bracket
        if  (found.substring(i, i+1).startsWith("]"))  break;

        // bump
        i++;
        len--;

      } // end-while

      // When at end
      if  (len < 1) {

          // bad format
          backBad = 5;
          return;

      } // endif 

      // try to convert
      try {
        numb = Integer.parseInt(found.substring(j, i));

      } // end-try

      catch (NumberFormatException e) {

        // bad format
        backBad = 5;
        return;

      } // end-catch

      // new array
      backList = new String[numb];
      
      // all the elements
      while (numb > 0) {
        
        try {
          // read a line
          found = bufferedReader.readLine();
  
          // When at end
          if  (found == null) {
  
              // all done
              backBad = 3;
              return;
  
          } // endif
  
          // line length
          len = found.length();
          i = 0;
  
          while (len > 0) {
  
            // When a quote
            if  (found.substring(i, i+1).startsWith("\""))  break;
  
            // bump
            i++;
            len--;
  
          } // end-while
  
          // When at end
          if  (len < 1) {
  
              // is error
              backBad = 6;
              return;
  
          } // endif
      
          // save first index
          j = ++i;
  
          while (len > 0) {
  
            // When a quote
            if  (found.substring(i, i+1).startsWith("\"")) break;
  
            // bump
            i++;
            len--;
  
          } // end-while
  
          // When not at end
          if  (len > 0) {
  
              // set  name
              backList[element] = found.substring(j, i);
  
          } // endif
  
          // next element
          element++;
          numb--;
          
        } // end-try
        catch (IOException e) {
      
          // print error            
          System.out.println(e);
  
          // get out
          backBad = 300;
          return; 
        
        } // end-catch  
      } // end-while
  } // endif 

} // end-method    

/**
 * import button
 * @param dir String - file directory 
 * @param monitor JTextField - monitor interval
 * @param activator JTextField - deactivation interval
 * @param sysexit JCheckBox - sysexit on termination
 * @param notify JTextfield - notification queue
 * @param context JTextfield - login context
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
public void importButton (String dir,
            JTextField monitor,
            JTextField activator,
            JCheckBox sys_exit,
            JTextField notify,
            JTextField context,
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

	
  // do the file read and load
	rc = 9;
  
  // save
  this.dir = dir;
  
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

  // When no good
	if  (rc != 0) return;

  // set monitor data
  monitor.setText(F_mon_interval);

  // set activation data
  activator.setText(F_inact_minutes);

  // set system exit data
  if  (F_sys_exit.equalsIgnoreCase("yes")) {
        
    sys_exit.setSelected(true);
  }
  else {
    sys_exit.setSelected(false);

  } // endif

  // set notify function data
  notify.setText(F_notify);
  
  // set login context data
  context.setText(F_login_context);
  
  // When there are start classes
  if  ((F_startup_classes != null) &&
       (F_startup_classes.length > 0)) {

    // remove all currently
    start_classes.removeAll();
    
    // update the JList
    start_classes.setListData(F_startup_classes);
 
  } // endif

  // When there are start functions
  if  ((F_startup_functions != null) &&
     	 (F_startup_functions.length > 0)) {

    // remove all currently
    start_functions.removeAll();
    // update the JList
    start_functions.setListData(F_startup_functions);
 
  } // endif

  // When there are stage1  classes
  if  ((F_shutdown_stage1_classes != null) &&
       (F_shutdown_stage1_classes.length > 0)) {

    // remove all currently
    shut1.removeAll();

    // update the JList
    shut1.setListData(F_shutdown_stage1_classes);
 
  } // endif
  
  // When there are shut2 classes
  if  ((F_shutdown_stage2_classes != null) &&
       (F_shutdown_stage2_classes.length > 0)) {

    // remove all currently
    shut2.removeAll();
    
    // update the JList
    shut2.setListData(F_shutdown_stage2_classes);
 
  } // endif

  // set file data
  statsDir.setText(F_stats_dir);
  statsFile.setText(F_stats_file);
  logDir.setText(F_log_dir);
  logFile.setText(F_log_file);

  // set alt data
  logAlt.setText(F_alt_log_class);
  statsAlt.setText(F_alt_stats_class);

} // end-method

/**
 * write the file
 * @param dir String - dir name
 * @return int
 */ 
public int writeJava (String dir) {

  // output definition 
  FileOutputStream fileOutStream = null;
  DataOutputStream dataOutStream = null;
	
  // append cr/lf to the end of each line
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
		fileOutStream = new FileOutputStream(newDir + "TymeacUserVariables.java");

  } // end-try
		  
  catch (IOException e) {
						  
		// to string
		System.out.println(e);

		// bad open 
		return 1001;
				
  } // end-catch  
	
  // new out stream   
  dataOutStream = new DataOutputStream(fileOutStream);

  // work
  int i, count; 
 
  try {
  	// each line 
  	dataOutStream.writeBytes("package com.tymeac.serveruser;" + crlf);	
  	dataOutStream.writeBytes("// DO NOT EDIT THIS FILE" + crlf);
  	dataOutStream.writeBytes("public class TymeacUserVariables {" + crlf);	
    
  	dataOutStream.writeBytes("  private String notify = null;" + crlf);				
  	dataOutStream.writeBytes("  private int sys_exit;" + crlf);				
  	dataOutStream.writeBytes("  private int mon_interval;" + crlf);				
  	dataOutStream.writeBytes("  private int inact_minutes;" + crlf);
      
    dataOutStream.writeBytes("  private String login_context = null;" + crlf);  
      		
    dataOutStream.writeBytes("  private boolean use_log = false;" + crlf);
    dataOutStream.writeBytes("  private String log_file = null;" + crlf);			
  	dataOutStream.writeBytes("  private String log_dir = null;" + crlf);						
  	dataOutStream.writeBytes("  private String alt_log_class = null;" + crlf);
      
  	dataOutStream.writeBytes("  private boolean use_stats = false;" + crlf);
    dataOutStream.writeBytes("  private String stats_file = null;" + crlf);					
    dataOutStream.writeBytes("  private String stats_dir = null;" + crlf);		
    dataOutStream.writeBytes("  private String alt_stats_class = null;" + crlf);
      
   	dataOutStream.writeBytes("  private String[] startup_classes = null;" + crlf);
  	dataOutStream.writeBytes("  private String[] startup_functions = null; " + crlf);			
  	dataOutStream.writeBytes("  private String[] shutdown_stage1_classes = null;" + crlf);		
  	dataOutStream.writeBytes("  private String[] shutdown_stage2_classes = null;" + crlf);
      
  	dataOutStream.writeBytes("public TymeacUserVariables() {" + crlf);		
  	dataOutStream.writeBytes("  notify = " + "\"" +  F_notify + "\"" + ";" + crlf);
									
		String out;

		out = (F_sys_exit.compareTo("yes") == 0)? "1" : "0";
								
		dataOutStream.writeBytes("  sys_exit = " + out  + ";"+ crlf);
		dataOutStream.writeBytes("  mon_interval = " + F_mon_interval  + ";" + crlf);	
		dataOutStream.writeBytes("  inact_minutes = " + F_inact_minutes + ";" + crlf);
    
    String isdone;
      
    if  (F_login_context  == null) {
        isdone = "null";
    }
    else {
        isdone = "\"" + F_login_context  +  "\"";

    } // endif      
      
    dataOutStream.writeBytes("  login_context = " + isdone + ";" + crlf);

		if  ((F_log_dir == null) &&
				 (F_log_file == null) &&
				 (F_alt_log_class == null)) {
				isdone = "false";
		}
		else {
				isdone = "true";

		} // endif
																													
		dataOutStream.writeBytes("  use_log = " + isdone + ";" + crlf);	

		if  (F_log_file == null) {
				isdone = "null";
		}
		else {
				isdone = "\"" + F_log_file + "\"";

		} // endif
																														
		dataOutStream.writeBytes("  log_file = " + isdone + ";" + crlf);

		if  (F_log_dir == null) {
				isdone = "null";
		}
		else {
				isdone =  "\"" + F_log_dir + "\"";

		} // endif
																													
		dataOutStream.writeBytes("  log_dir = " + isdone + ";" + crlf);	

		if  (F_alt_log_class == null) {
				isdone = "null";
		}
		else {
				isdone = "\"" + F_alt_log_class +  "\"";

		} // endif																							
																																			
		dataOutStream.writeBytes("  alt_log_class = " + isdone + ";" + crlf);								

		if  ((F_stats_dir == null) &&
				 (F_stats_file == null) &&
				 (F_alt_stats_class == null)) {
				isdone = "false";
		}
		else {
				isdone = "true";

		} // endif
																																			
		dataOutStream.writeBytes("  use_stats = " + isdone + ";" + crlf);	

		if  (F_stats_file == null) {
				isdone = "null";
		}
		else {
				isdone = "\"" + F_stats_file + "\"";

		} // endif
																																
		dataOutStream.writeBytes("  stats_file = " + isdone + ";" + crlf);

		if  (F_stats_dir == null) {
				isdone = "null";
		}
		else {
				isdone = "\"" + F_stats_dir + "\"";

		} // endif
																												
		dataOutStream.writeBytes("  stats_dir = " + isdone + ";" + crlf);	

		if  (F_alt_stats_class == null) {
				isdone = "null";
		}
		else {
				isdone = "\"" + F_alt_stats_class + "\"";

		} // endif	
    
		dataOutStream.writeBytes("  alt_stats_class = " + isdone + ";" + crlf);

		if  (F_startup_classes == null) {																	

				dataOutStream.writeBytes("  startup_classes = null;" + crlf);
		}
		else {
				// number in list
				count = F_startup_classes.length;

				// initial
				dataOutStream.writeBytes("  startup_classes = new String[" + count + "];" + crlf);

				// do all the strings
	 			for  (i = 0; i < count; i++) {

					// When something really there
					if  (F_startup_classes[i].length() > 0) {

						  // write each line
  						dataOutStream.writeBytes("  startup_classes[" + i + "] = " + "\"" + F_startup_classes[i] + "\";" + crlf);

					} // endif
 				} // end-for
		} // endif

		if  (F_startup_functions == null) {																	

				dataOutStream.writeBytes("  startup_functions = null;" + crlf);
		}
		else {																								
		
				// number in list
				count = F_startup_functions.length;

				// initial
				dataOutStream.writeBytes("  startup_functions = new String[" + count + "];" + crlf);

				// do all the strings
	 			for  (i = 0; i < count; i++) {

					// When something really there
					if  (F_startup_functions[i].length() > 0) {

						  // write each line
  						dataOutStream.writeBytes("  startup_functions[" + i + "] = " + "\"" + F_startup_functions[i] + "\";" + crlf);

					} // endif
 				} // end-for
		} // endif 

		if  (F_shutdown_stage1_classes == null) {																	

				dataOutStream.writeBytes("  shutdown_stage1_classes = null;" + crlf);
		}
		else {																								
		
				// number in list
				count = F_shutdown_stage1_classes.length;

				// initial
				dataOutStream.writeBytes("  shutdown_stage1_classes = new String[" + count + "];" + crlf);

				// do all the strings
	 			for  (i = 0; i < count; i++) {

					// When something really there
					if  (F_shutdown_stage1_classes[i].length() > 0) {

						  // write each line
  						dataOutStream.writeBytes("  shutdown_stage1_classes[" + i + "] = " + "\"" + F_shutdown_stage1_classes[i] + "\";" + crlf);

					} // endif
 				} // end-for
		} // endif 

		if  (F_shutdown_stage2_classes == null) {																	

				dataOutStream.writeBytes("  shutdown_stage2_classes = null;" + crlf);
		}
		else {																								
		
				// number in list
				count = F_shutdown_stage2_classes.length;

				// initial
				dataOutStream.writeBytes("  shutdown_stage2_classes = new String[" + count + "];" + crlf);

				// do all the strings
	 			for  (i = 0; i < count; i++) {

					// When something really there
					if  (F_shutdown_stage2_classes[i].length() > 0) {

						  // write each line
  						dataOutStream.writeBytes("  shutdown_stage2_classes[" + i + "] = " + "\"" + F_shutdown_stage2_classes[i] + "\";" + crlf);

					} // endif
 				} // end-for
		} // endif 																

		dataOutStream.writeBytes("} // end-constructor" + crlf);

		dataOutStream.writeBytes("public int getInactivate() {" + crlf);																							
		dataOutStream.writeBytes("   return inact_minutes;" + crlf);																							
		dataOutStream.writeBytes("} // end-method" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);            
																				
		dataOutStream.writeBytes("public String getLogAlt() {" + crlf);																							
		dataOutStream.writeBytes("  return alt_log_class;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																				
		dataOutStream.writeBytes("public String getLogDir() {" + crlf);																							
		dataOutStream.writeBytes("  return log_dir;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																		
		dataOutStream.writeBytes("public String getLogFile() {" + crlf);																							
		dataOutStream.writeBytes("  return log_file;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
    
    dataOutStream.writeBytes("public String getLoginContext() {" + crlf);                                              
    dataOutStream.writeBytes("   return login_context;" + crlf);                                              
    dataOutStream.writeBytes("} // end-method" + crlf);                                             
    dataOutStream.writeBytes("  " + crlf);      
															
		dataOutStream.writeBytes("public boolean getLogUse() {" + crlf);																							
		dataOutStream.writeBytes("  return use_log;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																				
		dataOutStream.writeBytes("public int getMonInterval() {" + crlf);																							
		dataOutStream.writeBytes("  return mon_interval;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																			
		dataOutStream.writeBytes("public String getNotify() {" + crlf);																							
		dataOutStream.writeBytes("  return notify;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);	
																		
		dataOutStream.writeBytes("public String[] getShut1Classes() {" + crlf);																							
		dataOutStream.writeBytes("  return shutdown_stage1_classes;" + crlf);																							
		dataOutStream.writeBytes("} // end-method" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);

		dataOutStream.writeBytes("public String[] getShut2Classes() {" + crlf);																							
		dataOutStream.writeBytes("  return shutdown_stage2_classes;" + crlf);																							
		dataOutStream.writeBytes("} // end-method" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																					
		dataOutStream.writeBytes("public String[] getStartUpClasses() {" + crlf);																							
		dataOutStream.writeBytes("  return startup_classes;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);	
																			
		dataOutStream.writeBytes("public String[] getStartUpFunctions() {" + crlf);																																								
		dataOutStream.writeBytes("  return startup_functions;" + crlf);						
		dataOutStream.writeBytes("} // end-method	" + crlf);																																			
		dataOutStream.writeBytes("  " + crlf);
																			
		dataOutStream.writeBytes("public String getStatsAlt() {" + crlf);																							
		dataOutStream.writeBytes("  return alt_stats_class;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																		
		dataOutStream.writeBytes("public String getStatsDir() {" + crlf);																							
		dataOutStream.writeBytes("  return stats_dir;" + crlf);											
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																				
		dataOutStream.writeBytes("public String getStatsFile() {" + crlf);																							
		dataOutStream.writeBytes("  return stats_file;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);	
																			
		dataOutStream.writeBytes("public boolean getStatsUse() {" + crlf);																							
		dataOutStream.writeBytes("  return use_stats;" + crlf);																							
		dataOutStream.writeBytes("} // end-method" + crlf);																																		
		dataOutStream.writeBytes("  " + crlf);
																					
		dataOutStream.writeBytes("public int getSysExit() {" + crlf);																							
		dataOutStream.writeBytes("  return sys_exit;" + crlf);																							
		dataOutStream.writeBytes("} // end-method" + crlf);																							
		dataOutStream.writeBytes(" " + crlf);	
																					
		dataOutStream.writeBytes("} // end-class" + crlf);
			
  } // end-try
		  
  catch (IOException e) {
						  
		System.out.println(e);
			
		return 1002;

  } // end-catch                                
	  
  try {
		// close
		fileOutStream.close();
		dataOutStream.close();

  } // end-try          
		  
  catch (IOException e) {
						  
		System.out.println(e);
			
		return 1003;

  } // end-catch

  // good 
  return 0;
	
} // end-method

 /**
 * Get the context for logging in. This is user defined  
 */
private LoginContext getContext() {
         
   // new logon Context without call back handler
   return ClientSecurityModule.getBasicContext();    

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doWrite() {

  // do the file write
  writeJava(dir);

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doRead() {

  rc =  getFile(dir);

} // end-method
} // end-class
