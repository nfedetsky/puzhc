package com.softline.csrv.exception.external.pupe;


/**
 * Искючение клиента Pupe
 */
public class PupeClientException extends RuntimeException {

private  final  String message;
    public PupeClientException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

