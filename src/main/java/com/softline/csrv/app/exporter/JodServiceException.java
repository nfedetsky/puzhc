package com.softline.csrv.app.exporter;


/**
 * Исключение сервиса конвертации в ODF
 */
public class JodServiceException extends RuntimeException {

    private final String message;

    public JodServiceException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}