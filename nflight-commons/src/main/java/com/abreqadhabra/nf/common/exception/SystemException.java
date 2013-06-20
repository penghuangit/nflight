package com.abreqadhabra.nf.common.exception;

/**
 * <p>[개요] 시스템 공통 예외 클래스</p>
 * <p>[상세] </p>
 * <p>[비고] </p>
 * <p>[환경] JDK 5.0 Update6</p>
 * <p>Copyright (c) 2013 Kim, Dong-Sup</p>
 * @author dongsup.kim@gmail.com
 * @since STEP1a
 */

public class SystemException extends RuntimeException {

    /**
     * serialVersionUID 상수
     */
    private static final long serialVersionUID = 344490355749585308L;

    /** 
     * 에러ID(메시지ID)
     */
    protected String errorCd = null;

    /** 
     * 메시지 문자열 배열
     */
    protected String[] params = null;

    /**
     * <p>[개요] 생성자 1</p>
     * <p>[상세] </p>
     * <p>[비고] </p>
     * @param errorCd 에러ID
     */
    public SystemException(String errorCd) {
        this.errorCd = errorCd;
    }

    /**
     * <p>[개요] 생성자 2</p>
     * <p>[상세] </p>
     * <p>[비고] </p>
     * @param errorCd エラーID
     * @param params 메시지 문자열 배열
     */
    public SystemException(String errorCd, String[] params) {
        this.errorCd = errorCd;
        this.params = params;
    }

    /**
     * <p>[개요] 생성자 3</p>
     * <p>[상세] </p>
     * <p>[비고] </p>
     * @param errorCd エラーID
     * @param param 메시지 문자열
     */
    public SystemException(String errorCd, String param) {
        this.errorCd = errorCd;
        String[] aParams = { param };
        this.params = aParams;
    }

    /**
     * <p>[개요] 생성자 3</p>
     * <p>[상세] </p>
     * <p>[비고] </p>
     * @param e 예외 객체
     * @param errorCd エラーID
     * @param param 메시지 문자열
     */
    public SystemException(Throwable e, String errorCd, String[] params) {
        super(e);
        this.errorCd = errorCd;
        this.params = params;
    }

    /**
     * <p>[概 要] 에러ID 출력 메서드</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @return 에러ID
     */
    public String getErrorCd() {
        return this.errorCd;
    }

    /**
     * <p>[概 要] 메시지 문자열 배열 출력 메서드</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @return 메시지 문자열 배열
     */
    public String[] getParams() {
        return this.params;
    }

}