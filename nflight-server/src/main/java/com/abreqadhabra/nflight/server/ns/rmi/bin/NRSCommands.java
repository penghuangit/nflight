package com.abreqadhabra.nflight.server.ns.rmi.bin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.server.StartupAllServer;
import com.abreqadhabra.nflight.server.exception.NFlightServerException;
import com.abreqadhabra.nflight.server.ns.rmi.INFlightRMIServer;

/**
 * <p>
 * [개 요] 데이터서버 관련 커맨드를 준비하고 있습니다.
 * </p>
 * <p>
 * [설 명] 데이터 서버의 시작, 종료, 상태 모니터링 명령이 있습니다.
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
 * @see NRSCommands
 */
public class NRSCommands {

	private static final Class<NRSCommands> THIS_CLAZZ = NRSCommands.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/**
	 * <p>
	 * [개 요] 데이터 서버에 기동/종료합니다.
	 * </p>
	 * <p>
	 * [상 세] 데이터 서버에 기동/종료합니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 * @since STEP1
	 */

	public static void main(String args[]) throws Exception {
		final String METHOD_NAME = "void main(String[] args)";

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "args[]:"
				+ Arrays.toString(args));

		

		// ＲＭＩレジストリのポート番号を取得します。
		String noturl = System.getProperty("nflight.server.nrs.rmi.url")
				.trim();
		String port = noturl.substring(noturl.indexOf(':') + 1,
				noturl.lastIndexOf('/'));

		if (args[0].equals("boot")) {
			// データサーバがすでに起動していない事を確認してから、起動します。
			if (checkstatus("localhost", port)) {
				LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(), METHOD_NAME,
						"Dataserver is already running.");
				System.exit(0);
			} else {
				boot(args);
			}
		} else if (args[0].equals("shutdown")) {
			shutdown("localhost", port);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"데이터서버를 정지하였습니다.");
		} else if (args[0].equals("status")) {
			boolean status = checkstatus("localhost", port);
			if (status == true) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"데이터서버가 기동 중에 있습니다.");
			} else if (status == false) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"데이터버서가 정지 중에 있습니다.");
			}
		}
	}

	/**
	 * <p>
	 * [개 요] 데이터 서버의 상태를 체크하는 명령입니다.
	 * </p>
	 * <p>
	 * [상 세] 이터 서버의 상태를 체크하는 명령입니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param host
	 *            데이터서버의 호스트명
	 * @param port
	 *            데이터서버의 포트번호
	 * @return boolean 데이터서버가 기동 중이면 true, 정지 중이면 false
	 * @throws Exception
	 * @since STEP1
	 */
	public static boolean checkstatus(String host, String port)
			throws Exception {
		INFlightRMIServer nrs = connectNFlightRMIServer(host, port);
		if (nrs == null) {
			return false;
		}
		boolean isExist = false;

		try {
			isExist = nrs.checkStatus();
		} catch (RemoteException e) {
			throw new NFlightServerException("데이터서버의 상태를 확인할 수 없습니다.", e)
					.addContextValue("isExist", isExist);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}

		return isExist;
	}

	/**
	 * <p>
	 * [개 요] 데이터 서버에 접속합니다.
	 * </p>
	 * <p>
	 * [상 세] 데이터 서버에 접속합니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param host
	 *            데이터서버의 호스트명
	 * @param port
	 *            데이터서버의 포트번호
	 * @return INFlightRMIServer 데이터서버에 접속 불가의 경우에는 null
	 * @throws Exception
	 * @since STEP1
	 */

	public static INFlightRMIServer connectNFlightRMIServer(String host,
			String port) throws Exception {
		final String METHOD_NAME = "INFlightRMIServer connectNFlightRMIServer(String host, String port)";

		INFlightRMIServer nrs = null;
		NFlightServerException nse = null;

		LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
				"Connecting NFlightRMIServer...");

		String nrsURL = "//" + host + ":" + port + "/nflight";
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"NFlightRMIServerURL: " + nrsURL);

		try {
			nrs = (INFlightRMIServer) Naming.lookup(nrsURL);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Connected " + nrsURL);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			StackTraceElement[] current = e.getStackTrace();
			// nse = new
			// NFlightServerException("Exception occured during connecting NFlightRMIServer.",
			// e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		} finally {
			if (nse != null) {
				StackTraceElement[] current = nse.getStackTrace();
				LOGGER.logp(Level.FINER, current[0].getClassName(),
						current[0].getMethodName(),
						"Exception occured during connecting NFlightRMIServer."
								+ nse.getStackTrace(nse));
				nrs = null;
			}
		}

		return nrs;
	}

	/*
	 * try { ds = (INFlightRMIServer) Naming.lookup(nrsURL); } catch
	 * (RemoteException e) { ex = e; } catch (NotBoundException e) { ex = e; }
	 * catch (MalformedURLException e) { ex = e; } finally { StackTraceElement[]
	 * current = ex.getStackTrace(); if (ex != null) { LOGGER.logp(
	 * Level.SEVERE, current[0].getClassName(), current[0].getMethodName(),
	 * "Exception occured during connecting DataServer." + ex.getMessage()); ds
	 * = null; } else if (ex == null) { LOGGER.logp(Level.FINER,
	 * THIS_CLAZZ.getName(), METHOD_NAME, "Connected DataServer" +
	 * ex.getMessage()); } }
	 */

	/**
	 * <p>
	 * [개 요] 데이터 서버를 시작하는 명령입니다.
	 * </p>
	 * <p>
	 * [상 세] 데이터 서버를 시작하는 명령입니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param str
	 *            []
	 * @since STEP1
	 */
	public static void boot(String[] str) {
		Runtime runtime = Runtime.getRuntime();
		String[] str1 = new String[str.length - 1];
		for (int i = 0; i < str1.length; i++) {
			str1[i] = str[i + 1];
		}

		try {
			runtime.exec(str1);
		} catch (IOException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(
					Level.SEVERE,
					current[0].getClassName(),
					current[0].getMethodName(),
					"Can't boot "+str1+" in background process:\n"
							+ e.getMessage());
		}
	}

	/**
	 * <p>
	 * [개 요] 데이터 서버를 정지하는 명령입니다.
	 * </p>
	 * <p>
	 * [상 세] 데이터 서버를 정지하는 명령입니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param host
	 *            데이터서버의 호스트명
	 * @param port
	 *            데이터서버의 포트번호
	 * @throws Exception
	 * @since STEP1
	 */

	public static void shutdown(String host, String port) throws Exception {
		final String METHOD_NAME = "void shutdown(String host, String port)";

		INFlightRMIServer ds = connectNFlightRMIServer(host, port);
		if (ds == null) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"데이터서버는 기동되어 있지 않습니다.");
			System.exit(1);
		}
		try {
			ds.shutdown();
		} catch (RemoteException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(),
					"데이터서버를 종료할 수 없습니다." + e.getMessage());
		}
	}

}