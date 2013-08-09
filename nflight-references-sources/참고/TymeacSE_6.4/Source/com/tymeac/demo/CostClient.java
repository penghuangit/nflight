package com.tymeac.demo;

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
 * Tymeac demonstration system
 */
public class CostClient {  

/**
 * Request processing of Tutorial Function PenCost
 * @param args java.lang.String[]
 */ 
public static void main(String args[]) {   
  
  // input data is string of United States dollars currency
  String data = "USD";

  // make obj
  Object pass = data;

  // return array
  Object back[] = null;
 
  // form a parameter for Tymeac   
  TymeacParm TP = new TymeacParm( pass,      // input data 
                                  "PenCost", // function name
                                  10,        // wait time
                                  1);        // priority
                                      
  // do a sync request   
  back = new TySvrComm().syncRequest(TP);

          // comment the above sync request and uncomment the below async request
          //    
          // do an async request   
          //back = new TySvrComm().asyncRequest(TP);
  
  // reformat the return array
  TymeacReturn ret = TymeacReturn.formatCallReturn(back);
  
  // return code from tymeac
  int rc = ret.getReturnCode();
  
  // When no back
  if  (rc == 9001)  {
        
      // say no good  
      System.out.println("back returned null");
      return;

  } // endif  

  // When any error
  if  (rc != 0) {
        
      System.out.println(ret.getTyMessage()); // problem
      return;
      
  } // endif 

  // cost object from the components
  CostObject cobj = null;

  // total cost 
  float total = 0.0F; 

  // any errors
  int error = 0;

  // number of objects returned
  int nbr = 0;

  // length of array
  nbr = back.length;

  // returned string from Tymeac
  String S; 

  // When an async request (has a session) )
  if  (ret.getSessionId() > 0) {
  
      // Say is scheduled
      System.out.println("Request scheduled successfully.");

      // all done
      return;         
    
  } // endif

  Object[] user_object = (Object[])ret.getUserData();

  // base data          
  String out = "";

  // concat all the passed strings  
  for  (int i = 1; i < nbr; i++) {

    // When a CostObject
    if  (user_object[i] instanceof CostObject) {

        // cost object
        cobj = (CostObject) user_object[i];
    
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
          
} // end-method
} // end-class
