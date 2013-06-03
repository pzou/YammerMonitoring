package com.paypal.monitor.tasks.yammer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.paypal.monitor.dao.yammer.YammerMessageDAO;
import com.paypal.monitor.services.client.YammerRestful;

/*********************************
 * Name    SentimentMgrTask
 * 
 * @author pzou
 *
 */

public class SentimentMgrTask {
	private static Logger logger = Logger.getLogger(SentimentMgrTask.class.getName());
	private String saveDir;
	private String returnDir;
	private int days;
	private YammerMessageDAO dao;

	public SentimentMgrTask(String saveDir, String returnDir, int days) {
		this.saveDir = saveDir;
		this.returnDir = returnDir;
		dao = YammerMessageDAO.getInstance();
		this.days = days;

	}

	public void save() {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			List<String> data = dao.getYammerMsgs(days);
			File file = new File(saveDir + "StageDown"
					+ System.currentTimeMillis());
			file.createNewFile();
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			for (String str : data) {
				//replace all the linefeed
				bw.write((str.replaceAll(LINE_RETURN,"").replaceAll("\n", "").replaceAll("\r", "")));
				bw.newLine();
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally{
			if (bw!= null){
				try{
		 	      bw.close();
				}catch(IOException ee){}
			}
			if (fw!= null){
				try{
			      fw.close();
				}catch(IOException ee){}
			}
		}
	}
	
	public List<String> readFile(String fileName){
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(fileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				//no need line return from readFile
				list.add(sCurrentLine);
			}
 
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}
	
	public static void main(String[] args) throws Exception {
		SentimentMgrTask task = new SentimentMgrTask("G:\\PayPal\\Verisign\\DataMining\\Jiri\\Jeff\\In\\",
				"G:\\PayPal\\Verisign\\DataMining\\Jiri\\Jeff\\In\\History\\", 10);
	  task.save();
	}
}
