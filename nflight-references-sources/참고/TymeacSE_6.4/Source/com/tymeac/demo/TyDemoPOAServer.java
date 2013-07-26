package com.tymeac.demo;

/*
 * Created on Mar 30, 2004
 * 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */
import com.tymeac.base.*;
import com.tymeac.common.*;

import javax.naming.InitialContext;
import javax.naming.Context;
import org.omg.PortableServer.*;
import java.util.*;
import org.omg.CORBA.*;
import javax.rmi.CORBA.Util;

/**
 * This is the non-activatable rmi server using 
 *   the Portable Object Adapter version of IIOP
 *
 * You must write your own export method
 * 
 */
public class TyDemoPOAServer extends POAServer {

/**
 * Constructor
 */
public TyDemoPOAServer() {
  super();
  
} // end-constructor

/**
 *  Create the impl Object and Rebind with the ORB
 * 
 */   
public void doExport() {
  
  // variables
  TyVariables TyV = new TyVariables();
  String iiop_name = TyV.getIIOPName();
  
  // When illogical data
  if  ((!TyV.useIIOP())  ||
       (iiop_name == null) || 
       (iiop_name.length() < 1)) {           

      // do so
      msgOut(TyMsg.getMsg(61), 10);
              
      // get out      
      return; 
      
  } // endif
  
  // rebinding message     
  // wait up to 10 sec for completion 
  msgOut(TyMsg.getMsg(71) + iiop_name, 10);
  
  // *--- remote object extends PortableRemoteObject ---*
  TymeacPOAImpl tyPOA = null;
  
  // properties
  Properties p = System.getProperties();
  
  // null args
  String[] args = new String[0];
  
  // * --- this is your code ---* 
  try {
    // initialize orb 
    orb = ORB.init( args, p );

    // Root
    POA rootPOA = (POA)orb.resolve_initial_references("RootPOA");

    // Create a POA with the appropriate policies
    Policy[] tpolicy = new Policy[3];
    
      tpolicy[0] = rootPOA.create_lifespan_policy(
        LifespanPolicyValue.TRANSIENT );
        
      tpolicy[1] = rootPOA.create_request_processing_policy(
        RequestProcessingPolicyValue.USE_ACTIVE_OBJECT_MAP_ONLY );
        
      tpolicy[2] = rootPOA.create_servant_retention_policy(
        ServantRetentionPolicyValue.RETAIN);
    
    // save  
    POA tPOA = rootPOA.create_POA("TransientPOA", 
                                  null,
                                  tpolicy);
        
    // Activate the POA Manager
    tPOA.the_POAManager().activate();

    // Instantiate the Servant     
    // *--- do not change this statement ---*
    tyPOA = new TymeacPOAImpl(T);
    
    // Activate the Tie, If the POA policy is USE_ACTIVE_OBJECT_MAP_ONLY
    _TymeacPOAImpl_Tie tie = (_TymeacPOAImpl_Tie)Util.getTie( tyPOA );
    
    // iiop name as a byte array
    byte[] id = iiop_name.getBytes();
    tPOA.activate_object_with_id( id, tie );
    
    // Initial context
    Context initialNamingContext = new InitialContext();    
    
    // Rebind to internal naming server
    initialNamingContext.rebind
    (
      iiop_name, 
      tPOA.create_reference_with_id(id, 
                                    tie._all_interfaces(tPOA,id)[0]) 
    );
                  
  } // end-try

  catch (Exception e) {
                               
    // wait up to 10 sec for completion 
    msgOut(TyMsg.getMsg(9) + TyMsg.getText(68), 10);
    msgOut(TyMsg.getText(2) + e, 10);
            
    // get out      
    return;   
   
  } // end-catch 
  
  // *--- do not change the following three statements ---*
  
  // set global remote object in base storage
  T.setTi(tyPOA); 
  
  // save impl class
  setImpl(tyPOA);

  // good bind
  setGood();
          
} // end-method
} // end-class
