package com.icomac.websocket;



import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;  
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor; 

/** 
 * WebSocket握手拦截器 
 */  
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {  
	
	@Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {  
    	System.out.println("Before Handshake");
    	if (request instanceof ServletServerHttpRequest) {
    		ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;  
            HttpSession session = servletRequest.getServletRequest().getSession(false);  
            if (session != null) {  
                String userName = (String) session.getAttribute("SESSION_USERNAME");  //这边获得登录时设置的唯一用户标识
                if (userName == null) {  
                    userName = "未知" + session.getId();  
                }  
                attributes.put("WEBSOCKET_USERNAME", userName);  //将用户标识放入参数列表后，下一步的websocket处理器可以读取这里面的数据
            }  
        }
        return true;  
    }
	
	@Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {  
        System.out.println("After Handshake");  
    }
}