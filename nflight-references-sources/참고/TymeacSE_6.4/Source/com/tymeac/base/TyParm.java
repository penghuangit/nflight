package com.tymeac.base;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Class used by diagnose method in the IMPL class
 */ 
public final class TyParm 
   implements java.io.Serializable  {
  
  static final long serialVersionUID = -4635611585130519244L;
  
  // instance fields
  private String string_1;
  private int    parm_1;
  private int    parm_2;
  private int    parm_3;  

/**
 * Constructor
 * @param c_string-1 String
 * @param c_parm_1 int
 * @param c_parm_2 int
 * @param c_parm_3 int
 */ 
public TyParm (String c_string_1, 
               int c_parm_1, int c_parm_2, int c_parm_3) {
                    
    string_1 = c_string_1;
    parm_1   = c_parm_1;
    parm_2   = c_parm_2;
    parm_3   = c_parm_3;
                    
} // end-constructor

/**
 *
 * @return java.lang.String
 */
public int getParm1 ( ) { return parm_1; } // end-method

/**
 *
 * @return java.lang.String
 */
public int getParm2 ( ) { return parm_2; } // end-method

/**
 *
 * @return java.lang.String
 */
public int getParm3 ( ) { return parm_3; } // end-method

/**
 *
 * @return java.lang.String
 */
public String getString ( ) { return string_1; } // end-method
} // end-class
