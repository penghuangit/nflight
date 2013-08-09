package com.tymeac.demo;

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
/**
 * Tymeac Demonstration System. 
 *   Interface for call back demonstration.
 */

public interface TyDemoCallback
       extends Remote {

/**
 * This method is the call back from the Tymeac Server Processing Application Class. 
 *
 * @param back java.lang.Object
 * @exception java.rmi.RemoteException The exception description.
 */

void giveBack(Object back) 
      throws java.rmi.RemoteException;
} // end-interface
