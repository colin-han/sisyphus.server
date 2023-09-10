package info.colinhan.sisyphus.server.exception;

import java.util.Optional;
import java.util.function.Supplier;

public class E {
    public static Supplier<NotFoundException> notFound() {
        return NotFoundException::new;
    }

    public static Supplier<NotFoundException> notFoundOf(String entityName) {
        return () -> new NotFoundException(entityName + " not found!");
    }

    public static Supplier<BadRequestException> badRequest() {
        return BadRequestException::new;
    }

    public static <T> T assertPresent(Optional<T> optional) {
        return optional.orElseThrow(E.notFound());
    }
    public static <T> T assertPresent(Optional<T> optional, String entityName) {
        return optional.orElseThrow(E.notFoundOf(entityName));
    }
}
