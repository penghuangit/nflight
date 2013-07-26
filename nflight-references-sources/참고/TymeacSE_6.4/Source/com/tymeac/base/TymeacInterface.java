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

import java.rmi.*;
/**
 * The interface for the Tymeac Server 
 */
public interface TymeacInterface
    extends Remote {  
  
/**
 * Alter server options
 * @param opts
 * @return
 */
public TyAltSvrElements alterSvrOptions( TyAltSvrElements opts) 
        throws RemoteException;

/**
 * Asynchronous request
 * @param in TymeacParm - input to the server
 * @return Object[]
 */
public Object[] asyncRequest(TymeacParm in) 
          throws RemoteException;
/**
 * Cancel a synchronous request
 * @return int
 * @param forCancel com.tymeac.base.CancelParm
 * @exception java.rmi.RemoteException The exception description.
 */
public int cancelSyncReq(CancelParm forCancel) 
    throws java.rmi.RemoteException;
/**
 * Internal display routines for debugging
 * @param in TyParm - integer switches
 * @return String
 */
public String diagnoseRequest(TyParm in)
           throws RemoteException;
/**
 * Search for generic function names
 * @param func String - partial function name
 * @return String[]
 */
public String[] func1Request(String func)
           throws RemoteException;
/**
 * Status of an async request
 * @param id long - tymeac startup time
 * @param req long - async request
 * @param type int - action (get or cancel)
 * @return int
 */
public int idStatus1Request(long id, long req, int action)
           throws RemoteException;
/**
 * Get a new copy of a Proc Appl Class
 * @param que String - queue name
 * @param pa_class - pap name
 * @return int
 */
public int newCopyRequest(String que, String pa_class)
           throws RemoteException;

/**
 * Reset or set a New Runtime Function (notify, log or stats)
 * @param type of request
 * @param one String when DBMS or Alt
 * @param two String when file or directory
 * @return what happened
 */
public int newRunTimeFunctions(int type, String one, String two) 
          throws RemoteException;

/**
 * Maintenance of New Runtime Functions (notify, log or stats)
 * @param type of request (notify, log or stats)
 * @return TyRunTimeElements fields
 */
public TyRunTimeElements newRTMaint(int type) 
          throws RemoteException;
/**
 * get the overall picture of processing
 * @param req_type int
 * @return TyOverallObj
 */
public TyOverallObj overallRequest(int req_type)
        throws RemoteException;
/**
 * Que Data GUI
 * @param que String - queue name
 * @return TyQueElements
 */
public TyQueElements que1Request(String que)
           throws RemoteException;
/**
 * Que Data update
 * @param ele TyQueElements - data for update
 * @return int
 */
public int que2Request(TyQueElements ele)
           throws RemoteException;
/**
 * Que Maint GUI
 * @param que String - queue name
 * @return String[]
 */
public String[] que3Request(String que)
           throws RemoteException;
/**
 * Que maint GUI
 * @param que String - queue name
 * @param nbr int - thread number
 * @return int
 */
public int que4Request(String que, int nbr)
           throws RemoteException;
/**
 * Que Maint GUI
 * @param que String - queue name
 * @return int
 */
public int que5Request(String que)
           throws RemoteException;
/**
 * Wait list display
 * @param que String - queue name
 * @return TyWLElements[]
 */
public TyWLElements[] que6Request(String que)
           throws RemoteException;
/**
 * Shutdown request 
 * @return String
 */
public String shutRequest()
           throws RemoteException;
/**
 * Shutdown request with force indicator
 * @return String
 */
public String shutRequest(boolean force)
           throws RemoteException;           
/**
 * Stall GUI 
 * @return String[]
 */
public String[] stall1Request()
           throws RemoteException;
/**
 * Stall GUI
 * @param req long - async id
 * @return int
 */
public int stall2Request(long req)
           throws RemoteException;
/**
 * Stall GUI
 * @param req long - async id
 * @return String[]
 */
public String[] stall3Request(long req)
           throws RemoteException;
/**
 * Stall GUI
 * @param req long - async id
 * @return int
 */
public int stall4Request(long req)
           throws RemoteException;
/**
 * Print statistics 
 * @return int
 */
public int stats1Request()
           throws RemoteException;
/**
 * Synchronous request
 * @param in TymeacParm - main parm to tymeac
 * @return Object[]
 */
public Object[] syncRequest(TymeacParm in)
           throws RemoteException;
} // end-interface
