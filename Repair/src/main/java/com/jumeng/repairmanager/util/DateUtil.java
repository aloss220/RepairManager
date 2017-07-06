package com.jumeng.repairmanager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	/** 
	 * 取得当前日期是多少周 
	 * 
	 * @param date 
	 * @return 
	 */ 
	public static int getWeekOfYear(Date date) { 
		Calendar c = new GregorianCalendar(); 
		c.setFirstDayOfWeek(Calendar.MONDAY); 
		c.setMinimalDaysInFirstWeek(7); 
		c.setTime (date);

		return c.get(Calendar.WEEK_OF_YEAR); 
	}

	/** 
	 * 得到某一年周的总数 
	 * 
	 * @param year 
	 * @return 
	 */ 
	public static int getMaxWeekNumOfYear(int year) { 
		Calendar c = new GregorianCalendar(); 
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

		return getWeekOfYear(c.getTime()); 
	}

	/** 
	 * 得到某年某周的第一天 
	 * 
	 * @param year 
	 * @param week 
	 * @return 
	 */ 
	public static Date getFirstDayOfWeek(int year, int week) { 
		Calendar c = new GregorianCalendar(); 
		c.set(Calendar.YEAR, year); 
		c.set (Calendar.MONTH, Calendar.JANUARY); 
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone(); 
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime ()); 
	}

	/** 
	 * 得到某年某周的最后一天 
	 * 
	 * @param year 
	 * @param week 
	 * @return 
	 */ 
	public static Date getLastDayOfWeek(int year, int week) { 
		Calendar c = new GregorianCalendar(); 
		c.set(Calendar.YEAR, year); 
		c.set(Calendar.MONTH, Calendar.JANUARY); 
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone(); 
		cal.add(Calendar.DATE , week * 7);

		return getLastDayOfWeek(cal.getTime()); 
	}

	/** 
	 * 取得指定日期所在周的第一天 
	 * 
	 * @param date 
	 * @return 
	 */ 
	public static Date getFirstDayOfWeek(Date date) { 
		Calendar c = new GregorianCalendar(); 
		c.setFirstDayOfWeek(Calendar.MONDAY); 
		c.setTime(date); 
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday 
		return c.getTime (); 
	}

	/** 
	 * 取得指定日期所在周的最后一天 
	 * 
	 * @param date 
	 * @return 
	 */ 
	public static Date getLastDayOfWeek(Date date) { 
		Calendar c = new GregorianCalendar(); 
		c.setFirstDayOfWeek(Calendar.MONDAY); 
		c.setTime(date); 
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday 
		return c.getTime(); 
	} 

	/** 
	 * 取得当前日期所在周的第一天 
	 * 
	 * @param date 
	 * @return 
	 */ 
	public static Date getFirstDayOfWeek() { 
		Calendar c = new GregorianCalendar(); 
		c.setFirstDayOfWeek(Calendar.MONDAY); 
		c.setTime(new Date()); 
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday 
		return c.getTime (); 
	}

	/** 
	 * 取得当前日期所在周的最后一天 
	 * 
	 * @param date 
	 * @return 
	 */ 
	public static Date getLastDayOfWeek() { 
		Calendar c = new GregorianCalendar(); 
		c.setFirstDayOfWeek(Calendar.MONDAY); 
		c.setTime(new Date()); 
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday 
		return c.getTime(); 
	} 

	/**
	 * 获取当天零点的毫秒值
	 * @return
	 */

	public static long getTodayZero() {
		Date date = new Date();     
		long l = 24*60*60*1000; //每天总的毫秒数 
		//date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。 
		//减8个小时的毫秒值是为了解决时区的问题。
		return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000 +l);
	}

	/**
	 * 格式化毫秒值
	 * @return
	 */ 
	public static String getTime(long time) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date d1=new Date(time);  
		return format.format(d1);  
	}

	
	public static String getDoubleDate(int n){
		String s=n<10?"0"+n:n+"";
		
		return s;
	}


	/**
	 * 获取前一天日期
	 * 
	 * @return 返回 yyyy-MM-dd
	 */
	public static String getDatePre() {
		Calendar ca = Calendar.getInstance();//得到一个Calendar的实例 
		ca.setTime(new Date()); //设置时间为当前时间 
		ca.add(Calendar.DATE, -1); //天数减1 0 表示当天  -1表示昨天  1表示明日
		Date yesterday = ca.getTime(); //结果
		return new SimpleDateFormat("yyyy-MM-dd").format(yesterday);
	}

	/**
	 * 获取当天的年月日   2015/12/09
	 * 
	 * @return 返回 yyyy-MM-dd
	 */	

	public static String getCurDate(){
		Calendar c=Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String result=year+"/"+getDoubleDate(month)+"/"+getDoubleDate(day);
		return result;
	}
	/**
	 * 获取前一天的年月日   2015/12/09
	 * 
	 * @return 返回 yyyy-MM-dd
	 */	

	public static String getPreDate(){
		Calendar c=Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		int preYear=c.get(Calendar.YEAR);
		int preMonth=c.get(Calendar.MONTH)+1;
		int preDate = c.get(Calendar.DAY_OF_MONTH);
		String result=preYear+"/"+getDoubleDate(preMonth)+"/"+getDoubleDate(preDate);
		return result;
	}
	/**
	 * 获取后一天的年月日   2015/12/09
	 * 
	 * @return 返回 yyyy-MM-dd
	 */	

	public static String getPostDate(){
		Calendar c=Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		int postYear=c.get(Calendar.YEAR);
		int postMonth=c.get(Calendar.MONTH)+1;
		int postDate = c.get(Calendar.DAY_OF_MONTH);
		String result=postYear+"/"+getDoubleDate(postMonth)+"/"+getDoubleDate(postDate);
		return result;
	}
	/**
	 * 获取本周第一天到最后一天的日期   2015/12/09~2015/12/23
	 * 
	 * @return 返回 yyyy-MM-dd
	 */	

	public static String getCurWeek(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String firDay =sdf.format(c.getTime());
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String lastDay =sdf.format(c.getTime());

		String result=firDay+"~"+lastDay;
		return result;

	}
	/**
	 * 获取上周第一天到最后一天的日期   2015/12/09~2015/12/23
	 * 
	 * @return 返回 yyyy-MM-dd
	 */	

	public static String getPreWeek(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.add(Calendar.WEEK_OF_YEAR, -1);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String firDay =sdf.format(c.getTime());
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String lastDay =sdf.format(c.getTime());

		String result=firDay+"~"+lastDay;
		return result;

	}
	/**
	 * 获取下周第一天到最后一天的日期   2015/12/09~2015/12/23
	 * 
	 * @return 返回 yyyy-MM-dd
	 */	

	public static String getPostWeek(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.add(Calendar.WEEK_OF_YEAR, 1);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String firDay =sdf.format(c.getTime());
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String lastDay =sdf.format(c.getTime());

		String result=firDay+"~"+lastDay;
		return result;

	}

	/**
	 * 获取本月的年月2015/12
	 */		

	public static String getCurMonth(){
		Calendar c = Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		String result=year+"/"+getDoubleDate(month);
		return result;
	}
	/**
	 * 获取上月月的年月2015/12
	 */		

	public static String getPreMonth(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		int preYear=c.get(Calendar.YEAR);
		int preMonth=c.get(Calendar.MONTH)+1;
		String result=preYear+"/"+getDoubleDate(preMonth);
		return result;
	}
	/**
	 * 获取下月的年月2015/12
	 */		

	public static String getPostMonth(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		int postYear=c.get(Calendar.YEAR);
		int postMonth=c.get(Calendar.MONTH)+1;
		String result=postYear+"/"+getDoubleDate(postMonth);
		return result;
	}




}
