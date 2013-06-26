package com.abreqadhabra.nflight.examples.common.exception;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.abreqadhabra.nflight.common.exception.CommonException;

public class CommonExceptionExample {

    public static void main(String[] args) {
	
	
	
	System.out.println("No Chaining:");
	try {
	    try {
		throw new Exception("foo");
	    } catch (Exception e) {
		throw new SQLException("bar");
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);
	}

	System.out.println("\nChaining:");
	try {
	    try {
		throw new SQLException("foo");
	    } catch (Exception e) {
		throw new Exception("bar", e);
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);

	}
	
	System.out.println("\nCommonRuntimeException:");
	try {
	    try{
	    try {
		throw new SQLException("foo");
	    } catch (Exception e) {
		throw new CommonException("bar", e);
	    }
	    } catch (Exception e) {
		throw new CommonException("zar", e);
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);
	}
	
/*	try {
	    a();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	try {
	    a1();
	} catch (Exception e) {
	    e.printStackTrace();
	}*/
    }

    public static void a() throws Exception {
	try {
	    b();
	} catch (Exception e) {
	    throw new CommonException("a", e);
	}
    }

    public static void b() throws Exception {
	try {
	    c();
	} catch (Exception e) {
	    throw new CommonException("b", e);
	}
    }

    public static void a1() throws Exception {
	    b1();
    }

    public static void b1() throws Exception {
	    c();
    }
    public static void c() throws ClassNotFoundException, SQLException {
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(
		    "jdbc:mysql://localhost:3306/test", "root", "root");
	    System.out.println(connection.getAutoCommit());
    }
}
