package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import javax.swing.*;
import java.sql.*;
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import com.tymeac.base.*;
import com.tymeac.client.*;
/**
 * Backend support for the Function GUI
 */
public class TyFuncMaintBean2 {
	
	// indicators
	private int does_exist = 0;
	private int import_Result;
	private int update_Result;
	private int delete_Result;
	private int error_que;
	private int nbr_que;
	
	// configuration data 
	private TyCfgFields fields = null; 
	
	// old name for update
	private String O_func_name = null;
		
	// new values from the window
	private String  N_func_name = null;
	private String  N_oa_name   = null;
	
	// work
	private boolean first_time = true; 
	private String[] my_list = null;
	
	// connection instance
	private Connection con = null;
	
	// statement                                                                     
	private Statement stmt = null;
  
  // security
  private LoginContext loginContext = null;
  
  //pass
  private String que;
  
  // back
  private int back;

/**
 * Constructor
 * @param cfg TyCfgFields - configuration file fields
 */
public TyFuncMaintBean2 (TyCfgFields cfg) {
    
  // cfg file fields
  fields = cfg;
  
  // security
  loginContext = getContext();
        
} // end-constructor

/**
 * Basic SQL prefix work
 */ 
private int basic() {          

  // When 1st time
  if  (first_time) {

      try {
        // When there is a mgr, try to load it
        if  (fields.dbms_DataManager != null)  {

            // load
            java.sql.DriverManager.registerDriver((Driver)
                  Class.forName(fields.dbms_DataManager).newInstance());
            
        } // endif
      } // end-try
                    
      catch (Exception ex) {

        // say what
        System.out.println ("Exception loading DriverManager: " + ex.getMessage());
      
        // done
        return 1;
      
      } // end-catch
      
      // not first_time now
      first_time = false;   
      
  } // endif   
  
  try {
    // When no name, use single parm constructor
    if  ((fields.dbms_UserName == null) || 
         (fields.dbms_UserName.length() == 0)) {

        // single
        con = DriverManager.getConnection(fields.dbms_URL);
      }
    else {      
        // use all three parms
        con = DriverManager.getConnection(fields.dbms_URL,  
                                          fields.dbms_UserName,
                                          fields.dbms_PassWord);
    } // endif            
      
    // get a stmt                                                                     
    stmt = con.createStatement();            
                                      
  } // end-try
  
  catch (SQLException ex) {

      System.out.println ("SQLException " + ex.getCause());
        
      while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");

      } // end-while 
      
      // done
      return 2;
                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

    System.out.println("Exception: " + ex);
    ex.printStackTrace (); 

    // done
    return 3;
                  
  } // end-catch 
  
  // good done
  return 0;

} // end-method  

/**
 * Delete button
 * @param S_func_name JTextField - function name
 */


public void deleteButton(javax.swing.JTextField S_func_name) {
    
  // set the queue name
  N_func_name = S_func_name.getText();
  
  // When not using security     
  if (loginContext == null) {
    
        System.out.println("UNSECURED modification"); 
        
      // non-secure
      doDelete();     
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
                    doDelete();
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
  
  // not 'read for update'
  if  (delete_Result == 0) does_exist = 0;
    
} // end-method

/**
 * delete the function
 * @return int
 */ 
private int deleteFunc() {    
        
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_FuncTable == null) {

      // failed
      delete_Result = 1;

      // bad exit
      return 1;

  } // endif
  
  int affected = 0;
  
  String delete   = "DELETE FROM " 
                    + fields.dbms_FuncTable
                    + " WHERE FUNC_NAME = '"
                    + N_func_name
                    + "'";
                   // + "'  ;";
                                 
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
      // say db error
      delete_Result = 3;
      
      // done
      return 3;
      
  } // endif    
                                                    
  //  
  try {                    
    // delete the queue
    affected = stmt.executeUpdate(delete);
      
    // When none were deleted, not found
    if  (affected != 1) {

        // ng
        delete_Result = 2;
        
        // close      
        stmt.close();
        con.close();

        // done
        return 2;

    } // endif
      
    // When delete the list of queues failed, done
    if  (deleteList(stmt) != 0) { 
      
        // close      
        stmt.close();
        con.close();
        
        return delete_Result;
        
    } // endif
              
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {

      System.out.println ("SQLException " + ex.getCause());
        
      while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");

      } // end-while

      // bad resule
      delete_Result = 3;

      // done
      return 3;
                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

      System.out.println("Exception: " + ex);
      ex.printStackTrace();
        
      // bad result
      delete_Result = 3;

      // done
      return 3;
                  
  } // end-catch 
  
  // good
  delete_Result = 0;          

  // done
  return 0;
    
} // end-method

