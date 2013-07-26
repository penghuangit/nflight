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

import java.util.*;

/**
 * Start up building the Tymeac Functions
 */
public final class StartupFunctions  {

  // common
  private final TyBase T;
  private final Startup base;
  private final StartupFields suFields;
  
/**
 * Constructor
 * @param Ty TyBase - tymeac base storage
 * @param Startup b - Base class
 */    
protected StartupFunctions(TyBase ty, Startup b) {
  
  T    = ty;
  base = b;
  suFields = base.getStartupFields();
  
} // end-constructor  

/**
 * build all the Functions for this session. 
 * @return boolean
 */
private boolean buildFunctions(TymeacIndividualFunction[] tif ) 
    throws Throwable {

  // function data
  String  a_func;
  int a_nbr_que, i, j;  

  // agent used for all queues
  AreaBase Agent = null; 

  // temporary array for the queue names
  String[] que_names;

  // number of functions
  int nbr_func = tif.length;

  // initialize the Main function table 
  T.setFunc_tbl(new FuncHeader(nbr_func, T));
  
  // addr just built
  FuncHeader func = T.getFunc_tbl();

  // do all the functions
  for (i = 0; i < nbr_func; i++) {

    // data
    a_func    = tif[i].getName();
    a_nbr_que = tif[i].getNbrQue();
                    
    // When Output Agent:
    if  ((tif[i].getOA() == null) || 
         (tif[i].getOA().length() < 1)) {
                
        // no agent
        Agent = null; 
    }
    else {
        // get the Area header
         Agent = T.getMain_tbl().getAreaBase(tif[i].getOA());
         
        // When invalid
        if  (Agent == null) {

            // send out
            doMsg(TyMsg.getMsg(38)
                + a_func
                + TyMsg.getText(9)
                + tif[i].getOA() 
                + TyMsg.getText(10));

            // all done
            return false;

        } // endif 
  
        // When a normal, error
        if  (Agent.isNormalQueue()) {
    
            // send out
            doMsg(TyMsg.getMsg(42)
                + a_func  
                + TyMsg.getText(14)
                + tif[i].getOA() 
                + TyMsg.getText(15));
    
            // all done
            return false;
    
        } // endif
    } // endif              

    // When number of queues < 1 is no good
    if  (a_nbr_que < 1) {
  
        // not found              
        doMsg(TyMsg.getMsg(37)
            + a_func  
            + TyMsg.getText(8));
  
        // all done
        return false;
        
    } // endif

    // List of queues
    que_names = tif[i].getList();

    // List of queues
    AreaBase[] Qtbl = new AreaBase[a_nbr_que];

    // do all the queues in this function
    for (j = 0; j < a_nbr_que; j++) {
      
      // get the Area header
      Qtbl[j] = T.getMain_tbl().getAreaBase(que_names[j]);
        
      // When not found, cannot continue
      if  (Qtbl[j] == null) {

          // send out
          doMsg(TyMsg.getMsg(38)
              + a_func
              + TyMsg.getText(14)
              + que_names[j]
              + TyMsg.getText(10));

          // all done
          return false;

      } // endif
  
      // When Not a normal, error
      if  (!Qtbl[j].isNormalQueue()) {
  
          // send out
          doMsg(TyMsg.getMsg(42)
              + a_func  
              + TyMsg.getText(14)
              + que_names[j] 
              + TyMsg.getText(173));
  
          // all done
          return false;
  
      } // endif      
    } // end-for        
      
    // build a base entry
    func.addFunction(a_func, Agent, Qtbl);
                        
  } // end-for

  // ok
  return true;

} // end-method

/**
 * create all the Tymeac functions
 * @return int
 */
protected boolean  createFunctions () 
        throws Throwable {
  
  // add in the internal queues to the previously built from stand-alone or DBMS
  TymeacIndividualFunction[] tif = internalFunctionLoad(suFields.getTif());
  
  // When failed, get out
  if  (tif == null) return false;
  
  // build the Tymeac Structure
  return buildFunctions(tif);
  
} // end-method

/**
 * Load the internal functions from the user classes
 * @return int
 */
private TymeacIndividualFunction[]  internalFunctionLoad (TymeacIndividualFunction[] tif) 
        throws Throwable {
  
  // variables
  int nbr_func = 0,
      nbr_user_func = tif.length,
      i, j; 
  
  // the Function Class
  TymeacInternalFunctions func_table = null;  

  try {
    // Load the Function Class
    func_table = new TymeacInternalFunctions();

  } // end-try

  catch (NoClassDefFoundError e) {

    // not found 
    doMsg(TyMsg.getMsg(35)
              + TyMsg.getText(108)
              + TyMsg.getText(106));

    // all done
    return null; 

  } // end-catch

  try { 
    // get number of functions
    nbr_func = func_table.getNbrFunctions();

    // < 1 is no good
    if  (nbr_func < 1) {
      
        // not good
        doMsg(TyMsg.getMsg(36)
              + TyMsg.getText(108)
              + TyMsg.getText(109));

        // all done
        return null;  

    } // endif
  } // end-try 

  catch (NoSuchMethodError e) {

    // not found 
    doMsg(TyMsg.getMsg(47) 
            + TyMsg.getText(117)
            + e.toString());

    // all done
    return null; 

  } // end-catch

  catch (Exception e) {

    // not found
    doMsg(TyMsg.getMsg(47)
            + TyMsg.getText(117)
            + e.toString());

    // all done
    return null; 

  } // end-catch  

  // load Function definitions
  TymeacIndividualFunction[] internal_tif = func_table.getFunctions();

  // *--- check for nulls & dups internally ---*

  // temp set for checking. HashSet rejects duplicates
  HashSet<String> hs = new HashSet<String>(nbr_func + nbr_user_func);
  
  // for error msg
  i = 0;
  
  // do each detail
  for (TymeacIndividualFunction func : internal_tif) {
    
    // When null, no good
    if  ((func == null) ||
         (func.getName() == null)) {

        // send out
        doMsg(TyMsg.getMsg(44)
            + TyMsg.getText(113)
            + i
            + TyMsg.getText(3));

        // all done
        return null;  

    } // endif
    
    // When Not added, is a duplicate
    if (!hs.add(func.getName())) {

        // send out
        doMsg(TyMsg.getMsg(43)
            + TyMsg.getText(111)
            + internal_tif[i].getName()
            + TyMsg.getText(4));

        // all done
        return null; 

    } // endif
    
    // next i
    i++;
    
  } // end-for 
  
  // *--- check for dups externally ---* 
  
  // do each detail in the main list
  for (TymeacIndividualFunction func : tif) {
    
    // When Not added, is a duplicate
    if  (!hs.add(func.getName())) {

        // can't use the table
        doMsg(TyMsg.getMsg(43)
            + func.getName()
            + TyMsg.getText(17));

        // all done
        return null;
         
    } // endif
  } // end-for
  
  // *--- put all the functions into one array ---*
  
  // new array
  TymeacIndividualFunction[] full_tif = new TymeacIndividualFunction[nbr_func + nbr_user_func];
  
  // load user queues
  for (i = 0; i < nbr_user_func; i++) {
    
    // each user function
    full_tif[i] = tif[i];
      
  } // end-for
  
  // load internal functions
  for (j = 0; j < nbr_func; j++, i++) {
    
    // each user queue
    full_tif[i] = internal_tif[j];
      
  } // end-for
          
  // all done   
  return full_tif;
  
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
  if  (base.isActivatable()) throw new Throwable(error);  

} // end-method 

} // end-class
