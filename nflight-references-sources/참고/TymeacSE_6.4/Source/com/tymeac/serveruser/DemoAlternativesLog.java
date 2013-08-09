package com.tymeac.serveruser;
/**
 * Tymeac Demonstration System. 
 *   called alternatives exit for logging
 *
 *
 */

public class DemoAlternativesLog
      implements com.tymeac.base.TymeacAlternativesInterface {  

/**
 * Alternatives Logging exit 
 * 
 * 
 * @return boolean
 */
public boolean verify() {

    // say so
    System.out.println("Alternatives Logging exit, verify()"); 

    // ok
    return true;  
  
} // end-method
/**
 * Alternatives Logging exit 
 * 
 * @param msg String - the message
 * @return boolean
 */
public boolean write(String msg) {

    // say what 
    System.out.println(msg);              

    // ok
    return true;  
  
} // end-method
} // end-class
