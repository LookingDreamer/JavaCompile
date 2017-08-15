package net.chetong.sdk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
  * 字符串操作工具类
  * @author Dylan
  * @date 2016年1月14日
  *
  */
public class StringUtil {
	/**
	 * 判断 字符串是否为空
	 * @param str
	 * @return 为空返回 TRUE
	 */
	public static boolean isNullOrEmpty(String o){
		if(o==null||"".equals(o.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断对象是否为空
	 * @param Obj
	 * @return 为空返回 TRUE
	 */
	public static boolean isNullOrEmpty(Object o){
		if(o==null||"".equals(o)){
			return true;
		}
		return false;
	}
	/**
	 * 数组是否包含对象
	 * @param o
	 * @param arr
	 * @return
	 */
	public static boolean isStringExistArray(Object o,String[] arr){
		if(isNullOrEmpty(o)||isNullOrEmpty(arr)){
			return false;
		}
		for(int i=0;i<arr.length;i++){
			if(o.equals(arr[i])){
				return true;
			}
		}
		return false;
	}
	
	public static boolean matches(String text, String pattern){
	    text = text + '\000';
	    pattern = pattern + '\000';

	    int N = pattern.length();

	    boolean[] states = new boolean[N + 1];
	    boolean[] old = new boolean[N + 1];
	    old[0] = true;

	    for (int i = 0; i < text.length(); i++) {
	      char c = text.charAt(i);
	      states = new boolean[N + 1];
	      for (int j = 0; j < N; j++) {
	        char p = pattern.charAt(j);

	        if ((old[j] != false) && (p == '*')) old[(j + 1)] = true;

	        if ((old[j] != false) && (p == c)) states[(j + 1)] = true;
	        if ((old[j] != false) && (p == '?')) states[(j + 1)] = true;
	        if ((old[j] != false) && (p == '*')) states[j] = true;
	        if ((old[j] != false) && (p == '*')) states[(j + 1)] = true;
	      }
	      old = states;
	    }
	    return states[N];
	  }
	
	
	/**
	 * 字符串是否数字，整数、小数、负数
	 * @param str
	 * @return boolean
	 */
	public static boolean isNum(String str){
		 if(str.matches("^\\d+$|-\\d+$") || str.matches("\\d+\\.\\d+$|-\\d+\\.\\d+$")) 
			return true;
		return false;
	}
	
	/**
	 * 获取现在的时间戳
	 * @author 2016年7月13日  下午4:11:15  温德彬
	 * @return
	 */
	public static String getDateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
}
