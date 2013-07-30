package com.abreqadhabra.nflight.samples.data;

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.derby.drda.NetworkServerControl;

import com.abreqadhabra.nflight.common.exception.NFUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.samples.data.bin.JavaDBServer;
import com.abreqadhabra.nflight.samples.data.exception.NFDataServerException;

public class JavaDBServerControl {

	private static final Class<JavaDBServer> THIS_CLAZZ = JavaDBServer.class;
	// 로그 출력을 위한 선언
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public final String STR_SQL_CREATE_FLIGHTS_TABLE = "CREATE TABLE FREELEC.flight ("
			+ "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
			+ "flight_number VARCHAR(10)  NOT NULL, "
			+ "origin_airport VARCHAR(10)  NOT NULL, "
			+ "destination_airport VARCHAR(10)  NOT NULL, "
			+ "carrier VARCHAR(20) NOT NULL, "
			+ "price INTEGER, "
			+ "departure TIMESTAMP, "
			+ "flight_type VARCHAR(8), "
			+ "available_seats INTEGER " + ")";

	public final String STR_SQL_INSERT_DUMMY_DATA = "INSERT INTO FREELEC.flight ("
			+ "flight_number, origin_airport, destination_airport, carrier, price, departure, flight_type, available_seats"
			+ ") VALUES "
			+ "('OZ8921', '김포', '제주', '아시아나항공', 400, TIMESTAMP('2012-09-23 12:00:00'), '국내선', 50), "
			+ "('MU512', '김포', '상해(홍차오)', '중국동방항공', 2000, TIMESTAMP('2012-09-23 12:00:00'), '국제선', 22), "
			+ "('JL92', '김포', '도쿄/하네다', '일본항공', 100, TIMESTAMP('2012-09-23 12:05:00'), '국제선', 37), "
			+ "('7C111', '김포', '제주', '제주항공', 100, TIMESTAMP('2012-09-23 12:05:00'), '국내선', 0), "
			+ "('KE1335', '김포', '여수', '대한항공', 800, TIMESTAMP('2012-09-23 12:20:00'), '국내선', 14), "
			+ "('OZ8813', '김포', '김해(부산)', '아시아나항공', 800, TIMESTAMP('2012-09-28 12:30:00'), '국내선', 4), "
			+ "('BX8813', '김포', '김해(부산)', '에어부산', 700, TIMESTAMP('2012-09-28 12:30:00'), '국내선', 97), "
			+ "('OZ8923', '김포', '제주', '아시아나항공', 700, TIMESTAMP('2012-09-30 12:30:00'), '국내선', 75), "
			+ "('CZ318', '김포', '북경', '중국남방항공', 756, TIMESTAMP('2012-09-30 12:30:00'), '국제선', 43), "
			+ "('NH1162', '김포', '도쿄/하네다', '전일본공수', 756, TIMESTAMP('2012-09-30 12:40:00'), '국제선', 28), "
			+ "('JL972', '김포', '오사카(간사이)', '일본항공', 536, TIMESTAMP('2012-09-30 12:45:00'), '국제선', 78), "
			+ "('OZ8927', '김포', '제주', '아시아나항공', 536, TIMESTAMP('2012-09-30 12:45:00'), '국내선', 21), "
			+ "('OZ8929', '김포', '제주', '아시아나항공', 700, TIMESTAMP('2012-09-30 12:50:00'), '국내선', 120), "
			+ "('KE1609', '김포', '울산', '대한항공', 700, TIMESTAMP('2012-10-06 12:50:00'), '국내선', 99), "
			+ "('TW711', '김포', '제주', '티웨이항공', 800, TIMESTAMP('2012-10-06 12:55:00'), '국내선', 43), "
			+ "('KE1113', '김포', '김해(부산)', '대한항공', 800, TIMESTAMP('2012-10-06 13:00:00'), '국내선', 95), "
			+ "('ZE215', '김포', '제주', '이스타항공', 536, TIMESTAMP('2012-10-06 13:00:00'), '국내선', 5), "
			+ "('LJ335', '김포', '제주', '진에어', 536, TIMESTAMP('2012-10-06 13:10:00'), '국내선', 5), "
			+ "('BX8815', '김포', '김해(부산)', '에어부산', 756, TIMESTAMP('2012-10-11 13:30:00'), '국내선', 7), "
			+ "('OZ8815', '김포', '김해(부산)', '아시아나항공', 756, TIMESTAMP('2012-10-11 13:30:00'), '국내선', 11), "
			+ "('KE1611', '김포', '울산', '대한항공', 387, TIMESTAMP('2012-10-11 13:30:00'), '국내선', 50), "
			+ "('KE1229', '김포', '제주', '대한항공', 1645, TIMESTAMP('2012-10-14 13:40:00'), '국내선', 22), "
			+ "('261', '김포', '쑹산', '중화항공', 99, TIMESTAMP('2012-10-14 13:45:00'), '국제선', 7), "
			+ "('7C113', '김포', '제주', '제주항공', 99, TIMESTAMP('2012-10-14 13:45:00'), '국내선', 120), "
			+ "('OZ8931', '김포', '제주', '아시아나항공', 100, TIMESTAMP('2012-10-15 13:55:00'), '국내선', 99), "
			+ "('KE1231', '김포', '제주', '대한항공', 101, TIMESTAMP('2012-10-16 14:00:00'), '국내선', 43), "
			+ "('KE1115', '김포', '김해(부산)', '대한항공', 102, TIMESTAMP('2012-10-17 14:00:00'), '국내선', 95)";

