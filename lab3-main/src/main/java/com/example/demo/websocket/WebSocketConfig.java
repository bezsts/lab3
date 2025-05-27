package com.example.demo.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final UserWebSocketHandler userWebSocketHandler;
    private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    public WebSocketConfig(UserWebSocketHandler userWebSocketHandler,
                           WebSocketHandshakeInterceptor webSocketHandshakeInterceptor) {
        this.userWebSocketHandler = userWebSocketHandler;
        this.webSocketHandshakeInterceptor = webSocketHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(userWebSocketHandler, "/crypto-websocket")
                .setAllowedOrigins("https://openidconnect.example.com:8443/")
                .addInterceptors(webSocketHandshakeInterceptor);
    }
}