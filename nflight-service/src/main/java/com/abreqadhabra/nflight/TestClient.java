/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the following 
 * conditions are met:
 * 
 * -Redistributions of source code must retain the above copyright  
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright 
 *  notice, this list of conditions and the following disclaimer in 
 *  the documentation and/or other materials provided with the 
 *  distribution.
 *  
 * Neither the name of Oracle or the names of 
 * contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that Software is not designed, licensed or 
 * intended for use in the design, construction, operation or 
 * maintenance of any nuclear facility.
 */
package com.abreqadhabra.nflight;

import java.net.InetAddress;
import java.rmi.registry.Registry;
import java.util.Arrays;

import com.abreqadhabra.nflight.service.core.NFlightService;
import com.abreqadhabra.nflight.service.core.boot.Profile;
import com.abreqadhabra.nflight.service.rmi.server.RMIManager;
import com.abreqadhabra.nflight.service.rmi.server.scoket.SecureSocketFactory;

public class TestClient {

	private TestClient() {
	}

	public static void main(String[] args) {

		try {

			String host = InetAddress.getLocalHost().getHostAddress();
			int port = 9999;
			SecureSocketFactory socketFactory = new SecureSocketFactory();
			Registry registry = RMIManager.getRegistry(host, port);
			// Registry registry = LocateRegistry.getRegistry();
			System.out.println(Arrays.toString(registry.list()));

			/*String command = System
					.getProperty(Env.Properties.BootCommand.PropertyKey.NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
							.toString());
*/

			String BASE_LOCATION = TestClient.class
					.getProtectionDomain().getCodeSource().getLocation().getFile();
			
		//	String command = "rmid  -stop";		
		//	String command = "rmid  -J-Djava.security.policy="+BASE_LOCATION+"com/abreqadhabra/nflight/service/core/boot/conf/boot.policy";
			String command = System.getProperty(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
							.toString());
			
			System.out
					.println(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
							.toString() + ": " + command);

	//		new BootCommand().execute(command);
			

			
	//		BootCommand.execute(command);

			NFlightService stub = (NFlightService) registry.lookup("rmi://"
					+ host
					+ ":9999/NFlight/UnicastRemoteObjectNFlightServiceImpl");
			// registry.lookup("rmi://192.168.0.100:9999/NFlight/UnicastRemoteObjectNFlightServiceImpl");
			String response = stub.sayHello();
			System.out.println("response: " + response);
			
			NFlightService stub2 = (NFlightService) registry.lookup("rmi://"
					+ host
					+ ":9999/NFlight/ActivatableNFlightServiceImpl");
			response = stub2.sayHello();
			System.out.println("response: " + response);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
