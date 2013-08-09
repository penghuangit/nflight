package com.tymeac.client.jframe;

/* 
 * Copyright (c) 2002 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Class to hold que data elements
 */

public final class QueData {

	// fields
  private String  que_name    = null;
  private String  pa_class    = null;
  
  private String pa_timeout  = null;
  private String que_type    = null;
  private String th_start    = null;
  private String nbr_threads = null;
  private String wait_time   = null;
  private String kill_time   = null;
  private String nbr_wl      = null;
  private String nbr_in_wl   = null;
  private String th_nbr_in_wl = null;
  
  private String overall   = null;
  private String individ   = null;
  private String factor    = null; 
  private String average   = null;

/**
 * Constructor
 */

public QueData() {

} // end-constructor
/**
 * Constructor
 * @param que_name      String - queue name
 * @param pa_class      String - appl class name
 * @param pa_timeout    String - timeout value
 * @param que_type      String - que type normal or oa
 * @param th_start      String - start threads at tymeac start up
 * @param nbr_threads   String - number of threads
 * @param wait_time     String - time to wait for next request
 * @param kill_time     String - time to kill waiting thread
 * @param nbr_wl        String - number of wait lists
 * @param nbr_in_wl     String - number of entries in each wait list
 * @param th_nbr_in_wl  String - entries in each wait list for thesholds
 * @param overall       String - overall %
 * @param individ       String - individual %
 * @param factor        String - weighted factor
 * @param average       String - weithted average
 */
public QueData(	String que_name,
								String pa_class,
								String pa_timeout, 
								String que_type,
								String th_start,
								String nbr_threads, 	
								String wait_time, 
								String kill_time,
								String nbr_wl, 
								String nbr_in_wl,  
                String th_nbr_in_wl,  
  							String overall,   
							  String individ,   
							  String factor,     
							  String average) {

	this.que_name 		= que_name; 
  this.pa_class 		= pa_class;    
  this.pa_timeout 	= pa_timeout;    
  this.que_type   	= que_type;   
  this.th_start    	= th_start; 
  this.nbr_threads	= nbr_threads; 
  this.wait_time  	= wait_time; 
  this.kill_time  	= kill_time;  
  this.nbr_wl     	= nbr_wl;
  this.nbr_in_wl  	= nbr_in_wl;
  this.th_nbr_in_wl = th_nbr_in_wl;
  this.overall    	= overall;
  this.individ    	= individ;
  this.individ     	= individ;
  this.factor		    = factor;
  this.average   	  = average;

} // end-constructor
/**
 * accessor for average
 * @return java.lang.String
 */
public java.lang.String getAverage() { return average; } // end-method

/**
 * accessor for factor
 * @return java.lang.String
 */
public java.lang.String getFactor() { return factor; } // end-method

/**
 * accessor for individual %
 * @return java.lang.String
 */
public java.lang.String getIndivid() { return individ; } // end-method

/**
 * accessor for kill time
 * @return java.lang.String
 */
public java.lang.String getKill_time() { return kill_time; } // end-method

/**
 * accessor for number of slots in a wait list
 * @return java.lang.String
 */
public java.lang.String getNbr_in_wl() { return nbr_in_wl; } // end-method

/**
 * accessor for number of slots in a wait list for thresholds
 * @return java.lang.String
 */
public java.lang.String getNbr_th_in_wl() { return th_nbr_in_wl; } // end-method

/**
 * accessor for number of threads
 * @return java.lang.String
 */
public java.lang.String getNbr_threads() { return nbr_threads; } // end-method

/**
 * accessor for number of wait lists
 * @return java.lang.String
 */
public java.lang.String getNbr_wl() { return nbr_wl; } // end-method

/**
 * accessor for overall %
 * @return java.lang.String
 */
public java.lang.String getOverall() { return overall; } // end-method

/**
 * accessor for appl class name
 * @return java.lang.String
 */
public java.lang.String getPa_class() { return pa_class; } // end-method

/**
 * accessor for time out seconds
 * @return java.lang.String
 */
public java.lang.String getPa_timeout() { return pa_timeout; } // end-method

/**
 * accessor for queue name
 * @return java.lang.String
 */
public java.lang.String getQue_name() { return que_name; } // end-method

/**
 * accessor for queue type
 * @return java.lang.String
 */
public java.lang.String getQue_type() { return que_type; } // end-method

/**
 * accessor for start threads field
 * @return java.lang.String
 */
public java.lang.String getTh_start() { return th_start; } // end-method

/**
 * accessor for wait time
 * @return java.lang.String
 */
public java.lang.String getWait_time() { return wait_time; } // end-method

/**
 * mutator for average
 * @param newAverage java.lang.String
 */
public void setAverage(java.lang.String newAverage) { average = newAverage; } // end-method

/**
 * mutator for factor
 * @param newFactor java.lang.String
 */
public void setFactor(java.lang.String newFactor) { factor = newFactor; } // end-method

/**
 * mutator for individual %
 * @param newIndivid java.lang.String
 */
public void setIndivid(java.lang.String newIndivid) { individ = newIndivid; } // end-method

/**
 * mutator for kill time
 * @param newKill_time java.lang.String
 */
public void setKill_time(java.lang.String newKill_time) { kill_time = newKill_time; } // end-method

/**
 * mutator for number of slots in wait lists for thresholds
 * @param newNbr_in_wl java.lang.String
 */
public void setNbr_th_in_wl(java.lang.String newNbr_th_in_wl) { th_nbr_in_wl = newNbr_th_in_wl; } // end-method

/**
 * mutator for number of slots in wait lists
 * @param newNbr_in_wl java.lang.String
 */
public void setNbr_in_wl(java.lang.String newNbr_in_wl) { nbr_in_wl = newNbr_in_wl; } // end-method


/**
 * mutator for number of threads
 * @param newNbr_threads java.lang.String
 */
public void setNbr_threads(java.lang.String newNbr_threads) { nbr_threads = newNbr_threads; } // end-method

/**
 * mutator for number of wait lists
 * @param newNbr_wl java.lang.String
 */
public void setNbr_wl(java.lang.String newNbr_wl) { nbr_wl = newNbr_wl; } // end-method

/**
 * mutator for overall %
 * @param newOverall java.lang.String
 */
public void setOverall(java.lang.String newOverall) { overall = newOverall; } // end-method

/**
 * mutator for appl class name
 * @param newPa_class java.lang.String
 */
public void setPa_class(java.lang.String newPa_class) { pa_class = newPa_class; } // end-method

/**
 * mutator for time out
 * @param newPa_timeout java.lang.String
 */
public void setPa_timeout(java.lang.String newPa_timeout) { pa_timeout = newPa_timeout; } // end-method

/**
 * mutator for queue name
 * @param newQue_name java.lang.String
 */
public void setQue_name(java.lang.String newQue_name) { que_name = newQue_name; } // end-method

/**
 * mutator for queue type
 * @param newQue_type java.lang.String
 */
public void setQue_type(java.lang.String newQue_type) { que_type = newQue_type; } // end-method

/**
 * mutator for start threads indicator
 * @param newTh_start java.lang.String
 */
public void setTh_start(java.lang.String newTh_start) { th_start = newTh_start; } // end-method

/**
 * mutator for wait time
 * @param newWait_time java.lang.String
 */
public void setWait_time(java.lang.String newWait_time) { wait_time = newWait_time; } // end-method

} // end-class
