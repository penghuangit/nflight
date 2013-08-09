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
import java.rmi.activation.*;
import com.tymeac.base.*;
/**
 * Tymeac Demonstration System. 
 *
 *  This Class is an example of how to inherit the Tymeac
 *    Activation Implementation of the TymeacInterface interface for a custom socket factory.
 *
 *  When developing an activation description activation.ActivationDesc()
 *    the name of the Class to be activated is needed.  This Class is
 *    TymeacActivationCSFactoryImpl.  However, you may wish to have several of these
 *    remote objects in any one system.  If there is only one name, then there
 *    can only be one occurance.
 *
 *  Therefore, by extending the main Class, TymeacActivationCSFactoryImpl, and overriding
 *    its constructor, you may have as many names as you wish for activatable
 *    remote objects.
 *
 *  This Class is an example of how you may do this.
 *
 *
 */
 
public class TyDemoActivationCSFactory
    extends TymeacActivationImpl {   
  
/**
   * 
   */
  private static final long serialVersionUID = 3280633756127808334L;

/**
 * Constructor
 *  You must duplicate this exactly.
 * @param id ActivationID - used for shut down
 * @param data MarshalledObject - data for application 
 */ 
 
public TyDemoActivationCSFactory (ActivationID id,
                                  MarshalledObject data)
     throws  RemoteException, 
            Throwable {

  // the parameters for TymeacActivationCSFactoryImpl
  super(id, 
        new TyDemoClientSocketFactory(),
        new TyDemoServerSocketFactory(), 
        data);  

} // end-method
} // end-class  
