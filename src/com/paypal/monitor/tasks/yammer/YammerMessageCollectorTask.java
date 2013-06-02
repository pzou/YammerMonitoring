package com.paypal.monitor.tasks.yammer;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/*********************************
 * Name    YammerMessageCollectorTask
 * 
 * @author pzou
 *
 */
public class YammerMessageCollectorTask  {
	private static Logger logger = Logger.getLogger(YammerMessageCollectorTask.class.getName());
	private YammerMessageProcessor processor;
	Timer timer;
	
	
    class  DataCollectionTask extends TimerTask {

        public void run() {
        	try{
        		processor.execTask();
        	}catch(Exception e){
        		logger.warn(e.getMessage());
        	}
        }
    }
    
    
	public YammerMessageCollectorTask(int execIntervalInSeconds, YammerMessageProcessor processor) {
		this.processor = processor;
		Timer  timer = new Timer();
		timer.schedule(new DataCollectionTask(), 1, execIntervalInSeconds);
	}
	

	
	
    public static void main(String args[]) {
    	int execIntervalInSeconds = 60000;  //5 minues;
    	if (args.length>0){
    		try{
    		execIntervalInSeconds = Integer.parseInt(args[0]);
    		}catch(NumberFormatException e) {
    			
    			execIntervalInSeconds = 60000;  
    			logger.warn(e.getMessage());
    		}
    	}
        new YammerMessageCollectorTask(execIntervalInSeconds, new YammerMessageProcessor());
    }
}



