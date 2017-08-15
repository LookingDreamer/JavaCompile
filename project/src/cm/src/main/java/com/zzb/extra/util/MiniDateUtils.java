package com.zzb.extra.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MiniDateUtils {

	private final static boolean isHoliday = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		// Calendar c = Calendar.getInstance();
		// System.out.println("当前日期： "+sf.format(c.getTime()));
		// c.add(Calendar.DAY_OF_MONTH, 1);
		//
		// System.out.println("增加一天后日期 ： "+sf.format(c.getTime()));

		int add = getWorkDate(3);
		System.out.println(add);
	}

	public static boolean isHoliday(String year) {
		List<String> ls = new ArrayList<String>();

		// 元旦isNewYear
		ls.add("2016-12-31");
		ls.add("2017-01-01");
		ls.add("2017-01-02");// 周一

		// 春节isSpring
		ls.add("2017-01-27");
		ls.add("2017-01-28");
		ls.add("2017-01-29");
		ls.add("2017-01-30");
		ls.add("2017-01-31");
		ls.add("2017-02-01");
		ls.add("2017-02-02");

		// 清明节isTomb
		ls.add("2017-04-02");
		ls.add("2017-04-03");
		ls.add("2017-04-04");

		// 劳动节isLabour
		ls.add("2017-04-29");
		ls.add("2017-04-30");
		ls.add("2017-05-01");

		// 端午节isBoat
		ls.add("2017-05-28");
		ls.add("2017-05-29");
		ls.add("2017-05-30");

		// 国庆节isNational

		ls.add("2017-10-01");
		ls.add("2017-10-02");
		ls.add("2017-10-03");
		ls.add("2017-10-04");
		ls.add("2017-10-05");
		ls.add("2017-10-06");
		ls.add("2017-10-07");
		ls.add("2017-10-08");

		//

		if (ls.contains(year))
			return true;
		return false;
	}

	// 春节放假三天，定义到2020年
	public static boolean isNewYear(String year) {
		List<String> ls = new ArrayList<String>();

		// 元旦isNewYear
		ls.add("2016-12-31");
		ls.add("2017-01-01");
		ls.add("2017-01-02");// 周一

		//

		if (ls.contains(year))
			return true;
		return false;
	}

	public static boolean isSpring(String year) {
		List<String> ls = new ArrayList<String>();
		// 春节isSpring
		ls.add("2017-01-27");
		ls.add("2017-01-28");
		ls.add("2017-01-29");
		ls.add("2017-01-30");
		ls.add("2017-01-31");
		ls.add("2017-02-01");
		ls.add("2017-02-02");

		if (ls.contains(year))
			return true;
		return false;
	}

	// isTomb
	public static boolean isTomb(String findDate) {
		List<String> ls = new ArrayList<String>();
		// 清明节isTomb
		ls.add("2017-04-02");
		ls.add("2017-04-03");
		ls.add("2017-04-04");
		if (ls.contains(findDate))
			return true;
		return false;
	}

	public static boolean isBoat(String findDate) {
		List<String> ls = new ArrayList<String>();

		// 劳动节isLabour
		ls.add("2017-04-29");
		ls.add("2017-04-30");
		ls.add("2017-05-01");
		if (ls.contains(findDate))
			return true;
		return false;
	}

	public static boolean isLabour(String findDate) {
		List<String> ls = new ArrayList<String>();

		// 端午节isBoat
		ls.add("2017-05-28");
		ls.add("2017-05-29");
		ls.add("2017-05-30");
		if (ls.contains(findDate))
			return true;
		return false;
	}

	public static boolean isNational(String findDate) {
		List<String> ls = new ArrayList<String>();

		// 国庆节isNational

		ls.add("2017-10-01");
		ls.add("2017-10-02");
		ls.add("2017-10-03");
		ls.add("2017-10-04");
		ls.add("2017-10-05");
		ls.add("2017-10-06");
		ls.add("2017-10-07");
		ls.add("2017-10-08");
		if (ls.contains(findDate))
			return true;
		return false;
	}
	//调休isDiaoxiu
	public static boolean isDiaoxiu(String findDate) {
		List<String> ls1 = new ArrayList<String>();

		
		ls1.add("2017-01-22");
		ls1.add("2017-02-04");
		ls1.add("2017-04-01");
		ls1.add("2017-05-27");
		ls1.add("2017-09-30");
		
		if (ls1.contains(findDate))
			return true;
		return false;
	}

	public static int getWorkDate(final int day) {
		GregorianCalendar gc = new GregorianCalendar();

		int tmpDate = 0;
		int dateDiff = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
//		System.out.println("当前日期：               " + sf.format(c.getTime()));

		gc.add(gc.DATE, 1);

		while (tmpDate < day) {

			if (gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY
					&& gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {

				if (!isHoliday(sf.format(gc.getTime()))) {
					dateDiff++;
					tmpDate++;
					c.add(Calendar.DAY_OF_MONTH, 1);

//					System.out.println("工作日      "+sf.format(c.getTime()));

				} else {
					c.add(Calendar.DAY_OF_MONTH, 1);
					dateDiff++;
//					System.out.println("节假日       " + sf.format(c.getTime()));
				}
			} else {
				if(isDiaoxiu(sf.format(gc.getTime()))) {
					tmpDate++;
					dateDiff++;
					c.add(Calendar.DAY_OF_MONTH, 1);
//					System.out.println("工作日      "+sf.format(c.getTime()));
				}else {
					
					c.add(Calendar.DAY_OF_MONTH, 1);
					dateDiff++;
//					System.out.println("节假日       " + sf.format(c.getTime()));
				}
			}
			gc.add(gc.DATE, 1);

		}
		System.out.println(sf.format(c.getTime()));
		return dateDiff;
	}
	public long getTwoDay(Date begin_date, Date end_date) {
		  long day = 0;
		  try {
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		   String sdate = format.format(Calendar.getInstance().getTime());
		   
		   if (begin_date == null) {
		    begin_date = format.parse(sdate);
		   }
		   if (end_date == null) {
		    end_date = format.parse(sdate);
		   }
		   day = (end_date.getTime() - begin_date.getTime())
		     / (24 * 60 * 60 * 1000);
		  } catch (Exception e) {
		   return -1;
		  }
		  return day;
		 }

}
