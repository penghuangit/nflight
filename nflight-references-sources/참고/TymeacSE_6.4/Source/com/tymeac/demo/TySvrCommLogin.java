package com.tymeac.demo;

/* 
 * Copyright (c) 2006 Cooperative Software Systems, Inc. 
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
 * This is the Tymeac Server Communication Class used by Clients.  This
 *  Class is not used by the Tymeac Server.  You may make any changes 
 *  for your installation.
 *
 * Constructor() is for use by all customer code. 
 *
 * The Client Communication Methods are described in your documentation.
 *
 */ 
public class TySvrCommLogin {
 
        // used internally
        private static TyVariables TyV = null;  
        private static TymeacInterface Ti = null;  

/**
 * This constructor is used by the Tymeac Client applications when a naming service
 *   look up is necessary.   
 *
 */
public TySvrCommLogin() {

    // new instance of the Tymeac Variables
    TyV = new TyVariables();

    // When using the RMI Registry
    if  (TyV.useRMI()) {

        // do the RMI Registry procedure
        doRMI();

        // return
        return;
      
    } // endif

    // When using the JNDI Registry
    if  (TyV.useIIOP()) {

        // do the JNDI Registry procedure
        doIIOP();

        // return
        return;
      
    } // endif

    // When using an alternate communication method
    if  ((TyV.useAlt()) && (TyV.getAltName() != null)) {

        // do the alternate procedure
        doAlternate(TyV.getAltName());

        // return
        return;
        
    } // endif

} // end-constructor

/**
 * This constructor is used by the Tymeac Client applications when no 
 *   naming look up is necessary.
 *
 *@param Ty TymeacInterface 
 */
public TySvrCommLogin(TymeacInterface Ty) {

    // set instance field
    Ti = Ty;

} // end-constructor

/**
 * This is the Asynchronous Request method for client applications.
 *
 * @param TP TymeacParm
 * @return Object
 * 
 */ 
public Object[] asyncRequest(TymeacParm TP) {  

    // When the connection failed
    if  (Ti == null) {
  
        // all done
        return null;
    
    } // endif           
            
    try {     
        // to Tymeac, returning an object[]
        return (Ti.asyncRequest(TP));
     
    } // end-try    
  
    catch(RemoteException e) {
        // add your code here
        e.printStackTrace(System.out); 
        //System.out.println("asyncRequest RemoteException: " + e); 
        
        // no good
        return null;
        
    } // end-catch  
    
    catch(Exception e) {
        // add your code here
        e.printStackTrace(System.out); 
        //System.out.println("asyncRequest RMI Error: " + e); 
        
        // no good
        return null;
        
    } // end-catch                
         
} // end-method

/**
 * This is the Cancel Synchronous Request method for client applications.
 * @param forCancel CancelParm containing request to cancel
 * @return int 
 */ 
public int cancelSyncReq(CancelParm forCancel) {
   
    // When the connection failed
    if  (Ti == null) {
  
        // all done
        return CancelParm.CONNECTION_FAILURE;
    
    } // endif
  
    try {     
        // to Tymeac
        return (Ti.cancelSyncReq(forCancel));
    
    } // end-try
    
    catch(RemoteException e) {
        // add your code here
        //e.printStackTrace(System.out); 
        System.out.println("cancelRequest RemoteException: " + e); 
        
        // no good
        return CancelParm.REMOTE_EXCEPTION;
        
    } // end-catch  
  
    catch(Exception e) {
        // add your code here
        //e.printStackTrace(System.out); 
        System.out.println("cancelRequest RMI Error: " + e); 
        
        // no good
        return CancelParm.EXCEPTION;
        
    } // end-catch              
   
} // end-method

/**
 * This is the alternate communication procedure
 *
 *   Although this Class and this method are your code, the functionality
 *     within this method is similar to the code with a private Tymeac
 *     communication Class.  That Class is used by the Tymeac GUI and
 *     non-GUI Client Classes to communicate with the Tymeac Server.
 *
 *  THEREFORE, you may not alter the functionality of this method.
 *    You may certainly do anything in any order.  However, it all
 *    must come down to invoking this method: "getTymeacInterface"
 *    and returning this object: "TymeacInterface"
 *    on an instance of your Alternate.class: "obj"
 *  
 *       (TymeacInterface) meth.invoke(obj, null); 
 *
 *   When you make changes to your Alternate.class, you should also
 *     make sure the Tymeac GUI Classes work.  Run the main Tymeac
 *     GUI Class, TyMenu.  Choose various windows within to make sure
 *     all is well.
 *
 * @param alt_name String Class name
 * 
 */
private void doAlternate(String alt_name) {

    // pointer to the imlemented method in Alternate.class
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
  
        System.out.println("Alternate class not found: " + e.toString());
    
        // done
        return;
        
    } // end-catch 
     
    // *--- get the needed method ---*
    try {
        // find the method
        meth = c.getMethod("getTymeacInterface", dummy);
    
    } // end-try
  
    catch (NoSuchMethodException e) {    
  
        System.out.println("Invalid Alternate class structure: " + e.toString());
    
        // done
        return;
    } // end-catch 
  
    catch (SecurityException e) {    
  
        System.out.println("Security error: " + e.toString());
    
        // done
        return;
        
    } // end-catch      
  
    // *--- get an object for the later method invocation ---*
    try {
      
        // get a new instance of this class
        obj = c.newInstance();
    
    } // end-try
  
    catch (InstantiationException e) {    
  
        System.out.println("Could not instantiate alt class: " + e.toString());
    
        // done
        return;
        
    } // end-catch  
  
    catch (IllegalAccessException e) {    
  
        System.out.println("non-accessable alt class: " + e.toString());
    
        // done
        return;
        
    } // end-catch  
  
