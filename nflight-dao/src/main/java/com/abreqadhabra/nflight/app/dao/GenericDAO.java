package com.abreqadhabra.nflight.app.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.dao.exception.DAORuntimeException;
import com.abreqadhabra.nflight.app.dao.util.ResultSetBeanUtil;
import com.abreqadhabra.nflight.common.exception.CommonException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.commons.util.PropertyFileUtil;

public abstract class GenericDAO {

	private static final String CLASSNAME = 
			GenericDAO.CLASSNAME; 
    private static final Logger LOGGER = LoggingHelper
    	    .getLogger(CLASSNAME);
    
	private String databaseType;
	private Properties dbProperties;
	private Connection connection;

	private static final String DB_PROPERTY_FILE_NAME = "com/abreqadhabra/nflight/app/dao/resources/config/db.properties";
	private static final String JDBC_DRIVER = ".jdbc.driver";
	private static final String JDBC_URL = ".jdbc.url";
	private static final String JDBC_USER = ".jdbc.user";
	private static final String JDBC_PASSWORD = ".jdbc.password";

	/**
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	private int maxRows;

	protected GenericDAO(String databaseType) throws DAORuntimeException {
		this.databaseType = databaseType;
		this.dbProperties = PropertyFileUtil
				.readTraditionalPropertyFile(GenericDAO.class
						.getProtectionDomain().getCodeSource().getLocation()
						.getFile()
						+ DB_PROPERTY_FILE_NAME);
		this.connection = this.getConnection();
	}

	protected synchronized Connection getConnection() {

		String jdbcDriver = getPropertyByDatabaseType(JDBC_DRIVER);
		String jdbcURL = getPropertyByDatabaseType(JDBC_URL);
		String jdbcUser = getPropertyByDatabaseType(JDBC_USER);
		String jdbcPassword = getPropertyByDatabaseType(JDBC_PASSWORD);

		try {
			Class<?> jdbcDriverClass = Class.forName(jdbcDriver);
			Driver jdbcDriverInstance = (Driver) jdbcDriverClass.newInstance();
			DriverManager.registerDriver(jdbcDriverInstance);
		} catch (SQLException | ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
		//	LOGGER.logp(Level.SEVERE, this.getClass().getSimpleName(), sourceMethod, msg, param1)
			throw new CommonException(e.getClass().getSimpleName(), e)
					.addContextValue("jdbcDriver", jdbcDriver);
		}

//		catch (Exception e) {
//			System.out.println("Failed to initialise JDBC driver");
//			e.printStackTrace();
//		}

		try {
			connection = DriverManager.getConnection(jdbcURL, jdbcUser,
					jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	protected String getPropertyByDatabaseType(String key) {
		return dbProperties.getProperty(databaseType + key);
	}

	/**
	 * Returns all rows from the table that match the specified arbitrary SQL
	 * statement
	 */

	protected void executeUpdateByDynamicQuery(String sql, String[] sqlParams) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setMaxRows(maxRows);
			// bind parameters
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				preparedStatement.setObject(i + 1, sqlParams[i]);
			}

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(connection, preparedStatement);
		}

	}

	/**
	 * Returns all rows from the table that match the specified arbitrary SQL
	 * statement
	 */

	protected <T> List<T> findByDynamicQuery(String sql, String[] sqlParams,
			Class<T> type) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<T> results = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setMaxRows(maxRows);
			// bind parameters
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				preparedStatement.setObject(i + 1, sqlParams[i]);
			}
			resultSet = preparedStatement.executeQuery();
			// fetch the results
			results = fetchMultiResults(resultSet, type);
		} catch (SQLException e) {
			e.printStackTrace();
		} /*
		 * finally { closeAll(connection, preparedStatement, resultSet); }
		 */

		return results;
	}

	/**
	 * Fetches multiple rows from the result set
	 * 
	 * @throws SQLException
	 */

	/**
	 * Sets the value of maxRows
	 */

	protected void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	/**
	 * Gets the value of maxRows
	 */
	protected int getMaxRows() {
		return this.maxRows;
	}

	/**
	 * Fetches multiple rows from the result set
	 * 
	 * @throws SQLException
	 */
	protected <T> List<T> fetchMultiResults(ResultSet resultSet, Class<T> type)
			throws SQLException {
		List<T> results = new ArrayList<T>();
		while (resultSet.next()) {
			results.add(ResultSetBeanUtil.populateDTO(resultSet, type));
		}
		return results;
	}

	protected void setAutoCommit(boolean autoCommit) {
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void rollback() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	protected void close(PreparedStatement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	protected void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	protected static void closeAll(Connection connection, Statement statement,
			ResultSet resultSet) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception exception) {
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception exception) {
			}
		}
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception exception) {
			}
		}

		return;
	}

	private void closeAll(Connection connection, Statement statement) {

		if (connection != null) {
			try {
				connection.close();
			} catch (Exception exception) {
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception exception) {
			}
		}

	}

}
