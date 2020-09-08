package com.xh.command;

import android.util.Log;

/**
 * Created by xuhang on 2020-09-08.
 */
public class FFmpegCommand {

    private static final String TAG = "FFmpegCommand";

    static {
        System.loadLibrary("ijkffmpeg");
        System.loadLibrary("ffmpegcommand");
    }

    public static native int execute(String[] commands);

    public static void execute(String[] commands, ExecuteListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        int result = execute(commands);
        Log.i(TAG, "execute result : " + result);
        if (listener != null) {
            listener.onEnd(result);
        }
    }

    public interface ExecuteListener {

        void onStart();

        void onEnd(int result);
    }
}
