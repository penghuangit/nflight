package com.abreqadhabra.nflight.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Constants;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.NFlightServer;
import com.abreqadhabra.nflight.server.NFlightService;
import com.abreqadhabra.nflight.server.rmi.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.server.rmi.remote.UnicastRemoteObjectNFlightServiceImpl;

public class NFlightServerImpl implements
		NFlightServer {

	private static final Class<NFlightServerImpl> THIS_CLAZZ = NFlightServerImpl.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	public final String SERVICE_COMMAND = System
			.getProperty(Constants.Boot.KEY_BOOT_OPTION_SERVICE_COMMAND);

	private RMIManager rman;
	private static final long serialVersionUID = 1L;

	public NFlightServerImpl() throws Exception {
		init();
		execute();
	}

	private void init() throws Exception {
		this.rman = new RMIManager();
	}

	private void execute() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String boundName = this.rman
				.getBoundName(UnicastRemoteObjectNFlightServiceImpl
						.getObjName());
		
		boolean _isActivated = this.rman.isActivatedRegistry(boundName);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"SERVICE_COMMAND:" + SERVICE_COMMAND);

		switch (this.SERVICE_COMMAND) {
		case Constants.Boot.STR_SERVICE_COMMAND_STARTUP:
			if (_isActivated) {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						boundName + "가 이미 Registry에 등록되어 있습니다.");
			} else {
				this.startup();
			}
			break;
		case Constants.Boot.STR_SERVICE_COMMAND_SHUTDOWN:
			if (_isActivated) {
				this.shutdown();
			} else {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						boundName + "가  Registry에 등록되어 있지 않습니다.");
			}
			break;
		case Constants.Boot.STR_SERVICE_COMMAND_STATUS:
			if (_isActivated) {
				boolean status = this.status();
				if (status == true) {
					LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
							"서비스가 정상적으로 기동 중에 있습니다.");
				} else if (status == false) {
					LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
							"서비스를 찾을 수 없습니다.");
				}
			} else {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						boundName + "가  Registry에 등록되어 있지 않습니다.");
			}
			break;
		default:
			throw new NFlightRemoteException("Service 실행이 실패하였습니다.")
					.addContextValue("Service Command", this.SERVICE_COMMAND);
		}
	}

	@Override
	public void startup() throws Exception {
		try {

			// NFlightService uniServant = new
			// UnicastRemoteObjectNFlightServiceImpl();
			// NFlightService actServant = new ActivatableNFlightServiceImpl();

			Remote obj = this.rman.getUnicastRemoteObjectNFlightServiceImpl();
			// Remote obj = this.rman.getActivatableNFlightServiceImpl();
			String boundName = this.rman
					.getBoundName(UnicastRemoteObjectNFlightServiceImpl
							.getObjName());
			rman.rebind(boundName, obj);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void shutdown() throws Exception {
		try {
			String boundName = this.rman
					.getBoundName(UnicastRemoteObjectNFlightServiceImpl
							.getObjName());
			rman.unbind(boundName);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean status() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String boundName = this.rman
				.getBoundName(UnicastRemoteObjectNFlightServiceImpl
						.getObjName());
		
		NFlightService service = (NFlightService) this.rman
				.lookup(boundName);
		boolean _isRunning = false;
		if (service != null) {
			try {
				_isRunning = service.isRunning();
			} catch (RemoteException re) {
				throw new NFlightRemoteException("데이터서버의 상태를 확인할 수 없습니다.", re)
						.addContextValue("service", service).addContextValue(
								"isRunning", _isRunning);
			}
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"_isRunning: " + _isRunning);

		return _isRunning;
	}





}
