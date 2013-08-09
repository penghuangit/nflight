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
import java.util.*;
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import com.tymeac.client.*;

/**
 * Backend object for User Function Maint GUI
 */
public class TyUserFuncMaintBean {

	// File hit
	private String found = null;
  
  // work
  private int rc = 0;
  private String dir;
        
	// multiple functions
	private FuncData[] multi = null;

	// add funtion
	private boolean adding = false;

	// delete funtion
	private boolean deleting = false;

	// back
	private int update_Result	= 0;
	private int delete_Result = 0;
	private int import_Result = 0;

  // fields
  private String   S_func_name	= null;
  private String   S_oa_name    = null;  
  private String[] S_list  			= null;

	// new values from the window
  private String   N_func_name	= null;
  private String[] N_list  			= null;
  
  // security
  private LoginContext loginContext = null;

/**
 * Constructor
 */ 
public TyUserFuncMaintBean () {   

  // security
  loginContext = getContext();

} // end-constructor 

/**
 * Delete button
 * @param dir String - directory name
 * @param P_func_name JTextField - function name
 */ 
public void deleteButton(String dir, JTextField P_func_name) {  

	// work
	S_func_name = P_func_name.getText();

	// When no prior load
	if  (multi == null) {

			// load it all
			rc = loadOneFunc(dir, S_func_name);

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
      updateFunc();   
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
                    updateFunc();
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

	// When ok  
	delete_Result = (rc == 0)? 0 : 2;

	// reset all
	multi = null;
	deleting = false;
  
} // end-method

/**
 * finish button
 * @param dir String - directory name
 * @param P_func_name JTextField - function name
 * @param P_oa_name JTextField - output agenet name
 * @param P_list JList - list of queues 
 */
public void finishButton( String dir,
													JTextField P_func_name,
              						JTextField P_oa_name,
              						JList 		 P_list) {        
  
  // name of this queue
  N_func_name = P_func_name.getText();

	// When no prior import
	if  (multi == null) {

			// try to load one
			rc = loadOneFunc(dir, N_func_name);

			// When one does not exist
			if  ((rc == 2) ||
					 (rc == 100)) {

					// adding
					adding = true;

					// name to add
					S_func_name = N_func_name; 
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
			if  (S_func_name.compareTo(N_func_name) != 0) {

					// must import first to change
					update_Result = 2;

  				// back	
  				return;

			} // endif
	} // endif
  
  // oa
  S_oa_name = P_oa_name.getText();

	// size of list
	int nbr_func = P_list.getModel().getSize();
	int real_size = nbr_func;
	
	// When none
	if  (nbr_func == 0) {
	
			// must have some
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

	// temp
	String temp = (String) P_list.getModel().getElementAt(0);

	// When the dummy
	if (temp == null) {

			// skip this one
			real_size--;

			// When none
			if  (nbr_func == 0) {
	
					// must have some
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
	} // endif
    
  // new String[] to hold the items
  S_list = new String[real_size];
    
  // get all the items
  for  (int i = 0, j = 0; i < nbr_func; i++) {

		// value
		temp = (String) P_list.getModel().getElementAt(i);

		// When not the dummy
		if (temp != null) {

       // add to array
	     S_list[j] = temp;

			// next j
			j++;

		} // endif			          
  } // end-for 

  // save
  this.dir = dir;
  
  // When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED modification"); 
      // non-secure
      updateFunc();   
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
                    updateFunc();
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
	if (rc != 0) {

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
  }
	else {
			update_Result = 0;
			multi = null;
			adding = false;
  		return;

	} // endif

} // end-method

/**
 * accessor for delete result
 * @return int
 */ 
public int getDelResult () { return delete_Result; } // end-method

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
 * @param dir String - directory name
 * @param P_func_name JTextField - function name
 * @param P_oa_name JTextField - output agenet name
 * @param P_list JList - list of queues 
 */
@SuppressWarnings("unchecked")
public void importButton( String dir,
							JTextField P_func_name,
              JTextField P_oa_name,
              JList      P_list) {


	// que name
	S_func_name = P_func_name.getText();

  // get the func data from the java file
  rc = loadOneFunc(dir, S_func_name);
  
	// result
	switch (rc) {

			case 0: 	import_Result = 0;
								break;

			case 100: import_Result = 1;
								break;

			case 200: 
			case 300: import_Result = 3;
								break;

			default : import_Result = 2;
								break;

	} // end-switch

  // When not there, return
  if  (import_Result != 0) return;
  
  // data from load
  P_oa_name.setText(S_oa_name);

 // remove all from list   
  P_list.removeAll();

	// update the JList
  P_list.setListData(S_list);
        
} // end-method

/**
 * load the functions
 * Executed as privileged or not as privileged
 */ 
private void loadAllFunc () {	
	  
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
		bufferedReader = new BufferedReader(new FileReader(newDir + "TymeacUserFunctions.java"));

  } // end-try
		  
  catch (IOException e) {			         

		// get out
    rc = 100;
		return; 
				
  } // end-catch 

  // work
  int i, j, k, count;

	// list array
	String[] my_list = null;

	// vector for temp phase
	Vector<FuncData> temp = new Vector<FuncData>();

	// local func data
	FuncData fd = null;
										
  // read each line of the file   
	try {
	 // find [
		while (true) {

  		// read a line
  		found = bufferedReader.readLine();

		  // When at end
			if  (found == null) {

					// all done
          rc = 1;
					return;

		  } // endif

		  // When at end, done
			if  (found.startsWith("// [END]")) break;
			
			// When a [
			if  (found.startsWith("// [FUNCTION]")) {

					// func data
					fd = new FuncData();

					// read list line
				 	found = bufferedReader.readLine();

				  // When at end
					if  (found == null) {

							// When all done
							rc = 2;
              return;

				  } // endif

					// line length
					len = found.length();
					i = 0;

					while (len > 0) {

						// When a left bracket, got it
						if  (found.substring(i, i+1).startsWith("["))  break;

						i++;
						len--;

					} // end-while

					// hit addr
					j = ++i;

					// When at end
					if  (len <= 0) {

							// bad format
							rc = 3;
              return;

				  } // endif
					
					while (len > 0) {

						// When a right bracket
						if  (found.substring(i, i+1).startsWith("]")) break;

							i++;
							len--;

					} // end-while

					// When at end
					if  (len <= 0) {

							// bad format
							rc = 4;
              return;

				  } // endif
	
					// count
					try {
  					count = Integer.parseInt(found.substring(j, i));

					} // end-try

					catch (NumberFormatException e) {

						// all done
						rc = 5;
            return;
	
					} // end-catch

					// also could be zero
					if  (count == 0) {

							// all done
							rc = 6;
              return;
	
					} // end-catch

					// new array 
					my_list = new String[count];

					// do all in list
					for  (k = 0; k < count; k++) {

						// read next line
						found = bufferedReader.readLine();

					  // When at end
						if  (found == null) {

								// all done
								rc = 7;
                return;

					  } // endif
				
						// line length
						len = found.length();
						i = 0;

						while (len > 0) {

							// When a quote
							if  (found.substring(i, i+1).startsWith("\"")) break;

								i++;
								len--;

						} // end-while

						// When at end
						if  (len <= 0) {

								// bad format
								rc = 8;
                return;

					  } // endif

						// start of string
						j = ++i;

						while (len > 0) {

							// When a quote
							if  (found.substring(i, i+1).startsWith("\""))  break;

								i++;
								len--;

						} // end-while

						// When at end
						if  (len <= 0) {

								// bad format
								rc = 9;
                return;

					  } // endif

						// string
						my_list[k] = found.substring(j, i);
							
					} // end-for

					// set list
					fd.setF_list(my_list);

					// read past addElement
		 			found = bufferedReader.readLine();

				  // When at end
					if  (found == null) {

							// When all done
							rc = 10;
              return;

				  } // endif

					// read func name
		 			found = bufferedReader.readLine();

				  // When at end
					if  (found == null) {

							// When all done
							rc = 11;
              return;

				  } // endif

					// When NOT a quote
					if  (!found.substring(0, 1).startsWith("\""))  {

							// done
							rc = 12;
              return;

					} // endif

					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a quote
						if  (found.substring(i, i+1).startsWith("\"")) break;

							i++;
							len--;

					} // end-while

					// When at end
					if  (len <= 0) {

							// bad format
							rc = 13;
              return;

				  } // endif
	
					// func name
					fd.setFunc_name(found.substring(1, i));

					// read oa name
		 			found = bufferedReader.readLine();

				  // When at end
					if  (found == null) {

							// When all done
							rc = 14;
              return;

				  } // endif

					// When NOT a quote
					if  (!found.substring(0, 1).startsWith("\""))  {

							// done
							rc = 15;
              return;

					} // endif

					// line length
					len = (found.length() - 1);
					i = 1;

					while (len > 0) {

						// When a quote
						if  (found.substring(i, i+1).startsWith("\"")) break;

							i++;
							len--;

					} // end-while

					// When at end
					if  (len <= 0) {

							// bad format
							rc = 16;
              return;

				  } // endif
	
					// oa name
					fd.setOa_name(found.substring(1, i));

					// done with one
					temp.addElement(fd);

			} // endif
		} // end-while
  } // end-try	  

  catch (IOException e) {
		  
		// print exception
		System.out.println(e);

		// get out
		rc = 200;
    return;               
		  
  } // end-catch 	
	  
  try {
		//close
		bufferedReader.close();

  } // end-try          
		  
  catch (IOException e) {
	  
		// print error            
		System.out.println(e);

		// get out
		rc = 300;
    return; 
		  
  } // end-catch 

	// *--- end of phase one ---*

	// total Queues 
  int total_func = temp.size();   

	// create a new array 
	multi = new FuncData[total_func];

	// fill up the array by taking objects out of the temporary Vector
	for  (i = 0; i < total_func; i++) {

	  // add this queue
		multi[i] = temp.elementAt(i);

	} // end-for		   

  // good
  rc = 0;
  return;                             
	  
} // end-method

/**
 * load a single function
 * @param dir String - directory name
 * @param func String - function name
 * @return int
 */ 
private int loadOneFunc (String dir, String func) {	
	 
  // save
  this.dir = dir;
  
  // load all the functions
      
	// When not using security     
  if (loginContext == null) {
        System.out.println("UNSECURED access"); 
      // non-secure
      loadAllFunc();   
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
                    loadAllFunc();
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

	// When ng
	if  (rc != 0) return rc;

	// work
	int nbr_func = multi.length, i;

	for  (i = 0; i < nbr_func; i++) {

		// When found
		if  (func.compareTo(multi[i].getFunc_name()) == 0) break;
    
	} // end-for

	// When not found
	if  (i == nbr_func) return 2;

	S_func_name	= func;
  S_oa_name   = multi[i].getOa_name();  
  S_list  		= multi[i].getF_list();

  // good
  return 0;                             
	  
} // end-method

/**
 * update the functions
 * Executed as privileged or not as privileged
 */ 
private void updateFunc() {

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
		fileOutStream = new FileOutputStream(newDir + "TymeacUserFunctions.java");

  } // end-try
		  
  catch (IOException e) {
						  
		// to string
		System.out.println(e);

		// bad open 
    rc = 1;
		return;
				
  } // end-catch  
	
  // new out stream   
  dataOutStream = new DataOutputStream(fileOutStream);

  // work
  int i, j, nbr_func = 0, nbr_list = 0;

	// When something threre
	if  (multi != null) {
		
			// number
			nbr_func = multi.length; 

	} // endif
 
  try {
		// each line 
		dataOutStream.writeBytes("package com.tymeac.serveruser;" + crlf);	
		dataOutStream.writeBytes("// DO NOT EDIT THIS FILE" + crlf);
		dataOutStream.writeBytes("import java.util.*;" + crlf);
		dataOutStream.writeBytes("import com.tymeac.base.*;" + crlf);

		dataOutStream.writeBytes("public class TymeacUserFunctions {" + crlf);	
		dataOutStream.writeBytes("  protected int total_functions;" + crlf);
		dataOutStream.writeBytes("  protected TymeacIndividualFunction[] functions;" + crlf);

		dataOutStream.writeBytes("public TymeacUserFunctions() {" + crlf);
		dataOutStream.writeBytes("  Vector<TymeacIndividualFunction> temp = new Vector<TymeacIndividualFunction>();" + crlf);
		dataOutStream.writeBytes("  String[] list;" + crlf);

		// do all the queues
		for  (i = 0; i < nbr_func; i++) {

			// When the updated one
			if  (S_func_name.compareTo(multi[i].getFunc_name()) == 0) {

					// When not deleteing
					if  (!deleting) {

							dataOutStream.writeBytes("// [FUNCTION]" + crlf);					

							// how many
							nbr_list = S_list.length;	

							// first
							dataOutStream.writeBytes("list = new String[" + nbr_list + "];" + crlf);

							// rest
							for  (j = 0; j < nbr_list; j++) {

								dataOutStream.writeBytes("list[" + j + "] = \"" + S_list[j] + "\";" + crlf);			
				
							} // end-for							

							dataOutStream.writeBytes("temp.addElement( new TymeacIndividualFunction(	" + crlf);

							dataOutStream.writeBytes("\"" + S_func_name + "\"," + crlf);
							dataOutStream.writeBytes("\"" + S_oa_name + "\"," + crlf);
							dataOutStream.writeBytes("list));" + crlf);

					} // endif
			}
			else {
					dataOutStream.writeBytes("// [FUNCTION]" + crlf);		

					// how many
					N_list = multi[i].getF_list();
					nbr_list = N_list.length;	

					// first
					dataOutStream.writeBytes("list = new String[" + nbr_list + "];" + crlf);

					// rest
					for  (j = 0; j < nbr_list; j++) {

						dataOutStream.writeBytes("list[" + j + "] = \"" + N_list[j] + "\";" + crlf);			
		
					} // end-for

					dataOutStream.writeBytes("temp.addElement( new TymeacIndividualFunction(	" + crlf);

					dataOutStream.writeBytes("\"" + multi[i].getFunc_name() + "\"," + crlf);
					dataOutStream.writeBytes("\"" + multi[i].getOa_name() + "\"," + crlf);
					dataOutStream.writeBytes("list));" + crlf);		

			} // endif		
		} // end-for

		// When adding a queue
		if  (adding) {

				dataOutStream.writeBytes("// [FUNCTION]" + crlf);					

				// how many
				nbr_list = S_list.length;	

				// first
				dataOutStream.writeBytes("list = new String[" + nbr_list + "];" + crlf);

				// rest
				for  (i = 0; i < nbr_list; i++) {

					dataOutStream.writeBytes("list[" + i + "] = \"" + S_list[i] + "\";" + crlf);			
		
				} // end-for	

				dataOutStream.writeBytes("temp.addElement( new TymeacIndividualFunction(	" + crlf);

				dataOutStream.writeBytes("\"" + S_func_name + "\"," + crlf);
				dataOutStream.writeBytes("\"" + S_oa_name + "\"," + crlf);
				dataOutStream.writeBytes("list));" + crlf);	

		} // endif
		
		dataOutStream.writeBytes("// [END]" + crlf);
		dataOutStream.writeBytes("  total_functions = temp.size(); " + crlf);	
		dataOutStream.writeBytes("  functions = new TymeacIndividualFunction[total_functions];" + crlf);	
		dataOutStream.writeBytes("  for  (int i = 0; i < total_functions; i++) {" + crlf);	
		dataOutStream.writeBytes("       functions[i] = temp.elementAt(i);" + crlf);	
		dataOutStream.writeBytes("  } // end-for" + crlf);

		dataOutStream.writeBytes("} // end-constructor" + crlf);

		dataOutStream.writeBytes("public int getNbrFunctions() {" + crlf);
		dataOutStream.writeBytes("  return total_functions;  " + crlf);
		dataOutStream.writeBytes("} // end-method" + crlf);
		dataOutStream.writeBytes("public TymeacIndividualFunction[] getFunctions() {" + crlf);
		dataOutStream.writeBytes("  return functions;" + crlf);
		dataOutStream.writeBytes("} // end-method" + crlf);
		dataOutStream.writeBytes("} // end-class" + crlf);
			
  } // end-try
		  
  catch (IOException e) {
						  
		System.out.println(e);
			
		rc = 2;
    return ;

  } // end-catch                                
	  
  try {
		// close
		fileOutStream.close();
		dataOutStream.close();

  } // end-try          
		  
  catch (IOException e) {
						  
		System.out.println(e);
			
		rc = 3;
    return;

  } // end-catch

  // good 
  rc = 0;
  return;
	
} // end-method

 /**
 * Get the context for logging in. This is user defined  
 */
private LoginContext getContext() { 
         
  // new logon Context without call back handler
  return ClientSecurityModule.getBasicContext();    

} // end-method

} // end-class
