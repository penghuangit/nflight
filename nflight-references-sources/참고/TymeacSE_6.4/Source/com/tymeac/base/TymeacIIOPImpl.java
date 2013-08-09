package com.tymeac.base;

/*
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php 
 * 
 */ 

import javax.rmi.PortableRemoteObject;
import java.rmi.*;

/**
 * This class is identical to com.tymeac.base.TymeacStandardImpl except
 *   that this class extends PortableRemoteObject. The extends is necessary
 *   to use the JNDI naming (must be an instance of org.obm.CORBA.Object).
 * 
 * To use another naming method (Jini, ORB connect, etc.) you may export the
 *   base class, com.tymeac.base.TymeacStandardImpl. 
 *
 */ 
public final class TymeacIIOPImpl
        extends PortableRemoteObject
        implements TymeacInterface {
    
    // real impl class
    private final TymeacImpl impl; 

/**
 * Constructor 
 * @param TyBase T 
 * @exception java.rmi.RemoteException The exception description.
 */ 
public TymeacIIOPImpl (TyBase T)
 throws RemoteException {

  // real impl
  impl = T.getImpl();              

} // end-constructor

/**
 * *--- Implementation of TymeacInterface ---*
 */

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
 * @return Object
 * @param Req TymeacMOParm
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
 * 
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */
 
public String  diagnoseRequest (TyParm req) 
        throws RemoteException {
    

    // off to real impl
    return impl.diagnoseRequest(req);
  
} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
 
public String[] func1Request (String func) 
                      throws RemoteException {


    // off to real impl
    return impl.func1Request(func);
      
} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
  
public int idStatus1Request (long id, long req, int action) 
     throws RemoteException {
    
    // off to real impl
    return impl.idStatus1Request(id, req, action);

} // end-method
/**
 * 
 * 
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
public int newRunTimeFunctions(int type, String one, String two) {
  
  // off to real impl
  return impl.newRunTimeFunctions(type, one, two);
   
} // end-method 

/**
 * New run time functions maintenance
 * @param type of elements needed
 * @return elements
 */
public TyRunTimeElements newRTMaint(int type) {
  
  // off to real impl
  return impl.newRTMaint(type);
  
} // end-method

/**
 * This method was generated.
 * @return TyOverallObj
 * @param tymeac java.lang.String
 * @param url java.lang.String
 * @param req_type int
 */
public TyOverallObj overallRequest(int req_type) 
              throws RemoteException {


    // off to real impl
    return impl.overallRequest(req_type);

} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
 
public TyQueElements que1Request (String que) 
     throws RemoteException {
    
    
    // off to real impl
    return impl.que1Request(que);
      
  
} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
  
public int que2Request (TyQueElements ele) 
     throws RemoteException {
   

    // off to real impl
    return impl.que2Request(ele);
  
} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
  
public String[] que3Request (String que) 
                      throws RemoteException {
    

    // off to real impl
    return impl.que3Request(que);
  
} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
  
public int que4Request (String que, int nbr ) 
     throws RemoteException {

    // off to real impl
    return impl.que4Request(que, nbr);
      
      
} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
  
public int que5Request (String que) 
     throws RemoteException {
    
  
    // off to real impl
    return impl.que5Request(que);
  
} // end-method
/**
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
  
public TyWLElements[] que6Request (String que) 
     throws RemoteException {
    

    // off to real impl
    return impl.que6Request(que);
  
} // end-method
/**
 *
 * Shut down Request.
 * 
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */
 
public String shutRequest () 
                  throws RemoteException {
  

    // off to real impl
    return impl.shutRequest(false);
  
} // end-method
/**
 *
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
 * Refresh button 
 * 
 *  
 * @exception java.rmi.RemoteException The exception description.
 */
  
public String[] stall1Request () 
     throws RemoteException {
        

    // off to real impl
    return impl.stall1Request();
  
} // end-method
/**
 * Purge button
 * 
 * 
 * @exception java.rmi.RemoteException The exception description.
 */

public int stall2Request (long req) 
     throws RemoteException {
    
    
    // off to real impl
    return impl.stall2Request(req);
  
} // end-method
/**
 * Detail button
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
 
public String[] stall3Request (long req) 
     throws RemoteException {
    
    
    // off to real impl
    return impl.stall3Request(req);
    
} // end-method
/**
 * Re-schd button
 * 
 * @exception java.rmi.RemoteException The exception description.
 */
 
public int stall4Request (long req) 
     throws RemoteException {
    

    // off to real impl
    return impl.stall4Request(req);
    
} // end-method
/**
 *
 * On Request statistics.
 *  
 * @exception java.rmi.RemoteException The exception description.
 */
 
public int stats1Request () 
                  throws RemoteException {
  
    
    // off to real impl
    return impl.stats1Request();
      
        
} // end-method
/**
 *
 * Synchronous Request -- Waiting for a reply.
 *
 * 
 * @return Object
 * @exception java.rmi.RemoteException The exception description.
 */
 
public Object[] syncRequest (TymeacParm Req) 
                      throws RemoteException { 

    // off to real impl
    return impl.syncRequest(Req);
  
  
} // end-method
} // end-class
