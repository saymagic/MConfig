package tech.saymagic.mconfig.log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by saymagic on 2017/2/8.
 */

public class SystemOutLogger implements ILogger {

    private static final String TAG = "%s : %s";

    @Override
    public void i(String tag, String msg) {
        System.out.println(String.format(TAG, tag, msg));
    }

    @Override
    public void d(String tag, String msg) {
        System.out.println(String.format(TAG, tag, msg));
    }

    @Override
    public void v(String tag, String msg) {
        System.out.println(String.format(TAG, tag, msg));
    }

    @Override
    public void e(String tag, String msg) {
        System.out.println(String.format(TAG, tag, msg));
    }

    @Override
    public void e(String tag, Throwable throwable) {
       throwable.printStackTrace();
    }
}
