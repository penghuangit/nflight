package examples.exception;

import com.abreqadhabra.nflight.commons.exception.CommonException;
import com.abreqadhabra.nflight.commons.exception.ExceptionHandler;

public class CommonExceptionExample {

    protected ExceptionHandler exceptionHandler = new ExceptionHandler() {
	public void handle(String errorContext, String errorCode,
		String errorText, Throwable t) {

	    if (!(t instanceof CommonException)) {
		throw new CommonException(errorContext, errorCode, errorText, t);
	    } else {
		((CommonException) t).addErrorInfo(errorContext, errorCode,
			errorText);
	    }
	}

	public void raise(String errorContext, String errorCode,
		String errorText) {
	    throw new CommonException(errorContext, errorCode, errorText);
	}
    };

    public void level1() {
	try {
	    level2();
	} catch (CommonException e) {
	    this.exceptionHandler.handle("L1", "E1",
		    "Error in level 1, calling level 2", e);
	    throw e;
	}
    }

    public void level2() {
	try {
	    level3();
	} catch (CommonException e) {
	    this.exceptionHandler.handle("L2", "E2",
		    "Error in level 2, calling level 3", e);
	    throw e;
	}
    }

    public void level3() {
	try {
	    level4();
	} catch (Exception e) {
	    this.exceptionHandler.handle("L3", "E3", "Error at level 3", e);
	}
    }

    public void level4() {
	throw new IllegalArgumentException("incorrect argument passed");
    }

    public static void main(String[] args) {
	CommonExceptionExample test = new CommonExceptionExample();
	try {
	    test.level1();
	} catch (Exception e) {
	    System.out.println(e.getClass().getCanonicalName());
	    e.printStackTrace();
	}
    }

}
