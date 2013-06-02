package com.paypal.monitor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ebay.kernel.logger.LogLevel;
import com.ebay.kernel.logger.Logger;

/**
 * Name    ResourceLoader
 * 
 * @author pzou
 *
 */
public class ResourceLoader {
	/** One kilo for numeric paramters. */
	private static final long ONEKILOBYTE = 1024;

	/** One mega for numeric paramters. */
	private static final long ONEMEGABYTE = ONEKILOBYTE * ONEKILOBYTE;

	private static Logger s_logger = LoggerUtil.getLoggerInstance(ResourceLoader.class);

	private final static String PROPERTY_FILE = "com/paypal/monitor/resources/EngService.properties";

	private static Properties properties;
	
	Properties getProperties(){
		return properties;
	}

	/**
	 * Compute factor for trailing K, M, G, or T suffix. Correspond to 1024,
	 * 1024**2, 1024**3, 1024**4
	 * 
	 * @param in
	 *            string from parameter to check for suffix.
	 * 
	 * @return the long integer value corresponding to the suffix, or zero if no
	 *         suffix matches.
	 **/
	private long suffixMatchesKMGT(final String in) {
		long result = 0;
		String s = in.toUpperCase();
		// check for K,M,G,T suffix characters
		if (s.endsWith("K")) {
			result = ONEKILOBYTE;
		} else if (s.endsWith("M")) {
			result = ONEMEGABYTE;
		} else if (s.endsWith("G")) {
			result = ONEMEGABYTE * ONEKILOBYTE;
		} else if (s.endsWith("T")) {
			result = ONEMEGABYTE * ONEMEGABYTE;
		}
		return result;
	}

	public ResourceLoader() {
		if (properties == null || properties.size() == 0) {
			try {
				/* properties = ResourceUtil.getResourceAsProperties(
						ResourceLoader.class, PROPERTY_FILE); */
				InputStream confFile = this.getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE);
				properties = new Properties();
				properties.load(confFile);
				confFile.close();
			} catch (IOException e) {
				System.out.println("In ResourceLoader Tool");
				s_logger.log(LogLevel.FATAL, "Failed to load " + PROPERTY_FILE);
				properties = new Properties();
			}
			catch (Exception e) {
				System.out.println("In ResourceLoader Tool");
				s_logger.log(LogLevel.FATAL, "Failed to load " + PROPERTY_FILE);
				properties = new Properties();
			}
		}
	}

	public ResourceLoader(String propertyFile){
		try {
			/* properties = ResourceUtil.getResourceAsProperties(
					ResourceLoader.class, propertyFile); */
			InputStream confFile = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
			properties = new Properties();
			properties.load(confFile);
			confFile.close();
		} catch (IOException e) {
			System.out.println("In ResourceLoader Tool");
			s_logger.log(LogLevel.FATAL, "Failed to load " + PROPERTY_FILE);
			properties = new Properties();
		}
		catch (Exception e) {
			System.out.println("In ResourceLoader Tool");
			s_logger.log(LogLevel.FATAL, "Failed to load " + PROPERTY_FILE);
			properties = new Properties();
		}
	}

	public String getProperty(final String key) {
		return properties.getProperty(key);
	}

	public String getProperty(final String key, final String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Get an integer value from the configuration.
	 * 
	 * @param prop
	 *            the property string
	 * @param defaultValue
	 *            the default property value to return
	 * 
	 * @return an integer value of the property
	 */
	public int getIntProperty(final String prop, final int defaultValue) {
		long result = 0;
		try {
			String s = getProperty(prop, Long.toString(defaultValue));
			if (s == null) {
				result = defaultValue;
			} else {
				long factor = suffixMatchesKMGT(s);
				if (factor == 0) {
					factor = 1;
				} else {
					// drop last character
					s = s.substring(0, s.length() - 1);
				}
				result = Integer.parseInt(s);
				result = result * factor;
			}
		} catch (Exception ee) {
			// warn and use default
			if (s_logger.isLogEnabled(LogLevel.WARN)) {
				s_logger.log(LogLevel.WARN, " error in configuration property "
						+ prop + " detected; check configuration file. Using "
						+ defaultValue);
			}
			result = defaultValue;
		}
		// TBD: check for result > maxInt first
		return (int) result;
	}

	/**
	 * Get a boolean value from the configuration.
	 * 
	 * @param prop
	 *            the property string
	 * @param defaultValue
	 *            the default property value to return
	 * 
	 * @return a boolean value of the property
	 */
	public boolean getBooleanProperty(final String prop,
			final boolean defaultValue) {
		boolean result = false;
		try {
			result = Boolean.parseBoolean(getProperty(prop, Boolean
					.toString(defaultValue)));
		} catch (Exception ee) {
			// warn and use default
			if (s_logger.isLogEnabled(LogLevel.WARN)) {
				s_logger.log(LogLevel.WARN, " error in configuration property "
						+ prop + " detected; check configuration file. Using "
						+ defaultValue);
			}
			result = defaultValue;
		}
		return result;
	}

	/**
	 * Get a long value from the configuration.
	 * 
	 * @param prop
	 *            the property string
	 * @param defaultValue
	 *            the default property value to return
	 * 
	 * @return an integer value of the property
	 */
	public long getLongProperty(final String prop, final long defaultValue) {
		long result = 0;
		try {
			String s = getProperty(prop, Long.toString(defaultValue));
			if (s == null) {
				result = defaultValue;
			} else {
				long factor = suffixMatchesKMGT(s);
				if (factor == 0) {
					factor = 1;
				} else {
					// drop last character
					s = s.substring(0, s.length() - 1);
				}
				result = Long.parseLong(s);
				result = result * factor;
			}
		} catch (Exception ee) {
			// warn and use default
			if (s_logger.isLogEnabled(LogLevel.WARN)) {
				s_logger.log(LogLevel.WARN, " error in configuration property "
						+ prop + " detected; check configuration file. Using "
						+ defaultValue);
			}
			result = defaultValue;
		}
		return result;
	}
}
