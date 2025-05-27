package com.example.demo.openidconnect;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openidconnect")
public class OpenIdConnectProperties {

    private String endpoint;
    private String clientId;
    private String clientSecret;

    public String getOpenIdConnectEndpoint() {
        return endpoint;
    }

    public String getOpenIdConnectClientId() {
        return clientId;
    }

    public String getOpenIdConnectClientSecret() {
        return clientSecret;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
