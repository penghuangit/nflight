package com.tymeac.demo;

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
 *      doShut(args) - for a server shut down (requires a manual entry of password= tyclient) )
 * 
 * 4. End the virtual machine. Some callback handles use a separate thread for login
 *      so sometimes just a return will not end the JVM.
 * 
 * 5. In all three available routines:
 *      Try to get a login context passing the command line args, a boolean for 
 *          getting a LoginContext with (true) or without (false) a call back handler,
 *          and login,
 *             getContext(args, false/true);
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
public class DemoClientBase {
  
        // parm to Tymeac
        protected static TymeacParm TP = null;
        
        // return from Tymeac
        private static Object[] backArray = null;
        private static String   backString = null;
        
        // basic login context
        private static LoginContext loginContext = null;
        
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
  Object pass = x;
   
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
  doSync(args);

// 4.    
  // end
  System.exit(0);
                                      
} // end-method

/**
 * Try to get a login context without call back handler for security checking
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
  // try to get a login context without call back handler and login
  getContext(args, false);

//6.        
  // When not using security     
  if (loginContext == null) {
  
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
 * Try to get a login context without call back handler for security checking
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
  // try to get a login context without call back handler and login
  getContext(args, false);
  

//6.
    // When not using security     
  if (loginContext == null) {
  
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
 * Try to get a login context with call back handler for security checking.
 *   You must manually enter the password at the prompt: tyclient is the password. 
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
  // try to get a call back login context with call back handler and login
  getContext(args, true);

//6.  
  // When not using security     
  if (loginContext == null) {
  
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
      
        case 1:   backArray = new TySvrComm().syncRequest(TP);
                  return;
        
        case 2:   backArray = new TySvrComm().asyncRequest(TP);
                  return;
        
        case 3:   backString = new TySvrComm().shutRequest(force);
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
 *                  keyStoreAlias="tymeacclient"
 *                  keyStoreURL="file:config/tymeacclient.keystore"
 *                  keyStorePasswordURL="file:config/tymeacclient.password";
 *        };
 *       
 *        The last two entries define a keystore location and password location.
 *        Both are in the same "config" subdirectory. You may not edit the keystore or
 *        password but you may always configure your own. The password is: tyclient
 * 
 *  Do a login using the Login Context returned from above.
 *  
 */
private static void getContext(String[] args, boolean callback) {

//10.                      
  /*
   * There is one command line option
   *   -secure
   * When this option is present, we make a call
   * to get the login context.
   * 
   */   
  // When 1st arg is for security 
  if  ((args.length > 0) &&
       (args[0].equalsIgnoreCase("-secure"))) {
         
         // When normal
         if  (!callback) {
           
             // new logon Context without call back handler
             loginContext = SecurityModule.getBasicContext();             
         }
         else {
            // new logon Context with call back handler
             loginContext = SecurityModule.getCallbackContext();
           
       } // endif        
  } // endif
  
  // When a login context
  if  (loginContext != null) {
    
      // When NOT a good login
      if  (!SecurityModule.login(loginContext)) {
        
          // no security
          loginContext = null;
          
          System.out.println("Login failed");
          System.exit(1);
          
      } // endif      
  } // endif      

} // end-method 

} // end-class