	public static final String STRING_DATABASE_NAME = "FREELEC";

	public static final String STRING_PROTOOL = "jdbc:derby:";

	// Server instance for testing connection
	NetworkServerControl networkServerControl = null;

	PrintWriter pw = new PrintWriter(System.out, true); // to print messages

	public JavaDBServerControl() {

	}

	public JavaDBServerControl(int port) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		try {
			networkServerControl = new NetworkServerControl(
					InetAddress.getByName("localhost"), port);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Derby Network Server 가 생성되었습니다.");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Start Derby Network acceptor
	 * 
	 * @throws Exception
	 * 
	 */
	public void start() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		networkServerControl.start(pw);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Derby Network Server 가 시작되었습니다.");

	}

	/**
	 * Shutdown the NetworkServer
	 * 
	 * @throws Exception
	 */
	public void shutdown() throws Exception {
		networkServerControl.shutdown();

	}

	/**
	 * trace utility of acceptor
	 * 
	 * @throws Exception
	 */
	public void logConnection(boolean onoff) throws Exception {
		networkServerControl.logConnections(onoff);
	}

	/**
	 * trace utility of acceptor
	 * 
	 * @throws Exception
	 */
	public void trace(boolean onoff) throws Exception {
		networkServerControl.trace(onoff);

	}

	/**
	 * Try to test for a connection Throws exception if unable to get a
	 * connection
	 * 
	 * @throws Exception
	 */
	public void testForConnection() throws Exception {

		networkServerControl.ping();

	}

	public Connection getConnection(Properties dbProperties) throws Exception {

		Connection connection = null;

		if (isDatabaseExists()) {
			System.out.println("기존 데이터베이스가 존재합니다.");
			connection = createConnection(dbProperties);
		} else {
			System.out.println("기존 데이터베이스가 존재하지 않습니다.");
			connection = checkAndCreateDatabase(dbProperties);
		}
		return connection;

	}

	private boolean isDatabaseExists() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		boolean isExists = false;
		String dbLocation = getDatabaseLocation();
		File dbFileDir = new File(dbLocation);
		if (dbFileDir.exists()) {
			isExists = true;
		} else {
			dbFileDir.mkdir();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Derby Network Server가 사용하는 시스템 디렉토리가 존재하지 않아 새롭게 생성하였습니다.");

		}
	
