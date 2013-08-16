package com.abreqadhabra.nflight.application.transport.rmi.rmid;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.launcher.runtime.process.SystemCommand;
import com.abreqadhabra.nflight.common.Env;

public class RMIDCommand implements Runnable {

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

		Configure configure = new ConfigureImpl(
				Configure.FILE_RMI_SERVICE_PROPERTIES);

		String command = configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START);

		command = command + " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString() + "="
				+ Configure.FILE_RMID_POLICY + " -log "
				+ Configure.CODE_BASE_PATH.resolve("rmid.log");

		new Thread(new RMIDCommand(command)).start();
	}
}
