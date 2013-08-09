package com.tymeac.base;

/* 
 * Copyright (c) 2002 - 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.util.*;

/**
 * Tymeac Function header contains the Function array setup, 
 *   the search methods for finding an entry
 *   and the methods for accessing the detail array.
 */
public final class FuncHeader {
  
// *--- private inner Classes Used For Sort & Search ---*
  
  // comparator using a hash  
  private final static class HashCompare<E> implements Comparator<FuncDetail>, 
                                          java.io.Serializable {
     static final long serialVersionUID = 1L;
    
    // overridden method
    public int compare(FuncDetail T1, FuncDetail T2) {
      
      // compare hash codes in details
      if (T1.getHash() > T2.getHash()) return 1;
      if (T1.getHash() < T2.getHash()) return -1;
      
      // equal
      return 0;
      
    } // end-method    
  } // end-class
  
  // comparator using a String  
  private final static class StringCompare<E> implements Comparator<FuncDetail>, 
                                            java.io.Serializable {
    static final long serialVersionUID = 1L;
    
    // overridden method
    public int compare(FuncDetail T1, FuncDetail T2) {
      
      // compare String 'name' in details
      return (T1.getName().compareTo(T2.getName()));
      
    } // end-method    
  } // end-class
  
// *--- End of Classes Used For Sort & Search ---*

  private final TyBase T;     // Tymeac base storage
  private final int nbr_func; // number of functions
  
  // Comparator for a hash
  private final Comparator<FuncDetail> usingHash;
  
  // Comparator for a String
  private final Comparator<FuncDetail> usingString;
  
  // The array of the function details
  private final ArrayList<FuncDetail> Functions; 
  
