package tech.saymagic.mconfig.androidlib;


import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import tech.saymagic.mconfig.MConfig;
import tech.saymagic.mconfig.parsing.map.PropertiesParser;
import tech.saymagic.mconfig.provider.IConfigProvider;
import tech.saymagic.mconfig.provider.map.MapConfigProvider;

import static org.junit.Assert.assertEquals;

/**
 * Created by saymagic on 2017/2/8.
 */
@RunWith(AndroidJUnit4.class)
public class SPCachedHttpConfigProviderTest  {

    private Context mContext;

    private String mName;

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        mName = "MConfig";
        mContext = instrumentation.getTargetContext().getApplicationContext();
        SharedPreferences.Editor editor = mContext.getApplicationContext()
                .getSharedPreferences(mName, Context.MODE_PRIVATE)
                .edit();
        editor.clear();
        editor.commit();
    }

    @Test
    public void spCachedHttpConfigProviderTest() {
        Map<String, String> defaultMap = new HashMap<String, String>(){{
            put("1", "Mercury");
            put("2", "Venus");
            put("3", "Earth");
            put("4", "Mars");
            put("5", "Jupiter");
            put("6", "Saturn");
            put("7", "Uranus");
            put("8", "Neptune");
        }};

        IConfigProvider<String, String> defaultProvider = new MapConfigProvider<String, String>(defaultMap, 0);

        Map<String, String> localMap = new HashMap<String, String>(){{
            put("4", "Local_Mars");
            put("5", "Local_Jupiter");
            put("6", "Local_Saturn");
        }};

        IConfigProvider<String, String> localProvider = new MapConfigProvider<String, String>(localMap, 1);

        MapConfigProvider<String, String> serverProvider = SPCachedHttpConfigProvider.getBuilder()
                .setContext(mContext)
                .setSpName("MConfig")
                .setMapParser(new PropertiesParser())
                .setUrl("http://static.saymagic.tech/server.properties")
                .setExector(MConfig.DEFAULT_EXECTOR)
                .setPriority(2).build();

        MConfig<String, String> config = new MConfig<>();
        config.init(defaultProvider, localProvider, serverProvider);

        assertEquals("Mercury", config.getProperty("1"));
        assertEquals("Venus", config.getProperty("2"));
        assertEquals("Earth", config.getProperty("3"));
        assertEquals("Local_Mars", config.getProperty("4"));
        assertEquals("Local_Jupiter", config.getProperty("5"));
        assertEquals("Local_Saturn", config.getProperty("6"));
        assertEquals("Uranus", config.getProperty("7"));
        assertEquals("Neptune", config.getProperty("8"));
        assertEquals(null, config.getProperty("9"));

        //waiting for http response
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Server_Mercury", config.getProperty("1"));
        assertEquals("Server_Venus", config.getProperty("2"));
        assertEquals("Earth", config.getProperty("3"));
        assertEquals("Local_Mars", config.getProperty("4"));
        assertEquals("Local_Jupiter", config.getProperty("5"));
        assertEquals("Local_Saturn", config.getProperty("6"));
        assertEquals("Uranus", config.getProperty("7"));
        assertEquals("Neptune", config.getProperty("8"));
        assertEquals(null, config.getProperty("9"));

        MapConfigProvider<String, String> serverProviderV2 = SPCachedHttpConfigProvider.getBuilder()
                .setContext(mContext)
                .setSpName(mName)
                .setMapParser(new PropertiesParser())
                .setUrl("http://static.saymagic.tech/server.properties")
                .setExector(MConfig.DEFAULT_EXECTOR)
                .setPriority(2).build();

        MConfig<String, String> configV2 = new MConfig<>();
        config.init(defaultProvider, localProvider, serverProviderV2);

        assertEquals("Server_Mercury", config.getProperty("1"));
        assertEquals("Server_Venus", config.getProperty("2"));
        assertEquals("Earth", config.getProperty("3"));
        assertEquals("Local_Mars", config.getProperty("4"));
        assertEquals("Local_Jupiter", config.getProperty("5"));
        assertEquals("Local_Saturn", config.getProperty("6"));
        assertEquals("Uranus", config.getProperty("7"));
        assertEquals("Neptunee", config.getProperty("8"));
        assertEquals(null, config.getProperty("9"));
    }
}