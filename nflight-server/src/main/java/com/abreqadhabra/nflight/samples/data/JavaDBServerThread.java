package com.abreqadhabra.nflight.samples.data;

import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.samples.data.bin.JavaDBServer;

public class JavaDBServerThread extends Thread {
	private static final Class<JavaDBServer> THIS_CLAZZ = JavaDBServer.class;
	// 로그 출력을 위한 선언
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	public static final String STRING_DB_USER = "freelec";
	public static final String STRING_DB_PASSWORD = "freelec!@#123";
	
	public static final String STRING_DATABASE_NAME = "FREELEC";

	public static final String STRING_PROTOOL = "jdbc:derby:";

	JavaDBServerControl serverControl = null;

	Connection dbConnection = null;

	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		serverControl = new JavaDBServerControl(1527);
		try {
			serverControl.start();
			initDatabaseEnviroments();
			while (true) {
				if (LOGGER.isLoggable(Level.FINER)) {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"now", DateUtils.now());
				}
				serverControl.waitForConnection();
				JavaDBServerThread.sleep(5000);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JavaDBServerThread() {
		// 네트워크서버 환경 초기화
		initNetworkServerEnviroments();

	}



	// 네트워크서버 환경 초기화
	private void initNetworkServerEnviroments() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					"Derby Network Server를 사용하기 위해 derby.drda.startNetworkServer 시스템 프로퍼티를 사용(true)으로 등록합니다.");
		}

		System.setProperty("derby.drda.startNetworkServer", "true");
		System.setProperty("derby.drda.logConnections", "true");

		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Derby Network Server가 사용하는 시스템 디렉토리를 설정합니다. 만일 대상 디렉토리가 존재하지 않다면 새롭게 생성합니다.");
		}

		setDBSystemDirectory();

		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"JDBC 드라이버를 로드합니다.");
		}
		JavaDBUtil.loadJDBCDriver();
	}

	// DB 시스템 디렉토리 설정
	private void setDBSystemDirectory() {
		// decide on the db system directory
		String userHomeDirectory = System.getProperty("user.home", ".");
		String systemDirectory = userHomeDirectory + "/."
				+ STRING_DATABASE_NAME;
		System.setProperty("derby.system.home", systemDirectory);
		// create the db system directory

//		File fileSystemDir = new File(systemDirectory);
//		if (fileSystemDir.exists()) {
//			fileSystemDir.mkdir();
//			logger.log(Level.INFO,
//					"Derby Network Server가 사용하는 시스템 디렉토리가 존재하지 않아 새롭게 생성하였습니다.");
//			
//			initDatabaseEnviroments();
//
//			
//		}
	}

	// 데이터베이스 환경 초기화
	private void initDatabaseEnviroments() throws Exception {
		// DB프로퍼티설정
		Properties dbProperties = getDBProperties();
		String url = getDatabaseUrl();

		dbConnection = serverControl.getConnection(dbProperties);
		serverControl.testConnection(dbConnection);
	}

	private Properties getDBProperties() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		Properties properties = new Properties();
		// 데이터베이스 FREELEC에서 사용되는 사용자와 패스워드 설정
		properties.put("user", STRING_DB_USER);
		properties.put("password", STRING_DB_PASSWORD);
		
		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"dbProperties", properties.toString());
		}
		
		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		return properties;
	}

	public String getDatabaseUrl() {
		String dbUrl = STRING_PROTOOL
				+ STRING_DATABASE_NAME;
		return dbUrl;
	}

}
