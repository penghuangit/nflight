package com.tymeac.base;

/* 
 * Copyright (c) 1998 - 2012 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.net.*;
import java.util.*;

/**
 * Start up building the Tymeac Queues
 */
public final class StartupQueues  {
  
  // common
  private final TyBase T;
  private final Startup base;
  private final StartupFields suFields;

/**
 * Constructor
 * @param Ty TyBase - tymeac base storage
 * @param Startup b - Base class
 */    
protected StartupQueues(TyBase ty, Startup b) {
  
  T    = ty;
  base = b;
  suFields = base.getStartupFields();
  
} // end-constructor

/**
 * build all the Queues for this session and when starting
 *  all threads for a queue, do so after the queue is 
 *  completely built 
 * @return boolean
 */
private boolean buildQueues(TymeacIndividualQueue[] full_tiq) 
                throws Throwable{

  // number of queues
  int nbr_que = full_tiq.length;      

  // variables
  int i;
  int que_seq = 0;

  // variables for the Queue
  ClsUrl  cls_url;
  String  a_name;
  String  a_full_class;
  String  a_class = null;
  URL[]    a_url = null;
  int     a_pa_timeout, a_waitlists, a_nbr_in, a_nbr_in_thresholds;
  int     a_waitime;
  int     a_killtime;
  int     a_threads;
  boolean a_type;
  String  a_start_threads;
  boolean a_thstart; 
  float   a_overall, a_indiv, a_factor, a_average;

  // area header used for all queues
  AreaBase Area;
  
  // class object used for all queues       
  Class<?> C1 = null;
    
  // setup for all the queues for object method search  
  Object[] o_args   = new Object[1];
  o_args[0]         = "Tymeac";
  Class[] obj_class = new Class[] { o_args.getClass() }; 
  
  // url class loader
  URLClassLoader urloader = null;

  // the main Tymeac storage table
  T.setMain_tbl(new AreaList(nbr_que));
  
  // just built
  AreaList main = T.getMain_tbl();
  
  // *--- load all the queues and maybe start its threads ---*
  
  // do each queue
  for  (i = 0; i < nbr_que; i++) {
  
    // get que data 
    a_name          = full_tiq[i].getName();
    a_start_threads = full_tiq[i].getStartThreads();
    a_pa_timeout    = full_tiq[i].getPa_timeout();
    a_waitlists     = full_tiq[i].getNbrWaitlists();
    a_nbr_in        = full_tiq[i].getNbrEntries();
    
    a_nbr_in_thresholds = full_tiq[i].getNbrEntriesThresholds();
    
    // When logical waitlist slots invalid, set s/a physical
    if  ((a_nbr_in_thresholds == 0) ||     
         (a_nbr_in_thresholds > a_nbr_in)) a_nbr_in_thresholds = a_nbr_in;
    
    a_waitime       = full_tiq[i].getWaitime();
    a_killtime      = full_tiq[i].getKillime();
    a_threads       = full_tiq[i].getNbrThreads();
    a_overall       = full_tiq[i].getOverall();
    a_indiv         = full_tiq[i].getIndividual();
    a_factor        = full_tiq[i].getFactor();
    a_average       = full_tiq[i].getAverage();      

    // separate the Class from the URL
    a_full_class = full_tiq[i].getPA();
    cls_url      = TyFormat.doClass(a_full_class);
                          
    // what came back
    switch (cls_url.getResult()) {
        
      case 1:
        // just a class
        a_class = cls_url.getClassName();
        
        // load the class 
        try {   
          C1 = T.getLoader().loadClass(a_class);        
                  
        } // end-try       
              
        catch (Exception e) {

          // send out
          doMsg(TyMsg.getMsg(31)
                + a_name
                + TyMsg.getText(6)
                + a_class
                + TyMsg.getText(7)
                + e.toString());

          // all done
          return false;

        } // end-catch 

        // done
        break;

      case 2:
        // both class and url
        a_class = cls_url.getClassName();
        a_url    = cls_url.getUrlName();
        
        try {          
          // url class loader
          urloader = new URLClassLoader(a_url);
             
          // get the loaded class
          C1 = urloader.loadClass(a_class); 
  
        } // end-try       
              
        catch (Exception e) {

          // send out
          doMsg(TyMsg.getMsg(31)
                + a_name
                + TyMsg.getText(6)
                + a_class
                + TyMsg.getText(7)
                + e.toString());

          // all done
          return false;
          
        } // end-catch

        // done 
        break;

      case 0:
      case 3:
        // bad class::url format
        doMsg(TyMsg.getMsg(48)
              + a_name
              + TyMsg.getText(19)
              + a_full_class
              + TyMsg.getText(20));

        // all done
        return false;

      case 4:
        // bad url format
        doMsg(TyMsg.getMsg(49)
              + a_name
              + TyMsg.getText(21)
              + a_full_class
              + TyMsg.getText(22));

        // all done
        return false;
        
      default: return false;

    } // end-switch          

    // check wait time, threads and waitlists
    
    // When wait time < 1, set as 1
    if  (a_waitime < 1)  a_waitime = 1;

    // When threads < 1, ng
    if  (a_threads < 1) {
      
        // send out
        doMsg(TyMsg.getMsg(39)
              + a_name
              + TyMsg.getText(11));

        // all done
        return false;

    } // endif
        
    // When waitlists < 1, ng
    if  (a_waitlists < 1) {
                  
        // send out
        doMsg(TyMsg.getMsg(40)
            + a_name
            + TyMsg.getText(12));

        // all done
        return false;

    } // endif
              
    // When waitlist entries < 1, ng
    if  (a_nbr_in < 1) {

        // send out
          doMsg(TyMsg.getMsg(41)
            + a_name
            + TyMsg.getText(13));

        // all done
        return false;

    } // endif          

    // When starting threads
    a_thstart = ((a_start_threads.length() > 2) &&
                 (a_start_threads.substring(0,3).equalsIgnoreCase("yes")))?
                 true : false;

    // When a normal Queue
    a_type = ((full_tiq[i].getQueType().length() > 2) &&
              (full_tiq[i].getQueType().substring(0,3).equalsIgnoreCase("yes")))?
              false : true;
              
    // load an area header
    Area = new AreaBase(T,   // base
              a_type,       // oa = true, normal = false
              a_name,       // que name
              a_class,      // pap class name
              a_pa_timeout, // timeout value  
              que_seq,      // seq number
              a_waitlists,  // number of wl
              a_waitime,    // max wait time
              a_killtime,   // thread life
              a_threads,    // nbr threads
              a_nbr_in,     // nbr in wl
              a_nbr_in_thresholds, // nbr in wl for thresholds 
              a_overall,    // %
              a_indiv,      // %
              a_factor,     // factor
              a_average);   // %
                
    // insert area addr in main table
    main.addArea(Area);
      
    // bump seq number
    que_seq++;    
    
    // When url class, set loader in Queue
    if  (cls_url.getResult() == 2)  Area.setQueClassLoader(urloader); 

    try {                                   
      // save the processing application class with object signiture 
      Area.setNewMethod(C1.getMethod("main", obj_class));
                  
    } // end-try       
              
    catch (SecurityException e) {

      // send out
       doMsg(TyMsg.getMsg(50)
        + a_name
        + TyMsg.getText(6)
        + a_class
        + TyMsg.getText(23)
        + e.toString());

      // all done
      return false;

    } // end-catch  
    
    catch (NoSuchMethodException e) {

      // send out
      doMsg(TyMsg.getMsg(51)
            + a_name
            + TyMsg.getText(6)
            + a_class
            + TyMsg.getText(24)
            + e.toString());

      // all done
      return false;

    } // end-catch
    
    catch (Exception e) { 

      // send out
      doMsg(TyMsg.getMsg(51)
            + a_name
            + TyMsg.getText(6)
            + a_class
            + TyMsg.getText(119)
            + e.toString());

      // all done
      return false;

    } // end-catch 
    
    catch (Error e) { 

      // send out
      doMsg(TyMsg.getMsg(51)
            + a_name
            + TyMsg.getText(6)
            + a_class
            + TyMsg.getText(119)
            + e.toString());

      // all done
      return false;

    } // end-catch
    
    // When starting all threads, do so
    if  (a_thstart) Area.startAllThreads();
    
  } // end-for

  // done
  return true;

} // end-method

/**
 * Load all the queues
 * @return boolean
 */
protected boolean createQueues () 
        throws Throwable {
    
  // add the internal queues to the user queues
  TymeacIndividualQueue[] tiq = internalQueueLoad(suFields.getTiq());
  
  // When failed, get out
  if  (tiq == null) return false;
  
  // build the Tymeac Queue structure
  return buildQueues(tiq);
  
} // end-method 

/**
 * Load the internal queues from the user classes and add both user and
 * internal queues together.
 * @return TymeacIndividualQueue[] both internal and external
 */
private TymeacIndividualQueue[] internalQueueLoad (TymeacIndividualQueue[] tiq ) 
        throws Throwable {
  
  // variables
  int nbr_que = 0,
      nbr_user_que = tiq.length,
      i, j;  

  // Queue Class
  TymeacInternalQueues que_table = null;
  
  // all the queues
  TymeacIndividualQueue[] full_tiq = null;

  try {     
    // Load the Queue Class
    que_table = new TymeacInternalQueues();

  } // end-try

  catch (NoClassDefFoundError e) {

    // not found 
    doMsg(TyMsg.getMsg(33)
              + TyMsg.getText(104)
              + TyMsg.getText(106));
    
    // not found, no internal 
    return null;

  } // end-catch

  try { 
    // get number of queues
    nbr_que = que_table.getNbrQueues();

    // When < 1 no good
    if  (nbr_que < 1) {

        // send out
        doMsg(TyMsg.getMsg(34)
              + TyMsg.getText(104)
              + TyMsg.getText(105));
  
        // all done
        return null;  

    } // endif
  } // end-try

  catch (NoSuchMethodError e) {

    // not found 
    doMsg(TyMsg.getMsg(46)
             + TyMsg.getText(115)
             + e);

    // all done
    return null;  

  } // end-catch

  catch (Exception e) {

    // not found 
    doMsg(TyMsg.getMsg(46) 
            + TyMsg.getText(115)
            + e);

    // all done
    return null;  

  } // end-catch
  
  // Queue definitions
  TymeacIndividualQueue[] internal_tiq = que_table.getQueues();
  
  // *--- check for nulls & dups internally ---*

  // temp set for checking. HashSet rejects duplicates
  HashSet<String> hs = new HashSet<String>(nbr_que + tiq.length);
  
  // for error msg
  i = 0;
  
  // do each detail
  for (TymeacIndividualQueue que : internal_tiq) {
    
    // When null, no good
    if  ((que == null) ||
         (que.getName() == null)) {

        // send out
        doMsg(TyMsg.getMsg(27)
            + i
            + TyMsg.getText(3));

        // all done
        return null;  

    } // endif
    
    // When Not added, is a duplicate
    if (!hs.add(que.getName())) {

        // send out
        doMsg(TyMsg.getMsg(28)
            + internal_tiq[i].getName()
            + TyMsg.getText(4));

        // all done
        return null; 

    } // endif
    
    // next i
    i++;
    
  } // end-for
  
  // *--- check for dups externally ---* 
  
  // do each detail in the main list
  for (TymeacIndividualQueue que : tiq) {
    
    // When Not added, is a duplicate
    if  (!hs.add(que.getName())) {

        // can't use the table
        doMsg(TyMsg.getMsg(30)
            + que.getName()
            + TyMsg.getText(5));

        // all done
        return null;
         
    } // endif
  } // end-for 
  
  // *--- put all the queues into one array ---*
  
  int total_queues = nbr_que + nbr_user_que;
  
  // new array
  full_tiq = new TymeacIndividualQueue[total_queues];
  
  // load user queues
  for (i = 0; i < nbr_user_que; i++) {
    
    // each user queue
    full_tiq[i] = tiq[i];
      
  } // end-for
  
  // load internal queues
  for (j = 0; j < nbr_que; j++, i++) {
    
    // each user queue
    full_tiq[i] = internal_tiq[j];
      
  } // end-for
          
  // all done   
  return full_tiq;
  
} // end-method

/**
 * General error routine 
 * @param error java.lang.String
 * @exception java.lang.Throwable The exception description.
 */
private void doMsg(String error) 
    throws java.lang.Throwable {

  // When logging, do so
  if  (T.isLogUsed()) T.getLog_tbl().writeMsg(error, 10);
  
  // When printing, do so
  if  (T.getSysout() == 1)  TyBase.printMsg(error);
  
  // When actavation, throw the error 
  if  (base.isActivatable())  throw new Throwable(error); 

} // end-method 
} // end-class