/**
 * Delete the list of queues
 * @return int
 * @param stmt java.sql.Statement
 */ 
private int deleteList(Statement stmt) {    
    
  // to delete the requested rows
  String base   = "DELETE FROM " 
                      + fields.dbms_ListTable
                      + " WHERE FUNC_NAME = '"
                      + N_func_name
                      + "' AND SEQ_NBR > 0 ";
                    //+ "' AND SEQ_NBR > 0 ;";
  
  
  // do the requested function  
  try {
      // kill the list
      stmt.executeUpdate(base);
                        
  } // end-try  
  
  catch (SQLException ex) {
        System.out.println ("SQLException " + ex.getCause());
        
        while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");
        } // end-while
        
        // return with error
        delete_Result = 4;
        
        return 4;
        
  } // end-catch
  
  catch (java.lang.Exception ex)  {
        System.out.println("Exception: " + ex);
        ex.printStackTrace();
        
        // return with error
        delete_Result = 5;
        
        return 5;
        
 } // end-catch 
  
  delete_Result = 0;    
  return 0;
    
} // end-method

/**
 * Finish button
 * @param S_func_name JTextField - function name
 * @param S_oa_name JTextField - output agent name
 * @param S_que_list JList - the list of queues 
 */ 
public void finishButton( javax.swing.JTextField S_func_name,
                          javax.swing.JTextField S_oa_name,
                          javax.swing.JList S_que_list) {
                                
    
  // data of this function
  N_func_name = S_func_name.getText();
  N_oa_name   = S_oa_name.getText(); 

	// work
	int i = 0, j = 0; 

  // size of list
  nbr_que = S_que_list.getModel().getSize();

	// When there might be a null
	if  (nbr_que > 1) {

			// When 1st is dummy
			if  (((String) S_que_list.getModel().getElementAt(i)) == null) {

					// decrement count
					nbr_que--;

					// new String[] to hold the items
  				my_list = new String[nbr_que];
  
  				// get all the items
  				for  (i = 0, j = 1; i < nbr_que; i++, j++) {
  
			      // add to array
   				  my_list[i] = (String) S_que_list.getModel().getElementAt(j);
        
			    } // end-for
			}
			else {    
			    // new String[] to hold the items
  				my_list = new String[nbr_que];
  
			    // get all the items
  				for  (i = 0; i < nbr_que; i++) {
  
			      // add to array
  				  my_list[i] = (String) S_que_list.getModel().getElementAt(i);
        
			    } // end-for        
			} // endif
	}
	else {
			// new String[] to hold the items
  		my_list = new String[1];
      
      // add to array
  	  my_list[0] = (String) S_que_list.getModel().getElementAt(0);          

	} // endif
  
  // When not using security     
  if (loginContext == null) {
    
        System.out.println("UNSECURED modification"); 
        
      // non-secure
      doFinish();     
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
                    doFinish();
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
 * accessor for delete result
 * @return int
 */ 
public int getDelResult () { return delete_Result; } // end-method

/**
 * accessor for error queue
 * @return int
 */
public int getErrorQue () { return error_que; } // end-method

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
 * Import button
 * @param S_func_name JTextField - function name
 * @param S_oa_name JTextField - output agent name
 * @param S_que_list JList - the list of queues  
 */ 
@SuppressWarnings("unchecked")
public void importButton( JTextField S_func_name,
                          JTextField S_oa_name,
                          JList S_list) {    
   
  // remove all from list   
  S_list.removeAll();
  
  // does not exist
  does_exist = 0;
  
  // set the func name
  N_func_name = S_func_name.getText();
  
  // When not using security     
  if (loginContext == null) {
    
      System.out.println("UNSECURED Access"); 
        
      // non-secure
      doImport();     
  } 
  else {
      // When NOT a good login
      if  (!ClientSecurityModule.login(loginContext)) {            
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif
                
      try {
        System.out.println("Secured Access");         
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doImport();
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
  
  // When not there, return
  if  (import_Result != 0) {

      // error
      does_exist = 0;

      // done
      return;

  } // endif      
  
  // does exist, can do an update
  does_exist = 1;
  
  // data from load
  O_func_name = N_func_name;
  S_oa_name.setText(N_oa_name);
                                
  // update the JList
  S_list.setListData(my_list); 
    
} // end-method

/*
 * insert a function
 * @return int
 */
private int insertFunc() {
        
  // When no queues, bye
  if  (nbr_que < 1) {

      update_Result = 4;
      
      return 4;

  } // endif
  
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
      // say db error
      update_Result = 3;
      
      // done
      return 3;
      
  } // endif     
  
  // to check for existing
  String check = "SELECT FUNC_NAME FROM " 
                + fields.dbms_FuncTable
                + " WHERE FUNC_NAME = '"
                + N_func_name
                + "'  ";
            //+ "'  ;";
  
  // insert string                
  String insert = "INSERT INTO "
                + fields.dbms_FuncTable
                + " (FUNC_NAME, AGENT_NAME, NBR_QUEUES) VALUES ('"
                + N_func_name
                + "', '"
                + N_oa_name
                + "', "
                + nbr_que
                + ") ";  
          //+ ") ;";    
                    
 // do it   
  try {
    // see if there already
    ResultSet rs = stmt.executeQuery(check);
      
    // When nothing there OK
    if  (!rs.next()) {

        // ok
        ;
    }
    else {
        // cannot add that which already exists         
        update_Result = 10;
        
        return 10;

    } // endif
        
    // When did not insert: 
    if  (stmt.executeUpdate(insert) < 1) {
            
        // error
        update_Result = 6;
        
        return 6;
    }
    else {
        // When the list of que names is deleted ok 
        if  (deleteList(stmt) == 0) {
                  
            // insert the new list
            insertList(stmt);

        } // endif      
    } // endif      
                 
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {

      System.out.println ("SQLException " + ex.getCause());
        
      while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");
            
      } // end-while

      // done
      update_Result = 3;
      return 3;
                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

      System.out.println("Exception: " + ex);
      ex.printStackTrace();

      // done
      update_Result = 3;
      return 3;
              
  } // end-catch 
  
  // good
  return 0;
      
} // end-method 

/*
 * Insert into List Table
 * @param stmt Statement - statement object
 * @return int
 */ 
private int insertList(Statement stmt) {
   
  int affected = 0, seq = 1;
  
   // base for all inserts        
  String base1 =  "INSERT INTO "
                    + fields.dbms_ListTable
                    + " (FUNC_NAME, SEQ_NBR, QUEUE_NAME) VALUES ('"
                    + N_func_name
                    + "',";
  
  // second part after seq number                 
  String base2 =  ",'";
  
  // third part after que name
  String base3 = "')";   
  //String base3 = "') ;";   

  // full insert string
  String insert;
                          
 // do the work   
  try {
    // add each queue
    for (int i = 0; i < nbr_que; i++, seq++) {
              
      // form the insert statement
      insert =  base1 
              + seq
              + base2
              + my_list[i]
              + base3;  
          
      // insert the new que           
      affected += stmt.executeUpdate(insert);

    } // end-for          
  } // end-try
  
  catch (SQLException ex) {

      System.out.println ("SQLException " + ex.getCause());
        
      while (ex != null) {

            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");

      } // end-while

      // done
      update_Result = 5; 
        
      return 5;
                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

      System.out.println("Exception: " + ex);
      ex.printStackTrace();
        
      // done
      update_Result = 5; 
      
      return 5;
                        
 } // end-catch 
  
  return 0;
    
} // end-method

/**
 * load a function
 * @return int 
 */ 
private int loadFunc() {        
    
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
      // say db error
      import_Result = 3;
      
      // done
      return 3;
      
  } // endif      
  
  // for testing OA queue
  String temp = null;
  
  // to get the requested row
  String base = "SELECT * FROM " 
              + fields.dbms_FuncTable
              + " WHERE FUNC_NAME = '"
              + N_func_name
              + "'  ";
        //+ "'  ;";
  
  // the result of all this
  ResultSet rs;   
  
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_FuncTable == null) {

      // done
      import_Result = 1;
      
      return 1;

  } // endif  
  
  // Find the requested function  
  try {          
    // get the queue
    rs = stmt.executeQuery(base);
      
    // When nothing there:
    if  (!rs.next()) {
            
        //System.out.println (base);
        import_Result = 2;
        
        return 2;

    } // endif
      
    // get the fields 

    // OA   
    temp = rs.getString(2);

    // When there                 
    if  ((temp != null) && 
         (temp.compareTo(" ")) != 0) {
        
        // is an oa name
        N_oa_name = temp;
        
    } // endif
            
    // nbr of queues
    nbr_que = rs.getInt(3);
      
    // get the queue list
    if  (loadList(stmt) == 0) {
                          
        // When 0 we have one
        import_Result = 0;

    } // endif
              
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {
        System.out.println ("SQLException " + ex.getCause());
        
        while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");
        } // end-while
        
        // return with error
        import_Result = 3;
        return 3;
        
  } // end-catch
  
  catch (java.lang.Exception ex)  {
        System.out.println("Exception: " + ex);
        ex.printStackTrace();
        
        // return with error
        import_Result = 3;
        return 3;
        
  } // end-catch 
  
  // good return    
  return 0;
    
} // end-method

/**
 * load the list of queues
 * @return int
 * @param stmt java.sql.Statement
 */ 
private int loadList(Statement stmt) {
    
  // stuff
  int i = 0;
  
  // to get the requested rows
  String base   = "SELECT QUEUE_NAME FROM " 
                      + fields.dbms_ListTable
                      + " WHERE FUNC_NAME = '"
                      + N_func_name
                      + "' AND SEQ_NBR > 0 ";
              //+ "' AND SEQ_NBR > 0 ;";
  
  // the result of all this
  ResultSet rs;
  
  // the new list
  my_list = new String[nbr_que]; 
  
  // Find the requested function  
  try {
    // get the list
    rs = stmt.executeQuery(base);
      
    // fill up the list
    while (rs.next()) {
          
        // next string
        my_list[i] = rs.getString(1); 
          
        // next i
        i++;

    } // end-while          
                
    // When nothing there:
    if  (i == 0) {
                        
        //System.out.println (base);
        import_Result = 4;
        
        return 1;

    } // endif
      
    // When not a match
    if  (i != nbr_que) {
                        
        //System.out.println (base);
        import_Result = 6;
        
        return 6;

    } // endif
        
  } // end-try
  
  catch (SQLException ex) {

        System.out.println ("SQLException " + ex.getCause());
        
        while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");

        } // end-while
        
        // return with error
        import_Result = 5;
        
        return 5;
        
  } // end-catch
  
  catch (java.lang.Exception ex)  {

        System.out.println("Exception: " + ex);
        ex.printStackTrace();
        
        // return with error
        import_Result = 5;
        
        return 5;
        
  } // end-catch 
  
  // ok
  import_Result = 0;   
  
  return 0;
    
} // end-method

/*
 * update a function
 * @return int
 */
private int updateFunc() {
    
  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
      // say db error
      update_Result = 3;
      
      // done
      return 3;
      
  } // endif    
           
  // When no queues, bye
  if  (nbr_que < 1) {

      update_Result = 4;
      
      return 4;

  } // endif 
  
  // database string for update 
  String update   = "UPDATE "
                  + fields.dbms_FuncTable
                  + " SET "
                  + "AGENT_NAME = '"
                  + N_oa_name
                  + "', "
                  + "NBR_QUEUES = "
                  + nbr_que
                  + " WHERE "
                  + "FUNC_NAME = '" 
                  + N_func_name
                  + "'  ";
        //+ "' ; ";
                      
  // initially ok
  update_Result = 0;
 
  // do the work  
  try {                    
    // update the function, When ok 
    if  (stmt.executeUpdate(update) == 1) {
            
        // When the list of que names is deleted ok 
        if  (deleteList(stmt) == 0) {
                  
            // insert the new list
            insertList(stmt);

        } // endif
    }
    else {
        // function not found 
        update_Result = 2;

  } // endif            
               
  // all done         
  stmt.close();
  con.close();

  } // end-try
  
  catch (SQLException ex) {

      System.out.println ("SQLException " + ex.getCause());
        
      while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");

      } // end-while

      // error
      update_Result = 3;
      
      return 3;
                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

      System.out.println("Exception: " + ex);
      ex.printStackTrace();
        
      update_Result = 3; 
      
      return 3;         

  } // end-catch 
  
  // ok
  return update_Result;
        
} // end-method

