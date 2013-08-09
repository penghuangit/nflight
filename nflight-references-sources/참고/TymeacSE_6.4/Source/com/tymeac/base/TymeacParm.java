package com.tymeac.base;

/* 
 * Copyright (c) 2004 - 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */
 
/**
 * This is the parameter passed to the Tymeac Server from Clients.
 */ 
public final class TymeacParm 
   implements java.io.Serializable  {

  static final long serialVersionUID = 5068699085997401882L;
  
  // input data for your invoked method
  private Object input = null;
  
  // Tymeac Function name       
  private String func_name = null;   
  
  // maximum time, in seconds, to wait for a reply when using a synRequest()
  private int wait_time = 0;   
  
  // maximum time, in milliseconds, to wait for a reply when using a syncRequest()
  private int wait_millis = 0; 
  
  // priority of the request
  private int priority = 1;    
  
  // callback Object for cancelling syncReq() as of 4.0
  private InterruptionReady callback = null;    
  
  // used in place of callback as of 4.0.1  
  private long cancel = 0;  

/**
 * This constructor loads the private instance fields from the passed
 *    parameters.   
 *      <br>c_in is the input for the function.
 *      <br>c_func is the Tymeac Function.
 *      <br>c_interval is the interval in seconds to wait for a reply.
 *      <br>c_pri is the priority for the request.  
 *
 * @param c_in java.lang.String
 * @param c_func java.lang.String
 * @param c_interval int
 * @param c_pri int
 * 
 */ 
public TymeacParm ( Object c_in,      // data for your invoked method 
                    String c_func,    // Tymeac Function name
                    int c_interval, // max wait time, in seconds
                    int c_pri) {      // priority of request             
             
    // parameters to instance fields                
    input       = c_in;
    func_name   = c_func;
    wait_time   = c_interval;
    priority    = c_pri;
                    
} // end-constructor

/**
 * Constructor 2, with interval addition in milliseconds. For a syncRequest() the c_millis
 *   field is added to the c_interval field (* 1000) to determine the maximum time
 *   in milliseconds the reqeust will wait for all queues to finish processing.
 *   <br>c_in is the input for the function.
 *    <br>c_func is the Tymeac Function.
 *    <br>c_interval is the interval in seconds to wait for a reply.
 *    <br>c_millis is the interval in milliseconds to wait for a reply.
 *    <br>c_pri is the priority for the request.  
 *
 * @param c_in java.lang.Object
 * @param c_func java.lang.String
 * @param c_interval int
 * @param c_millis int
 * @param c_pri int
 */
public TymeacParm(Object c_in, 
                  String c_func,
                  int c_interval, 
                  int c_millis, 
                  int c_pri) {

  // parameters to instance fields                
    input       = c_in;
    func_name   = c_func;
    wait_time   = c_interval;
    wait_millis = c_millis;
    priority    = c_pri;

} // end-constructor

/**
 * Constructor 3, adds call back.
 *    <br>c_in is the input for the function.
 *    <br>c_func is the Tymeac Function.
 *    <br>c_interval is the interval in seconds to wait for a reply.
 *    <br>c_millis is the interval in milliseconds to wait for a reply.
 *    <br>c_pri is the priority for the request.  
 *    <br>c_callback is the InterruptionReady object for callback
 * 
 *
 * @param c_in java.lang.Object
 * @param c_func java.lang.String
 * @param c_interval int
 * @param c_millis int
 * @param c_pri int
 * @param c_callback InterruptionReady
 */
public TymeacParm(Object c_in, 
                  String c_func,
                  int c_interval, 
                  int c_millis, 
                  int c_pri,
                  InterruptionReady c_callback ) {
                    
  // parameters to instance fields                
    input       = c_in;
    func_name   = c_func;
    wait_time   = c_interval;
    wait_millis = c_millis;
    priority    = c_pri;
    callback    = c_callback;

} // end-constructor

/**
 * Constructor 4 adds call back
 *    <br>c_in is the input for the function.
 *    <br>c_func is the Tymeac Function.
 *    <br>c_interval is the interval in seconds to wait for a reply.
 *    <br>c_pri is the priority for the request.  
 *    <br>c_callback is the InterruptionReady object for callback.
 * 
 *
 * @param c_in java.lang.String
 * @param c_func java.lang.String
 * @param c_interval int
 * @param c_pri int
 * @param c_callback InterruptionReady
 * 
 */ 
public TymeacParm ( Object c_in,    // data for your invoked method 
                    String c_func,  // Tymeac Function name
                    int c_interval, // max wait time, in seconds
                    int c_pri,      // priority of request 
                   InterruptionReady c_callback) { // callback object for cancelSyncReq()           
            
    // parameters to instance fields                
    input       = c_in;
    func_name   = c_func;
    wait_time   = c_interval;
    priority    = c_pri;
    callback    = c_callback; 
                    
} // end-constructor

/**
 * Constructor 5 adds cancel word
 *    <br>c_in is the input for the function.
 *    <br>c_func is the Tymeac Function.
 *    <br>c_interval is the interval in seconds to wait for a reply.
 *    <br>c_millis is the interval in milliseconds to wait for a reply.
 *    <br>c_pri is the priority for the request.  
 *    <br>c_cancel is the long integer for callback.
 * 
 *
 * @param c_in java.lang.String
 * @param c_func java.lang.String
 * @param c_interval int
 * @param c_pri int
 * @param c_cancel cancel word
 * 
 */ 
public TymeacParm ( Object c_in,    // data for your invoked method 
                    String c_func,  // Tymeac Function name
                    int c_interval, // max wait time, in seconds
                    int c_pri,      // priority of request 
                    long c_cancel) { // cancel word for cancelSyncReq()           
            
    // parameters to instance fields                
    input       = c_in;
    func_name   = c_func;
    wait_time   = c_interval;
    priority    = c_pri;
    cancel      = c_cancel; 
                    
} // end-constructor

/**
 * Constructor 6 adds cancel word
 *    <br>c_in is the input for the function.
 *    <br>c_func is the Tymeac Function.
 *    <br>c_interval is the interval in seconds to wait for a reply.
 *    <br>c_millis is the interval in milliseconds to wait for a reply.
 *    <br>c_pri is the priority for the request.  
 *    <br>c_cancel is the long integer for callback.
 * 
 * @param c_in
 * @param c_func
 * @param c_interval
 * @param c_millis
 * @param c_pri
 * @param c_cancel
 */
public TymeacParm ( Object c_in,    // data for your invoked method 
                    String c_func,  // Tymeac Function name
                    int c_interval, // max wait time, in seconds
                    int c_millis, 
                    int c_pri,      // priority of request 
                    long c_cancel) { // cancel word for cancelSyncReq()           
            
    // parameters to instance fields                
    input       = c_in;
    func_name   = c_func;
    wait_time   = c_interval;
    wait_millis = c_millis;
    priority    = c_pri;
    cancel      = c_cancel; 
                    
} // end-constructor

/**
 * Get the callback object
 * @return InterruptionReady
 */
public InterruptionReady getCallback () { return callback; } // end-method

/**
 * Get the cancel word
 * @return long when using a cancel
 */
public long getCancelWord () { return cancel; } // end-method

/**
 * Get the function name
 * @return java.lang.String
 */
public String getFuncname () { return func_name; } // end-method

/**
 * Get the input data object
 * @return Object
 */
public Object getInput () { return input; } // end-method

/**
 * Get the wait time in milliseconds
 * @return int
 */
public int getMillis() { return wait_millis; } // end-method

/**
 * Get the priority
 * @return int
 */
public int getPriority ( ) { return priority; } // end-method

/**
 * Get the wait time in seconds
 * @return int
 */
public int getTime ( ) { return wait_time; } // end-method

/** 
 * Set the callback object
 * @param O InterruptionReady The Object to set.
 */
public void setCallback (InterruptionReady O) { callback = O; } // end-method

/** 
 * Set the cancel word
 * @param L long to set.
 */
public void setCancel (long L) { cancel = L; } // end-method

/**
 * Set the function name
 * @param S java.lang.String The function name to set.
 */
public void setFuncname (java.lang.String S) { func_name = S; } // end-method

/** 
 * Set the input object
 * @param O Object  The input to set.
 */
public void setInput (Object O) { input = O; } // end-method

/**
 * For a syncRequest() the c_millis field is added to the wait interval field (* 1000)
 *    to determine the maximum time in milliseconds the reqeust will wait
 *    for all queues to finish processing. 
 * @param c_millis int set additional wait time in milliseconds
 */
public void setMillis(int c_millis) { wait_millis = c_millis; } // end-method

/**
 * Set the priority
 * @param P int  The priority of the request to set.
 */
public void setPriority (int P) { priority = P; } // end-method

/** 
 * Set the wait time in seconds
 * @param T int  The maximum wait time to set.
 */
public void setTime (int T) { wait_time = T; } // end-method

} // end-class
