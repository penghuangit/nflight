package com.abreqadhabra.nflight.service.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.boot.BootProfile;
import com.abreqadhabra.nflight.service.boot.Profile;
import com.abreqadhabra.nflight.service.boot.Profile.BOOT_OPTION_SERVICE_COMMAND;
import com.abreqadhabra.nflight.service.boot.exception.NFBootException;

public abstract class NFServer {
	private static final Class<NFServer> THIS_CLAZZ = NFServer.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private BootProfile bootPofile;
	
	public NFServer(BootProfile profile) throws Exception {
		this.setBootPofile(profile);
		init();
		excute();
	}

	private void excute() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String serviceCommand = this.bootPofile.getServiceCommand();
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

	public BootProfile getBootPofile() {
		return bootPofile;
	}

	public void setBootPofile(BootProfile bootPofile) {
		this.bootPofile = bootPofile;
	}

}
