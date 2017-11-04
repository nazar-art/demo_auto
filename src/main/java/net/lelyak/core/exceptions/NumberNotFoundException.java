package net.lelyak.core.exceptions;

public class NumberNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7012706090455278128L;

    public NumberNotFoundException() {
    }

    public NumberNotFoundException(String message) {
        super(message);
    }

    public NumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NumberNotFoundException(Throwable cause) {
        super(cause);
    }
}
