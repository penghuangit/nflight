package com.tymeac.demo;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.client.*;

/**
 * Test run time new stats
 */ 
public class TyDemoClient_NewRTStats {  

/**
 * @param args String[]
 */
 
public static void main(String[] args) {
  
  if  (args.length < 5)  {
      System.out.println("five args '1' or '2' and DBMS, Dir, FileName, Alt");
      return;
  }
 
  TyNewRTStatsClient client = new TyNewRTStatsClient();  
  
  if ((args[0]).compareTo("1") == 0) {
    
      System.out.println("status is " + client.resetStats());
      System.exit(0);       
  }
  
  String DBMS = null, Dir = null, FileName = null, Alt = null;
  
  if ((args[1]).compareTo("x") != 0) {
    
      DBMS = args[1];
    
  }
  else {
      if ((args[2]).compareTo("x") != 0) {
    
          Dir      = args[2];
          FileName = args[3];
    
      }
      else {
          if ((args[3]).compareTo("x") != 0) {
             
              FileName = args[3];
    
          }
          else {
              if ((args[4]).compareTo("x") != 0) {
             
                  Alt = args[4];
    
              }
          }          
      }
  }
   
  System.out.println("status is " + client.newStats(DBMS, Dir, FileName, Alt));   
   
} // end-method
} // end-class
