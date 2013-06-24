package com.abreqadhabra.nflight.common.exception;

import java.io.Serializable;

public class ExceptionContext implements Serializable {

    private static final long serialVersionUID = 1L;
    private String label = null;
    private Object value = null;

    public ExceptionContext(String label, Object value) {
	this.label = label;
	this.value = value;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
	this.value = value;
    }

}
