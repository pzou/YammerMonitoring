package com.paypal.monitor.dbconnection;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.paypal.monitor.utils.ConfigHelper;
import com.paypal.monitor.utils.ResourceLoader;


public class ConnectionPoolConfigHelper extends ConfigHelper{
	
	private static String DRIVER_NAME = "driverName";
	private static String DEFAULT_DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private static String CONNECTION_URI = "connectionUri";
	private static String DEFAULT_CONNECTION_URI = "jdbc:oracle:thin:@unitdb.vip.qa.ebay.com:1521:unitdb";
	private static String USER_NAME = "userName";
	private static String PASSWORD = "password";
	private static String DEFAULT_USER_NAME = "pipeln_dev1_app";
	private static String DEFAULT_PASSWORD = "pipeln_dev1_app";
	private static String MAX_IDLE_OBJECTS = "maxIdleObjects";
	private static int DEFAULT_MAX_IDLE_OBJECTS = 10;
	private static String INIT_IDLE_OBJECTS = "initIdleObjects";
	private static int DEFAULT_INIT_IDLE_OBJECTS = 10;
	private static String MAX_ACTIVE_OBJECTS = "maxActiveObjects";
	private static int DEFAULT_MAX_ACTIVE_OBJECTS = 10;
	private static String WHEN_EXHAUSTED = "whenExhausted";
	private static byte DEFAULT_WHEN_EXHAUSTED = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
	private static String DATASOURCE_NAME = "ApplicationDataSourceName";
	private static String PPH_MONITOR_DATASOURCE_NAME="DataSourceNamePPH_MONITOR";
	private static String PPH_MONITOR_MYSQL_DATASOURCE_NAME = "DataSourceNamePPHMYSQL";
	
	public ConnectionPoolConfigHelper(ResourceLoader resourceLoader) {
		super(resourceLoader);
		
	}
	
	public String getDriverName(String dataSourceIdentifier)
	{
		//return "oracle.jdbc.driver.OracleDriver";
		return getResourceLoader().getProperty(DRIVER_NAME+dataSourceIdentifier, DEFAULT_DRIVER_NAME);
	}
	
	public String getConnectionURI(String dataSourceIdentifier)
	{
		return getResourceLoader().getProperty(CONNECTION_URI+dataSourceIdentifier,DEFAULT_CONNECTION_URI);
	}
	
	public String getUserName(String dataSourceIdentifier)
	{
		return getResourceLoader().getProperty(USER_NAME+dataSourceIdentifier, DEFAULT_USER_NAME);
	}
	
	public String getPassword(String dataSourceIdentifier)
	{
		return getResourceLoader().getProperty(PASSWORD+dataSourceIdentifier, DEFAULT_PASSWORD);
	}
	
	public int getInitIdleObjects(String dataSourceIdentifier)
	{
		return getResourceLoader().getIntProperty(INIT_IDLE_OBJECTS+dataSourceIdentifier, DEFAULT_INIT_IDLE_OBJECTS);
	}
	
	public int getMaxIdleObjects(String dataSourceIdentifier)
	{
		return getResourceLoader().getIntProperty(MAX_IDLE_OBJECTS+dataSourceIdentifier, DEFAULT_MAX_IDLE_OBJECTS);
	}
	
	public int getMaxActiveObjects(String dataSourceIdentifier)
	{
		return getResourceLoader().getIntProperty(MAX_ACTIVE_OBJECTS+dataSourceIdentifier, DEFAULT_MAX_ACTIVE_OBJECTS);
	}
	
	public byte getPoolExhaustedPolicy(String dataSourceIdentifier)
	{
		String policy = getResourceLoader().getProperty(WHEN_EXHAUSTED+dataSourceIdentifier);
		byte whenExhaustedAction = 0;
		if(policy.equalsIgnoreCase("grow"))
		{
			whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
		}
		else if(policy.equalsIgnoreCase("block"))
		{
			whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		}
		else if(policy.equalsIgnoreCase("fail"))
		{
			whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
		}
		else
		{
			whenExhaustedAction = DEFAULT_WHEN_EXHAUSTED;
		}
		return whenExhaustedAction;
	}
	
	public String getDataSourceName()
	{
		return getResourceLoader().getProperty(DATASOURCE_NAME);
	}
	
	public String getPPHDataSourceName()
	{
		return getResourceLoader().getProperty(PPH_MONITOR_DATASOURCE_NAME);
	}
	
	public String getPPHMySQLDataSourceName()
	{
		return getResourceLoader().getProperty(PPH_MONITOR_MYSQL_DATASOURCE_NAME);
	}
	
	
	public static boolean isDevDB(String userName) {
		if (userName.equals(DEFAULT_USER_NAME)) 
			return true;
		else
			return false;
	}
}
