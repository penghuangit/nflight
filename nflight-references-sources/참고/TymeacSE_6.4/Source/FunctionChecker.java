/*
 * TYMEAC is a trademark of Cooperative Software Systems, Inc.
 *
 * TYMEAC 
 * Copyright (C) 1995, 1998 Cooperative Software Systems, Inc.
 * All Rights Reserved.
 *
 * TYMEAC for JAVA
 * Copyright (C) 1998 Cooperative Software Systems, Inc.
 * All Rights Reserved.
 *
 */
 
/* 
 * 
 * COOPERATIVE SOFTWARE SYSTEMS, INC, (CSS), 
 * MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY 
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. 
 * CSS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE 
 * AS A RESULT OF USING OR MODIFYING THIS SOFTWARE OR ITS DERIVATIVES.
 */
 
/**
 * Tymeac System. 
 *    
 * Class for checking user and DBMS Functions.
 *
 */

import java.sql.*;
import com.tymeac.base.*;
import com.tymeac.serveruser.*;

class FunctionChecker {

/**
 * This method checks the Tymeac DBMS Functions Table.
 *  Check is made for: Duplicate names and Duplicate hashCode() values.  
 *
 * @return int 0=ok, -1=not good 
 */

private static int dbmsFunctions (  ) {
    
    // work
    int i = 0, j, this_hash, nbr_func = 0;

    // result sets for the query
    ResultSet rs, mrs;

    // error message
    String error;

    // work
    boolean failed = false;
    
    // Strings for the functions  
    String[] func_tbl;

    try {
          // When you have a manager, add it here
          //   or comment the next two lines    
          //   We use DB2, therefore the name is the DB2 Driver           
          java.sql.DriverManager.registerDriver((Driver)
                  Class.forName("COM.ibm.db2.jdbc.app.DB2Driver").newInstance());
          
                
          // connection instance
          Connection con;
          
          // substitute your URL name here  
          String URL = "jdbc:db2:TYMEAC";
      
          // substitute your user name and password here
          String UserName = "ed"; // the name of our founder
          String PassWord = "ed"; // he's so nice, we use it twice

          // When you do not use a name and password, use single parm constructor
          //  uncomment the next line and comment the "use all three" lines
          //con = DriverManager.getConnection(URL);

          // use all three parms
          con = DriverManager.getConnection(URL, UserName, PassWord);
          
          
          // get a stmt                                                                     
          Statement stmt = con.createStatement();
                             
         // Query String
          String rest   = "SELECT * FROM "

                        // put your function table name here
                        + "TYFBASE.TYFUNCTION ;"; 

          // get the set just to see what is there
          rs = stmt.executeQuery(rest);
                              
          // get number of functions 
          while(rs.next()) {
              nbr_func++;
          } // end-while    
          
          // When no Functions
          if  (nbr_func == 0) {
                
                error = "TymeacDBMSFunctions, number of functions is zero"
                    + "  Cannot continue.";
              
                System.out.println(error);

                // get out
                stmt.close();
                con.close();
                return -1;

          } // endif
                    
          // Strings for the functions  
          func_tbl = new String[nbr_func];
          
          // get the full load
          mrs = stmt.executeQuery(rest);
          
          // loop thru all
          while(mrs.next()) {
              
              // 1st string is name
              func_tbl[i] = mrs.getString(1);

              // next i
              i++;
                                              
          } // end-while  

          // all done with these guys
          stmt.close();
          con.close();
            
    } // end-try
        
    catch (SQLException ex) {

                System.out.println ("SQLException:");
          
                while (ex != null) {
                    System.out.println ("SQLState: " 
                                            + ex.getSQLState());
                    System.out.println ("Message:  " 
                                            + ex.getMessage());
                    System.out.println ("Vendor:   " 
                                            + ex.getErrorCode());
                    ex = ex.getNextException();
                    System.out.println ("");
                    
                } // end-while

          // get out
          return -1;
              
    } // end-catch

    catch (java.lang.Exception ex)  {
          
          System.out.println("Exception: " + ex);
          ex.printStackTrace ();
          
          // get out
          return -1;
          
    } // end-catch 


    // having gotten this far the Function names are in the String[] 

    // hash code table
    int[] hc = new int[nbr_func];

    // max for dup check
    int max = nbr_func - 1;

    
    // *--- build the hash code table and check for dups ---*

    // do all the table
    for  (i = 0; i < max; i++) {

          // add to the hash table
          hc[i] = func_tbl[i].hashCode();

          // do all from here against all others
          for  (j = i + 1; j < nbr_func; j++) {

                // When a match, no good
                if  (func_tbl[i].equals(func_tbl[j])) {
                    
                    // set error msg
                    error = "Function ("
                          + func_tbl[i]
                          + "), is a duplicate, "
                          + " Cannot continue.";
                                        
                    System.out.println(error);  

                    // say failed
                    failed = true;  
  
                } // endif
          } // end-for
    } // end-for 

    // When any dups
    if  (failed) {

        // get out
        return -1;

    } // endif

    // *--- look for a matching hash - each entry against all the others ---*

    // do all the hash table
    for  (i = 0; i < max; i++) {

          // get this entry
          this_hash = hc[i]; 

          // do all from here against all others
          for  (j = i + 1; j < nbr_func; j++) {

                // When a match, no good
                if  (this_hash == hc[j]) {

                    // can't use the table
                    error = "hashCode() duplicate on Function ("
                          + func_tbl[i]
                          + "), with Function ("
                          + func_tbl[j]
                          + ").";
                                        
                    System.out.println(error);  

                    // say failed
                    failed = true;  
  
                } // endif
          } // end-for
    } // end-for 
        
    // When any dups
    if  (failed) {

        // get out
        return -1;

    } // endif

    // all done
    return 0;
    
} // end-method
/**
 * Use this to check for duplicate or null Functions, and, Functions with duplicate hashCode()'s   
 *   
 * 
 * @param args an array of command-line arguments
 */

public static void main(java.lang.String[] args) {


    // When the return from checking the user class Functions is zero
    if  (userClassFunctions() == 0) {
        
        System.out.println("User Class Functions are ok");
    } 
    else {
        System.out.println("User Class Functions are *--- Not good ---*");
    } // endif

/*
    // When the return from checking the DBMS Functions is zero
    if  (dbmsFunctions() == 0) {
        
        System.out.println("DBMS Functions are ok");
    } 
    else {
        System.out.println("DBMS Functions are *--- Not good ---*");
    } // endif
*/
    

} // end-method
/**
 * This method checks the TymeacUserFunctions Class.
 *  Checks are made for:  Null entries, Duplicate Function names, and Duplicate hashCode() values.   
 *
 * @return int 0=ok, -1=not good 
 */
private static int userClassFunctions ( ) {
    
    // variables
    int nbr_func  = 0, this_hash, i, j;
    String error;
    boolean failed = false;

    // the Function Class
    TymeacUserFunctions func_table; 


    try {
        // Load the Function Class
        func_table = new TymeacUserFunctions();
  
        // get number of functions
        nbr_func = func_table.getNbrFunctions();

        // < 1 is no good
        if  (nbr_func < 1) {
            
            // cannot continue    
            error = "TymeacUserFunctions Class, number of functions is zero"
                    + "  Cannot continue.";
              
            System.out.println(error);

            // get out
            return -1;

        } // endif  
          
    } 
    catch (Exception e) {

        // not found    
        error = "TymeacUserFunctions Class not found"
                + "  Cannot continue.";
              
        System.out.println(error);  
        System.out.println("Exception: " + e);
        e.printStackTrace ();
          
        // return with error
        return -1;

    } // end-catch

    // Function definition
    TymeacIndividualFunction[] tif = func_table.getFunctions();


    // hash code table
    int[] hc = new int[nbr_func];

    // max for dup check
    int max = nbr_func - 1;

    
    // *--- check for nulls, build the hash code table and check for dups ---*

    // do all the table
    for  (i = 0; i < max; i++) {

          // When null, no good
          if  (tif[i] == null) {
                    
              // set error msg
              error = "Function position="
                    + i
                      + ", is null. "
                      + " Cannot continue.";
                                        
              System.out.println(error);  

              // get out
              return -1;
  
          } // endif

          // add to the hash table
          hc[i] = tif[i].getName().hashCode();

          // do all from here against all others
          for  (j = i + 1; j < nbr_func; j++) {

                // When a Function Name match, no good
                if  (tif[i].getName().equals(tif[j].getName())) {
                    
                    // set error msg
                    error = "Function ("
                          + tif[i].getName()
                          + "), is a duplicate, "
                          + " Cannot continue.";
                                        
                    System.out.println(error);  

                    // say failed
                    failed = true;  
  
                } // endif
          } // end-for
    } // end-for 

    // When any dups
    if  (failed) {

        // get out
        return -1;

    } // endif

    // *--- look for a matching hash - each entry against all the others ---*

    // do all the hash table
    for  (i = 0; i < max; i++) {

          // get this entry
          this_hash = hc[i]; 

          // do all from here against all others
          for  (j = i + 1; j < nbr_func; j++) {

                // When a match, no good
                if  (this_hash == hc[j]) {

                    // can't use the table
                    error = "hashCode() duplicate on Function ("
                          + tif[i].getName()
                          + "), with Function ("
                          + tif[j].getName()
                          + ").";
                                        
                    System.out.println(error);  

                    // say failed
                    failed = true;  
    
                } // endif
          } // end-for
    } // end-for 
    
      // When any dups
    if  (failed) {

        // get out
        return -1;

    } // endif
              
    // all done   
    return 0;
    
} // end-method
} // end-class 
