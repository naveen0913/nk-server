package com.sample.sample.configuration;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadsPath = "file:" + System.getProperty("user.dir") + "/uploads/";

        registry.addResourceHandler("/uploads/")
                .addResourceLocations(uploadsPath)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        System.out.println("Serving static files from: " + uploadsPath);
}
}