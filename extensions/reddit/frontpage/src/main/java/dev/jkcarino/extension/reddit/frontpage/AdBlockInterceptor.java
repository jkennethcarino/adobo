package dev.jkcarino.extension.reddit.frontpage;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

@SuppressWarnings("unused")
public final class AdBlockInterceptor implements Interceptor {

    private static AdBlockInterceptor instance;

    private AdBlockInterceptor() {
    }

    public static AdBlockInterceptor getInstance() {
        if (instance == null) {
            instance = new AdBlockInterceptor();
        }
        return instance;
    }

    public void inject(List<Interceptor> interceptors) {
        if (!interceptors.contains(this)) {
            interceptors.add(this);
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = request.url().host();

        if (AdBlocker.isRequestBlocked(request)) {
            throw new ConnectException("Failed to connect to " + host);
        }

        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();

        MediaType contentType = responseBody != null ? responseBody.contentType() : null;
        if (contentType == null || !contentType.subtype().equalsIgnoreCase("json")) {
            return response;
        }

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.getBuffer().clone();

        Charset charset;
        try {
            charset = contentType.charset(StandardCharsets.UTF_8);
        } catch (UnsupportedCharsetException e) {
            return response;
        }

        String responseBodyJson = buffer.readString(charset);
        String modifiedBodyString = AdBlocker.removeAds(responseBodyJson);
        ResponseBody body = ResponseBody.create(contentType, modifiedBodyString);

        return response.newBuilder()
            .body(body)
            .build();
    }
}
