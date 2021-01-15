package com.alexm.bearspendings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class BearSpendingsApplication {
    public static final  String API_URL = "/api/v1/";
    public static void main(String[] args) {
        SpringApplication.run(BearSpendingsApplication.class, args);
    }
}
