package com.xh.command;

import android.os.AsyncTask;
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

    public static void enqueue(String[] commands, final ExecuteListener listener) {
        new AsyncTask<String[], Integer, Integer>() {

            @Override
            protected void onPreExecute() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            protected Integer doInBackground(String[]... params) {
                return FFmpegCommand.execute(params[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if (listener != null) {
                    listener.onEnd(integer);
                }
            }
        }.execute(commands);
    }

    private static class Task {

    }

    public interface ExecuteListener {

        void onStart();

        void onEnd(int result);
    }
}
