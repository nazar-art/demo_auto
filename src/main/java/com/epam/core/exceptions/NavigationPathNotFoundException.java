package com.epam.core.exceptions;

public class NavigationPathNotFoundException extends RuntimeException {

    public NavigationPathNotFoundException() {
    }

    public NavigationPathNotFoundException(String message) {
        super(message);
    }

    public NavigationPathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NavigationPathNotFoundException(Throwable cause) {
        super(cause);
    }
}
