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

/**
 * Demonstration of Tymeac functionality. 
 *
 * 
 */
 
public class DerbyEmbeddedShutdown {  

/**
 * No argument constructor
 * 
 */
public DerbyEmbeddedShutdown() {
  
  try  {
    // try to shut down Derby embedded data base
    DriverManager.getConnection("jdbc:derby:;shutdown=true");
    
  } catch (SQLException se) {
  
    // When printing
    System.out.println(se.getMessage()); 

  } // end-catch 
        
} // end-constructor
} // end-class
