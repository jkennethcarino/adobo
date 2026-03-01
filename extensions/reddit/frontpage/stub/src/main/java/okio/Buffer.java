package okio;

import java.io.IOException;
import java.nio.charset.Charset;

public class Buffer implements BufferedSource, Cloneable {

    @Override
    public Buffer getBuffer() {
        throw new UnsupportedOperationException("Stub");
    }

    @Override
    public boolean request(long byteCount) throws IOException {
        throw new UnsupportedOperationException("Stub");
    }

    @Override
    public String readString(Charset charset) throws IOException {
        throw new UnsupportedOperationException("Stub");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Stub");
    }

    @Override
    public Buffer clone() {
        throw new UnsupportedOperationException("Stub");
    }
}
