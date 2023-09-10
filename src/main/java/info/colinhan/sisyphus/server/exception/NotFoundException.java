package info.colinhan.sisyphus.server.exception;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {
    public static Supplier<NotFoundException> of(String entityName) {
        return () -> new NotFoundException(entityName + " not found!");
    }
    public NotFoundException() {
        super("Not Found!");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super("Not Found!", cause);
    }
}
