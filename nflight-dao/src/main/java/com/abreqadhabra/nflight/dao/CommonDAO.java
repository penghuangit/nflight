package com.abreqadhabra.nflight.dao;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFUnexpectedException;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.dao.exception.NFDAOException;
import com.abreqadhabra.nflight.dao.util.ResultSetBeanUtil;

public class CommonDAO {

	private static Class<CommonDAO> THIS_CLAZZ = CommonDAO.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private String databaseType;
	private Properties dbProperties;
	private Connection connection;

	private static String JDBC_DRIVER = ".jdbc.driver";
	private static String JDBC_URL = ".jdbc.url";
	private static String JDBC_USER = ".jdbc.user";
	private static String JDBC_PASSWORD = ".jdbc.password";

	/**
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	private int maxRows;

	protected CommonDAO(String databaseType) throws Exception {
		this.databaseType = databaseType;
		 Path CODE_BASE_PATH = IOStream
					.getCodebasePath(THIS_CLAZZ.getName());
		dbProperties = PropertyFile
				.readPropertyFilePath(CODE_BASE_PATH.resolve("com/abreqadhabra/nflight/dao/conf/db.properties"));
		this.connection = this.getConnection();

	}

	public synchronized Connection getConnection() throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

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
			
			DatabaseMetaData dbmd = connection.getMetaData() ;

			StringBuffer sb = new StringBuffer();
			sb.append("----------------------------------------------------") ;
			sb.append("\nDatabase Name    = " + dbmd.getDatabaseProductName()) ;
			sb.append("\nDatabase Version = " + dbmd.getDatabaseProductVersion()) ;
			sb.append("\nDriver Name      = " + dbmd.getDriverName()) ;
			sb.append("\nDriver Version   = " + dbmd.getDriverVersion()) ;
			sb.append("\nDatabase URL     = " + dbmd.getURL()) ;
			sb.append("\n----------------------------------------------------") ;

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					sb.toString());
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			throw new NFDAOException(e)
					.addContextValue("jdbcDriver", jdbcDriver)
					.addContextValue("jdbcURL", jdbcURL)
					.addContextValue("jdbcUser", jdbcUser)
					.addContextValue("jdbcPassword", jdbcPassword);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}

		return connection;
	}

	protected String getPropertyByDatabaseType(String key) {
		return dbProperties.getProperty(databaseType + key);
	}

	/**
	 * Returns all rows from the table that match the specified arbitrary SQL
	 * statement
	 * 
	 * @throws SQLException
	 * @throws Exception
	 * @throws WrapperException
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
			throw new NFDAOException(e).addContextValue("sql", sql)
					.addContextValue("sqlParams", Arrays.toString(sqlParams));
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
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
			throw new NFDAOException(e).addContextValue("sql", sql)
					.addContextValue("sqlParams", Arrays.toString(sqlParams))
					.addContextValue("type", type.getCanonicalName());
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
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
			throw new NFDAOException(e)
					.addContextValue("results", results).addContextValue(
							"type", type.getCanonicalName());
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
		return results;
	}

	protected void setAutoCommit(boolean autoCommit) throws Exception {
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new NFDAOException(e).addContextValue("autoCommit",
					autoCommit);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

	protected void commit() throws Exception {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new NFDAOException(e);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

	protected void rollback() throws Exception {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new NFDAOException(e);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

	protected void close(Connection conn) throws Exception {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new NFDAOException(e);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

	protected void close(PreparedStatement stmt) throws Exception {
		try {
			stmt.close();
		} catch (SQLException e) {
			throw new NFDAOException(e);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

	protected void close(ResultSet rs) throws Exception {
		try {
			rs.close();
		} catch (SQLException e) {
			throw new NFDAOException(e);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

	protected static void closeAll(Connection connection, Statement statement,
			ResultSet resultSet) throws Exception {
		try {
			connection.close();
			statement.close();
			resultSet.close();
		} catch (SQLException e) {
			throw new NFDAOException(e);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

	private void closeAll(Connection connection, Statement statement)
			throws Exception {
		try {
			connection.close();
			statement.close();
		} catch (SQLException e) {
			throw new NFDAOException(e);
		} catch (Exception e) {
			throw new NFUnexpectedException(e);
		}
	}

}
