package com.tymeac.jini.base;
/*
 * Created on Feb 7, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
 
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Used to shut down the Jini service lease renewal task
 * 
 */
public interface JiniFrontEndShutDown 
          extends Remote {

  // shut down the service
  public void shutDown ()
      throws RemoteException; 

} // end-interface
