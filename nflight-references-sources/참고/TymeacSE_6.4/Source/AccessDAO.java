// In the following code snippet, we are going to connect to an Access database which has a DSN of MyAccessDB using Sun's JDBC-ODBC bridge driver

import java.sql.*;

public class AccessDAO {

        private Connection con;
        private Statement st;
        private static final String url="jdbc:odbc:TymeacSE"; 
        private static final String className="sun.jdbc.odbc.JdbcOdbcDriver"; 
        private static final String user=""; 
        private static final String pass=""; 

AccessDAO()throws Exception {

  Class.forName(className); 
          con = DriverManager.getConnection(url, user, pass); 
          st = con.createStatement();  
          
          System.out.println("Got here");
  
          //do whatever database processing is required 
          
} // end-constructor

public static void main (String[] args) {
 
  try {
    AccessDAO dao = new AccessDAO();
    
  } catch (Exception e) {
    
    System.out.println(e.toString());
    
  } // end-catch 
  
} // end-main
} // end-class
  
