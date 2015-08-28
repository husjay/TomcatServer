package com.husjay.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.husjay.servlet.Servlet;

public class WebApp {
	private static ServletContent contxt;
	static {
		
		try {
			//获取解析工厂
			SAXParserFactory factory = SAXParserFactory.newInstance();
			//从解析工厂中获取解析器
			SAXParser parse = factory.newSAXParser();
			//加载文档 注册处理器
			//编写处理器
			WebHandler web = new WebHandler();
			parse.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("WEB_INFO/web.xml")
					, web);
			
			//将list转成map
			contxt = new ServletContent();
			//servlet-name  servlet-class
			Map<String, String> servlet = contxt.getServlet();
			for(Entity entity:web.getEntityList()) {
				servlet.put(entity.getName(), entity.getCls());
			}
			
			//url-pattern servlet-name
			Map<String, String> mapping = contxt.getMapping();
			for(Mapping mapp:web.getMappingList()) {
				List<String> urls = mapp.getUrlPattern();
				for(String url:urls) {
					mapping.put(url, mapp.getName());
				}
			}
				
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public static Servlet getServlet(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if(null == url || url.trim().equals("")) {
			return null;
		}
		//根据字符串（完整路径）创建对象
		//return contxt.getServlet().get(contxt.getMapping().get(url));
		String name = contxt.getServlet().get(contxt.getMapping().get(url));
		return (Servlet)Class.forName(name).newInstance();
	}
}
