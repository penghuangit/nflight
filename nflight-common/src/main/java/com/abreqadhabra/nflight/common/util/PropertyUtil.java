package com.abreqadhabra.nflight.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.PropertyException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class PropertyUtil {
	private static Class<PropertyUtil> THIS_CLAZZ = PropertyUtil.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void loadSystemProperties(String className, Path filePath)
			throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Reading Configuration");

		setSystemProperties(readProperties(className, filePath));

	}

	public static Properties readProperties(String className, Path filePath)
			throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Path path = getFilePath(className, filePath);

		Properties props = new Properties();
		try {

			InputStream is = Files.newInputStream(path);
			props.load(is);
			is.close();
		} catch (IOException e) {
			throw new PropertyException("Can't load properties: ", e)
					.addContextValue("path :", path);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"load properties : " + path + "\n" + renderPropsText(props));
		return props;
	}

	public static Path getFilePath(String className, Path... paths) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		URI codebaseURI = IOStream.getCodebaseURI(className);
		Path filePath = Paths.get(codebaseURI);
		for (Path path : paths) {
			filePath = filePath.resolve(path);
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"filePath :" + filePath);

		return filePath;
	}
	private static String renderPropsText(Properties props) {

		String[] keys = props.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();

		for (String key : keys) {
			String formatString = ":: key = %-60s value = %s%n";
			String str = String.format(formatString, key, props.get(key));
			sb.append(str);
		}
		return sb.toString();

	}

	public static void setSystemProperties(Properties props) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		for (Object object : props.keySet()) {
			String key = (String) object;
			if (System.getProperty(key) == null) {
				String value = props.getProperty(key);
				System.setProperty(key, value);
			}
		}
		if (LOGGER.isLoggable(Level.CONFIG)) {
			Properties systemProps = System.getProperties();
			String[] keys = systemProps.keySet().toArray(new String[0]);
			Arrays.sort(keys);
			StringBuffer sb = new StringBuffer("\n");
			for (String key : keys) {
				if (key.startsWith("nflight.") | key.startsWith("java.")
						| key.startsWith("sun.")) {
					String formatString = ":: key = %-80s value = %s%n";
					String str = String.format(formatString, key,
							systemProps.get(key));
					sb.append(str);
				}
			}
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"System Properties:" + sb);
		}
	}
}
