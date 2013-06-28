package com.abreqadhabra.nf.common.exception;


import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * <p>[개 요] 중첩된 예외 처리를 지원하는 시스템 공통 런타임 예외 클래스</p>
 * <p>[상 세] printStackTrace()메서드에서 중첩 예외의 예외 정보를 재귀적으로 출력합니다.</p>
 * <p>[비 고] RunTimeException을 상속받고 있기 때문에 반드시 try ~ catch를 추가할 필요는 없습니다.</p>
 * <p>[환 경] Java SDK 1.7_21</p>
 * <p>Copyright(c) Kim, Dong-Sup 2013</p>
 * @author dongsup.kim@gmail.com
 * @since STEP1
 */

public class NestedRuntimeException extends RuntimeException {
    

    /**
     * serialVersionUID 상수
     */
    private static final long serialVersionUID = -6965593075432839481L;
    
    /**
     * 중첩 예외
     */
    private Throwable nestedException = null;    
    
    /**
     * <p>[개 요] 지정된 상세 메시지를 가진 객체를 생성합니다.</p>
     * <p>[상 세] </p>
     * <p>[비 고] </p>
     * @param message 상세 메시지
     * @since STEP1
     */
    public NestedRuntimeException(String message) {
        // 슈퍼 클래스의 동일 인수를 가진 생성자를 호출합니다.
        super(message);
    }
    
    /**
     * <p>[개 요] 지정된 상세 메시지와 중첩 예외를 가진 객체를 생성합니다.</p>
     * <p>[상 세] </p>
     * <p>[비 고] </p>
     * @param message 상세 메시지
     * @param nestedException 중첩 예외
     * @since STEP1
     */
    public NestedRuntimeException(String message, Throwable nestedException) {
	// 부모  클래스의 동일 인수를 가진 생성자를 호출합니다.
	super(message);
        
        // 중첩 예외
        this.nestedException = nestedException;
    }
    
    /**
     * <p>[개 요] 중첩 예외를 가져옵니다.</p>
     * <p>[상 세] </p>
     * <p>[비 고] </p>
     * @return 중첩 예외
     * @since STEP1
     */
    public Throwable getChildException() {
        // 필드의 값을 그대로 반환
        return this.nestedException;
    }
    
    /**
     * <p>[개 요] Throwable의 추적값(Stack Trace)을 표준 에러 스트림에 출력합니다.</p>
     * <p>[상 세] </p>
     * <p>[비 고] </p>
     * @since STEP1
     */
    public void printStackTrace() {
        // 표준 오류 스트림을 인수에 전달하여 실제로 처리 할 메서드를 호출
        printStackTrace(System.err);
    }
    
    /**
     * <p>[개 요] Throwable의 추적값(Stack Trace)을 지정된 PrintStream에 출력합니다.</p>
     * <p>[상 세] </p>
     * <p>[비 고] </p>
     * @param printStream 출력에 사용할 PrintStream
     * @since STEP1
     */
    public void printStackTrace(PrintStream printStream) {
	// 중첩 예외가 존재하지 않는 경우
	if (getChildException() == null) {
	    // 부모 클래스의 메서드를 그대로 호출
	    super.printStackTrace(printStream);

	    // 중첩 예외가 존재하는 경우
	} else {
	    // 자신의 toString 메서드 문자열을 출력
	    printStream.println(this.toString());

	    // 중첩된 예외의 시작 메시지를 출력
	    printStream.println("  nested exception is:");

	    // 중첩 예외의 printStackTrace()를 호출
	    getChildException().printStackTrace(printStream);
	}
    }
    
    /**
     * <p>[개 요] Throwable의 추적값(Stack Trace)을 지정된 PrintWriter 출력합니다.</p>
     * <p>[상 세] </p>
     * <p>[비 고] </p>
     * @param printWriter 출력에 사용할 PrintWriter
     * @since STEP1
     */
    public void printStackTrace(PrintWriter printWriter) {
	// 중첩 예외가 존재하지 않는 경우
	if (getChildException() == null) {
	    // 부모 클래스의 메서드를 그대로 호출
	    super.printStackTrace(printWriter);

	    // 중첩 예외가 존재하는 경우
	} else {
	    // 자신의 toString 메서드 문자열을 출력
	    printWriter.println(this.toString());

	    // 중첩된 예외의 시작 메시지를 출력
	    printWriter.println("  nested exception is:");

	    // 중첩 예외의 printStackTrace()를 호출
	    getChildException().printStackTrace(printWriter);
	}
    }

    
}