    // *--- almost like it is local ---*
    try {       
        // do the method
        Ti = (TymeacInterface)meth.invoke(obj, objDummy);
        
    } // end-try
         
    catch (IllegalAccessException e) {    
  
        System.out.println("non-accessable alt class method: " + e.toString());
    
        // done
        return;
        
    } // end-catch
  
    catch (IllegalArgumentException e) {    
  
        System.out.println("invalid alt class method structure: " + e.toString());
    
        // done
        return;
        
    } // end-catch
   
    catch (InvocationTargetException e) {    
  
        System.out.println("alt class had an exception: " + e.toString());
    
        // done
        return;
    
  } // end-catch 
  
  // all done
  return;

} // end-method

/**
 * The variable needed for the CosNaming server is the Tymeac_IIOP name.
 *   This comes from the Tymeac Variables Class.
 *   
 */ 
private void doIIOP() {      
  
    // get the IIOP name
    String IIOP_name = TyV.getIIOPName(); 

    try { 
        // new context obj
        Context initialNamingContext = new InitialContext();
        
        // CosNaming look up
        Ti = (TymeacInterface)
              javax.rmi.PortableRemoteObject.narrow(
                  initialNamingContext.lookup(IIOP_name), 
                                              TymeacInterface.class);
                                   
    } // end-try
   
    catch(NamingException e) {
      
        // add your code here
        //e.printStackTrace(System.out); 
        System.out.println("Naming Error: " + e.toString()); 
        
        // get out
        return;           
        
    } // end-catch
    
    catch(ClassCastException e) {
      
        // add your code here
        //e.printStackTrace(System.out); 
        System.out.println("Cannot Cast: " + e.toString()); 
        
        // get out
        return;     
        
    } // end-catch
  
    catch(Exception e) {
      
        // add your code here
        //e.printStackTrace(System.out); 
        System.out.println("CosNaming.lookup Error: " + e.toString()); 
        
        // get out
        return;     
        
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
      
  catch (Exception e) {
      
      // put your code here
      //e.printStackTrace(System.out); 
      System.out.println("RMI Security Manager Error: " + e.toString()); 
        
  } // end-catch  
*/
  // try to connect this many times
  int count = 10;
  
  TymeacLogin tl = null;
 
  // keep trying the conection 
  while  (count > 0) {

    try { 
      // rmi naming look up
      tl = (TymeacLogin)java.rmi.Naming.lookup(url_name + tymeac_name);
                 
      // got one
      break;
                          
    } // end-try
 
      catch(ConnectException e) {
    
      // add your code here  
      count--;   
                
    } // end-catch

    catch(Exception e) {
  
      // add your code here
      //e.printStackTrace(System.out); 
      System.out.println("RMI Naming.lookup Error: " + e.toString()); 
  
      // no good
      break;
    
    } // end-catch        
  } // end-while
  
  // When did not succeed
  if  (tl == null) return;
  
  Object[] obj = new Object[2];
  obj[0] = "could be userid";
  obj[1] = "could be password";
  
  try { 
    // get the real remote object from Tymeac after a login 
    Ti = tl.login(obj);               
                        
  } // end-try   

  catch(Exception e) {

    // add your code here
    //e.printStackTrace(System.out); 
    System.out.println("Error returned from Tymeac Login: " + e.toString()); 
  
  } // end-catch 

} // end-method

/**
 * This is the Shut Down Request method for client applications.
 *
 * The return code is: ( Tymeac SD(xxxx) )  
 *   When the code, xxxx, is zero, Tymeac is fully shut down.
 *   When other, consult the documentation.  Usually, threads are still
 *     active.  Just rerun this method.
 *
 * @return java.lang.String 
 */ 
public String shutRequest() {
   
   
    // use the full method 
    return shutRequest(false);             
   
} // end-method

/**
 * This is the Shut Down Request method for client applications with a force indicator.
 *
 * The return code is: ( Tymeac SD(xxxx) )  
 *   When the code, xxxx, is zero, Tymeac is fully shut down.
 *   When other, consult the documentation.  Usually, threads are still
 *     active.  Just rerun this method.
 *
 * @return java.lang.String 
 */ 
public String shutRequest(boolean force) {
   
    // When the connection failed
    if  (Ti == null) {
  
        // all done
        return null;
    
    } // endif
  
    try {     
        // to Tymeac, returning a string
        return (Ti.shutRequest(force));
    
    } // end-try
    
    catch(RemoteException e) {
        // add your code here
        //e.printStackTrace(System.out); 
        System.out.println("shutRequest RemoteException: " + e.toString()); 
        
        // no good
        return null;
        
    } // end-catch  
  
    catch(Exception e) {
        // add your code here
        //e.printStackTrace(System.out); 
        System.out.println("shutRequest RMI Error: " + e.toString()); 
        
        // no good
        return null;
        
    } // end-catch              
   
} // end-method




/**
 * This is the Synchronous Request method for client applications.
 *
 * @param TP TymeacMOParm
 * @return Object
 * 
 */ 
public Object[] syncRequest(TymeacParm TP) {
   
    // When the connection failed
    if  (Ti == null) {
  
        // all done
        return null;
    
    } // endif
      
    try {
        // to Tymeac, returning an object[]
        return (Ti.syncRequest(TP));  
  
    } // end-try
    
    catch(RemoteException e) {
        // add your code here
        e.printStackTrace(System.out); 
        //System.out.println("syncRequest RemoteException: " + e.toString());  
                
        // no good
        return null;
        
    } // end-catch  
  
    catch(Exception e) {
        // add your code here
        e.printStackTrace(System.out); 
        //System.out.println("syncRequest RMI Error: " + e.toString()); 
        
        // no good
        return null;
        
    } // end-catch      
  
} // end-method
} // end-class
