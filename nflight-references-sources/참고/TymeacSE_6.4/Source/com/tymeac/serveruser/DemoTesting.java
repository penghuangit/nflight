package com.tymeac.serveruser;

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
 *  An aid to testing without using the threading environment
 *  
 *
 */

public class DemoTesting {

  // name of the Class to load *--- change this for your requirements ---*
  private static String class_name = "Demo1";

  // instance of the "main" method
  private static java.lang.reflect.Method pap_method;

  // return from the invoke
  private static Object back;  

/**
 * This method issues the java.lang.reflect.Method.invoke() for the loaded class.
 * @return int
 * @param obj java.lang.Object
 */
private static int invokeClass(Object obj) {

  // Object array is used as signature
  Object o_args[] = new Object[1];

  // 1st object in that array is the passed arg   
  o_args[0] = obj;

  // try to invoke the main method             
  try {
             
      back = pap_method.invoke(null, new Object[] { o_args });
        
  } // end-try
  
  catch (IllegalAccessException e) {
        
      // print error
      System.out.println("IllegalAccessException"); 

      // all done
      return 1;

  } // end-catch
 
   
 catch (java.lang.reflect.InvocationTargetException e) {

      // print error
      System.out.println("InvocationTargetException");  

      // all done
      return 2;

  } // end-catch
         
  catch (IllegalArgumentException e) {

      // print error
      System.out.println("IllegalArgumentException"); 

      // all done
      return 3;

  } // end-catch          
              
 
  catch (java.lang.Throwable e) {

      // print error
      System.out.println("Thrown exception=" + e.getMessage()); 

      // all done
      return 4;

  } // end-catch  

  // no problem
  return 0;

} // end-method

/**
 * This method loads the requested class and finds the method "main(Object[])"
 * @return int
 */
private static int loadClass() {

  // class object       
  Class<?> C1 = null;
    
  // setup for object method search  
  Object[] o_args = new Object[1];
  o_args[0]       = "Tymeac";
  Class[]  obj_class = new Class[] { o_args.getClass() };
      
  // load the class 
  try {   
      C1 = Class.forName(class_name);
                                      
  } // end-try       
                            
  catch (Exception e) {

      System.out.println("Class loading Exception: " + e);

      // all done
      return 1;

  } // end-catch 
        
  // get the processing application class "main" method with object[] signiture
  try {
      pap_method = C1.getMethod("main", obj_class);
                                      
  } // end-try       
                            
  catch (SecurityException e) {
    
      // print the error    
      System.out.println("Security exception on getMethod");

      // all done
      return 2;

  } // end-catch  
        
  catch (NoSuchMethodException e) {
                                  
      // print the error      
      System.out.println("No 'main(Object[]) method found");  

      // all done
      return 3;

  } // end-catch

  // no errors
  return 0;

} // end-method

/**
 * This is a means to test a Tymeac Processing Application Class without
 *   using the threading environment.
 *   
 * @param args String[] - passed args
 * 
 */
public static void main(String args[])  {
     
  // new string for Processing Application Class
  String x = "223344556677889911";
  Object pass = x;

  // When loaded ok
  if  (loadClass() == 0) {

      // When invoked ok
      if  (invokeClass(pass) == 0) {

          // must be a string
          if  ((back != null) &&  
               (back instanceof String)) {

              // print what came back
              System.out.println((String) back);
          }
          else {
              // print what came back
              System.out.println("returned msg null or not a String");

          } // endif
      } // endif
  } // endif
    
} // end-method
} // end-class
