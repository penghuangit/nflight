package com.abreqadhabra.nflight.dao.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ResultSetBeanUtil {

	private static Map<String, String> columnToPropertyOverrides = new HashMap<String, String>();
	private static int PROPERTY_NOT_FOUND = -1;

	private static Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

	static {
		primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
		primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
		primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
		primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
		primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
		primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
		primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
	}

	public static <T> T populateDTO(ResultSet rs, Class<T> type)
			throws SQLException {

		PropertyDescriptor[] props = propertyDescriptors(type);

		ResultSetMetaData rsmd = rs.getMetaData();
		int[] columnToProperty = mapColumnsToProperties(rsmd, props);

		return createBean(rs, type, props, columnToProperty);
	}

	private static PropertyDescriptor[] propertyDescriptors(Class<?> c)
			throws SQLException {
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);

		} catch (IntrospectionException e) {
			throw new SQLException("Bean introspection failed: "
					+ e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}

	protected static int[] mapColumnsToProperties(ResultSetMetaData rsmd,
			PropertyDescriptor[] props) throws SQLException {

		int cols = rsmd.getColumnCount();
		int[] columnToProperty = new int[cols + 1];
		Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

		for (int col = 1; col <= cols; col++) {
			String columnName = rsmd.getColumnLabel(col);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(col);
			}
			String propertyName = columnToPropertyOverrides.get(columnName);
			if (propertyName == null) {
				propertyName = columnName.replace("_", "");
			}
			for (int i = 0; i < props.length; i++) {
				if (propertyName.equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;
				}
			}
		}

		return columnToProperty;
	}

	private static <T> T createBean(ResultSet rs, Class<T> type,
			PropertyDescriptor[] props, int[] columnToProperty)
			throws SQLException {

		T bean = newInstance(type);

		for (int i = 1; i < columnToProperty.length; i++) {

			if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
				continue;
			}

			PropertyDescriptor prop = props[columnToProperty[i]];
			Class<?> propType = prop.getPropertyType();

			Object value = processColumn(rs, i, propType);

			if (propType != null && value == null && propType.isPrimitive()) {
				value = primitiveDefaults.get(propType);
			}

			callSetter(bean, prop, value);
		}

		return bean;
	}

	protected static <T> T newInstance(Class<T> c) throws SQLException {
		try {
			return c.newInstance();

		} catch (InstantiationException e) {
			throw new SQLException("Cannot create " + c.getName() + ": "
					+ e.getMessage());

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot create " + c.getName() + ": "
					+ e.getMessage());
		}
	}

	protected static Object processColumn(ResultSet rs, int index,
			Class<?> propType) throws SQLException {

		if (!propType.isPrimitive() && rs.getObject(index) == null) {
			return null;
		}

		if (propType.equals(String.class)) {
			return rs.getString(index);

		} else if (propType.equals(Integer.TYPE)
				|| propType.equals(Integer.class)) {
			return Integer.valueOf(rs.getInt(index));

		} else if (propType.equals(Boolean.TYPE)
				|| propType.equals(Boolean.class)) {
			return Boolean.valueOf(rs.getBoolean(index));

		} else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
			return Long.valueOf(rs.getLong(index));

		} else if (propType.equals(Double.TYPE)
				|| propType.equals(Double.class)) {
			return Double.valueOf(rs.getDouble(index));

		} else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
			return Float.valueOf(rs.getFloat(index));

		} else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
			return Short.valueOf(rs.getShort(index));

		} else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
			return Byte.valueOf(rs.getByte(index));

		} else if (propType.equals(Timestamp.class)) {
			return rs.getTimestamp(index);

		} else if (propType.equals(SQLXML.class)) {
			return rs.getSQLXML(index);

		} else {
			return rs.getObject(index);
		}

	}

	private static void callSetter(Object target, PropertyDescriptor prop,
			Object value) throws SQLException {

		Method setter = prop.getWriteMethod();

		if (setter == null) {
			return;
		}

		Class<?>[] params = setter.getParameterTypes();
		try {
			// convert types for some popular ones
			if (value instanceof java.util.Date) {
				String targetType = params[0].getName();
				if ("java.sql.Date".equals(targetType)) {
					value = new java.sql.Date(
							((java.util.Date) value).getTime());
				} else if ("java.sql.Time".equals(targetType)) {
					value = new java.sql.Time(
							((java.util.Date) value).getTime());
				} else if ("java.sql.Timestamp".equals(targetType)) {
					value = new java.sql.Timestamp(
							((java.util.Date) value).getTime());
				}
			}

			// Don't call setter if the value object isn't the right type
			if (isCompatibleType(value, params[0])) {
				setter.invoke(target, new Object[] { value });
			} else {
				throw new SQLException("Cannot set " + prop.getName()
						+ ": incompatible types, cannot convert "
						+ value.getClass().getName() + " to "
						+ params[0].getName());
				// value cannot be null here because isCompatibleType allows
				// null
			}

		} catch (IllegalArgumentException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": "
					+ e.getMessage());

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": "
					+ e.getMessage());

		} catch (InvocationTargetException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": "
					+ e.getMessage());
		}
	}

	private static boolean isCompatibleType(Object value, Class<?> type) {
		// Do object check first, then primitives
		if (value == null || type.isInstance(value)) {
			return true;

		} else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
			return true;

		} else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
			return true;

		} else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
			return true;

		} else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
			return true;

		} else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
			return true;

		} else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
			return true;

		} else if (type.equals(Character.TYPE)
				&& Character.class.isInstance(value)) {
			return true;

		} else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
			return true;

		}
		return false;

	}

}
