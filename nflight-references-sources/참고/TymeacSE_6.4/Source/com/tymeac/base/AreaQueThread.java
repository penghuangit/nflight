package com.tymeac.base;

import java.lang.reflect.InvocationTargetException;

/*
 * Copyright (c) 1998 - 2010 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */

/**
 * Queue Thread to process normal queues.
 */
public final class AreaQueThread 
          extends AreaBasicThread { 
  
  private final TyBase         ty;    // base storage 
  private final AreaThreadMgmt mgt;   // the mgt struct for this thread 
  private final AreaBase       qArea; // the Area Base for this thread
  
  private RequestDetail request = null;  // the request 
  private boolean expunged = false;  // this thread was expunged 

/**
 * Constructor
 * @param Ty
 * @param AH
 * @param AD
 * @param tname
 */
protected AreaQueThread(TyBase Ty,
                  AreaBase AH,
                  AreaThreadMgmt AT,
                  String tname) {
  
  // up to base                  
  super(Ty,       // common anchor point
        AH,       // Area Header (queue)
        AT,       // Area Threads (thread) 
        tname);    // name of thread
  
  ty    = Ty; // tymeac base
  mgt   = AT; // management object
  qArea = AH; // area base
    
  /*
   * final stages to keep processing
   *   1st -  Continue waiting for work.
   *           Keep looking for work while not in this stage of shutdown.
   *           This Normal Que Thread ends when in stage 1 shutdown. Therefore,
   *           it should not wait for new work when greater than not-in-shutdown.
   *   2nd - Force stage of shut down.
   *           Keep the thread alive while not greater than this stage of shutdown.
   *             This Normal Que Thread returns in the run() method when in stage 2.
   *             Stage 2 is a force ending for this type of thread irrelavant of the
   *             requests in wait lists.
   * 
   */     
  setShutStages(TymeacInfo.AVAILABLE,
                TymeacInfo.SHUT_STAGE2);  
  
} // end-construtor

/**
 * Tymeac Asynchronous Request
 */ 
protected void asyncRequest () { 
  
  // get the current request
  request = getReq();
   
  // When backing out
  if  (request.isBackout()) {  
    
      // decrement number and 
      // When number remaining is none, purge the request       
      if  (request.setDecrement(qArea) == 0) request.setFree();

      // done here
      return;
      
  } // endif
  
  // get input object
  setArg(request.getInput());
  
  // *--- link to the application ---*
  Object back = invokeApplication(); 
  
  // input object no longer needed
  setArg(null);
  
  // When expunged, get out quickly
  if  (isExpunged()) return; 
  
  // When a problem
  if  (isProblem()) {
    
      // say queue in this request finished
      request.setDecrement(qArea);
      
      // get out
      return; 
    
  } // endif
  
  // get the agent name  
  AreaBase agent = request.getAgent();
  
  // When a valid output exists and 
  //   using an agent and 
  //   NOT in backout
  //   then update the output data   
  if  ((back != null) && 
       (agent != null)  &&
       (!request.isBackout()))  request.setOutput(back);
      
  // say queue in this request finished returning number remaining 
  // When not complete, done here
  if  (request.setDecrement(qArea) > 0) return;
  
  // *--- async request normal queue processing is complete ---*
          
  // When being backed out or
  //  no output agent 
  if  ((request.isBackout()) ||
       (agent == null)) { 

      // purge the request                      
      request.setFree();

      // all done, no output agent processing
      return;
             
  } // endif
        
  // *--- Schedule the Agent ---*
  int schd_result = schedule(agent);
  
  // When expunged, get out quickly
  if  (isExpunged()) return;  
                                      
  // When scheduling failed:
  if  (schd_result != 0) {
          
      // add a stall table entry
      ty.getStall_tbl().addEntry(request.getUni(), schd_result);
          
      // log the part one error
      msgOut(TyMsg.getMsg(6025)
            + mgt.getName() 
            + TyMsg.getText(89)
            + qArea.getName()
            + TyMsg.getText(118) 
            + schd_result
            + TyMsg.getText(94)
            + request.getUni() 
            + TyMsg.getText(95), 10);
 
      // log the part two error    
      msgOut(TyMsg.getMsg(6026)
            + request.getUni() 
            + TyMsg.getText(96)
            + agent.getName(), 10);
      
  } // endif 
  
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected void caughtIllegalAccessException(IllegalAccessException e){
  
  // common error handling 
  caughtException(TyMsg.getMsg(6005)
          + mgt.getName() 
          + TyMsg.getText(89)
          + qArea.getName() 
          + TyMsg.getText(90),
      6005, 
      15);  
  
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected  void caughtInvocationTargetException(InvocationTargetException e) {
  
  // common error handling 
  caughtException(TyMsg.getMsg(6010)
          + mgt.getName() 
          + TyMsg.getText(89)
          + qArea.getName() 
          + TyMsg.getText(91)
          + " <"
          + e.getTargetException()
          + ">",
      6010, 
      20);
  
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected  void caughtIllegalArgumentException(IllegalArgumentException e) {
  
  // common error handling 
  caughtException(TyMsg.getMsg(6015)
          + mgt.getName() 
          + TyMsg.getText(89)
          + qArea.getName() 
          + TyMsg.getText(92), 
      6015, 
      25);
  
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected  void caughtThrowable(Throwable e) {
  
  // common error handling 
  caughtException(TyMsg.getMsg(6020)
            + mgt.getName() 
            + TyMsg.getText(89)
            + qArea.getName() 
            + TyMsg.getText(93)
            + e.toString(),
        6020, 
        10);
  
}// end-method

/**
 * For the caught exception
 * @param msg String - error message
 * @param reason int - reason for entry
 * @param cc      int - condition code from thread
 */
private void caughtException(String msg,
                    int reason, 
                    int cc) { 
      
  // send notification and log the msg 
  sendNotify(msg);
  msgOut(msg, 10);
      
  // When NOT a sync, is an Async request 
  if  (!request.isSync()) {    
          
      /*
       * Add a stall table entry. Even if this is the last
       * queue in the function, it did not complete normally, so
       * it's like it never happended at all.
       */
      ty.getStall_tbl().addEntry(request.getUni(), reason);
  }
  else {          
      // set the completion code
      request.setComp(cc);
      
  } // endif  
                  
  // this thread is no longer in use
  mgt.setDead(reason);
  
  // increment times caught
  mgt.addCaught();

  // all done
  setProblem();

} // end-method

/**
 * Expunged indicator
 * @return is expunged or not
 */
private boolean isExpunged() { return expunged; } // end-method

/**
 * Say the normal thread was expunged
 */
protected void sayExpunged() {
  
  // had a problem
  setProblem();
  setExpunged();
  
  // log a message
  msgOut(TyMsg.getMsg(6090)
    + mgt.getName() 
    + TyMsg.getText(89)
    + qArea.getName()
    + TyMsg.getText(176),
    10); 
  
} // end-method

/**
 * set the expunged indicator
 */
private void setExpunged() { expunged = true; } // end-method 

/**
 * call the Queue Scheduler 
 * @param que AreaBase - queue 
 * @return int
 */ 
private int schedule (AreaBase que) {  
  
  // When passed first stage of shut down, bye
  if  (ty.getEndit() > TymeacInfo.SHUT_STAGE1) return 5005;

  // When status change to "in Link to Schd" Not good
  if  (!mgt.setLinkSchd(getInstance())) {
      
      // say so
      sayExpunged();
      
      return -1;
      
  } // endif
   
  // schedule on the agent queue: the request, priority one, not normal queue
  int R = que.schedule(request, 1, false);

  // When status change to "actively processing" NOT successful
  if  (!mgt.setBackProcessing(getInstance())) {

      // say so
      sayExpunged();
      
      // what goes back doesn't matter
      return -1;

  } // endif

  // return what came back
  return R;
  
} // end-method

/**
 * Tymeac Synchronous Request
 */ 
protected void syncRequest () { 
  
  // get the current request
  request = getReq();
  
  // When backing out
  if  (request.isBackout()) {
    
      // decrement and post thread completion
      request.setDecrement(qArea);

      // done here
      return;  
    
  } // endif
  
  // get input object
  setArg(request.getInput());
  
  // *--- link to the application ---*
  Object back = invokeApplication(); 
  
  // input object no longer needed
  setArg(null);
  
  // When expunged, get out quickly
  if  (isExpunged()) return; 
      
  // When Not a problem and 
  //   Not backing out and 
  //   a valid output exists
  //   then save the output
  if  ((!isProblem()) && 
       (!request.isBackout()) &&
       (back != null)) request.setOutput(back);

  // decrement and post thread completion
  request.setDecrement(qArea);
  
} // end-method
} // end-class
