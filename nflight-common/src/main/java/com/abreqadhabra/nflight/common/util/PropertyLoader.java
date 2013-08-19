package com.abreqadhabra.nflight.common.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

/**
 * <p>
 * [개 요] 프로퍼티파일의 내용을 시스템 프로퍼티에 반영합니다.
 * </p>
 * <p>
 * [설 명] 모든 서버프로세스를 기동합니다.
 * </p>
 * <p>
 * [비 고]
 * </p>
 * <p>
 * [환 경] Java SDK 1.7_25
 * </p>
 * <p>
 * Copyright(c) Kim, Dong-Sup 2013
 * </p>
 * 
 * @author dongsup.kim@gmail.com
 * @since STEP1
 * @see PropertyLoader
 */
public class PropertyLoader {

	private static Class<PropertyLoader> THIS_CLAZZ = PropertyLoader.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	

	
	
	/**
	 * <p>
	 * [개 요] 지정된 파일을 읽어 내용을 시스템 프로퍼티에 반영합니다.
	 * </p>
	 * <p>
	 * [상 세]
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param nflightConfigFileName
	 * 
	 * @param String
	 *            propertyName 프로퍼티명
	 * @param String
	 *            fileName 프로퍼티파일의 파일명
	 * @return boolean 정상적으로 파일을 읽은 경우에는 true를 반환합니다.
	 * @since STEP1
	 */
	public static boolean load(String className, String filePath) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Reading Configuration");

			// String configurationFileProperty = propertyName;
			// String defaultConfigurationFile = propertyFileName;
			//
			// String configurationFile =
			// System.getProperty(configurationFileProperty,
			// defaultConfigurationFile);
			// InputStream propertiesInput = ClassLoader
			// .getSystemResourceAsStream(configurationFile);
			// if (propertiesInput == null) {
			// throw new NFlightRSystemException("Configuration Not Found")
			// .addContextValue("propertyName", propertyName).addContextValue(
			// "fileName", propertyFileName);
			// }
			// Properties properties = new Properties();
			// properties.load(propertiesInput);
			// propertiesInput.close();
			//

			Properties properties = PropertyFile.readPropertyFilePath(
					className, filePath);

			printProperties(properties);

