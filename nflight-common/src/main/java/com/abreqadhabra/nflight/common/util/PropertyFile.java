package com.abreqadhabra.nflight.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private static final Class<PropertyFile> THIS_CLAZZ = PropertyFile.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void convertAllXMLProperties(final Path dirPath)
			throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					dirPath.toString());
			final BasicFileAttributes pathAttr = Files.readAttributes(dirPath,
					BasicFileAttributes.class);

			Properties props = new Properties();

			if (pathAttr.isDirectory()) {
				final DirectoryStream<Path> ds = Files
						.newDirectoryStream(dirPath);
				for (final Path file : ds) {
					final BasicFileAttributes fileAttr = Files.readAttributes(
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
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Properties readPropertyFilePath(final Path path) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

	//	final Path path = Paths.get(filePath);

		final Properties props = new Properties();
		try {

			final InputStream is = Files.newInputStream(path);
			props.load(is);
			is.close();
		} catch (final IOException e) {
			throw new NFSystemException("Can't load properties: ", e)
					.addContextValue("path :", path);
		}
		LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
				"load properties : " + path + "\n"
						+ renderPropsText(props));
		return props;
	}
	
	public static Properties readPropertyFilePath(final String className,
			final String filePath) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final Path path = IOStream.getFilePath(className, filePath);

		final Properties props = new Properties();
		try {

			final InputStream is = Files.newInputStream(path);
			props.load(is);
			is.close();
		} catch (final IOException e) {
			throw new NFSystemException("Can't load properties: ", e)
					.addContextValue("path :", path);
		}
		LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
				"load properties : " + path + "\n"
						+ renderPropsText(props));
		return props;
	}

	public static String renderPropsText(final Properties props)

	{

		final String[] keys = props.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		final StringBuffer sb = new StringBuffer();

		for (final String key : keys) {
			final String formatString = ":: key = %-50s value = %s%n";
			final String str = String.format(formatString, key, props.get(key));
			sb.append(str);
		}
		return sb.toString();

	}

	public static String stripFileExtension(final String fileName) {
		final int dotIdx = fileName.lastIndexOf('.');

		// if dot is in the first position,
		// we are dealing with a hidden file rather than an extension
		return (dotIdx > 0) ? fileName.substring(0, dotIdx) : fileName;
	}

	public static String stripFileName(String pathName) {

		pathName = pathName.replace("\\", "/");

		final int slashIdx = pathName.lastIndexOf("/");

		// if dot is in the first position,
		// we are dealing with a hidden file rather than an extension
		return (slashIdx > 0) ? pathName.substring(slashIdx + 1,
				pathName.length()) : pathName;
	}

	private static void writeXMLPropertyFilePath(final Properties props,
			Path path) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final String newFileName = stripFileExtension(path.getFileName()
				.toString()) + ".xml";
		path = path.resolveSibling(newFileName);

		try {
			props.storeToXML(
					Files.newOutputStream(path, StandardOpenOption.CREATE),
					path.getFileName().toString(), Env.Charset);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, path
					+ " written successfully");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
