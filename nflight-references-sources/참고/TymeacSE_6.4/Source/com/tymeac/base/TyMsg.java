package com.tymeac.base;

/* 
 * Copyright (c) 2004 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * The String messages
 */
public final class TyMsg {

/**
 * All those messages
 * @param nbr int - the message number
 * @return java.lang.String
 */
public static String getMsg(int nbr) {

  // nbr is the msg number
  switch (nbr) {

// start up

    case 1: return 
      "Ty00001I Tymeac Server Initializing ";      
  
    case 3: return
      "Ty00003S Tymeac Server, No Configuration File found for start up";
  
    case 4: return
      "Ty00004I Tymeac Server Message Log for this session is ";
  
    case 5: return
      "Ty00005C Tymeac Server Logging is disabled for this session";
  
    case 6: return
      "Ty00006I Tymeac Server Statistics repository for this session is ";
  
    case 7: return
      "Ty00007C Tymeac Server Statistics are disabled for this session";
    
    case 8: return
      "Ty00008S Tymeac Server Class Loader Security Exception";
    
    case 9: return
      "Ty00009S Tymeac Server ";      
  
    case 11: return
      "Ty00011I Tymeac Server Notification Function for this session is " ;
  
    case 12: return
      "Ty00012C Tymeac Server Notification is disabled this session";
  
    case 14: return
      "Ty00014C Tymeac Server Monitor is not in use";
      
    case 15: return
      "Ty00015S Tymeac Server User Singleton getInstance() failure:";   
  
    case 16: return
      "Ty00016S Tymeac Server, Invalid agrument for start up: ";
  
    case 17: return
      "Ty00017I Tymeac Server RMI Security Manager failure:";
  
    case 18: return
      "Ty00018I Tymeac Server RMI Security Manager not in use"; 
  
    case 19: return
      "Ty00019S Tymeac Server failure opening configuration file:";
  
    case 20: return
      "Ty00020S Tymeac Server failure reading configuration file:";
  
    case 21: return
      "Ty00021S Tymeac Server failure closing configuration file:";
  
    case 22: return
      "Ty00022S Tymeac Server failure";
      
    case 23: return
      "Ty00023S Tymeac Server SQL Exception on DBMS Log Table: ";
  
    case 24: return
      "Ty00024S Tymeac Server failure";
  
    case 25: return
      "Ty00025S Tymeac Server SQL Exception on DBMS Statistics Table:";
  
    case 26: return
      "Ty00026S Tymeac Server configuration file data is in error:";
      
    case 27: return
      "Ty00027S Tymeac Server Internal Queue index position="; 
      
    case 28: return
      "Ty00028S Tymeac Server Internal Queue (";       
    
    case 29: return
      "Ty00029S Tymeac Server Queue index position=";
  
    case 30: return
      "Ty00030S Tymeac Server User Queue (";
  
    case 31: return
      "Ty00031S Tymeac Server Queue <";
      
    case 32: return
      "Ty00032S Tymeac Server TymeacUserVariables Class not found. Cannot continue";
  
    case 33: return
      "Ty00033S Tymeac Server ";
  
    case 34: return
      "Ty00034S Tymeac Server ";
    
    case 35: return
      "Ty00035S Tymeac Server ";
      
    case 36: return
      "Ty00036S Tymeac Server ";
    
    case 37: return
      "Ty00037S Tymeac Server Function:";
      
    case 38: return
      "Ty00038S Tymeac Server Function: <";
  
    case 39: return
      "Ty00039S Tymeac Server Queue (";
  
    case 40: return
      "Ty00040S Tymeac Server Queue (";
  
    case 41: return
      "Ty00041S Tymeac Server Queue (";

    case 42  : return  
          "Ty00042S Tymeac Server Function (";
          
    case 43  : return
          "Ty00043S Tymeac Server";

    case 44 : return    
          "Ty00044S Tymeac Server";
          
    case 45 : return
          "Ty00045S Tymeac Server an error occured in ";
    
    case 46 : return
          "Ty00046I Tymeac Server at least one Function generated a duplicate hash code. Using a String compare algorithm";
    
    case 47    : return
          "Ty00047S Tymeac Server an error occured in ";

    case 48: return
          "Ty00048S Tymeac Server Queue <";

    case 49: return
          "Ty00049S Tymeac Server Queue <";

    case 50  : return    
          "Ty00050S Tymeac Server Queue <";
  
    case 51  : return
          "Ty00051S Tymeac Server Queue <";

    case 52  : return
          "Ty00052C Tymeac Server User Exit Startup Class <";

    case 53  : return
          "Ty00053S Tymeac Server Alternatives Log Class <";
          
    case 55  : return
          "Ty00055S Tymeac Server Alternatives Statistics Class <"; 

    case 56  : return
          "Ty00056S Tymeac Server User Exit Startup Class <";

    case 57  : return
          "Ty00057S Tymeac Server User Exit Startup Class <";
    
    case 58  : return
          "Ty00058S Tymeac Server Activation start up and invalid inactivation minutes=";
          
    case 59: return
          "Ty00059I Tymeac Server New Notification Function for this session is " ;      

    case 60  : return    
          "Ty00060S Tymeac Server illogical data in TyVariables class, Cannot continue";
    
    case 61  : return    
          "Ty00061S Tymeac Server illogical data in TyVariables class, Cannot continue";    
    
    case 62  : return    
          "Ty00062I Tymeac Server Start-up Function return code=";   
    
    case 63  : return    
          "Ty00063I Tymeac Server Start-up Function call failed with invalid return object, Function=";    
    
    // see also 74, below
    case 70  : return    
          "Ty00070I Tymeac Server Rebinding to RMI Registry";    
    
    case 71  : return    
          "Ty00071I Tymeac Server Rebinding to JNDI ";   
          
    case 72   : return
          "Ty00072C Tymeac Server Alternatives Log Class <"; 
    
     case 73: return
          "Ty00073C Tymeac Server Alternatives Statistics Class <"; 
     
     // same as 70 but using the login remote object
     case 74  : return    
     "Ty00074I Tymeac Server RMI Login rebinding variable: ";    

    
    
    case 80  : return    
          "Ty00080I Tymeac Server using INSECURE startup";
    
    case 81  : return    
          "Ty00081I Tymeac Server using secure startup";  
          
    case 82  : return    
          "Ty00082S Tymeac Server loginContext, LoginException= ";      
     
    case 83  : return    
          "Ty00083S Tymeac Server loginContext, SecurityException= "; 
     
    case 84  : return    
          "Ty00084S Tymeac Server loginContext, Exception= ";
          
    case 85  : return         
          "Ty00085S Tymeac Server LoginException= ";
          
    case 86  : return    
          "Ty00086S Tymeac Server PrivilegedActionException= "; 
     
     case 88  : return    
          "Ty00088S Tymeac Server getEntry for Exporter error= ";                     
          
     case 89  : return    
          "Ty00089S Tymeac Server exporting error= ";                     
               
     case 90  : return    
          "Ty00090S Tymeac Server Custom Socket Factory Exception= ";            
          
     case 91  : return    
          "Ty00091S Tymeac Server Login failed ";   

            
    
    case 97  : return
          "Ty00097S Tymeac Server Java exception=";

    case 98  : return
          "Ty00098S Tymeac Server initialization failed";

    case 99  : return
          "Ty00099I Tymeac Server ";

    case 101 : return
          "Ty00101I Tymeac Server Notification is not in use";
    
    case 103 : return
          "Ty00103S Tymeac Server number of Queues is zero";

    case 110 : return
          "Ty00110S Tymeac Server SQLException Message 1: ";

    case 115 : return
          "Ty00115S Tymeac Server Java Exception: ";
    
    case 120 : return
          "Ty00120S Tymeac Server startup arguments error: directory name missing ";        
    
    case 125 : return
          "Ty00125S Tymeac Server startup arguments error: filename name missing ";        
      
    case 130 : return
          "Ty00130S Tymeac Server startup arguments error: port number missing "; 
    
    case 131 : return
          "Ty00131S Tymeac Server startup arguments error: shut down db name missing ";   
      
    case 132 : return
          "Ty00132S Tymeac Server startup arguments error: port number not a number "; 
    
    case 135 : return
          "Ty00135S Tymeac Server startup arguments error: Standalone mode is mutex with either a directory or filename";
    
    case 140 : return
          "Ty00140S Tymeac Server startup arguments error: configuration component name missing "; 
             
           

    case 210 : return
          "Ty00210S Tymeac Server SQLException Message 1: "; 

    case 215 : return
          "Ty00215S Tymeac Server Exception: "; 
          
          

    case 305 : return
          "Ty00305C Tymeac Server ";

    case 315 : return
          "Ty00315S Tymeac Server SQLException Message 1: "; 

    case 320 : return
          "Ty00320S Tymeac Server Java Exception: ";         
          
          
       
// Jini & Configuration startup

    case 1003  : return    
          "Ty01003S Tymeac Server No command line arguments";

    
    case 1005  : return    
          "Ty01005S Tymeac Server No configuration (-config) argument";

    case 1010  : return    
          "Ty01010S Tymeac Server No configuration parms after -config";                
                    
    case 1015  : return    
          "Ty01015S Tymeac Server ConfigurationProvider error= ";                 

    case 1020  : return    
            "Ty01020S Tymeac Server Configuration getEntry for Directory error= ";

    case 1025  : return    
          "Ty01025S Tymeac Server Configuration getEntry for File error= ";  

    case 1030  : return    
          "Ty01030S Tymeac Server Configuration getEntry for Stand Alone error= ";  

    case 1040  : return    
          "Ty01040S Tymeac Server Configuration getEntry for Verbose error= "; 
    
    case 1041  : return    
          "Ty01041S Tymeac Server Configuration getEntry for Embedded database shutdown error= "; 
          
    case 1045  : return    
          "Ty01045S Tymeac Server Configuration -- No ConfigurationProvider";
          
    case 1050 : return    
          "Ty01050S Tymeac Server Configuration getEntry error= ";  
  
    case 1055 : return    
          "Ty01055S Tymeac Server Discovery Manager failure= "; 

    case 1060 : return    
          "Ty01060S Tymeac Server Join Manager failure= ";
    
    case 1065 : return 
          "Ty01065S Tymeac Server No application entry, ";
    
    case 1070 : return 
          "Ty01070S Tymeac Server Activation creation, unbalanced properties";
    
    case 1075 : return 
          "Ty01075S Tymeac Server Activation creation, Error creating Marshalled Object: ";
         
    case 1080 : return 
          "Ty01080I Tymeac Server Created Tymeac Activatable environment successfully";
    
    case 1081 : return 
          "Ty01081S Tymeac Server Activatable environment creation failure";
              
    case 1085 : return 
          "Ty01085S Tymeac Server Activatable RMI Registry failure= ";         
    
    case 1090 : return 
          "Ty01090S Tymeac Server Activatable environment create Exception= ";         
    
    case 1095 : return 
          "Ty01095I Tymeac Server Activatable Jini Service shut down"; 
         
    case 1100 : return 
          "Ty01100I Tymeac Server Activatable Jini Service created successfully, waiting for a shut down";        
         
    case 1105 : return 
          "Ty01105S Tymeac Server Activatable RMI Naming failure= ";       
    
    case 1110 : return 
          "Ty01110I Tymeac Server Activatable Tymeac Join Manager Terminated";     
    
    case 1115 : return 
          "Ty01115I Tymeac Server Activatable Internal Join Manager Terminated";       
    
     
         
          
// shut down

    case 2001  : return
            "Ty02001I Tymeac Server shutting down";

    case 2009  : return
            "Ty02009I Tymeac Server shut down complete for MilliTime="; 
    
    case 2010  : return
            "Ty02010I Tymeac Server shut down completed by force for MilliTime="; 
    
    case 2020  : return
          "Ty02020I Tymeac Server User Exit Shutdown Class <";

    case 2021: return
          "Ty02021C Tymeac Server User Exit Shutdown Class <";

    case 2022: return
          "Ty02022C Tymeac Server User Exit Shutdown Class <";

    case 2025  : return
          "Ty02025I Tymeac Server Remote exception= ";

    case 2026  : return
          "Ty02026I Tymeac Server Activation exception= ";
          
    case 2027  : return
          "Ty02027I Tymeac Server unknown object exception= ";          

// Monitor

    case 3005  : return
          "Ty03005C Tymeac Server Stalled Asynchronous Requests are pending action";

    case 3013  : return
          "Ty03013I Tymeac Server Monitor interval is ";

    case 3015  : return
            "Ty03015I Tymeac Server New Stall Table entry: "; 

    case 3019  : return
            "Ty03019I Tymeac Server a thread is disabled in Queue ("; 
    
    case 3020  : return
            "Ty03020I Tymeac Server All threads are disabled in Queue: "; 

    case 3025  : return
            "Ty03025I Tymeac Server All threads are disabled in Queue: ";

    case 3098  : return
            "Ty03098I Tymeac Server unregister complete for MilliTime="; 
            
    case 3099  : return
            "Ty03099I Tymeac Server inactivation complete for MilliTime="; 

    case 3100  : return
            "Ty03100I Tymeac Server unknown object exception= ";

    case 3105  : return
            "Ty03105I Tymeac Server Remote exception= ";

    case 3110  : return
            "Ty03110I Tymeac Server Activation exception= ";

    case 3115  : return
            "Ty03115I Tymeac Server Logging NO longer in use from exception="; 

    case 3120  : return
            "Ty03115I Tymeac Server Statistics NO longer in use from exception="; 
    
    case 3201  : return
            "Ty03201I Tymeac Server activatable shut down may be stalled"; 
    
    case 3202  : return
            "Ty03202I Tymeac Server activatable shut down stage 2 may be stalled"; 
    
    case 3901  : return
            "Ty03901S Tymeac Server Unexpected Shut Down from Monitor uncaught exception="; 


// Request Brokering
    
    case 4010  : return
            "Ty04010C Tymeac Server 'TymeacInternalCancel' Function is missing, CallBacks non-functioning."; 
    
    case 4020  : return
            "Ty04020C Tymeac Server 'TymeacInternalCancel' Allocate new Request Detail failed for CallBack."; 

    
// Scheduling
    
    case 5010  : return
            "Ty05010C Tymeac Server 'TymeacInternalCancel' Queue failed scheduling. Reason="; 

    

// Queue Thread

    case 6005  : return
            "Ty06005S Tymeac Server Thread ";

    case 6010  : return
            "Ty06010S Tymeac Server Thread ";        

    case 6015  : return
            "Ty06015S Tymeac Server Thread ";

    case 6020  : return
            "Ty06020S Tymeac Server Thread ";

    case 6025  : return
            "Ty06025S Tymeac Server Thread ";            

    case 6026  : return
            "Ty06026I Tymeac Server Request=";
    
    case 6090  : return
            "Ty06090S Tymeac Server Thread "; 
    
    case 6901  : return
            "Ty06901S Tymeac Server Thread ";  


// Output Agent Thread

    case 7005  : return
            "Ty07005S Tymeac Server Thread ";

    case 7010  : return
            "Ty07010S Tymeac Server Thread ";         

    case 7015  : return
            "Ty07015S Tymeac Server Thread ";

    case 7020  : return
            "Ty07020S Tymeac Server Thread ";
    
    case 7090  : return
            "Ty07090S Tymeac Server Thread "; 
    
    case 7901  : return
            "Ty07901S Tymeac Server Thread ";  
            

// General Tymeac Component
    
    case 8001  : return
            "Ty08001I Tymeac Server Notification Disabled, returned async=";  
    
    case 8101  : return
            "Ty08101I Tymeac Server Logging re-verified";  
    
    case 8102  : return
            "Ty08102I Tymeac Server New Logging environment verified";  
    
    case 8201  : return
            "Ty08201I Tymeac Server Statistics re-verified"; 
    
    case 8301  : return
            "Ty08301I Tymeac Server Notification function re-verified"; 
    
    
    
// User Thread modification    

    case 9001  : return
            "Ty09001I Tymeac Server Thread ";
    
    default: return
      " Tymeac Server *invalid* TyMsg.getMsg(" + nbr + ") number in () is in error. Call your local programming support";   

  } // end-switch 

} // end-method

/**
 * All those text fields
 * @param nbr int - the message number
 * @return java.lang.String
 */
public static String getText(int nbr) {

  // nbr is the msg number
  switch (nbr) {

    case 1: return
      "Defining error: ";
      
    case 2: return
      "Error=";
   
    case 3: return
      ", has no value (null), Start up cannot continue";
   
    case 4: return
      "), is a duplicate, Cannot continue";
  
    case 5: return
      "), is a duplicate with the User Queue, Cannot continue";
   
    case 6: return
      ">, Class <";
  
    case 7: return
      ">, failed loadClass, Cannot continue";
   
    case 8: return  
      " Number of queues is zero,  Cannot continue";
  
    case 9: return
      ">, this Queue: <";
  
    case 10: return
      ">, does not exist Cannot continue"; 
    
    case 11: return
       "), number of threads invalid, Cannot continue";
  
    case 12: return
      "), number of waitlists invalid, Cannot continue";
    
    case 13: return        
      "), number of waitlist entries invalid, Cannot continue";

    case 14  : return  
          ") Queue (";

    case 15  : return  
          ") is not an Output Agent Queue, Cannot continue";

    case 16  : return
          "), is a duplicate, Cannot continue";
    
    case 17  : return
          "), is a duplicate with a user function, Cannot continue";

    case 18  : return    
          "), has no value Setup Cannot continue";
    
    case 19: return
          ">, Class <";

    case 20: return
          ">, Invalid format, Cannot continue";

    case 21: return
          ">, Class::URL <";

    case 22: return
          ">, Invalid URL format, Cannot continue";

    case 23  : return    
          ">, Security Exception, Cannot continue";

    case 24  : return
          ">, No valid PAC signiture, Cannot continue";
    
    case 25  : return
          "ClassNotFoundException";
    
    case 26  : return
          "InstantiationException";
    
    case 27  : return
          "IllegalAccessException"; 
    
    case 28: return
          ">, Invalid format";

    case 29: return
          ">, Invalid URL format";
    
    case 30  : return    
          " on port=";
    
    case 31  : return    
          "com.tymeac.TymeacServer";

    case 32 : return
          ">, failed loadClass";

    case 33 : return  
          ">, Security Exception";

    case 34 : return
          " Cannot continue";

    case 35 : return
          ">, No valid PAC signiture";
    
    case 36 : return                          
          "Function: <";

    case 37 : return
          ">, expected number of Queues: ";

    case 38 : return  
          ", Failed read on number; ";

    case 39 : return
          ", in Table <";

    case 40 : return
          "> Function disabled"; 

    case 41 : return
          ">, this Queue: <";

    case 42 : return
          ">, does not exist  ";
    
    case 43 : return
          "local file=";
  
    case 44 : return
          "NOT in use";

    case 45 : return
          "DBMS Table=";
    
    case 46 : return
          "SQLException:";

    case 47 : return
          "SQLState: ";

    case 48 : return
          "Message:  ";

    case 49 : return
          "Vendor:   "; 

    case 50 : return
          "Exception: ";

    case 51: return
          "Alternative Class=";
          
    case 52 : return
          "yes"; 
    
    case 53 : return
          "tymeac.xml";          
          
    case 54 : return
          "-port";    
    
    case 55 : return
          "no";          

    case 56 : return
          "-no";
    
    case 57 : return
          "-file";

    case 58 : return
          "-dir"; 

    case 59 : return
          "-s";

    case 60 : return
          "-config";
      
    case 61 : return
          "dir";
          
    case 62 : return
          "filename";          
  
    case 63 : return
          "standalone";        
          
    case 64 : return
          "verbose";
          
    case 65 : return
          "in stand alone mode";    
    
    case 66 : return
          "in DBMS mode";
          
    // see also 162, using login
    case 67 : return  
           "RMI rebinding failure: ";    
    
    case 68 : return  
           "JNDI rebinding failure: ";   
    
    case 69 : return     
          " local log file ";            
    
    case 70 : return     
          " on alternatives log class, write() "; 
          
    case 71 : return 
          "Tymeac SD(0000) All threads are inactive, shutdown complete, duration=";
          
    case 72 : return 
          "Tymeac SD(2030) Prior shutdown completed normally, awaiting termination";      
          
    case 73 : return 
          "Tymeac SD(2010) Shutdown forced past active threads, awaiting termination, duration=";   
    
    case 74 : return
          "discoveryManager";
          
    case 75 : return
          "myName";      
          
    case 76 : return
          "com.tymeac.jini.JiniServer";        
          
    case 77 : return
          "info";
          
    case 78 : return
          "Non Activatable Jini Tymeac Server";
          
    case 79 : return
          "CoopSoft.com";
          
    case 80 : return
          "Cooperative Software Systems, Inc.";
          
    case 81 : return
          "5.5";
          
    case 82 : return
          "";
          
    case 83 : return
          "";

    case 84  : return
            " Threads still active, try again later"; 
    
    case 85  : return
          "> Error=";
    
    case 86  : return
          " seconds";

    case 87  : return
          ", with non-activity deactivation in ";

    case 88  : return
          " minutes"; 
    
    case 89  : return
            " in Queue ";

    case 90  : return
            " disabled by IllegalAccessException";

    case 91  : return          
            " disabled by InvocationTargetException"; 

    case 92  : return        
            " disabled by IllegalArgumentException"; 

    case 93  : return
            " disabled by ThrownException ";
            
    case 94  : return
            ", Request=";

    case 95  : return
            ", is stalled";

    case 96  : return
            ", Output Agent is ";
    
    case 97 : return
            " disabled by user modification";

    case 98 : return
            "> ClassNotFoundException";
    
    case 99 : return
            "> InstantiationException";
            
    case 100 : return
            "> IllegalAccessException";
              
    case 101 : return
              " NO longer in use from exception=";
              
    case 102 : return
              " Threads still active, try again later"; 
     
    case 103 : return
              "TymeacUserQueues";
    
    case 104 : return
              "TymeacInternalQueues";        
                
    case 105 : return       
              " Class, number of Queues is zero. Cannot continue";
    
    case 106 : return  
              "Class not found";
              
    case 107 : return  
              "TymeacUserFunctions";
                        
    case 108 : return  
              "TymeacInternalFunctions";
    
    case 109 : return  
              "number of functions is zero";
         
    case 110 : return  
              " User Function (";
                       
    case 111 : return  
              " Internal Function (";
    
    case 112 : return  
              "  User Function position=";
    
    case 113 : return  
              "  Internal Function position=";
    
    case 114 : return  
              "  TymeacUserQueue Class, Error=";
    
    case 115 : return  
              "  TymeacInternalQueue Class, Error=";
    
    case 116 : return  
              "  TymeacUserFunction Class, Error=";
    
    case 117 : return  
              "  TymeacInternalFunction Class, Error=";
    
    case 118 : return  
              " Output Agent Scheduling failure=";                 
   
    case 119  : return
          "> Exception=";
    
    case 120  : return
          "exporter";      
     
    case 121  : return
          "loginContext";   
    
    case 122  : return 
          "applProgram";
    
    case 123  : return 
          "location";
    
    case 124  : return     
          "properties";
    
    case 125  : return   
          "options";
    
    case 126  : return 
          "actSysPreparer";
    
    case 127  : return 
          "com.tymeac.jini.base.JiniActivationCreate";
    
    case 128  : return 
          "com.tymeac.jini.JiniTymeacActivationImpl";
    
    case 129  : return
          "Activatable Jini Tymeac Server";
          
    case 130  : return
          "CoopSoft.com";
          
    case 131  : return
          "Cooperative Software Systems, Inc.";
          
    case 132  : return
           "5.0";
           
    case 133  : return 
           "";
           
    case 134  : return 
           ""; 
    
    case 135  : return 
          "com.tymeac.jini.JiniActivationCreate2";
    
    case 136  : return
          "Jini Tymeac Activatable Service lease renewer";
          
    case 137  : return
          "CoopSoft.com";
          
    case 138  : return
          "Cooperative Software Systems, Inc.";
          
    case 139  : return
           "5.0";
           
    case 140  : return 
           "";
           
    case 141  : return 
           ""; 
    
    case 142  : return 
           "jiniName"; 
    
    case 143  : return 
           "jiniInfo"; 
    
    case 144  : return 
           "selfName"; 
    
    case 145  : return 
           "selfInfo"; 
    
    case 146  : return 
            "*--- No Queues have attendant threads ---*";
    
    case 147  : return 
            "/com/tymeac/base";
            
    case 148  : return 
            ") - Never used / existent";
    
    case 149  : return 
            ") - Never used / null";
    
    case 150  : return 
            ") - Inactive / existent";
    
    case 151  : return 
            ") - Inactive / null";
    
    case 152  : return 
            ") - Waiting for work";
    
    case 153  : return 
            ") - Thread processing";
    
    case 154  : return 
            ") - In application Class";
    
    case 155  : return 
            ") - Scheduling Output Agent";
    
    case 156  : return 
            ") - Notified, awaiting execution";
    
    case 157  : return             
            ") - Reactivated, awaiting execution";
    
    case 158  : return 
            ") - Cancelled, reason = ";
    
    case 159 : return 
            ") - Disabled, reason is: Ty000000";
    
    case 160  : return 
            ") - Disabled, reason is: Ty0";
    
    case 161  : return 
            "com.tymeac.serveruser.TyServerRMISecMgr";
    
    // same as 67 but using the login server
    case 162 : return  
            "RMI Login Remote Object rebinding failure: ";  
    
    case 163 : return
            "Excessive Thread Processing Time";
    
    case 164 : return
            "Excessive Appl Processing/Default Time";
    
    case 165 : return
            "Excessive Appl Processing/Specific Time";
    
    case 166 : return
            "Excessive Thread Scheduling Time";
    
    case 167 : return
            "Excessive Posting Interval";
    
    case 168 : return
            "Excessive Thread Started Interval";
    
    case 169 : return
            "No longer cancelled";  
    
    case 170 : return
            ", from cancelled = ";
    
    case 171 : return
            "-EDBSD";
    
    case 172 : return
            "edbsd";
    
    case 173  : return  
          ") is not an Normal Queue, Cannot continue";
    
     case 174  : return  
          " initialized successfully, MilliTime=";
     
     case 175 : return
          "SQLException on close of connection during startup: ";
     
     case 176 : return
          " Expunged ";
     
     case 177 : return 
          "Tymeac SD(2040) Shutdown Stage 1 incomplete, try again later"; 
     
     case 178 : return 
          "Tymeac SD(2050) Shutdown Stage 2 incomplete, should complete shortly"; 
     
     case 179 : return 
          " Failed with uncaught Exception="; 
     
     case 180 : return 
          ", for Function="; 
     
     case 181 : return 
          ", duration="; 
            
    default: return
      " *invalid* TyMsg.getText(" + nbr + ") number in () is in error. Call your local programming support.";   
  } // end-switch
      
} // end-method       
} // end-class
