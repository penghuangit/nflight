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
 * Class for checking user and DBMS Queues.
 *
 */

import java.sql.*;
import com.tymeac.base.*;
import com.tymeac.serveruser.*;

class QueueChecker {

/**
 * This method checks the Tymeac DBMS Queue Table.
 *  Check is made for: Duplicate names 
 *
 * @return int 0=ok, -1=not good 
 */

private static int dbmsQueues ( ) {
    
    // work
    int i = 0, j, nbr_queue = 0;

    // result sets for the query
    ResultSet rs, mrs;

    // error message
    String error;

    // work
    boolean failed = false;
    
    // Strings for the queues
    String[] queue_tbl;

    // connection instance
    Connection con = null;
    
    // statement instance                                                                     
    Statement stmt = null;
          

    try {
          // When you have a manager, add it here
          //   or comment the next two lines    
          //   We use DB2, therefore the name is the DB2 Driver           
          java.sql.DriverManager.registerDriver((Driver)
                  Class.forName("COM.ibm.db2.jdbc.app.DB2Driver").newInstance());
          
                          
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
          stmt = con.createStatement();
                             
          // Query String
          String rest   = "SELECT * FROM "

                        // put your queue table name here
                        + "TYQBASE.TYQUEUE ;"; 

          // get the set just to see what is there
          rs = stmt.executeQuery(rest);
                              
          // get number of queues
          while(rs.next()) {
              nbr_queue++;
          } // end-while    
          
          // When no queues
          if  (nbr_queue == 0) {
                
                error = "TymeacDBMSQueues, number of queues is zero"
                    + "  Cannot continue.";
              
                System.out.println(error);

                // get out
                stmt.close();
                con.close();
                return -1;

          } // endif
                    
          // Strings for the functions  
          queue_tbl = new String[nbr_queue];
          
          // get the full load
          mrs = stmt.executeQuery(rest);
          
          // loop thru all
          while(mrs.next()) {
              
              // 1st string is name
              queue_tbl[i] = mrs.getString(1);

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


    // having gotten this far the queue names are in the String[] 

    

    // max for dup check
    int max = nbr_queue - 1;

    
    // *--- check for dups ---*

    // do all the table
    for  (i = 0; i < max; i++) {

          // do all from here against all others
          for  (j = i + 1; j < nbr_queue; j++) {

                // When a match, no good
                if  (queue_tbl[i].equals(queue_tbl[j])) {
                    
                    // set error msg
                    error = "Queue ("
                          + queue_tbl[i]
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

    
    // all done
    return 0;
    
} // end-method
/**
 * Use this to check for duplicate or null Queues   
 *   
 * 
 * @param args an array of command-line arguments
 */

public static void main(java.lang.String[] args) {


    // When the return from checking the user class Queues is zero
    if  (userClassQueues() == 0) {
        
        System.out.println("User Class Queues are ok");
    } 
    else {
        System.out.println("User Class Queues are *--- Not good ---*");
    } // endif

/*
    // When the return from checking the DBMS Queues is zero
    if  (dbmsQueues() == 0) {
        
        System.out.println("DBMS Queues are ok");
    } 
    else {
        System.out.println("DBMS Queues are *--- Not good ---*");
    } // endif
*/
    

} // end-method
/**
 * This method checks the TymeacUserQueue Class.
 *  Checks are made for:  Null entries, Duplicate Queue names.   
 *
 * @return int 0=ok, -1=not good 
 */
private static int userClassQueues (  ) {
    
    // variables
    int nbr_queue = 0, i, j;
    String error;
    boolean failed = false;

    // the Queue Class
    TymeacUserQueues queue_table; 


    try {
        // Load the Queue Class
        queue_table = new TymeacUserQueues();
  
        // get number of queues
        nbr_queue = queue_table.getNbrQueues();

        // < 1 is no good
        if  (nbr_queue < 1) {
            
            // cannot continue    
            error = "TymeacUserQueues Class, number of queues is zero"
                    + "  Cannot continue.";
              
            System.out.println(error);

            // get out
            return -1;

        } // endif  
          
    } 
    catch (Exception e) {

        // not found    
        error = "TymeacUserQueues Class not found"
                + "  Cannot continue.";
              
        System.out.println(error);  
        System.out.println("Exception: " + e);
        e.printStackTrace ();
          
        // return with error
        return -1;

    } // end-catch

    // Queue definition
    TymeacIndividualQueue[] tiq = queue_table.getQueues();


    // max for dup check
    int max = nbr_queue - 1;

    
    // *--- check for nulls and check for dups ---*

    // do all the table
    for  (i = 0; i < max; i++) {

          // When null, no good
          if  (tiq[i] == null) {
                    
              // set error msg
              error = "Queue position="
                    + i
                      + ", is null. "
                      + " Cannot continue.";
                                        
              System.out.println(error);  

              // get out
              return -1;
  
          } // endif

          // do all from here against all others
          for  (j = i + 1; j < nbr_queue; j++) {

                // When a Queue Name match, no good
                if  (tiq[i].getName().equals(tiq[j].getName())) {
                    
                    // set error msg
                    error = "Queue ("
                          + tiq[i].getName()
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

                  
    // all done   
    return 0;
    
} // end-method
} // end-class 
