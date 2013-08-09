package com.tymeac.base;

/* 
 * Copyright (c) 2002 - 2011 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * This class contains the objects for the Alter Server Options client.
 */
public final class TyAltSvrElements 
      implements java.io.Serializable  {  
  
  private static final long serialVersionUID = 6158277812486255146L;  
  
  private boolean shut = false;  // shut down exit(0)
  private int monitor  = 0;  // monitor interval seconds
  private int inactive = 0;  // inactivate minutes

/**
 * Constructor
 * @param shut_down
 * @param mon
 * @param inact
 */
public TyAltSvrElements(boolean shut_down, int mon, int inact) {
  
  shut     = shut_down;
  monitor  = mon;
  inactive = inact;

} // end-constructor

/**
 * Return the monitor interval
 * @return int
 */
public int getMonInterval() { return monitor; } // end-method

/**
 * Return the inactivate minutes
 * @return int
 */
public int getInactivate() { return inactive; } // end-method

/**
 * Return the shut down thread usage
 * @return boolean
 */
public boolean getShut() { return shut; } // end-method
} // end-class