/*
 * The oa que and all list queues must exist
 * @return int
 */
private int verifyQue() {

  // get basic db stuff
  int back = basic();
  
  // When an error
  if  (back != 0) {
  
      // say db error
      update_Result = 3;
      
      // done
      return 3;
      
  } // endif    
 
  // to get the que
  String que_base = "SELECT QUEUE_NAME, QUE_TYPE FROM " 
                  + fields.dbms_QueTable
                  + " WHERE QUEUE_NAME = '";
                      
  String que_last   = "'  ";
  //String que_last   = "'  ;";
  
  String inquiry;  
  
  // the result of all this
  ResultSet rs;     
            
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_QueTable == null) {

      update_Result = 1;      return 1;

  } // endif 
  
  // When no queues, bye
  if  (nbr_que < 1) {

      // is error
      update_Result = 4;      return 4;

  } // endif        
  
 // do it   
  try {
    // When an oa que, try it
    if  (N_oa_name.length() > 0) {
            
        // build the query
        inquiry = que_base
                + N_oa_name
                + que_last;
                        
        // see if there             
        rs = stmt.executeQuery(inquiry);          
            
        // When nothing there:
        if  (!rs.next()) {
                  
            // is error
            update_Result = 8;
            
            return 7;
        }
        else {
            // When not an OA Queue
            if  (rs.getInt(2) != 1) {
                        
                // is error
                update_Result = 9;
                
                return 7;

            } // endif      
        } // endif  
    } // endif      
                
    // do all the queues
    for (int i = 0; i < nbr_que; i++) {

			// skip nulls
			if  (my_list[i] != null) {									
          
      		// build the query
      		inquiry = que_base
              + my_list[i]
              + que_last;
                    
      		// see if there             
      		rs = stmt.executeQuery(inquiry);          
        
      		// When nothing there:
      		if  (!rs.next()) {
                  
        		  // is error
          		error_que = i;
          		update_Result = 11;

          		return 8;
          }
          else {
              // When an OA Queue
	            if  (rs.getInt(2) == 1) {
                    
		              // is error
  		            error_que = i;
    		          update_Result = 12;

      		        return 8;

              } // endif      
          } // endif
			} // endif
    } // end-for          
      
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {

      System.out.println ("SQLException " + ex.getCause());
        
      while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");
            
      } // end-while

      // error
      update_Result = 9;
      return 9;
                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

        System.out.println("Exception: " + ex);
        ex.printStackTrace();
        
        update_Result = 9;
        return 9;                 

 } // end-catch 
  
  return 0;
    
} // end-method

