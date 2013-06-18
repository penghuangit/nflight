package com.abreqadhabra.nflight.commons.exception;

import java.util.ArrayList;
import java.util.List;

public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<ErrorInfo> errorInfoList = new ArrayList<ErrorInfo>();

    public CommonException(String errorContext, String errorCode,
	    String errorMessage) {
	this.addErrorInfo(errorContext, errorCode, errorMessage);
    }

    public CommonException(String errorContext, String errorCode,
	    String errorMessage, Throwable cause) {
	super(cause);
	this.addErrorInfo(errorContext, errorCode, errorMessage);
    }

    public CommonException addErrorInfo(String errorContext, String errorCode,
	    String errorText) {
	this.errorInfoList
		.add(new ErrorInfo(errorContext, errorCode, errorText));

	return this;
    }

    // StringBuffer 또는 StringBuilder 클래스 http://blog.naver.com/skawkslrk87?Redirect=Log&logNo=70168224180
    public String getCode() {
	StringBuilder sb = new StringBuilder();
	for (int i = this.errorInfoList.size() - 1; i >= 0; i--) {
	    ErrorInfo errorInfo = this.errorInfoList.get(i);
	    sb.append('[');
	    sb.append(errorInfo.getErrorContext());
	    sb.append(':');
	    sb.append(errorInfo.getErrorCode());
	    sb.append(']');
	}

	return sb.toString();
    }

    public String toString() {
	StringBuilder sb = new StringBuilder();

	sb.append(getCode());
	sb.append('\n');

	// append additional context information.
	for (int i = this.errorInfoList.size() - 1; i >= 0; i--) {
	    ErrorInfo info = this.errorInfoList.get(i);
	    sb.append('[');
	    sb.append(info.getErrorContext());
	    sb.append(':');
	    sb.append(info.getErrorCode());
	    sb.append(']');
	    sb.append(info.getErrorText());
	    if (i > 0) {
		sb.append('\n');
	    }
	}

	// append root causes and text from this exception first.
	if (getMessage() != null) {
	    sb.append('\n');
	    if (getCause() == null) {
		sb.append(getMessage());
	    } else if (!getMessage().equals(getCause().toString())) {
		sb.append(getMessage());
	    }
	}
	appendException(sb, getCause());

	return sb.toString();
    }

    private void appendException(StringBuilder sb, Throwable throwable) {
	if (throwable == null) {
	    return;
	} else {
	    appendException(sb, throwable.getCause());
	    sb.append(throwable.toString());
	    sb.append('\n');
	}
    }

}