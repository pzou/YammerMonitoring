package com.paypal.monitor.elements.yammer;

/*******************************************
 * Name :  YammerLightMessage
 * 
 * @author pzou
 *
 */
public class YammerLightMessage {
	private String plainText;
	private long id;
	public YammerLightMessage(String plainText, long id ){
		this.plainText = plainText;
		this.id = id;
	}
	
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	
	
}
