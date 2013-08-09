package com.abreqadhabra.nflight.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class IOStream {
	private static final Class<IOStream> THIS_CLAZZ = IOStream.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static String convertStreamToString(final InputStream inputStream) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final StringBuffer sb = new StringBuffer();
		final String charsetName = System
				.getProperty(Env.PROPERTIES_SYSTEM.SUN_JNU_ENCODING.toString());
		try {
			final InputStreamReader isr = new InputStreamReader(inputStream,
					charsetName);
			final BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append("\n" + line);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"charsetName :" + charsetName);

		return sb.toString();
	}

	
	public static Path getCodebasePath(final String className) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final ProtectionDomain pd = cls.getProtectionDomain();
		final CodeSource cs = pd.getCodeSource();
		final URL url = cs.getLocation();
		try {
			return Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//.replace(":", "/");
		
		return null;
	}
	
	public static String getCodebase(final String className) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final ProtectionDomain pd = cls.getProtectionDomain();
		final CodeSource cs = pd.getCodeSource();
		final URL url = cs.getLocation();
		return url.getFile();//.replace(":", "/");
	}

	public static URI getCodebaseURI(final String className) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final ProtectionDomain pd = cls.getProtectionDomain();
		final CodeSource cs = pd.getCodeSource();
		final URL url = cs.getLocation();
		URI uri = null;
		try {
			uri = url.toURI();
		} catch (final URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return uri;
	}

	public static Path getFilePath(final String className,
			final String... paths) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final URI codebaseURI = IOStream.getCodebaseURI(className);
		Path filePath = Paths.get(codebaseURI);
		for (final String path : paths) {
			filePath = filePath.resolve(path);
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"filePath :" + filePath);

		return filePath;
	}
}