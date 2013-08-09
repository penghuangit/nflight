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
 * RMI implementation of Tymeac interface 
 */ 
public class TymeacStandardImpl
    implements TymeacInterface { 
  
  // real implementation of interface
  protected TymeacImpl impl; 

/**
 * Constructor
 * @param TyBase T 
 */
public TymeacStandardImpl (TyBase T) {
  
  // set the real impl class
  impl = T.getImpl();
  
} // end-constructor

/**
 * Alter server options
 * @param opts
 * @return
 */
public TyAltSvrElements alterSvrOptions( TyAltSvrElements opts) 
        throws RemoteException {
  
  // off to real impl
  return impl.alterSvrOptions(opts);
  
} // end-method

/**
 * Async reqeust
 * @return Object[]
 * @param Req TymeacParm
 * @exception java.rmi.RemoteException The exception description.
 */ 
public Object[] asyncRequest (TymeacParm Req )
           throws RemoteException {

  // off to real impl
  return impl.asyncRequest(Req);
    
} // end-method

/**
 * 
 * @return int
 * @param forCancel com.tymeac.base.CancelParm
 * @exception java.rmi.RemoteException The exception description.
 */
public int cancelSyncReq(CancelParm forCancel) 
        throws java.rmi.RemoteException {

  // off to real impl
  return impl.cancelSyncReq(forCancel);                          
  
} // end-method 

/**
 * internal debugging
 * @param req TyParm
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String  diagnoseRequest (TyParm req) 
        throws RemoteException {    

  // off to real impl
  return impl.diagnoseRequest(req);
  
} // end-method 

/**
 * Function Display GUI
 * @param func String - partial name
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String[] func1Request (String func) 
                      throws RemoteException {

  // off to real impl
  return impl.func1Request(func);
      
} // end-method

/**
 * Status of an async request
 * @param id long - startup time
 * @param req long - async request
 * @param action int - action to take
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int idStatus1Request (long id, long req, int action) 
     throws RemoteException {    

  // off to real impl
  return impl.idStatus1Request(id, req, action);

} // end-method

/**
 * New Copy of a PAC
 * @param que String - queue name
 * @param pa_class String PAC name
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int newCopyRequest (String que, String pa_class) 
     throws RemoteException {
    
  // off to real impl
  return impl.newCopyRequest(que, pa_class);
  
} // end-method

/**
 * Reset or set a New Runtime Function (notify, log or stats)
 * @param type of request
 * @param one String when DBMS or Alt
 * @param two String when file or directory
 * @return what happened
 */
public int newRunTimeFunctions(int type, String one, String two) 
            throws RemoteException {
  
  // off to real impl
  return impl.newRunTimeFunctions(type, one, two);
   
} // end-method 

/**
 * New run time functions maintenance
 * @param type of elements needed
 * @return elements
 */
public TyRunTimeElements newRTMaint(int type) 
              throws RemoteException {
  
  // off to real impl
  return impl.newRTMaint(type);
  
} // end-method

/**
 * Overall GUI
 * @return TyOverallObj
 * @param req_type int
 */
public TyOverallObj overallRequest(int req_type) 
              throws RemoteException {

  // off to real impl
  return impl.overallRequest(req_type);

} // end-method

/**
 * Que Data GUI
 * @param que String - queue name
 * @return TyQueElements
 * @exception java.rmi.RemoteException The exception description.
 */ 
public TyQueElements que1Request (String que) 
     throws RemoteException {    
    
  // off to real impl
  return impl.que1Request(que);      
  
} // end-method

/**
 * Que Data GUI
 * @param ele TyQueElements - data for update
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int que2Request (TyQueElements ele) 
     throws RemoteException {
   
  // off to real impl
  return impl.que2Request(ele);
  
} // end-method

/**
 * Que maint GUI
 * @param que String - queue name
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */  
public String[] que3Request (String que) 
                      throws RemoteException {    

  // off to real impl
  return impl.que3Request(que);
  
} // end-method

/**
 * Que Maint GUI
 * @param que String - queue name
 * @param nbr int - thread number
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int que4Request (String que, int nbr ) 
     throws RemoteException {

  // off to real impl
  return impl.que4Request(que, nbr);      
      
} // end-method

/**
 * Que Maint
 * @param que String - queue name
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int que5Request (String que) 
     throws RemoteException {    
  
  // off to real impl
  return impl.que5Request(que);
  
} // end-method

/**
 * Wait list GUI
 * @param que String - queue name
 * @return TyWLElements[]
 * @exception java.rmi.RemoteException The exception description.
 */  
public TyWLElements[] que6Request (String que) 
     throws RemoteException {    

  // off to real impl
  return impl.que6Request(que);
  
} // end-method

/**
 * Shut down Request.
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String shutRequest () 
                  throws RemoteException {  

  // off to real impl
  return impl.shutRequest(false);
  
} // end-method

/**
 * Shut down Request with force indicator.
 *
 * @param force shutdown
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String shutRequest (boolean force) 
                  throws RemoteException {
  
  // off to real impl
  return impl.shutRequest(force);
    
} // end-method 

/**
 * Refresh button on stall GUI 
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */  
public String[] stall1Request () 
     throws RemoteException {        

  // off to real impl
    return impl.stall1Request();
  
} // end-method

/**
 * Purge button on stall GUI
 * @param req long - async request id
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */
public int stall2Request (long req) 
     throws RemoteException {    
    
  // off to real impl
  return impl.stall2Request(req);
  
} // end-method

/**
 * Detail button on stall GUI
 * @param req long - async request
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String[] stall3Request (long req) 
     throws RemoteException {    
    
  // off to real impl
  return impl.stall3Request(req);
    
} // end-method

/**
 * Re-schd button on stall GUI
 * @param req long - async id
 * @return  int
 * @exception java.rmi.RemoteException The exception description.
 */ 
public int stall4Request (long req) 
     throws RemoteException {    

  // off to real impl
  return impl.stall4Request(req);
    
} // end-method

/**
 * On Request statistics.
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */ 
public int stats1Request () 
                  throws RemoteException {  
    
  // off to real impl
  return impl.stats1Request();      
        
} // end-method

/**
 * Synchronous Request -- Waiting for a reply.
 * @param Req TymeacParm - input to tymeac
 * @return Object
 * @exception java.rmi.RemoteException The exception description.
 */ 
public Object[] syncRequest (TymeacParm Req) 
                      throws RemoteException {       

  // off to real impl
  return impl.syncRequest(Req);
  
} // end-method
} // end-class
