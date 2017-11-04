package net.lelyak.core.exceptions;

public class XlsDataNotFoundException extends RuntimeException {

    public XlsDataNotFoundException() {
    }

    public XlsDataNotFoundException(String message) {
        super(message);
    }

    public XlsDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public XlsDataNotFoundException(Throwable cause) {
        super(cause);
    }
}
