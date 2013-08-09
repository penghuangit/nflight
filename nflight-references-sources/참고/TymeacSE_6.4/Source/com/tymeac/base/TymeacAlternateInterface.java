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
 * This is the Interface between a Client communication Class and
 *   an alternate means for getting the remote object, TymeacInterface.
 *
 * You must use the "implements TymeacAlternateInterface" clause in your
 *   Class definition. 
 *
 */ 

public interface TymeacAlternateInterface {
     
/**
 * This is the only method.  You must implement this method in
 *   your alternate.class 
 * 
 */
 
public TymeacInterface getTymeacInterface();
} // end-interface          
