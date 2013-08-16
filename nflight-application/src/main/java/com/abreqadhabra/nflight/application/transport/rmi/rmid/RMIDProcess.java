package com.abreqadhabra.nflight.application.transport.rmi.rmid;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.runtime.process.ProcessExecutor;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMIDProcess extends AbstractRunnable {
	private static Class<RMIDProcess> THIS_CLAZZ = RMIDProcess.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Configure configure;
	private ProcessExecutor processExecutor;

	public RMIDProcess(Configure configure) {
		super.setShutdownHookThread(new RMIDShutdownHook(this).getThread());
		this.configure = configure;
		processExecutor = new ProcessExecutor();
	}

	@Override
	public void start() throws NFlightException {
		String command = this.configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START)
				+ " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString()
				+ "="
				+ Configure.FILE_RMID_POLICY
				+ " -log "
				+ Configure.CODE_BASE_PATH.resolve("rmid.log");
		processExecutor.execute(command);
	}

	@Override
	public void stop() throws NFlightException {
		String command = this.configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_STOP);
		processExecutor.execute(command);
	}
}