/*
 * The que must exist
 * @param que String - queue name
 * @return int
 */
public int verifySingle(String que) {

  // save
  this.que = que;
    
  // When not using security     
  if (loginContext == null) {
    
        System.out.println("UNSECURED modification"); 
        
      // non-secure
      doVerifySingle();     
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
                    doVerifySingle();
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
     
  // back
  return back;
    
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
private void doDelete() {

  // delete the que from the database
  deleteFunc();

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doFinish() {

  // When verify good, done
  if  (verifyQue() != 0)  return;
  
  // When doing an update
  if  (does_exist == 1) {
        
      // When the func name doesnt match what was 'read for update'
      if  (O_func_name.compareTo(N_func_name) != 0) {

          // error
          update_Result = 7;

          // done
          return;

      } // endif
        
      // update the data
      update_Result = updateFunc();
        
      // When successful
      if  (update_Result == 0) {
              
          // no longer 'read for update'
          does_exist = 0;

      } // endif      
  }
  else {
      // insert into db
      update_Result = insertFunc();

  } // endif

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doImport() {

  // get the func data from the database
  loadFunc();

} // end-method

/**
 * Executed as privileged or not as privileged
 */
private void doVerifySingle() {

  // get basic db stuff
  int see = basic();
  
  // When an error
  if  (see != 0) {
      
      // done
      back = 1;
      
      return;
      
  } // endif    
 
  // to get the que
  String que_base = "SELECT QUEUE_NAME, QUE_TYPE FROM " 
                  + fields.dbms_QueTable
                  + " WHERE QUEUE_NAME = '";
                      
  String que_last   = "'  ";
  // String que_last   = "'  ;";
  
  String inquiry;    
  
  // the result of all this
  ResultSet rs;      
            
  // When the database name is null, the cfg file read failed
  if  (fields.dbms_QueTable == null) {

      // cfg error
      back = 2;
      
      return;

  } // endif 
  
  // When no queues, bye
  if  (que.length()  < 1) {

      // is error 
      back = 3;
      
      return;

  } // endif          
  
 // do it   
  try {                
    // build the query
    inquiry = que_base
            + que
            + que_last;
                        
    // see if there             
    rs = stmt.executeQuery(inquiry);          
            
    // When nothing there:
    if  (!rs.next()) {
                
        // is error
        back = 4;
        
        return;
         
    } // endif  
    
    // When an OA Queue
    if  (rs.getInt(2) == 1) {
                        
       // is not normal queue
       back = 7;
       
       return;

    } // endif              
      
    // all done         
    stmt.close();
    con.close();

  } // end-try
  
  catch (SQLException ex) {

      System.out.println ("SQLException " + ex.getCause());
        
      while (ex != null) {
            System.out.println ("SQLState: " 
                                    + ex.getSQLState());
            System.out.println ("Message:  " 
                                    + ex.getMessage());
            System.out.println ("Vendor:   " 
                                    + ex.getErrorCode());
            ex = ex.getNextException();
            System.out.println ("");
            
      } // end-while

      // error
      back = 5;
      return;
                  
  } // end-catch
  
  catch (java.lang.Exception ex)  {

      System.out.println("Exception: " + ex);
      ex.printStackTrace();
                             
      back = 6;   
      
      return;

 } // end-catch 
  
 // good
 back = 0;                               

} // end-method
} // end-class
