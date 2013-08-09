package com.tymeac.jini.client;

/*
 * Created on Jan 24, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
import java.security.*;
import javax.security.auth.Subject;
import javax.security.auth.login.*;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.security.BasicProxyPreparer;
import net.jini.security.ProxyPreparer;

import com.tymeac.jini.base.*;
import com.tymeac.base.*;

/**
 * @author CoopSoft.com
 * Tymeac demonstration system. Display (println) the result of
 *    Tymeac Demo Functions.
 * 
 * This class as a base from which to construct demo calls to the Tymeac Server
 *   Inherit this class and override the main()method.
 * 
 * 1. When using an [a]syncRequest() 
 *      Define a String to pass to the Tymeac Processing Application Class
 *        and cast it to Object. Ignore or eliminate for a shutRequest().
 * 
 * 2. When using an [a]syncRequest()
 *      Define the TymeacParm that is passed to the TymeacServer.
 *        Ignore or eliminate for a shutRequest().
 * 
 * 3. Call one of the three available routines passing the command line options:
 *      doSync(args) - for a synchronous request
 *      doAsync(args) - for an asynchronous request
 *      doShut(args) - for a server shut down
 * 
 * 4. End the virtual machine. Some callback handles use a separate thread for login
 *      so sometimes just a return will not end the JVM.
 * 
 * 5. In all three available routines:
 *      Try to get a login context passing the command line args and login,
 *        getContext(args);
 * 
 * 6. In all three available routines:
 *      When the login context was successful, use a Privileged call to doTymeac() otherwise
 *        use a normal call to doTymeac().
 * 
 * 7. Print out what came back from the doTymeac().
 * 
 * 8. doTymeac() - Call the server through the TySvrComm Class. This returns either an
 *      Object[] or a String.
 * 
 * 9. printAll() - Prints the Object[] of Strings.
 * 
 * 10. getContext() - Get the context for logging in and do a login.
 *        This is user defined.
 * 
 */
public class JiniClientBase {
  
  // parm to Tymeac
  protected static TymeacParm TP = null;
  
  //Tymeac interface
  private static TymeacInterface server = null;
  
  // return from Tymeac
  private static Object[] backArray = null;
  private static String   backString = null;
  
  // config provider
  private static Configuration  config = null;
  
  // component name for getEntry()
  private static final String  configComponent = "com.tymeac.TymeacClient";
  
  // security login context
  private static LoginContext loginContext = null;   
  
  // security switch
  private static boolean useSecurity = false;
  
  // shut down force indicator
  private static boolean force = false;

/**
 * Request processing of Tymeac Function. You must override this method
 *    and change the function name for [a]syncRequest() methods.
 *    For a shut request you may ignore this.
 *    
 * @param args java.lang.String[]
*/ 
public static void main(String args[]) {
    
//1.    
  // new string for Tymeac function
  String x = "223344556677889911";

  // make obj
  Object pass =  x;
   
//2.   
  // form a parameter for Tymeac   
  TP = new TymeacParm(pass,         // data 
                      "Function_2", // function name *--- change this ---*
                      10,            // wait time
                      1);            // priority
                      
// 3.
  // doAsync(args) or
  // doSync(args) or
  // doShut(args) 
  doAsync(args);

// 4.    
  // end
  System.exit(0);
                                      
} // end-method

/**
 * Try to get a login context for security checking
 * 
 * Call Tymeac Server with an asynchronous request either
 *   using a security framework or without security.
 * 
 * Print out what came back.
 *  
 * @param args
 */
protected static void doAsync(String args[]) {

//5.  
  // When args are invalid
  if  (!parseArgs(args)) {
    
      // done
      return;
      
  } // endif      
  
  // When using security     
  if (useSecurity) {
  
      // try to get a login context and login
      if  (!getContext()) { 
    
          // cannot continue
          System.exit(1);
      
      } // endif
  } // endif  
  
  // try to get a server
  if  (!doJini()) { 
    
      // cannot continue
      System.exit(1);
      
  } // endif          

//6.        
  // When not using security     
  if (!useSecurity) {
  
      // say what
      System.out.println("Using INSECURE client");
      
      // non-secure
      doTymeac(2);     
  } 
  else {    
      try {
        // say what
        System.out.println("Using secure client");
      
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doTymeac(2);
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);                    
                   
      } // end-catch              
    } // endif

//7.    
    // When nothing came back
    if  (backArray == null)  {
            
        // say no good  
        System.out.println("Null returned from Tymeac Server");

        // bye
        return;

    } // endif        
      
