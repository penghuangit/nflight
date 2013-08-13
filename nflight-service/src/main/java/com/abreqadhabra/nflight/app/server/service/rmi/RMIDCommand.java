package com.abreqadhabra.nflight.app.server.service.rmi;

import com.abreqadhabra.nflight.app.core.BootCommand;
import com.abreqadhabra.nflight.app.core.Profile;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.util.IOStream;

public class RMIDCommand {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new RMIDCommand();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String codeBase = IOStream.getCodebase(RMIDCommand.class
			.getName());

	public RMIDCommand() {
		try {
			this.startRMID();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startRMID() throws Exception {
		// TODO Auto-generated method stub
		BootCommand cmd = new BootCommand();
		String command = System
				.getProperty(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
						.toString());
		command = command + " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString() + "="
				+ this.codeBase + Profile.FILE_RMID_POLICY + " -log rmid.log";
		cmd.execute(command);
	}
}
