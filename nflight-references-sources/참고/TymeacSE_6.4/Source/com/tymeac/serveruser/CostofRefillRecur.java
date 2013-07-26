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

import com.tymeac.base.*;
/**
 * Tymeac Demonstration System. 
 *   Invoked method for processing Tutorial Queue, Refill_Queue, *-- substitution --*.  
 *
 *
 */

public class CostofRefillRecur {
  
/**
 * This method is the recursive call to the Tymeac Server
 * 
 * @return CostObject
 * @param obj java.lang.Object
 * @param currency java.lang.String
 */

private static CostObject goTymeac(Object obj, String currency) {


  /* The second arg passed to the invoked main method is the Tymeac Server
   *   interface.  The object is cast to: TymeacInterface  and used in the
   *   second constructor for TySvrComm, (signature: TySvrComm(TymeacInterface Ty) ).
   */
   

  // return object to caller
  CostObject myobj = new CostObject("Refill Components failure", // name of component
                    1,            // initially error return code 
                    0.0F);        // initially cost of $0.00

  // concatenated Strings of components
  String out = "";

  // total cost
  float total = 0.0F;

  // returned array from Tymeac
  Object back[] = null;

  // cost object from the components
  CostObject cobj = null;

  // form a parameter for Tymeac   
  TymeacParm TP = new TymeacParm( currency,   // input data to Queues is currency  
                  "RefillCost", // function name
                  10,            // wait time
                  1);            // priority
                    

  // all in block
  try {
    // obj is TymeacInterface

    // do a sync request   
    back = ((TymeacInterface)obj).syncRequest(TP);
  
    // Should be an array
    if  (back == null)  {

      // error code
      myobj.setReturnCode(1);

      // error
      return myobj;

    } // endif      
    
    // number of objects in array
    int nbr = back.length;

    // sum all the costs
    for  (int i = 0; i < nbr; i++) {

        // When a CostObject
        if  (back[i] instanceof CostObject) {

          // cast to cost object
          cobj = (CostObject) back[i];
              
          // When return code is good
          if  (cobj.getReturnCode() == 0) {

            // concatenate the string
            out = out.concat( " " 
                    + cobj.getUnit() 
                    + " cost is $"
                    + cobj.getCost());


            // sum total
            total += cobj.getCost();
        
          } // endif
        } // endif
    } // end-for
  } // end-try

  catch (Exception e) {

    // print
    System.out.println("Caught Exception= " + e.getMessage());

    // error code
    myobj.setReturnCode(2);

    // error 
    return myobj;

  } // end-catch

  // total cost
  myobj.setCost(total);

  // new msg
  myobj.setUnit(new String("Refill (Components" + out + ")"));

  // good return code
  myobj.setReturnCode(0); 

  // good
  return myobj;

} // end-method
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

    // return object to caller
    CostObject cobj = new CostObject("Refill",      // name of component
                                      0,            // initially normal return code 
                                      0.0F);        // initially cost of $0.00

    // return object from goTymeac method
    CostObject tyobj;
    
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
             
            // *--- calc cost of this component by recursive call ---*
                            
            // call to Tymeac (second arg is TymeacInterface cast to Object) 
            tyobj = goTymeac(args[1], Currency);

            // *-- return code within cobj is not checked --*

            // total cost
            cobj.setCost(tyobj.getCost());

            // msg from components
            cobj.setUnit(tyobj.getUnit());

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