    // print out the array
    PrintAll();
    
    // done
    return;      
       
} // end-method

/**
 * Try to get a login context for security checking
 * 
 * Call Tymeac Server with a synchronous request either
 *   using a security framework or without security.
 * 
 * Print out what came back.
 *  
 * @param args
 */
protected static void doSync(String args[]) {
  
//5.  
  // When args are invalid
  if  (!parseArgs(args)) {
    
      // done
      return;
      
  } // endif      
  
  // When using security     
  if (useSecurity) {
  
      // try to get a login context and login
      if  (!getContext()) { 
    
          // cannot continue
          System.exit(1);
      
      } // endif
  } // endif  
  
  // try to get a server
  if  (!doJini()) { 
    
      // cannot continue
      System.exit(1);
      
  } // endif          

//6.        
  // When not using security     
  if (!useSecurity) {
  
      // say what
      System.out.println("Using INSECURE client");
      
      // non-secure
      doTymeac(1);     
  } 
  else {    
      try {
        // say what
        System.out.println("Using secure client");
      
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doTymeac(1);
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try 
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);                    
                   
      } // end-catch              
    } // endif

//7.    
    // When nothing came back
    if  (backArray == null)  {
            
        // say no good  
        System.out.println("Null returned from Tymeac Server");

        // bye
        return;

    } // endif        
      
    // print out the array
    PrintAll();
    
    // done
    return;      
  
} // end-method

/**
 * Try to get a login context for security checking
 * 
 * Call Tymeac Server with an shut down request either
 *   using a security framework or without security.
 * 
 * Print out what came back.
 *  
 * @param args
 */
protected static void doShut(String args[], boolean kill) {
  
  // save indicator
  force = kill;
  
//5.  
  // When args are invalid
  if  (!parseArgs(args)) {
    
      // done
      return;
      
  } // endif      
  
  // When using security     
  if (useSecurity) {
  
      // try to get a login context and login
      getContext();
      
  } // endif  
  
  // try to get a server
  if  (!doJini()) { 
    
      // cannot continue
      System.exit(1);
      
  } // endif          

//6.        
  // When not using security     
  if (!useSecurity) {
  
      // say what
      System.out.println("Using INSECURE client");
      
      // non-secure
      doTymeac(3);     
  } 
  else {    
      try { 
        // say what
        System.out.println("Using secure client");
      
        // do as privileged
        Subject.doAsPrivileged(
            loginContext.getSubject(),
            new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    doTymeac(3);
                    return null;
                } // end-method
            }, // end-PEA
            null);
            
      } // end-try 
      
      catch (PrivilegedActionException e) {
        
          // say what found
          System.out.println("PrivilegedActionException= " + e);                    
                   
      } // end-catch              
    } // endif

//7.    
    // When nothing came back
    if  (backString == null)  {
            
        // say no good  
        System.out.println("Null returned from Tymeac Server");

        // bye
        return;

    } // endif        
      
    // print out the string
    System.out.println(backString);
    
    // done
    return;        
  
} // end-method

