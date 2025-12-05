package com.example.versioning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        // Enable header-based versioning using X-API-Version header
        configurer.useRequestHeader("X-API-Version");

        // Add supported versions
        configurer.addSupportedVersions("1.0", "1.1", "2.0");
    }
}
