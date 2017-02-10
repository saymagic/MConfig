package tech.saymagic.mconfig.parsing;

import java.io.IOException;

/**
 * Created by saymagic on 2017/2/8.
 */

public interface IParser<K, V> {

    V parse(K k) throws IOException;

}
