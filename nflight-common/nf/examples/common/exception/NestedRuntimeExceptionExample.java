package com.abreqadhabra.nf.examples.common.exception;

import java.io.IOException;

import com.abreqadhabra.nf.common.exception.NestedRuntimeException;

public class NestedRuntimeExceptionExample {

    public static void main(String[] args) {

	System.out.println("***no chaining example:");
	try {
	    try {
		throw new IOException("One");
	    } catch (Exception e) {
		throw new IOException("Two");
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);
	}

	System.out.println("\n***chaining example 1:");
	try {
	    try {
		throw new IOException("One");
	    } catch (Exception e) {
		throw new IOException("Two", e);
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);
	    System.out.println("###what was the cause:");
	    e.getCause().printStackTrace(System.out);
	}

	System.out.println("\n***chaining example 2:");
	try {
	    try {
		throw new IOException("One");
	    } catch (Exception e) {
		throw new Exception("Two", e);
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out);
	}

	
	System.out.println("\n***nested example 1:");
	try {
	    try {
		throw new IOException("One");
	    } catch (Exception e) {
		throw new NestedRuntimeException("Two", e);
	    }
	} catch (Exception e) {
	    
	    e.printStackTrace(System.out);
	    if (e instanceof NestedRuntimeException) {
		NestedRuntimeException nre = (NestedRuntimeException) e;
		nre.printStackTrace();
	    }
	}

	
    }
    
    
    
    
  
}