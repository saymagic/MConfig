package tech.saymagic.mconfig.provider;

import tech.saymagic.mconfig.log.ILogger;

/**
 * Created by saymagic on 2017/2/8.
 */

public interface IConfigProvider<K, V> extends Comparable<IConfigProvider<K, V>>{

    void init(ILogger logger);

    V getProperty(K k);

    int getPriority();

}
