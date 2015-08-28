package com.husjay.servlet;

import com.husjay.server.Request;
import com.husjay.server.Response;

/**
 * 抽象成一个父类
 * @author hsj
 *
 */
public abstract class Servlet {
	public void service(Request req, Response rep) throws Exception {
		this.doget(req, rep);
		this.dopost(req, rep);
	}
	
	protected abstract void doget(Request req, Response rep) throws Exception;
	protected abstract void dopost(Request req, Response rep) throws Exception;
}
