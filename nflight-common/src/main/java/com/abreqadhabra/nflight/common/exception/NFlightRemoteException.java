package com.abreqadhabra.nflight.common.exception;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class NFlightRemoteException extends RemoteException implements
	IWrapperException {


	/**
     * JVM에서 자동으로 serialVersionUID를 생성시키기 위한 기본값 설정
     */
  	private static final long serialVersionUID = 1L;

    /**
     * 자식(중첩) 예외
     */
    private Throwable childException;

    /**
     * 에러ID
     */
    private String errorId;

    /**
     * 메시지ID
     */
    private String messageId;

    private List<ExceptionContext> contextEntries = new ArrayList<ExceptionContext>();

    /**
     * <p>
     * [개 요] 설정된 상세 메시지를 가진 Exception 객체를 생성합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @param message
     *            상세 메시지
     * @since STEP1
     */
    public NFlightRemoteException(String message) {
	// 슈퍼 클래스의 동일 인수를 가진 생성자를 호출합니다.
	super(message);
    }

    /**
     * <p>
     * [개 요] 설정된 상세 메시지와 자식 예외를 가진 객체를 생성합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @param message
     *            상세 메시지
     * @param childException
     *            자식 예외
     * @since STEP1
     */
    public NFlightRemoteException(String message, Throwable childException) {
	// 부모 클래스의 동일 인수를 가진 생성자를 호출합니다.
	super(message);
	// 자식 예외
	this.childException = childException;
    }

    /**
     * <p>
     * [개 요] 자식 예외를 가진 객체를 생성합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @param childException
     *            자식 예외
     * @since STEP1
     */
    public NFlightRemoteException(Throwable childException) {
	// 자식 예외
	this.childException = childException;
    }

    @Override
    public Throwable getChildException() {
	return this.childException;
    }

    @Override
    public void setErrorId(String errorId) {
	this.errorId = errorId;
    }

    @Override
    public String getErrorId() {
	return this.errorId;
    }

    @Override
    public void setMessageId(String messageId) {
	this.messageId = messageId;
    }

    @Override
    public String getMessageId() {
	return this.messageId;
    }

    @Override
    public List<ExceptionContext> getContextEntries() {
	return this.contextEntries;
    }

    @Override
    public NFlightRemoteException addContextValue(String label, Object value) {
	contextEntries.add(new ExceptionContext(label, value));
	return this;
    }

    @Override
    public NFlightRemoteException setContextValue(String label, Object value) {
	contextEntries.add(new ExceptionContext(label, value));
	return this;
    }

    @Override
    public String getMessage() {
	return getFormattedExceptionMessage(super.getMessage());
    }

    @Override
    public String getFormattedExceptionMessage(String baseMessage) {
	StringBuilder buffer = new StringBuilder(256);
	if (baseMessage != null) {
	    buffer.append(baseMessage);
	}

	if (contextEntries.size() > 0) {
	    if (buffer.length() > 0) {
		buffer.append('\n');
	    }
	    buffer.append("  Exception Context:\n");

	    int i = 0;
	    for (ExceptionContext contextValue : contextEntries) {
		buffer.append("\t[");
		buffer.append(++i);
		buffer.append(':');
		buffer.append(contextValue.getLabel());
		buffer.append("=");
		Object value = contextValue.getValue();
		if (value == null) {
		    buffer.append("null");
		} else {
		    String valueStr;

		    valueStr = value.toString();

		    buffer.append(valueStr);
		}
		buffer.append("]\n");
	    }
	    buffer.append("---------------------------------");
	}
	return buffer.toString();
    }

    /**
     * <p>
     * [개 요] Throwable의 추적값(Stack Trace)을 표준 에러 스트림에 출력합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @since STEP1
     */
    public void printStackTrace() {
	// 표준 오류 스트림을 인수에 전달하여 실제로 처리 할 메서드를 호출
	printStackTrace(System.err);
    }

    /**
     * <p>
     * [개 요] Throwable의 추적값(Stack Trace)을 설정된 PrintStream에 출력합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @param printStream
     *            출력에 사용할 PrintStream
     * @since STEP1
     */
    public void printStackTrace(PrintStream printStream) {
	// 자식 예외가 존재하지 않는 경우
	if (getChildException() == null) {
	    // 부모 클래스의 메서드를 그대로 호출
	    super.printStackTrace(printStream);

	    // 자식 예외가 존재하는 경우
	} else {
	    // 자신의 toString 메서드 문자열을 출력
	    printStream.println(this.toString());

	    // 중첩된 예외의 시작 메시지를 출력
	    printStream.println("  nested exception is:");

	    // 자식 예외의 printStackTrace()를 호출
	    getChildException().printStackTrace(printStream);
	}
    }

    /**
     * <p>
     * [개 요] Throwable의 추적값(Stack Trace)을 설정된 PrintWriter 출력합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @param printWriter
     *            출력에 사용할 PrintWriter
     * @since STEP1
     */
    public void printStackTrace(PrintWriter printWriter) {
	// 자식 예외가 존재하지 않는 경우
	if (getChildException() == null) {
	    // 부모 클래스의 메서드를 그대로 호출
	    super.printStackTrace(printWriter);

	    // 자식 예외가 존재하는 경우
	} else {
	    // 자신의 toString 메서드 문자열을 출력
	    printWriter.println(this.toString());

	    // 중첩된 예외의 시작 메시지를 출력
	    printWriter.println("  nested exception is:");

	    // 자식 예외의 printStackTrace()를 호출
	    getChildException().printStackTrace(printWriter);
	}
    }

	public static String getStackTrace(Exception e) {
		StringBuffer sb = null;
		try {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);

			sb = stringWriter.getBuffer();

			stringWriter.flush();
			stringWriter.close();
			printWriter.flush();
			printWriter.close();
		} catch (IOException ioe) {
		}

		if (sb != null) {
			return sb.toString();
		} else {
			return "";
		}
	}
}
