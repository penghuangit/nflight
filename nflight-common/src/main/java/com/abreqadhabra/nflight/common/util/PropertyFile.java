package com.abreqadhabra.nflight.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFile {

	public static void convertAllPropertiesToXML(File dir) {

		System.out.println(dir.getAbsolutePath());

		Properties properties = new Properties();
		if (dir.isDirectory()) {
			for (File child : dir.listFiles()) {
				if (child.isFile() && child.getName().endsWith(".properties")) {
					properties = PropertyFile
							.readTraditionalPropertyFile(child
									.getAbsolutePath());
					PropertyFile.writeXMLPropertyFile(properties,
							child.getAbsolutePath());
				}
				if (child.isDirectory()) {
					convertAllPropertiesToXML(child);
				}
			}
		}
	}
	
	public static void convertAllPropertiesToXML(String propertyFilePath) {

		System.out.println(propertyFilePath);

		File dir = new File(propertyFilePath);
		
		Properties properties = new Properties();
		if (dir.isDirectory()) {
			for (File child : dir.listFiles()) {
				if (child.isFile() && child.getName().endsWith(".properties")) {
					properties = PropertyFile
							.readTraditionalPropertyFile(child
									.getAbsolutePath());
					PropertyFile.writeXMLPropertyFile(properties,
							child.getAbsolutePath());
				}
				if (child.isDirectory()) {
					convertAllPropertiesToXML(child);
				}
			}
		}
	}

	/**
	 * This method reads property files from file system
	 * 
	 * @param propertyFileName
	 * @param xmlFileName
	 *            @
	 * @throws IOException
	 * @throws FileNotFoundException
	 *             경로명이 한글일 경우 파일을 찾을 수 없음
	 */
	public static Properties readXMLProperties(final String xmlPropertyFileName) {
		Properties properties = new Properties();
		InputStream is;
		try {
			is = new FileInputStream(xmlPropertyFileName);
			properties.loadFromXML(is);
			is.close();
		} catch (FileNotFoundException fnfEx) {
			fnfEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		return properties;
	}

	public static Properties readTraditionalPropertyFile(
			String traditionalPropertyFileName) {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader
				    .getSystemResourceAsStream(traditionalPropertyFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static void writeXMLPropertyFile(Properties properties,
			String traditionalPropertyFileName) {
		String xmlPropertyFileName = stripFileExtension(traditionalPropertyFileName)
				+ ".xml";

		try {
			System.out.println("Start of writePropertyFile");
			properties.storeToXML(new FileOutputStream(xmlPropertyFileName),
					stripFileName(xmlPropertyFileName), "UTF8");
			System.out.println(xmlPropertyFileName + " written successfully");
			System.out.println("End of writePropertyFile");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String stripFileName(String pathName) {

		pathName = pathName.replace("\\", "/");

		int slashIdx = pathName.lastIndexOf("/");

		// if dot is in the first position,
		// we are dealing with a hidden file rather than an extension
		return (slashIdx > 0) ? pathName.substring(slashIdx + 1,
				pathName.length()) : pathName;
	}

	public static String stripFileExtension(String fileName) {
		int dotIdx = fileName.lastIndexOf('.');

		// if dot is in the first position,
		// we are dealing with a hidden file rather than an extension
		return (dotIdx > 0) ? fileName.substring(0, dotIdx) : fileName;
	}
}
