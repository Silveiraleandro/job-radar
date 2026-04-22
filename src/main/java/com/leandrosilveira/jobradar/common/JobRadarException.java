package com.leandrosilveira.jobradar.common;

public class JobRadarException extends RuntimeException {
    public JobRadarException(String message) {
        super(message);
    }

    public JobRadarException(String message, Throwable err) {
        super(message, err);
    }
}
