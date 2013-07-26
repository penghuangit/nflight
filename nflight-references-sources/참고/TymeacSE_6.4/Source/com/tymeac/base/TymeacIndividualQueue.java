package com.tymeac.base;

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
 * This is the Class for an Individual User Queue   
 *   See also TymeacIndividualQueue 
 */
public final class TymeacIndividualQueue {

  // Name of the Queue     
  private String name;

  // Processing Application Class
  private String pa_name;

  // Output Agent Queue Type, Yes/No (not case sensitive)
  private String que_type;

  // Start all threads at Tymeac Start Up, Yes/No (not case sensitive)
  private String start_threads;

  // PAC timeout value in seconds
  private int pa_timeout;

  // Number of threads
  private int nbr_threads;

  // Number of Wait Lists
  private int nbr_waitlists;

  // Number of Wait List entries
  private int nbr_entries;
  
  // Number of Wait List entries for thresholds
  private int nbr_threshold_entries;

  // Queue Wait Time for time-out
  private int wait_time;

  // Queue Wait Time for time-out
  private int kill_time;

  // New Thread Thresholds:

  // Overall
  private float overall;

  // Individual
  private float individual;

  // Weighted Factor
  private float factor;

  // Weighted Average
  private float average;

/**
 * no-arg Constructor for the Tymeac Individual User Queue
 */
public TymeacIndividualQueue() {

} // end-constructor

/**
 * Constructor for the Tymeac Individual User Queue
 *
 * @param u_name String  is the name of the Queue    
 * @param u_pa_name String  is the name of the Processing Application Class
 * @param u_que_type String  is the Type of Queue, normal or OA
 * @param u_nbr_threads int  is the number of threads
 * @param u_pa_timeout int  is the PAC timeout value in seconds
 * @param u_nbr_waitlists int  is the number of wait lists       
 * @param u_nbr_entries int  is the number of wait list entries
 * @param u_nbr_entries_thresholds int  is the number of wait list entries for thresholds
 * @param u_wait_time int  is the Queue time out value        
 * @param u_overall float  is the New Thread Threshold overall percent
 * @param u_individual float  is the New Thread Threshold individual percent
 * @param u_factor float  is the New Thread Threshold weighted factor
 */
public TymeacIndividualQueue(String u_name,
                             String u_pa_name,
                             String u_que_type,
                             String u_start_threads,  
                             int    u_pa_timeout,
                             int    u_nbr_threads,
                             int    u_nbr_waitlists,
                             int    u_nbr_entries,
                             int    u_nbr_entries_thresholds,
                             int    u_wait_time,
                             int    u_kill_time,  
                             float  u_overall,
                             float  u_individual,
                             float  u_factor,
                             float  u_average) {

  
  // Queue name
  name = u_name;

  // Processing Application Class
  pa_name = u_pa_name;

  // Queue Type
  que_type = u_que_type;

  // Start threads at Tymeac Start Up
  start_threads = u_start_threads;
  
  // PAC timeout value
  pa_timeout = u_pa_timeout;

  // Number of threads
  nbr_threads = u_nbr_threads;

  // Number of Wait Lists
  nbr_waitlists = u_nbr_waitlists;

  // Number of Wait List Entries
  nbr_entries = u_nbr_entries;
  
  // Number of Wait List Entries for thresholds
  nbr_threshold_entries = u_nbr_entries_thresholds;

  // Queue Wait Time
  wait_time = u_wait_time;

  // Threads Kill Time
  kill_time = u_kill_time;

  // Overall %
  overall = u_overall;

  // Individual
  individual = u_individual;

  // Weighted Factor
  factor = u_factor;

  // Weighted Average
  average = u_average;
  
} // end-constructor

/**
 * This accessor method returns the Threshold weighted average
 * @return float weighted average     
 */
public float getAverage() { return average; } // end-method

/**
 * This accessor method returns the Threshold weighted factor
 * @return float weighted factor      
 */
public float getFactor() { return factor; } // end-method

/**
 * This accessor method returns the Threshold individual %
 * @return float individual percent   
 */
public float getIndividual() { return individual; } // end-method

/**
 * This accessor method returns the Queue wait time
 * @return int wait time           
 */
public int getKillime() { return kill_time; } // end-method

/**
 * This accessor method returns the name of the Function
 * @return java.lang.String name of function
 */
public String getName() { return name; } // end-method

/**
 * This accessor method returns the number of Wait List Entries
 * @return int number of wait list entries
 */
public int getNbrEntries() { return nbr_entries; } // end-method

/**
 * This accessor method returns the number of Wait List Entries for thresholds
 * @return int number of wait list entries
 */
public int getNbrEntriesThresholds() { return nbr_threshold_entries; } // end-method

/**
 * This accessor method returns the Number of threads
 * @return int number of threads
 */
public int getNbrThreads() { return nbr_threads; } // end-method

/**
 * This accessor method returns the number of Wait Lists
 * @return int number of wait lists
 */
public int getNbrWaitlists() { return nbr_waitlists; } // end-method

/**
 * This accessor method returns the Threshold overall %
 * @return float overall percent   
 */
public float getOverall() { return overall; } // end-method

/**
 * This accessor method returns the name of the Processing Application Class
 * @return java.lang.String Name of the Processing Application Class
 */
public String getPA() { return pa_name; } // end-method

/**
 * This accessor method returns the Processing Application Class timeout value
 * @return int
 */
public int getPa_timeout() { return pa_timeout; } // end-method

/**
 * This accessor method returns the Queue Type
 * @return java.lang.String
 */
public String getQueType() { return que_type; } // end-method

/**
 * This accessor method returns the start threads string
 * @return java.lang.String
 */
public String getStartThreads() { return start_threads; } // end-method

/**
 * This accessor method returns the Queue wait time
 * @return int wait time           
 */
public int getWaitime() { return wait_time; } // end-method

/**
 * Set the weighted average
 * @param newAverage float
 */
public void setAverage(float newAverage) { average = newAverage; } // end-method

/**
 * Set the weighted factor 
 * @param newFactor float
 */
public void setFactor(float newFactor) { factor = newFactor; } // end-method

/**
 * Set the Individual %
 * @param newIndividual float
 */
public void setIndividual(float newIndividual) { individual = newIndividual; } // end-method

/**
 * Set the time to kill the thread
 * @param newKill_time int
 */
public void setKill_time(int newKill_time) { kill_time = newKill_time; } // end-method

/**
 * Set number of waitlist entries
 * @param newNbr_entries int
 */
public void setNbr_entries(int newNbr_entries) { nbr_entries = newNbr_entries; } // end-method

/**
 * Set number of waitlist entries for thresholds
 * @param newNbr_entries int
 */
public void setNbr_entries_thresholds(int newNbr_entries) { nbr_threshold_entries = newNbr_entries; } // end-method

/**
 * Set the number of threads
 * @param newNbr_threads int
 */
public void setNbr_threads(int newNbr_threads) { nbr_threads = newNbr_threads; } // end-method

/**
 * Set the number of waitlists
 * @param newNbr_waitlists int
 */
public void setNbr_waitlists(int newNbr_waitlists) { nbr_waitlists = newNbr_waitlists; } // end-method

/**
 * Set the overall %
 * @param newOverall float
 */
public void setOverall(float newOverall) { overall = newOverall; } // end-method

/**
 * Set processing application class name
 * @param newPa_name java.lang.String
 */
public void setPa_name(java.lang.String newPa_name) { pa_name = newPa_name; } // end-method

/**
 * Set PAC timeout value
 * @param newPa_timeout int
 */
public void setPa_timeout(int newPa_timeout) { pa_timeout = newPa_timeout; } // end-method

/**
 * Set que type, no=normal, yes=OA
 * @param newQue_type java.lang.String
 */
public void setQue_type(java.lang.String newQue_type) { que_type = newQue_type; } // end-method

/**
 * Set name of this queue
 * @param newName java.lang.String
 */
public void setQueName(java.lang.String newName) { name = newName; } // end-method

/**
 * Set to start threads at startup, no, or yes
 * @param newStart_threads java.lang.String
 */
public void setStart_threads(java.lang.String newStart_threads) { start_threads = newStart_threads; } // end-method

/**
 * Set timeout time
 * @param newWait_time int
 */
public void setWait_time(int newWait_time) { wait_time = newWait_time; } // end-method

} // end-class
