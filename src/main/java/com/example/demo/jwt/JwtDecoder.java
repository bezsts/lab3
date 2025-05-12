package com.example.demo.jwt;

import com.example.demo.openidconnect.OpenIdConnectProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Component
public class JwtDecoder {
    private final OpenIdConnectProperties openIdConnectProperties;

    @Autowired
    public JwtDecoder(OpenIdConnectProperties openIdConnectProperties) {
        this.openIdConnectProperties = openIdConnectProperties;
    }

    public Claims decodeToken(String token) {
        try {
            JWKSet jwkSet = JWKSet.load(new URL(openIdConnectProperties.getOpenIdConnectEndpoint()
                    + "/.well-known/jwks"));

            List<JWK> keys = jwkSet.getKeys();
            if (keys.isEmpty()) {
                throw new RuntimeException("No keys found in JWK Set");
            }

            JWK jwk = keys.get(0);
            RSAPublicKey publickey = (RSAPublicKey) jwk.toRSAKey().toPublicKey();

            return Jwts.parserBuilder()
                    .setSigningKey(publickey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode token", e);
        }
    }
}