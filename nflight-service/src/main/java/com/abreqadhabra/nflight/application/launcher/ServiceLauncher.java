package com.abreqadhabra.nflight.application.launcher;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.Env;
import com.abreqadhabra.nflight.application.server.ServerImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;

public class ServiceLauncher implements Launcher {
	private static final Class<ServiceLauncher> THIS_CLAZZ = ServiceLauncher.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ServiceLauncher() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
		
		if (System.getSecurityManager() == null) {
			LauncherHelper.setSecurityManager();
		}
	}

	@Override
	public void launch(final String[] cmdLineArgs) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Command-Line Arguments Size : " + cmdLineArgs.length);

		Properties props = null;

		if (cmdLineArgs.length == 0) {
			LOGGER.logp(Level.FINEST, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Settings specified in the default property file : "
							+ Env.FILE_BOOT_PROPERTIES);

			// Settings specified in the default property file
			props = PropertyFile.readPropertyFilePath(Env.FILE_BOOT_PROPERTIES);

		} else if (cmdLineArgs.length > 1
				&& cmdLineArgs[0].equals(Env.BOOT_OPTION_CONF)) {

			LOGGER.logp(Level.FINEST, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Settings specified in a property file : "
							+ Env.FILE_BOOT_PROPERTIES);

			String propsPath = cmdLineArgs[0];
			Path path = Env.CODE_BASE_PATH.resolve(propsPath);
			// Settings specified in a property file
			props = PropertyFile.readPropertyFilePath(path);
		} else if (cmdLineArgs.length > 0
				&& cmdLineArgs[0].startsWith(Env.BOOT_OPTION_PREFIX)) {

			LOGGER.logp(
					Level.FINEST,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"Settings specified as command line arguments : "
							+ Arrays.toString(cmdLineArgs));

			// Settings specified as command line arguments
			props = LauncherHelper.parseCMDLineArgs(cmdLineArgs);
		}

		final ProfileImpl profile = new ProfileImpl(props);

		// throw new
		// NFServiceException("No arguments specified for Night Flighr Service");

		new ServerImpl(profile);
	}
	
	

}
