package com.revolut.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception of RevolutTransfer
 * @author Kanat K.B.
 */
public class RevolutException extends Exception{
    private final Logger LOG = LoggerFactory.getLogger(RevolutException.class);

    public RevolutException() {  super(); }

    public RevolutException(String message) {
        super(message);
        LOG.error(message);
    }

    public RevolutException(Throwable cause) { super(cause);  }


}
