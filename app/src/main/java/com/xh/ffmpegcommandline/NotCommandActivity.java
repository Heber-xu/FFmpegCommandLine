package com.xh.ffmpegcommandline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xh.command.FFmpegNotCommand;

import java.io.File;


public class NotCommandActivity extends AppCompatActivity {

    private static final String TAG = "NotCommandActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_command);

        findViewById(R.id.btnRemux).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remux();
            }
        });
        findViewById(R.id.btnMultiRemux).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiRemux();
            }
        });
    }

    private void remux() {

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "remux start.");
                String inputFile = "/sdcard/MyVpVideo/3bb02win_general_record_20200910145648-00-00.MP4";
                File f = new File(inputFile);
                Log.i(TAG, "inputFile exist : " + f.exists() + ", isFile : " + f.isFile());
                String outputFile = "/sdcard/MyVpVideo/remux.mp4";
                int result = FFmpegNotCommand.remux(inputFile, outputFile);
                Log.i(TAG, "remux result : " + result);
            }
        }.start();

    }

    private void multiRemux() {

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "remux1 start.");
                String inputFile = "/sdcard/MyVpVideo/3bb02win_general_record_20200910145648-00-00.MP4";
                File f = new File(inputFile);
                Log.i(TAG, "inputFile exist : " + f.exists() + ", isFile : " + f.isFile());
                String outputFile = "/sdcard/MyVpVideo/remux1.mp4";
                int result = FFmpegNotCommand.remux(inputFile, outputFile);
                Log.i(TAG, "remux1 result : " + result);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "remux2 start.");
                String inputFile = "/sdcard/MyVpVideo/3bb02win_general_record_20200910151448-00-00.MP4";
                File f = new File(inputFile);
                Log.i(TAG, "inputFile exist : " + f.exists() + ", isFile : " + f.isFile());
                String outputFile = "/sdcard/MyVpVideo/remux2.mp4";
                int result = FFmpegNotCommand.remux(inputFile, outputFile);
                Log.i(TAG, "remux2 result : " + result);
            }
        }.start();
    }
}
