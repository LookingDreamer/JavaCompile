package com.common.filter;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class XssFilter implements Filter {
	FilterConfig filterConfig = null;
	/**必须过滤地址，优先级大于FILTER_PATH**/
	private static String MUST_FILTER_PATH="";
	/**不过滤地址**/
	private static String FILTER_PATH ="";
	
	static {
		//读取配置文件
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/xss");
		FILTER_PATH = resourceBundle.getString("FILTER_PATH");
		MUST_FILTER_PATH=resourceBundle.getString("MUST_FILTER_PATH");
	}
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//System.out.println("进入XssFilter");
		HttpServletRequest httpRequest =(HttpServletRequest) request;
		String pathInfo = httpRequest.getPathInfo();
		//System.out.println("pathinfo:"+pathInfo);
		//不过滤|过滤
		boolean result=true,resultMust=false;
		if(pathInfo==null){
			chain.doFilter(request,response);
		}
		else
		{
			//判断必须过滤
			if(MUST_FILTER_PATH!=null&&!MUST_FILTER_PATH.equals("")){
				String arrayMustPath[]=MUST_FILTER_PATH.split(",");
				for(int i=0;i<arrayMustPath.length;i++){
					if(pathInfo.startsWith(arrayMustPath[i])){
						resultMust=true;
						break;
					}
				}
			}
			if(resultMust){
				chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
			}else{
				//判断不过滤
				if(FILTER_PATH!=null&&!FILTER_PATH.equals("")){
					String arrayPath[]=FILTER_PATH.split(",");
					for(int i=0;i<arrayPath.length;i++){
						if(pathInfo.startsWith(arrayPath[i])){
							result=false;
							break;
						}
					}
				}
				if(result)
				{
					chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
				}else{
					chain.doFilter(request,response);
				}
			}
		}
		//System.out.println("结束XssFilter");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}
