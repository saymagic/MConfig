package tech.saymagic.mconfig.parsing.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by saymagic on 2017/2/8.
 */

public class PropertiesParser implements IMapParser<InputStream, String, String> {

    @Override
    public Map<String, String> parse(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return new HashMap(properties);
    }
}
