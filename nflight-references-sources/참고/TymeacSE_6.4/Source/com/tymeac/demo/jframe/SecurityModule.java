package com.tymeac.demo.jframe;

/*
 * Created on Jan 25, 2004
 * Copyright (c) 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
import javax.security.auth.login.*;

/**
 * @author CoopSoft.com
 *
 * 
 */
public class SecurityModule {


protected static LoginContext getContext() {
          
    try {
        // new logon Context
        return new LoginContext("com.tymeac.TymeacClient");  
                
      } // end-try
        
    catch (LoginException e) {

        // say what found
        System.out.println("Login Exception= " + e);
    
    } // end-catch 
  
    catch (java.lang.SecurityException e) {
  
        // say what found
        System.out.println("Security Exception= " + e);
    
    } // end-catch 
  
    catch (Exception e) {

        // say what found
        System.out.println("Exception= " + e);
        
    } // end-catch
  
    // no login context
    return null;
  
} // end-method

protected static boolean login(LoginContext context) {
  
    try {
        // login
        context.login();
        
        // good
        return true;
        
    } // end-try
    
    catch (LoginException e) {
       
        // say what found
        System.out.println("LoginException= " + e);          
          
    } // end-catch
      
    catch (Exception e) {
        
        // say what found
        System.out.println("Exception= " + e);                    
                   
    } // end-catch                   

    // not logged in
    return false;
  
} // end-method

} // end-class
