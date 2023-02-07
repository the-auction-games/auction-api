package com.theauctiongames.auctionapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The spring configuration class.
 */
@Configuration
public class SpringConfig implements WebMvcConfigurer {

    /**
     * Add CORS mapping to all endpoints.
     *
     * @param registry the registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
