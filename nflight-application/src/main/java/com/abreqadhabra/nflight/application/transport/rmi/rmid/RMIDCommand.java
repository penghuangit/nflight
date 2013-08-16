package com.abreqadhabra.nflight.application.transport.rmi.rmid;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.transport.rmi.activation.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.launcher.Configure;
import com.abreqadhabra.nflight.common.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.launcher.runtime.process.SystemCommand;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMIDCommand implements Runnable {
	private static Class<RMIDCommand> THIS_CLAZZ = RMIDCommand.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	String command;

	public RMIDCommand(String command) {
		this.command = command;
	}

	@Override
	public void run() {
		SystemCommand cmd = new SystemCommand();
		try {
			cmd.execute(command);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Configure configure = new ConfigureImpl(THIS_CLAZZ,
				Configure.FILE_SERVICE_PROPERTIES);

		String command = configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START);

		command = command + " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString() + "="
				+ Configure.FILE_RMID_POLICY + " -log "
				+ Configure.CODE_BASE_PATH.resolve("rmid.log");

		new Thread(new RMIDCommand(command)).start();
	}
}
