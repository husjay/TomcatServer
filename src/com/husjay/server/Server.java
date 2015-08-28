package com.husjay.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.husjay.server.util.CloseUtil;

public class Server {
	private ServerSocket server;
	public static final String CRLF="\r\n";
	public static final String BLANK=" ";
	
	private boolean isShutdown = false;
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
	/**
	 * 启动方法
	 */
	public void start() {
		start(8888);
	}
	public void start(int port) {
		try {
			server = new ServerSocket(port);
			this.receive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			stop();
		}
		
	}
	/**
	 * 停止方法
	 */
	public void stop() {
		isShutdown = true;
		CloseUtil.clossAll(server);
	}
	/**
	 * 接收方法
	 */
	private void receive() {
		try {
			while(!isShutdown) {
				Socket client = server.accept();
				new Thread(new Dispatcher(client)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			isShutdown = true;
		}
	}
}
