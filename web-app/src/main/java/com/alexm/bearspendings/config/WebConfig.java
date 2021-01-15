package com.alexm.bearspendings.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author AlexM
 * Date: 4/13/20
 **/
@Profile("devdev")
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String ALLOWED_ORIGIN = "http://localhost:4200";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(ALLOWED_ORIGIN);
    }
}
