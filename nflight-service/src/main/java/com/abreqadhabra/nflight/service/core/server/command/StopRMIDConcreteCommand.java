package com.abreqadhabra.nflight.service.core.server.command;

import com.abreqadhabra.nflight.service.core.BootCommand;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.command.Command;

public class StopRMIDConcreteCommand implements Command {
	BootCommand bootCommand;
	String command;

	public StopRMIDConcreteCommand() {
		this.bootCommand = new BootCommand();
		this.command = System
				.getProperty(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_STOP_WINDOWS
						.toString());
	}

	public void execute() throws Exception {
		bootCommand.execute(this.command);
	}
}