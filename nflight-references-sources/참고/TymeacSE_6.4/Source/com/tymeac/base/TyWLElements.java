package com.tymeac.base;

/* 
 * Copyright (c) 2002 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * The Queue Wait Lists passed information
 * 
 */
public final class TyWLElements 
        implements java.io.Serializable  {    
  
  static final long serialVersionUID = -3246568476435007852L;
  
  public int status  = 0;
  public int in_use  = 0;
  public int used    = 0;
  public int reset   = 0;
  public int hi_used = 0;
  public int over_p  = 0;
  public int over_s  = 0;  

} // end-class
