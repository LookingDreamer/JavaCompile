package com.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cninsure.core.utils.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ModelUtil {

	/**定义HTML标签的正则表达式**/
	private static final String regEx_html = "<[^>]+>"; 
	/**定义回车换行符**/
	private static final String regEx_space = "\t|\r|\n";
	/**
	 * 过滤字符串中的html标签
	 * @param htmlStr 包含html字符串
	 * @return
	 */
	public static String replaceHtml(String htmlStr){
		Pattern pHtml = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher mHtml = pHtml.matcher(htmlStr);
		htmlStr = mHtml.replaceAll(""); 
		Pattern pSpace = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher mSpace = pSpace.matcher(htmlStr);
		htmlStr = mSpace.replaceAll(""); 
		return  htmlStr.trim(); 
	}
	public static String splitString(String value,String key){
		String[] arr = value.split(";");
		for(String str : arr){
			String[] oarr = str.split("=");
			if(key.equalsIgnoreCase(oarr[0]) && oarr.length > 1){
				return oarr[1];
			}else{
				continue;
			}
		}
		return "";
	}
	
	/**
	 * java 转换成xml
	 * @param obj 对象实例
	 * @return String xml字符串
	 */
	public static String toXml(Object obj){
		XStream xstream=new XStream();
		//如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
		xstream.processAnnotations(obj.getClass()); //通过注解方式的，一定要有这句话
		return xstream.toXML(obj);
	}
	
	/**
	 * 将传入xml文本转换成Java对象
	 * @param xmlStr
	 * @param cls  xml对应的class类
	 * @return T   xml对应的class类的实例对象
	 * 
	 * 调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr, PersonBean.class);
	 */
	@SuppressWarnings("unchecked")
	public static <T> T  toBean(String xmlStr,Class<T> cls){
		//注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
		XStream xstream=new XStream(new DomDriver());
		xstream.processAnnotations(cls);
		return (T) xstream.fromXML(xmlStr);			
	}
	
	public static Date conbertStringToDate(String str){
        if (StringUtil.isEmpty(str)) return null;

		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date conbertStringToNyrDate(String str){
        if (StringUtil.isEmpty(str)) return null;

        Date date = new Date();
		try {
			SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
			date = ymd.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String conbertToString(Date date){
		String result = "";
		if(null != date){
			SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
			result = ymd.format(date);
		}
		return result;
	}
	
	public static String conbertToStringsdf(Date date){
		String result = "";
		if(null != date){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = sdf.format(date);
		}
		return result;
	}
	/**
	 * 判断str是否为数字，正整数 包含0
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("^\\d+$"); 
	    return pattern.matcher(str).matches();    
	 }
	/**
	 * 当前日期增加一天
	 * @return
	 */
	public static Date nowDateAddOneDay(Date date){
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
	        c.add(Calendar.DAY_OF_MONTH, 1);
	        date =(Date) c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期增加一天
	 * @return
	 */
	public static String nowDateAddOneDay(String date) {
		String newDate;
		date = date.trim();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date oldDate = simpleDateFormat.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(oldDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			newDate = simpleDateFormat.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return date;
		}
		return newDate;
	}
	
	/**
	 * 当前日期增加一年
	 * //02-29 特殊处理 减一天 (不用减，02-29起保的终保日期是02-28，如果减了会变成02-27，因为终保日期会在当前返回日期基础上减1天)
	 * @return
	 */
	public static Date nowDateAddOneYear(Date date,int num){
		Date newDate = new Date();
		try {
			Calendar curr = Calendar.getInstance();
			curr.setTime(date);
			curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+num);
			newDate = curr.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return /*specialDate(date)?nowDateMinusOneDay(newDate):*/newDate;
	}

	/**
	 * 闰年2月最后一天特殊处理
	 * @param date
	 * @return
	 */
	public static boolean specialDate(Date date){
		return conbertToString(date).contains("02-29");
	}
	
	/**
	 * 日期比较
	 * @param date1
	 * @param date2
	 * @return 1 前面日期大 0 相等 -1 后面日期大
	 */
	 public static int compareDate(Date date1, Date date2) {
	        try {
	            if (date1.getTime() > date2.getTime()) {
	                return 1;
	            } else if (date1.getTime() < date2.getTime()) {
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }

	/**
	 * 判断str是否为浮点数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isFloatNumber(String str) {
		Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
		return pattern.matcher(str).matches();
	}
	
	/** 
     * 根据身份编号获取性别 
     *  
     * @param idCard 
     *            身份编号 
     * @return 性别 0-男，1-女
     */  
    public static int getGenderByIdCard(String idCard) {
		int sGender = 0;
		if (StringUtil.isEmpty(idCard)) return sGender;
		if (18 == idCard.length()) {
			String sCardNum = idCard.substring(16, 17);
			if (Integer.parseInt(sCardNum) % 2 != 0) {
				sGender = 0;
			} else {
				sGender = 1;
			}
		}
		if (15 == idCard.length()) {
			String sCardNum = idCard.substring(14);
			if (Integer.parseInt(sCardNum) % 2 != 0) {
				sGender = 0;
			} else {
				sGender = 1;
			}
		}
		return sGender;
    }
    /**
     * 隐藏身份证不符信息
     * @param idCard
     * @return
     */
    public static String hiddenIdSubCard(String idCard) {  
        String sub = idCard.substring(6, 14);  
        return idCard.replace(sub, "********");  
    }

	/**
	 * 隐藏手机号部分信息
	 */
	public static String hiddenPhoneSubNum(String phone) {
		if(phone == null || phone.length() < 8){
			return phone;
		}
		String sub = phone.substring(4, 7);
		return phone.replace(sub, "****");
	}

    /**
     * 隐藏部分信息
     * @param number
     * @return
     */
    public static String hiddenNumber(String number) {
        if (StringUtil.isEmpty(number)) return "";
    	int length = number.length();
    	String result = number;
    	if(length > 3){
    		StringBuilder builder = new StringBuilder();
    		for(int i = 0 ;i < length - 3;i ++){
        		builder.append("*");
        	}
    		String sub = number.substring(1, length-2);
    		result = number.replace(sub, builder.toString());
    	}
        return result;  
    }
    
    /**
	 * 日期减一天
	 * @return
	 */
	public static Date nowDateMinusOneDay(Date date){
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
	        c.add(Calendar.DAY_OF_MONTH, -1);
	        date =(Date) c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 得到两个日期相差的天数
	 * 闰年 包含二月  2008-02-02 -- 2009-02-01 相差 365
	 * 平年 2009-02-02 -- 2010-02-01 相差 364 
	 */
	public static int getBetweenDay(String start, String end) {
		Date date1 = ModelUtil.conbertStringToNyrDate(start);
		Date date2 = ModelUtil.conbertStringToNyrDate(end);
		return daysBetween(date1, date2);
	}
	/**
	 * 计算两个日期之间相差的天数, 用第二个参数日期 减去 第一个参数日期，精确度到天
	 * @param olddate
	 * @param nowdate
	 * @return nowdate - olddate
	 */
    public static int daysBetween(Date olddate,Date nowdate){    
        try {
			SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        	olddate=ymd.parse(ymd.format(olddate));
			nowdate=ymd.parse(ymd.format(nowdate));  
		} catch (ParseException e) {
			e.printStackTrace();
		}  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(olddate);    
        long oldtime = cal.getTimeInMillis();                 
        cal.setTime(nowdate);    
        long nowtime = cal.getTimeInMillis();         
        long between_days=(nowtime-oldtime)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }
	/**
	 * 与当前日期相差天数
	 * @param date
	 * @param day 
	 * @return 大于等于0可以投保
	 */
	public static int compareDateWithDay(Date date, int day) {
		//days > 0 date在当前日期之前，可以投保
		int days = daysBetween(date, new Date());
		if(days >= 0){
			return days;
		}else{
			return day - Math.abs(days);
		}
	}
	/**
	 * 隐藏vin部分信息
	 * @param vin
	 * @return
	 */
	public static String hiddenVin(String vin) {
        if (StringUtil.isEmpty(vin)) return "";
		if(vin.length() < 10){
			return vin;
		}
        String sub = vin.substring(vin.length()-10, vin.length()-4);
        return vin.replace(sub, "******");
    }
    
	/**
	 * 隐藏发动机部分信息
	 * @param engineno
	 * @return
	 */
	public static String hiddenEngineNo(String engineno) {
        if (StringUtil.isEmpty(engineno)) return "";
		if(engineno.length() < 6){
			return engineno;
		}
        String sub = engineno.substring(engineno.length()-6, engineno.length()-3);
        return engineno.replace(sub, "***");
    }
	
	/**
	 * 支付有效期判断,不包含当天
	 * @param sydate
	 * @param jqdate
	 * @return 离得近的时间
	 */
	public static Date gatFastPaydate(Date sydate,Date jqdate){
		Date newdate = null;
		if(null != sydate && null != jqdate){
			newdate = sydate.before(jqdate)?sydate:jqdate;
		}else{
			newdate = null == sydate?jqdate:sydate;
		}
		return minusOneDay(newdate);
	}
	
	/**
	 * 报价支付有效期判断，cm配置了有效期,不包含当天
	 * @param sydate
	 * @param jqdate
	 * @param sdate
	 * @param day
	 * @return 离得近的时间
	 */
	public static Date gatFastPaydateToNow(Date sydate,Date jqdate,Date sdate, int day){
		Date newdate = null;
		if(null != sydate && null != jqdate){
			newdate = sydate.before(jqdate)?sydate:jqdate;
		}else{
			newdate = null == sydate?jqdate:sydate;
		}
		if(null != sdate){
			//核保成功日期加支付有效期和pdate比较，取里当前日期近的
			Calendar c = Calendar.getInstance();
			c.setTime(sdate);
			c.add(Calendar.DAY_OF_MONTH, Math.abs(day));
			Date newstartdate =  (Date) c.getTime();
			return minusOneDay(newstartdate.before(newdate)?newstartdate:newdate);
		}else{
			return minusOneDay(newdate);
		}
	}
	/**
	 * 减去一天
	 * @param date
	 * @return
	 */
	public static Date minusOneDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return  (Date) c.getTime();
	}
	
	/**
	 * 时间差  单位秒
	 * @param start
	 * @param end
	 * @return
	 */
	public static long twoDatesDifferSeconds(Date start,Date end){
		return (start.getTime() - end.getTime())/1000;
	}


	//根据传入的保险期间计算下一个保险期间
	public static Map<String, Date> calNextPolicyDate(String startDateString, String endDateString) {
		Date effectDate = null, expireDate = null;
		Date endDate = ModelUtil.conbertStringToNyrDate(endDateString);
		Date startDate = ModelUtil.conbertStringToNyrDate(startDateString);

		if (endDate != null && startDate != null) {
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			Calendar start = Calendar.getInstance();
			start.setTime(startDate);
			start.set(Calendar.YEAR, start.get(Calendar.YEAR)+1);

			effectDate = end.getTime();

			if (ModelUtil.compareDate(start.getTime(), effectDate) == 1) {
				end.add(Calendar.DAY_OF_MONTH, 1);
				effectDate = end.getTime();
			}

		} else if (endDate != null) {
			effectDate = endDate;
		} else if (startDate != null) {
			effectDate = ModelUtil.nowDateAddOneYear(startDate, 1);
		} else {
			effectDate = ModelUtil.nowDateAddOneDay(new Date());
		}

		//修正起保日期
		if (ModelUtil.compareDate(effectDate, new Date()) != 1) {
			effectDate = ModelUtil.nowDateAddOneDay(new Date());
		}

		expireDate = ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(effectDate, 1));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(effectDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		effectDate = calendar.getTime();

		calendar.setTime(expireDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		expireDate = calendar.getTime();


		Map<String, Date> data = new HashMap<>(2);
		data.put("startDate", effectDate);
		data.put("endDate", expireDate);

		return data;
	}

	public static boolean isEmpty(Map<String, Object> map) {
		if (map == null || map.isEmpty()) return true;

		boolean isnull = true;
		Iterator iterator = map.values().iterator();
		Object value = null;

		while (iterator.hasNext()) {
			value = iterator.next();
			if (value == null) continue;
			if (value instanceof String) {
				if (StringUtil.isNotEmpty(value.toString())) {
					isnull = false;
				}
			} else {
				isnull = false;
			}
		}

		return isnull;
	}
	/**
	 * 是否需要后台设置默认分页值
	 * @param offset 起始记录数
	 * @param limit 记录总数
	 * @return
	 */
	public static boolean isVoluation(Integer offset,Integer limit){
		if(offset==null||limit==null){
			return true;
		} 
		if(offset<0||limit<0){
			return true;
		}
		int count=offset > limit ? offset-limit:limit-offset;
		if(count>ConstUtil.COUNT){
			return true;
		}
		return false;
	}
    public static void main(String[] args) {
		//System.out.println(hiddenEngineNo("SQR480EDEUUUUUU"));
		//System.out.println(compareDateWithDay(conbertStringToDate("2016-06-17 12:12:00"), 30));
    	//Date sydate = null;
    	/*Date sydate = conbertStringToDate("2016-04-26 00:00:00");
    	Date jqdate = conbertStringToDate("2016-04-26 00:00:00");
    	Date startdate = conbertStringToDate("2016-07-14 10:41:04");
    	System.out.println(conbertToString(gatFastPaydateToNow(sydate,jqdate, startdate, 0)));
    	System.out.println(conbertToString(gatFastPaydate(sydate,jqdate)));
		System.out.println(conbertToString(minusOneDay(startdate)));
		System.out.println(compareDateWithDay(startdate, 30));
		System.out.println(conbertStringToNyrDate("2016-07-14 10:41:04"));
		System.out.println(conbertToString(ModelUtil.nowDateAddOneDay(new Date())));
		System.out.println(conbertToString(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.nowDateAddOneDay(new Date()), 1))));
		
		System.out.println(twoDatesDifferSeconds(new Date(), conbertStringToDate("2016-08-02 05:02:02")));*/
		//System.out.println(conbertToString(ModelUtil.nowDateAddOneYear(conbertStringToDate("2016-02-29 00:00:00"), 1)));

		/*Map data = calNextPolicyDate("2016-02-29", "2017-02-28");
		System.out.println(data);
		data = calNextPolicyDate("2015-02-28", "2016-02-27");
		System.out.println(data);
		data = calNextPolicyDate("2016-01-01", "2016-12-31");
		System.out.println(data);
		data = calNextPolicyDate("2015-12-01", "2016-11-30");
		System.out.println(data);
		data = calNextPolicyDate("2015-11-01", "2016-10-31");
		System.out.println(data);
		data = calNextPolicyDate("2016-02-29", "2017-03-01");
		System.out.println(data);
		data = calNextPolicyDate("2015-02-28", "2016-02-28");
		System.out.println(data);
		data = calNextPolicyDate("2016-01-01", "2017-01-01");
		System.out.println(data);
		data = calNextPolicyDate("2015-12-01", "2016-12-01");
		System.out.println(data);
		data = calNextPolicyDate("2015-11-01", "2016-11-01");
		System.out.println(data);
		data = calNextPolicyDate("2016-11-01", "2017-05-31");
		System.out.println(data);*/
    	Map data = calNextPolicyDate("2016-07-29 00:00:00", "2017-08-01 23:59:29");
		System.out.println(data);
	}
	
}
