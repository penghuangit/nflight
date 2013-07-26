package com.tymeac.demo.jini2.base;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.rmi.*;

/**
 * Tymeac Jini Demonstration, document service.
 *
 *  This is the document system Interface.
 * 
 */ 
public interface TyDemoJiniDocInterface
		  extends Remote {
	 
/**
 * You must implement this method
 * 
 * @param document String[] - this is the document, (1st position is the name of the document)
 * @param waiting boolean - true  wait for a reply, false, start and get out
 * @return String
 * @exception java.rmi.RemoteException The exception description.
 * 
 */ 
public String putDoc(String[] document, boolean waiting)
        throws RemoteException;
        
} // end-interface