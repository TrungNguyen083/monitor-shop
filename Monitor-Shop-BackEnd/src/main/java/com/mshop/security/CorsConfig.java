//package com.mshop.security;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*") // Allow all origins
//                .allowedMethods("*") // Allow all HTTP methods
//                .allowedHeaders("*") // Allow all headers
//                .exposedHeaders("*") // Expose specific headers
//                .allowCredentials(true) // Allow credentials (e.g., cookies)
//                .maxAge(3600); // Max age for preflight requests in seconds
//    }
//}