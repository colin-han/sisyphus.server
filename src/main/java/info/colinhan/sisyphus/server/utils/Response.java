package info.colinhan.sisyphus.server.utils;

public class Response<T> {
    private final boolean success;
    private final T data;
    private final String error;

    private Response(boolean success, T data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static Response<?> success() {
        return new Response<>(true, null, null);
    }

    public static <T> Response<T> of(T data) {
        return new Response<>(true, data, null);
    }

    public static <T> Response<T> failure(String error) {
        return new Response<>(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
