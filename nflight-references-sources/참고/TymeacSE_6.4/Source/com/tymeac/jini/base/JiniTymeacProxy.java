package com.tymeac.jini.base;

/*
 * Created on Feb 4, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import java.io.Serializable;
import java.rmi.RemoteException;

import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.security.TrustVerifier;
import net.jini.security.proxytrust.ProxyTrustIterator;
import net.jini.security.proxytrust.SingletonProxyTrustIterator;
import net.jini.security.proxytrust.TrustEquivalence;
import net.jini.export.ProxyAccessor;
import net.jini.security.proxytrust.ServerProxyTrust;

import com.tymeac.base.*;

/**
 * smart proxy for the server
 *
 */
public class JiniTymeacProxy 
        implements Serializable,
                   TymeacInterface,
                   ServerProxyTrust, 
                   ProxyAccessor {

	static final long serialVersionUID = 8861090798194535075L;
				
  // server proxy
  final TymeacInterface serverProxy;        
    
    /**
     * constrainable implementation of the smart proxy
     *
     */
    private static final class ConstrainableProxy extends JiniTymeacProxy
            implements RemoteMethodControl {
                  
      /**
			 * 
			 */
			private static final long serialVersionUID = -6091000009734625525L;

				// constructor
        ConstrainableProxy(TymeacInterface p_serverProxy) {
        
          // call up
          super(p_serverProxy);
                        
        } // end-constructor

        // *--- Implementation of RemoteMethodControl ---*
        
        /**
         * Getter
         */
        public MethodConstraints getConstraints() {
        
            // constrainable              
            return ((RemoteMethodControl) serverProxy).getConstraints();
  
        } // end-method
  
        /**
         * setter
         */
        public RemoteMethodControl setConstraints(MethodConstraints mc) {
        
            return new ConstrainableProxy( (TymeacInterface)
                 ((RemoteMethodControl) serverProxy).setConstraints(mc));
  
        } // end-method
     
        /**
        * Provide access to the underlying server proxy to permit the
        * ProxyTrustVerifier class to verify the proxy.
        */
        @SuppressWarnings("unused")
        private ProxyTrustIterator getProxyTrustIterator() {
        
            // new iterator
            return new SingletonProxyTrustIterator(serverProxy);
            
        } // end-method
    } // end-class ConstrainableProxy

    /**
     * trust verifier for secure smart proxies
     *
     */
    final static class Verifier 
            implements TrustVerifier,
                       Serializable {
                       
      /**
			 * 
			 */
			private static final long serialVersionUID = 4445428539045180427L;
      
			  // remote method control of server object
        private final RemoteMethodControl v_serverProxy;
    
        /**
        * Create the verifier, throwing UnsupportedOperationException if the
        * server proxy does not implement both RemoteMethodControl and
        * TrustEquivalence.
        */
        Verifier(TymeacInterface p_serverProxy) {
            
            if ((p_serverProxy instanceof RemoteMethodControl) &&
                (p_serverProxy instanceof TrustEquivalence)) {
      
                v_serverProxy = (RemoteMethodControl) p_serverProxy;
            }
            else {
                throw new UnsupportedOperationException();
                
            } // end-if
        } // end-method

        // *--- Implement TrustVerifier ---*
  
        /**
         * check Trust Equivalence
         */
        public boolean isTrustedObject(Object obj, TrustVerifier.Context ctx)
                throws RemoteException {
  
            // When nothing here
            if (obj == null || ctx == null) {
            
                // cannot continue
                throw new NullPointerException();
                
            } // endif
            
            // When NOT constrainable
            if (!(obj instanceof ConstrainableProxy)) {
                
                // not constrainable
                return false;

            } // endif 
            
            // cast TymeacProxy field to remote method control
            RemoteMethodControl otherServerProxy = (RemoteMethodControl)
                ((ConstrainableProxy) obj).serverProxy;
            
            // get the contraints    
            MethodConstraints mc = otherServerProxy.getConstraints();
            
            // get the trust equivalence
            TrustEquivalence trusted = (TrustEquivalence)
                v_serverProxy.setConstraints(mc);
                
            // true when good    
            return trusted.checkTrustEquivalence(otherServerProxy);
            
        } // end-method
    } // end-class Verifier
 
/**
 * Main class constructor
 */
JiniTymeacProxy(TymeacInterface p_serverProxy) {
    
    // instance field
    serverProxy = p_serverProxy;
          
} // end-constructor  

/**
 * Create a smart proxy, using an implementation that supports constraints
 * if the server proxy does.
 */       
public static JiniTymeacProxy create(TymeacInterface p_serverProxy) {
    
    // When constaints used
    if  (p_serverProxy instanceof RemoteMethodControl) {
        
        // containable proxy
        return new ConstrainableProxy(p_serverProxy);
    }
    else {
        // normal proxy              
        return new JiniTymeacProxy(p_serverProxy);
          
    } // endif
                    
} // end-method 

// Override 
public boolean equals(Object o) {
        
    // When passed is same as this
    if  ((getClass() == o.getClass()) &&
         (serverProxy.equals(((JiniTymeacProxy) o).serverProxy))) {
            
        // equal
        return true;
    }
    else {
        // not equal  
        return false;      
             
        } // endif
        
} // end-method    

// Override
public int hashCode() {
    
    // new hash
    return serverProxy.hashCode();
        
} // end -method

/**
 * Implement the ServerProxyTrust interface to provide a verifier for
 * secure smart proxies.
 */
public TrustVerifier getProxyVerifier() {

  return new JiniTymeacProxy.Verifier(serverProxy);
  
} // end-method
    
/**
 * Returns a proxy object for this remote object.
 *
 * @return the proxy
 */
public Object getProxy() {

    return serverProxy;

} // end-method    

// *--- Implement TymeacInterface ---*

/**
 * Alter server options
 * @param opts
 * @return
 */
public TyAltSvrElements alterSvrOptions( TyAltSvrElements opts) 
        throws RemoteException {
  
  // off to real impl
  return serverProxy.alterSvrOptions(opts);
  
} // end-method
    
/**
 * Async reqeust
 * @return Object[]
 * @param Req TymeacParm
 * @exception java.rmi.RemoteException The exception description.
 */ 
public Object[] asyncRequest (TymeacParm Req )
           throws RemoteException {

    // off to Server
    return serverProxy.asyncRequest(Req);
    
} // end-method

/**
 * 
 * @return int
 * @param forCancel com.tymeac.base.CancelParm
 * @exception java.rmi.RemoteException The exception description.
 */
public int cancelSyncReq(CancelParm forCancel) 
        throws java.rmi.RemoteException {

  // off to Server
  return serverProxy.cancelSyncReq(forCancel);                          
  
} // end - method 

/**
 * internal debugging
 * @param req TyParm
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String  diagnoseRequest (TyParm req) 
        throws RemoteException {
    

    // off to Server
    return serverProxy.diagnoseRequest(req);
  
} // end-test1Request

/**
 * Function Display GUI
 * @param func String - partial name
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String[] func1Request (String func) 
                      throws RemoteException {

    // off to Server
    return serverProxy.func1Request(func);
      
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
    

    // off to Server
    return serverProxy.idStatus1Request(id, req, action);

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
    
    // off to Server
    return serverProxy.newCopyRequest(que, pa_class);
  
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
  return serverProxy.newRunTimeFunctions(type, one, two);
   
} // end-method 

/**
 * New run time functions maintenance
 * @param type of elements needed
 * @return elements
 */
public TyRunTimeElements newRTMaint(int type) 
               throws RemoteException {
  
  // off to real impl
  return serverProxy.newRTMaint(type);
  
} // end-method

/**
 * Overall GUI
 * @return TyOverallObj
 * @param req_type int
 */
public TyOverallObj overallRequest(int req_type) 
              throws RemoteException {

    // off to Server
    return serverProxy.overallRequest(req_type);

} // end-method

/**
 * Que Data GUI
 * @param que String - queue name
 * @return TyQueElements
 * @exception java.rmi.RemoteException The exception description.
 */ 
public TyQueElements que1Request (String que) 
     throws RemoteException {    
    
    // off to Server
    return serverProxy.que1Request(que);      
  
} // end-method

/**
 * Que Data GUI
 * @param ele TyQueElements - data for update
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int que2Request (TyQueElements ele) 
     throws RemoteException {
   
    // off to Server
    return serverProxy.que2Request(ele);
  
} // end-method

/**
 * Que maint GUI
 * @param que String - queue name
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */  
public String[] que3Request (String que) 
                      throws RemoteException {    

    // off to Server
    return serverProxy.que3Request(que);
  
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

    // off to Server
    return serverProxy.que4Request(que, nbr);      
      
} // end-method

/**
 * Que Maint
 * @param que String - queue name
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */  
public int que5Request (String que) 
     throws RemoteException {    
  
    // off to Server
    return serverProxy.que5Request(que);
  
} // end-method

/**
 * Wait list GUI
 * @param que String - queue name
 * @return TyWLElements[]
 * @exception java.rmi.RemoteException The exception description.
 */  
public TyWLElements[] que6Request (String que) 
     throws RemoteException {    

    // off to Server
    return serverProxy.que6Request(que);
  
} // end-method

/**
 * Shut down Request.
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String shutRequest () 
                  throws RemoteException {  

    // off to Server
    return serverProxy.shutRequest(false);
  
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
    return serverProxy.shutRequest(force);
    
} // end-method   

/**
 * Refresh button on stall GUI 
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */  
public String[] stall1Request () 
     throws RemoteException {        

    // off to Server
    return serverProxy.stall1Request();
  
} // end-method

/**
 * Purge button on stall GUI
 * @param req long - async request id
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */
public int stall2Request (long req) 
     throws RemoteException {    
    
    // off to Server
    return serverProxy.stall2Request(req);
  
} // end-method

/**
 * Detail button on stall GUI
 * @param req long - async request
 * @return String[]
 * @exception java.rmi.RemoteException The exception description.
 */ 
public String[] stall3Request (long req) 
     throws RemoteException {    
    
    // off to Server
    return serverProxy.stall3Request(req);
    
} // end-method

/**
 * Re-schd button on stall GUI
 * @param req long - async id
 * @return  int
 * @exception java.rmi.RemoteException The exception description.
 */ 
public int stall4Request (long req) 
     throws RemoteException {    

    // off to Server
    return serverProxy.stall4Request(req);
    
} // end-method

/**
 * On Request statistics.
 * @return int
 * @exception java.rmi.RemoteException The exception description.
 */ 
public int stats1Request () 
                  throws RemoteException {  
    
    // off to Server
    return serverProxy.stats1Request();      
        
} // end-method

/**
 * Synchronous Request -- Waiting for a reply.
 * @param Req TymeacParm - input to tymeac
 * @return Object
 * @exception java.rmi.RemoteException The exception description.
 */ 
public Object[] syncRequest (TymeacParm Req) 
                      throws RemoteException {       

    // off to Server
    return serverProxy.syncRequest(Req);
  
} // end-method

} // end-class TymeacProxy
