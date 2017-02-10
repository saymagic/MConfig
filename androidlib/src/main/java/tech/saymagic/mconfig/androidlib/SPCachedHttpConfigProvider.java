package tech.saymagic.mconfig.androidlib;


import android.content.Context;
import android.content.SharedPreferences;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import tech.saymagic.mconfig.log.ILogger;
import tech.saymagic.mconfig.parsing.map.IMapParser;
import tech.saymagic.mconfig.provider.map.HttpConfigProvider;

/**
 * Created by saymagic on 2017/2/8.
 */

public class SPCachedHttpConfigProvider extends HttpConfigProvider {

    private String mName;

    private Context mContext;

    private SharedPreferences mSP = null;

    private Object mLock = new Object();

    SPCachedHttpConfigProvider(Context context,
                               String spName,
                               String url,
                               String requestMethod,
                               Map<String, String> headers,
                               IMapParser<InputStream, String, String> mapParser,
                               Executor exector,
                               int priority) {
        super(url, requestMethod, headers, mapParser, exector, priority);
        this.mContext = context.getApplicationContext();
        this.mName = spName;
        this.mSP = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
    }

    @Override
    public void init(ILogger logger) {
        super.init(logger);
    }

    @Override
    public Map<String, String> getSource() {
        if (mSP == null) {
            return Collections.emptyMap();
        }
        synchronized (mLock) {
            Map<String, String> ret = new HashMap<>();
            Map<String, ?> source = new HashMap<>(mSP.getAll());
            for (Map.Entry<String, ?> entry : source.entrySet()) {
                ret.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            return ret;
        }
    }

    @Override
    public void saveSource(Map<String, String> source) {
        if (mSP == null || source == null) {
            return;
        }
        synchronized (mLock) {
            SharedPreferences.Editor editor = mSP.edit();
            editor.clear();
            source = new HashMap<>(source);
            for (Map.Entry<String, String> entry : source.entrySet()) {
                editor.putString(entry.getKey(), entry.getValue());
            }
            editor.apply();
        }
    }

    public static SPCachedHttpConfigProviderBuilder getBuilder() {
        return new SPCachedHttpConfigProviderBuilder();
    }

    public static class SPCachedHttpConfigProviderBuilder {
        private Context mContext;
        private String mSpName;
        private String mUrl;
        private String mRequestMethod;
        private Map<String, String> mHeaders;
        private IMapParser<InputStream, String, String> mMapParser;
        private Executor mExector;
        private int mPriority;

        public SPCachedHttpConfigProviderBuilder setContext(Context context) {
            mContext = context;
            return this;
        }

        public SPCachedHttpConfigProviderBuilder setSpName(String spName) {
            mSpName = spName;
            return this;
        }

        public SPCachedHttpConfigProviderBuilder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public SPCachedHttpConfigProviderBuilder setRequestMethod(String requestMethod) {
            mRequestMethod = requestMethod;
            return this;
        }

        public SPCachedHttpConfigProviderBuilder setHeaders(Map<String, String> headers) {
            mHeaders = headers;
            return this;
        }

        public SPCachedHttpConfigProviderBuilder setMapParser(IMapParser<InputStream, String, String> mapParser) {
            mMapParser = mapParser;
            return this;
        }

        public SPCachedHttpConfigProviderBuilder setExector(Executor exector) {
            mExector = exector;
            return this;
        }

        public SPCachedHttpConfigProviderBuilder setPriority(int priority) {
            mPriority = priority;
            return this;
        }

        public SPCachedHttpConfigProvider build() {
            return new SPCachedHttpConfigProvider(mContext, mSpName, mUrl, mRequestMethod, mHeaders, mMapParser, mExector, mPriority);
        }
    }
}
