package com.alexm.bearspendings;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author AlexM
 * Date: 4/30/20
 **/
@Slf4j
@SpringBootTest()
public class ImportToolTest {

    @Test
    void watchImportPathFolder() throws IOException {
        log.info("copying a file to import path ");
    }


}
