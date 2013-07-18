package com.abreqadhabra.nflight.service.core.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.exception.NFBootException;

public abstract class AbstractServer {
	private static final Class<AbstractServer> THIS_CLAZZ = AbstractServer.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ProfileImpl profile;
	private IService service;
	
	public AbstractServer(ProfileImpl profile, IService service) throws Exception {
		this.setBootPofile(profile);
		this.setService(service);
		
		/*ServerFactory serverFactory = ServerFactory
				.getServerFactory(ServerFactory.SERVER_MODE_RMI);*/
		
		init();
		excute();
	}

	public IService getService() {
		return service;
	}

	public void setService(IService service) {
		this.service = service;
	}

	private void excute() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String serviceCommand = this.profile.getServiceCommand();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"serviceCommand: " + serviceCommand);
		Profile.BOOT_OPTION_SERVICE_COMMAND command = Profile.BOOT_OPTION_SERVICE_COMMAND
				.valueOf(serviceCommand);
		
		switch (command) {
		case startup:
			startup();
			break;
		case shutdown:
			shutdown();
			break;
		case status:
			boolean status = status();
			if (status == true) {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						"서비스가 현재 기동 중에 있습니다.");
			} else if (status == false) {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						"서비스를 찾을 수 없습니다.");
			}
			break;
		default:
			throw new NFBootException("Service 실행이 실패하였습니다.").addContextValue(
					"Service Command", command.toString());
		}
	}

	public abstract void init() throws Exception;

	public abstract void startup() throws Exception;

	public abstract void shutdown() throws Exception;

	public abstract boolean status() throws Exception;

	public ProfileImpl getBootPofile() {
		return profile;
	}

	public void setBootPofile(ProfileImpl bootPofile) {
		this.profile = bootPofile;
	}

}
