package tech.saymagic.mconfig.demo.config;

import android.content.Context;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

import tech.saymagic.mconfig.MConfig;
import tech.saymagic.mconfig.androidlib.AndroidConfigLogger;
import tech.saymagic.mconfig.androidlib.SPCachedHttpConfigProvider;
import tech.saymagic.mconfig.demo.BuildConfig;
import tech.saymagic.mconfig.parsing.map.PropertiesParser;
import tech.saymagic.mconfig.provider.BaseConfigProvider;
import tech.saymagic.mconfig.provider.IConfigProvider;
import tech.saymagic.mconfig.provider.map.MapConfigProvider;
import tech.saymagic.mconfig.provider.misc.StaticFieldConfigProvider;

/**
 * Created by caoyanming on 2017/2/9.
 */
public class ConfigManager {

    private static final int DEFAULT = 0;

    private static ConfigManager sInstance;

    public static ConfigManager getInstance(Context... contexts) {
        if (sInstance == null) {
            synchronized (ConfigManager.class) {
                if (contexts == null || contexts.length < 1) {
                    throw new IllegalStateException("context must be set when init instance");
                }
                sInstance = new ConfigManager(contexts[0]);
            }
        }
        return sInstance;
    }

    private SparseArray<MConfig> mConfigs;

    private ConfigManager(Context context) {
        mConfigs = new SparseArray<>();
        mConfigs.put(DEFAULT, generateDefaultConfig(context));
    }

    private MConfig generateDefaultConfig(Context context) {
        Map<String, String> defaultMap = new HashMap<String, String>(){{
            put("one", "Mercury");
            put("two", "Venus");
            put("three", "Earth");
            put("four", "Mars");
            put("five", "Jupiter");
            put("fix", "Saturn");
            put("seven", "Uranus");
            put("eight", "Neptune");
        }};

        IConfigProvider<String, String> defaultProvider = new MapConfigProvider<String, String>(defaultMap, 0);

        Map<String, String> localMap = new HashMap<String, String>(){{
            put("four", "Local_Mars");
            put("five", "Local_Jupiter");
            put("six", "Local_Saturn");
        }};

        IConfigProvider<String, String> localProvider = new MapConfigProvider<String, String>(localMap, 1);

        MapConfigProvider<String, String> serverProvider = SPCachedHttpConfigProvider.getBuilder()
                .setContext(context)
                .setSpName("MConfig")
                .setMapParser(new PropertiesParser())
                .setUrl("http://static.saymagic.tech/server.properties")
                .setExector(MConfig.DEFAULT_EXECTOR)
                .setPriority(2).build();

        BaseConfigProvider<String, String> staticProvider = new StaticFieldConfigProvider<String>(BuildConfig.class, 3);

        MConfig<String, String> config = new MConfig<>(new AndroidConfigLogger());
        config.init(defaultProvider, localProvider, serverProvider, staticProvider);
        return config;
    }

    public MConfig<String, String> getDefault() {
        return mConfigs.get(DEFAULT);
    }

    public void registerConfig(int type, MConfig config) {
        mConfigs.put(type, config);
    }

    public <K,V> MConfig<K, V> getConfig(int type) {
        return mConfigs.get(type);
    }

}
