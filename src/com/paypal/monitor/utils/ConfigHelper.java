package com.paypal.monitor.utils;

/**
 * Name    ConfigHelper
 * 
 * @author pzou
 *
 */

public class ConfigHelper {
	
		
private ResourceLoader resourceLoader;
		
	public ConfigHelper (ResourceLoader resourceLoader){
			this.resourceLoader = resourceLoader;
	}
		
	public long getLongProperty(final String prop, final long defaultValue){
			return resourceLoader.getLongProperty(prop, defaultValue);
	}
	public int getIntProperty(final String prop, final int defaultValue){
			return resourceLoader.getIntProperty(prop, defaultValue);
	}
	public String getProperty(final String key, final String defaultValue) {
			return resourceLoader.getProperty(key, defaultValue);
	}
		
	public boolean getBooleanProperty(final String prop,
				final boolean defaultValue) {
			return resourceLoader.getBooleanProperty(prop, defaultValue);
	}
	public ResourceLoader getResourceLoader(){
		return resourceLoader;
	}

}
