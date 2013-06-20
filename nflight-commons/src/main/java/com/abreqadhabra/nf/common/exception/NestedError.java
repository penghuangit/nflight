package com.abreqadhabra.nf.common.exception;


import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * <p>[概要] 子例外を持つエラー。</p>
 * <p>[詳細] printStackTrace()メソッドで子例外の例外情報も再帰的に出力します。</p>
 * <p>[備考] エラーなのでcatchしないで下さい。</p>
 * <p>[環境] Java2SDK 1.4.2_06</p>
 * <p>Copyright(c) NTT COMWARE 2004</p>
 * <p>$Revision: 1.1 $</p>
 * @author USE imatomi
 * @since STEP1
 */
public class NestedError extends Error {
    
    /** 
     * 履歴情報格納変数です。
     */
    private static final String rcsid = "$Id: NestedError.java,v 1.1 2005/10/18 02:52:23 koshida Exp $";
    
    /**
     * 子例外。
     */
    private Throwable _childException = null;
    
    
    /**
     * <p>[概 要] 指定された詳細メッセージを持つ Error を構築します。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param strArg 詳細メッセージ
     * @since STEP1
     */
    public NestedError(String strArg) {
        // 基底クラスの引数付きコンストラクタをそのまま呼び出す
        super(strArg);
    }
    
    /**
     * <p>[概 要] 指定された詳細メッセージと子例外を持つ Error を構築します。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param strArg 詳細メッセージ
     * @param childExceptionArg 子例外
     * @since STEP1
     */
    public NestedError(String strArg, Throwable childExceptionArg) {
        // 基底クラスの引数付きコンストラクタの呼び出し
        super(strArg);

        // 子例外
        this._childException = childExceptionArg;
    }
    
    /**
     * <p>[概 要] 子例外を取得します。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @return 子例外
     * @since STEP1
     */
    public Throwable getChildException() {
        // フィールドの値をそのまま返す
        return this._childException;
    }
    
    /**
     * <p>[概 要] Throwable とそのバックトレースを標準エラーストリームに出力します。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @since STEP1
     */
    public void printStackTrace() {
        // 標準エラーストリームを引数に渡して、実際に処理を行うメソッドを呼び出す
        printStackTrace(System.err);
    }
    
    /**
     * <p>[概 要] Throwable とそのバックトレースを指定された印刷ストリームに出力します。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param streamArg 出力に使用するPrintStream
     * @since STEP1
     */
    public void printStackTrace(PrintStream streamArg) {
        // 子例外が存在しない場合
        if (getChildException() == null) {
            // 基底クラスのメソッドをそのまま呼び出す
            super.printStackTrace(streamArg);
            
        // 子例外が存在する場合
        } else {
            // 自分自身の文字列表現を出力
            streamArg.println(this.toString());

            // 入れ子の例外の始まりの目印を出力
            streamArg.println("  nested exception is:");

            // 子例外のprintStackTrace()を呼び出す
            getChildException().printStackTrace(streamArg);
        }
    }
    
    /**
     * <p>[概 要] Throwable とそのバックトレースを指定されたプリントライターに出力します。</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param writerArg 出力に使用するPrintWriter
     * @since STEP1
     */
    public void printStackTrace(PrintWriter writerArg) {
        // 子例外が存在しない場合
        if (getChildException() == null) {
            // 基底クラスのメソッドをそのまま呼び出す
            super.printStackTrace(writerArg);
            
        //子例外が存在する場合
        } else {
            // 自分自身の文字列表現を出力
            writerArg.println(this.toString());

            // 入れ子の例外の始まりの目印を出力
            writerArg.println("  nested exception is:");

            // 子例外のprintStackTrace()を呼び出す
            getChildException().printStackTrace(writerArg);
        }
    }

}

/*
 * $Log: NestedError.java,v $
 * Revision 1.1  2005/10/18 02:52:23  koshida
 * 新規登録
 *
 */