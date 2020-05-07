package com.alexm.bearspendings;

import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class ImportExportApplication {

    @Value("${com.alexm.bearspendings.imports.importpath}")
    private String importPath;

    public static void main(String[] args) {
        log.info("Starting import/export application ...");
        final ConfigurableApplicationContext run = SpringApplication.run(ImportExportApplication.class);
        final Runnable watchFile = run.getBean("watchFile", Runnable.class);
        watchFile.run();
        log.info("Application started.");
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
    Runnable watchFile(WatchService watchService, BillImporter importer, Path importPath) {
        return () -> {
            try (watchService) {
                WatchKey key;
                log.info("Waiting for files in path:{}",importPath.toAbsolutePath());
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        log.info("Event kind:{}. File affected:{} ",event.kind(), event.context());
                        processInputFile(importer, event);
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

    private void processInputFile(BillImporter importer, WatchEvent<?> event)  {
        try {
            importer.imports(importPath().resolve(event.context().toString()));
        } catch (ImportsException e) {
            log.error("Exception occurred during import of file with bills.", e);
        } catch (IOException e) {
            log.warn("Exception on access watch service", e);
        }
    }

}




