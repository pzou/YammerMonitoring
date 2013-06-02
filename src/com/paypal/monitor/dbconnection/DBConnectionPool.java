package com.paypal.monitor.dbconnection;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.StackKeyedObjectPoolFactory;

import com.paypal.monitor.utils.ResourceLoader;

/**
 * Connection Pooling using Apache Commons DBCP and Pool.
 * 
 * 
 */
public class DBConnectionPool {
	public static final String VALIDATE_QUERY = "SELECT 1 FROM Dual";
	//1 min
	public static final int VALIDATE_QUERY_TIMEOUT = 1;
	
	public enum DBEnv {
		DEV, PROD
	};
	
	private ObjectPool pool;

	private static DBEnv dbEnv;

	public DBConnectionPool(String dataSourceIdentifier) {
		try {

			/*
			 * Initialization of values from DBConnectionPool.properties file is
			 * done in helper method
			 */
			ResourceLoader resourceLoader = new ResourceLoader();

			ConnectionPoolConfigHelper configHelper = new ConnectionPoolConfigHelper(
					resourceLoader);

			// System.out.println("Got the config data as.."
			// +configHelper.getDriverName());
			Class.forName(configHelper.getDriverName(dataSourceIdentifier)).newInstance();

			/*
			 * Driver manager Connection Factory is chosen for making this
			 * program run as command line program without spending time on
			 * setting up datasource specific efforts.
			 */
			DriverManagerConnectionFactory drvMgrConnFactory = new DriverManagerConnectionFactory(
					configHelper.getConnectionURI(dataSourceIdentifier),
					configHelper.getUserName(dataSourceIdentifier),
					configHelper.getPassword(dataSourceIdentifier));
			
			drvMgrConnFactory.createConnection();

			GenericObjectPool objectPool = new GenericObjectPool(null);
			objectPool.setMaxActive(configHelper
					.getMaxActiveObjects(dataSourceIdentifier));
			objectPool.setMaxIdle(configHelper
					.getMaxIdleObjects(dataSourceIdentifier));
			/*
			 * Default policy is to block the request till the pool is available
			 * with idle objects
			 */
			objectPool.setWhenExhaustedAction(configHelper
					.getPoolExhaustedPolicy(dataSourceIdentifier));
			
			//runs every two minutes
			objectPool.setTimeBetweenEvictionRunsMillis(2*60*1000);

			// idle at most 10 minutes
			objectPool.setMinEvictableIdleTimeMillis(10*60*1000);
			
			objectPool.setTestWhileIdle(true);
			
			//Un-comment this when it is still not enough checking
			//objectPool.setTestOnBorrow(true);

			/*
			 * This pool factory to be used for pooling prepared statements, and
			 * to be used by PoolableConnectionFactory.
			 */
			StackKeyedObjectPoolFactory sKeyObjPoolFactory = new StackKeyedObjectPoolFactory(
					configHelper.getMaxIdleObjects(dataSourceIdentifier),
					configHelper.getInitIdleObjects(dataSourceIdentifier));

			/*
			 * PoolableConnectionFactory for managing all Poolable connections,
			 * those are borrowed from it.
			 */

			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(
					drvMgrConnFactory, objectPool, sKeyObjPoolFactory, null,
					true, true);
			
			//Check connection valudation query and timeout
			poolableConnFactory.setValidationQuery(VALIDATE_QUERY);
			poolableConnFactory.setValidationQueryTimeout(VALIDATE_QUERY_TIMEOUT);

			pool = poolableConnFactory.getPool();
			/* Set the DBEnv */

			if (dbEnv == null) {
				if (ConnectionPoolConfigHelper.isDevDB(configHelper
						.getUserName(dataSourceIdentifier))) {
					dbEnv = DBEnv.DEV;
				} else {
					dbEnv = DBEnv.PROD;
				}
			}
		} catch (Exception ex) {

			System.out.println("Initialization error. Msg:" + ex.getMessage());
			ex.printStackTrace();

		}
	}

	/**
	 * Method to borrow connection object from the available pool of connections
	 * 
	 * @return PoolableConnection
	 * @exception Exception simply throw DB exception up to the caller
	 */
	public PoolableConnection getConnection() throws Exception {
		//
		return (PoolableConnection)this.pool.borrowObject();
	}

	/**
	 * Method which releases the connection object to the pool of connections.
	 * 
	 * @param connectionObject
	 */
	public void releaseConnection(PoolableConnection connectionObject) {
		try {
			
			this.pool.returnObject(connectionObject);
			// System.out.println(
			// "---- Releasing connection to the Pool ----");
			// System.out.println( "No of Active Objects in the Pool :" +
			// poolableConnFactory.getPool().getNumActive());
			// System.out.println( "No of Idle Objects in the Pool :" +
			// poolableConnFactory.getPool().getNumIdle());
			// poolableConnFactory.getPool().addObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DBEnv getDbEnv() {
		return dbEnv;
	}

}
