package examples.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbcExample {

	private static String dbURL = "jdbc:derby://localhost:1527/JavaDB/nflightDB;create=true;user=freelec;password=password1!";
	private static String tableName = "FREELEC.AIRLINE";
	// JDBC Connection
	private static Connection conn = null;
	private static Statement stmt = null;

	public static void main(String[] args) {
		createConnection();
		insertAirline("KE", "대한항공");
		selectAaddreses();
		shutdown();
	}

	private static void createConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			// Get a connection
			conn = DriverManager.getConnection(dbURL);
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
	 
	private static void insertAirline(String airlineCode, String airlineName) {
		try {
			stmt = conn.createStatement();
			stmt.execute("INSERT INTO " + tableName + " VALUES ('"
					+ airlineCode + "','" + airlineName + "')");
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	private static void selectAaddreses() {
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from " + tableName);
			ResultSetMetaData rsmd = results.getMetaData();
			int numberCols = rsmd.getColumnCount();
			for (int i = 1; i <= numberCols; i++) {
				// print Column Names
				System.out.print(rsmd.getColumnLabel(i) + "\t\t");
			}

			System.out.println("\n-------------------------------------");

			while (results.next()) {
				String airlineCode = results.getString(1);
				String airlineName = results.getString(2);

				System.out.println(airlineCode + "'\t\t'" + airlineName);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	private static void shutdown() {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}
		} catch (SQLException sqlExcept) {

		}

	}
}
