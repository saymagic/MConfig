package tech.saymagic.mconfig.androidlib;

import android.util.Log;

import tech.saymagic.mconfig.log.ILogger;

/**
 * Created by caoyanming on 2017/2/9.
 */

public class AndroidConfigLogger implements ILogger {

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    @Override
    public void e(String tag, Throwable throwable) {
        Log.wtf(tag, throwable);
    }
}
