package com.abreqadhabra.nf.common.exception;

/**
 * <p>[概 要] 共通ロジック例外クラス。</p>
 * <p>[詳 細] </p>
 * <p>[備 考] </p>
 * <p>[環 境] JDK 5.0 Update6</p>
 * <p>Copyright (c) 2006 NTT Corporation</p>
 * <p>$Id: SystemLogicException.java 4098 2006-10-25 07:21:03Z son $</p>
 * @author NTTコムウェア 有末英樹
 * @since STEP1a
 */
public class SystemLogicException extends SystemException {


    /**
     * <p>[概 要] コンストラクタ１。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param errorCd エラーID
     */
    public SystemLogicException(String errorCd) {
        super(errorCd);
    }

    /**
     * <p>[概 要] コンストラクタ２。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param errorCd エラーID
     * @param params 埋め込み文字列
     */
    public SystemLogicException(String errorCd, String[] params) {
        super(errorCd, params);
    }

    /**
     * <p>[概 要] コンストラクタ３。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param errorCd エラーID
     * @param param 埋め込み文字列
     */
    public SystemLogicException(String errorCd, String param) {
        super(errorCd, param);
    }

    /**
     * <p>[概 要] コンストラクタ４。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param e 例外オブジェクト
     * @param errorCd エラーID
     * @param params 埋め込み文字列
     */
    public SystemLogicException(Throwable e, String errorCd, String[] params) {
        super(e, errorCd, params);
    }
}
/*
 * $Log$
 */