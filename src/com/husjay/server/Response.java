package com.husjay.server;

import com.husjay.server.util.CloseUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 封装响应信息
 * @author hsj
 *
 */
public class Response {
	public static final String CRLF="\r\n";
	public static final String BLANK=" ";
	//存储正文信息
	private StringBuffer headInfo;
	//正文长度
	private int len;
	//正文
	private StringBuffer content;
	private BufferedWriter bw;
	
	public Response(Socket client) {
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			headInfo = null;
		}
	}
	public Response(OutputStream os) {
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}
	public Response() {
		headInfo = new StringBuffer();
		content = new StringBuffer();
		len = 0;
	}
	/**
	 * 构建正文
	 */
	public Response print(String info) {
		content.append(info);
		len += info.getBytes().length;
		return this;
	}
	/**
	 * 正文＋回车
	 */
	public Response println(String info) {
		content.append(info).append(CRLF);
		len += (info+CRLF).getBytes().length;
		return this;
	}
	/**
	 * 构建响应头
	 */
	private void createHeadInfo(int code) {
		//1 HTTP协议版本、状态代码、描述符
		headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
		switch(code) {
			case 200:headInfo.append("OK");break;
			case 404:headInfo.append("Not Found");break;
			case 505:headInfo.append("Server Error");break;
		}
		headInfo.append(CRLF);
		//2 响应头(Response Head)
		headInfo.append("Server:husjay Server/0.01").append(CRLF);
		headInfo.append("Date:").append(new Date()).append(CRLF);
		headInfo.append("Content-type:text/html;charset=utf-8").append(CRLF);
		//正文长度：字节长度
		headInfo.append("Content-Length:").append(len).append(CRLF);
		headInfo.append(CRLF); //分隔符
	}
	/**
	 * 推送到客户端
	 * @throws IOException 
	 */
	void pushToClient(int code) throws IOException {
		if(null == headInfo) {
			code = 500;
		}
		createHeadInfo(code);
		//头信息＋分隔符
		bw.append(headInfo.toString());
		bw.append(content.toString());
		bw.flush();
	}
	public void close() {
		CloseUtil.clossAll(bw);
	}
}
