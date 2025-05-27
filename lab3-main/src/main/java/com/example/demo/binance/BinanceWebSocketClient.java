package com.example.demo.binance;

import com.example.demo.PriceUpdate;
import com.example.demo.websocket.UserWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
public class BinanceWebSocketClient extends WebSocketClient {
    @Autowired
    private UserWebSocketHandler userWebSocketHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BinanceWebSocketClient() {
        super(URI.create("wss://stream.binance.com:9443/stream?streams=btcusdt@miniTicker/dogeusdt@miniTicker/ethusdt@miniTicker/xrpusdt@miniTicker"));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to Binance WebSocket");
    }

    @Override
    public void onMessage(String message) {
        try {
            Map<String, Object> binanceData = objectMapper.readValue(message, Map.class);
            Map<String, Object> data = (Map<String, Object>) binanceData.get("data");

            if (data != null) {
                String symbol = (String) data.get("s");
                long timestamp = (long) data.get("E");
                double price = Double.parseDouble((String) data.get("c"));


                PriceUpdate priceUpdate = PriceUpdate.newBuilder()
                        .setCoin(symbol.replace("USDT", "").toUpperCase())
                        .setTimestamp(timestamp)
                        .setPrice(price)
                        .build();

                userWebSocketHandler.sendPriceUpdate(priceUpdate.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from Binance WebSocket: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}