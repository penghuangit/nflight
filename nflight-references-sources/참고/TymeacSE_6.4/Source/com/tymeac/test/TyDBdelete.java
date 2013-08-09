package com.tymeac.test;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
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
class TyDBdelete {
  
/**
 * Delete all functions.
 * @param stmt Statement
 */

private static void deleteFunc(Statement stmt)
      throws SQLException {
  
  // put your code here       
  String base = 
    "DELETE FROM TYFBASE.TYFUNCTION ; ";  
  
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
    "DELETE FROM TYLBASE.TYLIST ; ";  
   
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
    "DELETE FROM TYLMSG.TYLOG ; ";
   
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
    "DELETE FROM TYQBASE.TYQUEUE "; 
  
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
    "DELETE FROM TYSBASE.TYSTATS ; ";
   
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

	// put your driver name here
  String dvr = "COM.ibm.db2.jdbc.app.DB2Driver";
  try {
      // load driver 
      Class.forName(dvr);

      //java.sql.DriverManager.registerDriver((Driver)
      //        Class.forName(dvr).newInstance());          
       
			// put your code here
      String url        = "jdbc:db2:TYMEAC";
      String user       = "ed";
      String password   = "ed";
      Connection con  = DriverManager.getConnection(url,  
                            user,
                            password);
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
  
} // end-method
} // end-class
