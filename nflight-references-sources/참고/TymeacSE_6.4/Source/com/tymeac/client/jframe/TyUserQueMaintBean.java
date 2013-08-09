package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import com.tymeac.client.*;

/**
 * Backend object for user queue maint GUI
 */
public class TyUserQueMaintBean {

	// work
  private int rc;
  private String dir;
  
  // File hit
	private String found = null;
        
	// multiple queues
	private QueData[] multi = null;

	// add funtion
	private boolean adding = false;

	// delete funtion
	private boolean deleting = false;

	// back
	private int update_Result	= 0;
	private int delete_Result = 0;
	private int import_Result = 0;

  // fields
  private String  S_que_name    = null;
  private String  S_pa_class    = null;
  
  private String S_pa_timeout  = null;
  private String S_que_type    = null;
  private String S_th_start    = null;
  private String S_nbr_threads = null;
  private String S_wait_time   = null;
  private String S_kill_time   = null;
  private String S_nbr_wl      = null;
  private String S_nbr_in_wl   = null;
  private String S_th_nbr_in_wl = null;
  
  private String S_overall   = null;
  private String S_individ   = null;
  private String S_factor    = null; 
  private String S_average   = null;

	// new values from the window
  private String  N_que_name    = null;
  private int N_nbr_threads = 1;
  private int N_nbr_wl      = 1;
  private int N_nbr_in_wl   = 10;
  private int N_th_nbr_in_wl = 10;
  
