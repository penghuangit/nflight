package com.abreqadhabra.nflight.service.rmi.server;

import com.abreqadhabra.nflight.service.core.boot.BootCommand;
import com.abreqadhabra.nflight.service.core.boot.Profile;

public class RMIDCommand {

	private static final String BASE_LOCATION = RMIDCommand.class
			.getProtectionDomain().getCodeSource().getLocation().getFile();
	
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
			startRMID();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void startRMID() throws Exception {
		// TODO Auto-generated method stub
		BootCommand cmd = new BootCommand();
		String command = System
				.getProperty(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
						.toString());
		command = command + " -J-D"
				+ Profile.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString()
				+ "=" + BASE_LOCATION + Profile.FILE_RMID_POLICY
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
				+ Profile.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString()
				+ "=" + BASE_LOCATION + Profile.FILE_RMID_POLICY
				+ " -log rmid.log";
		cmd.execute(command);
	}
}
