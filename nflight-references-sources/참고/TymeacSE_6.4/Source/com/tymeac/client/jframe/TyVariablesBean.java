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
 * Backend class to support the variables GUI 
 */
public final class TyVariablesBean {

	// work
  private int rc;
  private String dir;
  
  // security
  private LoginContext loginContext = null;
  
  private String found;

	private boolean F_use_rmi = false;
	private boolean F_use_iiop = false;
	private boolean F_use_alt = false;
	
	private String F_url_name = null;
	private String F_ty_name = null;
	private String F_iiop_name = null;
	private String F_alt_name = null;

	private int F_rmi_result  = 0;
	private int F_iiop_result = 0;
	private int F_alt_result  = 0;

/**
 * Constructor
 */  
public TyVariablesBean() {
  
  // security
  loginContext = getContext();
  
} // end-constructor  
  
/**
 * finish alt button
 * @param dir String - dir name
 * @param alt_name JTextField - alternative class name
 */ 
public void finishAltButton ( String dir,
              								JTextField alt_name) {

	// work
	String test;

	// alt class
	test = alt_name.getText();

	// When the field is missing
	if (test.length() == 0) {

			// must be there
			F_alt_result = 1;
			
			// ng
			return;
	}
	else {
			// what was passed
  		F_alt_name = test;

			// good
			F_alt_result = 0;

	} // endif

	// use iiop
	F_use_alt = true;

	// others false
	F_use_rmi  = false;
	F_use_iiop = false;

  // do the file write
  fileWrite(dir);
   
} // end-method

/**
 * finish Basic button
 * @param dir String - dir name
 * @param url_name JTextField - URL name
 * @param ty_name JTextField - tymeac name
 */ 
public void finishBasicButton ( String dir,
              									JTextField url_name,
              									JTextField ty_name) {
	// work
	String test;

	// rmi url
	test = url_name.getText();

	// When the field is missing
	if (test.length() == 0) {

			// must be there
			F_rmi_result = 1;
			
			// ng
			return;
	}
	else {
			// what was passed
  		F_url_name = test;

	} // endif

	// tymeac name
	test = ty_name.getText();

	// When the field is missing
	if (test.length() == 0) {

			// must be there
			F_rmi_result = 2;
			
			// ng
			return;
	}
	else {
			// what was passed
  		F_ty_name = test;

	} // endif

	// good
	F_rmi_result = 0;
			
	// use rmi
	F_use_rmi = true;

	// others false
	F_use_iiop = false;
	F_use_alt = false;

  // do the file write
  fileWrite(dir);
   
} // end-method

/**
 * finish IIOP button
 * @param dir String - dir name
 * @param iiop_name JTextField - iiop name
 */ 
public void finishIIOPButton ( String dir,
              									JTextField iiop_name) {

	// work
	String test;

	// rmi url
	test = iiop_name.getText();

	// When the field is missing
	if (test.length() == 0) {

			// must be there
			F_iiop_result = 1;
			
			// ng
			return;
	}
	else {
			// what was passed
  		F_iiop_name = test;

	} // endif

	// good
	F_iiop_result = 0;
			
	// use iiop
	F_use_iiop = true;

	// others false
	F_use_rmi = false;
	F_use_alt = false;

 // do the file write
  fileWrite(dir);
   
} // end-method

/**
 * accessor for alt result
 * @return int
 */ 
public int getAltResult () { return F_alt_result; } // end-method

/**
 * accessor for basic result
 * @return int
 */ 
public int getBasicResult () { return F_rmi_result; } // end-method

/**
 * read the file
 * @param dir String - dir name
 * @return int
 */ 
public int getFile (String dir) {	
	  
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
		bufferedReader = new BufferedReader(new FileReader(newDir + "TyVariables.java"));

  } // end-try
		  
  catch (IOException e) {
			
		// print error              
		System.out.println(e.toString());          

		// get out
		return 100; 
				
  } // end-catch 

  // work
  int count = 4, i, j;
	String isNull = "null";

	// starting
	F_url_name  = null;
	F_ty_name   = null;
	F_iiop_name = null;
	F_alt_name  = null;
										
  // read each line of the file   
  try { 
		// bypass until class name
    while (true) {

      // read a line
      found = bufferedReader.readLine();

      // When at end
      if  (found == null) return 1;

      // When  class
    if  (found.equalsIgnoreCase("public class TyVariables {")) break;
    if  (found.equalsIgnoreCase("public final class TyVariables {")) break;

    } // end-while

		// new count
		count = 4;

		// bypass the next part
		while (count > 0) {

		  // read a line
 			found = bufferedReader.readLine();

		  // When at end
 			if  (found == null) break;

			// decrement
			count--;

		} // end-while

	  // When premature end
		if  (count > 0) return 1;

		// line length
		len = found.length();
		i = 0;

// do URL

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\"")) break;

			i++;
			len--;

		} // end-while

		// When at end
		if  (len <= 0) F_url_name = null;
		
		// save first index
		j = ++i;

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\"")) 	break;

			i++;
			len--;

		} // end-while

		// When not at end
		if  (len > 0) {

				// When a null value
				if  (found.substring(j, i).compareTo(isNull) == 0) {

						// is null
						F_url_name = null;				
				}
				else {

						// set notify name
						F_url_name = found.substring(j, i);
		
				} // endif
	  } // endif

		// read a line
	 	found = bufferedReader.readLine();

	  // When at end, done
		if  (found == null)	return 3;

		// line length
		len = found.length();
		i = 0;

