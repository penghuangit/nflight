package com.abreqadhabra.nflight.examples.common.exception;

public class ChainingExceptionExample {

    public static void main(String[] args) {

	System.out.println("No Chaining:");
	try {
	    try {
		throw new Exception("foo");
	    } catch (Exception e) {
		throw new Exception("bar");
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);
	}

	System.out.println("\nChaining:");
	try {
	    try {
		throw new Exception("foo");
	    } catch (Exception e) {
		throw new Exception("bar", e);
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);

	}
    }
}