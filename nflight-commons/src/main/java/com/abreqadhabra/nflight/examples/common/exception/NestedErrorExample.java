package com.abreqadhabra.nflight.examples.common.exception;

import java.io.UnsupportedEncodingException;

import com.abreqadhabra.nflight.common.exception.NestedError;

public class NestedErrorExample {

    public static void main(String[] args) {
	a(System.getProperty("file.encoding"));
    }

    public static void a(String strArg) {
	b(strArg);
    }

    public static void b(String strArg) {
	c(strArg);
    }

    public static void c(String strArg) {
	try {

	    String charsetName = strArg;
	    // 문자열을 지정된 문자 인코딩 바이트 배열로 변환 - CP932/일본어
	    byte[] byteArray = strArg.getBytes(charsetName);
	    System.out.println(charsetName + " : " + byteArray.toString());

	    charsetName = "CP932";
	    // 문자열을 지정된 문자 인코딩 바이트 배열로 변환 - CP932/일본어
	    byteArray = strArg.getBytes(charsetName);
	    System.out.println(byteArray.length + " : " + byteArray.toString());

	    // 시스템의 기본 인코딩이 지원되지 않는 것은있을 수 없기 때문에 catch 처리
	} catch (UnsupportedEncodingException e) {
	    // 내부 오류로 처리
	    throw new NestedError("내부 오류가 발생하였습니다.", e);
	}
    }
}
