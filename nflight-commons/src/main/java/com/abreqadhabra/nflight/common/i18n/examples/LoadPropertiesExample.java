package com.abreqadhabra.nflight.common.i18n.examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import com.abreqadhabra.nflight.commons.constant.Env;

public class LoadPropertiesExample {

	public static void main(String[] args) {

		String propertyPath = "examples/resources/i18n/";

		String propertyFileName = "LoggingMessages_ko.properties";

		Properties properties = new Properties();

		String propertyFilePath = Env.CONFIG.BASE_LOCATION + propertyPath
				+ propertyFileName;

		properties = readPropertiesFromFilePath(propertyFilePath);

		printProperties(propertyFilePath, properties);

		properties.clear();

		String propertyClassPath = "/" + propertyPath + propertyFileName;

		properties = readPropertiesFromClassPath(propertyClassPath);

		printProperties(propertyClassPath, properties);

	}

	private static void printProperties(String propertyFile,
			Properties properties) {

		Set<Object> keys = properties.keySet();
		keys = properties.keySet();
		System.out.println("\n----\n" + propertyFile + "\n");
		for (Object obj : keys) {
			System.out.println(":: Key = " + obj.toString() + "\tValue = "
					+ properties.getProperty(obj.toString()));
		}
		System.out.println("----\n");
	}

	private static Properties readPropertiesFromFilePath(String filePath) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(filePath));
		} catch (IOException e) {
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