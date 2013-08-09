package com.tymeac.base;

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
 * This is the Class for Individual User Function
 *   See also TyIndividualQueue  
 * 
 */
public final class TymeacIndividualFunction {

  // Name of the Function
  private String name;

  // Output Agent Queue
  private String oa_name;

  // List of Queues
  private String[] list;

  // Number of Queues
  private int nbr_que;

/**
 *  no-arg Constructor for the Tymeac Individual User Function
 */
public TymeacIndividualFunction() {

} // end-constructor

/**
 * Constructor for the Tymeac Individual User Function
 *
 * @param u_name String is the name of the Function
 * @param u_oa   String is the name of the Output Agent Queue
 * @param u_list String[] is the List of Queues for this Function
 *        
 */
public TymeacIndividualFunction(String   u_name,
                                String   u_oa,
                                String[] u_list) {

  // local variables
  int i; 
  
  // number of queues
  nbr_que = u_list.length;

  // Function name
  name = u_name;

  // Output Agent name
  oa_name = u_oa;

  // Create new List of queues
  list = new String[nbr_que];

  // Load each slot
  for  (i = 0; i < nbr_que; i++) {

    // move to instance field 
    list[i] = u_list[i];
    
  } // end-for   

} // end-constructor

/**
 * This accessor method returns the List of queues
 * @return java.lang.String[] List of queues
 */

public String[] getList() { return list; } // end-method

/**
 * This accessor method returns the name of the Function
 * @return java.lang.String name of the Function
 */
public String getName() { return name; } // end-method

/**
 * This accessor method returns the number of queues in the function
 * @return int number of queues
 */
public int getNbrQue() { return nbr_que; } // end-method

/**
 * This accessor method returns the name of the Output Agent Queue
 * @return java.lang.String name of the Output Agent Queue
 */
public String getOA() { return oa_name; } // end-method

/**
 * Set the List of Queues
 * @param newList java.lang.String[]
 */
public void setList(java.lang.String[] newList) {

  // number of queues
  nbr_que = newList.length; 

  // Create new List of queues
  list = new String[nbr_que];

  // Load each slot
  for  (int i = 0; i < nbr_que; i++) {

    list[i] = newList[i];
        
  } // end-for

} // end-method

/**
 * Set the name of the function
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) { name = newName; } // end method

/**
 * Set number of queues
 * @param nbr int - number of queues
 */
public void setNbrQue(int nbr) { nbr_que = nbr; } // end-method

/**
 * Set the Output Agent name
 * @param newOa_name java.lang.String
 */
public void setOa_name(java.lang.String newOa_name) { oa_name = newOa_name; } // end-method

} // end-class
