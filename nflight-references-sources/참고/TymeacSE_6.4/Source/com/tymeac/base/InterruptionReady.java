package com.tymeac.base;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for notifying a callback client that the syncRequest() interruption 
 *   is available.          
 */ 
public interface InterruptionReady extends Remote {

/**
 * Informs a callback client that the Tymeac Server is ready for a 
 *   cancelSyncReq()
 * @parm forCancel com.tymeac.base.CancelParm - two long integers 
 * @throws  RemoteException if a remote communication failure occurs
 */     
void ready(CancelParm forCancel) 
        throws RemoteException;

} // end-interface
