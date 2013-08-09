package com.tymeac.serveruser;

/* 
 * Copyright (c) 2002 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Tymeac Demonstration System. 
 *   Invoked method for processing Tutorial Queue, MetalTip_Queue.  
 *
 *
 */

public class CostofMetalTip {  

/**
 * Invoked method by java.lang.reflect.Method
 * 
 * @param args Object[]
 * @return Object
 * @exception java.lang.Throwable Your exception
 */
public static Object main(Object args[])
            throws  java.lang.Throwable    {

/*
  
  Catch your exception.  Process as needed.  Then reThrow the exception.
  
    The Queue Thread that invoked this method catches exceptions and
    does a clean-up procedure.  The procedure disables this Thread and
    sends messages to the Log and Notification facility. 
       
*/
    // passed value
    String Currency = null;

    // return object
    CostObject cobj = new CostObject("MetalTip",      // name of component
                                      0,            // initially normal return code 
                                      0.0F);        // initially cost of $0.00
    
    try {
        // Should be a string passed
        if  (args[0] instanceof String) {

            // ok           
            ;
        }
        else {
            // error 1, arg is not a string
            cobj.setReturnCode(1);

            // cast to an object and return
            return cobj;

        } // endif      
  
        // passed string
        Currency = (String) args[0];
          
        // see what we have
        if  (Currency.compareTo("US$") == 0) {
             
            // *--- calc cost of this component ---*
                
            // forced for this demo
            cobj.setCost(0.02F);

            // cast to an object and return
            return cobj;
        }   
        else {
            // error 2, curreny is not United States Dollars
            cobj.setReturnCode(2);

            // cast to an object and return
            return cobj;

        } // endif    
    } // end-try
     
    catch (java.lang.Throwable e) {
          
        //e.printStackTrace(System.out);
        
        throw  e; 
    } // end-catch              
    
        
} // end-method
} // end-class
