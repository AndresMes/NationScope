package com.example.nationscope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NationScopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NationScopeApplication.class, args);
    }

}
