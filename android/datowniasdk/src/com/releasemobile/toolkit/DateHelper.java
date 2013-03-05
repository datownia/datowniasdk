package com.releasemobile.toolkit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author Ian
 *
 */
public class DateHelper {

	public static Calendar dateToCalendar(Date theDate)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(theDate);
		
		return calendar;
	}
	

	public static Date stringToDate(String dateAsString) {
		Date result = null;
		if (dateAsString != null && dateAsString.length() > 0)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
			try {
				result = dateFormat.parse(dateAsString);
			} catch (ParseException e) {
				// if the date was unparsable, don't crash leave it null
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String dateToString(Date date) {
		String result = null;
		if (date != null)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
			result = dateFormat.format(date);

		}
		return result;
	}
	
	
	/**
	 * adds to the supplied calendar but returns a new calendar instance
	 * @param calendar
	 * @param field
	 * @param amount
	 * @return a new instance of calendar with the added amount
	 */
	public static Calendar add(Calendar calendar, int field, int amount)
	{
		Calendar result = (Calendar)calendar.clone();
		result.add(field, amount);
		return result;
	}
	
	public static boolean isTimeNearNow(Calendar compare) 
	{
		Calendar now = Calendar.getInstance();
		Calendar oneMinuteLater = DateHelper.add(now, Calendar.MINUTE, 1);
		Calendar oneMinuteEarlier = DateHelper.add(now, Calendar.MINUTE, -1);
		return (compare.before(oneMinuteLater) && compare.after(oneMinuteEarlier));

	}
	
	public static boolean isTimeNearNow(Date value) 
	{
		//if the hour is the same as current hour
		//and the mins is within 1 minute of current mins
		//then set result = "now"
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTime(value);	     

		return isTimeNearNow(calendar);

	}
	
	public static String getTimeString(Date value) 
	{
		return getTimeString(value, false);

	}
	
	public static String getTimeString(Date value, Boolean displayNowIfClose) 
	{
		if (displayNowIfClose && isTimeNearNow(value))
			return "Now";
		
		SimpleDateFormat ft = new SimpleDateFormat("HH:mm", Locale.getDefault());
		
		String result = ft.format(value);
				
		return result;

	}
	
	
	public static String getTimeString(Calendar value, Boolean displayNowIfClose) 
	{
		return getTimeString(value.getTime(), displayNowIfClose);

	}
	
	public static String getTimeString(Calendar value) 
	{
		return getTimeString(value, false);

	}
	
	public static String getTimeRemainingString(long millis) {
		double sign = Math.signum(millis); //get sign
		millis *= sign; //ensure it is positive (we will reapply the minus sign later
		
		
		String hoursMinutes = String.format("%s%02d:%02d:%02d", sign < 0 ? "-" : "",
			    TimeUnit.MILLISECONDS.toHours(millis),
			    TimeUnit.MILLISECONDS.toMinutes(millis) - 
			    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		return hoursMinutes;
	}
}
