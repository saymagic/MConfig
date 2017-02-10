package tech.saymagic.mconfig.provider.map;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;

import tech.saymagic.mconfig.log.ILogger;
import tech.saymagic.mconfig.parsing.map.IMapParser;
import tech.saymagic.mconfig.parsing.map.PropertiesParser;
import tech.saymagic.mconfig.persistence.IPersistence;

/**
 * Created by saymagic on 2017/2/8.
 */

public class HttpConfigProvider extends MapConfigProvider<String, String> implements IPersistence<Map<String, String>> {

    private static final String TAG = "HttpConfigProvider";

    private String mUrl;

    private String mRequestMethod;

    private Map<String, String> mHeaders;

    private IMapParser<InputStream, String, String> mMapParser;

    /**
     * if mExecutor is null, fetch function will happened in calling thread.
     */
    private Executor mExecutor;

    protected HttpConfigProvider(String url,
                       String requestMethod,
                       Map<String, String> headers,
                       IMapParser<InputStream, String, String> mapParser,
                       Executor exector,
                        int priority) {
        super(Collections.<String, String>emptyMap(), priority);
        this.mUrl = url;
        this.mRequestMethod = requestMethod == null || requestMethod.length() == 0 ? "GET" : requestMethod;
        this.mHeaders = headers;
        this.mMapParser = mapParser == null ? new PropertiesParser() : mapParser;
        this.mExecutor = exector;
    }

    @Override
    public void init(ILogger logger) {
        super.init(logger);
        Map<String, String> cache = getSource();
        if (cache != null) {
            resetSource(cache);
        }
        Runnable fetchTask = new FetchNewConfigTask();
        if (mExecutor == null) {
            fetchTask.run();
        } else {
            mExecutor.execute(fetchTask);
        }
    }

    public void tryToFetchNewSource() throws IOException {
        URL url = new URL(mUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(mRequestMethod);
        if (null != mHeaders && mHeaders.size() > 0) {
            for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.connect();
        int code = connection.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            Map<String, String> map = null;
            try {
                map = mMapParser.parse(inputStream);
            } catch (IOException e) {
                mLogger.e(TAG, e);
            }
            if (map != null) {
                resetSource(map);
                saveSource(map);
            }
        } else {
            mLogger.i(TAG, String.format("fetch new config failed : %d", code));
        }
    }

    @Override
    public String getProperty(String o) {
        return super.getProperty(o);
    }

    @Override
    public void saveSource(Map<String, String> source) {

    }

    @Override
    public Map<String, String> getSource() {
        return null;
    }

    class FetchNewConfigTask implements Runnable {

        @Override
        public void run() {
            try {
                tryToFetchNewSource();
            } catch (IOException e) {
                mLogger.e(TAG, e);
            }
        }

    }

    public static HttpConfigProviderBuilder builder() {
        return new HttpConfigProviderBuilder();
    }

    public static class HttpConfigProviderBuilder {
        private String mUrl;
        private Map<String, String> mHeaders;
        private String mRequestMethod;
        private IMapParser<InputStream, String, String> mMapParser;
        private Executor mExector;
        private int mPriority;

        public HttpConfigProviderBuilder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public HttpConfigProviderBuilder setHeaders(Map<String, String> headers) {
            mHeaders = headers;
            return this;
        }

        public HttpConfigProviderBuilder setMapParser(IMapParser<InputStream, String, String> mapParser) {
            mMapParser = mapParser;
            return this;
        }

        public HttpConfigProviderBuilder setExector(Executor exector) {
            mExector = exector;
            return this;
        }

        public HttpConfigProviderBuilder setRequestMethod(String requestMethod) {
            mRequestMethod = requestMethod;
            return this;
        }

        public HttpConfigProviderBuilder setPriority(int priority) {
            mPriority = priority;
            return this;
        }

        public HttpConfigProvider build() {
            return new HttpConfigProvider(mUrl, mRequestMethod, mHeaders, mMapParser, mExector, mPriority);
        }
    }
}
