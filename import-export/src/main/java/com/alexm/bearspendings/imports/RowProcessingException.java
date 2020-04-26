package com.alexm.bearspendings.imports;

/**
 * @author AlexM
 * Date: 4/23/20
 **/
public class RowProcessingException extends Exception {
    public RowProcessingException(Throwable cause) {
        super(cause);
    }

    public RowProcessingException(String message) {
        super(message);
    }

    public RowProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
