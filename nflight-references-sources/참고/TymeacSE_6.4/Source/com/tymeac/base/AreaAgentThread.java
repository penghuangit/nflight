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
 * Queue Thread to process the Output Agent Queue
 */
public final class AreaAgentThread 
          extends AreaBasicThread {   
    
  private final TyBase         ty;    // base storage 
  private final AreaThreadMgmt mgt;   // the mgt struct for this thread 
  private final AreaBase       qArea; // the Area Base for this thread
  
  private RequestDetail request = null;  // the request 

/**
 * Constructor
 * @param Ty Tymeac base structure
 * @param AH The Queue to which this thread belongs
 * @param AD The Thread Management structure for this thread
 * @param tname The name of this thread
 */
protected AreaAgentThread(TyBase Ty,
                   AreaBase AH,
                   AreaThreadMgmt AT,
                   String tname) {
                      
  super(Ty, AH, AT, tname);
  
  ty    = Ty; // tymeac base
  mgt   = AT; // management object
  qArea = AH; // area base
    
  /*
    * final stages to keep processing
    *   1st -  Continue waiting for work.
    *           Keep looking for work while not in this stage of shutdown.
    *           This Output Agent Que Thread ends when in stage 2 shutdown.
    *           Since new Output Agent requests may be scheduled by the normal
    *           queues in stage1, this thread must keep working through Stage1.
    *           Therefore, it should not wait for new work when greater than stage1.
    *   2nd - Force stage of shut down.
    *           Keep the thread alive while not greater than this stage of shutdown.
    *             This Output Agent Que Thread returns in the run() method when
    *             in Force-shutdown.
    *             Force-shutdown is a forced ending for this type of thread
    *             irrelavant of the requests in wait lists.
    * 
    */     
  setShutStages(TymeacInfo.SHUT_STAGE1, 
                TymeacInfo.SHUT_FORCE);
  
} // end-constructor

/**
 * Tymeac Asynchronous Request Output Agent processing.
 */ 
protected void asyncRequest () {
  
  // get the current request
  request = getReq();
    
  // passed to PAC
  Object[] pass = null, outputs = null;

  // When backing out
  if  (request.isBackout()) {

      // Delete the entry      
      request.setFree(); 
      
      // done
      return;
      
  } // endif
  
  // pick up all the outputs from previous processing and pass this to the OA PAC.
    
  // When there are outputs stored, get the array of outputs
  if  (request.getNextOut() > 0) outputs = request.getOutput();
     
  // When there are outputs
  if  (outputs != null) {

      // work
      int i = 0, nbr_outs = outputs.length, max_outs = 0;
  
      // find out how many real outputs
      for (; i < nbr_outs; i++) {
  
        // When not a valid output, get out
        if  (outputs[i] == null)  break;
        
        // count of good outputs
        max_outs++;
  
      } // end-for          
  
      // When some outputs
      if  (max_outs > 0) {
  
          // get the new obj[]
          pass = new Object[max_outs];
    
          // only those up to the first null
          for (i = 0; i < max_outs; i++) {
                  
            // get the output array
            pass[i] = outputs[i]; 
    
          } // end-for
      } // endif
  } // endif    
  
  // the outputs, now input   
  setArg(pass);
  
  // *--- invoke the Processing Application Class ---*  
  invokeApplication();
  
  // arg no longer needed 
  setArg(null); 

  // When had any problem, get out
  if  (isProblem()) return;
  
  // Function is complete  
      
  // request is finished   
  request.setFree();
    
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected void caughtIllegalAccessException(IllegalAccessException e){
  
  // common error handling 
  caughtException(TyMsg.getMsg(7005)
          + mgt.getName() 
          + TyMsg.getText(89)
          + qArea.getName() 
          + TyMsg.getText(90),
      7005);  
  
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected  void caughtInvocationTargetException(InvocationTargetException e) {
  
  // common error handling 
  caughtException(TyMsg.getMsg(7010)
          + mgt.getName() 
          + TyMsg.getText(89)
          + qArea.getName() 
          + TyMsg.getText(91)
          + " <"
          + e.getTargetException()
          + ">",
      7010);
  
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected  void caughtIllegalArgumentException(IllegalArgumentException e) {
  
  // common error handling 
  caughtException(TyMsg.getMsg(7015)
          + mgt.getName() 
          + TyMsg.getText(89)
          + qArea.getName() 
          + TyMsg.getText(92), 
      7015);
  
} // end-method

/**
 * Caught exception from method.invoke()
 */
protected  void caughtThrowable(Throwable e) {
  
  // common error handling 
  caughtException(TyMsg.getMsg(7020)
            + mgt.getName() 
            + TyMsg.getText(89)
            + qArea.getName() 
            + TyMsg.getText(93)
            + e.toString(),
        7020);
  
}// end-method

/**
 * For the caught exception
 * @param msg String - error message
 * @param reason int - reason code
 */
private void caughtException(String msg, int reason) {
          
  // send notification and log the msg 
  sendNotify(msg); 
  msgOut(msg, 10);             
                                
  // this entry is finished
  mgt.setDead(reason);
  
  // increment times caught
  mgt.addCaught();

  // fatal error
  setProblem();
  
  // Add a stall table entry.        
  ty.getStall_tbl().addEntry(request.getUni(), reason);

} // end-method

/**
 * Say the agent thread was expunged
 */
protected void sayExpunged() {
  
  // had a problem
  setProblem();  
  
  // log a message
  msgOut(TyMsg.getMsg(7090)
    + mgt.getName() 
    + TyMsg.getText(89)
    + qArea.getName()
    + TyMsg.getText(176),
    10); 
  
} // end-method

/**
 * Dummy
 */ 
protected void syncRequest () {
} // end-method  
} // end-class
