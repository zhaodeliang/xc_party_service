package com.daling.party.infrastructure.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 放置常用的与日期时间处理有关的功能
 * 
 * @author Wangshubing
 */

public class DateTimeTool {
	public static final String strDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String strDateFormat = "yyyy-MM-dd";
	
	/**
	 * 算时间差,返回毫秒
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Long getTimeDifference(String startTime,String endTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		Date endDate = null;
		Long between = 0L;
		try {
			startDate = sdf.parse(startTime);
			endDate = sdf.parse(endTime);
			between = endDate.getTime()-startDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return between;
	}
		
	
	public static String format(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	
	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(format).format(date);
	}

	public static Date getDateFromString(String date) {
		if (date == null || date.trim().length() <= 0) {
			return null;
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date parse(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public static Date parse(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * yyyyMMdd HH:mm:ss yyyyMMdd HH:mm:ss
	 * 
	 * @param format
	 * @return
	 */
	public static String getToday(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	public static Date getTodayDate() {
		return new Date();
	}

	/**
	 * 取出日期中，今天凌晨的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getBeginTime(Date date) {
		if (date == null) {
			return getMinDate();
		}
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
		try {
			return new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS").parse(dateStr + " 00:00:00 000");
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			return date;
		}
	}

	/**
	 * 取出日期中，今天结束的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndTime(Date date) {
		if (date == null) {
			return getMaxDate();
		}
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
		try {
			return new SimpleDateFormat("yyyyMMdd HH:mm:ss SSS").parse(dateStr + " 23:59:59 999");
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			return date;
		}
	}

	/**
	 * 取出今天日期中，今天凌晨的时间
	 * 
	 * @return
	 */
	public static Date getTodayBeginTime() {
		return getBeginTime(new Date());
	}

	/**
	 * 取出今天日期中，今天结束的时间
	 * 
	 * @return
	 */
	public static Date getTodayEndTime() {
		return getEndTime(new Date());
	}

	public static Date getMinDate() {
		try {
			return new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse("19000101 00:00:00");
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}

	public static Date getMaxDate() {
		try {
			return new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse("29000101 00:00:00");
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 取出某个日期的多少个月之后的日期
	 * 
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date getAfterMonths(Date date, int months) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	
	
	

	
	/**
	 * 返回数组结果 某周的开始日期时间  某周的结束日期时间
	 * 带有时分秒
	 * @param year
	 * @param weekOfYear
	 * @return
	 */
	public static Date[]  getWeekStartEndDate(int year,int weekOfYear){
		Date[] ds = new Date[2];
		int y,m,d,y1,m1,d1;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal  = Calendar.getInstance();
		
		try{
			cal.set(Calendar.YEAR,year);
			cal.set(Calendar.WEEK_OF_YEAR,weekOfYear);
			cal.setFirstDayOfWeek(Calendar.MONDAY);	
			cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
			cal.setMinimalDaysInFirstWeek(7);
			y=cal.get(Calendar.YEAR);
			m=cal.get(Calendar.MONTH);
			d= cal.get(Calendar.DATE);
			ds[0] = format.parse(y+"-"+(m+1)+"-"+d+" 00:00:00");
			
			cal.add(Calendar.DATE,6);
			y1=cal.get(Calendar.YEAR);
			m1=cal.get(Calendar.MONTH);
			d1= cal.get(Calendar.DATE);
			ds[1] = format.parse(y1+"-"+(m1+1)+"-"+d1+" 23:59:59");
			
			System.out.println(ds[0]+" , "+ds[1]);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
			System.out.println(e.toString());
			
		}
		
		return ds;
	} 
	
	
	/**
	 * 获取某天的开始结束时间
	 * @param date
	 * @return
	 */
	public static Date[]  getDayStartEndDate(Date date){
		Date[] ds = new Date[2];
		int y,m,d;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal  = Calendar.getInstance();
		cal.setTime(date);
		try{
			y=cal.get(Calendar.YEAR);
			m=cal.get(Calendar.MONTH);
			d= cal.get(Calendar.DATE);
			ds[0] = format.parse(y+"-"+(m+1)+"-"+d+" 00:00:00");
			ds[1] = format.parse(y+"-"+(m+1)+"-"+d+" 23:59:59");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
			
		}
		return ds;
	}
	
	/**
	 * 
	 * @param year
	 * @param month  1~12  Jan~Nov
	 * @return
	 */
	
	@SuppressWarnings("unused")
	public static Date[]  getMonthStartEndDate(int year,int  month){
		Date[] ds = new Date[2];
		int y,m,d;
		Date date =null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal  = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,(month-1));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		date = cal.getTime();
		
		try{
			y=cal.get(Calendar.YEAR);
			m=cal.get(Calendar.MONTH);
			d= cal.get(Calendar.DATE);
			ds[0] = format.parse(y+"-"+(m+1)+"-"+d+" 00:00:00");
			
			cal.add(Calendar.MONTH,1);
			cal.add(Calendar.DATE,-1);
			
			y=cal.get(Calendar.YEAR);
			m=cal.get(Calendar.MONTH);
			d= cal.get(Calendar.DATE);
			ds[1] = format.parse(y+"-"+(m+1)+"-"+d+" 23:59:59");
			

			System.out.println(ds[0]+" , "+ds[1]);
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
			
			
		}
		return ds;
	}

    public static int getDayDifference(String startTime, String endTime) {
        org.joda.time.format.DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime startDate = DateTime.parse(startTime, dateTimeFormat);
        DateTime endDate = DateTime.parse(endTime);
        return Days.daysBetween(startDate, endDate).getDays();
    }


    /**
	 * 每个季节的开始和结束时间
	 * @param year
	 * @param season
	 * @return
	 */
	public static  Date[]  getSeasonStartEndDate(int year,int  season){
		Date[] ds = new Date[2];
		int y,m,d;
		Date date =null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal  = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		y =year;
		Map<String,String[]> map= null;
		
		String[] dates =null;
		
		try{
			map = new HashMap();
			map.put("1",new String[]{"-01-01 00:00:00","-03-31 23:59:59"});
			map.put("2",new String[]{"-04-01 00:00:00","-06-30 23:59:59"});
			map.put("3",new String[]{"-07-01 00:00:00","-09-30 23:59:59"});
			map.put("4",new String[]{"-10-01 00:00:00","-12-31 23:59:59"});
			
			if(season>0 && season <5){
				dates= map.get(""+season);	
			}else{
				
			}
			
		
		
			ds[0] = format.parse(year + dates[0]);
			ds[1] = format.parse(year + dates[1]);
			System.out.println("ds[0]="+ds[0]+", ds[1]="+ds[1]);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
			
		}
		
		
		
		
		
		return ds;
	}


	public static Date addHours(Date date, int hours) {
		return new Date(date.getTime()+hours*60L*60L*1000L);
	}
	
	public static Date[] getYearStartEndDate(int year){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date[] ds = new Date[2];
		try {
			ds[0] = format.parse(year+"-"+"01-01 00:00:00");
			ds[1] = format.parse(year+"-"+"12-31 23:59:59");
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		} 
		
		return ds;
	}
	
	public static Date parse(String date, String format, boolean createIfIllegal) {
		try {
			return new SimpleDateFormat(format).parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
			if (createIfIllegal) {
				return new Date();
			}
		}
		return null;
	}

    public static String getCurrentTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
	/**
	 * 
	 * 日期转换
	 * 
	 * @param date
	 * @return Timestamp
	 * @Author 杨健/YangJian
	 * @Date 2015年4月29日 下午7:15:02
	 * @Version 1.0.0
	 */
	public static Timestamp toTimestamp(Date date) {
		if (date == null) {
			return null;
		}
		return new Timestamp(date.getTime());
	}    
	
	/**
	 * Timestamp
	 * 
	 * @return Timestamp
	 * @Author 杨健/YangJian
	 * @Date 2016年3月11日 下午6:52:34
	 * @Version 1.0.0
	 */
	public static Timestamp currentTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	/**
	 * 当前日期为，今年的第几周
	 * @param date
	 * @return
	 */
	public static String weekOfYear(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		/**设置一年中第一个星期所需的最少天数，例如，如果定义第一个星期包含一年第一个月的第一天，则使用值 1 调用此方法。 
         * 如果最少天数必须是一整个星期，则使用值 7 调用此方法。 **/  
		cal.setMinimalDaysInFirstWeek(4); 
		cal.setTime(date);
		//cal.add(Calendar.DAY_OF_MONTH, -7);  
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		int year = cal.get(Calendar.YEAR);  
		/*if(week<cal.get(Calendar.WEEK_OF_YEAR)){  
		    year+=1;  
		} */
		//System.out.println(cal.get(Calendar.MONTH));
		if(cal.get(Calendar.MONTH)==11&&week<3){
			year+=1;
		}else if(cal.get(Calendar.MONTH)==0&&week>10){
			year-=1;
		}
		
		return year+"_"+week;
	}
	
	/**  
     * 取得当前日期是多少周  
     *  
     * @param date  
     * @return  
     */  
    public static int getWeekOfYear(Date date) {  
        Calendar c = Calendar.getInstance();  
        c.setFirstDayOfWeek(Calendar.MONDAY);  
        /**设置一年中第一个星期所需的最少天数，例如，如果定义第一个星期包含一年第一个月的第一天，则使用值 1 调用此方法。 
         * 如果最少天数必须是一整个星期，则使用值 7 调用此方法。 **/  
        c.setMinimalDaysInFirstWeek(4);  
        c.setTime(date);  
  
        return c.get(Calendar.WEEK_OF_YEAR);  
    }  
    /**  
     * 得到某年某周的第一天  
     *  
     * @param year  
     * @param week  
     * @return  
     */  
    public static Date getFirstDayOfWeek(int year, int week) {  
        Calendar c = Calendar.getInstance();  
        c.set(Calendar.YEAR, year);  
        c.setFirstDayOfWeek(Calendar.MONDAY);  
        c.setMinimalDaysInFirstWeek(4);  
        c.set(Calendar.WEEK_OF_YEAR, week);  
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//设置周一  
  
        return c.getTime();  
    }
    
    public static int getQuarterOfYear(Date date){
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int currentMonth = cal.get(Calendar.MONTH) + 1;
		if (currentMonth >= 1 && currentMonth <= 3) 
			return 1;
        else if (currentMonth >= 4 && currentMonth <= 6) 
        	return 2;
        else if (currentMonth >= 7 && currentMonth <= 9) 
        	return 3;
        else if (currentMonth >= 10 && currentMonth <= 12) 
        	return 4;
        else
        	return 0;
    }
    
    /**
     * 根据生产日期获取本周/月/年/季的第一天的日期
     * @param productionDate 生产日期
     * @param validityDm 效段维度：ValidityManageUnit_Year:年，ValidityManageUnit_Quarter:季，ValidityManageUnit_Month:月，ValidityManageUnit_Week:周，ValidityManageUnit_Day:日
     * @return
     */
    public static Date getFirstDayByDateValidityDm(Date productionDate,String validityDm){
    	
    	if(productionDate==null){
    		return null;
    	}
    	
    	Date rtDate=productionDate;
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(productionDate);
		
    	if("ValidityManageUnit_Month".equals(validityDm)){
    		//当月的第一天日期
    		int currentMonth = cal.get(Calendar.MONTH) + 1;
    		rtDate=new Date(cal.get(Calendar.YEAR)+"/"+currentMonth+"/01");
		}else if("ValidityManageUnit_Week".equals(validityDm)){
			//得到周一的日期
			String validity= DateTimeTool.weekOfYear(productionDate);
			if(validity!=null){
				String[] ary=validity.split("_");
				//获取周的第一天
				rtDate= DateTimeTool.getFirstDayOfWeek(Integer.parseInt(ary[0]), Integer.parseInt(ary[1]));
			}
		}else if("ValidityManageUnit_Quarter".equals(validityDm)){
			
			//得到当季的第一天日期
			int quarter= DateTimeTool.getQuarterOfYear(productionDate);
			if(quarter==1){
				rtDate=new Date(cal.get(Calendar.YEAR)+"/01/01");
			}else if(quarter==2){
				rtDate=new Date(cal.get(Calendar.YEAR)+"/04/01");
			}else if(quarter==3){
				rtDate=new Date(cal.get(Calendar.YEAR)+"/07/01");
			}else if(quarter==4){
				rtDate=new Date(cal.get(Calendar.YEAR)+"/10/01");
			}
			
		}else if("ValidityManageUnit_Year".equals(validityDm)){
			//当年第一天日期
			rtDate=new Date(cal.get(Calendar.YEAR)+"/01/01");
		}else if("ValidityManageUnit_Day".equals(validityDm)){
			//当天
    		rtDate=productionDate;
		}
    	
    	return rtDate;
    }
	

	/**
	 * 当前日期为，今年的第几天
	 * @param date
	 * @return
	 */
	public static String dayOfYear(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int c = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR); 
		return year+"_"+c;
	}

	public static Calendar dayOfMonth(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static String monthOfYear(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return  cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH) + 1);
	}
	
	public static String quarterOfYear(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int currentMonth = cal.get(Calendar.MONTH) + 1;
		if (currentMonth >= 1 && currentMonth <= 3) 
			return year+"_"+1;
        else if (currentMonth >= 4 && currentMonth <= 6) 
        	return year+"_"+2;
        else if (currentMonth >= 7 && currentMonth <= 9) 
        	return year+"_"+3;
        else if (currentMonth >= 10 && currentMonth <= 12) 
        	return year+"_"+4;
        else
        	return null;
	}

	/**
	 * 转化CST
	 * @param data
	 * @return
	 */
	public static String CSTFormat(String data) {
		String format = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format = sdf.format(df.parse(data));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format;
	}

}
