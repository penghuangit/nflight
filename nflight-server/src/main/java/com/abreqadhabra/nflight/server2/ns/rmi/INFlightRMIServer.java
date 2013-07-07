package com.abreqadhabra.nflight.server2.ns.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>
 * [개 요] 데이터서버 인터페이스를 제공합니다.
 * </p>
 * <p>
 * [설 명] 데이터서버 인터페이스를 제공합니다.
 * </p>
 * <p>
 * [비 고] main 메소드를 구현하고 있습니다.
 * </p>
 * <p>
 * [환 경] Java SDK 1.7_25
 * </p>
 * <p>
 * Copyright(c) Kim, Dong-Sup 2013
 * </p>
 * 
 * @author dongsup.kim@gmail.com
 * @since STEP1
 * @see INFlightRMIServer
 */

public interface INFlightRMIServer extends Remote {

	/**
	 * <p>
	 * [개 요] 서버 헬스 체크.
	 * </p>
	 * <p>
	 * [상 세] 서버 헬스 체크.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @throws java.rmi.RemoteException
	 *             동작할수없는 상태일 경우, RemoteException를throw
	 * @since STEP1
	 */
	public void check() throws RemoteException;

	/**
	 * <p>
	 * [개 요] 데이터서버를 종료.
	 * </p>
	 * <p>
	 * [상 세] 데이터서버를 종료.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @throws java.rmi.RemoteException
	 *             동작할수없는 상태일 경우, RemoteException를throw
	 * @since STEP1
	 */
	public void shutdown() throws RemoteException;

	/**
	 * <p>
	 * [機　能] データサーバの生死を確認します。
	 * </p>
	 * <p>
	 * [説　明] データサーバの生死を確認します。
	 * </p>
	 * <p>
	 * [備　考] なし
	 * </p>
	 * 
	 * @return boolean データサーバが生きているならば、true
	 * @throws RemoteException
	 */

	/**
	 * <p>
	 * [개 요] 데이터서버를 기동유무를 체크.
	 * </p>
	 * <p>
	 * [상 세] 데이터서버를 기동유무를 체크.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @return boolean 데이터서버가 기동 중이면 true, 정지 중이면 false
	 * @throws java.rmi.RemoteException
	 *             동작할수없는 상태일 경우, RemoteException를throw
	 * @since STEP1
	 */
	public boolean checkStatus() throws RemoteException;
}
