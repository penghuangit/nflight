package com.abreqadhabra.nflight.server.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.app.NFlightCommands;
import com.abreqadhabra.nflight.server.app.NFlightServer;
import com.abreqadhabra.nflight.server.exception.NFlightServerException;

public class RMIServerImpl extends NFlightCommands implements
		NFlightServer {

	public static final String NFLIGHT_SERVER_PROPERTY_RMISERVER_KEY = NFLIGHT_SERVER_PROPERTY_KEY
			+ ".rmiserver";
	public static final String NFLIGHT_SERVER_PROPERTY_RMISERVER_URL_KEY = NFLIGHT_SERVER_PROPERTY_RMISERVER_KEY
			+ ".url";

	public RMIServerImpl() throws Exception {
		super();
	}

	private static final Class<RMIServerImpl> THIS_CLAZZ = RMIServerImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static boolean checkStatus() throws Exception {

		// 데이터서버의 RMI 레지스트리의 포트 번호를 가져옵니다. - java -D<name>=<value> 시스템 속성
		String url = System.getProperty(
				NFLIGHT_SERVER_PROPERTY_RMISERVER_URL_KEY).trim();
		String port = url.substring(url.indexOf(':') + 1, url.lastIndexOf('/'));

		NFlightServer ns = getRemoteServerObject(NFLIGHT_SERVER_LOCALHOST, port);
		if (ns == null) {
			return false;
		}
		boolean isRunning = false;

		try {
			isRunning = ns.checkHealth();
		} catch (RemoteException e) {
			throw new NFlightServerException("데이터서버의 상태를 확인할 수 없습니다.", e)
					.addContextValue("isRunning", isRunning);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}

		return isRunning;
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

	public static NFlightServer getRemoteServerObject(String host, String port)
			throws Exception {
		final String METHOD_NAME = "NFlightServer connectNFlightRMIServer(String host, String port)";

		NFlightServer ns = null;
		NFlightServerException nse = null;

		LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
				"Connecting NFlight RMI Server...");

		String url = "//" + host + ":" + port + "/nflight";

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "url: "
				+ url);

		try {
			ns = (NFlightServer) Naming.lookup(url);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Connected " + url);
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
				ns = null;
			}
		}

		return ns;
	}

	/**
	 * <p>
	 * [機　能] データサーバの生死を確認する。
	 * </p>
	 * <p>
	 * [説　明] データサーバの生死を確認する。
	 * </p>
	 * <p>
	 * [備　考] なし
	 * </p>
	 * 
	 * @return boolean データサーバが生きているならば、true
	 * @throws RemoteException
	 */
	@Override
	public boolean checkHealth() throws RemoteException {
		return true;
	}

	@Override
	public void startup() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() throws RemoteException {
		// TODO Auto-generated method stub

	}

}
