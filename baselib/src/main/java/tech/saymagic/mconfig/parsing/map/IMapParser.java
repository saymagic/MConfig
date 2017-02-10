package tech.saymagic.mconfig.parsing.map;

import java.io.IOException;
import java.util.Map;

import tech.saymagic.mconfig.parsing.IParser;

/**
 * Created by saymagic on 2017/2/8.
 */

public interface IMapParser<Source, K, V> extends IParser<Source, Map<K, V>> {

    @Override
    Map<K, V> parse(Source source) throws IOException;

}