  // security
  private LoginContext loginContext = null;

/**
 * Constructor
 */
public TyUserQueMaintBean () {  

  // security
  loginContext = getContext();
  
} // end-constructor 

/**
 * Delete the queue
 */ 
public void deleteButton(String dir, JTextField P_que_name) {  

	// work
	S_que_name = P_que_name.getText();

	// When no prior load
	if  (multi == null) {

			// load it all
			rc = loadOneQueue(dir, S_que_name);

			// When not there
			if  (rc != 0) {

					// error
					delete_Result = 1;

					// done
					return;

			} // endif
	} // endif
  
  // delete this one
  deleting = true;
  
  // save
  this.dir = dir;

  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED modification"); 
      // non-secure
      doUpdate();   
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
                    doUpdate();
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

	// When good
	delete_Result = (rc == 0)? 0 : 2;

	// reset all
	multi 	 = null;
	deleting = false;
  
} // end-method

/**
 * Finish button
 * @param dir        String     - directory
 * @param S_que_name JTextField - que name
 * @param S_pa_class JTextField - appl class name
 * @param S_que_type JCheckBox  - Normal or Output Agent 
 * @param S_th_start JCheckBox  - start threads at start up yes or no
 * @param S_pa_timeout JTextField - timeout seconds
 * @param S_nbr_threads JTextField - number of threads   
 * @param S_wait_time JTextField -  time to wait for new work  
 * @param S_kill_time JTextField -  time to kill thread
 * @param S_nbr_wl    JTextField -  number of wait lists
 * @param S_nbr_in_wl JTextField -  number of slots in a wait list
 * @param S_th_nbr_in_wl JTextField -  number for thresholds
 * @param S_overall   JTextField -  Overall %
 * @param S_individ   JTextField -  Individual %
 * @param S_factor    JTextField -  Weighted Factor
 * @param S_average   JTextField -  Weighted Average % 
 */
public void finishButton( String dir,
							JTextField P_que_name,
              JTextField P_pa_class,
              JTextField P_pa_timeout,
              JCheckBox  P_que_type,
              JCheckBox  P_th_start,
              JTextField P_nbr_threads,
              JTextField P_wait_time,
              JTextField P_kill_time,
              JTextField P_nbr_wl,
              JTextField P_nbr_in_wl,
              JTextField P_th_nbr_in_wl,
              JTextField P_overall,
              JTextField P_individ,
              JTextField P_factor,
              JTextField P_average) {

  // name of this queue
  N_que_name = P_que_name.getText();

	// When no prior import
	if  (multi == null) {

			// try to load one
			rc = loadOneQueue(dir, N_que_name);

			// When one does not exist
			if  (rc == 100) {

					// adding
					adding = true;

					// name to add
					S_que_name = N_que_name; 

			}
			else {
					// adding already exists
					update_Result = 1;

					// no read
					multi = null;

					// back	
  				return;

			} // endif
	}
	else {
			// When imported not the same as current
			if  (S_que_name.compareTo(N_que_name) != 0) {

					// must import first to change
					update_Result = 2;

  				// back	
  				return;

			} // endif
	} // endif
  
  // class to invoke
  S_pa_class = P_pa_class.getText();
  
  // must enter something
  if  ((S_pa_class == null) ||
   		 (S_pa_class.length() < 1)) {
  
  		update_Result = 3;

			// when adding
			if  (adding) {
	
					// not adding
					adding = false;

					// no read
					multi = null;	

			} // endif

			// back
  		return;

  } // endif   

  // timeout value
  try {
		S_pa_timeout = P_pa_timeout.getText();
  } // end-try

	catch (NumberFormatException e) {
		S_pa_timeout = "0";
  } // end-catch
  
  // number of wait lists
  try {
		N_nbr_wl = Integer.parseInt(P_nbr_wl.getText());
		S_nbr_wl = P_nbr_wl.getText();
  } // end-try

  catch (NumberFormatException e) {
	  N_nbr_wl = 0;
  } // end-catch
  
  // When not at least 1, no good
  if  (N_nbr_wl > 0) {
  		;
  }
  else {      
  		update_Result = 4;

  		// when adding
			if  (adding) {
	
					// not adding
					adding = false;

					// no read
					multi = null;	

			} // endif

			// back
  		return;

  } // endif    
  
  // number in a wait list
  try {
	  N_nbr_in_wl = Integer.parseInt(P_nbr_in_wl.getText());
		S_nbr_in_wl = P_nbr_in_wl.getText();
  } // end-try

  catch (NumberFormatException e) {
	  N_nbr_in_wl = 0;
  } // end-catch
  
  // When not at least 1, no good
  if  (N_nbr_in_wl > 0) {
			;
  }
  else {      
			update_Result = 5;

		  // when adding
			if  (adding) {
	
					// not adding
					adding = false;

					// no read
					multi = null;	

			} // endif

			// back
  		return;

  } // endif   
  
  // number in a wait list for thresholds
  try {
    N_th_nbr_in_wl = Integer.parseInt(P_th_nbr_in_wl.getText());
    S_th_nbr_in_wl = P_th_nbr_in_wl.getText();
  } // end-try

  catch (NumberFormatException e) {    
    
    // force same as physical
    N_th_nbr_in_wl = N_nbr_in_wl;
    S_th_nbr_in_wl = P_nbr_in_wl.getText();
  } // end-catch
  
  // When invalid
  if  ((N_th_nbr_in_wl < 1) ||
       (N_th_nbr_in_wl > N_nbr_in_wl)) {
      
      // force same as wl entries
      N_th_nbr_in_wl = N_nbr_in_wl;
      S_th_nbr_in_wl = P_nbr_in_wl.getText();

  } // endif  
  
  // wait time
  try {
		S_wait_time = P_wait_time.getText();
  } // end-try

  catch (NumberFormatException e) {
		S_wait_time = "0";
  } // end-catch    

  // kill time
  try {
		S_kill_time = P_kill_time.getText();
  } // end-try

  catch (NumberFormatException e) {
		S_kill_time = "0";
  } // end-catch  
  
  // number of threads
  try {
		N_nbr_threads = Integer.parseInt(P_nbr_threads.getText());
		S_nbr_threads = P_nbr_threads.getText();
  } // end-try

  catch (NumberFormatException e) {
		N_nbr_threads = 0;
  } // end-catch    
  
  // When not at least 1, no good
  if  (N_nbr_threads > 0) {
  		;
  }
  else {      
  		update_Result = 6;

  		// when adding
			if  (adding) {
	
					// not adding
					adding = false;

					// no read
					multi = null;	

			} // endif

			// back
  		return;

  } // endif    
  
  // thread start
  if  (P_th_start.getAccessibleContext().getAccessibleStateSet().contains(javax.accessibility.AccessibleState.CHECKED)) {
  
			S_th_start = "yes";
  }
  else {  
			S_th_start = "no";

  } // endif
  
  // que type
  if  (P_que_type.getAccessibleContext().getAccessibleStateSet().contains(
              javax.accessibility.AccessibleState.CHECKED)) {
  
			S_que_type = "yes";
  }
  else {  
			S_que_type = "no";

  } // endif
      
  // overall %
  try {      
    S_overall = "0." + P_overall.getText();    
  } // end-try

  catch (NumberFormatException e) {
		update_Result = 8;

	  // when adding
		if  (adding) {

				// not adding
				adding = false;

				// no read
				multi = null;	

		} // endif

		// back
		return;

  } // end-catch

  // individual %
  try {
    S_individ = "0." + P_individ.getText();    
  } // end-try

  catch (NumberFormatException e) {
		update_Result = 9;

	  // when adding
		if  (adding) {

				// not adding
				adding = false;

				// no read
				multi = null;	

		} // endif

		// back
		return;

  } // end-catch
  
  // weighted factor
  try {
    S_factor = "0." + P_factor.getText();    
  } // end-try

  catch (NumberFormatException e) {
	  update_Result = 10;

	  // when adding
		if  (adding) {

				// not adding
				adding = false;

				// no read
				multi = null;	

		} // endif

		// back
		return;

  } // end-catch

  // weighted average
  try {
    S_average = "0." + P_average.getText();  
  } // end-try

  catch (NumberFormatException e) {
	  update_Result = 11;

	  // when adding
		if  (adding) {

				// not adding
				adding = false;

				// no read
				multi = null;	

		} // endif

		// back
		return;

  } // end-catch

  // save
  this.dir = dir;
	// update 
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED modification"); 
      // non-secure
      doUpdate();   
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
                    doUpdate();
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
			
	// When not successfull
	update_Result = (rc != 0)? 12 : 0;

	multi = null;
	adding = false;

} // end-method

/**
 * accessor for delete result
 * @return int
 */ 
public int getDelResult () {  return delete_Result; } // end-method

/**
 * accessor for import result
 * @return int
 */ 
public int getImpResult () { return import_Result; } // end-method

/**
 * accessor for update result
 * @return int
 */ 
public int getUpdResult () { return update_Result; } // end-method

/**
 * import button
 * @param dir        String     - directory
 * @param S_que_name JTextField - que name
 * @param S_pa_class JTextField - appl class name
 * @param S_que_type JCheckBox  - Normal or Output Agent 
 * @param S_th_start JCheckBox  - start threads at start up yes or no
 * @param S_pa_timeout JTextField - timeout seconds
 * @param S_nbr_threads JTextField - number of threads   
 * @param S_wait_time JTextField -  time to wait for new work  
 * @param S_kill_time JTextField -  time to kill thread
 * @param S_nbr_wl    JTextField -  number of wait lists
 * @param S_nbr_in_wl JTextField -  number of slots in a wait list
 * @param S_overall   JTextField -  Overall %
 * @param S_individ   JTextField -  Individual %
 * @param S_factor    JTextField -  Weighted Factor
 * @param S_average   JTextField -  Weighted Average % 
 */

public void importButton( String dir,
							JTextField P_que_name,
              JTextField P_pa_class,
              JTextField P_pa_timeout,
              JCheckBox  P_que_type,
              JCheckBox  P_th_start,
              JTextField P_nbr_threads,
              JTextField P_wait_time,
              JTextField P_kill_time,
              JTextField P_nbr_wl,
              JTextField P_nbr_in_wl,
              JTextField P_th_nbr_in_wl,
              JTextField P_overall,
              JTextField P_individ,
              JTextField P_factor,
              JTextField P_average) {


	// que name
	S_que_name = P_que_name.getText();

  // get the que data from the java file
  rc = loadOneQueue(dir, S_que_name);
  
	// result
	switch (rc) {

			case 0: 	import_Result = 0;
								break;

			case 100: import_Result = 1;
								break;

			case 200:  import_Result = 4;
                break;
      
			case 300: import_Result = 3;
								break;

			default : import_Result = 2;
								break;

	} // end-switch

  // When not there, return
  if  (import_Result != 0) {
   
 		 // done
  		return;

  } // endif    
  
  // data from load
  P_pa_class.setText(S_pa_class);
  
  // When que type 0, no, 1, yes
  if  (S_que_type.compareTo("no") == 0) {
    
  		P_que_type.setSelected(false);
  }
  else {
  		P_que_type.setSelected(true);

  } // endif    

  // When th_start type 0, no, 1, yes
  if  (S_th_start.compareTo("no") == 0) {
    
		  P_th_start.setSelected(false);
  }
  else {
  		P_th_start.setSelected(true);

  } // endif                      
  
  String S;   

  S = "" + S_pa_timeout;
  P_pa_timeout.setText(S);

  S = "" + S_nbr_threads;
  P_nbr_threads.setText(S);
  
  S = "" + S_wait_time;
  P_wait_time.setText(S);

  S = "" + S_kill_time;
  P_kill_time.setText(S);
  
  S = "" + S_nbr_wl;
  P_nbr_wl.setText(S);
    
  S = "" + S_nbr_in_wl;
  P_nbr_in_wl.setText(S);
  
  S = "" + S_th_nbr_in_wl;
  P_th_nbr_in_wl.setText(S);
  
  S = "" + S_overall.substring(2);
  P_overall.setText(S);
  
  S = "" + S_individ.substring(2);
  P_individ.setText(S);
  
  S = "" + S_factor.substring(2);
  P_factor.setText(S);
  
  S = "" + S_average.substring(2);
  P_average.setText(S);
        
} // end-method

/**
 * load the queues 
 * @param dir String - directory name
 * @return int 
 */ 
private int loadAllQueues (String dir) {	
	  
  BufferedReader bufferedReader = null;
	
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
		// new reader
		bufferedReader = new BufferedReader(new FileReader(newDir + "TymeacUserQueues.java"));

  } // end-try
		  
