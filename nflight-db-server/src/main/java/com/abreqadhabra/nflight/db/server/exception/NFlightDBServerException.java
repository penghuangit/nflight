package com.abreqadhabra.nflight.db.server.exception;

import com.abreqadhabra.nflight.common.exception.CommonException;

/**
 * <p>
 * [개 요] DB Server 공통 기본 예외클래스
 * </p>
 * <p>
 * [상 세] CommonException을 상속받은 DAO 공통  예외클래스
 * </p>
 * <p>
 * [비 고]
 * </p>
 * <p>
 * [환 경] Java SDK 1.7_21
 * </p>
 * <p>
 * Copyright(c) Kim, Dong-Sup 2013
 * </p>
 * 
 * @author dongsup.kim@gmail.com
 * @since STEP1
 * @see CommonException
 */
public class NFlightDBServerException extends CommonException {

    /**
     * JVM에서 자동으로 serialVersionUID를 생성시키기 위한 기본값 설정
     */
    private static final long serialVersionUID = 1L;

    /**
     * 에러ID
     * 
     * @since STEP1
     */
    private static final String ERROR_ID = "NF000002";

    /**
     * 메시지ID
     * 
     * @since STEP1
     */
    private static final String MESSAGE_ID = "CE000002";

    /**
     * 에러메시지
     * 
     * @since STEP1
     */
    private static final String MESSAGE = "데이터베이스 서버 실행중에 예기치 않은 오류가 발생했습니다.";

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
    public NFlightDBServerException(String message) {
	super(message);

	// NFlightException의 에러ID를 설정합니다.
	setErrorId(ERROR_ID);

	// NFlightException의 메시지ID를 설정합니다.
	setMessageId(MESSAGE_ID);

    }

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
     * @param cause
     *            자식 예외
     * @since STEP1
     */
    public NFlightDBServerException(String message, Throwable cause) {
	super(message, cause);

	// NFlightException의 에러ID를 설정합니다.
	setErrorId(ERROR_ID);

	// NFlightException의 메시지ID를 설정합니다.
	setMessageId(MESSAGE_ID);
    }

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
     * @param cause
     *            자식 예외
     * @since STEP1
     */
    public NFlightDBServerException(Throwable cause) {
	super(MESSAGE, cause);

	// NFlightException의 에러ID를 설정합니다.
	setErrorId(ERROR_ID);

	// NFlightException의 메시지ID를 설정합니다.
	setMessageId(MESSAGE_ID);
    }
}