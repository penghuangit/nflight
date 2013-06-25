package com.abreqadhabra.nflight.examples.common.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.CommonRuntimeException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRuntimeException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnexpectedRuntimeExceptionExample {

    private static final String CLASS_NAME = UnexpectedRuntimeExceptionExample.class
	    .getName();
    private static final Logger LOGGER = LoggingHelper.getLogger(CLASS_NAME);

    public static void main(String[] args) {
	final String METHOD_NAME = "main";
	

	try {
	 // Log object entry
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
	 
		LOGGER.logp(Level.FINER, CLASS_NAME, METHOD_NAME, "ENTRY");
		LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "ENTRY");
		LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD_NAME, "ENTRY");

		System.out.println("LOGGER:" + LOGGER.getLevel());

            // Method body
	  //  a();
            // Log exiting

            if (LOGGER.isLoggable(Level.FINER)) {
        	//LOGGER.exiting(CLASS_NAME, METHOD_NAME, new Boolean(result));
    
                //If the method does not return a value
        	LOGGER.exiting(CLASS_NAME, METHOD_NAME);
            }
	    
	    
	} catch (Exception e) {
	    StackTraceElement[] current = e.getStackTrace();
	    

		String className = current[0].getClassName();
		String methodName = current[0].getMethodName();
		int lineNumber = current[0].getLineNumber();
		String fileName = current[0].getFileName();
		
		
		
	    if (e instanceof CommonRuntimeException) {
		CommonRuntimeException ce = (CommonRuntimeException) e;
		System.out.println("에러ID:" + ce.getErrorId());
		System.out.println("메시지ID:" + ce.getMessageId());
		// System.out.println("Stack Trace:\n" + ce.getStackTrace(e));
		LOGGER.logp(Level.SEVERE, current[0].getClassName(),
			current[0].getMethodName(), "\n" + ce.getStackTrace(e));

	    }
	    // e.printStackTrace();

	}
    }

    public static void a() throws Exception {
	String transactionId = "TX0001";
	try {
	    b();
	} catch (Exception e) {
	    StackTraceElement[] current = e.getStackTrace();
	    if (e instanceof UnexpectedRuntimeException) {
		LOGGER.logp(Level.SEVERE, current[0].getClassName(),
			current[0].getMethodName(), "예기치 않은 오류가 발생했습니다.");

		throw new UnexpectedRuntimeException(e).addContextValue(
			"Transaction Id", transactionId);
	    }
	}
    }

    public static void b() throws Exception {

	String empno = "1234";
	String empName = "홍길동";
	String deptCode = "ABCD";
	String deptName = "IT기획팀";
	try {
	    c();
	} catch (Exception e) {
	    StackTraceElement[] current = e.getStackTrace();
	    if (e instanceof CommonRuntimeException) {
		throw new CommonRuntimeException("b", e);
	    } else {
		LOGGER.logp(Level.SEVERE, current[0].getClassName(),
			current[0].getMethodName(), "예기치 않은 오류가 발생했습니다.");
		UnexpectedRuntimeException ue = (UnexpectedRuntimeException) new UnexpectedRuntimeException(
			"b", e).addContextValue("empno", empno)
			.addContextValue("empName", empName);
		ue.setContextValue("deptCode", deptCode);
		ue.setContextValue("deptName", deptName);
		throw ue;
	    }
	}
    }

    public static void c() throws Exception {
	throw new Exception("c");
    }
}