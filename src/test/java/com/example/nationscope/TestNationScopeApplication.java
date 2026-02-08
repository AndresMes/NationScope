package com.example.nationscope;

import org.springframework.boot.SpringApplication;

public class TestNationScopeApplication {

    public static void main(String[] args) {
        SpringApplication.from(NationScopeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
