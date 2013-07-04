package com.abreqadhabra.nflight.common.exception;

import java.util.List;

public interface IWrapperException {

    /**
     * <p>
     * [개 요] 자식 예외를 가져옵니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @return 자식 예외
     * @since STEP1
     */
    public Throwable getChildException();

    /**
     * <p>
     * [개 요] 에러 ID를 설정합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @param errorId
     *            에러 ID
     * @since STEP1
     */
    public void setErrorId(String errorId);

    /**
     * <p>
     * [개 요] 에러 ID를 가져옵니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @return 에러 ID
     * @since STEP1
     */
    public String getErrorId();

    /**
     * <p>
     * [개 요] 메시지 ID를 설정합니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @param messageId
     *            메시지 ID
     * @since STEP1
     */
    public void setMessageId(String messageId);

    /**
     * <p>
     * [개 요] 상세 메시지를 가져옵니다.
     * </p>
     * <p>
     * [상 세]
     * </p>
     * <p>
     * [비 고]
     * </p>
     * 
     * @return 상세 메시지
     * @since STEP1
     */
    public String getMessageId();

    public WrapperException addContextValue(String label, Object value);

    public WrapperException setContextValue(String label, Object value);

    public List<ExceptionContext> getContextEntries();
    
    public String getMessage();

    public String getFormattedExceptionMessage(String baseMessage);


}
