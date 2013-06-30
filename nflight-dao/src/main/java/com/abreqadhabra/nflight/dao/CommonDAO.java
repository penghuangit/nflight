package com.abreqadhabra.nflight.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.CommonException;
import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFileUtil;
import com.abreqadhabra.nflight.dao.exception.NFlightDAOException;
import com.abreqadhabra.nflight.dao.util.ResultSetBeanUtil;

public class CommonDAO {

	private static final Class<CommonDAO> THIS_CLAZZ = CommonDAO.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private String databaseMode;
	private String databaseType;
	private Properties dbProperties;
	private Connection connection;

	private static final String JDBC_DRIVER = ".jdbc.driver";
	private static final String JDBC_URL = ".jdbc.url";
	private static final String JDBC_USER = ".jdbc.user";
	private static final String JDBC_PASSWORD = ".jdbc.password";

	/**
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	private int maxRows;

	protected CommonDAO(String databaseType, String databaseMode) throws Exception {
		this.databaseType = databaseType;
		this.databaseMode = databaseMode;


		this.connection = this.getConnection();

	}

	public synchronized Connection getConnection() throws Exception {
		String jdbcDriver = getPropertyByDatabaseType(JDBC_DRIVER);
		String jdbcURL = getPropertyByDatabaseType(JDBC_URL);
		String jdbcUser = getPropertyByDatabaseType(JDBC_USER);
		String jdbcPassword = getPropertyByDatabaseType(JDBC_PASSWORD);
		try {
			Class<?> jdbcDriverClass = Class.forName(jdbcDriver);
			Driver jdbcDriverInstance = (Driver) jdbcDriverClass.newInstance();
			DriverManager.registerDriver(jdbcDriverInstance);

			connection = DriverManager.getConnection(jdbcURL, jdbcUser,
					jdbcPassword);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			throw new NFlightDAOException(e)
					.addContextValue("jdbcDriver", jdbcDriver)
					.addContextValue("jdbcURL", jdbcURL)
					.addContextValue("jdbcUser", jdbcUser)
					.addContextValue("jdbcPassword", jdbcPassword);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}

		return connection;
	}

	protected String getPropertyByDatabaseType(String key) {
		return dbProperties.getProperty(databaseType +"." + databaseMode + key);
	}

	/**
	 * Returns all rows from the table that match the specified arbitrary SQL
	 * statement
	 * 
	 * @throws SQLException
	 * @throws Exception
	 * @throws CommonException
	 */

	protected void executeUpdateByDynamicQuery(String sql, String[] sqlParams)
			throws Exception {
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
			throw new NFlightDAOException(e).addContextValue("sql", sql)
					.addContextValue("sqlParams", Arrays.toString(sqlParams));
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		} finally {
			closeAll(connection, preparedStatement);
		}
	}

	/**
	 * Returns all rows from the table that match the specified arbitrary SQL
	 * statement
	 * 
	 * @throws SQLException
	 * 
	 * @throws Exception
	 */

	protected <T> List<T> findByDynamicQuery(String sql, String[] sqlParams,
			Class<T> type) throws Exception {
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
			throw new NFlightDAOException(e).addContextValue("sql", sql)
					.addContextValue("sqlParams", Arrays.toString(sqlParams))
					.addContextValue("type", type.getCanonicalName());
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		} finally {
			closeAll(connection, preparedStatement);
		}

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
	 * 
	 * @throws Exception
	 */
	protected <T> List<T> fetchMultiResults(ResultSet resultSet, Class<T> type)
			throws Exception {
		List<T> results = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				results.add(ResultSetBeanUtil.populateDTO(resultSet, type));
			}
		} catch (SQLException e) {
			throw new NFlightDAOException(e)
					.addContextValue("results", results).addContextValue(
							"type", type.getCanonicalName());
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
		return results;
	}

	protected void setAutoCommit(boolean autoCommit) throws Exception {
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new NFlightDAOException(e).addContextValue("autoCommit",
					autoCommit);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

	protected void commit() throws Exception {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new NFlightDAOException(e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

	protected void rollback() throws Exception {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new NFlightDAOException(e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

	protected void close(Connection conn) throws Exception {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new NFlightDAOException(e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

	protected void close(PreparedStatement stmt) throws Exception {
		try {
			stmt.close();
		} catch (SQLException e) {
			throw new NFlightDAOException(e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

	protected void close(ResultSet rs) throws Exception {
		try {
			rs.close();
		} catch (SQLException e) {
			throw new NFlightDAOException(e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

	protected static void closeAll(Connection connection, Statement statement,
			ResultSet resultSet) throws Exception {
		try {
			connection.close();
			statement.close();
			resultSet.close();
		} catch (SQLException e) {
			throw new NFlightDAOException(e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

	private void closeAll(Connection connection, Statement statement)
			throws Exception {
		try {
			connection.close();
			statement.close();
		} catch (SQLException e) {
			throw new NFlightDAOException(e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}
	}

}
