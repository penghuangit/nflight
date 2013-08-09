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
 * Tymeac Demonstration System. 
 *   Parameter for call back demonstration.
 *   This is used both for the Object passed to Tymeac Server, (in TymeacParm), and
 *     as the output of each Processing Application Class, (DemoCallback1-3).  In this
 *     way, the reference to the remote object, (field, cb), is easily passed from Class
 *     to Class.
 */

public class TyDemoCallbackParm 
      implements java.io.Serializable {
  
  private static final long serialVersionUID = -8486439326550441328L;

  // the call back interface
  private TyDemoCallback cb = null;

  // the object
  private Object obj = null;

/**
 * TyDemoCallbackParm constructor1
 */
public TyDemoCallbackParm() {

} // end-constructor

/**
 * TyDemoCallbackParm constructor2
 * @param n_cb TyDemoCallback - callback object
 * @param n_obj Object - object
 */
public TyDemoCallbackParm(TyDemoCallback n_cb, Object n_obj) {

  // set fields
  cb = n_cb;
  obj = n_obj;  

} // end-constructor

/**
 * remote object accessor
 * @return TyDemoCallback
 */
public TyDemoCallback getCb() { return cb; } // end-method

/**
 * obj accessor
 * @return java.lang.Object[]
 */
public java.lang.Object getObj() { return obj; } // end-method

/**
 * Unused mutator for remote object
 * @param newCb TyDemoCallback
 */
public void setCb(TyDemoCallback newCb) { cb = newCb; } // end-method

/**
 *  Unused mutator for object
 * @param newObj java.lang.Object[]
 */
public void setObj(java.lang.Object newObj) { obj = newObj; } // end-method

} // end-class
