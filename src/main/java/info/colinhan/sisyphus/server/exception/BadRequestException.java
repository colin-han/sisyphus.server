package info.colinhan.sisyphus.server.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Bad request!");
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super("Bad request!", cause);
    }
}
