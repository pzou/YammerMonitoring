package com.paypal.monitor.elements.yammer;


import java.util.Date;

import com.paypal.monitor.tasks.yammer.YammerMessageHandler;
import com.paypal.monitor.utils.DateConvert;
import com.paypal.monitor.utils.StringUtils;

/*******************************************
 * Name :  YammerMessage
 * 
 * @author pzou
 *
 */

public class YammerMessage {

	public static final String  WEB = "Web";
	public static final String EMAIL = "Email";
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'kk:mm:ss'Z'";

	private static final String STAGE2 = "stage2";
	private static final int MAX_TEXT_SIZE = 512;
	private static final int MAX_STAGE_NAME_SIZE = 64;
	private static final long MAX_SIZE = 15;
	private String plainText;
	private int clientType;
	private long id;
	private Date createdAt;
	private long repliedToId;
	private int likedCount;
	private long sendId;
	
	private String stageName;


    public YammerMessage(){
    	
    }
    
    public YammerMessage( String plainText, int clientType, long id, Date createdAt, long repliedToId, int likedcount, long sendId){

    	this.plainText = plainText;
    	this.clientType = clientType;
    	this.id = id;
    	this.createdAt = createdAt;
    	this.repliedToId = repliedToId;
    }
    
    /***************
     * Name : SetData
     * @param i
     * @param data
     * 
     */
    public void setData(int i, String data){
    	switch(i){
    	case YammerMessageHandler.PLAIN_TEXT :
    		setPlainText(data);
    		break;
    	case YammerMessageHandler.CLIENT_TYPE :
    		setClientTypeStr(data);
    		break;
    	case YammerMessageHandler.ID :
    		setIdStr(data);
    		break;
    	case YammerMessageHandler.CREATED_AT :
    		setCreatedAtStr(data);
    		break;
    	case YammerMessageHandler.REPLIED_TO_ID :
    		setRepliedToIdStr(data);
    		break;
    	case YammerMessageHandler.LIKED_COUNT :
    		setLikedCountStr(data);
    		break;
    	case YammerMessageHandler.SEND_ID :
    		setSendIdStr(data);
    		break;
    	}
    }
    
    
    
    
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	for ( int i = 0 ; i < YammerMessageHandler.SIZE; i++ ){
    		sb.append(YammerMessageHandler.element[i]).append(" : ");
        	switch(i){
        	case YammerMessageHandler.PLAIN_TEXT :
        		sb.append(getPlainText()).append("|");
        		continue;
        	case YammerMessageHandler.CLIENT_TYPE :
        		sb.append(getClientType()).append("|");
        		continue;
        	case YammerMessageHandler.ID :
        		sb.append(getId()).append("|");
        		continue;
        	case YammerMessageHandler.CREATED_AT :
        		sb.append(getCreatedAt()).append("|");
        		continue;
        	case YammerMessageHandler.REPLIED_TO_ID :
        		sb.append(getRepliedToId()).append("|");
        		continue;
        	case YammerMessageHandler.LIKED_COUNT :
        		sb.append(getLikedCount()).append("|");
        		continue;
        	case YammerMessageHandler.SEND_ID :
        		sb.append(getSendId()).append("|");
        		continue;
        	}
    	}
    	sb.append(getStageName1()).append("|");
    	return sb.toString();

    }
	
    


	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public int getClientType() {
		return clientType;
	}

	public String getClientTypeStr(){
		if (this.clientType ==1){
			return WEB;
		}
		return EMAIL;
	}
	


	public void setClientTypeStr(String clientType) {
		if (clientType.equals(WEB)){
		    this.clientType = 1;
		}else{
			this.clientType = 2;
		}
	}
	
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void setIdStr(String id){
		if (id == null || (id.trim()).length()> MAX_SIZE){
			return;
		}
		try{
			this.id = Long.valueOf(id);
		}catch( NumberFormatException e){
			//set id -1 to indicate the problem.
			this.id = 0;
		}
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public void setCreatedAtStr(String createdAt) {
	this.createdAt = DateConvert.toDateWithFormatString(createdAt,DATE_FORMAT );
	}

	public long getRepliedToId() {
		return repliedToId;
	}

	public void setRepliedToId(long repliedToId) {
		this.repliedToId = repliedToId;
	}
	

	public void setRepliedToIdStr(String repliedToId){
		try{
			this.repliedToId = Long.valueOf(repliedToId);
		}catch( NumberFormatException e){
			//set id -1 to indicate the problem.
			this.repliedToId = 0;
		}
	}
	
	public int getLikedCount() {
		return likedCount;
	}

	public void setLikedCount(int likedCount) {
		this.likedCount = likedCount;
	}

	public void setLikedCountStr(String likedCount) {
		try{
			this.likedCount = Integer.valueOf(likedCount);
		}catch( NumberFormatException e){
			//set id -1 to indicate the problem.
			this.likedCount = 0;
		}
	}
	
	public long getSendId() {
		return sendId;
	}

	public void setSendId(long sendId) {
		this.sendId = sendId;
	}
	
	public void setSendIdStr(String sendId) {
		try{
			this.sendId = Integer.valueOf(sendId);
		}catch( NumberFormatException e){
			//set id -1 to indicate the problem.
			this.sendId = 0;
		}
	}

	
	public int getPartition(){
		return createdAt.getMonth()+1;
	}
	
	public String getStageName1(){
    	if ( plainText!= null  ){
    		try{
    			stageName = StringUtils.getMatchedString(plainText, STAGE2);
    		    return getRightSize(stageName, MAX_STAGE_NAME_SIZE);
    		}catch(IndexOutOfBoundsException  e){
    			e.printStackTrace();
    		}
    	}
    	return null;
	}
	
	public String getPlainText(){
		if (plainText == null){
			return null;
		}
    	return getRightSize(plainText,MAX_TEXT_SIZE);
	}

	private String getRightSize(String str, int size){
		if (str == null){
			return str;
		}
		if ((str.trim()).length() > size){
			return (str.trim()).substring(0, size);
		}
		return str.trim();
	}

}
