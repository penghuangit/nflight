package com.abreqadhabra.nflight.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class IOStream {
	private static final Class<IOStream> THIS_CLAZZ = IOStream.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static String convertStreamToString(InputStream inputStream) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		StringBuffer sb = new StringBuffer();
		String charsetName = System
				.getProperty(Env.PROPERTIES_SYSTEM.SUN_JNU_ENCODING.toString());
		try {
			InputStreamReader isr = new InputStreamReader(inputStream,
					charsetName);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append("\n" + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"charsetName :" + charsetName);

		return sb.toString();
	}

	public static String getCodebase(String className) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProtectionDomain pd = cls.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		URL url = cs.getLocation();
		return url.getFile();
	}
}