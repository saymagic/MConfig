package tech.saymagic.mconfig.provider.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import tech.saymagic.mconfig.provider.BaseConfigProvider;

/**
 * Created by saymagic on 2017/2/8.
 */

public class MapConfigProvider<K, V> extends BaseConfigProvider<K, V> {

    private Map<K, V> mSource;

    public MapConfigProvider(Map<K, V> source) {
        this(source, 0);
    }

    public MapConfigProvider(Map<K, V> source, int priority) {
        super(priority);
        mSource = source == null ? Collections.<K, V>emptyMap() : new ConcurrentHashMap<>(source);
    }

    protected void resetSource(Map<K, V> source) {
        mSource = source == null ? Collections.<K, V>emptyMap() : new ConcurrentHashMap<>(source);
    }

    @Override
    public V getProperty(K k) {
        return mSource.get(k);
    }

}
