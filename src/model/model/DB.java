package model.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	static Connection conn = null;;

	public static Connection getConnection() {
		try {
			if (conn == null) {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			}
			return conn;
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		}
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		}
	}

	public static void closeStatement(Statement st) {
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		}
	}

	private static Properties loadProperties() {
		try (FileInputStream sf = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(sf);
			return props;
		} catch (IOException e) {
			throw new DBException("Error: " + e.getMessage());
		}
	}
}
