package com.abreqadhabra.nflight.examples.common.exception;

import java.sql.Connection;
import java.sql.DriverManager;

import com.abreqadhabra.nflight.common.exception.CommonException;

public class CommonExceptionExample {
    
    public static void main(String[] args) {
	try {
	    a();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void a() throws Exception {
	try {
	    b();
	} catch (Exception e) {
	    throw new CommonException("foo", e);
	}
    }

    public static void b() throws Exception {
	try {
	    c();
	} catch (Exception e) {
	    throw new CommonException("bar", e);
	}
    }

    public static void c() throws Exception {
	Class.forName("com.mysql.jdbc.Driver");
	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
	System.out.println(connection.getAutoCommit());
    }
}
