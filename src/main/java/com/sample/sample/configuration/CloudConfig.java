package com.sample.sample.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudConfig {

    @Value("${cloudinary.name}")
    private String cloudName;

    @Value("${cloudinary.api.key}")
    private String cloudinaryKey;

    @Value("${cloudinary.api.secret}")
    private String cloudinarySecret;


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudinaryKey,
                "api_secret",cloudinarySecret,
                "secure", true
        ));
    }

}