		return isExists;
	}

	private String getDatabaseLocation() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		String dbLocation = System.getProperty("derby.system.home") + "/"
				+ STRING_DATABASE_NAME;

		LOGGER.logp(
				Level.FINER,
				THIS_CLAZZ.getName(),
				METHOD_NAME,
				"dbLocation", dbLocation);

		return dbLocation;
	}

	// DriverManager API를 사용하여 데이터베이스 컨넥션 취득
	public Connection createConnection(Properties properties) throws Exception {

		/* load the desired JDBC driver */
		// loadJDBCDriver();
		Connection connection = null;
		/*
		 * By default, the schema APP will be used when no username is provided.
		 * Otherwise, the schema name is the same as the user name (in this case
		 * "user1" or USER1.)
		 * 
		 * Note that user authentication is off by default, meaning that any
		 * user can connect to your database using any password. To enable
		 * authentication, see the Derby Developer's Guide.
		 */

		// String dbName = "jdbcLMSDB"; // the name of the database

		/*
		 * This connection specifies create=true in the connection URL to cause
		 * the database to be created when connecting for the first time. To
		 * remove the database, remove the directory derbyDB (the same as the
		 * database name) and its contents.
		 * 
		 * The directory derbyDB will be created under the directory that the
		 * system property derby.system.home points to, or the current directory
		 * (user.dir) if derby.system.home is not set.
		 */

		String url = getDatabaseUrl();
		System.out.println("url: " + url);

		try {
			connection = DriverManager.getConnection(url, properties);
		} catch (SQLException e) {
			throw new NFDataServerException(e)
					.addContextValue("Connected to database",
							STRING_DATABASE_NAME).addContextValue("url", url)
					.addContextValue("properties", properties);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}

		// We want to control transactions manually. Autocommit is on by
		// default in JDBC.
		connection.setAutoCommit(false);

		return connection;
	}

	public String getDatabaseUrl() {
		String dbUrl = STRING_PROTOOL + STRING_DATABASE_NAME;
		return dbUrl;
	}

	private Connection checkAndCreateDatabase(Properties dbProperties) {
		boolean isTableCreated = false;
		String dbUrl = getDatabaseUrl();
		Connection connection = null;

		dbProperties.put("create", "true");
		try {
			connection = DriverManager.getConnection(dbUrl, dbProperties);
			System.out.println("Connected to and created database "
					+ STRING_DATABASE_NAME);
			isTableCreated = createTables(connection);
			if (isTableCreated) {
				insertDummyDataset(connection);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// dbProperties.remove("create");
		return connection;
	}

	private boolean createTables(Connection dbConnection) {
		boolean isTableCreated = false;
		Statement statement = null;
		try {
			statement = dbConnection.createStatement();
			// statement.execute(Constants.DERBY_ADDRESS_DAO.STR_SQL_CREATE_ADDRESS_TABLE);
			statement.execute(STR_SQL_CREATE_FLIGHTS_TABLE);
			isTableCreated = true;
		} catch (SQLException se) {
			se.printStackTrace();
			// printSQLException(se);
		}

		return isTableCreated;
	}

	private void insertDummyDataset(Connection connection) {

		PreparedStatement preStmtSaveAddress = null;

		try {
			// preStmtSaveAddress = connection.prepareStatement(
			// Constants.DERBY_ADDRESS_DAO.STR_SQL_SAVE_ADDRESS,
			// Statement.RETURN_GENERATED_KEYS);
			preStmtSaveAddress = connection
					.prepareStatement(STR_SQL_INSERT_DUMMY_DATA);

			int rowCount = preStmtSaveAddress.executeUpdate();

			/*
			 * preStmtSaveAddress.clearParameters();
			 * 
			 * preStmtSaveAddress.setString(1, "LastName");
			 * preStmtSaveAddress.setString(2, "FirstName"); ;
			 * preStmtSaveAddress.setString(3, "MiddleName");
			 * preStmtSaveAddress.setString(4, "Phone");
			 * preStmtSaveAddress.setString(5, "Email");
			 * preStmtSaveAddress.setString(6, "Address1");
			 * preStmtSaveAddress.setString(7, "Address2");
			 * preStmtSaveAddress.setString(8, "City");
			 * preStmtSaveAddress.setString(9, "State");
			 * preStmtSaveAddress.setString(10, "PostalCode");
			 * preStmtSaveAddress.setString(11, "Country");
			 * 
			 * int rowCount = preStmtSaveAddress.executeUpdate();
			 * 
			 * preStmtSaveAddress.clearParameters();
			 * 
			 * preStmtSaveAddress.setString(1, "1");
			 * preStmtSaveAddress.setString(2, "2"); ;
			 * preStmtSaveAddress.setString(3, "3");
			 * preStmtSaveAddress.setString(4, "4");
			 * preStmtSaveAddress.setString(5, "5");
			 * preStmtSaveAddress.setString(6, "6");
			 * preStmtSaveAddress.setString(7, "7");
			 * preStmtSaveAddress.setString(8, "8");
			 * preStmtSaveAddress.setString(9, "9");
			 * preStmtSaveAddress.setString(10, "10");
			 * preStmtSaveAddress.setString(11, "11");
			 * 
			 * rowCount = preStmtSaveAddress.executeUpdate();
			 */
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testConnection(Connection conn) {

		Statement stmt = null;
		ResultSet rs = null;
		try {
			// To test our connection, we will try to do a select from the
			// system catalog tables
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select count(*) from sys.systables");
			while (rs.next()) {
				System.out.println("number of rows in sys.systables = "
						+ rs.getInt(1));
			}

		} catch (SQLException sqle) {
			System.out
					.println("SQLException when querying on the database connection; "
							+ sqle);
			try {
				throw sqle;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	void waitForConnection() {
		// Server instance for testing connection
		// NetworkServerControl acceptor = null;

		// Use NetworkServerControl.ping() to wait for
		// NetworkServer to come up. We could have used
		// NetworkServerControl to start the acceptor but the property is
		// easier.
		try {
			// acceptor = new NetworkServerControl();

			networkServerControl.ping();
			networkServerControl.logConnections(true);
			System.out.println(networkServerControl.getRuntimeInfo());

			Thread.currentThread().sleep(5000);

			System.out.println("Derby Network Server now running");
			//
			// System.out.println(networkServerControl.getSysinfo());
			// System.out.println("Testing if Network Server is up and running!");

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

}
