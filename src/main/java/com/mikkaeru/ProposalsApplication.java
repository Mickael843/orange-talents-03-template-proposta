package com.mikkaeru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProposalsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProposalsApplication.class, args);
    }
}
