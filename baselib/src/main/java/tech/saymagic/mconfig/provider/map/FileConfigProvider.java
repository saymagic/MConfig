package tech.saymagic.mconfig.provider.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;

import tech.saymagic.mconfig.log.ILogger;
import tech.saymagic.mconfig.parsing.map.IMapParser;
import tech.saymagic.mconfig.persistence.IPersistence;

/**
 * Created by saymagic on 2017/2/8.
 */

public class FileConfigProvider extends MapConfigProvider<String, String> implements IPersistence<Map<String, String>> {

    private static final String TAG = "FileConfigProvider";

    private File mFile;

    private IMapParser<InputStream, String, String> mMapParser;

    /**
     * if mExecutor is null, fetch function will happened in calling thread.
     */
    private Executor mExecutor;

    public FileConfigProvider(File source,
                              IMapParser<InputStream, String, String> mapParser,
                              Executor exector,
                              int priority) {
        super(Collections.<String, String>emptyMap(), priority);
        this.mFile = source;
        this.mMapParser = mapParser;
        this.mExecutor = exector;
    }

    @Override
    public void init(ILogger logger) {
        super.init(logger);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = getSource();
                if (map != null && !map.isEmpty()) {
                    resetSource(map);
                }
            }
        };

        if (mExecutor == null) {
            task.run();
        } else {
            mExecutor.execute(task);
        }
    }

    @Override
    public void saveSource(Map<String, String> stringStringMap) {
        //do nothing
    }

    @Override
    public Map<String, String> getSource() {
        Map<String, String> map = Collections.emptyMap();
        if (mFile == null || mMapParser == null) {
            return map;
        }
        try {
            map = mMapParser.parse(new FileInputStream(mFile));
        } catch (IOException e) {
            mLogger.e(TAG, e);
        }
        return map;
    }

    public static FileConfigProviderBuilder builder() {
        return new FileConfigProviderBuilder();
    }

    public static class FileConfigProviderBuilder {
        private File mSource;
        private IMapParser<InputStream, String, String> mMapParser;
        private Executor mExector;
        private int mPriority;

        public FileConfigProviderBuilder setSource(File source) {
            mSource = source;
            return this;
        }

        public FileConfigProviderBuilder setMapParser(IMapParser<InputStream, String, String> mapParser) {
            mMapParser = mapParser;
            return this;
        }

        public FileConfigProviderBuilder setExector(Executor exector) {
            mExector = exector;
            return this;
        }

        public FileConfigProviderBuilder setPriority(int priority) {
            mPriority = priority;
            return this;
        }

        public FileConfigProvider build() {
            return new FileConfigProvider(mSource, mMapParser, mExector, mPriority);
        }
    }
}
