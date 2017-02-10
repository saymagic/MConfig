package tech.saymagic.mconfig.log;

/**
 * Created by saymagic on 2017/2/8.
 */

public class EmptyLogger implements ILogger {

    @Override
    public void i(String tag, String msg) {

    }

    @Override
    public void d(String tag, String msg) {

    }

    @Override
    public void v(String tag, String msg) {

    }

    @Override
    public void e(String tag, String msg) {

    }

    @Override
    public void e(String tag, Throwable throwable) {

    }

}
