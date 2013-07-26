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
import java.util.concurrent.*; 

/**
 * This is the main implementation logic for the Tymeac Server. It doesn't matter
 *    what the front end is -- RMI, local call, Sockets, etc. Each front end method
 *    simply calls the identical method in this class passing the same parameters and
 *    returns the same Object. The front end methods must handle any exceptions (such
 *    as RemoteException). This class doesn't catch most exceptions. They go up the chain
 *    to the front end class.
 */ 
public final class TymeacImpl { 
  
  // base of shared storage
  private final TyBase Ty; 

  // array pointers
  private final AreaList      main_tbl; // Main array of Queues     
  private final FuncHeader    func_tbl; // Function array   
  private final RequestHeader request_tbl; // requests
  private final StallHeader   stall_tbl; // Stall array
  private final GenTable      gen_tbl; // Number generation array
  
  // start up time
  private final long start_time;
  
  // async request return messages  
  private final String ar_good;
  
  private static final String ar_base = "Tymeac AR(";
  private static final String ar_4020 = "Tymeac AR(4020)";
  private static final String ar_4030 = "Tymeac AR(4030)";
  private static final String ar_4040 = "Tymeac AR(4040)";  
  private static final String ar_5010 = "Tymeac AR(5010)";
  private static final String ar_5060 = "Tymeac AR(5060)";
  private static final String ar_5065 = "Tymeac AR(5065)";     
  
  // sync request return messages 
  private static final String sr_base = "Tymeac SR(";  
  private static final String sr_good = "Tymeac SR(0000)";
  private static final String sr_4020 = "Tymeac SR(4020)";
  private static final String sr_4025 = "Tymeac SR(4025)";
  private static final String sr_4030 = "Tymeac SR(4030)";
  private static final String sr_4040 = "Tymeac SR(4040)";
  private static final String sr_4050 = "Tymeac SR(4050)";
  private static final String sr_4060 = "Tymeac SR(4060)";
  private static final String sr_4070 = "Tymeac SR(4070)";
  private static final String sr_4120 = "Tymeac SR(4120)";
  private static final String sr_4130 = "Tymeac SR(4130)";
  private static final String sr_4140 = "Tymeac SR(4140)";
  private static final String sr_60   = "Tymeac SR(60";
  
