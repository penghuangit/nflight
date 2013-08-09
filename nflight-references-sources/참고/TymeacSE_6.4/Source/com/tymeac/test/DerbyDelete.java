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
 * Delete various DBMS tables, for testing only.
 */
@SuppressWarnings("unused")
class DerbyDelete {
  
/**
 * Delete all functions.
 * @param stmt Statement
 */

private static void deleteFunc(Statement stmt)
      throws SQLException {
  
  // put your code here       
  String base = 
    "DELETE FROM FUNCTION_TABLE ";  
  
  stmt.executeUpdate(base);  
  
  System.out.println("Functions deleted");
  
} // end-method
/**
 * Delete all Lists.
 * @param stmt Statement
 */


private static void deleteList(Statement stmt)
      throws SQLException {
  
  // put your code here 
  String base = 
    "DELETE FROM LIST_TABLE ";  
   
  String L11 = base;
  
  stmt.executeUpdate(L11);  
  
  System.out.println("List deleted");
  
} // end-method
/**
 * Delete the log.
 * @param stmt Statement
 */

private static void deleteLog(Statement stmt)
      throws SQLException {
  
  // put your code here       
  String base = 
    "DELETE FROM LOG_TABLE ";
   
  String L11 = base;
  
  stmt.executeUpdate(L11);  
  
  System.out.println("Log deleted");
  
} // end-method
/**
 * Delete all queues.
 * @param stmt Statement
 */

private static void deleteQue(Statement stmt)
      throws SQLException {
      
  String base = 
    "DELETE FROM QUEUE_TABLE "; 
  
  stmt.executeUpdate(base);
  
  System.out.println("Queues deleted ");                  
  
} // end-method
/**
 * Delete stats.
 * @param stmt Statement
 */

private static void deleteStats(Statement stmt)
      throws SQLException {
  
     
  String base = 
    "DELETE FROM STATS_TABLE ";
   
  String L11 = base;
  
  stmt.executeUpdate(L11);
   
  System.out.println("Stats deleted");
  
} // end-method   
/**
 * Delete the tables
 * @param args String[]
 */

public static void main (String args[])
        throws SQLException { 

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
   
   
  
      //deleteFunc(stmt); 
     
     //deleteList(stmt);
      
      //deleteQue(stmt);
      
      deleteLog(stmt);
      
      deleteStats(stmt);
      
      
      stmt.close();
      con.close();
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
} // end-class
