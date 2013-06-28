/**

  자바 데이터베이스 서버 샘플 프로그램은 Derby Network Server와 상호작용 하는 간단한 JDBC 애플리케이션입니다.
  
 이 프로그램은 다음과 같은 동작을 합니다.
 
 
 
 1) Java DB의 구동환경을 설정합니다. 
 a Derby Network Server를 사용하기 위해 derby.drda.startNetworkServer 시스템 프로퍼티를 사용(true)으로 등록합니다. 
 b Java DB가 사용하는 시스템 디렉토리를 설정합니다. 만일 대상 디렉토리가 존재하지 않다면 새롭게 생성합니다.
 c JDBC드라이버를 로드합니다.
 
 2) Embedded용 JDBC 드라이버를 로드합니다.
 * 
 * 


일반기능
로그를 출력합니다.

 1.	Derby Network Server를 시작합니다.
 2.	클라이언트용 JDBC 드라이버를 적재합니다.
 3. creates the database if not already created
 4. checks to see if the schema is already created, and if not,
 5. creates the schema which includes the table SAMPLETBL and corresponding indexes.
 6. connects to the database
 7. loads the schema by inserting data
 8. starts client threads to perform database related operations
 9. has each of the clients perform DML operations (select, insert, delete, update) using JDBC calls,
    i)	 one client opens an embedded connection to perform database operations
         You can open an embedded connection in the same JVM that starts the Derby Network
         Server.
    ii)  one client opens a client connection to the Derby Network Server to perform database operations.
 10.waits for the client threads to finish the tasks
 11.shuts down the Derby Network Server at the end of the demo

 <P>
 Usage: java nserverdemo.NsSample
 <P>
 Please note, a file derby.log is created in the directory you run this program.
 This file contains the logging of connections made with the derby network server
 */

package com.abreqadhabra.nflight.db.server.bin;

import java.io.File;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.derby.drda.NetworkServerControl;

import com.abreqadhabra.nflight.common.exception.CommonException;
import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.db.server.JavaDBServerControl;
import com.abreqadhabra.nflight.db.server.JavaDBUtil;
import com.abreqadhabra.nflight.db.server.exception.NFlightDBServerException;

public class JavaDBServer {
	private static final Class<JavaDBServer> THIS_CLAZZ = JavaDBServer.class;
	// 로그 출력을 위한 선언
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	// Derby Connect URL
	public static final String STRING_PROTOOL = "jdbc:derby:";
	public static final String STRING_DB_USER = "freelec";
	public static final String STRING_DB_PASSWORD = "freelec!@#123";
	public static final String STRING_DATABASE_NAME = "FREELEC";
	public static final String STRING_DATASOURCE_NAME = "jdbc/datasource";

	JavaDBServerControl serverControl = null;
	Connection dbConnection = null;

	// 생성자
	public JavaDBServer() {
		// 네트워크서버 환경 초기화
		initNetworkServerEnviroments();
	}

	// 네트워크서버 환경 초기화
	private void initNetworkServerEnviroments() {
		final String METHOD_NAME = "initNetworkServerEnviroments()";

		LOGGER.logp(
				Level.FINER,
				THIS_CLAZZ.getName(),
				METHOD_NAME,
				"Derby Network Server를 사용하기 위해 derby.drda.startNetworkServer 시스템 프로퍼티를 사용(true)으로 등록합니다.");

		System.setProperty("derby.drda.startNetworkServer", "true");
		System.setProperty("derby.drda.logConnections", "true");

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Derby Network Server가 사용하는 시스템 디렉토리를 설정합니다. 만일 대상 디렉토리가 존재하지 않다면 새롭게 생성합니다.");

		setDBSystemDirectory();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"JDBC 드라이버를 로드합니다.");

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
		File fileSystemDir = new File(systemDirectory);
		fileSystemDir.mkdir();
	}

	public static void main(String[] args) throws Exception {
		JavaDBServer dbServer = new JavaDBServer();
		try {
			dbServer.start();
			// 데이터베이스 환경 초기화
			dbServer.initDatabaseEnviroments();

			// dbServer.waitForConnection();
			// dbServer.shutdown();

		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof CommonException) {
				CommonException ce = (CommonException) e;
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), "\n" + ce.getStackTrace(e));
			} else {
				throw new NFlightUnexpectedException(e);
			}
		}

	}

	private void start() throws Exception {
		serverControl = new JavaDBServerControl(1621);
		serverControl.start();

		// serverControl.testForConnection();
		// serverControl.logConnection(true);동작불능?
		// serverControl.trace(true);
	}

	// 데이터베이스 환경 초기화
	private void initDatabaseEnviroments() throws Exception {
		final String METHOD_NAME = "initDatabaseEnviroments()";

		// DB프로퍼티설정
		Properties dbProperties = getDBProperties();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"dbProperties", dbProperties);

		dbConnection = serverControl.getConnection(dbProperties);
		serverControl.testConnection(dbConnection);
	}

	private Properties getDBProperties() {
		final String METHOD_NAME = "getDBProperties()";

		Properties properties = new Properties();
		// 데이터베이스 FREELEC에서 사용되는 사용자와 패스워드 설정
		properties.put("user", STRING_DB_USER);
		properties.put("password", STRING_DB_PASSWORD);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"properties", properties);

		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		return properties;
	}

	private void shutdown() throws Exception {
		serverControl.shutdown();
	}

	/**
	 * Tries to check if the Network Server is up and running by calling ping If
	 * successful, then it returns else tries for 50 seconds before giving up
	 * and throwing an exception.
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 *             when there is a problem with testing if the Network Server is
	 *             up and running
	 */
	private static void waitForConnection() throws Exception {
		final String METHOD_NAME = "waitForConnection()";

		NetworkServerControl server = null;
		try {
			// Use NetworkServerControl.ping() to wait for
			// NetworkServer to come up. We could have used
			// NetworkServerControl to start the server but the property is
			// easier.
			// Server instance for testing connection
			server = new NetworkServerControl();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Testing if Network Server is up and running!",
					server.getSysinfo());

		} catch (Exception e) {
			throw new NFlightDBServerException(e);
		}

		while (true) {
			server.ping();
			server.logConnections(true);
			System.out.println(server.getRuntimeInfo());

			Thread.currentThread().sleep(5000);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Derby Network Server now running");

		}

	}

}
