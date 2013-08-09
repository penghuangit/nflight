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

public class CallbackTymeacUserFunctions {

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

public CallbackTymeacUserFunctions() {

  // vector for temp phase
  Vector<TymeacIndividualFunction> temp = new Vector<TymeacIndividualFunction>();

  // String array variable for the list of queues
  String[] list; 


// define the Functions here



// Alternatively, use the no-arg constructor and add each field with a mutator
/*  
  // allocate a new object for this function
  //    set each mutator field
  //    add to the vector
  TymeacIndividualFunction tif = new TymeacIndividualFunction();

  // *--- Demo Function 1 ---*

  // get a new array 
  list = new String[3];
  
  // load the list of Queues
  list[0] = "AAAA";
  list[1] = "AAAA";
  list[2] = "AAAA";   

  // name of this function
  tif.setName("Function_1"); 

  // name of Output Agent Queue
  tif.setOa_name(""); 

  // list of queues for this function
  tif.setList(list);

  // there is no mutator for the number_of_queues field.
  // this value is set in the setList() method. 
  
  // add to vector
  temp.addElement(tif);

  // allocate a new object for this function
  //    set each mutator field
  //    add to the vector
  TymeacIndividualFunction tif2 = new TymeacIndividualFunction();

*/



// *--- Demo Function 1 ---*
  
  // get a new array 
  list = new String[3];
  
  // load the list of Queues
  list[0] = "AAAA";
  list[1] = "AAAA";
  list[2] = "AAAA"; 
  
  // load the function
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

// start up test

// *--- start up ---*
  
  // get a new array 
  list = new String[1];
  
  // load the list of Queues
  list[0] = "StartUp";
  
  // function
  temp.addElement( new TymeacIndividualFunction("SFunction_1", // name of function
            "",     // Output Agent Queue  
            list)); // list of queues 


// *------- Callback Function Start ----------*


// *--- Demo Function Callback ---*
  
  // get a new array
  list = new String[3];
  
  // load the list of Queues
  list[0] = "CALA";
  list[1] = "CALB";
  list[2] = "CALC"; 
  
  // load the function
  temp.addElement( new TymeacIndividualFunction("Function_Callback", // name of function
              "CALD",       // Output Agent Queue 
              list));       // list of queues 
  

// *------- Callback Function End ----------*




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
 * @return TymeacIndividualFunction[]
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