// do tymeac name

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\"")) 	break;

			i++;
			len--;

		} // end-while

		// When at end
		if  (len <= 0) 	F_ty_name = null;
		
		// save first index
		j = ++i;

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\"")) 	break;

			i++;
			len--;

		} // end-while

		// When not at end
		if  (len > 0) {

				// When a null value
				if  (found.substring(j, i).compareTo(isNull) == 0) {

						// is null
						F_ty_name = null;				
				}
				else {

						// set name 
						F_ty_name = found.substring(j, i);
		
				} // endif
	  } // endif

		// read a line
	 	found = bufferedReader.readLine();

	  // When at end, done
		if  (found == null)	return 3;

		// line length
		len = found.length();
		i = 0;

// do iiop name

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\"")) 	break;

			i++;
			len--;

		} // end-while

		// When at end
		if  (len <= 0) 	F_iiop_name = null;
		
		// save first index
		j = ++i;

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\""))  break;

			i++;
			len--;

		} // end-while

		// When not at end
		if  (len > 0) {

				// When a null value
				if  (found.substring(j, i).compareTo(isNull) == 0) {

						// is null
						F_iiop_name = null;				
				}
				else {

						// set notify name
						F_iiop_name = found.substring(j, i);
		
				} // endif
	  } // endif

		// read a line
	 	found = bufferedReader.readLine();

	  // When at end, done
		if  (found == null)	return 3;

		// line length
		len = found.length();
		i = 0;

// do alt

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\""))  break;

			i++;
			len--;

		} // end-while

		// When at end
		if  (len <= 0) 	F_alt_name = null;
		
		// save first index
		j = ++i;

		while (len > 0) {

			// When a quote
			if  (found.substring(i, i+1).startsWith("\""))  break;

			i++;
			len--;

		} // end-while

		// When not at end
		if  (len > 0) {

				// When a null value
				if  (found.substring(j, i).compareTo(isNull) == 0) {

						// is null
						F_alt_name = null;				
				}
				else {

						// set notify name
						F_alt_name = found.substring(j, i);
		
				} // endif
	  } // endif
  } // end-try
		  
  catch (IOException e) {
		  
		// print exception
		System.out.println(e);

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

  // good
  return 0;                             
	  
} // end-method

/**
 * accessor for iiop result
 * @return int
 */ 
public int getIIOPResult () { return F_iiop_result; } // end-method

/**
 * import alt button
 * @param dir String - dir name
 * @param alt_name JTextField - alt class name
 */ 
public void importAltButton (String dir,
            JTextField alt_name) {
	
  // do the file read and load
	rc = 9;
	rc =  fileRead(dir);

  // when good
	if  (rc !=0) {

			// ng
			F_alt_result = rc;

			// exit
			return;

	} // endif  
  
  // set alt class name
  alt_name.setText(F_alt_name);

	// When not there
	F_alt_result = (F_alt_name  == null)? 1 : 0;

} // end-method

/**
 * finish Basic button
 * @param dir String - dir name
 * @param url_name JTextField - URL name
 * @param ty_name JTextField - tymeac name
 */ 
public void importBasicButton (String dir,
            JTextField url_name,
            JTextField ty_name) {

	
  // do the file read and load
	rc = 9;
	rc =  fileRead(dir);

  // when good
	if  (rc !=0) {

			// ng
			F_rmi_result = rc;

			// exit
			return;

	} // endif  

  // set url
  url_name.setText(F_url_name);

  // set tymeac name
  ty_name.setText(F_ty_name);

	// When not there
	F_rmi_result = ((F_url_name == null) &&
			            (F_ty_name  == null))?
                1 : 0;

} // end-method

/**
 * finish Basic button
 * @param dir String - dir name
 * @param iiop_name JTextField - iiop name
 */ 
