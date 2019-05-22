package com.icomac.service;


import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;  
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;  
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.icomac.websocket.WebSocketHandler;
import com.icomac.websocket.WebSocketHandshakeInterceptor;
/** 
 * Spring WebSocket的配置，这里采用的是注解的方式 
 */  
  
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {  
	
	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {  
        //1.注册WebSocket                           
        registry.addHandler(webSocketHandler(), "/websocket").      ////注册Handler,设置websocket的地址  
                addInterceptors(myInterceptor());                   //注册Interceptor
        System.out.println("After Regist");
    }
  
    @Bean
    public TextWebSocketHandler webSocketHandler() {  
        return new WebSocketHandler();
    }
    
    @Bean
    public WebSocketHandshakeInterceptor myInterceptor(){
        return new WebSocketHandshakeInterceptor();
    }
    
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
    	ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
    	container.setMaxTextMessageBufferSize(8192*4);
    	container.setMaxBinaryMessageBufferSize(8192*4);
    	return container;
    }
}
