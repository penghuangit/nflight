package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Used for the Run Time Functions GUI 
 */ 
public final class TyRunTimeElements 
        implements java.io.Serializable  {
  
    static final long serialVersionUID = -1943344206828695611L;
  
  private String FuncName = null; // notify function
  private String DBMS     = null; // log/stats dbms table name 
  private String Dir      = null; // log/stats directory   
  private String FileName = null; // log/stats file name
  private String Alt      = null; // log/stats alternate class name
  
  private boolean alive   = true; // logging is alive
  private boolean inUse   = true; // logging is in use

/**
 * Normal constructor, no arg
 */  
public TyRunTimeElements () {
  
} // end-constructor

/**
 * Used for returning run time is not alive
 * @param dead
 */
public TyRunTimeElements (boolean dead) {
  
  this.alive = false;
  
} // end-constructor

public boolean isAlive()    { return alive; }
public boolean isInUse()    { return inUse; }
public String getAlt()      { return Alt;   }
public String getDBMS()     { return DBMS;  }
public String getDir()      { return Dir;   }
public String getFileName() { return FileName; }
public String getFuncName() { return FuncName; }
  
public void setAlive(boolean alive)       { this.alive = alive; }
public void setInUse(boolean inUse)       { this.inUse = inUse; }
public void setAlt(String alt)            { Alt = alt;  }
public void setDBMS(String dbms)          { DBMS = dbms; }
public void setDir(String dir)            { Dir = dir; }
public void setFileName(String fileName)  { FileName = fileName; }
public void setFuncName(String funcName)  { FuncName = funcName; }

} // end-class