  // shut down return messages 
  private static final String sd_2020 = "Tymeac SD(2020) ";

/**
 * Constructor
 * @param TyBase T  
 */ 
public TymeacImpl (TyBase T) {

  // load base of shared storage
  Ty = T;  

  // set the other pointers
  main_tbl    = Ty.getMain_tbl();
  func_tbl    = Ty.getFunc_tbl();
  request_tbl = Ty.getRequest_tbl();
  stall_tbl   = Ty.getStall_tbl();
  gen_tbl     = Ty.getGen_tbl();
  
  // Tymeac start up time
  start_time = Ty.getStart_time();
  
  // basic async request good return message
  ar_good = "Tymeac AR(0000)[" 
          + start_time 
          + ","; 

} // end-constructor

/**
 * Alter server options. Not synchronized since it is not critical. 
 * @param opts
 * @return the elements
 */
public TyAltSvrElements alterSvrOptions( TyAltSvrElements opts) {
  
  // When a request for update
  if  (opts != null) {    
  
      // alter whatever
      Ty.setSysexit(opts.getShut());
      
      boolean changed = false;
      int new_mon = opts.getMonInterval();     // new monitor interval
      int new_min = opts.getInactivate();      // new inactivate time
      int old_mon = Ty.getMonitor_interval();  // current monitor interval
      int old_min = Ty.getInactivateMinutes(); // current inactivate time in minutes
      
      String msg = null; // in case of change
      
      // When using the monitor and could be changing the mon interval
      if  ((old_mon > 0) && (new_mon > 0)) {
        
          // When new monitor interval
          if  (new_mon != old_mon) {
            
              // set new
              Ty.setMonitor_interval(new_mon);
              
              // did a change
              changed = true;              
            
          } // endif        
      } // endif
      
      // When using the activation framework and could be changing
      if  ((old_min > 0) && (new_min > 0)) {
        
          // When new inactivation value
          if  (old_min != new_min) {
            
              // set new
              Ty.setInactivateMinutes(new_min);  
              
              // did a change
              changed = true;              
            
          } // endif        
      } // endif
      
      // When anything changed, same message as start up
      if  (changed) {
        
          // When using the Activation system, inactivate
          if  (old_min > 0) {
        
              // full message 
              msg = TyMsg.getMsg(3013)
                    + Ty.getMonitor_interval() 
                    + TyMsg.getText(86)
                    + TyMsg.getText(87) 
                    + Ty.getInactivateMinutes()
                    + TyMsg.getText(88); 
          }
          else {    
              // no inactivation
              msg = TyMsg.getMsg(3013) 
                    + Ty.getMonitor_interval() 
                    + TyMsg.getText(86);  
          } // endif
          
          // say whatever
          msgOut(msg, 5);        
        
      } // endif      
  } // endif
  
  // give back what is here now
  return new TyAltSvrElements(
                         Ty.isSysexit(),
                         Ty.getMonitor_interval(),
                         Ty.getInactivateMinutes()); 

} // end-method

/**
 * The asynchronous request.
 *  The return is an object array, the only entry of which is the Tymeac
 *   information. The return is an array to keep it consistent with the
 *   syncRequest(), for prior releases and for any future enhancement.
 *
 * @return Object[]
 * @param Req TymeacParm
 */ 
public Object[] asyncRequest (TymeacParm Req ) {
    
  // When in shut down mode, bye
  if  (Ty.getEndit() != TymeacInfo.AVAILABLE) return backMsg(ar_4030); 
  
  // get the detail entry from the function array  
  FuncDetail F = func_tbl.getFunction(Req.getFuncname());
  
  // not found, is error
  if  (F == null) return backMsg(ar_4040); 
  
  // increment times used 
  F.addUsed();
  
  // get the Que array for this Function
  AreaBase[] qtbl = F.getTbl(); 
    
  // save number of queues
  int nbr_que = qtbl.length;
  
  // output agent que, if there
  AreaBase agent = F.getAgent();
  
  // When there is an output agent, try fail-fast checking  
  if  (agent != null) {
  
      // When not an agent queue
      //  There is probably system damage. This was checked on start up.
      if  (agent.isNormalQueue()) return backMsg(ar_5010 + (nbr_que + 1 ) + ")");
      
      // When there is no thread available, return with error  
      if  (agent.getThreadsAnchor().getThAvail() != 0) return backMsg(ar_5060); 
  
      // When there is no Wait List entry available, return with error
      if  (!agent.getWaitlist().getWlAvail()) return backMsg(ar_5065);
      
  } // endif
    
  // get next async seq number and bump up by one
  long next_seq = gen_tbl.nextAsync();
   
  int i, schd_rc = 0;
   
  // Priority, (wait list number)
  int priority = Req.getPriority();
            
  // get a new Async Request array entry
  RequestDetail request = request_tbl.assignAsync(
                            next_seq,  // name of this req. 
                            qtbl,      // names of the queues
                            Req.getInput(), // input
                            F); // function 
  
  // When the new allocate failed
  if  (request == null) return backMsg(ar_4020); 
    
  // Schedule each Queue 
  for (i = 0; i < nbr_que; i++) {
 
    // schedule          
    schd_rc = schedule(qtbl[i], request, priority);
           
    // when return is not zero, 
    if  (schd_rc != 0)  {

        // backout all previously scheduled Queues              
        backoutAsync(next_seq, qtbl, i, request, schd_rc); 
        
        // back with reason why
        return backMsg(ar_base + schd_rc + (i + 1) + ")");    
                     
    } // endif 
  } // end-for
  
  // successfull return message
  return backMsg(ar_good + next_seq + "]");  
  
} // end-method

/**
 * A request has failed for some reason. Put the msg in an Object array as the
 *   first entry. 
 * @param msg String
 * @return Object[]
 */
private Object[] backMsg (String msg) {

  // object array
  Object[] back = new Object[1];
  
  // first entry
  back[0] = msg;
  
  return back;
  
} // end-method

/**
 * An async request has failed scheduling, backout all previously scheduled queues
 * @param table AreaBase[] - queues to backout
 * @param last int - last queue to finish
 * @param ar_nbr int - async array entry
 * @param why int - why failed
 */
private void backoutAsync (long uni, AreaBase[] table, int last, RequestDetail request, int why) {
  
  // number of elements
  int max = table.length, 
      i; // work
  
  // set the backout indicator in the AR array
  request.setBackout(uni);
    
  // When first in list (no others were scheduled)
  if  (last == 0) {
    
      // purge it
      request.setFree();

      // bye
      return;
    
  } // endif  
  
  // negate current failure and all that did not schedule
  for (i = last; i < max; i++) {
        
    // decrement number and 
    // When number remaining is none       
    if  (request.setDecrement(table[i]) == 0) {
      
        // purge it
        request.setFree();

        // bye
        return;
      
    } // endif
  } // end-for
  
  /*
   * Go backward trying to backout all the prior queues that were
   *   scheduled.
   */
        
  // do all the queues from current backward                  
  for (i = last - 1; i >= 0; i--) {
        
      // reset and when entry purged, get out
      if  (backoutQueue(request, table[i])) return; 
      
  } // end-for
      
  // When the nbr remaining to be processed is zero
  if  (request.getRemaining() == 0) {
                       
      // purge it
      request.setFree();

      // bye
      return;
      
  } // endif
  
  // finished with all the backing out, some remain unprocessed 
    
  // add a stall entry
  stall_tbl.addEntry(uni, why);
  
} // end-method

/**
 * Backout a Request Queue
 * @param request int - [a]sync request number
 * @param que AreaBase
 * @return boolean finished or not 
 */
private boolean backoutQueue (RequestDetail request, AreaBase que) {

  // When able to reset the request in a wait list
  if  (que.getWaitlist().resetEntry(request)) {
    
      // decrement count and null queue name in current queue
      // When done
      if  (request.setDecrement(que) == 0) {
        
          // free the entry
          request.setFree();
          
          // all done
          return true;
          
      } // endif 
  } // endif 
  
  // not done
  return false;
  
} // end-method

/**
 * A sync request has failed scheduling. Backout all previously scheduled queues.
 * @param sr_nbr int - sync request table entry
 * @param atThis int - starting here
 * @param que AreaBase[] - the queue table
 */
private void backoutSync (long uni, RequestDetail request, int atThis, AreaBase[] que) {  
  
  // say backing out    
  request.setBackout(uni);
  
  // number of elements
  int max = que.length;
  
  // When first in list (no others were scheduled)
  if  (atThis == 0) {
    
      // free the entry
      request.setFree();
      
      // all done
      return;
      
  } // endif 
  
  // failure position
  int i = atThis; 
  
  // negate current failure and all that did not schedule
  for (; i < max; i++) {     

    // decrement table count and null queue name in current queue
    request.setDecrement(que[i]);
    
  } // end-for
  
  /*
   * Go backward trying to backout all the prior queues that were
   *   scheduled
   */

  // do all the queues that were scheduled
  for (i = atThis - 1; i >= 0; i--) {

      // reset and 
      // When freed entry, get out 
      if  (backoutQueue(request, que[i])) return; 
               
  } // end-for
  
  // When the nbr remaining to be processed is zero, free the sync table entry
  if  (request.getRemaining() == 0)  request.setFree();
      
  // did not release entry, monitor will do it  
     
} // end-method

/**
 * Backout a timed-out or cancelled sync Request
 * @param sr_nbr int - sync request number
 * @param que AreaBase[] queue table  
 */
private void backoutTimedout (long uni, RequestDetail request, AreaBase[] que) {
  
  // say backing out    
  request.setBackout(uni);
  
  // work
  int max = que.length;

  // do all the queues 
  for (int i = 0; i < max; i++) {
         
      // reset and When all Queue are done get out
      if  (backoutQueue(request, que[i])) return; 
      
  } // end-for
   
} // end-method

/**
 * Two versions.
 *   One, Have a Client supplied a cancel word.
 *   Two, Have both the Tymeac supplied session id and request id. 
 * Cancel the request.       
 * 
 * @return int
 * @param forCancel com.tymeac.base.CancelParm
 */
public int cancelSyncReq(CancelParm forCancel) {
  
  // cancel word
  long cancel_word = forCancel.getCancelWord(); 

  // When cancel word passed
  if  (cancel_word != 0) {              
      
      // When exists      
      if  (request_tbl.matchCancelWord(cancel_word)) {
                    
          // try to cancel
          return (request_tbl.cancelWordRequest(cancel_word))?
              CancelParm.SUCCESSFUL : CancelParm.INVALID_REQUEST_ID;
                                         
      }
      else {
          // no good
          return CancelParm.INVALID_CANCEL_WORD;
          
      } // endif  
  } // end if
        
  // When not current session, not this session
  if  (forCancel.getSession() != Ty.getStart_time()) return CancelParm.INVALID_SESSION_ID;
      
  // try to cancel using request id
  return (request_tbl.cancelSyncRequest(forCancel.getRequest()))?
      CancelParm.SUCCESSFUL : CancelParm.INVALID_REQUEST_ID;  

} // end method

/**
 * concatenate all the return objects into one object array.
 * @return Obj[]
 * @param output int[]
 */
private Object[] concatObjects(Object[] output) {

  // When there are no outputs, only one occurance
  if  (output == null) return new Object[1];

  // return objects[]
  Object[] back;

  // work
  int i, j, nbr_outs = output.length, new_outs = 0;

  // count outputs actually stored (not null)
  for  (i = 0; i < nbr_outs; i++) {
                    
    // When used
    if  (output[i] != null) {

        // bump count
        new_outs++;  
    }   
    else {
        // all done, not every slot used 
        break;

    } // endif 
  } // end-for

  // When there are no outputs, all done, return one occurance for string message
  if  (new_outs == 0)  return new Object[1];

  // Object[0] is for String message, Object[1-n] for Client data
  back = new Object[new_outs + 1];

  // move all outputs stored
  for  (i = 0, j = 1; i < new_outs; i++, j++) {
                    
    // get the output
    back[j] = output[i];

  } // end-for

  // give back what was found
  return back;

} // end-method

/**
 * Internally used during testing. Basically, you can put anything here to help pinpoint the
 *   problem. Multi-threaded applications do weird things when run at full bore. So, let it
 *   rip and use the print methods herein to display the internal tables at various times.
 *   
 *   The various displays come out on the server window.
 *   
 *   The return msg here is to let the caller know that it got this far.
 *    
 * @param req TyParm - the encoded request
 * @return java.lang.String
 */ 
public String  diagnoseRequest (TyParm req) {    
    
    int p1, p2;
    
    p1 = req.getParm1();
    p2 = req.getParm2();
    
    
    // 1,2 is Sync table display all active details
    if  ((p1 == 1) &&
         (p2 == 2)) {
        
        request_tbl.dsplyActiveSyncDetails();
        
         return "SyncDetail display Ok";
         
    } // endif
    
    // 2,2 is Async table display all active details
    if  ((p1 == 2) &&
         (p2 == 2)) {
        
        request_tbl.dsplyActiveAsyncDetails(); 
        
         return "AsyncDetail display Ok";
         
    } // endif
    
    // 3,x is Stall table display all active details
    if  (p1 == 3) {
                       
         stall_tbl.dsplyUsedEntries();
        
         return "Stall display Ok";
         
    } // endif
    
    
    // 4,x is Area table display 
    if  (p1 == 4) {
          
          // get the Queue 
          AreaBase area = main_tbl.getArea(p2);
          
          // When valid
          if  (area != null) {
          
             area.getThreadsAnchor().dsplyThreads();
             area.getWaitlist().dsplyWaitLists();
             
             return "Area display Ok";
             
          }
          else {
            
              return "Area Invalid";
            
          } // endif         
    } // endif    
    
    // 5,1 is Func table display 
    if  ((p1 == 5) &&
          (p2 == 1)) {
                
         func_tbl.dsplyFunctions();
         
         return "Function display Ok";
         
    } // endif
    
    // 5,2 is Func que display 
    if  ((p1 == 5) &&
          (p2 == 2)) {
                
         func_tbl.dsplyFuncQues();
         
         return "Function ques Ok";
         
    } // endif 
    
     
    
    return "Invalid Parameters";
  
} // end-method

/**
 * This gets the list of matching function names for the Func Display GUI.
 * @param func String
 * @return String[]
 */ 
public String[] func1Request (String func) {
        
  // get the list of matches
  return func_tbl.matchName(func);    
      
} // end-method

/**
 * Get the status of, or cancel an asynchronous request
 * @param id long - system time stamp
 * @param req long - request id
 * @param action int - action to take
 * @return int
 */  
public int idStatus1Request (long id, long req, int action) {    
            
  // When not the same time_stamp, no good
  if  (id != Ty.getStart_time()) return 5;

  // When this > highest used, bye
  if  (req > gen_tbl.getAsync()) return 3;
  
  // When stalled, say so
  if  (stall_tbl.matchRequest(req)) return 6;

  // get status
  int back = request_tbl.findRequest(req);

  // When a cancel request
  if  (action == 2) { 
   
      // When waiting to execute or executing
      if  ((back == 8) || (back == 10)) {

          // try to cancel it
          back = request_tbl.cancelAsyncRequest(req); 

      } // endif
  } // endif

  // give back 
  return back;  
  
} // end-method

/**
 * Get a new copy of a Processing Application Class
 * @param que String - Queue
 * @param pa_class String - new Procesing Application Class
 * @return int
 */  
public int newCopyRequest (String que, String pa_class)  {  
  
  // area header 
  AreaBase area;
 
  // class object         
  Class<?> C1;

  // setup for all the queues for object method search  
  Object[] o_args = new Object[1];
  o_args[0]       = "Tymeac";
  Class[]  obj_class = new Class[] { o_args.getClass() };
  
  // Method address
  java.lang.reflect.Method pap_method = null;

  // work
  boolean newLoader = false;

  //  url loader
  URLClassLoader urloader = null;

  // get the que area
  area = main_tbl.getAreaBase(que);

  // When any error, say its a que name error
  if  (area == null) return 3;

  // When no class name was passed
  if  ((pa_class == null) || (pa_class.length() < 1)) {
    
      // reload from original url
        
      // loader
      urloader = area.getQueClassLoader();

      // When no url classloader
      if  (urloader == null) return 6;

      // get the url from the loader
      URL[] origURL = urloader.getURLs();

      // get new loader
      urloader = new URLClassLoader(origURL);

      try {
        // load the sucker
        C1 = urloader.loadClass(area.getPaClass());

        // find the main method       
        pap_method  = C1.getMethod("main", obj_class); 
      
      } // end-try

      catch (Exception e) { 

        // error
        return 7;

      } // end-catch
   
      // set the new method for the area header
      area.setNewMethod(pap_method); 

      // set the new loader for the area header
      area.setQueClassLoader(urloader); 

    // good
    return 0; 

  } // endif  

// *** split the Class and URLs ***  

  // class and url
  ClsUrl  cls_url = null;
    
  // separate the Class from the URL
  cls_url = TyFormat.doClass(pa_class);
                                
  // what came back
  switch (cls_url.getResult()) {
          
    case 1: // *** no URLs
        
      // When NOT originally loaded by a URLClassLoader
      if  (area.getQueClassLoader() == null) {
 
            // When same class
          if  (cls_url.getClassName().compareTo(area.getPaClass()) == 0) {
  
               // ng
              return 10;
  
          } // endif

          // new processing application class
          try {
            // load the class
            C1 = Ty.getLoader().loadClass(cls_url.getClassName());
  
            // find the main method       
            pap_method  = C1.getMethod("main", obj_class);
  
          } // end-try
 
          catch (Exception e) {

            // new class load error
            return 2;

          } // end-catch 
      }
      else {
          // use the original url loader

          // new processing application class
          try {
            // load the class
            C1 = area.getQueClassLoader().loadClass(cls_url.getClassName());

            // find the main method       
            pap_method  = C1.getMethod("main", obj_class);

          } // end-try
 
          catch (Exception e) {

            // new class load error
            return 2;

          } // end-catch 
      } // endif

      // done
      break;
    
    case 2: // *** URLs, use the URL ClassLoader

      // is a new loader
      newLoader = true;
 
      // loader
      urloader = new URLClassLoader(cls_url.getUrlName());

      try {
        // load the sucker
        C1 = urloader.loadClass(cls_url.getClassName());

        // find the main method       
        pap_method  = C1.getMethod("main", obj_class); 

      } // end-try

      catch (Exception e) { 

        // error
        return 2;

      } // end-catch

      // done
      break; 

    case 0:
    case 3:
      // done
      return 8;

    case 4:
      // bad url format

      // done
      return 9;
            
  } // end-switch
   
  // set the new method for the area header and threads
  area.setNewMethod(pap_method); 
  
  // and the new class name
  area.setNewClass(cls_url.getClassName());

  // When a new urloader, set the new loader for the area header
  if  (newLoader) area.setQueClassLoader(urloader);  

  // good
  return 0; 
      
} // end-method

/**
 * Reset or set a New Runtime Function (notify, log or stats)
 * @param type of request
 * @param one String when DBMS or Alt
 * @param two String when dir and/or file
 * @return what happened
 */
public int newRunTimeFunctions(int type, String one, String two) {
  
  // Notify, Log or Stats
  switch (type) {
  
    case SetUpNotify.NotifyReset: 
    case SetUpNotify.NotifyNew: 
    case SetUpNotify.NotifyStop:
      
      // do notify function
      return newRTNotify(type, one);
      
    case SetUpLog.LogReset:
    case SetUpLog.LogNewDBMS:
    case SetUpLog.LogNewFile:
    case SetUpLog.LogNewAlt:
    case SetUpLog.LogStop:
      
      // do log function
      return newRTLog(type, one, two);
      
    case SetUpStats.StatsReset:
    case SetUpStats.StatsNewDBMS:
    case SetUpStats.StatsNewFile:
    case SetUpStats.StatsNewAlt:
    case SetUpStats.StatsStop:
      
      // do stats function
      return newRTStats(type, one, two);
    
    // programming error
    default: return 999;      
      
  } // end-switch
  
} // end-method

/**
 * Reset or New logging
 * @param type reset or new
 * @param one String new DBMS or Alt
 * @param two String new directory or file name
 * @return int what happended
 */
private synchronized int newRTLog (int type, String one, String two) {
  
  // log setup object
  SetUpLog log = new SetUpLog(Ty);
  
  switch (type) {
  
    // reset current
    case SetUpLog.LogReset:
            
      // reset the old log environment
      return log.resetLogOld();
    
    // new with 1 String DBMS Table name
    case SetUpLog.LogNewDBMS:
      
      // log init with DBMS log table
      log.initLog(one, null, null, null);
      
      // verify and set new log 
      return log.finiLogNew(); 
      
    // new with 2 positional Strings Dir (null permitted) and/or File  
    case SetUpLog.LogNewFile: 
      
      // log init with dir and file log table
      log.initLog(null, one, two, null);
      
      // verify and set new log 
      return log.finiLogNew(); 
      
    // new with 1 String Alt class
    case SetUpLog.LogNewAlt:
      
      // log init with Alt class
      log.initLog(null, null, null, one);
      
      // verify and set new log 
      return log.finiLogNew(); 
      
    // Stop service
    case SetUpLog.LogStop:
      
      // stop logging 
      return log.stopService(); 
      
    // programming error
    default: return 997;
    
  } // end-switch
  
} // end-method

/**
 * Reset or New Notify function. 
 * @param type reset or new
 * @param one new notify function name
 * @return int what happended
 */
private synchronized int newRTNotify (int type, String one) {
  
  // work class
  SetUpNotify notify = new SetUpNotify(Ty);
  
  switch (type) {
  
    // reset current
    case SetUpNotify.NotifyReset: 
      
      // back with what happened
      return notify.resetNotify();
      
    // New Notify function
    case SetUpNotify.NotifyNew: 
      
      // back with what happened
      return notify.newNotify(one);
    
    // Stop service
    case SetUpNotify.NotifyStop:
      
      // stop notification 
      return notify.stopService();   
      
    // programming error
    default: return 998;
  
  } // end-switch
  
} // end-method

/**
 * Reset or New statistics respository
 * @param type reset or new
 * @param one String new DBMS or Alt
 * @param two String new directory or file name
 * @return int what happended
 */
private synchronized int newRTStats (int type, String one, String two) {
  
  // stats setup object
  SetUpStats stats = new SetUpStats(Ty);
  
  switch (type) {
  
    // reset current
    case SetUpStats.StatsReset:
            
      // reset the old stats environment
      return stats.resetStatsOld();
    
    // new with 1 String DBMS Table name
    case SetUpStats.StatsNewDBMS:
      
      // stats init with DBMS stats table
      stats.initStats(one, null, null, null);
      
      // verify and set new stats 
      return stats.finiStatsNew(); 
      
    // new with 2 positional Strings Dir (null permitted) and/or File (null Not permitted) 
    case SetUpStats.StatsNewFile: 
     
      // stats init with dir and file 
      stats.initStats(null, one, two, null);
      
      // verify and set new stats 
      return stats.finiStatsNew(); 
      
    // new with 1 String Alt class
    case SetUpStats.StatsNewAlt:
      
      // stats init with Alt class
      stats.initStats(null, null, null, one);
      
      // verify and set new stats 
      return stats.finiStatsNew(); 
     
    // Stop service
    case SetUpStats.StatsStop:
      
      // stop logging 
      return stats.stopService();   
      
    // programming error
    default: return 996;
    
  } // end-switch
} // end-method

/**
 * New run time functions maintenance. Read only so sync not needed.
 * @param type of elements needed
 * @return elements
 */
public TyRunTimeElements newRTMaint(int type) {
  
  switch (type) {
  
    case SetUpNotify.NotifyMaint: // notify
      
      // notify object
      TyNotifyTable notify = Ty.getNotify_tbl();
      
      // When notify is in effect, get the elements
      return (notify != null)? notify.getElements() : new TyRunTimeElements(false);
      
    case SetUpLog.LogMaint: // log
      
      // logging object
      TyLogTable log = Ty.getLog_tbl();
      
      // When notify is in effect, get the elements
      return (log != null)? log.getElements() : new TyRunTimeElements(false);
      
    case SetUpStats.StatsMaint: // stats
      
      // stats object
      TyStatsTable stats = Ty.getStats_tbl();
      
      // When stats is in effect, get the elements
      return (stats != null)? stats.getElements() : new TyRunTimeElements(false);
      
    default: // oops
      
      return null;
  
  } // end-switch
  
} // end-method

/**
 * Get the overall picture of sync, async and stalled requests in the system.
 * @return TyOverallObj
 * @param req_type int
 */
public TyOverallObj overallRequest(int req_type)  {

  // get number of sync requests pending
  int r_sync = request_tbl.getAllSyncUsed();

  // get number of async requests pending
  int r_async = request_tbl.getAllAsyncUsed();

  // get number of async requests stalled
  int r_stalled = stall_tbl.getBusy();

  // When not using the detail queue section 
  if  (req_type == 1) {   
      
      // return new obj
      return new TyOverallObj(r_sync,
                              r_async,
                              r_stalled,
                              null);  

  } // endif
      
  // Area header
  AreaBase area;

  // start and end string arrays
  String[] startString, endString;

  // work
  int i, nbr_entries, used_entries = 0;

  // get total number of queues
  nbr_entries = main_tbl.getNbrEntries();

  // get beginning string[]
  startString = new String[nbr_entries];

  // do all the queues
  for  (i = 0; i < nbr_entries; i++) {

    // get the queue area
    area = main_tbl.getArea(i);

    // When any threads are alive
    if  (area.getThreadsAnchor().checkThreads()) {
        
        // get the name of the queue
        startString[used_entries] = area.getName();

        // bump entries
        used_entries++;

    } // endif
  } // end-for

  // When no queues have threads alive
  if  (used_entries == 0) {

      // slip in one entry with none
      endString = new String[1];

      endString[0] = TyMsg.getText(146);
  }
  else {
      // get the full wack
      endString = new String[used_entries];

      // move the old array to the new one
      for (i = 0; i < used_entries; i++) {

        // move
        endString[i] = startString[i];

      } // end-for
  } // endif

  // return new obj
  return new TyOverallObj(r_sync,
                          r_async,
                          r_stalled,
                          endString); 

} // end-method

/**
 * Import button on Que Data 
 * @param que String
 * @return TyQueElements
 */ 
public TyQueElements que1Request (String que) {    
    
  // parm passed back
  TyQueElements tqe = new TyQueElements();    
  
  // When shutting down
  if  (Ty.getEndit() > TymeacInfo.AVAILABLE) {

      // load queue name with this
      tqe.que_name = "N286";

      // send it back
      return tqe;

  } // endif
        
  // area header 
  AreaBase area = main_tbl.getAreaBase(que);
  
  // When any error, say its a que name error
  if  (area == null) {

      // load queue name with this
      tqe.que_name = "N287";

      // send it back
      return tqe;

  } // endif  
  
  // fill in the parm
  tqe.que_name  = que;
  tqe.wait_time = area.getWaitTime();
  tqe.kill_time = area.getKillTime();
  tqe.overall   = area.getOverall();
  tqe.individ   = area.getIndivid();
  tqe.factor    = area.getFactor();
  tqe.average   = area.getAverage();
  tqe.entries   = area.getWaitlist().getNbrPhysicalEntries();
  tqe.th_entries = area.getWaitlist().getNbrLogicalEntries();
  
  // When using the default value
  if  (area.getPaTimeout() == 0) {
        
      // use default
      tqe.timeout = Ty.getPac_timeout(); 
            
  } else {          
      // use specific
      tqe.timeout = area.getPaTimeout();
  
  } // endif
  
  // back
  return tqe;    
  
} // end-method

/**
 * Update button on Que Data
 * @param ele TyQueElements - The queue data
 * @return int
 */ 
public int que2Request (TyQueElements ele) {   
   
 // When shutting down, cannot do
  if  (Ty.getEndit() > TymeacInfo.AVAILABLE) return 2;
        
  // area header 
  AreaBase area = main_tbl.getAreaBase(ele.que_name);
  
  // When any error, say its a que name error
  if  (area == null) return 3;
    
  // set the new values
  area.setWaitTime(ele.wait_time);
  area.setKillTime(ele.kill_time);
  area.setOverall(ele.overall);
  area.setIndivid(ele.individ);
  area.setFactor(ele.factor);
  area.setAverage(ele.average);
  area.getWaitlist().alterPhysicalWlEntries(ele.entries); 
  area.getWaitlist().alterLogicalWlEntries(ele.th_entries);
  area.setPaTimeout(ele.timeout);

  // back
  return 4;
  
} // end-method

/**
 *  Refresh button on Que Thread
 * @param que String - name of queue
 * @return String[]
 */ 
public String[] que3Request (String que) {   
    
  // area header 
  AreaBase area = main_tbl.getAreaBase(que);  
  
  // When any error, say its a que name error
  if  (area == null) {

      // new string with error msg
      String[] S = new String[1];
      S[0] = "N287"; 

      // send it back
      return S;

  } // endif 
  
  // return data
  return area.getThreadsAnchor().fetchThreads(); 
  
} // end-method

/**
 * Disable or enable selected button on Que Thread.
 *   For endable, this method re-uses this method by passing
 *   the selected thread number as a negative value. Otherwise,
 *   would require adding a method to the TymeacInterface. The
 *   next time we update that interface, we'll add a new method
 *   here.
 * @param que String - queue
 * @param nbr int - thread number
 * @return int
 */ 
public int que4Request (String que, int nbr ) {    
    
  // area header 
  AreaBase area = main_tbl.getAreaBase(que);
  
  // When any error, say its a que name error
  if  (area == null) return 0;
  
  // When positive, is disable
  if  (nbr > -1) return area.getThreadsAnchor().disableThread(nbr); 
  
  // get the real number: positive and -1 to include zero
  int my_nbr = (nbr * -1) - 1;
  
  return area.getThreadsAnchor().enableSpecificThread(my_nbr); 
      
} // end-method

/**
 * Enable all button on Que Thread
 * @param que String - queue
 * @return int
 */ 
public int que5Request (String que)  {    
    
  // When shutting down, cannot do
  if  (Ty.getEndit() > TymeacInfo.AVAILABLE) return 2;
        
  // area header 
  AreaBase area = main_tbl.getAreaBase(que);
  
  // When any error, say its a que name error
  if  (area == null) return 3;
        
  // enable all the threads
  area.getThreadsAnchor().enableThreads(); 

  // return successful
  return 0;
  
} // end-method

/**
 * Wait List Display
 * @param que String - queue name
 * @return TyWLElements[]
 */ 
public TyWLElements[] que6Request (String que)  {    
                  
  // area header 
  AreaBase area = main_tbl.getAreaBase(que);
  
  // When any error, say its a que name error
  if  (area == null) {

      // error
      TyWLElements[] twe  = new TyWLElements[1];
      twe[0]              = new TyWLElements();
      twe[0].status = 2;

      // send it back
      return twe;

  } // endif
  
  // get the data
  return area.getWaitlist().getDisplayable();
  
} // end-method

/**
 * Schedule a request on a queue 
 * @param que int - queue number
 * @param req_nbr - request 
 * @param prioity int - request priority
 * @return int
 */ 
private int schedule (AreaBase que,          // queue to schedule
                      RequestDetail request, // request 
                      int priority) {        // priority  
  
  // When in shut down mode, bye
  if  (Ty.getEndit() != TymeacInfo.AVAILABLE) return 5005;
   
  // no good, bye
  if  (que == null) return 5010;
     
  // schedule on the normal queue, priority, is normal queue and return what came back
  return que.schedule(request, priority, true); 
  
} // end-method

/**
 * Schedule a request on an Output Agent queue 
 * @param que int - queue number
 * @param req_nbr - request 
 * @param prioity int - request priority
 * @return int
 */ 
private int scheduleOA (AreaBase que,        // queue to schedule
                      RequestDetail request, // request 
                      int priority) {        // priority  
  
  // When in shut down mode, bye
  if  (Ty.getEndit() != TymeacInfo.AVAILABLE) return 5005;
   
  // no good, bye
  if  (que == null) return 5010;
     
  // schedule OA queue, priority, is OA queue and return what came back
  return que.schedule(request, priority, false); 
  
} // end-method

/**
 * Execute the shut down exit classes
 * @param names String[] - names of the classes 
 */
protected void shutExit(String[] names) {

  // number of classes
  int nbr = names.length;

  // class and url
  ClsUrl cls_url = null;
      
  // do all here
  for (int i = 0; i < nbr; i++) {
    
    // separate the Class from the URL
    cls_url = TyFormat.doClass(names[i]);
                          
    // what came back
    switch (cls_url.getResult()) {
        
      case 1: // good value but not a url      
          try {
            // load the class 
            Ty.getLoader().loadClass(cls_url.getClassName()).newInstance(); 

          } // end-try  
            
          catch (Exception  e) {

            // the error msg
             msgOut(TyMsg.getMsg(2020)
                  + names[i]
                  + TyMsg.getText(119)
                  + e.toString(), 10);
                  
          } // end-catch
          
          catch (Error e) {
            
            // the error msg
            msgOut(TyMsg.getMsg(2020)
                  + names[i]
                  + TyMsg.getText(85)
                  + e.toString(), 10);
                  
          } // end-catch

          // done 
          break;
      
      case 2: // good value, is a URL
          try {   
            // get the url class loader
            URLClassLoader urloader = new URLClassLoader(cls_url.getUrlName()); 
            
            // load the class with a url loader               
            urloader.loadClass(cls_url.getClassName()).newInstance();
            
            // save loader
            Ty.setExit_list(urloader);
                
          } // end-try        
          
          catch (Exception  e) {

            // the error msg
             msgOut(TyMsg.getMsg(2020)
                  + names[i]
                  + TyMsg.getText(119)
                  + e.toString(), 10);
            
          } // end-catch

          catch (Error e) {
            
            // the error msg
            msgOut(TyMsg.getMsg(2020)
                  + names[i]
                  + TyMsg.getText(85)
                  + e.toString(), 10);
                  
          } // end-catch

          // done 
          break;

      case 0:
      case 3: // bad class::url format
          // error msg
           msgOut(TyMsg.getMsg(2021)
                + names[i]
                + TyMsg.getText(28), 10);  

          // done 
          break;

      case 4: // bad format

          // bad url format
           msgOut(TyMsg.getMsg(2022)
                + names[i]
                + TyMsg.getText(29), 10);
                
    } // end-switch
  } // end-for 

} // end-method

/**
 * Shut down Request without force This method is for backward compatability
 * @return java.lang.String
 */ 
public String shutRequest () {  

  // do request without forcing past hanging threads
  return shutRequest(false);
  
} // end-method

/**
 * Shut down Request 
 * @since 4.0.4
 * @param force force shut down
 * @return java.lang.String
 */ 
public String shutRequest (boolean force) { 
  
  // Prevents multiple threads from access. 
  synchronized (Ty) { 
    
    // shut down indicator
    switch (Ty.getEndit()) {
      
      // currently not in shut down      
      case TymeacInfo.AVAILABLE:
       
        // now in stage 1
        Ty.setEndit(TymeacInfo.SHUT_STAGE1);
    
        // origin of shutdown is by request
        Ty.setShutOrigin(TymeacInfo.SHUT_REQUEST);
    
        // done here      
        break;
      
      // currently in stage 1        
      case TymeacInfo.SHUT_STAGE1:
        
        // When Not finished with the shut down stage 1 exits
        if  (!Ty.isStage1Done()) {
      
            // cannot continue, can't start next stage yet 
            return TyMsg.getText(177);  
            
        } // endif
          
        // set stage2 
        Ty.setEndit(TymeacInfo.SHUT_STAGE2);
    
        // done here  
        break;
              
      // currently in stage 2       
      case TymeacInfo.SHUT_STAGE2:
                      
        // set force
        Ty.setEndit(TymeacInfo.SHUT_FORCE);
    
        // done here  
        break;
        
      // currently in force       
      case TymeacInfo.SHUT_FORCE:                      
    
        // done here  
        break;
      
      // currently complete    
      case TymeacInfo.SHUT_COMPLETE: 
      
        // cannot continue, just waiting for termination 
        return TyMsg.getText(72);      
                  
    } // end-switch    
  } // end-sync
  
  // When in stage one
  if  (Ty.getEndit() == TymeacInfo.SHUT_STAGE1) {
    
      // When there is no stage1 exit array of classes
      if  ((Ty.getShut1_array() == null) ||
           (Ty.getShut1_array().length < 1)) {
  
          // ignore
      }
      else {
          // do the exit routine
          shutExit(Ty.getShut1_array());
  
      } // endif 
      
      // finished exit 1 classes
      Ty.setShut1_done();
      
  } // endif

  // total number of queue's in the system   
  int nbr_entries = main_tbl.getNbrEntries();
    
  // total number of async requests working
  int async_busy = request_tbl.getAllAsyncUsed();
  
  // Queue definition 
  AreaBase area;
  
  // none busy so far
  int count = 0;
           
  // loop thru all the Queue Areas checking for threads still active  
  for (int i = 0; i < nbr_entries; i++) {
      
    // get next area addr
    area = main_tbl.getArea(i); 
        
    // say 'shutdown' and accum # threads still busy
    count += area.getThreadsAnchor().setShutdown(async_busy);
    
  } // end-for

  // When some threads are still active
  if  (count > 0) {
  
      // if not a forced shut down
      if  (!force) {
      
          // send "there are threads working" msg
          msgOut(TyMsg.getMsg(2001), 10);
          
          // back with number remaining
          return sd_2020 + count + TyMsg.getText(102);
          
      } // endif      
  } // endif
    
  /*
   * *--- Server is finished ---*  
   * 
   *  Only one thread can continue past here 
   */
  
  // When Not first thread to get here, cannot continue
  if  (!Ty.setServerDone()) return TyMsg.getText(178); 
  
  // When there is NO stage2 exit array of classes
  if  ((Ty.getShut2_array() == null) ||
       (Ty.getShut2_array().length < 1)) {
  
      // ignore
  }
  else {
      // do the exit routine
      shutExit(Ty.getShut2_array());

  } // endif
  
  // When using a stats table, write the stats
  if  (Ty.isStatsUsed()) Ty.getStats_tbl().writeStats();  
  
  // When using the activation framework
  if  (Ty.getTyActivation() != null) {
  
    // try to unregister myself
    try {
      java.rmi.activation.Activatable.unregister(Ty.getTyActivation());
      
    } // end-try
         
    catch (java.rmi.activation.UnknownObjectException e) {

        // log message
        msgOut(TyMsg.getMsg(2027) + e.getMessage(), 10);
    
    } // end-catch
    
    catch (java.rmi.RemoteException e) {
      
        // log message
        msgOut(TyMsg.getMsg(2025) + e.getMessage(), 10);
         
    } // end-catch 
      
    catch (java.rmi.activation.ActivationException e) {
      
        // log message
        msgOut(TyMsg.getMsg(2026) + e.getMessage(), 10);
        
    } // end-catch  
  } // endif
  
  // calc duration
  long diff = (System.currentTimeMillis() - start_time) / 1000;
  long hh1 = diff / 36000;
  long mm1 = diff - (hh1 * 36000);
  long mm2 = mm1 / 60;
  long ss1 = mm1 - (mm2 * 60);  
  String duration = hh1 + ":" + mm2 + ":" + ss1;
  
  // When a forced shutdown
  if  (count > 0) {
    
      // server ended by force with start time 
      msgOut(TyMsg.getMsg(2010) + Ty.getStart_time() + TyMsg.getText(181) + duration, 10);
  }
  else {
      // server ended normally with start time 
      msgOut(TyMsg.getMsg(2009) + Ty.getStart_time() + TyMsg.getText(181) + duration, 10);  
      
  } // endif  
  
  // shut down indicator
  Ty.setEndit(TymeacInfo.SHUT_COMPLETE);
  
  // When there is an embedded data base, try to shut it
  if  (Ty.getDBShut() != null) shutEmbeddedDataBase();
            
  // When using the shutdown thread, start it
  if  (Ty.isSysexit()) new TyShutThread(Ty).start();  

  // When a forced shutdown, bye msg by force or all threads inactive
  return (count > 0)? TyMsg.getText(73) + duration : TyMsg.getText(71) + duration;  
  
} // end-method

/**
 * Try to shut down the embedded data base by instantiating the user class passed at startup.
 */
protected void shutEmbeddedDataBase() {
  
  // new string array
  String[] pass = new String[1];
  
  // only using first entry
  pass[0] = Ty.getDBShut();
  
  // Tymeac is shut down, there is no more logging
  Ty.setLogUsed(false);
  
  // use the shut exit for this
  shutExit(pass);
  
} // end-method

/**
 * Refresh button on stall GUI
 * @return String[]
 */  
public String[] stall1Request () {  
        
  // get the list of stall table entries
  return stall_tbl.getList();
  
} // end-method

/**
 * Purge button on stall GUI
 * @param req long - id of stalled request
 * @return int
 */
public int stall2Request (long req) {    
      
  // purge the stall table entry
  stall_tbl.purgeATname(req);        
    
  // 2=ok
  return 2;
  
} // end-method

/**
 * Detail button on stall GUI
 * @param req long - id of stalled request
 * @return String[]
 */ 
public String[] stall3Request (long req) {
    
  int i, j, nbr_que;
  AreaBase[] que = null; 
  AreaBase agent;
  
  // Stall detail
  StallDetail s_detail = stall_tbl.getMatch(req); 
  
  long ar_name = 0;
  
  // When found
  if  (s_detail != null) {
    
      // get async table entry number 
      ar_name = s_detail.getAtName();
  } 
  else { 
      // new string msg
      String[] S = new String[1];
      S[0] = "N287"; 

      // send it back
      return S;

  } // endif
  
  // Request Detail
  RequestDetail detail = request_tbl.getAsyncDetail(ar_name);
  
  // get the agent if used
  agent = detail.getAgent(); 
  
  // get number of queues 
  nbr_que = detail.getNbr(); 
  
  // get the list of ques
  que = detail.getQueNames(); 
  
  // When no agent and none remain unprocessed, say not found
  if  ((agent == null) && 
       (detail.getRemaining() == 0)) {

      // new string msg
      String[] S = new String[1];
      S[0] = "N287"; 

      // send it back
      return S;

  } // endif
  
  // the agent name
  String agentName = (agent != null)? agent.getName() : null; 
  
  // When only an agent
  if  (nbr_que == 0) {

      // new msg string
      String[] S = new String[1];
      S[0] = agentName;

      // send it back
      return S;
      
  } // endif
  
  // When nothing came back
  if  (que == null) {
        
      // When an agent
      if  (agent != null) {
              
          // pass back only the agent 
          String[] S = new String[1];
          S[0] = agentName; 

          // send it back
          return S;
      }
      else {
          // nothing was found
          String[] S = new String[1];
          S[0] = "N287"; 

          // send it back
          return S;     

      } // endif
  } // endif 
  
  // total queues start at zero
  int tot_nbr = 0; 
  
  // find all that remain, now
  for (i = 0; i < nbr_que; i++) {
          
      // When the que number did not finish processing 
      if  (que[i] != null) {

          // incre count
          tot_nbr++;

      } // endif      
  } // end-for 
  
  // When an agent, add one
  if  (agent != null) tot_nbr++;
  
  // the returning string array
  String[] list = new String[tot_nbr];
  
  // start at the 1st entry
  j = 0;
  
  // When an agent
  if  (agent != null) {
        
      // put it in, bump index
      list[0] = agentName;
      j++;

  } // endif
            
  // put all queues in list
  for (i = 0; i < nbr_que; i++) {
          
      // When not finished
      if  (que[i] != null) {  
              
          // get the name of the queue
          list[j] = que[i].getName();
  
          // incre pointer
          j++;

      } // endif  
  } // end-for        
          
  // give back
  return list;
    
} // end-method

/**
 * Re-schd button on stall GUI
 * @param req long - id of stalled request
 * @return int
 */ 
public int stall4Request (long req) {    
 
  // work 
  int remain;
  long async_name = 0;
  AreaBase agent;
  
  // Stall detail
  StallDetail s_detail = stall_tbl.getMatch(req); 
    
  // When found
  if  (s_detail != null) {
    
      // get async table entry number 
     async_name = s_detail.getAtName();      
  } 
  else {
     // not here
     return 1;
     
  } // endif
  
   // Request Detail
  RequestDetail detail = request_tbl.getAsyncDetail(async_name);
  
  // get the agent if used
  agent = detail.getAgent();
  
  // get remaining
  remain = detail.getRemaining();
    
  // When not an agent, get out 
  if  (agent == null) return 2;

  // When some queues remain unprocessed, get out
  if  (remain  > 0) return 3;
  
  // When already scheduled, get out
  if  (agent.getWaitlist().matchEntry(detail)) return 5;

  // schedule OA Queue
  int rc = scheduleOA(agent, detail, 1);

  // When scheduling ok
  if  (rc == 0) {
    
      // purge the stall table entry
      s_detail.setFree();

      // good return
      return 0;
  
  } // endif
  
  // return with schd failure
  return rc;
  
} // end-method

/**
 * On Request statistics.
 * @return int
 */ 
public int stats1Request () {  
        
  // When using a stats table, write the stats
  return (Ty.isStatsUsed())? Ty.getStats_tbl().onReqStats() : 2;
        
} // end-method

/**
 * Synchronous Request -- Waiting for a reply.
 *   The return is an object array, the first entry of which is the Tymeac
 *   information. The return array always has at least this one entry.
 *   Subsequent objects in the array are from the application.
 *
 * @param Req TymeacParm - input class
 * @return Object[]
 */ 
public Object[] syncRequest (TymeacParm Req) {      

  // When in shut down mode, bye
  if  (Ty.getEndit() != TymeacInfo.AVAILABLE) return backMsg(sr_4030); 
       
  // get the detail entry from the function table  
  FuncDetail F = func_tbl.getFunction(Req.getFuncname());
  
  // not found, is error
  if  (F == null) return backMsg(sr_4040);  
  
  // increment times used 
  F.addUsed();
  
  // get the Que Table for this Function
  AreaBase[] qtbl = F.getTbl();
    
  // save number of queues
  int nbr_que = qtbl.length; 
  
  // for canceling this request with a cancel word
  long cancel_word = Req.getCancelWord();
    
  // When using a cancel word, verify
  if  (cancel_word != 0) {    
  
      // When already exists, error      
      if  (request_tbl.matchCancelWord(cancel_word)) return backMsg(sr_4050);  

  } // endif 
      
  // get next sync seq number 
  long next_seq = gen_tbl.nextSync();

  // Priority, (wait list number)
  int priority = Req.getPriority();
   
  // wait time in seconds and covert to milliseconds + wait time in milliseconds
  int waitime = ((Req.getTime() * 1000)) + Req.getMillis();

  // When invalid, force 1 millisecond
  if  (waitime < 1) waitime = 1;
  
  // latch obj
  CountDownLatch latch = new CountDownLatch(nbr_que);
              
  // get a new Sync Request Detail entry for the request
  RequestDetail request = request_tbl.assignSync(
               Req.getInput(),  // input 
               next_seq,    // unique name
               cancel_word, // for canceling request
               qtbl,       // queues
               waitime,   // wait time in millis
               latch);      // latch obj)
  
  // When the new allocate failed
  if  (request == null) return backMsg(sr_4020); 
  
  // work
  int i, schd_rc = 0;
  
  // schedule each Queue in the request
  for (i = 0; i < nbr_que; i++) {
            
    // schedule the request
    schd_rc = schedule(qtbl[i], request, priority);
   
    // when return is not zero
    if  (schd_rc != 0) {
       
        // do a backout of the prior
        backoutSync(next_seq, request, i, qtbl);

        // say why
        return backMsg(sr_base + schd_rc + (i + 1) + ")");          
      
    } // endif
  } // end-for
  
  /*
   * Check for using call back. 
   *   Callback functionality may result in a blocking state, therefore, callback
   *   needs to be a separate thread. Rather than creating a new thread here and
   *   all the logic to handle a multi-threading environment, we use the Tymeac
   *   asyncRequest() for a TymeacInternalCancel (internal function).
   *   
   *   Errors are fatal. It isn't like processing can continue normally 
   *     or the Client can do something about it. There is system damage.  
   */
  // for canceling this request with a callback
  Object callback = Req.getCallback();
  
  // When using callback, need to schedule an async request
  if  (callback != null) {
    
      // schedule an async request
      int rc = syncRequestCallback(callback, next_seq);      
      
      // When failed
      if  (rc != 0) {
        
          // do a backout (same as a time out since all queues were scheduled)
          backoutTimedout(next_seq, request, qtbl);
          
          // When a function error
          if  (rc == 4010) return backMsg(sr_4060);  
          
          // When an allocate error
          if  (rc == 4020) return backMsg(sr_4025);  
          
          // queue error
          return backMsg(sr_4070); 
        
      } // endif    
  } // endif
  
  /*
   * Wait for the request to complete, time out, or cancel. 
   *   If the request already finished, then the count is already zero. 
   */ 
  try {  
    // wait until latch zero or timeout   
    latch.await(waitime, TimeUnit.MILLISECONDS);
      
  // ignore interrupted, not part of Tymeac  
  } catch (InterruptedException e) { 
  } // end-catch 
  
  // get the cancelled status
  boolean isCancelled = request.isCancelled();
  
  // get the thread completion code (one for all threads)
  int thd_comp = request.getComp();
  
  /*
   * get the nbr remaining to be processed
   * 
   * If the number is greater than zero, meaning this is a time-out,
   *   then another thread may complete after we concatenate
   *   the output objects. That thread's output is lost. The request
   *   did not complete sucessfully. 
   *   
   * If the concatenated outputs contain all the proper objects, 
   *   then this is an oxymoron. The threads completed between the time
   *   we got the count and before we got the outputs. It is up to the
   *   application to parse the output objects to determine success or
   *   failure when Tymeac returns a time-out response.
   *  
   * If the number is equal to zero, meaning either
   *   all threads completed, or
   *   there was a cancel then:
   *     if not a cancel, the request is successful.
   *     if a cancel, the request is unsuccessful (cancel logic can lose output objects.) 
   */
  long nbr_remaining = latch.getCount();
  
  // concat the output objects leaving the first slot free for the string message
  Object[] back = concatObjects(request.getOutput()); 
  
  // When Not the same as what was set above, get out
  //   this must always be the last checked  
  if  (request.getUni() != next_seq) return backMsg(sr_4130);  

  // When none remain, success (maybe), even though the time may have elapsed
  if  (nbr_remaining < 1) {
    
      // When NOT cancelled
      //   (if cancelled we could have lost some processing output)
      if  (!isCancelled) {
        
          // When thread completion code is zero
          if  (thd_comp == 0) {
        
              // return with successfull message and concatenated outputs
              back[0] = sr_good;  
          }
          else {
              // return with error message and concatenated outputs
              back[0] = sr_60 + thd_comp + ")"; 
          
          } // endif
          
          // free the sync table entry
          request.setFree();
          
          // send back
          return back;          
          
      } // endif       
  } // endif

  /*
   * timed out or cancelled. Back out all the queues that can be. 
   *   sync request detail remains 
   *     Will be removed by monitor.
   */  
  
  // backout everything that can be            
  backoutTimedout(next_seq, request, qtbl);  
  
  // When NOT cancelled 
  if  (!isCancelled) {     
      
      // say timed out
      back[0] = sr_4120;
  }
  else {
      // say cancelled
      back[0] = sr_4140;
        
  } // endif
  
  // send back
  return back;
  
} // end-method

/**
 * Create a new async request to process the callback. The function name
 * comes from the internal functions. 
 * 
 * What we're doing is having an internal Processing Application Class 
 * do the actual callback. This way if there is an exception, error or "hanging", 
 * then normal Tymeac async processing can handle it.
 * 
 * There is no fail-fast checking.
 * 
 * @param callback
 * @param sync_request
 * @return int
 */
private int syncRequestCallback(Object callback,
                                 long sync_request) { 
 
  // get the "internal function" detail entry from the function table  
  FuncDetail F = func_tbl.getFunction("TymeacInternalCancel");

  // When not found
  if  (F == null) {
    
      // log
      msgOut(TyMsg.getMsg(4010), 10);
      
      // notify
      sendNotify(TyMsg.getMsg(4010));    
    
      // finished here
      return 4010;
    
  } // endif

  // increment times used 
  F.addUsed();

  // get the Que Table for this Function
  AreaBase[] qtbl = F.getTbl();
  
  // save number of queues
  int nbr_que = qtbl.length;
  
  // get next async seq number 
  long next_seq = gen_tbl.nextAsync();
  
  // new input for cancel request Processing Application Class
  TymeacCancelParm toCancel = 
        new TymeacCancelParm(
            callback,      // callback field from TymeacParm
            sync_request,  // syncRequest() request id
            start_time);   // session id 
              
  // get a new Async Request entry
  RequestDetail request = request_tbl.assignAsync(
                            next_seq, // name of this req
                            qtbl,       // names of the queues
                            toCancel,   // input
                            F);  // function 
  
  // When the new allocate failed
  if  (request == null) {
    
      // log
      msgOut(TyMsg.getMsg(4020), 10);
      
      // notify
      sendNotify(TyMsg.getMsg(4020));    
    
      // finished here
      return 4020;
    
  } // endif
         
  // return
  int schd_rc = 0;
  
  // Schedule each Queue 
  for (int i = 0; i < nbr_que; i++) {
 
    // schedule, priority always 1
    schd_rc = schedule(qtbl[i], request, 1);    
    
    // When failure, say what happended
    if  (schd_rc != 0) { 
      
        // log
        msgOut(TyMsg.getMsg(5010) + schd_rc + (i + 1), 10);
      
        // notify
        sendNotify(TyMsg.getMsg(5010) + schd_rc + (i + 1));
        
        // backout all previously scheduled Queues              
        backoutAsync(next_seq, qtbl, i, request, schd_rc); 
        
        // done
        return 5010;
      
    } // endif      
  } // end-for
  
  // good
  return 0;
 
} // end-method
 
/**
 * Send the message to print and log 
 * @param msg
 * @param time
 */
private void msgOut(String msg, int time) {
  
  // When printing, do so
  if  (Ty.getSysout() == 1) TyBase.printMsg(msg);
  
  // When logging, do so
  if  (Ty.isLogUsed()) Ty.getLog_tbl().writeMsg(msg, time);
  
} // end-method  

/**
 * Send a message to the notification queue
 * @param msg java.lang.String
 */
private void sendNotify (String msg )  {
  
  // When using a notify, pass to the notify class
  if  (Ty.isNotifyUsed()) Ty.getNotify_tbl().sendMsg(msg);
  
} // end-method
} // end-class
