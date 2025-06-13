package com.sample.sample.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.nio.file.Paths;

@Configuration
public class FileConfig implements WebMvcConfigurer {

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String uploadsPath = "file:" + System.getProperty("user.dir") + "/uploads/";
//
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations(uploadsPath)
//                .setCachePeriod(0)
//                .resourceChain(true)
//                .addResolver(new PathResourceResolver());
//
//        System.out.println("Serving static files from: " + uploadsPath);
//    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}