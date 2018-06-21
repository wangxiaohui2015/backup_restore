package com.my.br.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.my.br.util.PropertiesUtil;

public class DBHelper {

	private static final DBHelper instance = new DBHelper();

	private ComboPooledDataSource dataSource;

	private DBHelper() {
		try {
			dataSource = new ComboPooledDataSource();
			dataSource.setUser(PropertiesUtil.getInstance().getProperties(
					"db.username"));
			dataSource.setPassword(PropertiesUtil.getInstance().getProperties(
					"db.password"));
			dataSource.setJdbcUrl(PropertiesUtil.getInstance().getProperties(
					"db.url"));
			dataSource.setDriverClass(PropertiesUtil.getInstance()
					.getProperties("db.driver"));
			dataSource.setInitialPoolSize(20);
			dataSource.setMinPoolSize(10);
			dataSource.setMaxPoolSize(90);
			dataSource.setMaxStatements(100);
			dataSource.setMaxIdleTime(60);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static DBHelper getInstance() {
		return instance;
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public void closeConnection(Connection con) {
		if (null != con) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
