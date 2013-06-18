package com.abreqadhabra.nflight.commons.exception;

public class ErrorInfo {

    private String errorContext = null;
    private String errorCode = null;
    private String errorText = null;

    public ErrorInfo(String contextCode, String errorCode, String errorText) {

	this.errorContext = contextCode;
	this.errorCode = errorCode;
	this.errorText = errorText;
    }

    public String getErrorContext() {
        return errorContext;
    }

    public void setErrorContext(String errorContext) {
        this.errorContext = errorContext;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
    
}
