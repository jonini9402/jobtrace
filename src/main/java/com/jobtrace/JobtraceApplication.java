package com.jobtrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JobtraceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobtraceApplication.class, args);
    }

}
