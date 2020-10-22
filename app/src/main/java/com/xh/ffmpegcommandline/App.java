package com.xh.ffmpegcommandline;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by xuhang on 2020-10-22.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "8794ac9686", false);
    }
}
