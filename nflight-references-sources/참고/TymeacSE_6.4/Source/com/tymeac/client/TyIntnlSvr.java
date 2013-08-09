package com.tymeac.client;

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
import javax.naming.*;
import java.lang.reflect.*;
import com.tymeac.base.*;
import com.tymeac.common.*; 

/**
 * Internal communication module for calling the Tymeac Server. Used by the
 *  supplied client classes.
 */
public final class TyIntnlSvr {

  // variables
  TyVariables TyV = null;

  // The Remote Object
  TymeacInterface Ti = null;  

/**
 * This is the front end for use by the GUI Tymeac modules in communicating with the
 *   Tymeac Server.
 */
public TyIntnlSvr() { 

  // new instance of the Tymeac Variables
  TyV = new TyVariables();

  // When using the RMI Registry
  if  (TyV.useRMI()) {  
                         
      // do the Registry procedure
      doRMI();

      // return
      return;
    
  } // endif

  // When using the CosNaming server
  if  (TyV.useIIOP()) {  
                         
      // do the CosNaming server procedure
      doIIOP();

      // return
      return;
    
  } // endif

  // When using an alternate communication medium
  if  ((TyV.useAlt()) && 
       (TyV.getAltName() != null)) {  
  
      // do the alternate procedure
      doAlternate(TyV.getAltName());
  
      // return
      return;
    
  } // endif  

} // end-constructor

/**
 * This is the alternate communication procedure
 *
 *  Add your code here
 *
 * @parm java.lang.String
 */ 
private void doAlternate(String alt_name) {  
     
  // address of imlemented method in TymeacAlternateInterface
  java.lang.reflect.Method meth;
                    
  // class definition
  Class<?> c;
  
  // dummy
  Class[] dummy = new Class[0];
  
  // new instance of alternate class
  Object obj;
  
  // dummy
  Object[] objDummy = new Object[0];
  
  // *--- get the class definition ---*
  try {
    // load the class 
    c = Class.forName(alt_name); 
    
  } // end-try
  
  catch (ClassNotFoundException e) {    
  
    System.out.println("Alternate class not found: " + e.getMessage());
    
    return;
    
  } // end-catch 
     
  // *--- get the needed method ---*
  try {
    // find the method    
    meth = c.getMethod("getTymeacInterface", dummy);
    
  } // end-try
  
  catch (NoSuchMethodException e) {    
  
    System.out.println("Invalid Alternate class structure: " + e.getMessage());
    
    return;
    
  } // end-catch 
  
  catch (SecurityException e) {    
  
    System.out.println("Security error: " + e.getMessage());
    
    return;
    
  } // end-catch      
  
  // *--- get an object for the later method invocation ---*
  try {
    // get a new instance of this class
    obj = c.newInstance();
    
  } // end-try
  
  catch (InstantiationException e) {    
  
    System.out.println("Could not instantiate alt class: " + e.getMessage());
    
    return;
    
  } // end-catch  
  
  catch (IllegalAccessException e) {    
  
    System.out.println("non-accessable alt class: " + e.getMessage());
    
    return;
    
  } // end-catch  
  
  // *--- almost like it is local ---*
  try {
    // do the method
    Ti = (TymeacInterface)meth.invoke(obj, objDummy);
    
  } // end-try
         
  catch (IllegalAccessException e) {    
  
    System.out.println("non-accessable alt class method: " + e.getMessage());
    
    return;
    
  } // end-catch
  
  catch (IllegalArgumentException e) {    
  
    System.out.println("invalid alt class method structure: " + e.getMessage());
    
    return;
    
  } // end-catch
   
  catch (InvocationTargetException e) {    
  
    System.out.println("alt class had an exception: " + e.getMessage());
    
    return;
    
  } // end-catch 

} // end-method 

/**
 * The variables needed for the CosNaming server are the Tymeac_IIOP name.
 *  This comes from the Tymeac Variables Class.
 */ 
private void doIIOP() {                  
  
  // get the IIOP name
  String IIOP_name = TyV.getIIOPName(); 

  try { 
    // new context obj
    Context initialNamingContext = new InitialContext();
    
    // CosNaming look up
    Ti = (TymeacInterface)
          javax.rmi.PortableRemoteObject.narrow(initialNamingContext.lookup(IIOP_name), 
                                                TymeacInterface.class);                                   
  } // end-try
 
  catch(NamingException e) {
    
    // add your code here
    //e.printStackTrace(System.out); 
    System.out.println("Naming Error: " + e); 
      
  } // end-catch
  
  catch(ClassCastException e) {
    
    // add your code here
    //e.printStackTrace(System.out); 
    System.out.println("Cannot Cast: " + e); 
      
  } // end-cast

  catch(Exception e) {
    
    // add your code here
    //e.printStackTrace(System.out); 
    System.out.println("CosNaming.lookup Error: " + e); 
            
  } // end-catch

} // end-method

/**
 * The variables needed for RMI are the local URL name and the
 *     Tymeac name.  These come from the Tymeac Variables Class.
 *
 *   You may use this to set a security manager.  
 */ 
private void doRMI() {

  // get the URL name
  String url_name = TyV.getUrl();
  
  // get the Tymeac name
  String tymeac_name = TyV.getTymeac();    

/*
  // new security manager for RMI
  try {
     System.setSecurityManager( new RMISecurityManager());
  } // end-try
      
  catch (SecurityException e) {
      
      // put your code here
        
  } // end-catch
  
*/
    
  // keep trying the conection 
  for (int count = 10; count > 0; count--) {
  
    try { 
        // rmi naming look up
      Ti = (TymeacInterface)java.rmi.Naming.lookup(url_name + tymeac_name);
                       
      // got one
      break;
                              
    } // end-try
   
    catch(ConnectException e) {
      
      // add your code here  
      
        
    } // end-catch
  
    catch(Exception e) {
      
      // add your code here
      //e.printStackTrace(System.out); 
      System.out.println("RMI Naming.lookup Error: " + e); 
      
      // no good
      break;
        
    } // end-catch        
  } // end-for

} // end-method

/**
 *  Used internally by Tymeac. 
 */ 
public String[] func1Request(String func_name) {

  // When no internal connection, oops
  if  (Ti == null)   return null;
  
  try {
    // to Tymeac, returning a string[]
    return (Ti.func1Request(func_name));
      
  } // end-try      
  
  catch(Exception e) {
 
      //System.out.println("Error: " + e);    
            
      return null;
            
  } // end-catch  
  
} // end-method

/**
 *  Used internally by Tymeac. 
 */ 
public TyAltSvrElements alterSvrOptions( TyAltSvrElements opts) {

  // When no internal connection, oops
  if  (Ti == null)  return null;
  
  try {
    // to Tymeac, returning a string[]
    return (Ti.alterSvrOptions(opts));
      
  } // end-try      
  
  catch(Exception e) {
 
      //System.out.println("Error: " + e);    
            
      return null;
            
  } // end-catch  
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int idStatus1Request(long id, 
                            long req,
														int action) {

  // When no internal connection, oops
  if  (Ti == null) return -1;
  
  try {
    // to Tymeac, returning a string
    return (Ti.idStatus1Request(id, req, action));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return -1;
            
  } // end-catch  
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int newCopyRequest(String Q, 
                          String C) {

  // When no internal connection, oops
  if  (Ti == null)  return -1;
  
  try {
     // to Tymeac, returning an int
     return (Ti.newCopyRequest(Q, C));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return -1;
            
  } // end-catch
  
} // end-method

/**
 * Reset or set a New Runtime Function (notify, log or stats)
 * @param type of request
 * @param one String when DBMS or Alt
 * @param two String when file or directory
 * @return what happened
 */
public int newRunTimeFunctions(int type, String one, String two) {

  // When no internal connection, oops
  if  (Ti == null)  return -1;
  
  try {
     // to Tymeac, returning an int
     return (Ti.newRunTimeFunctions(type, one, two));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return -1;
            
  } // end-catch
  
} // end-method

/**
 * New run time functions maintenance
 * @param type of elements needed
 * @return elements
 */
public TyRunTimeElements newRTMaint(int type) {

  // When no internal connection, oops
  if  (Ti == null)  return null;
  
  try {
     // to Tymeac, returning an int
     return (Ti.newRTMaint(type));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public TyOverallObj overallRequest(int req_type) {   
    
  // When no internal connection, oops
  if  (Ti == null)  return null;
  
  try {
     // to Tymeac, returning an obj
     return (Ti.overallRequest(req_type));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public TyQueElements que1Request(String que_name) {

  // When no internal connection, oops
  if  (Ti == null) return null;
  
  try {
     // to Tymeac, returning an obj
     return (Ti.que1Request(que_name));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch
          
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int que2Request(TyQueElements que_ele) {   

  // When no internal connection, oops
  if  (Ti == null)  return -1;
  
  try {
    // to Tymeac, returning a string
    return (Ti.que2Request(que_ele));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return -1;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public String[] que3Request(String que_name) {
   
  // When no internal connection, oops
  if  (Ti == null)  return null;
  
  try {
     // to Tymeac, returning a string[]
     return (Ti.que3Request(que_name));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch

} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int que4Request(String que_name,
                       int    thread_nbr) {
   
  // When no internal connection, oops
  if  (Ti == null) return 3;
  
  try {
     // to Tymeac, returning an int
     return (Ti.que4Request(que_name, thread_nbr));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return 3;
            
  } // end-catch

} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int que5Request(String que_name) {
   
  // When no internal connection, oops
  if  (Ti == null) return 1;
  
  try {
     // to Tymeac, returning a string
     return (Ti.que5Request(que_name));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return 1;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public TyWLElements[] que6Request(String que_name) {   

  // When no internal connection, oops
  if  (Ti == null) return null;
  
  try {
    // to Tymeac, returning an object
    return (Ti.que6Request(que_name));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch
  
} // end-method

/**
 * Shut down server
 * @return java.lang.String
 */
public String shutRequest () {

  // for backward compatability
  return shutRequest(false);
  
} // end-method

/**
 * Shut down server as of release 4.0.4
 * @return java.lang.String
 */
public String shutRequest (boolean force) {

  // When no internal connection, oops
  if  (Ti == null) return null;
  
  try {
     // to Tymeac, returning a string
     return (Ti.shutRequest(force));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public String[] stall1Request() {   

  // When no internal connection, oops
  if  (Ti == null) return null;
  
  try {
    // to Tymeac, returning a string[]
   return (Ti.stall1Request());
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int stall2Request(long id) {   

  // When no internal connection, oops
  if  (Ti == null) return 0;
  
  try {
    // to Tymeac, returning an int
    return (Ti.stall2Request(id));
      
  } // end-try      
  
  catch(Exception e) {
 
    // System.out.println("Error: " + e);     
          
    return 0;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public String[] stall3Request(long id) {   

  // When no internal connection, oops
  if  (Ti == null)  return null;
  
  try {
     // to Tymeac, returning a string[]
     return (Ti.stall3Request(id));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return null;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int stall4Request(long id) {   

  // When no internal connection, oops
  if  (Ti == null) return 0;
  
  try {
    // to Tymeac, returning an int
    return (Ti.stall4Request(id));
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return 0;
            
  } // end-catch
  
} // end-method

/**
 *  Used internally by Tymeac
 */ 
public int stats1Request() {
                                        
  // When no internal connection, oops
  if  (Ti == null) return 0;
  
  try {
    // to Tymeac, returning an int
    return (Ti.stats1Request());
      
  } // end-try      
  
  catch(Exception e) {
 
    //System.out.println("Error: " + e);    
          
    return 0;
            
  } // end-catch
  
} // end-method
} // end-class
