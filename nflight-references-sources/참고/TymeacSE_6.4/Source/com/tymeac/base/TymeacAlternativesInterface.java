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
 * This is the Interface for the Statistics and Log methods
 *
 */ 

public interface TymeacAlternativesInterface {
     
/**
 * Verify that the class is valid
 * @return boolean
 */

public boolean verify();
/**
 * Write a message
 * @param msg String - the message
 * @return boolean
 */

public boolean write(String msg);
} // end-interface          
