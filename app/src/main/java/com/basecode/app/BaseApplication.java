package com.basecode.app;

import android.app.Application;
import android.os.StrictMode;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * User: 王旭国
 * Date: 16/11/9 16:42
 * Email:wangxuguo@jhyx.com.cn
 */

public class BaseApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        enabledStrictMode();
        LeakCanary.install(this);
        // Normal app init code...
    }
    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }
}
