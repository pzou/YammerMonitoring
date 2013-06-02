package com.paypal.monitor.tasks.yammer;


import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import com.paypal.monitor.dao.yammer.ETLDAO;
import com.paypal.monitor.dao.yammer.YammerMessageDAO;
import com.paypal.monitor.elements.yammer.YammerMessage;
import com.paypal.monitor.elements.yammer.YammerAppInfo;
import com.paypal.monitor.services.client.YammerRestful;

/***********************************
 * Name YammerMessageProcessor
 * 
 * @author pzou
 * 
 */

public class YammerMessageProcessor {
	private static Logger logger = Logger.getLogger(YammerMessageProcessor.class.getName());
	
	private static final String APP_NAME = "PayPal Stage Down";

	/**
	 * Name : process
	 * 
	 * @param str
	 * @param lastProcessMessageId
	 *            -- yammerId got from highwatermark table
	 */
	// public static void processStr(String xml) {
	// try {
	// SAXParserFactory factory = SAXParserFactory.newInstance();
	// SAXParser saxParser = factory.newSAXParser();
	// YammerMessageHandler handler = new YammerMessageHandler();
	// saxParser.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")),
	// handler);
	// List<YammerMessage> msgs =handler.getYammerMessages();
	// //insert msg to db
	// YammerMessageDAO.insertMessages(msgs);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private long process(InputStream xml) {

		long maxProcessId = 0l;
		if (xml == null){
			return maxProcessId;
		}
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			YammerMessageHandler handler = new YammerMessageHandler();
			saxParser.parse(xml, handler);
			List<YammerMessage> msgs = handler.getYammerMessages();
			// insert msg to db
			maxProcessId = YammerMessageDAO.getInstance().insertMessages(msgs);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return maxProcessId;

	}

	public void execTask() {
		// Group id and access token from config table.
		// According the yammer it is permanent access_token.
		YammerAppInfo info = ETLDAO.getInstance().getYammerAppInfo(APP_NAME );
		YammerRestful api = new YammerRestful(info);
		// last_process_id from high water mark table as well.

		long maxId = process(api.retrieveMessageFromGroup(info
				.getLastProcessId()));
		if (maxId > 0) {
			ETLDAO.getInstance().recordLastProcessId(APP_NAME , maxId);
		}
	}

	public static void main(String[] args) throws Exception {
		YammerMessageProcessor processor = new YammerMessageProcessor();
		processor.execTask();
	}

}
