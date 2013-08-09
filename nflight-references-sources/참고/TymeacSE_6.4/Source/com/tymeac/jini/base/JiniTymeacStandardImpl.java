package com.tymeac.jini.base;


/*
 * Created on Jan 30, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import java.rmi.*;
import net.jini.security.*;
import net.jini.security.proxytrust.*;
import net.jini.export.ProxyAccessor;
import com.tymeac.base.*;

/**
 * Jini implementation of Tymeac interface
 */
public class JiniTymeacStandardImpl 
          extends TymeacStandardImpl 
          implements ServerProxyTrust,
                      ProxyAccessor {
            
  // base storage
  private TyBase Ty = null;            

public JiniTymeacStandardImpl(TyBase T) {
  
  // up to base
  super(T);
  
  // save base
  Ty = T;
      
} // end-constructor

/**
 * Implement the ServerProxyTrust interface to provide a verifier for
 * secure smart proxies.
 */
public TrustVerifier getProxyVerifier()
                               throws RemoteException {
                                 
  return new JiniTymeacProxy.Verifier(Ty.getTi()); 
                                 
} // end-method 

/**
  * Returns a proxy object for this remote object.
  * @return proxy
  */
public Object getProxy() { return Ty.getTi(); } // end-method                                       
} // end-class