public void importIIOPButton (String dir,
            JTextField iiop_name) {
	
  // do the file read and load
	rc = 9;
	rc =  fileRead(dir);

	// when good
	if  (rc !=0) {

			// ng
			F_iiop_result = rc;

			// exit
			return;

	} // endif  
  
  // set iiop name
  iiop_name.setText(F_iiop_name);

	// When not there
	F_iiop_result = (F_iiop_name  == null)? 1 : 0;

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
		fileOutStream = new FileOutputStream(newDir + "TyVariables.java");

  } // end-try
		  
  catch (IOException e) {
						  
		// to string
		System.out.println(e);

		// bad open 
		return 1001;
				
  } // end-catch  
	
  // new out stream   
  dataOutStream = new DataOutputStream(fileOutStream);
  
  try {
		// each line 
		dataOutStream.writeBytes("package com.tymeac.common;" + crlf);	
		dataOutStream.writeBytes("// DO NOT EDIT THIS FILE" + crlf);
		dataOutStream.writeBytes("  " + crlf);
		dataOutStream.writeBytes("public class TyVariables {" + crlf);	

		// When basic rmi
		if  (F_use_rmi) {

				// data			
				dataOutStream.writeBytes("  private static final boolean tymeac_use_rmi       = true;" + crlf);				
				dataOutStream.writeBytes("  private static final boolean tymeac_use_iiop      = false;" + crlf);				
				dataOutStream.writeBytes("  private static final boolean tymeac_use_alternate = false;" + crlf);				
	
				// When a url
				if  (F_url_name != null) { 

						// set data
						dataOutStream.writeBytes("  private static final String  tymeac_url_name      = " + "\"" + F_url_name + "\"" + ";" + crlf);				

				}
				else {
						// set data
						dataOutStream.writeBytes("  private static final String  tymeac_url_name      = null;" + crlf);				
			
				} // endif

				dataOutStream.writeBytes("  private static final String  tymeac_name          = " + "\""  + F_ty_name + "\"" + ";" + crlf);
				dataOutStream.writeBytes("  private static final String  tymeac_iiop_name     = null;" + crlf);				
				dataOutStream.writeBytes("  private static final String  tymeac_alternate_class_name = null;" +  crlf);
		
		} // endif

		// When iiop
		if  (F_use_iiop) {

				// data			
				dataOutStream.writeBytes("  private static final boolean tymeac_use_rmi       = false;" + crlf);				
				dataOutStream.writeBytes("  private static final boolean tymeac_use_iiop      = true;" + crlf);				
				dataOutStream.writeBytes("  private static final boolean tymeac_use_alternate = false;" + crlf);				
	
				dataOutStream.writeBytes("  private static final String  tymeac_url_name      = null;" + crlf);				
				dataOutStream.writeBytes("  private static final String  tymeac_name          = null;" + crlf);
				dataOutStream.writeBytes("  private static final String  tymeac_iiop_name     = " + "\""  + F_iiop_name + "\""  + ";"  + crlf);				
				dataOutStream.writeBytes("  private static final String  tymeac_alternate_class_name = null;" +  crlf);
		
		} // endif

		// When alt
		if  (F_use_alt) {

				// data			
				dataOutStream.writeBytes("  private static final boolean tymeac_use_rmi       = false;" + crlf);				
				dataOutStream.writeBytes("  private static final boolean tymeac_use_iiop      = false;" + crlf);				
				dataOutStream.writeBytes("  private static final boolean tymeac_use_alternate = true;" + crlf);				
	
				dataOutStream.writeBytes("  private static final String  tymeac_url_name      = null;" + crlf);				
				dataOutStream.writeBytes("  private static final String  tymeac_name          = null;" + crlf);
				dataOutStream.writeBytes("  private static final String  tymeac_iiop_name     = null;" + crlf);				
				dataOutStream.writeBytes("  private static final String  tymeac_alternate_class_name = " + "\"" + F_alt_name + "\""  + ";"  +  crlf);
				dataOutStream.writeBytes("  " + crlf);			

		} // endif

		// the other stuff	

		dataOutStream.writeBytes("  " + crlf);
		dataOutStream.writeBytes("public String getAltName() { " + crlf);																							
		dataOutStream.writeBytes("   return tymeac_alternate_class_name;"  + crlf);																							
		dataOutStream.writeBytes("} // end-method" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																				
		dataOutStream.writeBytes("public String getIIOPName ( ) { " + crlf);																							
		dataOutStream.writeBytes("  return tymeac_iiop_name;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																				
		dataOutStream.writeBytes("public String getTymeac ( ) {" + crlf);																							
		dataOutStream.writeBytes("  return tymeac_name;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																		
		dataOutStream.writeBytes("public String getUrl ( ) { " + crlf);																							
		dataOutStream.writeBytes("  return tymeac_url_name; " + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
															
		dataOutStream.writeBytes("public boolean  useAlt() { " + crlf);																							
		dataOutStream.writeBytes("  return tymeac_use_alternate;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																				
		dataOutStream.writeBytes("public boolean  useIIOP() {" + crlf);																							
		dataOutStream.writeBytes("  return tymeac_use_iiop;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																			
		dataOutStream.writeBytes("public boolean  useRMI() { " + crlf);																							
		dataOutStream.writeBytes("  return tymeac_use_rmi;" + crlf);																							
		dataOutStream.writeBytes("} // end-method	" + crlf);																							
		dataOutStream.writeBytes("  " + crlf);
																					
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
 * Get the file  
 */
private int fileRead(String dir) {
   
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
   
  // done
  return rc;

} // end-method

 /**
 * write the file  
 */
private int fileWrite(String dir) {
   
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
         
   // done
  return rc;

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

  rc = writeJava(dir);

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doRead() {

  rc = getFile(dir);

} // end-method
} // end-class
