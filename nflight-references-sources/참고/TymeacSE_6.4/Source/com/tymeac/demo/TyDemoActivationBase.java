package com.tymeac.demo;

/* 
 * Copyright (c) 2002 - 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.io.IOException;
import java.util.Properties;
import java.rmi.*;
import java.rmi.activation.*;
import com.tymeac.common.*;
import com.tymeac.base.*;
/**
 * Tymeac Demonstration System. 
 *
 *  This Class is an example of how to prepare and register the Tymeac Server
 *    activatable remote object.
 *
 *
 */

public class TyDemoActivationBase {   

  // the file separator character
  private String file_sep;

  // the path separator character
  @SuppressWarnings("unused")
  private String path_sep;
  
  // the file directory
  private String dir;
  
  // tymeac home location
  private String ty_home;
  
  // *--- properties key / value ---*
  
    //  security policy key/value 
    private String sec_key;
    private String sec_value; 
    
    //  classpath key/value
    private String cp_key;
    private String cp_value;
    
    // sec manager key/value
    private String sec_mgr_key;
    private String sec_mgr_value;
             
    // login key/value
    private String login_key;
    private String login_value;
    
    // truststore key/value
    private String trust_key;
    private String trust_value;
  
  // location of the Tymeac Activation Class
  private String location;
  
  // Marshalled Object
  MarshalledObject data = null;   
  
  // Tymeac variables definitions
  private TyVariables TyV;
  
  // Tymeac String names
  private String tymeac_url;
  private String tymeac_name;

/**
 * Constructor -
 *   Fill in the String values for your installation
 * 
 */   
public TyDemoActivationBase() {
  
  // the file separator character
  file_sep = System.getProperties().getProperty("file.separator");

  // the path separator character
  path_sep = System.getProperties().getProperty("path.separator");
  
  // file directory 
  dir = "c:";
  
  // tymeac home location
  ty_home = dir 
            + file_sep 
            + "eclipse" 
            + file_sep 
            + "usr" 
            + file_sep 
            + "workspace"
            + file_sep 
            + "TymeacSE"
            + file_sep 
            + "bin";
  
  // *--- properties key / value ---*
  
    //  security policy key/value 
    sec_key = "java.security.policy";
    sec_value = ty_home 
                + file_sep 
                + "security" 
                + file_sep 
                + "policy.all";      
    
    //  classpath key/value
    cp_key = "java.class.path";
    cp_value = ty_home; 
        
    // security manager key/value 
    sec_mgr_key = "java.security.manager";
    sec_mgr_value = "";
       
    // login key/value
    login_key = "java.security.auth.login.config=";
    login_value = ty_home 
                  + file_sep 
                  + "config"
                  + file_sep
                  + "tymeac-activatable-ssl-server.login";
    
    // truststore key/value
    trust_key = "javax.net.ssl.trustStore=";
    trust_value = ty_home 
                  + file_sep 
                  + "config"
                  + file_sep 
                  + "tymeac.truststore";     
     
  
  // location of the Tymeac Activation Object
  //    your o/s may require additional "/", i.e. file:// or file:/// 
//  location = "file:/" + ty_home + "/";
  //location = "file:/" + "c:\\Documents and Settings\\eharned\\eclipse\\workspace\\TymeacSE\\com\\tymeac\\demo" + "/";  
   location = "file://" + "c:\\usr\\elclipse\\workspace\\TymeacSE\\bin\\com\\tymeac\\demo" + "/";  

  // marshalled objects for passing data to the Tymeac Server constructor
  // these are string[]
      
      // no marshalled data default
      //MarshalledObject data = null;
      
      // marshalled object for a database with only a directory input
      //String[] my_data = new String[2];
    
      //my_data[0] = "-dir";
      //my_data[1] = dir;
    
      // marshalled object with embedded database shutdown and stand alone option
      //String[] my_data = new String[3]; 
      //my_data[0] = "-s";
      //my_data[1] = "-edbsd";
      //my_data[2] = "com.tymeac.serveruser.DummyEmbeddedShutdown";
  
  // marshalled object with only a stand alone option
  String[] my_data = new String[1]; 
  my_data[0] = "-s";  
  
   
  try {        
  // set a marshalled Object
  data = new MarshalledObject<Object>(my_data);
  
   } // end-try 
  
  catch (IOException e) {
  System.out.println("Error creating Marshalled Object: " +
  e.getMessage()); 
  
  System.exit(0);
          
  } // end-catch            
  
  // Tymeac variables definitions
  TyV = new TyVariables();
  
  // Tymeac String names
  tymeac_url  = TyV.getUrl();
  tymeac_name = TyV.getTymeac(); 
  
} // end-constructor

/**
 * Create the backend Tymeac Server Remote Object
 * @return TymeacInterface      
 */
public TymeacInterface createBackend()
     throws IOException {    
    
  // create and register the Remote Object   
  try {
    // new properties object             
    Properties props = new Properties();
      
      // security policy property                              
      props.put(sec_key, sec_value);
        
      // classpath property                              
      props.put(cp_key, cp_value);
      
      // security mgr property                              
      props.put(sec_mgr_key, sec_mgr_value);
        
      // login property                              
      props.put(login_key, login_value);
      
      // truststore
      props.put(trust_key, trust_value);
      
    // do not use a command env
    ActivationGroupDesc.CommandEnvironment ace = null; 
      
    // new group
    ActivationGroupDesc group = new ActivationGroupDesc(props, ace);
      
    // Register the group and get the ID.
    ActivationGroupID gid = 
      ActivationGroup.getSystem().registerGroup(group); 
        
    // Now create the group
    ActivationGroup.createGroup(gid, group, 0);
    
    System.out.println(location);
    
    // Create an activation descriptor
    ActivationDesc desc = new ActivationDesc( tymeac_name,  // name of the class 
                          location, // dasd storage location of the class
                          data);  // parms class needs to initialize 
    
    
    //System.out.println("registering " + tymeac_name + " as activatable.");
       
    // Create the 'backend' object 
    return (TymeacInterface) Activatable.register(desc);
                  
  } // end-try 
    
  catch (RemoteException ex1) {
    System.err.println("Error creating backend object: " +
    ex1.getMessage()); 
                    
  } // end-catch
     
  catch (ActivationException ex2) {
    System.err.println("Problem with activation: " + ex2.getMessage());
    ex2.printStackTrace();  
      
  } // end-catch 
  
  // no good
  return null;
  
} // end-method

/**
 * Register the backend Tymeac Server Remote Object with the
 *    RMI Registry
 * @param backend TymeacInterface - the backend server
 * @return int 
 */
public int registerWithRMI(TymeacInterface backend) {    
             
  // rebind the TymeacInterface Remote Object to the RMI Registry 
  try {
     // Naming 
     Naming.rebind(tymeac_url + tymeac_name, // name to use 
             backend);                 // remote object
         
    return 0;
         
  } // end-try
              
  catch (Exception ex3) {
    System.out.println("Rebinding failed");
    ex3.printStackTrace();    
                         
  } // end-catch
             
  // all done
  return 1;                        
       
} // end-method 
} // end-class
