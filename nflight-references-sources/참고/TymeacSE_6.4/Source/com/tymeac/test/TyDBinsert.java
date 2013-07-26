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
 * Insert various DBMS tables, for testing only.
 */
@SuppressWarnings("unused")
class TyDBinsert {  

/**
 * Insert functions.
 * @param stmt Statement
 */

private static void insertFunc(Statement stmt)
      throws SQLException {
      
  String base = 
    "INSERT INTO TYFBASE.TYFUNCTION (FUNC_NAME, AGENT_NAME, NBR_QUEUES) ";
  
  
  String F1 = base       
          + " VALUES ('Function_1', ' ', 1) ;";
  
  stmt.executeUpdate(F1);
  
  
  String F2 = base       
          + " VALUES ('Function_2', ' ', 1) ;";
  
  stmt.executeUpdate(F2);
  
  String F3 = base       
          + " VALUES ('Function_3', ' ', 2) ;";
  
  stmt.executeUpdate(F3);
  
  String F4 = base       
          + " VALUES ('Function_4', ' ', 3) ;";
  
  stmt.executeUpdate(F4);
  
  String F5 = base       
          + " VALUES ('Function_5', 'DDDD', 1) ;";
  
  stmt.executeUpdate(F5);
  
  String F6 = base       
          + " VALUES ('Function_6', 'DDDD', 2) ;";
  
  stmt.executeUpdate(F6);
  
  String F7 = base       
          + " VALUES ('Function_7', 'DDDD', 3) ;";
  
  stmt.executeUpdate(F7);
  
  String F8 = base       
          + " VALUES ('Function_8', ' ', 1) ;";
  
  stmt.executeUpdate(F8);
  
  String F9 = base       
          + " VALUES ('TyNotify', ' ', 1) ;";
  
  stmt.executeUpdate(F9);
  
  System.out.println("Func finished");
  
  
} // end-method  
/**
 * Insert Lists.
 * @param stmt Statement
 */

private static void insertList(Statement stmt)
      throws SQLException {
  
  // base for all inserts       
  String base = 
    "INSERT INTO TYLBASE.TYLIST (FUNC_NAME, SEQ_NBR, QUE_NAME) ";
  
  
  // function 1, 1-que
  String L11 = base        
          + " VALUES ('Function_1', 1, 'AAAA') ;";
  stmt.executeUpdate(L11);
  
  
  // function 2, 1-que 
  String L21 = base        
          + " VALUES ('Function_2', 1, 'AAAA') ;";
  stmt.executeUpdate(L21);
  
  
  // function 3, 2-ques
  String L31 = base        
          + " VALUES ('Function_3', 1, 'AAAA') ;";
  stmt.executeUpdate(L31);
  
  String L32 = base        
          + " VALUES ('Function_3', 2, 'BBBB') ;";
  stmt.executeUpdate(L32);
  
  
  // function 4, 3-ques
  String L41 = base        
          + " VALUES ('Function_4', 1, 'AAAA') ;";
  stmt.executeUpdate(L41);
  
  String L42 = base        
          + " VALUES ('Function_4', 2, 'BBBB') ;";
  stmt.executeUpdate(L42);
  
  String L43 = base        
          + " VALUES ('Function_4', 3, 'CCCC') ;";
  stmt.executeUpdate(L43);
  
  
  // function 5, 1-que 
  String L51 = base        
          + " VALUES ('Function_5', 1, 'AAAA') ;";
  stmt.executeUpdate(L51);
  
  
  // function 6, 2-ques
  String L61 = base        
          + " VALUES ('Function_6', 1, 'AAAA') ;";
  stmt.executeUpdate(L61);
  
  String L62 = base        
          + " VALUES ('Function_6', 2, 'BBBB') ;";
  stmt.executeUpdate(L62);
  
  
  // function 7, 3-ques
  String L71 = base        
          + " VALUES ('Function_7', 1, 'AAAA') ;";
  stmt.executeUpdate(L71);
  
  String L72 = base        
          + " VALUES ('Function_7', 2, 'BBBB') ;";
  stmt.executeUpdate(L72);
  
  String L73 = base        
          + " VALUES ('Function_7', 3, 'CCCC') ;";
  stmt.executeUpdate(L73);
  
  
  // fucntion 8, 1-que 
  String L8 = base       
          + " VALUES ('Function_8', 1, 'EEEE') ;";
  stmt.executeUpdate(L8);
  
  
  // function notify, 1-que
  String L9 = base       
          + " VALUES ('TyNotify', 1, 'Notify') ;";
  stmt.executeUpdate(L9);
  
  System.out.println("List done");
  
} // end-method
/**
 * Insert queues.
 * @param stmt Statement
 */

private static void insertQue(Statement stmt)
      throws SQLException {
      
  String base = 
    "INSERT INTO TYQBASE.TYQUEUE " +
    "(QUEUE_NAME, PA_CLASS, NBR_WAITLISTS, NBR_IN_WAITLIST, WAIT_TIME, NBR_THREADS, " +
    " QUE_TYPE, OVERALL_PERCENT, INDIVIDUAL_PERCENT, WEIGHTED_FACTOR, WEIGHTED_AVERAGE) ";
  
  // que AAAA
  String Q1 = base       
          + " VALUES ('AAAA', 'Demo1', 3, 3, 20, 3, 0, 75.0, 75.0, 0.0, 0.0) ;";
  
  stmt.executeUpdate(Q1);
  
  
  // que BBBB
  String Q2 = base       
          + " VALUES ('BBBB', 'Demo2', 3, 3, 20, 3, 0, 75.0, 75.0, 0.0, 0.0) ;";
  
  stmt.executeUpdate(Q2);
  
  // que CCCC
  String Q3 = base       
          + " VALUES ('CCCC', 'Demo3', 3, 3, 20, 3, 0, 75.0, 75.0, 0.0, 0.0) ;";
  
  stmt.executeUpdate(Q3);
  
  // que DDDD
  String Q4 = base       
          + " VALUES ('DDDD', 'DemoAgent1', 3, 3, 20, 3, 1, 75.0, 75.0, 0.0, 0.0) ;";
  
  stmt.executeUpdate(Q4);
  
  // que EEEE
  String Q5 = base       
          + " VALUES ('EEEE', 'Demo5', 3, 3, 20, 3, 0, 75.0, 75.0, 0.0, 0.0) ;";
  
  stmt.executeUpdate(Q5);
  
  // que Notify
  String Q6 = base       
          + " VALUES ('Notify', 'DemoNotify', 3, 3, 20, 3, 0, 75.0, 75.0, 0.0, 0.0) ;";
  
  stmt.executeUpdate(Q6);
  
  
  System.out.println("Queues done ");                 
  
} // end-method
/**
 * Insert elements into tables
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
   
  
      //insertFunc(stmt); 
     
     //insertList(stmt);
      
      insertQue(stmt);
      
      //insertLend(stmt);
      
      //deleteFunc(stmt);
      
      
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
