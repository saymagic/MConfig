package tech.saymagic.mconfig.provider.misc;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import tech.saymagic.mconfig.log.ILogger;
import tech.saymagic.mconfig.provider.BaseConfigProvider;

public class StaticFieldConfigProvider<V> extends BaseConfigProvider<String, V> {

    private static final String TAG = "BuildFieldConfigProvider";

    private Map<String, V> mCahce;

    private Class mHostClass;

    public StaticFieldConfigProvider(Class hostClass) {
        this.mHostClass = hostClass;
    }

    public StaticFieldConfigProvider(Class hostClass, int priority) {
        super(priority);
        this.mHostClass = hostClass;
    }

    @Override
    public void init(ILogger logger) {
        super.init(logger);
        mCahce = new ConcurrentHashMap<>();
    }

    @Override
    public V getProperty(String key) {
        if (mCahce.containsKey(key)) {
            return mCahce.get(key);
        }
        Object object = getStaticFieldFromHostClass(key);
        if (object != null) {
            V ret = (V) object;
            mCahce.put(key, ret);
            return ret;
        }
        return null;
    }

    private Object getStaticFieldFromHostClass(String fileName) {
        try {
            Class klass = mHostClass;
            while (klass != null) {
                try {
                    Field field = klass.getDeclaredField(fileName);
                    if (field != null) {
                        field.setAccessible(true);
                        return field.get(null);
                    }
                }catch (Exception ignore){}
                klass = klass.getSuperclass();
            }
        } catch (Throwable ignore) {
            mLogger.e(TAG, String.format("find field %s for class %s failed", fileName, mHostClass == null ? "null" : mHostClass.getSimpleName()));
        }
        return null;
    }
}
