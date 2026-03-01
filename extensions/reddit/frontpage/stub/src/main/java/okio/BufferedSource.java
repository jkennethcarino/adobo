package okio;

import java.io.IOException;
import java.nio.charset.Charset;

public interface BufferedSource extends Source {

    Buffer getBuffer();

    boolean request(long byteCount) throws IOException;

    String readString(Charset charset) throws IOException;
}
