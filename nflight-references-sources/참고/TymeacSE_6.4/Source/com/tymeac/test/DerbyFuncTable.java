package com.tymeac.test;

/* 
 * Copyright (c) 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.sql.*;
/**
 * Retrieve various DBMS tables, for testing only.
 * 
 */
 
public class DerbyFuncTable {  

/**
 * Retrieve tables
 * @param args String[] 
 */

public static void main (String args[]) { 
  
  // drive and url for net
  String driver = "org.apache.derby.jdbc.ClientDriver"; 
  String url    = "jdbc:derby://localhost:1527/TYMEAC";
  
  // is embedded or not
  boolean embedded = false;
  
  // When no args
  if  ((args == null) ||
       (args.length == 0)) {
    
    // ignore
    ;
  }
  else {    
      // When the proper arg
      if  (args[0].substring(0,9).equalsIgnoreCase("-embedded")) {      
      
          // set for embedded
          driver   = "org.apache.derby.jdbc.EmbeddedDriver"; 
          url      = "jdbc:derby:TYMEAC";
          embedded = true;
          
      } // endif      
  } // endif  
  
  try {
      // load driver 
      Class.forName(driver);       
     
   //   String user = "ed";
   //   String password = "ed";
   //   Connection con = DriverManager.getConnection( url,  
   //                             user,
   //                             password);
      Connection con = DriverManager.getConnection(url);
                                 
      Statement stmt = con.createStatement();
   
      
      int back = retrieveQue(stmt);
      
      
      stmt.close();
      con.close();
      
      System.out.println ("Total Printed=" + back);
  }
  catch (SQLException ex) {
      System.out.println ("SQLException:");
      while (ex != null) {
        System.out.println ("SQLState: " 
                    + ex.getSQLState());
        System.out.println ("Message:  " 
                    + ex.getMessage());
        System.out.println ("Vendor:   " 
                    + ex.getErrorCode());
        ex = ex.getNextException();
        System.out.println ("");
      }
  }
  
  catch (java.lang.Exception ex)  {
      System.out.println("Exception: " + ex);
      ex.printStackTrace ();
  }
  
  // When embedded
  if  (embedded) {
    
    try  {
    // try to shut down embedded data base
    DriverManager.getConnection("jdbc:derby:;shutdown=true" );
    
    } catch (SQLException se) {
  
      System.out.println(se.getMessage());
    
    } // end-catch 
  } // endif
  
} // end-method
/**
 * Retrieve queues
 * @return int
 * @param stmt java.sql.Statement
 */
 
private static int retrieveQue(Statement stmt) {
  
  // the result of all this
  ResultSet rs;
  int nbr_calc = 0;
  
  // get the header
  String base   = "SELECT * FROM FUNCTION_TABLE ";             
  
  try {          
      // get the header
      rs = stmt.executeQuery(base);
      
      // loop thru all
      while (rs.next()) {
        
        System.out.println(rs.getString(1));
        System.out.println(rs.getString(2));
        System.out.println(rs.getInt(3));
        System.out.println(" ");
        
        nbr_calc++;
        
      } // end-while 
        
  } // end-try
  
  catch (SQLException ex) {
      System.out.println ("SQLException:");
      
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
  } // end-catch
  
  catch (java.lang.Exception ex)  {
      System.out.println("Exception: " + ex);
      ex.printStackTrace ();
   } // end-catch 
  
  return nbr_calc;
  
} // end-method
} // end-class
