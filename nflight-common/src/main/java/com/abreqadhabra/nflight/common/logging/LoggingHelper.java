package com.abreqadhabra.nflight.common.logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingHelper {
	private static final Class<LoggingHelper> THIS_CLAZZ = LoggingHelper.class;
	private static final String DEFAULT_RESOURCE_BUNDLE_NAME = "com.abreqadhabra.nflight.common.conf.logging.LoggingMessages";
	private static final String LOGGING_CONFIG_FILE_NAME = "/com/abreqadhabra/nflight/common/conf/logging/logging.properties";

	/** Root Logger */
	private static final Logger ROOT_LOGGER = Logger.getLogger("");

	public static String describe(final Class<?> clazz) {
		return describe(clazz, "", "");
	}

	public static String describe(final Class<?> clazz, final String pad,
			final String leadin) {

		StringBuffer sb = new StringBuffer();

		if (clazz == null) {
			return sb.toString();
		} else {
			final String type = clazz.isInterface() ? "Interface" : clazz
					.isArray() ? "Array" : clazz.isPrimitive()
					? "Primitive"
					: clazz.isEnum() ? "Enum" : "Class";

			sb.append(String.format("%n%s%s%s %s [%s]", pad, leadin, type,
					clazz.getSimpleName(), clazz.getName()));
			for (final Class<?> interfaze : clazz.getInterfaces()) {
				sb.append(describe(interfaze, pad + "\t", "implements "));
			}
			sb.append(describe(clazz.getComponentType(), pad + "\t\t",
					"elements are "));
			sb.append(describe(clazz.getSuperclass(), pad + "\t\t", "extends "));
		}
		return sb.toString();
	}

	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz, null);
	}

	public static Logger getLogger(final Class<?> clazz,
			String resourceBundleName) {
		if (resourceBundleName == null) {
			resourceBundleName = DEFAULT_RESOURCE_BUNDLE_NAME;
		}
		final String loggerName = getPackageName(clazz, 4);
		final Logger _logger = Logger.getLogger(loggerName, resourceBundleName);
		final LogManager logManager = LogManager.getLogManager();
		final InputStream inputStream = THIS_CLAZZ
				.getResourceAsStream(LOGGING_CONFIG_FILE_NAME);
		try {
			logManager.readConfiguration(inputStream);
			inputStream.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			ROOT_LOGGER.log(Level.WARNING,
					"Failed to configure log manager due to an access problem",
					e);
		} catch (final IOException e) {
			ROOT_LOGGER.log(Level.WARNING,
					"Failed to configure log manager due to an IO problem", e);
		} catch (final Exception e) {
			ROOT_LOGGER
					.log(Level.WARNING,
							"Failed to configure log manager due to an unknown problem",
							e);
		}
		logManager.addLogger(_logger);

		return logManager.getLogger(loggerName);
	}

	private static String getPackageName(final Class<?> clazz, final int depth) {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		final String packageName = clazz.getPackage().getName();
		final StringBuffer sb = new StringBuffer();
		final String[] strArray = packageName.split("\\.");

		if (strArray.length >= depth) {
			for (int i = 0; i < depth; i++) {
				sb.append(strArray[i]);
				if (i < (depth - 1)) {
					sb.append(".");
				}
			}
		}

		return sb.toString();
	}

	public static void initializeLogging(final String componentName) {

		// Get the logger that you want to attach a custom Handler to
		final String defaultResourceBundleName = DEFAULT_RESOURCE_BUNDLE_NAME;
		final Logger logger = Logger.getLogger(componentName,
				defaultResourceBundleName);

		// Set up a custom Handler (see MyCustomHandler example)
		Handler handler = null;

		// handler = new CustomHandler("MyOutputFile.log");
		handler = new ConsoleHandler();

		// Set up a custom Filter (see MyCustomFilter example)
		final Vector<Level> acceptableLevels = new Vector<Level>();
		acceptableLevels.add(Level.ALL);
		acceptableLevels.add(Level.CONFIG);
		acceptableLevels.add(Level.FINE);
		acceptableLevels.add(Level.FINER);
		acceptableLevels.add(Level.FINEST);
		acceptableLevels.add(Level.INFO);
		acceptableLevels.add(Level.WARNING);
		acceptableLevels.add(Level.SEVERE);

		final Filter filter = new CustomFilter(acceptableLevels);

		// Set up a custom Formatter (see MyCustomFormatter example)
		final Formatter formatter = new CustomFormatter();

		// Connect the filter and formatter to the handler
		handler.setFilter(filter);
		handler.setFormatter(formatter);

		// Connect the handler to the logger
		logger.addHandler(handler);

		// avoid sending events logged to com.myCompany showing up in WebSphere
		// Application Server logs
		logger.setUseParentHandlers(false);

	}


	public static String getThreadName(Thread currentThread) {
		return currentThread.getName();
	}


}