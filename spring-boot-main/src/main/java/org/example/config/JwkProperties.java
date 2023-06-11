package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("jwk")
public class JwkProperties {
    private int connectTimeout;
    private int readTimeout;
    private String url;
}
