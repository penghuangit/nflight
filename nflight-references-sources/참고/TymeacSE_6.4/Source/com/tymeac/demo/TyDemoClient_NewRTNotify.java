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
 * 
 */
 
public class TyDemoClient_NewRTNotify {  

/**
 * @param args String[]
 */
 
public static void main(String[] args) {
  
  if  (args.length < 2)  {
      System.out.println("two args '1' or '2' and func name");
      return;
  }
  
  int rc = 0;   
 
 
  TyNewRTNotifyClient client = new TyNewRTNotifyClient();
  
  if ((args[0]).compareTo("1") == 0) {
    
      rc = client.resetNotify();          
  }
  else {
      rc = client.newNotify(args[1]);
  }  
  
  System.out.println("status is " + rc);
   
} // end-method
} // end-class
