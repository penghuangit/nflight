package com.tymeac.client.jframe;

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
 * Internal storage for the function fields. 
 *
 */

public final class FuncData {

	// fields
	String func_name 	= null;
	String oa_name  	= null;
	String[] F_list 	= null;	

/**
 * FuncData constructor comment.
 */
public FuncData() {

} // end-constructor
/**
 * Constructor
 * @param P_func_name String - function name
 * @param P_oa_name String - output agent name
 * @param P_list String[] - list of queues
 */
public FuncData(String P_func_name, String P_oa_name, String[] P_list) {

	func_name = P_func_name;
	oa_name   = P_oa_name;
	F_list    = P_list;

} // end-constructor
/**
 * Accesssor for queue list
 * @return java.lang.String[]
 */
public java.lang.String[] getF_list() {

	return F_list;

} // end-method
/**
 * Accessor for function name
 * @return java.lang.String
 */

public java.lang.String getFunc_name() {

	return func_name;

} // end-method
/**
 * Accessor for output agent name
 * @return java.lang.String
 */

public java.lang.String getOa_name() {

	return oa_name;

} // end-method
/**
 * Mutator for list of queues
 * @param newS_list java.lang.String[]
 */

public void setF_list(java.lang.String[] newF_list) {

	F_list = newF_list;

} // end-method
/**
 * Mutator for function name
 * @param newS_func_name java.lang.String
 */

public void setFunc_name(java.lang.String newFunc_name) {

	func_name = newFunc_name;

} // end-method
/**
 * mutator for output agent name
 * @param newS_oa_name java.lang.String
 */

public void setOa_name(java.lang.String newOa_name) {

	oa_name = newOa_name;

} // end-method
} // end-class