  private int type; // type of search: 
                    //  0-hash serial, 1-hash binary
                    //  2-string serial, 3-string binary

/**
 * Constructor 
 * @param numb int - number of entries
 * @SuppressWarnings({"unchecked"})
 */  
protected FuncHeader(int numb, TyBase Ty) {
  
  T        = Ty;   // Base
  nbr_func = numb; // number of functions in all
  
  // When < 10 entries, use a serial search else binary
  type = (nbr_func < 10)? 0 : 1;
  
  // Comparator for a hash
  usingHash = new FuncHeader.HashCompare<FuncDetail>();
  
  // Comparator for a String
  usingString = new FuncHeader.StringCompare<FuncDetail>();

  // new functions array, size never changes
  Functions = new ArrayList<FuncDetail>(numb);

} // end-constructor

/**
 * New function, call the detail add method. 
 *   When at end, check for dup hash codes. When any dups, we cannot use a hash code search,
 *     therefore, we need to sort the String function names.
 * @param name String - function name
 * @param agent AreaBase - output agent queue number
 * @param Q AreaBase[] - queue array
 */   
protected void addFunction(String name,
                  AreaBase agent,
                  AreaBase[] Q) {
   
  // new FuncDetail into the array
  Functions.add(new FuncDetail(name, agent, Q));

  // When at end
  if  (Functions.size() == nbr_func) {      
      
      // check for dup hash codes
      checkHash();
      
      // Type 0, 1, = binary, 2, 3 = linear
      switch (type) {
    
        case 0: // will use a serial search on hash code
                break;
  
        case 1: // will use a binary search on hash code
                // sort using a hash code
                Collections.sort(Functions, usingHash);
                break;                  
  
        case 2: // will use a serial search on String
                break;
  
        case 3: // will use a binary search on String
                // sort using a String
                Collections.sort(Functions, usingString); 
                break;   
    
      } // end-switch
  } // endif  
    
} // end-method 

/**
 * Check for dup hash codes
 */
private void checkHash() {
  
  // temp set for checking. HashSet rejects duplicates
  HashSet<Integer> hs = new HashSet<Integer>(nbr_func);
  
  // do each detail
  for (FuncDetail FD : Functions) {
    
    // When Not added, is a duplicate
    if (!hs.add(Integer.valueOf(FD.getHash()))) {
      
        // reset the type from 0 or 1, to 2 or 3 respectively
        type += 2;
        
        // When logging, do so & wait 10 sec for completion
        if  (T.isLogUsed()) T.getLog_tbl().writeMsg(TyMsg.getMsg(46), 10);
        
        // When printing, do so
        if  (T.getSysout() == 1) TyBase.printMsg(TyMsg.getMsg(46));
        
        // done here - will be using linear searching
        return;

    } // endif    
  } // end-for 

} // end-method

/**
 * Diagnose - display all the function's queues 
 */ 
protected void dsplyFuncQues () {
    
  // do each detail
  for (FuncDetail FD : Functions) {
     
   FD.dsplyQues();
       
  } // end-for      
    
} // end-method

/**
 * Diagnose - display the function names and count
 */ 
protected void dsplyFunctions () {    
    
  TyBase.printMsg("Total Functions=" + nbr_func);
  
  // do each detail
  for (FuncDetail FD : Functions) {
     
   TyBase.printMsg(FD.getName() + " Used= " + FD.getUsed());
 
  } // end-for      
    
} // end-method

/**
 * Fetch a single detail object from the array
 * @param nbr int - the detail number
 * @return FuncDetail
 */           
protected FuncDetail getEntry(int nbr) {

  // When invalid detail number, return null
  if  ((nbr < 0) || 
       (nbr >= nbr_func)) return null;
  
  // return detail obj
  return Functions.get(nbr);
    
} // end-method  

/**
 * Get a function for a sync or async request
 * @param name String - name to search
 * @return FuncDetail
 */ 
protected FuncDetail getFunction(String name) {

  // Type 0, 1, = hash, 2, 3 = string
  switch (type) {

      case 0: return lookSerialHash(name);

      case 1: return lookBinaryHash(name);

      case 2: return lookSerialString(name);

      case 3: return lookBinaryString(name);

  } // end-switch
  
  // oops
  return null;
      
} // end-method

/**
 * Accessor for number of functions
 * @return int
 */      
protected int getNumb() { return nbr_func; } // end-method  

/**
 * Call the detail getUsed method
 * @param nbr int - function number
 * @return long 
 */           
protected long getUsed(int nbr) {

  // When invalid detail number, return invalid
  if  ((nbr < 0) || 
       (nbr >= nbr_func)) return -1;
  
  // return number used
  return Functions.get(nbr).getUsed();
    
} // end-method  
      
/**
 * Binary search using a hash table
 * @param name String - name to search
 * @return FuncDetail 
 */
private FuncDetail lookBinaryHash(String name) {
  
  // For this method's object to compare against
  FuncDetail toCompare = new FuncDetail(name);
  
  // search for a hit
  int ret = Collections.binarySearch(Functions, toCompare, usingHash);
  
  // When found, get it
  return (ret >= 0)? Functions.get(ret) : null;

} // end-method

/**
 * Binary search using a String
 * @return FuncDetail
 * @param name String
 */
private FuncDetail lookBinaryString(String name) {
  
  // For this method's object to compare against
  FuncDetail toCompare = new FuncDetail(name);
  
  // return what happended
  int ret = Collections.binarySearch(Functions, toCompare, usingString);
    
  // When the search was successful, return the FuncDetail object
  return (ret >= 0)? Functions.get(ret) : null;

} // end-method

/**
 * This is a serial search using the hash code
 * @return FuncDetail
 * @param name String
 */
private FuncDetail lookSerialHash(String name) {
  
  // get the hash code
  int hash = name.hashCode();
  
  // look at each detail
  for (FuncDetail FD : Functions) {
    
    // When hash codes match
    if  (FD.getHash() == hash) {

        // When the strings match, return the detail object
        if  (FD.getName().compareTo(name) == 0) return FD;
        
    } // endif    
  } // end-for
  
  // not found
  return null;

} // end-method

/**
 * This is a linear search
 * @return FuncDetail
 * @param name String
 */
private FuncDetail lookSerialString(String name) {
   
  // look at each detail
  for (FuncDetail FD : Functions) {
    
    // When the strings match, return the detail object
    if  (FD.getName().compareTo(name) == 0) return FD;
    
  } // end-for
  
  // not found
  return null;

} // end-method

/**
 * Find all Function names that match a string of characters
 * @param from String - name to search
 * @return String[]
 */ 
protected String[] matchName(String from) {    
    
  int j = 0, len, count = 0;
  String[] rData;
  
  // get the length of the passed string
  len = from.length();
      
  // first count the number of matches
  for (FuncDetail FD : Functions) {
          
    // When a match on func name to passed, bump count    
    if  (FD.getName().regionMatches(0, from, 0, len)) count++;

  } // end-for      
  
  // When none, say none
  if  (count < 1) {
        
      // get new string to put reply
      rData = new String[1];

      // say is not here
      rData[0] = "N287";

      // give back
      return rData;

  } // endif        
  
  // new string array
  rData = new String[count];
  
  // do all 
  for (FuncDetail FD : Functions) {
          
    // When a match on func name : passed
    if  (FD.getName().regionMatches(0, from, 0, len)) {
            
        // make a string
        rData[j] =  "("
                    + String.format(Locale.getDefault(), "%,d", FD.getUsed())
                    + ") - "
                    + FD.getName();
                
        // incr string array index 
       j++;           
       
    } // endif       
  } // end-for      

  // give back what was found
  return rData;
    
} // end-method   
} // end-class
