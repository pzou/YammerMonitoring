package com.paypal.monitor.dao.yammer;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


import com.paypal.monitor.dao.DAOUtil;
import com.paypal.monitor.elements.yammer.YammerAppInfo;
import com.paypal.monitor.elements.yammer.YammerMessage;
import com.paypal.monitor.locator.DBConnectionLocator;
import com.paypal.monitor.tasks.yammer.YammerMessageProcessor;


/****************************
 * Name    YammerMessaeDAO
 * 
 * @author pzou
 *
 */

public class YammerMessageDAO {
	private static Logger logger = Logger.getLogger(YammerMessageDAO.class.getName());
	
  private final static String INSERT = 
		  "insert into YAMMER_MSG ( MSG_ID, PARTITION_KEY, STAGE_NAME, TEXT, CLIENT_TYPE, CREATED_TIME, PARENT_MSG_ID, SEND_ID, LIKED_COUNT) values (?,?,?,?,?,?,?,?,?)";
  //private final static String GET_TODAY_MESSAGES = "select MSG_ID, TEXT from YAMMER_MSG where TO_DATE(creation_time,'YYYY-MM-DD')= ?";
  
  private final static int COMMIT_SIZE = 100;
  
  private final static String SELECT_ID_TEXT = "select MSG_ID,TEXT from yammer_msg where created_time between  sysdate- ?  and sysdate";  

  private static YammerMessageDAO instance = new YammerMessageDAO();
  
  private YammerMessageDAO(){
  }
  
  public static YammerMessageDAO getInstance(){
	  return instance;
  }
  
  public  List<String> getYammerMsgs(int days){
	  
	List<String> list = new ArrayList<String>();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	Connection conn = null;
	try {
		conn = DBConnectionLocator.getInstance().getConnectionFromPool(DAOUtil.STAGE_SENTIMENT_MONITOR );
		stmt = conn.prepareStatement(SELECT_ID_TEXT);		
		stmt.setInt(1, days);
		rs = stmt.executeQuery();
		while (rs.next()){
			list.add(rs.getLong(1)+"|"+rs.getString(2));
		}
	} catch (SQLException e) {
		logger.error(e.getMessage());
	} catch(Exception ee){
		logger.error(ee.getMessage());
	}finally {
		DAOUtil.releaseConnection(conn, stmt, null, DAOUtil.STAGE_SENTIMENT_MONITOR,false);
	}
	return list;
	  
  }
  
  
  public  long insertMessages(List<YammerMessage> msgs) {

		PreparedStatement stmt = null;
		Connection conn = null;
		int count = 0;
		long minFailedId = 0;
		long maxId = 0;
		try {
			conn = DBConnectionLocator.getInstance().getConnectionFromPool(DAOUtil.STAGE_SENTIMENT_MONITOR);
			stmt = conn.prepareStatement(INSERT);			
		    for (YammerMessage msg: msgs){
		    	try{
		    	stmt.setLong(1, msg.getId());
		    	stmt.setInt(2, msg.getPartition());
		    	if (msg.getStageName1()== null){
		    		stmt.setNull(3, java.sql.Types.VARCHAR);
		    	}else {
		    		stmt.setString(3, (msg.getStageName1()));
		    	}
		    	if (msg.getPlainText() == null){
		    		stmt.setNull(4, java.sql.Types.VARCHAR);
		    	}else {
		    		stmt.setString(4, msg.getPlainText());
		    	}
		    	stmt.setInt(5, msg.getClientType());
		    	stmt.setTimestamp(6, new Timestamp(msg.getCreatedAt().getTime()));
		    	if (msg.getRepliedToId() == 0L){
		    	  stmt.setNull(7, java.sql.Types.BIGINT);
		    	}else{
		    	  stmt.setLong(7, msg.getRepliedToId());
		    	}
		    	if (msg.getSendId() == 0L){
			    	  stmt.setNull(8, java.sql.Types.BIGINT);
			    	}else{
			    	  stmt.setLong(8, msg.getSendId());
			    	}		    	
		    	if (msg.getLikedCount()== 0){
			    	  stmt.setNull(9, java.sql.Types.BIGINT);
			    	}else{
			    	  stmt.setLong(9, msg.getLikedCount());
			    	}	
		    	
		    	stmt.executeUpdate();
			    if (count == COMMIT_SIZE){
				    conn.commit();
				    count = 0;
			    }
			    if (maxId < msg.getId() ){
			    	maxId = msg.getId();
			    }
		    	}catch(Exception e){
		    		if (e.getMessage().contains("ORA-00001")){
		    			continue;
		    		}
		    		if (minFailedId > msg.getId()  || minFailedId == 0){
		    			minFailedId = msg.getId();
		    		}
		    		e.printStackTrace();
		    	}
		    }
		    if (count > 0){
		    	conn.commit();
		    }
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			DAOUtil.releaseConnection(conn, stmt, null, DAOUtil.STAGE_SENTIMENT_MONITOR,false);
		}
		if (maxId == 0){
			return maxId;
		}
		if (minFailedId != 0){
			minFailedId --;
			return minFailedId;
		}
		return maxId;

	}
  
}