  catch (IOException e) {			         

		// get out
		return 100; 
				
  } // end-catch 

  // work
  int i;

	// vector for temp phase
	Vector<QueData> temp = new Vector<QueData>();

	// local que data
	QueData qd = null;
										
  // read each line of the file   
	try {
	 // find [
		while (true) {

			// read a line
 			found = bufferedReader.readLine();

		  // When at end, all done
			if  (found == null) return 1;

		  // When at end, all done
			if  (found.startsWith("// [END]")) break;
			
			// When a queue
			if  (found.startsWith("// [QUEUE]")) {

					// que data
					qd = new QueData();

					// read past addElement
		 			found = bufferedReader.readLine();

				  // When at end, all done
					if  (found == null) return 3;

					// read name line
				 	found = bufferedReader.readLine();

				  // When at end, all done
					if  (found == null) return 4;

					// When NOT a quote, done
					if  (!found.substring(0, 1).startsWith("\""))  return 5;

					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a quote, got it
						if  (found.substring(i, i+1).startsWith("\""))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 6;
	
					// que name
					qd.setQue_name(found.substring(1, i));

					// all the rest
					
			// read pa class line
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 7;

					// When NOT a quote, done
					if  (!found.substring(0, 1).startsWith("\""))  return 8;

					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a quote, got it
						if  (found.substring(i, i+1).startsWith("\""))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) 	return 9;
	
					// pa class
					qd.setPa_class(found.substring(1, i));

			// read type line
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 10;

					// When NOT a quote, done
					if  (!found.substring(0, 1).startsWith("\""))  return 11;

					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a quote, got it
						if  (found.substring(i, i+1).startsWith("\"")) break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) 	return 12;
	
					// queue type
					qd.setQue_type(found.substring(1, i));

			// read start threads line
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 13;

					// When NOT a quote, done
					if  (!found.substring(0, 1).startsWith("\""))  return 14;

					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a quote, got it
						if  (found.substring(i, i+1).startsWith("\""))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 15;
	
					// start threads
					qd.setTh_start(found.substring(1, i));

			// read pa timeout
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 16;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(","))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 17;
	
