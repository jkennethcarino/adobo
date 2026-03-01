package okhttp3;

import java.io.Closeable;
import java.io.IOException;

public final class Response implements Closeable {

    public ResponseBody body() {
        throw new UnsupportedOperationException("Stub");
    }

    public Builder newBuilder() {
        throw new UnsupportedOperationException("Stub");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Stub");
    }

    public static class Builder {

        public Builder() {
            throw new UnsupportedOperationException("Stub");
        }

        public Builder body(ResponseBody body) {
            throw new UnsupportedOperationException("Stub");
        }

        public Response build() {
            throw new UnsupportedOperationException("Stub");
        }
    }
}
