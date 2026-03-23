package com.unirun.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RunTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunTrackerApplication.class, args);
    }
}
