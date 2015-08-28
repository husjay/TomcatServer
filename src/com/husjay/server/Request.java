package com.husjay.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 封装Request
 * @author hsj
 *
 */
public class Request {
	//请求方式
	private String method;
	//请求资源
	private String url;
	//请求参数
	private Map<String, List<String>> parameterMapValues;
	
	//内部
	public static final String CRLF="\r\n";
	public static final String BLANK=" ";
	private InputStream is;
	private String requestInfo;
	
	public Request() {
		method = "";
		url = "";
		parameterMapValues = new HashMap<String, List<String>>();
		requestInfo = "";
	}
	public Request(InputStream is) {
		this();
		this.is = is;
		try {
			byte[] data = new byte[20480];
			int len = is.read(data);
			requestInfo = new String(data, 0, len);
		} catch (IOException e) {
			return ;
		}
		//分析请求信息
		parseRequestInfo();
	}
	
	//分析头信息
	private void parseRequestInfo() {
		if(null == requestInfo || requestInfo.trim().equals("")) {
			return ;
		}
		/**
		 * ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		 * 从信息的首行分析出：请求方式、请求路径、请求参数(get可能存在)
		 * 如：GET /index.html?uname=123&pwd=123 HTTP/1.1
		 * 	  POST /index.html HTTP/1.1
		 * 
		 * post方式：求情参数可能存在最后
		 * －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		 */
		String parseString = ""; //接收请求参数
		//1 请求方式
		String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
		int index = firstLine.indexOf("/");
		this.method = firstLine.substring(0, index).trim();
		String urlStr = firstLine.substring(index, firstLine.indexOf("HTTP/")).trim();
		if(this.method.equalsIgnoreCase("post")) {
			this.url = urlStr;
			parseString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
		}else if(this.method.equalsIgnoreCase("get")) {
			if(urlStr.contains("?")) {//是否存在参数
				String[] urlArray = urlStr.split("\\?");
				this.url = urlArray[0];
				parseString = urlArray[1]; //接收请求参数
				
			} else {
				this.url = urlStr;
			}
			
		}
		if(parseString.equals("")) { //不存在请求参数
			return ;
		}
		//2 请求参数封装到Map中
		parseParame(parseString);
	}
	
	private void parseParame(String parseString) {
		//1 分割 将字符串转成数组
		StringTokenizer token = new StringTokenizer(parseString,"&");
		while(token.hasMoreTokens()) {
			String keyValue = token.nextToken();
			String[] keyValues = keyValue.split("=");
			if(keyValues.length == 1) {
				keyValues = Arrays.copyOf(keyValues, 2);
				keyValues[1] = null;
			}
			String key = keyValues[0].trim();
			String value = (null == keyValues[1]) ? null:decode(keyValues[1].trim(), "utf-8");
			//转换成Map
			if(!parameterMapValues.containsKey(key)) {
				parameterMapValues.put(key, new ArrayList<String>());
			}
			List<String> values = parameterMapValues.get(key);
			values.add(value);
		}
	}
	/**
	 * 解决中文问题
	 * @param value
	 * @param code
	 * @return
	 */
	private String decode(String value, String code) {
		try {
			return java.net.URLDecoder.decode(value, code);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据页面的name获取页面的值
	 * @param args
	 */
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		if(null == values) {
			return null;
		} 
		return values[0];
	}
	public String[] getParameterValues(String name) {
		List<String> values = null;
		if((values = parameterMapValues.get(name)) == null) {
			return null;
		} else {
			return values.toArray(new String[0]);
		}
		
	}
	public String getUrl() {
		return url;
	}
}
