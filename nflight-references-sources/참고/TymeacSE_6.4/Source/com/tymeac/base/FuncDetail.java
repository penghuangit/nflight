package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.util.concurrent.atomic.*;

/**
 * A Tymeac Function detail object
 */
public final class FuncDetail {

   private final String name; // Function name
   private final int hash;    // hash of name
   private AreaBase agent;   // Output agent queue reference
   private AreaBase[] qtbl;  // queue areas references 
   
   private AtomicLong used; // times used

/**
 * Constructor w/ only name Used by Comparator in binary searching.
 */
protected FuncDetail(String u_name) {
   
  name = u_name; // name 
  hash = u_name.hashCode(); // hash of that name
   
} // end-constructor

/**
 * Create the object with all fields
 * @param u_name String - function name
 * @param u_agent AreaBase - output agent number
 * @param u_qtbl AreaBase[] - queue table
 */ 
protected FuncDetail(String u_name,
                   AreaBase u_agent,
                   AreaBase[] u_qtbl) {
          
  name  = u_name;         
  agent = u_agent;                         
  qtbl  = u_qtbl;   
  hash  = u_name.hashCode(); // hash code of name
  used  = new AtomicLong(0); // times this func used
          
} // end-constructor

/**
 * Increment times this function was used 
 */ 
protected void addUsed ( ) { used.getAndIncrement(); } // end-method

/**
 * Diagnose - display the internal queue table
 */ 
protected void dsplyQues () {
     
  // number of queues
  int nbr_que = getNbrQue();
    
  TyBase.printMsg(name + " Nbr que=" + nbr_que);
  
  // do all queues
  for  (int i = 0; i < nbr_que; i++) {
          
    TyBase.printMsg("Queue Name = " + qtbl[i].getName());
          
  } // end-for
  
  // When an agent, print the agent
  if  (agent != null) TyBase.printMsg("-->Agent Name = " + agent.getName());
  
} // end-method

/**
 * Accessor for the output agent queue number
 * @return AreaBase
 */
protected AreaBase getAgent ( ) { return agent; } // end-method

/**
 * Accessor for the hash code
 * @return int
 */
protected int getHash ( ) { return hash; } // end-method

/**
 * Accessor for the function name
 * @return java.lang.String
 */ 
protected String getName ( ) { return name; } // end-method

/**
 * Accessor for the number of queues
 * @return int
 */ 
private int getNbrQue ( ) { return qtbl.length; } // end-method

/**
 * Accessor for the queue array
 * @return AreaBase[]
 * 
 */         
protected AreaBase[] getTbl() { return qtbl; } // end-method  
    
/**
 * Accessor for the times used
 * @return long 
 */        
protected long getUsed() { return used.get(); } // end-method 
 
} // end-class