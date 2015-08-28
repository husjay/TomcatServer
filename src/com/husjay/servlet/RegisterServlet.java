package com.husjay.servlet;

import com.husjay.server.Request;
import com.husjay.server.Response;

public class RegisterServlet extends Servlet{

	@Override
	public void doget(Request req, Response rep) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dopost(Request req, Response rep) throws Exception {
		// TODO Auto-generated method stub
		rep.println("<html><head><title>欢迎注册</title>");
		rep.println("</head><body>");
		rep.println("Hello Tomcat").println(req.getParameter("uname")).println("回来了");
		rep.println("</body></html>");
	}

}
