package com.icomac.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Websocket处理器
 */

public class WebSocketHandler extends TextWebSocketHandler {
 
	// 已建立连接的用户
	private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();
 
	// 在线用户人数
	private static LinkedList<String> onlineuser = new LinkedList<String>();
	
	/**
	 * 处理前端发送的文本信息 js调用websocket.send时候，会调用该方法
	 * 
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		String file_name = "", target = "", send_message = "";
		// 获取提交过来的消息详情
		System.out.println("收到用户 " + username + " 的消息:" + message.toString());
		// 分割成";" 
		String resend_message = message.getPayload();
		String[] messageInfo = resend_message.split(";");
		for(int i=0; i<messageInfo.length; i++) {
			String item = messageInfo[i];
			if(item.indexOf("Message:")!=-1) {send_message = item.split(":")[1];}
			else if(item.indexOf("Filename:")!=-1) {file_name = item.split(":")[1];}
			else if(item.indexOf("@")!=-1) {target = item.split("@")[1];}
		}
		System.out.println(resend_message+"\n"+send_message+"\n"+target+"\n"+file_name+"\n");
		if(target.equals("")) {
			System.out.println("发送者信息丢失,服务器出错!!!");
			sendMessageToUser(username, new TextMessage("Message:服务器出错请稍后再发送吧@System"));
		} 
		else if((!target.equals("")) && file_name.equals("") && send_message.equals("")) {
			sendMessageToUser(target, new TextMessage("Message:发送失败@"+username));
		}
		else {
			try {
				if(target.equals("Allusers")) {
					sendMessageToUsers(new TextMessage(resend_message));
					}
				else {
					for (WebSocketSession user : users) {
						if (user.getAttributes().get("WEBSOCKET_USERNAME").equals(target)) {
							if (user.isOpen()) {
								sendMessageToUser(target, new TextMessage(resend_message));
							}else {
								sendMessageToUser(username, new TextMessage("Message:对方暂时不在线@System"));
								}
							}
						}
					}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				sendMessageToUser(username, new TextMessage("Message:对方在线异常,发送失败@System"));
			}
		}
	}
	
	/*
	 * 接受二进制文件
	 */
	@Override
	public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		System.out.println("收到二进制文件");
		sendMessageToUsers(message);
	}
 
	/**
	 * 当新连接建立的时候，被调用 连接成功时候，会触发页面上onOpen方法
	 * 
	 * @param session
	 * @throws Exception
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("afterConnection");
		users.add(session);
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		addOnline(username);
		int onlinecount = getOnline().size();
		System.out.println("用户 " + username + " Connection Established\n目前在线人数为: " + onlinecount);
		//session.sendMessage(new TextMessage(username + " connect<br>目前在线人数为: " + onlinecount));
	}
 
	/**
	 * 当连接关闭时被调用
	 * 
	 * @param session
	 * @param status
	 * @throws Exception
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		System.out.println("用户 " + username + " Connection closed. Status: " + status);
		subOnline(username);
		users.remove(session);
	}
 
	/**
	 * 传输错误时调用
	 * 
	 * @param session
	 * @param exception
	 * @throws Exception
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
		if (session.isOpen()) {
			session.close();
		}
		System.out.println("用户: " + username + " websocket connection closed......");
		users.remove(session);
	}
 
	/**
	 * 给某个用户发送消息
	 * 
	 * @param userName
	 * @param message
	 */
	public static void sendMessageToUser(String targetName, TextMessage message) {
		for (WebSocketSession user : users) {
			if (user.getAttributes().get("WEBSOCKET_USERNAME").equals(targetName)) {
				try {
					if (user.isOpen()) {
						user.sendMessage(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 */
	public static void sendMessageToUsers(AbstractWebSocketMessage<?> message) {
		for (WebSocketSession user : users) {
			try {
				if (user.isOpen()) {
					user.sendMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 在线用户
	 * 
	 */
	public static synchronized LinkedList<String> getOnline() {
		return onlineuser;
	}
	
	/*
	 * 在线人数增加
	 * 
	 */
	public static synchronized void addOnline(String username) {
		onlineuser.add(username);
		reflash();
	}
	
	/*
	 * 在线人数减少
	 * 
	 */
	public static synchronized void subOnline(String username) {
		onlineuser.remove(username);
		reflash();
	}
	
	/*
	 * 实时发送在线用户
	 */
	public static void reflash() {
		LinkedList<String> onlineuser = getOnline();
		String message = "";
		for(int i=0; i<onlineuser.size(); i++) {
			message += onlineuser.get(i) + "<br/>";
    	}
		sendMessageToUsers(new TextMessage("Online:"+message+";"));
	}
}
