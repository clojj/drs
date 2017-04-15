package de.fisp.skp;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Resource
	private SocketHandlerUpdate socketHandlerUpdate;

	@Resource
	private SocketHandler socketHandler;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketHandler, "/drs");
		registry.addHandler(socketHandlerUpdate, "/update");
	}

}