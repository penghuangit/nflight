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

import java.net.*;
import java.util.*;

/**
 * Format class and URL
 */
public final class TyFormat {

/**
 * Format the String into its Class name and URL array
 * @return com.tymeac.base.ClsUrl
 * @param full_name java.lang.String
 */
public static ClsUrl doClass(String full_name) {
    
  // the formatted stuff
  ClsUrl back = new ClsUrl();

  // temp
  String a_class;
  String a_url;
  int full_len, i, j;

  // characters
  char colon      = ':';
  char semicolon  = ';';

  // When nothing passed, return with result zero
  if  ((full_name == null) || 
       (full_name.length() < 1)) return back;

  // *** separate the Class from the URLs ***

  // length of string
  full_len = full_name.length();

  // no urls
  a_url = null;

  // could be without url
  a_class = full_name;       

  // look for a colon
  for  (i = 0, j = 0; i < full_len; i++) {

    // look for colon
    if  (full_name.charAt(i) == colon) {

        // back with where
        j = i;

        // done
        break;

    } // endif
  } // end-for
    
  // When there is a url list
  if  (j > 0) { 

      // When next char is also a colon
      if  (((j + 2) < full_len) && 
            (full_name.charAt(j + 1) == colon)) {

          // is a url list
          a_class = full_name.substring(0, j);
          a_url   = full_name.substring((j + 2), full_len);
      }
      else {
          // bad format
          back.setResult(3);

          // return
          return back;
            
      } // endif
  }
  else {
      // only a class
      back.setClassName(a_class);
      back.setResult(1);

      // return
      return back;

  } // endif

  // *** format the URLs ***

  // total length of url stream
  int url_len = a_url.length();

  // temp url
  URL tempURL;

  // vector for temp phase
  ArrayList<URL> temp = new ArrayList<URL>();

  // find all urls
  for  (i = 0, j = 0; i < url_len; i++) {

    // look for semicolon 
    if  (a_url.charAt(i) == semicolon) {

        // format into a url
        try {
          // each
          tempURL = new URL(a_url.substring(j, i));

        } // end-try

        catch (MalformedURLException e) {

          // bad url
          back.setResult(4);

          // return
          return back;  

        } // end-catch
 
        // save it
        temp.add(tempURL);

        // new j
        j = i + 1;
    }
    else {
        // When end of string
        if  ((i + 1) == url_len)  {

            // save last one
            // format into a url
            try {
              // each
              tempURL = new URL(a_url.substring(j));

            } // end-try

            catch (MalformedURLException e) {

              // bad url
              back.setResult(4);

              // return
              return back;  

            } // end-catch
 
            // save it
            temp.add(tempURL);

        } // endif
    } // endif
  } // end-for
     
  // how many urls
  int tot_url = temp.size();

  // When there is NO url list
  if  (tot_url < 1) {

      // bad format
      back.setResult(3);

      // return
      return back; 

  } // endif

  // the list
  URL[] the_list = new URL[tot_url];

  // put all strings in list
  for  (i = 0; i < tot_url; i++) {

    // each
    the_list[i] = temp.get(i);

  } // end-for

  // new URL
  back.setUrlName(the_list);

  // class name
  back.setClassName(a_class);

  // both
  back.setResult(2);

  // return
  return back;  

} // end-method
} // end-class
