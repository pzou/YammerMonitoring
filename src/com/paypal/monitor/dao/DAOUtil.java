package com.paypal.monitor.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.paypal.monitor.locator.DBConnectionLocator;

public class DAOUtil {

	public static final String PPH_MONITOR_DATA_SOURCE = "PPH_MONITOR";
	public static final String PPH_MYSQL_DATA_SOURCE = "PPHMySQL";
	public static final String STAGE_SENTIMENT_MONITOR ="YAMMER_MONITOR";

	public static void closeResource(Connection conn, String dsName,
			Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}

		DBConnectionLocator.getInstance().releaseConnectionToPool(conn, dsName);

	}

	public static void releaseConnection(Connection conn,
			Statement stmt, ResultSet rs, String dataSourceName,
			boolean resetAutoCommit) {

		if (resetAutoCommit) {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
			}
		}
		closeResource(conn, dataSourceName, stmt, rs);

	}

}
