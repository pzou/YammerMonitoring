package com.paypal.monitor.utils;

/**
 *
 *  author @ping Zou
 *  date   @12/10
 *  Name   BESConfig
 *  Purpose: read properties file
 *
 **/

import java.io.*;
import java.util.*;


public class Config 
{

	public static Properties properties_;
	private static Object lock_ = new Object();
	private boolean isFileOK = true;
	private static Config instance;


	private Config(String fileName_)
	{
		resetProperties(fileName_);
	}

	public static synchronized  Config getInstance(String fileName)
	{
		if (instance == null )
		{
			instance = new Config(fileName);
		}
		return instance;

	}

    // May add synchronized if there are any resetProperties happens
	public static Config getInstance()
	{
			return instance;
	}

	public boolean isFileExist()
	{
		return isFileOK;
	}

	public Properties resetProperties(String fileName_)
	{
		synchronized (lock_)
		{
			try
			{
                                if (fileName_== null)
                                {
                                    isFileOK = false;
                                    return null;
                                }
				loadProperties(fileName_);
			}
			catch (FileNotFoundException ex)
			{
				isFileOK = false;
				//ex.printStackTrace();
			}
			catch (IOException ex)
			{
				isFileOK = false;
				//ex.printStackTrace();
			}
		}
		return properties_;
	}

	private void loadProperties(String fileName)
		throws FileNotFoundException, IOException
	{
		FileInputStream file = new FileInputStream(fileName);
		properties_ = new Properties();
		properties_.load(file);
		file.close();
	}

	public Properties getProperties(String fileName_)
	{
		if (properties_ == null)
		{
			synchronized (lock_)
			{
				try
				{
				  if (fileName_ == null)
                                  {
                                    isFileOK = false;
                                    return null;
                                  }
				  loadProperties(fileName_);
				}
				catch (FileNotFoundException ex)
				{
					isFileOK = false;
					//ex.printStackTrace();
				}
				catch (IOException ex)
				{
					isFileOK = false;
					//ex.printStackTrace();
				}
			}
		}
		return properties_;
	}

	public static String getProperty(String xsKey, String xsDefaultValue)
	{
		return properties_.getProperty(xsKey, xsDefaultValue);
	}

	public static String getProperty(String xsKey)
	{
		return properties_.getProperty(xsKey);
	}

	public void put(String xsKey, String xsValue)
	{
		properties_.put(xsKey, xsValue);
	}


}