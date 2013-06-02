package com.paypal.monitor.utils;

/***
 *  @pzou
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConvert {
	public static final int YEAR_MOD = 10000;
	public static final int MONTH_MOD = 100;

	public static String getCALLogFormatDaily(Calendar calendar) {
		int mon = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String sDay = day < 10 ? "0" + day : day + "";
		String sMon = mon < 10 ? "0" + mon : mon + "";

		return calendar.get(Calendar.YEAR) + "/" + sMon + "-"
				+ getMonthString(calendar) + "/" + sDay;
	}

	public static String getCALLogFormatHourly(Calendar calendar) {
		return getCALLogFormatHourly(calendar, false);
	}

	
	public static String getCALLogFormatHourly(Calendar calendar, boolean needMinutes) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		String sHour = hour < 10 ? "0" + hour + ":00:00" : hour + ":00:00";
		if (needMinutes){
			return getCALLogFormatDaily(calendar) + "/" + sHour+"/"+calendar.get(Calendar.MINUTE);
		}
		return getCALLogFormatDaily(calendar) + "/" + sHour;
	}
	
	public static String getCALLogFormat(Calendar calendar, boolean isHourly) {
		return getCALLogFormat(calendar, isHourly, false);
	}

	public static String getCALLogFormat(Calendar calendar, boolean isHourly, boolean needMinutes) {
		if(needMinutes){
			return getCALLogFormatHourly(calendar, true);
		}
		if (isHourly) {
			return getCALLogFormatHourly(calendar);
		}
		//This is daily
		return getCALLogFormatDaily(calendar);
	}
	
	public static int convertDateToInt(Calendar calendar) {
		if (calendar == null) {
			return 0;
		}

		int ret = calendar.get(Calendar.YEAR) * 10000
				+ (calendar.get(Calendar.MONTH) + 1) * 100
				+ calendar.get(Calendar.DAY_OF_MONTH);
		return ret;
	}

	public static int convertDateHourToInt(Calendar calendar) {
		if (calendar == null) {
			return 0;
		}

		int ret = convertDateToInt(calendar) * 100
				+ calendar.get(Calendar.HOUR_OF_DAY);
		return ret;
	}

	public static Date convertCalendarToDate(Calendar calendar) {
		if (calendar == null) {
			return null;
		}

		Date ret = calendar.getTime();
		return ret;
	}

	public static String convertDateTimeToString(Calendar calendar) {
		return convertDateToString(calendar) + " 00:00:00";
	}

	public static String convertDateToString(Calendar calendar, String separator) {
		if (calendar == null) {
			return "";
		}
		int mon = calendar.get(Calendar.MONTH) + 1;
		int d = calendar.get(Calendar.DAY_OF_MONTH);
		
		String day = d > 9 ? d + "" : "0" + d;
		String month = mon > 9 ? mon + "" : "0" + mon;
		
		String ret = calendar.get(Calendar.YEAR) + separator + month + separator
				+ day;
		return ret;
	}
	
	public static String convertDateToTimeString(Calendar calendar, String separator) {
		return convertDateToString(calendar, separator) 
			+ " " + calendar.get(Calendar.HOUR_OF_DAY)
			+ ":" + calendar.get(Calendar.MINUTE)
			+ ":" + calendar.get(Calendar.SECOND);
	}

	public static String convertDateToString(Calendar calendar) {
		return convertDateToString(calendar, "-");
	}

	public static String getMonthString(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		int mon = calendar.get(Calendar.MONTH);
		switch (mon) {
		case 0:
			return "January";
		case 1:
			return "February";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "December";

		}
		return "";
	}

	public static Calendar getNextDay(Calendar ca) {
		ca.add(Calendar.DAY_OF_MONTH, 1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca;
	}

	public static Calendar getNextHour(Calendar ca) {
		ca.add(Calendar.HOUR_OF_DAY, 1);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca;
	}

	public static Calendar getToday(boolean hasHour) {
		Calendar calendar = Calendar.getInstance();
		if (!hasHour) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		}
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

	public static Date convertFromString(String time, boolean hasHour) {
		Calendar calendar = Calendar.getInstance();
		
		try {
			calendar.set(Calendar.YEAR, Integer.parseInt(time.substring(0, 4)));
			calendar.set(Calendar.MONTH,
					Integer.parseInt(time.substring(4, 6)) - 1);
			calendar.set(Calendar.DAY_OF_MONTH,
					Integer.parseInt(time.substring(6, 8)));
			
			if (hasHour && time.length() == 10) {
				calendar.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(time.substring(8, 10)));
			} else {
				calendar.set(Calendar.HOUR_OF_DAY, 0);
			}
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return calendar.getTime();
	}

	public static java.sql.Date convertFromString(String time, int hour) {
		Calendar result = convertFromStringToCal(time, hour);
		
		return result == null ? null : 
			new java.sql.Date(result.getTimeInMillis());
	}
	
	public static Calendar convertFromStringToCal(String time, int hour) {
		Calendar calendar = Calendar.getInstance();
		
		try {
			calendar.set(Calendar.YEAR, Integer.parseInt(time.substring(0, 4)));
			calendar.set(Calendar.MONTH,
					Integer.parseInt(time.substring(5, 7)) - 1);
			calendar.set(Calendar.DAY_OF_MONTH,
					Integer.parseInt(time.substring(8, 10)));
			
			calendar.set(Calendar.HOUR_OF_DAY, hour);

			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static final ThreadLocal<SimpleDateFormat> dateformatter = new ThreadLocal<SimpleDateFormat>(){
		
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy HH");
			
		};
	};
	
	public static final Date getDate(final String arg0){
		SimpleDateFormat format = dateformatter.get();
		try {
			return format.parse(arg0);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	
	public static final Calendar getCalendarForDate(Date dt){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		return calendar;
	}
	
	public static final Calendar getCalendarForMillis(Long arg0){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(arg0);
		return calendar;
	}
	
	
	public static Date toDateWithFormatString(String date, String format) {
	    if (date == null)
	      return null;

	    try {
	      return new SimpleDateFormat(format).parse(date);
	    } catch (ParseException e) {
	       System.err.print("Unable to parse date '" + date +"' using format string '" + format );
	    }
	    return new Date();
	  }
	
	
}