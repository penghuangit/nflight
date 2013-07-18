package com.abreqadhabra.nflight.service.rmi.server;

import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.ProfileImpl;

 // Client
public class RMIServerAbstractFactoryTest {
	private static final Class<RMIServerAbstractFactoryTest> THIS_CLAZZ = RMIServerAbstractFactoryTest.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(String args[]) throws Exception {

		// Settings specified in the default property file
		Properties props = PropertyFile.readPropertyFilePath(
				THIS_CLAZZ.getName(), Profile.FILE_BOOT_PROPERTIES);

		ProfileImpl profile = new ProfileImpl(props);

		RMIServerAbstractFactory unicastAbstractFactory = RMIServerFactoryMaker
				.getFactory("unicast");
		AbstractRMIServer unicastServer = unicastAbstractFactory
				.createServer(profile);
		AbstractRMIService unicastService = unicastAbstractFactory
				.createService();

		RMIServerAbstractFactory activatableAbstractFactory = RMIServerFactoryMaker
				.getFactory("activatable");
		AbstractRMIServer activatableServer = activatableAbstractFactory
				.createServer(profile);
		AbstractRMIService activatableService = activatableAbstractFactory
				.createService();

		// more function calls on product
	}
}