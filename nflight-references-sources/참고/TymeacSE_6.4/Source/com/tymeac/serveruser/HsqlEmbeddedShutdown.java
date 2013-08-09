package com.tymeac.serveruser;

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
import com.tymeac.base.*;

/**
 * Demonstration of Tymeac functionality. 
 *
 * 
 */
 
public class HsqlEmbeddedShutdown {  

/**
 * No argument constructor
 * 
 */
public HsqlEmbeddedShutdown() {
  
  // dbms
  Connection conn = null;
  Statement  stmt = null;
  
  // get the tymeac info instance
  TymeacInfo info = TymeacInfo.getInstance();
  
  try  {  
    // When no name use single parm constructor
    if  ((info.getDBUserid() == null) ||
         (info.getDBUserid().length() == 0)) {
      
        // no user name/password
        conn = DriverManager.getConnection(info.getDBUrl());
    }
    else {      
        // use all three parms
        conn = DriverManager.getConnection(info.getDBUrl(),  
                                          info.getDBUserid(),
                                          info.getDBPassword());
    } // endif  
  
   
    // get a stmt                                                                     
    stmt = conn.createStatement();
    
    // shut down
    stmt.executeQuery("SHUTDOWN");
    
    // close
    stmt.close();
    conn.close();
    
  } // end-try 
  
  catch (SQLException se) {
  
    // When printing
    System.out.println(se.getMessage()); 

  } // end-catch 
  
  catch (Exception e) {
  
    // When printing
    System.out.println(e.getMessage()); 

  } // end-catch 
        
} // end-constructor
} // end-class
