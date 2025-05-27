package com.example.demo.websocket;

import com.example.demo.binance.BinanceWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WebSocketInitializer implements CommandLineRunner {

    @Autowired
    private BinanceWebSocketClient binanceWebSocketClient;

    @Override
    public void run(String... args) throws Exception {
        binanceWebSocketClient.connect();
    }
}
