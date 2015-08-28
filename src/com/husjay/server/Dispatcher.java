package com.husjay.server;

import java.io.IOException;
import java.net.Socket;

import com.husjay.server.util.CloseUtil;
import com.husjay.servlet.Servlet;

/**
 * 一个请求和一个响应就是一个是Dispatcher
 * @author hsj
 *
 */
public class Dispatcher implements Runnable{
	private Socket client;
	private Request req;
	private Response rep;
	private int code = 200;

	Dispatcher(Socket client) {
		this.client = client;
		try {
			//请求
			req = new Request(client.getInputStream());
			//响应
			rep = new Response(client.getOutputStream());
		} catch (IOException e) {
			code = 500;
			return ;
		}		
		
	}
	public void run() {
		try {
			Servlet serv = WebApp.getServlet(req.getUrl());
			if(null == serv) {
				this.code = 404;
			} else {
				serv.service(req, rep);
			}
			rep.pushToClient(code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.code = 500;
		}
		try {
			rep.pushToClient(code);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CloseUtil.clossAll(client);
	}

}
