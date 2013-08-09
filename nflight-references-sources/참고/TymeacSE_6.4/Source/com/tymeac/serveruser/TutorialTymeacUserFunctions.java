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

import java.util.*;
import com.tymeac.base.*;
/**
 * This is the Class for User Functions when not using a DBMS.
 *   See also TymeacUserQueues and TymeacUserVariables  
 * 
 */

public class TutorialTymeacUserFunctions {

  // total number of functions
  private int total_functions;

  // This is the array of all the functions
  private TymeacIndividualFunction[] functions;

/**
 * Constructor for the Tymeac User Functions 
 *
 *  First build a Vector of the TymeacIndividualFunction objects
 *
 *  Then load the functions array from the temporary Vector 
 */

public TutorialTymeacUserFunctions() {

    // vector for temp phase
    Vector<TymeacIndividualFunction> temp = new Vector<TymeacIndividualFunction>();

    // String array variable for the list of queues
    String[] list; 


// define the Functions here


// *--- Demo Function 1 ---*
  
    // get a new array 
    list = new String[3];
  
    // load the list of Queues
    list[0] = "AAAA";
    list[1] = "AAAA";
    list[2] = "AAAA"; 
  
    // function
    temp.addElement( new TymeacIndividualFunction("Function_1", // name of function
                                                  "",           // no Output Agent Queue  
                                                  list));       // list of queues 
  

// *--- Demo Function 2 ---*
  
    // get a new array
    list = new String[1];
  
    // load the list of Queues
    list[0] = "AAAA";
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("Function_2", // name of function
                                                  "",           // no Output Agent Queue  
                                                  list));       // list of queues 
  

// *--- Demo Function 3 ---*
  
    // get a new array
    list = new String[2];
  
    // load the list of Queues
    list[0] = "AAAA";
    list[1] = "BBBB";
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("Function_3", // name of function
                                                  "",           // no Output Agent Queue  
                                                  list));       // list of queues 
  

// *--- Demo Function 4 ---*
  
    // get a new array
    list = new String[3];
  
    // load the list of Queues
    list[0] = "AAAA";
    list[1] = "BBBB";
    list[2] = "CCCC"; 
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("Function_4", // name of function
                                                  "",           // no Output Agent Queue  
                                                  list));       // list of queues 
  

// *--- Demo Function 5 ---*
  
    // get a new array
    list = new String[1];
  
    // load the list of Queues
    list[0] = "AAAA";
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("Function_5", // name of function
                                                  "DDDD",       // Output Agent Queue 
                                                  list));       // list of queues 
  

// *--- Demo Function 6 ---*
  
    // get a new array
    list = new String[2];
  
    // load the list of Queues
    list[0] = "AAAA";
    list[1] = "BBBB";
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("Function_6", // name of function
                                                  "DDDD",       // Output Agent Queue 
                                                  list));       // list of queues 
  

// *--- Demo Function 7 ---*
  
    // get a new array
    list = new String[3];
  
    // load the list of Queues
    list[0] = "AAAA";
    list[1] = "BBBB";
    list[2] = "CCCC"; 
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("Function_7", // name of function
                                                  "DDDD",       // Output Agent Queue 
                                                  list));       // list of queues 
  

// *--- Demo Function 8 ---*
  
    // get a new array
    list = new String[1];
  
    // load the list of Queues
    list[0] = "EEEE";
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("Function_8", // name of function
                                                  "",           // no Output Agent Queue  
                                                  list));       // list of queues 
  

// *--- Demo TyNotify ---*
  
    // get a new array
    list = new String[1];
  
    // load the list of Queues
    list[0] = "Notify";
  
    // load the function
    temp.addElement( new TymeacIndividualFunction("TyNotify",   // name of function
                                                  "",           // no Output Agent Queue  
                                                  list));       // list of queues 

// *--- Jini Demo Function 1 ---*
  
  // get a new array 
  list = new String[4];
  
  // load the list of Queues
  list[0] = "JQue1";
  list[1] = "JQue2";
  list[2] = "JQue3";
  list[3] = "JQue4"; 
  
  // function
  temp.addElement( new TymeacIndividualFunction("JFunction_1", // name of function
                          "JQueAgent",   // Output Agent Queue  
                          list));       // list of queues 




// *--- Tutorial, part 1 ---*
  
  // get a new array 
  list = new String[4];
  
  // load the list of Queues
  list[0] = "Cylinder_Queue";
  list[1] = "Refill_Queue";
  list[2] = "TopCap_Queue";
  list[3] = "BottomCap_Queue"; 
  
  // function
  temp.addElement( new TymeacIndividualFunction("PenCost", // name of function
                          "CostAgent_Queue",   // Output Agent Queue  
                          list));       // list of queues 


// *--- Tutorial, part 2 ---*
  
  // get a new array 
  list = new String[4];
  
  // load the list of Queues
  list[0] = "Tube_Queue";
  list[1] = "Ink_Queue";
  list[2] = "MetalTip_Queue";
  list[3] = "Ball_Queue"; 
  
  // function
  temp.addElement( new TymeacIndividualFunction("RefillCost", // name of function
                          "",   // No Output Agent Queue  
                          list));       // list of queues 





// *--- end of phase one ---*


    // total functions
    total_functions = temp.size();    

    // create a new array 
    functions = new TymeacIndividualFunction[total_functions];

    // fill up the array by taking objects out of the temporary Vector
    for  (int i = 0; i < total_functions; i++) {

          // add this queue
          functions[i] = temp.elementAt(i);

    } // end-for

} // end-constructor
/**
 * This accessor method returns the list of Functions     
 *
 * @return TymeacIndividualFunction
 */

public TymeacIndividualFunction[] getFunctions() {

  // return the private instance field
  return functions;

} // end-method
/**
 * This accessor method returns the total number of Functions
 * @return int total_functions is the total number of Functions
 */

public int getNbrFunctions() {

  // return the instance field
  return total_functions;

} // end-method
} // end-class
