package com.tymeac.demo.jini2.base;

/* 
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import net.jini.jeri.*;
import net.jini.export.*;
import net.jini.jeri.tcp.TcpServerEndpoint; 
import net.jini.discovery.*;
import net.jini.id.*;
import net.jini.core.lookup.*;
import net.jini.lookup.*;

import com.tymeac.base.*;

/**
 * Tymeac Jini Demonstration, document service
 *
 *  This is the document system Jini Service
 *
 */  
public class TyDemoJiniDocService
                  implements TyDemoJiniDocInterface {

        // server object
        InternalServer server = null;
        
        // server 
        TymeacInterface ti = null;
        
        // proxy
        TyDemoJiniDocProxy proxy = null;
        
        // myself as a remote object
        TyDemoJiniDocInterface remote_obj = null;
  	
/**
 * Constructor: 
 * 
 *  get a new instance of the internal Tymeac Server and create it.
 *
 */   
public TyDemoJiniDocService() {

  // get new internal server
  server = new InternalServer();  

  // passed arg is "only stand alone"     
  String[] in = {"-s"};

  // create the server
  ti = server.createServer(in);        

} // end-constructor

/**
 *  Create the service
 * @param args String[]
 *
 */    
public static void main(String args[]) {
  
  // new me
 TyDemoJiniDocService service = new TyDemoJiniDocService();      

  // not a good create of the tymeac server
  if  (service.ti == null) {
    
      // bye
      System.exit(1);
      
  } // endif
   
  // not a good service
  if  (!service.doExport()) {
    
      // bye
      System.exit(1);
      
  } // endif
   
  // Clean up
  System.gc();

  // wait forever    

  // object for monitor
  Object forever = new Object();

  // lock object
  synchronized (forever) {
      
      try {
        // wait forever
        forever.wait();

      } // end-try

      catch (InterruptedException e) {
      } // end-catch
  } // end-sync 
  
} // end-method 

/**
 * export myself with a Jeri exporter so no _Stub is necessary.
 * @return
 */
public boolean doExport() {
    
  // exporter object
  Exporter exporter = new BasicJeriExporter(
                       TcpServerEndpoint.getInstance(1198),
                       new BasicILFactory(),
                       true,
                       true); 
     
  try {    
    // export the impl class
    remote_obj = (TyDemoJiniDocInterface)exporter.export(this);
  
  } // end-try
  
  catch (Exception e) {
    
    // say what
    System.out.println("Exporting error= " + e.toString());    
        
    // get out      
    return false; 
    
  } // end-catch
  
  // create proxy
  proxy = new TyDemoJiniDocProxy(remote_obj);
    
  // do jini discovery and join stuff
  if  (!doJini()) {
    
      // no good
      return false;
  
  } // endif  
  
  // done
  return true;      
    
} // end-method

/**
 * Jini work: discovery and join managers
 * @return boolean 
 */
private boolean doJini() {
    
  // discovery manager
  DiscoveryManagement discoveryManager = null;
  
  // join manager
  JoinManager joinManager = null;
        
  try {
      // get a new disc mgr for only this service
      discoveryManager = 
        new LookupDiscovery(
            new String[] { "TymeacDocService" });
  
  } // end-try
  
  catch (Exception e) {

      // print msgs
      System.out.println("Discovery Manager error= " + e);
      
      // get out  
      return false;  
                 
  } // end-catch
  
  try {
      // Get the join manager
      joinManager = new JoinManager(
          proxy,               // proxy
          null,             // attribute sets
          createServiceID(),  // new serice id
          discoveryManager,   // the above discovery manager
          null);            // no lease renewal mgr  
   
  } // end-try 
  
  catch (Exception e) {

      // print msgs
      System.out.println("Join Manager error= " + e);
      
      // get out  
      return false;  
                 
  } // end-catch  
   
  // good
  return true;        
  
} // end-method 

/** 
 * Creates a new service ID. 
 */
private ServiceID createServiceID() {
  
  // get base id
  Uuid uuid = UuidFactory.generate();
  
  // new Service ID
  return new ServiceID(
      uuid.getMostSignificantBits(),
      uuid.getLeastSignificantBits());
      
} // end-method

/**
 * Implementation of interface.
 * 
 * @param document String[] - this is the document, (1st position is the name of the document)
 * @param waiting boolean - true  wait for a reply, false, start and get out
 * @return String
 * @exception java.rmi.RemoteException The exception description.
 * 
 */ 
public String putDoc(String[] document, boolean waiting) {
  
  // save the document in permanent storage and form the beginning of a return message
  //   how you save it is installation dependent
  
  // base return message
  String ret_msg = "Document: " + document[0] + " safe-stored ";
  
  // object for tymeac
  Object pass = document[0];    
  
  // Tymeac parm
  TymeacParm TP = new TymeacParm(
                           pass,  // document name
                           "JFunction_1", // function name
                           20,    // wait time
                           1);    // priority
  
  // Tymeac return string
  String ty_msg;
           
  // When waiting for a reply
  if  (waiting) {
  
      // do a sync request   
      ty_msg = doSync(TP);        
      
      // return the concatenated msg
      return (ret_msg + ty_msg);
  }
  else {
     // do an async request 
     ty_msg = doAsync(TP);        
      
      // return the concatenated msg
      return (ret_msg + ty_msg);
      
  } // endif

} // end-method

/**
 * Do an asynchronous request (not waiting for a reply) to Tymeac Server.
 * 
 * @param tp TymeacParm - standard tymeac input
 * @return String  
 */ 
private String doAsync(TymeacParm tp) {

   // Tymeac return Object[]
  Object[] R = null; 
  
  // method return String
  String back = null;

  // may throw an exception
  try {       
    // do an async request 
    R = ti.asyncRequest(tp);
    
  } // end-try    
  
  catch (Exception e) {
  
    // return with an error msg
    return new String("Unable to complete request. Comm failure with: " 
                        + e.getMessage());  
    
  } // end-catch 
  
  // When nothing came back
  if  (R == null) {   
  
    // give back this String
    return new String("Unable to complete request. Comm failure with back-end service");
    
  } // endif
  
  // Should be a string passed
  if  (R[0] instanceof String) {
      
    // cast to a string
    back = (String) R[0];
  }
  else {
    // give back this String
    return new String("Unable to complete request. Comm failure with back-end service");
        
  } // endif      
  
  // length sould be at least the base message: Tymeac AR(nnnn)
  if  (back.length() < 15) {
  
    // give back this String
    return new String("Unable to complete request. Format failure with back-end service: " + back);
        
  } // endif                  
  
  // the return message should be n=0000 from msg: Tymeac AR(nnnn)
  int rc = 0;

  try {
    // atoi
    rc = Integer.parseInt(back.substring(10, 14));
  } // end-try

  catch (NumberFormatException e) {
    
  } // end-catch
   
  //  When the function request was NOT successfull: 
  if  (rc != 0) {
    
    // give back this String
    return new String("Unable to complete request. Backend scheduling failed with rc=" 
                      + rc);
          
  } // endif  
  
  // all good
  return new String("Futher processing scheduled successfully"); 

} // end-method

/**
 * Do a synchronous request (waiting for a reply) to Tymeac Server.
 * 
 * @param tp TymeacParm - standard tymeac input
 * @return String 
 */        

private String doSync(com.tymeac.base.TymeacParm tp) {

  // Tymeac return Object[]
  Object[] R = null; 
             
  // may throw an exception
  try {       
    // do a sync request 
    R = ti.syncRequest(tp);
    
  } // end-try    
  
  catch (Exception e) {
  
    // return with an error msg
    return new String("Unable to complete request. Comm failure with: " 
                      + e.getMessage());  
    
  } // end-catch 
  
  // When nothing came back
  if  (R == null) {   
  
    // give back this String
    return new String("Unable to complete request. Comm failure with back-end service");
    
  } // endif
  
  // working
  String back;
  
  // 1st object is the standared tymeac return value of Tymeac SR(nnnn)  
  
  // Should be a string passed
  if  (R[0] instanceof String) {
      
    // cast to a string
    back = (String) R[0];
  }
  else {
    // give back this String
    return new String("Unable to complete request. Comm failure with back-end service");
        
  } // endif      
  
  // length sould be at least the base message: Tymeac SR(nnnn)
  if  (back.length() < 15) {
  
    // give back this String
    return new String("Unable to complete request. Format failure with back-end service: " + back);
        
  } // endif                                  
  
  // the return message should be n=0000 from msg: Tymeac SR(nnnn)
  int rc = 0;

  try {
    // atoi
    rc = Integer.parseInt(back.substring(10, 14));
  } // end-try

  catch (NumberFormatException e) {
    
  } // end-catch
   
  //  When the function request was NOT successfull: 
  if  (rc != 0) {
    
    // give back this String
    return new String("Unable to complete request. Backend scheduling failed with rc=" 
                        + rc);
          
  } // endif
  
   // find out what came back

  // number of objects minus the first
  int nbr = R.length;

  // get new string for concatenating
  String stuff = "";

  // concatenate the Strings, second to end
  for  (int i = 1; i < nbr; i++) {

      // When good string
      if  (R[i] != null) {

        // each string
        stuff = stuff.concat((String) R[i]);

      } // endif
  } // end-for  
  
  // all good
  return stuff; 

} // end-method   
} // end-class
