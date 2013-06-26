package com.abreqadhabra.nflight.common.logging.examples;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class LoggingExample {

    private static final Class<LoggingExample> THIS_CLAZZ = LoggingExample.class;
    private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    /**
     * http://publib.boulder.ibm.com/infocenter/ledoc/v6r1/index.jsp?topic=/com.
     * ibm .rcp.tools.doc.appdev/serviceability_java.util.loggingbestpractices.
     * html
     */
    public static void main(String[] args) {
	final String METHOD_NAME = "main";

	try {
	    // Log object entry

	    LOGGER.entering(THIS_CLAZZ.getName(), METHOD_NAME);

	    // Method body
	    int number1 = 8;
	    int number2 = 7;
	    String operator = "*";
	    int result = Calculate(number1, number2, operator);
	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, number1
		    + operator + number2);

	    // Log exiting
	    LOGGER.exiting(THIS_CLAZZ.getName(), METHOD_NAME,
		    Integer.toString(result));
	    // If the method does not return a value
	    // LOGGER.exiting(THIS_CLAZZ.getName(), METHOD_NAME);

	} catch (Exception e) {
	    StackTraceElement[] current = e.getStackTrace();
	    if (e instanceof NFlightException) {
		NFlightException ce = (NFlightException) e;
		LOGGER.logp(Level.SEVERE, current[0].getClassName(),
			current[0].getMethodName(), "\n" + ce.getStackTrace(e));

	    }
	}

    }

    private static int Calculate(int number1, int number2, String operator)
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
		throw new NFlightException("Divide zero")
			.addContextValue("number1", number1)
			.addContextValue("number2", number2)
			.addContextValue("operator", operator); // divide
	    } else {
		return number1 /= number2;
	    }
	case "%":
	    if (number2 == 0) {
		throw new NFlightException("Divide zero")
			.addContextValue("number1", number1)
			.addContextValue("number2", number2)
			.addContextValue("operator", operator); // mod
	    } else {
		return number1 %= number2;
	    }
	default:
	    throw new NFlightException("Unknown operator");
	}

    }

}
