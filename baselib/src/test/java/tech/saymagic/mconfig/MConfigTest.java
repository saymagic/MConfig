package tech.saymagic.mconfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import tech.saymagic.mconfig.parsing.map.PropertiesParser;
import tech.saymagic.mconfig.provider.BaseConfigProvider;
import tech.saymagic.mconfig.provider.IConfigProvider;
import tech.saymagic.mconfig.provider.map.HttpConfigProvider;
import tech.saymagic.mconfig.provider.map.MapConfigProvider;
import tech.saymagic.mconfig.provider.misc.StaticFieldConfigProvider;

import static org.junit.Assert.*;

/**
 * Created by saymagic.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class MConfigTest {

    @Test
    public void emptyConfigProvidersTest() throws Exception {
        MConfig<String, String> config = new MConfig<>();
        config.init(null);
        assertEquals(null, config.getProperty(null));
        assertEquals(null, config.getProperty("one"));
        assertEquals(null, config.getProperty("three"));
        assertEquals(null, config.getProperty("four"));
    }

    @Test
    public void singletonConfigProviderTest() {
        Map<String, String> single = new HashMap<String, String>(){{
            put("one", "Mercury");
            put("two", "Venus");
            put("three", "Earth");
            put("four", "Mars");
            put("five", "Jupiter");
            put("six", "Saturn");
            put("seven", "Uranus");
            put("eight", "Neptune");
        }};
        IConfigProvider<String, String> provider = new MapConfigProvider<String, String>(single);
        MConfig<String, String> config = new MConfig<>();
        config.init(provider);
        assertEquals("Mercury", config.getProperty("one"));
        assertEquals("Venus", config.getProperty("two"));
        assertEquals("Earth", config.getProperty("three"));
        assertEquals("Mars", config.getProperty("four"));
        assertEquals("Jupiter", config.getProperty("five"));
        assertEquals("Saturn", config.getProperty("six"));
        assertEquals("Uranus", config.getProperty("seven"));
        assertEquals("Neptune", config.getProperty("eight"));
        assertEquals(null, config.getProperty("9"));
    }

    @Test
    public void multipleConfigProviderTest() {

        Map<String, String> defaultMap = new HashMap<String, String>() {{
            put("one", "Mercury");
            put("two", "Venus");
            put("three", "Earth");
            put("four", "Mars");
            put("five", "Jupiter");
            put("six", "Saturn");
            put("seven", "Uranus");
            put("eight", "Neptune");
        }};

        Map<String, String> localMap = new HashMap<String, String>() {{
            put("four", "Local_Mars");
            put("five", "Local_Jupiter");
            put("six", "Local_Saturn");
        }};

        Map<String, String> serverMap = new HashMap<String, String>() {{
            put("one", "Server_Mercury");
            put("two", "Server_Venus");
            put("three", "Server_Earth");
            put("four", "Server_Mars");
        }};

        IConfigProvider<String, String> defaultProvider = new MapConfigProvider<String, String>(defaultMap, 0);
        IConfigProvider<String, String> localProvider = new MapConfigProvider<String, String>(localMap, 1);
        IConfigProvider<String, String> serverProvider = new MapConfigProvider<String, String>(serverMap, 2);

        MConfig<String, String> config = new MConfig<>();
        config.init(defaultProvider, localProvider, serverProvider);

        assertEquals("Server_Mercury", config.getProperty("one"));
        assertEquals("Server_Venus", config.getProperty("two"));
        assertEquals("Server_Earth", config.getProperty("three"));
        assertEquals("Server_Mars", config.getProperty("four"));
        assertEquals("Local_Jupiter", config.getProperty("five"));
        assertEquals("Local_Saturn", config.getProperty("six"));
        assertEquals("Uranus", config.getProperty("seven"));
        assertEquals("Neptune", config.getProperty("eight"));
        assertEquals(null, config.getProperty("9"));
    }

    @Test
    public void serverConfigProviderTest(){

        Map<String, String> defaultMap = new HashMap<String, String>(){{
            put("one", "Mercury");
            put("two", "Venus");
            put("three", "Earth");
            put("four", "Mars");
            put("five", "Jupiter");
            put("six", "Saturn");
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

        MapConfigProvider<String, String> serverProvider = HttpConfigProvider.builder()
                .setMapParser(new PropertiesParser())
                .setUrl("http://static.saymagic.tech/server.properties")
                .setExector(MConfig.DEFAULT_EXECTOR)
                .setPriority(2).build();

        MConfig<String, String> config = new MConfig<>();
        config.init(defaultProvider, localProvider, serverProvider);

        assertEquals("Mercury", config.getProperty("one"));
        assertEquals("Venus", config.getProperty("two"));
        assertEquals("Earth", config.getProperty("three"));
        assertEquals("Local_Mars", config.getProperty("four"));
        assertEquals("Local_Jupiter", config.getProperty("five"));
        assertEquals("Local_Saturn", config.getProperty("six"));
        assertEquals("Uranus", config.getProperty("seven"));
        assertEquals("Neptune", config.getProperty("eight"));
        assertEquals(null, config.getProperty("9"));

        //waiting for http response
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Server_Mercury", config.getProperty("one"));
        assertEquals("Server_Venus", config.getProperty("two"));
        assertEquals("Earth", config.getProperty("three"));
        assertEquals("Local_Mars", config.getProperty("four"));
        assertEquals("Local_Jupiter", config.getProperty("five"));
        assertEquals("Local_Saturn", config.getProperty("six"));
        assertEquals("Uranus", config.getProperty("seven"));
        assertEquals("Neptune", config.getProperty("eight"));
        assertEquals(null, config.getProperty("9"));
    }

    @Test
    public void staticFiledConfigProviderTest(){

        Map<String, String> defaultMap = new HashMap<String, String>(){{
            put("one", "Mercury");
            put("two", "Venus");
            put("three", "Earth");
            put("four", "Mars");
            put("five", "Jupiter");
            put("six", "Saturn");
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

        BaseConfigProvider<String, String> staticProvider = new StaticFieldConfigProvider<>(Constants.class, 2);

        MConfig<String, String> config = new MConfig<>();
        config.init(defaultProvider, localProvider, staticProvider);

        assertEquals("Static_Mercury", config.getProperty("one"));
        assertEquals("Static_Venus", config.getProperty("two"));
        assertEquals("Static_Earth", config.getProperty("three"));
        assertEquals("Static_Mars", config.getProperty("four"));
        assertEquals("Local_Jupiter", config.getProperty("five"));
        assertEquals("Local_Saturn", config.getProperty("six"));
        assertEquals("Uranus", config.getProperty("seven"));
        assertEquals("Neptune", config.getProperty("eight"));
        assertEquals(null, config.getProperty("9"));
    }
}