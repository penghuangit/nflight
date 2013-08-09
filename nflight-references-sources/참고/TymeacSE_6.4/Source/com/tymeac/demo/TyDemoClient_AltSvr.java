package com.tymeac.demo;

/* 
 * Copyright (c) 2002 2012 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import com.tymeac.base.TyAltSvrElements;
import com.tymeac.client.*;
/**
 * Tymeac demonstration system. Display, (println), the result of
 *    Tymeac Alter Server
 * 
 */
 
public class TyDemoClient_AltSvr {  

/**
 * 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {   
        
  TyAltSvrClient client = new TyAltSvrClient();

  TyAltSvrElements back =  client.importElements(null);

  if  (back == null) {
      System.out.println("Import returned a null result" );
       return;
  }  
  
  System.out.println("Good ");
 
  return;
   
} // end-method
} // end-class