private static boolean parseArgs(String[] args) {
  
  // for arguement testing
  int arg_count = args.length;
  
  // argument strings
  String configName[] = new String[1];

  // set the passed arguments
  switch (arg_count) {

    // no good, nothing passed
    case 0:
      // say what
      System.out.println("No command line arguments (-config + location) required");
      
      // done
      return false;

    // no good, need at least two
    case 1:
      // say what
      System.out.println("insufficient command line arguments (-config + (location) required");
      
      // done
      return false;
    
    // this must be -config and config location  
    case 2:
      // When 1st arg is for -config 
      if  (args[0].equalsIgnoreCase("-config")) {
        
          // set the config location
          configName[0] = args[1];
          
          // ok
          break;  

      } // endif
      
      // say what
      System.out.println("invalid command line argument="
                        + args[0]
                        + " must be (-config + (location)");
      
      // done
      return false; 

    // may be -secure -config configLocation or -secure last
    case 3:
       
      // When 1st arg is for -config 
      if  (args[0].equalsIgnoreCase("-config")) {
        
          // set the config location
          configName[0] = args[1];
          
          // When 3rd arg is for -secure 
          if  (args[2].equalsIgnoreCase("-secure")) {
        
              // set using secure 
              useSecurity = true;
          
              // good
              break;                
          }
          else {
              // say what
              System.out.println("invalid command line argument="
                                + args[2]
                                + " must be (-config + (location)");
      
              // done
              return false; 
          } // endif    
      } 
      else {
          // When 1st arg is for -secure 
          if  (args[0].equalsIgnoreCase("-secure")) {
        
              // set using secure 
              useSecurity = true;
          
              // When 2nd arg is for -config 
              if  (args[1].equalsIgnoreCase("-config")) {
        
                  // set the config location
                  configName[0] = args[2];
          
                  // good
                  break;  
              }
              else {
                  // say what
                  System.out.println("invalid command line argument="
                                    + args[1]
                                    + " must be (-config + (location)");
      
                  // done
                  return false; 
                  
              } // endif        
          } // endif
      } // endif
      
      // say what
      System.out.println("invalid command line argument="
                        + args[0]
                        + " must be (-config or -secure)");
      
      // done
      return false;             

    // nothing good here
    default:
      // say what
      System.out.println("invalid command line arguments"
                        + " must be [-secure] -config (location)");
      
      // done
      return false;        
  
  
   } // end-switch
   
   /*
    * We get here because there is a configuration name. Now we have to
    *   validate the configuration provider. The context name is hard coded
    *   as a static final String. 
    */
    
  try {
      // try to get a new provider            
      config = ConfigurationProvider.getInstance(configName);
      
    } // end-try
    
    catch (Exception e) {
      
      // say what
      System.out.println("Could not get a Configuration Provider= " + e.toString());
      
      // done
      return false;   
                
    } // end-catch
  
  // ok
  return true;
      
} // end-method

/**
 * Jini work: service discovery and proxy preparing
 * @return boolean 
 */
private static boolean doJini() {
  
  // service discovery manager
  ServiceDiscoveryManager serviceDiscovery = null;  
  
  try {
    /*
     * In order to use a configuration entry you must set up the full
     *   Service Discovery Manager with an exporter and Proxy Trust and
     *    method contraints, etc. 
     */
    // get the service discovery manager
    serviceDiscovery = (ServiceDiscoveryManager) config.getEntry(
          configComponent,
          "serviceDiscovery",
          ServiceDiscoveryManager.class);
    
           
    
    
    /*
    // Get the basic discovery manager with only "TymeacServer" registered  
    serviceDiscovery = new ServiceDiscoveryManager(
        new LookupDiscovery(
          new String[] { "TymeacServer" }),
        null);
      
              
    */                           
  } // end-try 
  
  catch (Exception e) {
    
    // say what
    System.out.println("Could not get a Service Discovery Manager= " + e.toString());
      
    // done
    return false;   
  
  } // end-catch    

  // service item is only Tymeac      
  ServiceItem serviceItem = null;
  
  try {    
  // Look up the remote server
  serviceItem = serviceDiscovery.lookup(
      new ServiceTemplate(
        null,
        new Class[] { JiniTymeacProxy.class },
        null),
      null,
      10000L); // 10 seconds
        
  } // end-try 
  
  catch (Exception e) {
    
    // say what
    System.out.println("Service Discovery returned= " + e.toString());
      
    // done
    return false;
       
  } // end-catch           
  
  // When could not find
  if  (serviceItem == null) {
    
      // say what
      System.out.println("Could not find a Tymeac Server on the network");
      
    // done
    return false;
       
  } // endif  
        
  // cast to a basic tymeac interface        
  TymeacInterface baseServer = (TymeacInterface) serviceItem.service;
  
  // proxy preparer
  ProxyPreparer preparer = null;
  
  try {
    // get the entry
    preparer = (ProxyPreparer) config.getEntry(
      configComponent,
      "preparer",
      ProxyPreparer.class,
      new BasicProxyPreparer());
      
  } // end-try 
  
  catch (Exception e) {
    
    // say what
    System.out.println("Could not get a Proxy prepare= " + e.toString());
      
    // done
    return false;
       
  } // end-catch          
  
  try {      
    // prepare for use
    server = (TymeacInterface) preparer.prepareProxy(baseServer);
  
  } // end-try 
  
  catch (Exception e) {
  
    // say what
    System.out.println("Proxy preparer returned= " + e.toString());
      
    // done
    return false;
       
  } // end-catch    
     
  // good
  return true;        
  
} // end-method 



 

/**
 * Call the Tymeac Server using the TySvrComm module. It is the TySvrComm module that
 *   gets the remote object from either the RMI/JNDI Registry or an alternate source
 *   programmed by you.  
 * 
 *  @param int What type of call to make.
 */
