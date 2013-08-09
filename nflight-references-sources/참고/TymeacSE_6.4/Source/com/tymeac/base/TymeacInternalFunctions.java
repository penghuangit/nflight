package com.tymeac.base;

/*
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

import com.tymeac.serveruser.*;
import java.util.*;

/**
 * This class holds the internal functions used by the Tymeac Server
 *   just like the com.tymeac.serveruser.TymeacUserFunctions holds
 *   functions for users.
 * 
 * This class is loaded by the start up for the Server after the normal functons. That
 *   may be either user classes or DBMS tables. 
 */
public class TymeacInternalFunctions
          extends TymeacUserFunctions {

public TymeacInternalFunctions() {
  
  // vector for temp phase
  ArrayList<TymeacIndividualFunction> temp = new ArrayList<TymeacIndividualFunction>();

// *--- define all the functions here ---*

// load the cancel function
  temp.add(
    new TymeacIndividualFunction(
        "TymeacInternalCancel", // name of function
        "",           // no Output Agent Queue  
        new String[] { "TymeacInternalCancel" })); // list of queues 
  

// *--- define all the functions above ---*

  // total Queues 
  total_functions = temp.size();   

  // create a new array in super
  functions = new TymeacIndividualFunction[total_functions];

  // fill up the array by taking objects out of the temporary list
  for  (int i = 0; i < total_functions; i++) {

    // add this queue
    functions[i] = temp.get(i);

  } // end-for

} // end-constructor
} // end-class
