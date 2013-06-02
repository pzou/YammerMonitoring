package com.paypal.monitor.services.client;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MultivaluedMap;


import com.paypal.monitor.elements.yammer.YammerAppInfo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/****************************
 * Name   YammerRestful
 * 
 * @author pzou
 *
 */


public class YammerRestful {
	
	private static Logger logger = Logger.getLogger(YammerRestful.class.getName());

   private static final String YAMMER_GROUP_MSGS_URI = "https://www.yammer.com/api/v1/messages/in_group/";
   private static final String ACCESS_TOKEN = "access_token";
   private static final String NEWER_THAN = "newer_than";

   private String yammerMsgsUrl;
   private String token ;
   
   public YammerRestful(YammerAppInfo info){
	   yammerMsgsUrl = YAMMER_GROUP_MSGS_URI + info.getGroupId();
	   token = info.getToken();
   }
   
 
   public  InputStream retrieveMessageFromGroup(long last_process_id)  {
	   try{
       ClientConfig config = new DefaultClientConfig();
       Client client = Client.create(config);
       WebResource resource = client.resource(yammerMsgsUrl);
       MultivaluedMap queryParams = new MultivaluedMapImpl();
       //queryParams.add(ACCESS_TOKEN, "P1eZn4AcsJaB1dNEBF59Jg");
       queryParams.add(ACCESS_TOKEN, token);
       // yammer always display last 20 message.
   //    queryParams.add(NEWER_THAN, last_process_id+"");
       ClientResponse response = resource.queryParams(queryParams).get(ClientResponse.class);
       return response.getEntity(InputStream.class);
	   }catch(ClientHandlerException e1){
		   logger.error(e1.getMessage());
	   }catch(UniformInterfaceException e2){
		   logger.error(e2.getMessage());
	   }catch(Exception e){
		   logger.error(e.getMessage());
	   }
	   return null;
	}
    
    public static void main(String[] args) throws Exception{
    	//Group id and access token from config table.
    	//According the yammer it is permanent access_token.
    	//param 1 --- group id,  param 2 -- token   , param 3 -- last process_id
    	YammerAppInfo info = new YammerAppInfo(1883168, "P1eZn4AcsJaB1dNEBF59Jg",298795532);
    	YammerRestful api =  new YammerRestful( info );
    	// last_process_id from high water mark table as well.  
    	//Optimization: only retrieve msg newer than last process id
    	InputStream input = api.retrieveMessageFromGroup(info.getLastProcessId()) ;
    	if (input != null ){
	    	BufferedReader in = null;
	    	try{
		    	in = new BufferedReader(new InputStreamReader(input));
		    	String inputLine;
		    	while ((inputLine = in.readLine()) != null){
		    	    System.out.println(inputLine);
		    	}
	    	}catch(IOException e){
	    		//handle IO exception here
	    	}
	    	finally{
	    	in.close();
	    	}
    	}
    }

}
