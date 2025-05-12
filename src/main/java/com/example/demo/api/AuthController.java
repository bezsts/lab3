package com.example.demo.api;

import com.example.demo.openidconnect.OpenIdConnectProperties;
import com.example.demo.ssl.SSLUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
public class AuthController {
    private final OpenIdConnectProperties openIdConnectProperties;

    private final String redirectUri = "https://openidconnect.example.com:8443/callback";

    @Autowired
    public AuthController(OpenIdConnectProperties openIdConnectProperties) {
        this.openIdConnectProperties = openIdConnectProperties;
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        System.out.println("Login requested");

        String authorizeUrl = openIdConnectProperties.getOpenIdConnectEndpoint() + "/login/oauth/authorize" +
                "?client_id=" + openIdConnectProperties.getOpenIdConnectClientId() +
                "&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&scope=openid profile email";
        response.sendRedirect(authorizeUrl);
    }

    @GetMapping("/callback")
    public void callback(@RequestParam String code, HttpServletResponse response) throws Exception {
        System.out.println("Got code: " + code);

        RestTemplate restTemplate = SSLUtils.getRestTemplateWithoutSSL();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> jsonBody = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "client_id", openIdConnectProperties.getOpenIdConnectClientId(),
                "client_secret", openIdConnectProperties.getOpenIdConnectClientSecret(),
                "redirect_uri", redirectUri
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                openIdConnectProperties.getOpenIdConnectEndpoint() + "/api/login/oauth/access_token",
                request,
                Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Set-Cookie", "access_token=" + accessToken + "; Path=/");

        response.sendRedirect("/");
    }
}