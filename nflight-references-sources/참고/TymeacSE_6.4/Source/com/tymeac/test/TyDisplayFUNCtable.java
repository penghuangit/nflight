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

import java.rmi.*;
import com.tymeac.common.*;
import com.tymeac.base.*;
/**
 ** Display the Function Array, for testing only. 
 */

public class TyDisplayFUNCtable {

/**
 * Display Function Array
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {   
   
   TyParm parm = new TyParm("", 5, 1, 0);
   
   TyVariables TyV = new TyVariables();   
 
   try {
     TymeacInterface Ti =
       (TymeacInterface)Naming.lookup(TyV.getUrl() + TyV.getTymeac());
           
     
     System.out.println(Ti.diagnoseRequest(parm));
   
   
   } catch(Exception e) {  
       System.out.println("Error: " + e);  
   } // end-catch
  
} // end-method
} // end-class
