package com.paypal.monitor.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ebay.kernel.logger.LogLevel;
import com.ebay.kernel.logger.Logger;

public class LoggerUtil {
	

	
	private static String LOG_FILE_DIR = "";
	private static String LOG_FILE_NAME = LOG_FILE_DIR+"Log_"+"%u_"+(new SimpleDateFormat("MM_dd_yyyy")).format(Calendar.getInstance().getTime())+".log";	
	
	private static java.util.logging.FileHandler handler = null;

	private static LoggerUtil local = new LoggerUtil();
	
	private LoggerUtil() {
		initLogger();
	}
	
	public static Logger getLoggerInstance(Class<?> callingClass) {

		Logger logger = Logger.getInstance(callingClass);
				
		logger.addHandler(handler);
		logger.setLevel(LogLevel.ALL);
		logger.setUseParentHandlers(false);
		logger.log(LogLevel.INFO, "Logging for CLASS: "+callingClass+" started");
		
		return logger;
	}
	
	private static void initLogger()
	{
		SimpleDateFormat formatter=  new SimpleDateFormat("MM_dd_yyyy");
		if(new File("log").mkdirs())
		{
			LOG_FILE_DIR = "log/";
		}
		else if(new File("log").exists())
		{
			LOG_FILE_DIR = "log/";
		}
		LOG_FILE_NAME = LOG_FILE_DIR+"Log_"+"%u_"+formatter.format(Calendar.getInstance().getTime())+".log";
		try {
			
			handler = new java.util.logging.FileHandler(LOG_FILE_NAME,102400,10,true);
			handler.setFormatter(new java.util.logging.SimpleFormatter());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
