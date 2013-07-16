package com.abreqadhabra.nflight.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFPropertyException;
import com.abreqadhabra.nflight.common.exception.NFSystemException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class PropertyFile {
    private static final Class<PropertyFile> THIS_CLAZZ = PropertyFile.class;
    private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
    
	public static void convertAllPropertiesToXML(File dir) throws Exception {

		System.out.println(dir.getAbsolutePath());

		Properties properties = new Properties();
		if (dir.isDirectory()) {
			for (File child : dir.listFiles()) {
				if (child.isFile() && child.getName().endsWith(".properties")) {
					properties = PropertyFile
							.readPropertyFile(child
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
	
	public static void convertAllPropertiesToXML(String propertyFilePath) throws Exception {

		System.out.println(propertyFilePath);

		File dir = new File(propertyFilePath);
		
		Properties properties = new Properties();
		if (dir.isDirectory()) {
			for (File child : dir.listFiles()) {
				if (child.isFile() && child.getName().endsWith(".properties")) {
					properties = PropertyFile
							.readPropertyFile(child
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

	public static Properties readPropertyFile(String fileName) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		Properties properties = new Properties();
		try {
			InputStream is = THIS_CLAZZ.getResourceAsStream(fileName);
			if (is != null) {
				properties.load(is);
				is.close();
			} else {
				throw new NFPropertyException("Can't read property file")
						.addContextValue("fileName", fileName);
			}
			// properties.load(ClassLoader.getSystemResourceAsStream(fileName));
		} catch (IOException e) {
			throw new NFSystemException("Can't load properties: ", e)
					.addContextValue("fileName", fileName);
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				fileName+" : " +properties);
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
