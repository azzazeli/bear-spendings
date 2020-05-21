package com.alexm.bearspendings;

import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsConfig;
import com.alexm.bearspendings.imports.ImportsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(ImportsConfig.class)
public class ImportExportApplication {

    public static void main(String[] args) {
        log.info("Starting import/export application ...");
        final ConfigurableApplicationContext run = SpringApplication.run(ImportExportApplication.class);
        final Runnable watchFile = run.getBean("watchFile", Runnable.class);
        watchFile.run();
        log.info("Application started.");
    }

    @Bean
    Path importPath(ImportsConfig importsConfig) throws IOException {
        Path path = Paths.get(importsConfig.getImportPath());
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
    Runnable watchFile(WatchService watchService, ImportsService importsService, BillImporter importer, Path importPath) {
        return () -> {
            try (watchService) {
                WatchKey key;
                log.info("Waiting for files in path:{}",importPath.toAbsolutePath());
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        log.info("Event kind:{}. File affected:{} ",event.kind(), event.context());
                        importsService.importsBills(event.context().toString());
                    }
                    key.reset();
                }
            } catch (InterruptedException e) {
                log.warn("Interrupted!", e);
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                log.warn("Exception on access watch service", e);
            } catch (ClosedWatchServiceException e) {
                log.info("Closed Watch service exception.");
            }
        };
    }


}




