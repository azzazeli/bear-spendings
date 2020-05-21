package com.alexm.bearspendings.imports;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
public class ImportsException extends RuntimeException {
    public ImportsException(String message) {
        super(message);
    }

    public ImportsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportsException(Throwable cause) {
        super(cause);
    }
}
