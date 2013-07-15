package com.abreqadhabra.nflight.service.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.boot.Profile;

public class IOStream {
	private static final Class<IOStream> THIS_CLAZZ = IOStream.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static String convertStreamToString(InputStream inputStream) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		StringBuffer sb = new StringBuffer();
		String charsetName = System
				.getProperty(Profile.PROPERTIES_SYSTEM.SUN_JNU_ENCODING
						.toString());
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
}