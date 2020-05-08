package com.alexm.bearspendings.imports;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author AlexM
 * Date: 5/8/20
 **/
@Data
@ConfigurationProperties(prefix = "com.alexm.bearspendings.imports")
@Configuration
public class ImportsConfig {
    Integer billsBatchSize;
    String importPath;
}
