package com.alexm.bearspendings;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author AlexM
 * Date: 4/20/20
 **/
@Slf4j
@SpringBootApplication
public class ImportExportApplication implements CommandLineRunner {

    public static void main(String[] args) {
        log.info("Starting import/export application ...");
        SpringApplication.run(ImportExportApplication.class);
        log.info("Application started.");
    }

    @Override
    public void run(String... args) throws Exception {
        // app here
    }
}
