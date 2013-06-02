package com.paypal.monitor.locator;

/***************************
 * Name: DBConnectionLocator
 * By:   pzou
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.dbcp.PoolableConnection;

import com.paypal.monitor.dbconnection.DBConnectionPool;
import com.paypal.monitor.dbconnection.DBConnectionPool.DBEnv;

public class DBConnectionLocator {

	//TODO: logging
	
	private static final ConcurrentMap<String, DBConnectionPool> connectionPoolMap = new ConcurrentHashMap<String, DBConnectionPool>();

	private static DBConnectionLocator s_instance = new DBConnectionLocator();

	public static DBEnv dbEnv = null;

	private DBConnectionLocator() {
	}

	public static DBConnectionLocator getInstance() {
		return s_instance;
	}


	private static DBConnectionPool getConnectionPool(
			String dataSourceIdentifier) {
		
		DBConnectionPool connectionPool = connectionPoolMap
				.get(dataSourceIdentifier);

		if (connectionPool == null) {
			try {
				connectionPool = new DBConnectionPool(dataSourceIdentifier);
				dbEnv = connectionPool.getDbEnv();

				connectionPoolMap.put(dataSourceIdentifier,
						connectionPool);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return connectionPool;
	}

	public Connection getConnectionFromPool(String dataSourceIdentifier)
		throws Exception {

		return getConnectionPool(dataSourceIdentifier.toUpperCase())
				.getConnection();

	}

	public void releaseConnectionToPool(Connection connection,
			String dataSourceIdentifier) {
		
		getConnectionPool(dataSourceIdentifier.toUpperCase())
				.releaseConnection((PoolableConnection) connection);
	}

	 public static Connection getTmpConnection()
	 {
		 Connection conn = null;
		 try {
			 Class.forName ("oracle.jdbc.OracleDriver").newInstance ();
			 conn = DriverManager.getConnection
			 ("jdbc:oracle:thin:@unitdb.vip.qa.ebay.com:1521:unitdb", "pipeln_dev10_app", "pipeln_dev10_app");
		
			 conn.setAutoCommit(true);
		 } catch(Exception e) {
			 throw new RuntimeException("Failed to establist jdbc connection", e);
		 }
		 return conn;
	 }
}
