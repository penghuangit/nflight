package com.abreqadhabra.nflight.service.rmi.server;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.service.core.boot.BootCommand;
import com.abreqadhabra.nflight.service.core.boot.Profile;

public class RMIDCommand {
	
	private String codeBase = IOStream.getCodebase(RMIDCommand.class.getName());
	
	public RMIDCommand(){
		try {
			startRMID();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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

	private void startRMID() throws Exception {
		// TODO Auto-generated method stub
		BootCommand cmd = new BootCommand();
		String command = System
				.getProperty(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
						.toString());
		command = command + " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString()
				+ "=" + codeBase + Profile.FILE_RMID_POLICY
				+ " -log rmid.log";
		cmd.execute(command);
	}

	private void stopRMID() throws Exception {
		// TODO Auto-generated method stub
		BootCommand cmd = new BootCommand();
		String command = System
				.getProperty(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_STOP_WINDOWS
						.toString());
		command = command + " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString()
				+ "=" + codeBase + Profile.FILE_RMID_POLICY
				+ " -log rmid.log";
		cmd.execute(command);
	}
}
