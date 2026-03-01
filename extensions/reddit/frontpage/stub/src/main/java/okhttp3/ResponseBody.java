package okhttp3;

import okio.BufferedSource;

import java.io.Closeable;

public abstract class ResponseBody implements Closeable {

    public static ResponseBody create(MediaType contentType, String content) {
        throw new UnsupportedOperationException("Stub");
    }

    public abstract MediaType contentType();

    public abstract BufferedSource source();
}
