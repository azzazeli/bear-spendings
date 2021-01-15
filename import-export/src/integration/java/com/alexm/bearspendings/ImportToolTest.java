package com.alexm.bearspendings;

import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

/**
 * @author AlexM
 * Date: 4/30/20
 **/
@ActiveProfiles("import-export")
@Slf4j
@SpringBootTest(classes = ImportExportApplication.class)
class ImportToolTest {
    //todo: run this test without h2
    Path out1;
    Path out2;
    Path out3;
    @Autowired
    Runnable watchFile;
    @Autowired
    Path importPath;
    @Autowired
    WatchService watchService;
    @MockBean
    BillImporter mockImporter;
    final ExecutorService executorService = Executors.newFixedThreadPool(1);
    static final String PROCESSED_FOLDER = "processed";

    @Bean
    ApplicationRunner createFolder(Path importPath) {
        return args -> {
            System.out.println("here");
        };
    }

    @EventListener
    public void handleContextRefreshEvent(ContextStartedEvent ctxStartEvt) {
        System.out.println("Context Start Event received.");
    }

    @BeforeEach
    void setup() throws IOException {
        out1 = Paths.get(importPath.toString(), "test.xlsm");
        out2 = Paths.get(importPath.toString(), "test2.xlsm");
        out3 = Paths.get(importPath.toString(), "test3.xlsm");
        Files.deleteIfExists(out1);
        Files.deleteIfExists(out2);
        Files.deleteIfExists(out3);
        final Path processed = Paths.get(out1.getParent().toString(), PROCESSED_FOLDER);
        if (Files.notExists(processed)) {
            Files.createDirectory(processed);
        }
        executorService.submit(watchFile);
    }

    @AfterEach
    void cleanUp() throws IOException {
        executorService.shutdown();
//        FileSystemUtils.deleteRecursively(Paths.get(importPath.toString(), PROCESSED_FOLDER));
    }

    @Test
    void watchImportPathFolder() throws ImportsException, InterruptedException, IOException {
        final long existingProcessedFiles = getProcessedFilesNumber();
        copyTestFile(out1.toFile(), 1);
        Thread.sleep(2000L);

        ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
        verify(mockImporter).imports(pathCaptor.capture());
        assertThat(pathCaptor.getValue().toString().equals(out1.toString()));
        assertThat(Files.notExists(out1));
        assertThat(getProcessedFilesNumber()).isGreaterThan(existingProcessedFiles);
    }

    private long getProcessedFilesNumber() throws IOException {
        final Path processed = Paths.get(importPath.toString(), PROCESSED_FOLDER);
        return Files.notExists(processed) ? 0 : Files.list(processed).count();
    }

    private void copyTestFile(File out, int afterSec) {
        new Thread( () -> {
            try {
                Thread.sleep(afterSec * 1000L);
                File file = ResourceUtils.getFile("classpath:import_sample.xlsm");
                FileCopyUtils.copy(file, out);
            } catch (IOException e) {
                // todo: use @SneakyThrows
                throw new RuntimeException(e); //NOSONAR
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }


    @Test
    void importerThrowsException() throws ImportsException, FileNotFoundException, InterruptedException {
        AtomicBoolean firstCall = new AtomicBoolean(true);
        doAnswer(invocation -> {
            if(firstCall.getAndSet(false)) {
                throw new ImportsException("e");
            }
            return null;
        }).when(mockImporter).imports(any(Path.class));
        copyTestFile(out2.toFile(), 1);
        copyTestFile(out3.toFile(), 2);
        Thread.sleep(3000L);
        verify(mockImporter).imports(out3);
    }

}