					// pa timeout
					qd.setPa_timeout(found.substring(0, i));

			// read nbr threads
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 18;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(","))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) 	return 19;
	
					// pa timeout
					qd.setNbr_threads(found.substring(0, i));  

			// read nbr waitlists
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 20;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(","))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) 	return 21;
	
					// nbr waitlists
					qd.setNbr_wl(found.substring(0, i));   

			// read nbr waitlist entries 
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 20;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(",")) break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) 	return 21;
	
					// nbr waitlists
					qd.setNbr_in_wl(found.substring(0, i));  
          
      // read nbr waitlist entries for thresholds
          found = bufferedReader.readLine();

          // When at end, done
          if  (found == null) return 20;
          
          // line length
          len = (found.length() - 1);
          i = 1;

          while (len > 0) {

            // When a comma, got it
            if  (found.substring(i, i+1).startsWith(",")) break;

            i++;
            len--;

          } // end-while

          // When at end, bad format
          if  (len <= 0)  return 21;
  
          // nbr waitlists for thresholds
          qd.setNbr_th_in_wl(found.substring(0, i));      

			// read wait time
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 22;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(","))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) 	return 23;
	
					//  wait time
					qd.setWait_time(found.substring(0, i));     

			// read kill time
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 24;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(","))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 25;
	
					// kill time
					qd.setKill_time(found.substring(0, i));     

			// read overall
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 26;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(","))  break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 27;
	
					// overall
					qd.setOverall(found.substring(0, i - 1));     

			// read individ
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 28;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(",")) break; 

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 29;
	
					// individ
					qd.setIndivid(found.substring(0, i - 1));     

			// read factor
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 30;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a comma, got it
						if  (found.substring(i, i+1).startsWith(",")) break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 31;
	
					// factor
					qd.setFactor(found.substring(0, i - 1));     

			// read average
 					found = bufferedReader.readLine();

				  // When at end, done
					if  (found == null) return 32;
					
					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a right paren, got it
						if  (found.substring(i, i+1).startsWith(")")) break;

						i++;
						len--;

					} // end-while

					// When at end, bad format
					if  (len <= 0) return 33;
	
					// average
					qd.setAverage(found.substring(0, i - 1));

					// done
					temp.addElement(qd);

			} // endif
		} // end-while
  } // end-try	  

  catch (IOException e) {
		  
		// print exception
		System.out.println(e.toString());

		// get out
		return 200;               
		  
  } // end-catch 	
	  
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

	// *--- end of phase one ---*

	// total Queues 
  int total_queues = temp.size();   

	// create a new array 
	multi = new QueData[total_queues];

	// fill up the array by taking objects out of the temporary Vector
	for  (i = 0; i < total_queues; i++) {

	  // add this queue
		multi[i] = temp.elementAt(i);

	} // end-for		   

  // good
  return 0;                             
	  
} // end-method

