package com.alexm.bearspendings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.*;

/**
 * @author AlexM
 * Date: 4/20/20
 **/
@Slf4j
@SpringBootApplication
public class ImportExportApplication implements CommandLineRunner {

    @Value("${com.alexm.bearspendings.imports.importpath}")
    private String importPath;

    public static void main(String[] args) {
        log.info("Starting import/export application ...");
        final ConfigurableApplicationContext run = SpringApplication.run(ImportExportApplication.class);
        final Runnable watchFile = run.getBean("watchFile", Runnable.class);
        watchFile.run();
        log.info("Application started.");
    }

    @Override
    public void run(String... args) throws Exception {
        // app here
    }

    @Bean
    Path importPath() throws IOException {
        Path path = Paths.get(importPath);
        if (Files.exists(path)) {
            return path;
        }
        return Files.createDirectory(path);
    }

    @Bean
    public WatchService watchService(Path importPath) throws IOException {
        final WatchService watchService = FileSystems.getDefault().newWatchService();
        importPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        return watchService;
    }

    @Bean
    Runnable watchFile(WatchService watchService) {
        return () -> {
            try {
                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                    }
                    key.reset();
                }
            } catch (InterruptedException e) {
                log.warn("Interrupted!", e);
                Thread.currentThread().interrupt();
            }
        };
    }
}
