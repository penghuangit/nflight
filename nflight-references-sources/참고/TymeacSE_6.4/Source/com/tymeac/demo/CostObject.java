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

/**
 * Tutorial cost of component
 * 
 */

public class CostObject 
    implements java.io.Serializable  {

  private static final long serialVersionUID = 6355557518672886482L;

  // name of cost component
  private String unit; 

  // return code from processing
  private int rc;

  // cost of component
  private float cost;   

/**
 * basic constructor
 */

public CostObject() {

} // end-constructor
/**
  * constructor with all fields
  * @param p_unit String - name 
  * @param p_rc int - return code
  * @param p_cost float  - cost 
  */

public CostObject(String p_unit, int p_rc, float p_cost) {
  
    // name of component
    unit = p_unit;

    // return code from processing
    rc = p_rc;

    // cost of component
    cost = p_cost;
            
} // end-constructor
/**
 * accessor for cost
 * @return float cost  
 */

public float getCost() {

    return cost;    
                      
} // end-method
/**
 * accessor for return code
 * @return int return code  
 */

public int getReturnCode() {

    return rc;    
                      
} // end-method
/**
 * accessor for name
 * @return String name of component  
 */

public String getUnit() {

    return unit;    
                      
} // end-method
/**
 * mutator for cost
 * @param float cost  
 */

public void setCost(float p_cost) {

    // set the cost
    cost = p_cost;    
                      
} // end-method
/**
 * mutator for return code
 * @param int return code  
 */

public void setReturnCode(int p_rc) {

    // set the return code
    rc = p_rc;    
                      
} // end-method
/**
 * mutator for name
 * @param String name of component  
 */

public void setUnit(String p_unit) {

    // set the name
    unit = p_unit;    
                      
} // end-method
} // end-class
