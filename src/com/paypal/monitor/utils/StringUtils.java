package com.paypal.monitor.utils;

/******************************
 * Name    String Utils
 * Pupose: to get stage2 name
 * @author pzou
 *
 */
public class StringUtils {


	public static String[] SEPT = new String[] { " ", ",",
		";", "."};

	
	
	public static String getMatchedString(String origText, String MatchedString) {
		if (origText == null ){
			return null;
		}
		if(MatchedString == null){
			return origText;
		}

		String ret = null;
		try {
			int start = (origText.toLowerCase()).indexOf(MatchedString);
			if (start == -1) {
				return null;
			}
			
			//check if after stage 2 is letter.
			char ch  = origText.charAt(start +  MatchedString.length());
			if ( ! ( Character.isLetter(ch) && Character.isDigit(ch))) {
				return null;
			}
			
			int idx = indexOfSept(origText.substring(start));
			if (idx != -1) {
				ret = origText.substring(start, start + idx - 1);
			} else {
				
				ret = origText.substring(start);
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static int  indexOfSept(String str){
		int len = SEPT.length;
		int ret = -1;
		for (int i = 0; i < len ; i ++){
			ret = str.indexOf(SEPT[i]);
			if (ret != -1 ){
				return ret;
			}
		}
		return ret;
	}
	
	
	public static void main(String[] args) throws Exception {
		String test ="This is my note of experinece. Please enhance the docuement if you found new facts.https://confluence.paypal.com/cnfl/display/CE/Enabling+CAL+in+STAGE2+and+DEVELOPMENT";
        System.out.println(getMatchedString(test,"stage2"));
	}

}