private static void doTymeac(int what) {                                  

//8.
    try {
      // do what is necessary
      switch (what){
      
        case 1:   backArray = server.syncRequest(TP);
                  return;
        
        case 2:   backArray = server.asyncRequest(TP);
                  return;
        
        case 3:   backString = server.shutRequest(force);
                  return;
        
        default:  System.out.println("Invalid request for Tymeac");
                  return;
        
      } // end-switch
    } // end-try           
    
    catch (Exception e) {
    
      // print msgs
      System.out.println("Exception from calling Tymeac Server = " + e); 
      
     // get out  
     return; 
  
    } // end-catch     
} // end-method

/**
 * Print out the concatination of all the returned Strings
 *   from a Tymeac call.
 *
 */
private static void PrintAll() {

//9.  
    // *--- [a]syncReq ---* 

    // number of objects in array
    int nbr = backArray.length;

    // Display string
    StringBuffer S = new StringBuffer(nbr);
    
    /*
     * The returned array contains objects from both Tymeac and the Processing
     *   Application Classes.
     *
     *  The first object is a String cast to an object. It contains the Tymeac
     *    result of the processing: Tymeac SR (0000) 
     *      Tymeac is the prefix. 
     *      SR stands for Synchronous Request, AR stands for an Asynchronous Request. 
     *      (nnnn) Return code, should be 0000.  
     *           When the number in parentheses is not zero, 
     *           then check the return code document for the failure reason.
     *
     *  The other objects in the array are Strings cast to an object. Naturally,
     *    when you develop your own systems, these objects will be other classes.
     * 
     */

    // concatenate all the strings
    for  (int i = 0; i < nbr; i++) {

        // must be a string
        if  ((backArray[i] != null) &&  
             (backArray[i] instanceof String)) {

              // concat
              S = S.append((String) backArray[i]);

        } // endif
    } // end-for
    
    // say what
    System.out.println(S.toString());
         
   // done      
   return;   
  
} // endif  

/**
 * Get the context for logging in. This is user defined.
 *   For the demonstration we supply several modules that do a login:
 *    Three (3) System Properties:
 *      -Djava.security.manager 
 *      -Djava.security.policy=<TYMEAC_HOME>/security/policy.all
 *      -Djava.security.auth.login.config=config/tymeac-ssl-client.login
 * 
 *    1. Sets the security manager in effect
 *    2. Sets the security policy in effect. We supply an ALL Permissions in the file
 *         location where you installed Tymeac <TYMEAC_HOME>, subdirectory "security".
 *    3. Defines the configuration file in the file location where you are currently
 *         running (current-directory), subdirectory "config". The file is as follows:
 *     
 *        com.tymeac.TymeacServer {
 *                  com.sun.security.auth.module.KeyStoreLoginModule required
 *                  keyStoreAlias="client"
 *                  keyStoreURL="file:config/client.keystore"
 *                  keyStorePasswordURL="file:config/client.password";
 *        };
 *       
 *        The last two entries define a keystore location and password location.
 *        Both are in the same "config" subdirectory. You may not edit the keystore or
 *        password but you may always configure your own. The password is: clientps
 * 
 *  Do a login using the Login Context returned from above.
 *  
 */
private static boolean getContext() {

//10. 
  try {
    // get the context
    loginContext = (LoginContext) config.getEntry(
         configComponent,          // component from static final field
         "loginContext",           // name
         LoginContext.class,       // type of class
         null);                    // no data
    
  } // end-try
  
  catch (java.lang.SecurityException e) {
  
      // say what
      System.out.println("LogonContext security exception= " + e.toString());
      
      // done
      System.exit(1);   
    
  } // end-catch 
  
  catch (Exception e) {
              
      // say what
      System.out.println("LogonContext exception= " + e.toString());
      
      // done
      System.exit(1);                    
                       
  } // end-catch
  
  // When NO login context
  if  (loginContext == null) {
    
      // no security
      useSecurity = false;
      return false;
      
  } // endif      
    
  try {
    // login
    loginContext.login();
        
    // good
    useSecurity = true;
    return true;
        
  } // end-try
    
  catch (LoginException e) {
        
    // say what
    System.out.println("Login exception= " + e.toString());
      
    // done
    System.exit(1);   
             
  } // end-catch
      
  catch (Exception e) {
        
    // say what
    System.out.println("Logging in, exception= " + e.toString());
      
    // done
    System.exit(1);                    
                   
  } // end-catch
  
  // no good
  useSecurity = false;
  return false;

} // end-method 

} // end-class