/**
 * load a single queue
 * @param dir String - directory name
 * @param que String - queue name
 * @return int  
 */ 
private int loadOneQueue (String dir, String que) {	
	  
  // save
  this.dir = dir;
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      doLoadAll();   
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
                    doLoadAll();
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

	// When ng, done
	if  (rc != 0) return rc;

	// work
	int nbr_queues = multi.length, i;

	for  (i = 0; i < nbr_queues; i++) {

		// When found
		if  (que.compareTo(multi[i].getQue_name()) == 0) break;
      
	} // end-for

	// When not found
	if  (i == nbr_queues) return 200;

	S_que_name    = que;
  S_pa_class    = multi[i].getPa_class();  
  S_pa_timeout  = multi[i].getPa_timeout();
  S_que_type    = multi[i].getQue_type();
  S_th_start    = multi[i].getTh_start();
  S_nbr_threads = multi[i].getNbr_threads();
  S_wait_time   = multi[i].getWait_time();
  S_kill_time   = multi[i].getKill_time();
  S_nbr_wl      = multi[i].getNbr_wl();
  S_nbr_in_wl   = multi[i].getNbr_in_wl();
  S_th_nbr_in_wl = multi[i].getNbr_th_in_wl();  
  S_overall   = multi[i].getOverall();
  S_individ   = multi[i].getIndivid();
  S_factor    = multi[i].getFactor(); 
  S_average   = multi[i].getAverage(); 

  // good
  return 0;                             
	  
} // end-method

/**
 * update the queue
 * @param dir String - directory name
 * @return int 
 */ 
