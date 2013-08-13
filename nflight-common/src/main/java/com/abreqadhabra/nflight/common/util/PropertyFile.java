package com.abreqadhabra.nflight.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.exception.NFSystemException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class PropertyFile {
	private static Class<PropertyFile> THIS_CLAZZ = PropertyFile.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void convertAllXMLProperties(Path dirPath)
			throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					dirPath.toString());
			BasicFileAttributes pathAttr = Files.readAttributes(dirPath,
					BasicFileAttributes.class);

			Properties props = new Properties();

			if (pathAttr.isDirectory()) {
				DirectoryStream<Path> ds = Files
						.newDirectoryStream(dirPath);
				for (Path file : ds) {
					BasicFileAttributes fileAttr = Files.readAttributes(
							file, BasicFileAttributes.class);
					if (fileAttr.isRegularFile()
							&& file.getFileName().toString()
									.endsWith(".properties")) {
						props = PropertyFile.readPropertyFilePath(
								THIS_CLAZZ.getName(), file.toString());
						PropertyFile.writeXMLPropertyFilePath(props, file);
					}
					if (fileAttr.isDirectory()) {
						convertAllXMLProperties(file);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Properties readPropertyFilePath(Path path) throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

	//	Path path = Paths.get(filePath);

		Properties props = new Properties();
		try {

			InputStream is = Files.newInputStream(path);
			props.load(is);
			is.close();
		} catch (IOException e) {
			throw new NFSystemException("Can't load properties: ", e)
					.addContextValue("path :", path);
		}
		LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
				"load properties : " + path + "\n"
						+ renderPropsText(props));
		return props;
	}
	
	public static Properties readPropertyFilePath(String className,
			String filePath) throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Path path = IOStream.getFilePath(className, filePath);

		Properties props = new Properties();
		try {

			InputStream is = Files.newInputStream(path);
			props.load(is);
			is.close();
		} catch (IOException e) {
			throw new NFSystemException("Can't load properties: ", e)
					.addContextValue("path :", path);
		}
		LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
				"load properties : " + path + "\n"
						+ renderPropsText(props));
		return props;
	}

	public static String renderPropsText(Properties props)

	{

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

	public static String stripFileExtension(String fileName) {
		int dotIdx = fileName.lastIndexOf('.');

		// if dot is in the first position,
		// we are dealing with a hidden file rather than an extension
		return (dotIdx > 0) ? fileName.substring(0, dotIdx) : fileName;
	}

	public static String stripFileName(String pathName) {

		pathName = pathName.replace("\\", "/");

		int slashIdx = pathName.lastIndexOf("/");

		// if dot is in the first position,
		// we are dealing with a hidden file rather than an extension
		return (slashIdx > 0) ? pathName.substring(slashIdx + 1,
				pathName.length()) : pathName;
	}

	private static void writeXMLPropertyFilePath(Properties props,
			Path path) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String newFileName = stripFileExtension(path.getFileName()
				.toString()) + ".xml";
		path = path.resolveSibling(newFileName);

		try {
			props.storeToXML(
					Files.newOutputStream(path, StandardOpenOption.CREATE),
					path.getFileName().toString(), Env.Charset);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, path
					+ " written successfully");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
