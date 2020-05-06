package com.alexm.bearspendings;

import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@Slf4j
@SpringBootTest()
public class ImportToolTest {
    //todo: run this test without h2
    Path out1;
    Path out2;
    @Autowired
    Runnable watchFile;
    @Autowired
    Path importPath;
    @Autowired
    WatchService watchService;
    @MockBean
    BillImporter mockImporter;
    final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @BeforeEach
    void setup() throws IOException {
        out1 = Paths.get(importPath.toString(), "test.xlsm");
        out2 = Paths.get(importPath.toString(), "test2.xlsm");
        Files.deleteIfExists(out1);
        Files.deleteIfExists(out2);
        executorService.submit(watchFile);
    }

    @AfterEach
    void cleanUp() {
        executorService.shutdown();
    }

    @Test
    void watchImportPathFolder() throws ImportsException, InterruptedException {
        copyTestFile(out1.toFile(), 1);
        Thread.sleep(2000L);

        ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
        verify(mockImporter).imports(pathCaptor.capture());
        assertThat(pathCaptor.getValue()).endsWith(out1);
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
        copyTestFile(out1.toFile(), 1);
        copyTestFile(out2.toFile(), 2);
        Thread.sleep(3000L);
        verify(mockImporter).imports(out2);
    }

}