private int updateQue (String dir) {

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
		fileOutStream = new FileOutputStream(newDir + "TymeacUserQueues.java");

  } // end-try
		  
  catch (IOException e) {
						  
		// to string
		System.out.println(e);

		// bad open 
		return 1;
				
  } // end-catch  
	
  // new out stream   
  dataOutStream = new DataOutputStream(fileOutStream);

  // work
  int i, nbr_que = 0;

	// When something threre
	if  (multi != null) {
		
			// number
			nbr_que = multi.length; 

	} // endif
 
  try {
		// each line 
		dataOutStream.writeBytes("package com.tymeac.serveruser;" + crlf);	
		dataOutStream.writeBytes("// DO NOT EDIT THIS FILE" + crlf);
		dataOutStream.writeBytes("import java.util.*;" + crlf);
		dataOutStream.writeBytes("import com.tymeac.base.*;" + crlf);

		dataOutStream.writeBytes("public class TymeacUserQueues {" + crlf);	
		dataOutStream.writeBytes("  protected int total_queues;" + crlf);
		dataOutStream.writeBytes("  protected TymeacIndividualQueue[] queues;" + crlf);

		dataOutStream.writeBytes("public TymeacUserQueues() {" + crlf);
		dataOutStream.writeBytes("  Vector<TymeacIndividualQueue> temp = new Vector<TymeacIndividualQueue>();" + crlf);

		// do all the queues
		for  (i = 0; i < nbr_que; i++) {

			// When the updated one
			if  (S_que_name.compareTo(multi[i].getQue_name()) == 0) {

					// When not deleteing
					if  (!deleting) {

							dataOutStream.writeBytes("// [QUEUE]" + crlf);
							dataOutStream.writeBytes("temp.addElement( new TymeacIndividualQueue(	" + crlf);

							dataOutStream.writeBytes("\"" + S_que_name + "\"," + crlf);
							dataOutStream.writeBytes("\"" + S_pa_class + "\"," + crlf);
							dataOutStream.writeBytes("\"" + S_que_type + "\"," + crlf);
							dataOutStream.writeBytes("\"" + S_th_start + "\"," + crlf);	

							dataOutStream.writeBytes(S_pa_timeout 	+ "," + crlf);
							dataOutStream.writeBytes(S_nbr_threads 	+ "," + crlf);	
							dataOutStream.writeBytes(S_nbr_wl 			+ "," + crlf);	
							dataOutStream.writeBytes(S_nbr_in_wl 		+ "," + crlf);	
              dataOutStream.writeBytes(S_th_nbr_in_wl + "," + crlf);  
							dataOutStream.writeBytes(S_wait_time 		+ "," + crlf);	
							dataOutStream.writeBytes(S_kill_time 		+ "," + crlf);	

							dataOutStream.writeBytes(S_overall 	+ "F" + "," + crlf);	
							dataOutStream.writeBytes(S_individ 	+ "F" + "," + crlf);	
							dataOutStream.writeBytes(S_factor 	+ "F" + "," + crlf);	
							dataOutStream.writeBytes(S_average 	+ "F" + "));" + crlf);	
		
					} // endif
			}
			else {
					dataOutStream.writeBytes("// [QUEUE]" + crlf);
					dataOutStream.writeBytes("temp.addElement( new TymeacIndividualQueue(	" + crlf);

					dataOutStream.writeBytes("\"" + multi[i].getQue_name() + "\"," + crlf);
					dataOutStream.writeBytes("\"" + multi[i].getPa_class() + "\"," + crlf);
					dataOutStream.writeBytes("\"" + multi[i].getQue_type() + "\"," + crlf);
					dataOutStream.writeBytes("\"" + multi[i].getTh_start() + "\"," + crlf);	

					dataOutStream.writeBytes(multi[i].getPa_timeout() 	+ "," + crlf);
					dataOutStream.writeBytes(multi[i].getNbr_threads() 	+ "," + crlf);	
					dataOutStream.writeBytes(multi[i].getNbr_wl() 			+ "," + crlf);	
					dataOutStream.writeBytes(multi[i].getNbr_in_wl() 		+ "," + crlf);
          dataOutStream.writeBytes(multi[i].getNbr_th_in_wl() + "," + crlf);
					dataOutStream.writeBytes(multi[i].getWait_time() 		+ "," + crlf);	
					dataOutStream.writeBytes(multi[i].getKill_time() 		+ "," + crlf);	

					dataOutStream.writeBytes(multi[i].getOverall() 	+ "F" + "," + crlf);	
					dataOutStream.writeBytes(multi[i].getIndivid() 	+ "F" + "," + crlf);	
					dataOutStream.writeBytes(multi[i].getFactor() 	+ "F" + "," + crlf);	
					dataOutStream.writeBytes(multi[i].getAverage() 	+ "F" + "));" + crlf);	

			} // endif		
		} // end-for

		// When adding a queue
		if  (adding) {

				dataOutStream.writeBytes("// [QUEUE]" + crlf);
				dataOutStream.writeBytes("temp.addElement( new TymeacIndividualQueue(	" + crlf);

				dataOutStream.writeBytes("\"" + S_que_name + "\"," + crlf);
				dataOutStream.writeBytes("\"" + S_pa_class + "\"," + crlf);
				dataOutStream.writeBytes("\"" + S_que_type + "\"," + crlf);
				dataOutStream.writeBytes("\"" + S_th_start + "\"," + crlf);	

				dataOutStream.writeBytes(S_pa_timeout 	+ "," + crlf);
				dataOutStream.writeBytes(S_nbr_threads 	+ "," + crlf);	
				dataOutStream.writeBytes(S_nbr_wl 			+ "," + crlf);	
				dataOutStream.writeBytes(S_nbr_in_wl 		+ "," + crlf);
        dataOutStream.writeBytes(S_th_nbr_in_wl + "," + crlf);  
				dataOutStream.writeBytes(S_wait_time 		+ "," + crlf);	
				dataOutStream.writeBytes(S_kill_time 		+ "," + crlf);	

				dataOutStream.writeBytes(S_overall 	+ "F" + "," + crlf);	
				dataOutStream.writeBytes(S_individ 	+ "F" + "," + crlf);	
				dataOutStream.writeBytes(S_factor 	+ "F" + "," + crlf);	
				dataOutStream.writeBytes(S_average 	+ "F" + "));" + crlf);

		} // endif
/*
			// When there are no prior queues
			if  (nbr_que == 0) {

					// When not deleteing
					if  (!deleting) {

							dataOutStream.writeBytes("// [QUEUE]" + crlf);
							dataOutStream.writeBytes("temp.addElement( new TymeacIndividualQueue(	" + crlf);

							dataOutStream.writeBytes("\"" + S_que_name + "\"," + crlf);
							dataOutStream.writeBytes("\"" + S_pa_class + "\"," + crlf);
							dataOutStream.writeBytes("\"" + S_que_type + "\"," + crlf);
							dataOutStream.writeBytes("\"" + S_th_start + "\"," + crlf);	

							dataOutStream.writeBytes(S_pa_timeout 	+ "," + crlf);
							dataOutStream.writeBytes(S_nbr_threads 	+ "," + crlf);	
							dataOutStream.writeBytes(S_nbr_wl 			+ "," + crlf);	
							dataOutStream.writeBytes(S_nbr_in_wl 		+ "," + crlf);	
							dataOutStream.writeBytes(S_wait_time 		+ "," + crlf);	
							dataOutStream.writeBytes(S_kill_time 		+ "," + crlf);	

							dataOutStream.writeBytes(S_overall 	+ "F" + "," + crlf);	
							dataOutStream.writeBytes(S_individ 	+ "F" + "," + crlf);	
							dataOutStream.writeBytes(S_factor 	+ "F" + "," + crlf);	
							dataOutStream.writeBytes(S_average 	+ "F" + "));" + crlf);

					} // endif
			} // endif
*/								
		dataOutStream.writeBytes("// [END]" + crlf);
		dataOutStream.writeBytes("  total_queues = temp.size(); " + crlf);	
		dataOutStream.writeBytes("  queues = new TymeacIndividualQueue[total_queues];" + crlf);	
		dataOutStream.writeBytes("  for  (int i = 0; i < total_queues; i++) {" + crlf);	
		dataOutStream.writeBytes("       queues[i] = temp.elementAt(i);" + crlf);	
		dataOutStream.writeBytes("  } // end-for" + crlf);

		dataOutStream.writeBytes("} // end-constructor" + crlf);

		dataOutStream.writeBytes("public int getNbrQueues() {" + crlf);
		dataOutStream.writeBytes("  return total_queues;  " + crlf);
		dataOutStream.writeBytes("} // end-method" + crlf);
		dataOutStream.writeBytes("public TymeacIndividualQueue[] getQueues() {" + crlf);
		dataOutStream.writeBytes("  return queues;" + crlf);
		dataOutStream.writeBytes("} // end-method" + crlf);
		dataOutStream.writeBytes("} // end-class" + crlf);
			
  } // end-try
		  
  catch (IOException e) {
						  
		System.out.println(e);
			
		return 2;

  } // end-catch                                
	  
  try {
		// close
		fileOutStream.close();
		dataOutStream.close();

  } // end-try          
		  
  catch (IOException e) {
						  
		System.out.println(e.toString());
			
		return 3;

  } // end-catch

  // good 
  return 0;
	
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
 */
private void doLoadAll() {

  // load all the queues
  rc = loadAllQueues(dir);

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doUpdate() {

  // load all the queues
  rc = updateQue(dir);

} // end-method
} // end-class
