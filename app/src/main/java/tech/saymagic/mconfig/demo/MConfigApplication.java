package tech.saymagic.mconfig.demo;

import android.app.Application;

import tech.saymagic.mconfig.demo.config.ConfigManager;

/**
 * Created by caoyanming on 2017/2/9.
 */

public class MConfigApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initMConfig();
    }

    private void initMConfig() {
        ConfigManager.getInstance(this);
    }
}
