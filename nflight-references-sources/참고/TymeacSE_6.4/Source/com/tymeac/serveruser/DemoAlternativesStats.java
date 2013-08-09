package com.tymeac.serveruser;
/**
 * Tymeac Demonstration System. 
 *   called alternatives exit for statistics writing
 *
 *
 */

public class DemoAlternativesStats
      implements com.tymeac.base.TymeacAlternativesInterface {  

/**
 * Alternatives Statistics exit 
 * 
 * 
 * @return boolean
 */
public boolean verify() {

    // say so
    System.out.println("Alternatives Statistics exit, verify()");              

    // ok
    return true;  
  
} // end-method
/**
 * Alternatives statistics exit 
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
