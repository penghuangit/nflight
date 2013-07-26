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
 * Demonstration of Tymeac functionality.  
 *
 *
 */
public class CostAgent {
  

/**
 * Invoked method by java.lang.reflect.Method
 *
 * This is an example of an Output Agent method.  There is no return from this
 *    method.   
 *
 * 
 * @param args Object[]
 * @exception java.lang.Throwable Your exception
 */

public static void main(Object[] args)  
                        throws  java.lang.Throwable {

/*
  
  Catch your exception.  Process as needed.  Then reThrow the exception.
  
    The Queue Thread that invoked this method catches exceptions and
    does a clean-up procedure.  The procedure disables this Thread and
    sends messages to the Log and Notification facility. 
    
   
*/

    // cost object from the components
    CostObject cobj = null;

    // total cost 
    float total = 0.0F; 

    // any errors
    int error = 0;
  
    try {  
        // number of objects passed
        int nbr = 0;

        // the passed object array
        Object[] what = null;

        // arg is really an object[]        
        what = (Object[]) args[0];  

        // length of array
        nbr = what.length;

        // When none, bye
        if  (nbr < 1) {

            // all done here
            System.out.println("CostAgent did not receive any arguments");
  
            // bye
            return;

        } // endif

        // base data          
        String out = "CostAgent ==>";

        // concat all the passed strings  
        for  (int i = 0; i < nbr; i++) {

            // When a CostObject
            if  (what[i] instanceof CostObject) {

                // cost object
                cobj = (CostObject) what[i];
          
                // When return code is good
                if  (cobj.getReturnCode() == 0) {

                    // concatenate the string
                    out = out.concat( " " 
                                      + cobj.getUnit() 
                                      + " cost is $"
                                      + cobj.getCost());

                    // sum total
                    total += cobj.getCost();
                }  
                else {
                    // concatenate the error string
                    out = out.concat( " " 
                                      + cobj.getUnit() 
                                      + " returned a "
                                      + cobj.getReturnCode());

                    // say is error
                    error = 1;

                } // endif    
            }
            else {                  
                // concatenate the error string
                out = out.concat(" Component did not return a CostObject");

                    // say is error
                    error = 1;

            } // endif
        } // end-for
    
        // When no errors
        if  (error == 0) {

            // concatenate the final string
            out = out.concat( " Total cost is $"  
                              + total);

            // put out
            System.out.println(out);

            // all done
            return;

        } // endif      
        
        // concatenate the final string with errors
        out = out.concat( " Cost is indeterminate");

        // put out 
        System.out.println(out);

        // all done
        return;
          
    } // end-try
     
    catch (java.lang.Throwable e) {
          
        e.printStackTrace(System.out);
        
        throw  e; 

    } // end-catch  
    
} // end-method
} // end-class
