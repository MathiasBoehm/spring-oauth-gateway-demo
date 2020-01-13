package de.struktuhr.springgatewaydemo.token;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(Throwable cause) {
        super(cause);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
