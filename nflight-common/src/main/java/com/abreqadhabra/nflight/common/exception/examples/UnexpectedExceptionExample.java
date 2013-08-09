package com.abreqadhabra.nflight.common.exception.examples;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFSystemException;
import com.abreqadhabra.nflight.common.exception.NFUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnexpectedExceptionExample {

    private static final Class<UnexpectedExceptionExample> THIS_CLAZZ = UnexpectedExceptionExample.class;
    private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    public static void main(String[] args) {
	final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();
	try {
	    // Log object entry
	    if (LOGGER.isLoggable(Level.FINER)) {
		LOGGER.entering(THIS_CLAZZ.getName(), METHOD_NAME);
	    }

	    // Method body
	    level1();

	    // Log exiting
	    if (LOGGER.isLoggable(Level.FINER)) {
		LOGGER.exiting(THIS_CLAZZ.getName(), METHOD_NAME);
	    }
	} catch (Exception e) {
	    StackTraceElement[] current = e.getStackTrace();
	    if (e instanceof NFSystemException) {
		NFSystemException ce = (NFSystemException) e;
		LOGGER.logp(Level.SEVERE, current[0].getClassName(),
			current[0].getMethodName(), "\n" + ce.getStackTrace(e));
	    }
	}
    }

    public static void level1() throws Exception {
	String transactionId = "TX0001";
	try {
	    level2();
	} catch (Exception e) {
	    if (e instanceof NFUnexpectedException) {
		throw new NFUnexpectedException("Error at level 1", e)
			.addContextValue("Transaction Id", transactionId);
	    }
	}
    }

    public static void level2() throws Exception {

	String empno = "1234";
	String empName = "홍길동";

	try {
	    level3();
	} catch (Exception e) {
	    StackTraceElement[] current = e.getStackTrace();
	    if (e instanceof NFSystemException) {
		throw new NFSystemException("Error at level 2", e);
	    } else {
		NFUnexpectedException ure = new NFUnexpectedException(
			"Error at level 2", e);
		ure.setContextValue("empno", empno);
		ure.setContextValue("empName", empName);
		throw ure;
	    }
	}
    }

    public static void level3() throws Exception {
	throw new Exception("Error at level 3");
    }
}