package com.alexm.bearspendings.service;

/**
 * @author AlexM
 * Date: 8/16/19
 **/
public class ServiceException extends RuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }
}
