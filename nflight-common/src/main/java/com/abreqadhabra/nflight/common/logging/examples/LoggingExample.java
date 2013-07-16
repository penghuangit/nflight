package com.abreqadhabra.nflight.common.logging.examples;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFSystemException;
import com.abreqadhabra.nflight.common.exception.NFUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class LoggingExample {

    private static final Class<LoggingExample> THIS_CLAZZ = LoggingExample.class;
    private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    public static void main(String[] args) throws NFUnexpectedException {
	final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	try {
	    // Log object entry
	    if (LOGGER.isLoggable(Level.FINER)) {
		LOGGER.entering(THIS_CLAZZ.getName(), METHOD_NAME);
	    }

	    // Method body
	    int number1 = 8;
	    int number2 = 7;
	    String operator = "*";
	    
	    LoggingExample example = new LoggingExample();
	    
	    int result = example.calculate(number1, number2, operator);

	    if (LOGGER.isLoggable(Level.FINER)) {
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
			number1 + operator + number2);
	    }

	    // Log exiting
	    if (LOGGER.isLoggable(Level.FINER)) {
		LOGGER.exiting(THIS_CLAZZ.getName(), METHOD_NAME,
			Integer.toString(result));
		// If the method does not return a value
		// LOGGER.exiting(THIS_CLAZZ.getName(), METHOD_NAME);
	    }
	} catch (Exception e) {
	    StackTraceElement[] current = e.getStackTrace();
	    if (e instanceof NFSystemException) {
		NFSystemException ce = (NFSystemException) e;
		LOGGER.logp(Level.SEVERE, current[0].getClassName(),
			current[0].getMethodName(), "\n" + ce.getStackTrace(e));
	    }else{
		throw new NFUnexpectedException(e);
	    }
	}

    }

    private int calculate(int number1, int number2, String operator)
	    throws Exception {
	switch (operator) // 연산자 별로 연산을 함
	{
	case "*":
	    return number1 *= number2; // multiple
	case "+":
	    return number1 += number2; // plus
	case "-":
	    return number1 -= number2; // minus
	case "/":
	    if (number2 == 0) {
		throw new NFSystemException("Divide zero")
			.addContextValue("number1", number1)
			.addContextValue("number2", number2)
			.addContextValue("operator", operator); // divide
	    } else {
		return number1 /= number2;
	    }
	case "%":
	    if (number2 == 0) {
		throw new NFSystemException("Divide zero")
			.addContextValue("number1", number1)
			.addContextValue("number2", number2)
			.addContextValue("operator", operator); // mod
	    } else {
		return number1 %= number2;
	    }
	default:
	    throw new NFSystemException("Unknown operator");
	}
    }

}
