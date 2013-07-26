package com.tymeac.base;

/* 
 * Copyright (c) 2006 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.rmi.Remote;

/**
* The login interface for the Tymeac Server. This remote reference
*   is bound to the registry. Clients call this remote method and
*   if correct, the real TymeacInterface is returned.
* 
*/
public interface TymeacLogin 
      extends Remote {
  
/**
 * 
 * @return TymeacInterface
 * @param Object[] for the userid, password, etc.
 * @exception java.rmi.RemoteException The exception description.
 */
public TymeacInterface login(Object[] obj) 
    throws java.rmi.RemoteException;    

} // end-interface
