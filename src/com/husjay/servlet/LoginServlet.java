package com.husjay.servlet;

import com.husjay.server.Request;
import com.husjay.server.Response;

public class LoginServlet extends Servlet{

	@Override
	public void doget(Request req, Response rep) throws Exception {
		// TODO Auto-generated method stub
		String name = req.getParameter("uname");
		String pwd = req.getParameter("pwd");
		if(login(name, pwd)) {
			rep.println("登录成功");
		} else {
			rep.println("登录失败");
		}
		
	}
	public boolean login(String name, String pwd) {
		return (name.equals("hsj") && pwd.equals("123"));
	}
	@Override
	public void dopost(Request req, Response rep) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
