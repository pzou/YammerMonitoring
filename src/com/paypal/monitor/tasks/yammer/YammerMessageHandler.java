package com.paypal.monitor.tasks.yammer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.paypal.monitor.elements.yammer.YammerMessage;

/*********************************
 * Name YammerMessageHandler
 * 
 * @author pzou
 * 
 */

public class YammerMessageHandler extends DefaultHandler {
	
	private static Logger logger = Logger.getLogger(YammerMessageHandler.class.getName());

	public final static int PLAIN_TEXT = 0;
	public final static int CLIENT_TYPE = 1;
	public final static int ID = 2;
	public final static int REPLIED_TO_ID = 3;
	public final static int LIKED_COUNT = 4;
	public final static int SEND_ID = 5;
	public final static int CREATED_AT = 6;

	public final static int SIZE = 7;

	private final static String END_TAG = "</";
	
	// all the element tags
	public static String[] element = new String[] { "plain", "client-type",
			"id", "replied-to-id", "count", "sender-id", "created-at" };
	
	private final static String MESSAGE_TAG = "message";

	private boolean[] isOccurred = new boolean[SIZE];

	// List of YammerMessage objects
	private List<YammerMessage> msgs = new ArrayList<YammerMessage>();
	private YammerMessage msg = null;
	private boolean isStart = true;
	//private long lastProcessedYammerId;
	private boolean isDone;
	
	
//	public YammerMessageHandler(long lastProcessedYammerId){
//		super();
//		//this.lastProcessedYammerId = lastProcessedYammerId;
//	}
//	

	public List<YammerMessage> getYammerMessages() {
		return msgs;
	}



	/**
	 * Name : startElement
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// if isDone then directly return.
		if (isDone){
			return;
		}
		if (isStart) {
			msg = new YammerMessage();
			isStart = false;
		}
		for (int i = 0; i < SIZE; i++) {
			if (qName.equalsIgnoreCase(element[i])) {
				isOccurred[i] = true;
				return;
			}
		}

	}

	/**
	 *  Name : endElement
	 *  param:  uri, localName, qName
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// if isDone then directly return.
		if (qName.equalsIgnoreCase("messages")){
			isDone = true;
			return;
		}
        // Make sure YammerMessage  got added here
		if (qName.equalsIgnoreCase(MESSAGE_TAG)) {
			if (msg != null ) {
	//			if (lastProcessedYammerId < msg.getId()){
				msgs.add(msg);
	//			}else {
	//				isDone = true;
	//			}
			}
			isStart = true;
		}
	}

	/***
	 * Name : characters 
	 * parameters : ch[] , start , length
	 */
	public void characters(char ch[], int start, int length)
			throws SAXException {
		// if isDone then directly return.
		if (isDone){
			return;
		}
		for (int i = 0; i < SIZE; i++) {
			if (isOccurred[i]) {
				isOccurred[i] = false;
				if (i == PLAIN_TEXT) {
					// Below code to overcome SAX linefeed, ] issues.
					boolean isEndTag = false;

					if (start + length + 1 <= ch.length) {
						String tmp = new String(ch, start + length, 2);
						isEndTag = tmp.equals(END_TAG) ? true : false;
					}
					if (isEndTag) {
						msg.setData(i, new String(ch, start, length));
					} else {

						String tmp = new String(ch, start, ch.length - start);
						// Find fist end tag.
						int pos = tmp.indexOf(END_TAG);
						if (pos != -1) {
							msg.setData(i, new String(ch, start, pos));
						} else {
							msg.setData(i, new String(ch, start, ch.length
									- start));
						}
					}
				} else {
					if (i == CREATED_AT){
						String str = new String(ch, start, length);
						msg.setData(i, str);					
					}else{
					msg.setData(i, new String(ch, start, length));
					}
				}
				return;
			}
		}
	}
}
