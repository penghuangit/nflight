package com.abreqadhabra.nflight.service.rmi.server;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.rmi.RMIManager;
import com.abreqadhabra.nflight.service.core.server.NFServer;
import com.abreqadhabra.nflight.service.core.server.NFService;
import com.abreqadhabra.nflight.service.exception.NFRemoteException;

public abstract class AbstractRMIServer extends NFServer {

	private static final Class<AbstractRMIServer> THIS_CLAZZ = AbstractRMIServer.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	protected RMIManager rman;
	private boolean isActivated;

	public AbstractRMIServer(ProfileImpl profile) throws Exception {
		super(profile);
	}
	
	@Override
	public void init() throws Exception {
		this.rman = new RMIManager();
		this.isActivated = isRegistered();
	}
	
	private boolean isRegistered() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		List<String> serviceNameList = Arrays.asList(RMIServerFactoryMaker.SERVICE_NAMES);
		for (int i = 0; i < serviceNameList.size(); i++) {
			String serviceName = rman.getBoundName(serviceNameList.get(i));
			System.out.println(serviceName);

			if (this.rman.isActivatedRegistry(serviceName)) {
				return true;
			} else {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						serviceName + "가  Registry에 등록되어 있지 않습니다.");
			}
		}

		return false;
	}
	
	@Override
	public void shutdown() throws Exception {
		if (isActivated) {
			try {
				List<String> serviceNameList = Arrays.asList(RMIServerFactoryMaker.SERVICE_NAMES);
				for (int i = 0; i < serviceNameList.size(); i++) {
					String serviceName = rman.getBoundName(serviceNameList.get(i));
					rman.unbind(serviceName);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	@Override
	public boolean status() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		boolean _isRunning = false;
		if (isActivated) {
			List<String> serviceNameList = Arrays.asList(RMIServerFactoryMaker.SERVICE_NAMES);
			for (int i = 0; i < serviceNameList.size(); i++) {
				String serviceName = rman.getBoundName(serviceNameList.get(i));
				NFService service = (NFService) this.rman.lookup(serviceName);
				if (service != null) {
					try {
						_isRunning = service.isRunning();
					} catch (RemoteException re) {
						throw new NFRemoteException("서버의 상태를 확인할 수 없습니다.", re)
								.addContextValue("service", service)
								.addContextValue("isRunning", _isRunning);
					}
				}
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"serviceName is running: " + _isRunning);
			}

			return _isRunning;
		}

		return _isRunning;
	}
}
