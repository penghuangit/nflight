package com.abreqadhabra.nflight.examples.common.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;

public class LoadPropertiesExample {
	private static Class<LoadPropertiesExample> THIS_CLAZZ = LoadPropertiesExample.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(String[] args) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String filePath = "com/abreqadhabra/nflight/examples/common/conf";
		String fileName = "logging.properties";
		
		Path propsFilePath = IOStream.getFilePath(THIS_CLAZZ.getName(),
				filePath, fileName);

		Properties props = readPropertiesFromFilePath(propsFilePath);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, fileName
				+ " is:\n" + props);

		props.clear();

		String classPath = "/" + filePath + "/" + fileName;
		props = readPropertiesFromClassPath(classPath);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, classPath
				+ " is:\n" + props);

	}



	private static Properties readPropertiesFromFilePath(Path path) {
		Properties properties = new Properties();

		try {
			properties
					.load(Files.newInputStream(path, StandardOpenOption.READ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return properties;
	}

	private static Properties readPropertiesFromClassPath(String classPath) {
		InputStream inputStream = LoadPropertiesExample.class
				.getResourceAsStream(classPath);
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

}