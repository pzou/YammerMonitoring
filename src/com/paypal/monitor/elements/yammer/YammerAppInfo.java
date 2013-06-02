package com.paypal.monitor.elements.yammer;

/*****************************
 * Name    YammerAppInfo
 * Purpose: Class to store yammer app
 *         group info to retrieve the msgs
 * @author pzou
 *
 */

public class YammerAppInfo {

	private long lastProcessId;
	private String token;
	private long groupId;
	
	public YammerAppInfo(long groupId, String token, long lastProcessId){
		this.groupId = groupId;
		this.token = token;
		this.lastProcessId = lastProcessId;
	}
	
	public YammerAppInfo(){
		
	}

	public long getLastProcessId() {
		return lastProcessId;
	}

	public void setLastProcessId(long lastProcessId) {
		this.lastProcessId = lastProcessId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	
	
}