			for (Object object : properties.keySet()) {
				String key = (String) object;
				if (System.getProperty(key) == null) {
					String value = properties.getProperty(key);
					System.setProperty(key, value);
				}
			}
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(), "Reading Configuration Failed");
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * [개 요] 클라리언트용 프로퍼티파일로 부터 프로퍼티리스트를 읽어 옵니다.
	 * </p>
	 * <p>
	 * [상 세] getLocation() 메서드가 반환하는 위치에서 클라이언트용 프로퍼티리스트를 읽어 옵니다.
	 * </p>
	 * <p>
	 * [비 고] 현재 보유하고있는 프로퍼티 키에 대한 값은 무시됩니다.
	 * </p>
	 * 
	 * @param String
	 *            propertyName 프로퍼티명
	 * @param String
	 *            fileName 프로퍼티파일의 파일명
	 * @return boolean 정상적으로 파일을 읽은 경우에는 true를 반환합니다.
	 * @exception IOException
	 *                입출력 에러가 발생한 경우
	 * @since STEP1
	 */

	/*
	 * public static boolean load(String propertyName) throws IOException {
	 * String METHOD_NAME =
	 * Thread.currentThread().getStackTrace()[1].getMethodName();
	 * 
	 * LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,"load");
	 * Properties prop = new Properties(); InputStream propertiesInput = null;
	 * try { URL propertiesLocation = getLocation(propertyName); if
	 * (propertiesLocation == null) { throw new
	 * FileNotFoundException("Not found configuration file"); } propertiesInput
	 * = propertiesLocation.openStream(); prop.load(propertiesInput); } finally
	 * { if (propertiesInput != null) { propertiesInput.close(); } } return
	 * true; }
	 */

	public static void printProperties(Properties props) {

		Set<Object> keys = props.keySet();
		keys = props.keySet();
		// System.out.println("\n----\n" + propertyFile + "\n");
		for (Object obj : keys) {
			System.out.println(":: Key = " + obj.toString() + "\tValue = "
					+ props.getProperty(obj.toString()));
		}
		System.out.println("----\n");
	}

	public static void setSystemProperties(Properties props) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		for (Object object : props.keySet()) {
			String key = (String) object;
			if (System.getProperty(key) == null) {
				String value = props.getProperty(key);
				System.setProperty(key, value);
			}
		}
		if (LOGGER.isLoggable(Level.CONFIG)) {
			Properties systemProps = System.getProperties();
			String[] keys = systemProps.keySet().toArray(new String[0]);
			Arrays.sort(keys);
			StringBuffer sb = new StringBuffer("\n");
			for (String key : keys) {
				if (key.startsWith("nflight.") | key.startsWith("java.")
						| key.startsWith("sun.")) {
					String formatString = ":: key = %-50s value = %s%n";
					String str = String.format(formatString, key,
							systemProps.get(key));
					sb.append(str);
				}
			}
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"System Properties:" + sb);
		}
	}
	



	/**
	 * <p>
	 * [개 요] 지정된 파일을 읽어 내용을 시스템 프로퍼티에 반영합니다.
	 * </p>
	 * <p>
	 * [상 세]
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param String
	 *            propertyName 프로퍼티명
	 * @param String
	 *            fileName 프로퍼티파일의 파일명
	 * @return boolean 정상적으로 파일을 읽은 경우에는 true를 반환합니다.
	 * @since STEP1
	 */

	/**
	 * [機　能] このプロパティリストの保存先を URL で返します。 [説　明] CONFIGURATION_PROPERTY
	 * キーに対する値のファイル名から、 その位置を URL で返します。 ファイルの位置を変更する場合は、起動時にコマンドラインオプション
	 * -D"CONFIGURATION_PROPERTYキー名"="ファイル名" で変更してください。 [備　考] なし。
	 */
	/*
	 * public static URL getLocation(String configurationPropertyName) { URL
	 * location = null; if (location == null) { location =
	 * PropertyLoader.getResource(configurationPropertyName); } return location;
	 * }
	 */

	/**
	 * [機　能] 指定された名前のリソースを検索して返します。 [説　明] 以下の順に指定された名前のリソースを検索して返します。
	 * <ol>
	 * <li>指定された名前が URL として解釈できる場合は、 そのまま URL に変換して返します。 リソースの存在は確認しません。
	 * <li>指定された名前をファイルパスの直接参照として検索します。 ファイルまたはディレクトリが存在する場合は、 その絶対パスを URL
	 * に変換して返します。
	 * <li>指定された名前をこのクラスのパッケージ位置で検索します。 リソースが見つかった場合は、その URL を返します。
	 * <li>指定された名前をシステムクラスパスから検索します。 リソースが見つかった場合は、その URL を返します。
	 * <li>指定された名前のリソースが見つからない場合は、 null を返します。
	 * </ol>
	 * [備　考] なし。
	 * 
	 * @param name
	 *            リソースの名前
	 * @return リソースの URL
	 */
	/*
	 * public static URL getResource(String name) { URL resourceURL = null; try
	 * { resourceURL = new URL(name); } catch (MalformedURLException e) {
	 * resourceURL = null; } if (resourceURL != null) { return resourceURL; }
	 * File file = new File(name); if (file.exists()) { try { resourceURL =
	 * file.toURL(); } catch (MalformedURLException e) { resourceURL = null; } }
	 * if (resourceURL != null) { return resourceURL; } resourceURL =
	 * PropertyLoader.class.getResource(name); if (resourceURL != null) { return
	 * resourceURL; } resourceURL = ClassLoader.getSystemResource(name); if
	 * (resourceURL != null) { return resourceURL; } return null; }
	 */

}
