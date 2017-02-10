package tech.saymagic.mconfig.persistence;

/**
 * Created by saymagic on 2017/2/8.
 */

public interface IPersistence<Source> {

    void saveSource(Source source);

    Source getSource();

}
