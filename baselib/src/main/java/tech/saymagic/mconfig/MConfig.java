package tech.saymagic.mconfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import tech.saymagic.mconfig.log.ILogger;
import tech.saymagic.mconfig.provider.IConfigProvider;

public class MConfig<K, V> {

    private ILogger mLogger;

    private List<IConfigProvider<K, V>> mConfigProviders;

    public static final Executor DEFAULT_EXECTOR = Executors.newSingleThreadExecutor();

    public MConfig() {
        this(null);
    }

    public MConfig(ILogger logger) {
        this.mLogger = logger == null ? ILogger.DEFAULT : logger;
        mConfigProviders = new ArrayList<>();
    }

    public void init(IConfigProvider<K, V>... configProviders) {
        if (configProviders == null || configProviders.length == 0) {
            return;
        }
        for (IConfigProvider<K, V> configProvider : configProviders) {
           affixionConfig(configProvider);
        }
        Collections.sort(mConfigProviders);
    }

    private void affixionConfig(IConfigProvider<K, V> configProvider) {
        configProvider.init(mLogger);
        mConfigProviders.add(configProvider);
    }

    public V getProperty(K k) {
        for (IConfigProvider<K, V> configProvider : mConfigProviders) {
            V v = configProvider.getProperty(k);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

}
