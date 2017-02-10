package tech.saymagic.mconfig.log;

/**
 * Created by saymagic on 2017/2/8.
 */

public interface ILogger {

    void i(String tag, String msg);

    void d(String tag, String msg);

    void v(String tag, String msg);

    void e(String tag, String msg);

    void e(String tag, Throwable throwable);

    ILogger DEFAULT = new EmptyLogger();
}
