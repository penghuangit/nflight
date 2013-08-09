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
 * Retrieve various DBMS tables, for testing only.
 * 
 */
 
public class TyDBretrieve {  

/**
 * Retrieve tables
 * @param args String[] 
 */

public static void main (String args[]) { 
  try {
      // load driver 
      Class.forName("COM.ibm.db2.jdbc.app.DB2Driver"); 
      
      String url = "jdbc:db2:TYMEAC";
      String user = "ed";
      String password = "ed";
      Connection con = DriverManager.getConnection( url,  
                                user,
                                password);
                                 
      Statement stmt = con.createStatement();
   
      
      retrieveQue(stmt);
      
      
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
/**
 * Retrieve queues
 * @return int
 * @param stmt java.sql.Statement
 */
 
private static int retrieveQue(Statement stmt) {
  
  // the result of all this
  ResultSet rs, mrs;
  int nbr_que = 0;
  int nbr_calc = 0;
  
  String q_name;
  int    nbr_threads;
  
  // get the header
  String header = "TyQueueHeader26446138010000";
  String base   = "SELECT NBR_WAITLISTS FROM " 
            + "TYQBASE.TYQUEUE" 
            + " WHERE QUEUE_NAME = '"
            + header
            + "'  ;";
            
  String rest   = "SELECT * FROM " 
            + "TYQBASE.TYQUEUE ;"; 
  
  try {
          
      // get the header
      rs = stmt.executeQuery(rest);
      
      // loop thru all
      while (rs.next()) {
        
        nbr_que++;
      } // end-while        
      
      
      // When nogood, rc=0
      if  (nbr_que == 0) {
        System.out.println("number of queues is zero");
        return 0;
      } // endif      
  
      System.out.println("number of queues is:" + (nbr_que -1));
      
      // get the full load
      mrs = stmt.executeQuery(rest);
      
      // loop thru all
      while (mrs.next()) {
        
        q_name = mrs.getString(1);
        
        if  (q_name.equalsIgnoreCase(header)) {
          ;
        }
        else {
            
          nbr_threads = mrs.getInt(4);          
          nbr_calc += nbr_threads;
        
          System.out.println("Que is    :" + q_name);
          System.out.println("Threads is:" + nbr_threads);
          System.out.println(" ");
        } // endif                
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
  
  System.out.println("number calc is:" + nbr_calc);
  
  return nbr_calc;
  
} // end-method
} // end-class
