package com.abreqadhabra.nflight.application.common.launcher;

import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyUtil;

// final 키워드를 사용하여 dl 클래스로부터 상속이 불가능하도록 설정
public final class Config {
	private static Class<Config> THIS_CLAZZ = Config.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	// // static inner class (여기도 final 키워드 사용) 를 사용하여 Singleton 클래스의 객체를 생성
	// private static final class ConfigSingleton {
	// // 역시 이 내부에서도 static final 키워드 사용
	// static final Config singleton = new Config();
	// }
	//
	// public static Config getInstance() {
	// return ConfigSingleton.singleton;
	// }

	public static void load(Class<?> clazz, Path path) throws NFlightException {
		Path codebasePath = IOStream.getCodebasePath(clazz.getName());
		path = codebasePath.resolve(path);
		PropertyUtil.loadSystemProperties(CLAZZ_NAME, path);
	}

	public static String get(String key) {
		return System.getProperty(key).trim();
	}

	public static void set(String key, String value) {
		System.setProperty(key, value);
	}

	public static Properties getProperties() {
		return System.getProperties();
	}

	public static int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public static boolean getBoolean(String key) {
		return Boolean.valueOf(get(key)).booleanValue();
	}
}
