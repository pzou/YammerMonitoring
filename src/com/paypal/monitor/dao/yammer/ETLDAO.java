package com.paypal.monitor.dao.yammer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;


import com.paypal.monitor.dao.DAOUtil;
import com.paypal.monitor.elements.yammer.YammerAppInfo;
import com.paypal.monitor.elements.yammer.YammerMessage;
import com.paypal.monitor.locator.DBConnectionLocator;

/**
 * Name    ETLDAO
 * 
 * @author pzou
 *
 */


public class ETLDAO {
	
	  private static Logger logger = Logger.getLogger(ETLDAO.class.getName());
	
	  private final static String GET_LAST_PROCESS_ID =
			  "select  b.group_id, b.token , a.last_process_id from etl_process a , yammer_group b where a.group_id = b.group_id and b.name=?"; 

	  private final static String UPDATE_LAST_PROCESS_ID = "update etl_process set last_process_id=?, last_process_time=sysdate where group_id in (select group_id from yammer_group where name=?) ";
	  
	  private static ETLDAO instance = new ETLDAO();
	  
	  private ETLDAO(){
		  
	  }
	  public static ETLDAO getInstance(){
		  return instance;
	  }
	  
	  public  YammerAppInfo  getYammerAppInfo(String appName) {

			PreparedStatement stmt = null;
			ResultSet rs = null;
			Connection conn = null;
			YammerAppInfo info = null;
			try {
				conn = DBConnectionLocator.getInstance().getConnectionFromPool(DAOUtil.STAGE_SENTIMENT_MONITOR);
				stmt = conn.prepareStatement(GET_LAST_PROCESS_ID);		
				stmt.setString(1, appName);
				rs = stmt.executeQuery();
				if (rs.next()){
					info = new YammerAppInfo(rs.getLong(1), rs.getString(2), rs.getLong(3) );
					System.out.println(info.getGroupId() +"  "+ info.getLastProcessId());
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			} catch(Exception ee){
				logger.error(ee.getMessage());
			}finally {
				DAOUtil.releaseConnection(conn, stmt, null, DAOUtil.STAGE_SENTIMENT_MONITOR,false);
			}
			return info;
		}
		
		
		public  void recordLastProcessId(String appName, long lastProcessId){
	
				PreparedStatement stmt = null;
				Connection conn = null;
				try {
					conn = DBConnectionLocator.getInstance().getConnectionFromPool(DAOUtil.STAGE_SENTIMENT_MONITOR);
					stmt = conn.prepareStatement(UPDATE_LAST_PROCESS_ID);	
					stmt.setLong(1,  lastProcessId);
					stmt.setString(2, appName);
					stmt.executeUpdate();
				    conn.commit();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				} catch(Exception ee){
					logger.error(ee.getMessage());
				}finally {
					DAOUtil.releaseConnection(conn, stmt, null, DAOUtil.STAGE_SENTIMENT_MONITOR,false);
				}
                
		  
		}
		
}
