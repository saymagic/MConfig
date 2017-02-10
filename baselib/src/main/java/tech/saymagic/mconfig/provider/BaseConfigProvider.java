package tech.saymagic.mconfig.provider;

import tech.saymagic.mconfig.log.ILogger;

/**
 * Created by saymagic on 2017/2/8.
 */

public abstract class BaseConfigProvider<K, V> implements IConfigProvider<K, V>{

    protected int mPriority;

    protected ILogger mLogger = ILogger.DEFAULT;

    public BaseConfigProvider(int priority) {
        mPriority = priority;
    }

    public BaseConfigProvider() {}

    @Override
    public void init(ILogger logger) {
        this.mLogger = logger == null ? ILogger.DEFAULT :logger;
    }

    @Override
    public V getProperty(K o) {
        return null;
    }

    @Override
    public int getPriority() {
        return this.mPriority;
    }

    @Override
    public int compareTo(IConfigProvider configProvider) {
        return configProvider.getPriority() - this.getPriority();
    }
}
