package com.abreqadhabra.nf.examples.common.exception;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nf.common.exception.NestedError;
import com.abreqadhabra.nf.common.exception.NestedRuntimeException;
import com.abreqadhabra.nf.common.exception.SystemException;
import com.abreqadhabra.nf.common.exception.SystemLogicException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;



public class SystemExceptionExample {

    private static final Logger LOGGER = LoggingHelper
	    .getLogger(SystemExceptionExample.class);

    public static void main(String[] args) {
	SystemExceptionExample example = new SystemExceptionExample();

	try {
	     example.excute1(3);
	    // example.excute1(1);
	    example.excute1(2);
	    example.excute1(3);

	} catch (Exception e) {
	     
	  
	  LOGGER.info(e.toString());
	  

	    if (e instanceof SystemLogicException) {
		SystemLogicException sle = (SystemLogicException) e;
		System.out.println(sle.getErrorCd() + "\t" + sle.getParams()
			+ "\t" + sle.toString());

	    } else if (e instanceof SystemException) {
		SystemException se = (SystemException) e;
		System.out.println(se.getErrorCd() + "\t" + se.getParams()
			+ "\t" + se);
	    }else if (e instanceof NestedRuntimeException) {
		NestedRuntimeException nre = (NestedRuntimeException) e;
		nre.printStackTrace();
	    }

	}

    }


    
    private void excute1(int type) throws SystemException {
	try {

	    excute2(type);

	} catch (Exception e) {

	    if (e instanceof SystemLogicException) {
		throw (SystemLogicException) e;
	    } else if (e instanceof SystemException) {
		throw (SystemException) e;
	    } else if (e instanceof SQLException) {
		LOGGER.severe("登録時に予期せぬエラーが発生");
		throw new SystemLogicException(e, "W01011", null);
	    }else if (e instanceof NestedRuntimeException) {
		throw new NestedRuntimeException("X0001", e);
	    } else {
		LOGGER.severe("予期せぬエラー");
		throw new SystemException(e, "F00001", null);
	    }

	}
    }

    private void excute2(int type) throws Exception {

	switch (type) {
	case 0:
	    throw new SystemLogicException("W00005");
	case 1:
	    Exception ex = new Exception("データベース設定がメモリ上に存在しません。");
	    LOGGER.log(Level.SEVERE, "設定ファイルが正常にロードされていません。", ex);
	    throw new SystemException(ex, "E00001", null);
	case 2:
	    throw new SQLException();
	case 3:  
            //例外を投げる
            throw new NestedRuntimeException("パラメーターのCollectionに不正な要素が含まれています", new SQLException() );
	case 4:  
            // 一応内部エラーを発生させる
            throw new NestedError("例外が発生しました", new SQLException());
	
	default:
	    LOGGER.severe("メソッド引数が不正です(type=" + type + ")");
	    throw new SystemException("F00001");
	}

    }

}
