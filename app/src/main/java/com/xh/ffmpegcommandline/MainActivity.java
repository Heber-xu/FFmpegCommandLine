package com.xh.ffmpegcommandline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xh.command.FFmpegCommand;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_contact_video).setOnClickListener(this);
        findViewById(R.id.btn_extract_video).setOnClickListener(this);
        findViewById(R.id.btn_extract_audio).setOnClickListener(this);
        findViewById(R.id.btn_change_resolution).setOnClickListener(this);
        findViewById(R.id.btn_remux).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions();
    }

    private int request_code = 100;

    private void requestPermissions() {
        String writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        List<String> withoutPermissions = PermissionHelper.checkWithoutPermissions(this, writePermission, readPermission);
        if (!ContainerUtil.isEmpty(withoutPermissions)) {
            String[] permissions = ContainerUtil.listToArray(String.class, withoutPermissions);
            PermissionHelper.requestPermissions(this, request_code, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_contact_video:
                contactVideo();
                break;
            case R.id.btn_extract_video:
                extractVideo();
                break;
            case R.id.btn_extract_audio:
                extractAudio();
                break;
            case R.id.btn_change_resolution:
                changeResolution();
                break;
            case R.id.btn_remux:
                remux();
                break;
        }
    }

    private void contactVideo() {
        String compressVideoFilepath = "/sdcard/compresslist.txt";
        String resultFilepath = "/sdcard/a_result.mp4";
        String commandStr = "ffmpeg -f concat -safe 0 -i " + compressVideoFilepath + " -c copy " + resultFilepath;
        final String[] commands = commandStr.split(" ");
        new Thread() {
            @Override
            public void run() {
                FFmpegCommand.execute(commands, new FFmpegCommand.ExecuteListener() {
                    @Override
                    public void onStart() {
                        Log.i(TAG, "contactVideo onStart.");
                    }

                    @Override
                    public void onEnd(int result) {
                        Log.i(TAG, "contactVideo onEnd result : " + result);
                    }
                });
            }
        }.start();
    }

    private void extractVideo() {
//        String src = "/sdcard/a_result.mp4";
//        String dest = "/sdcard/a_extractVideo.mp4";
//        String[] commands = FFmpegCommands.extractVideo(src, dest);
//        FFmpegRun.execute(commands, new FFmpegRun.FFmpegRunListener() {
//            @Override
//            public void onStart() {
//                Log.i(TAG, "extractVideo onStart.");
//            }
//
//            @Override
//            public void onEnd(int result) {
//                Log.i(TAG, "extractVideo onEnd.");
//            }
//        });
    }

    private void extractAudio() {
//        String src = "/sdcard/a_result.mp4";
//        String dest = "/sdcard/a_extractAudio.mp4";
//        String[] commands = FFmpegCommands.extractAudio(src, dest);
//        FFmpegRun.execute(commands, new FFmpegRun.FFmpegRunListener() {
//            @Override
//            public void onStart() {
//                Log.i(TAG, "extractAudio onStart.");
//            }
//
//            @Override
//            public void onEnd(int result) {
//                Log.i(TAG, "extractAudio onEnd.");
//            }
//        });
    }

    private void changeResolution() {
        String srcPath = "/sdcard/a_result.mp4";
        String destPath = "/sdcard/a_resolution.mp4";
//        String commandStr = "ffmpeg -i " + srcPath + " -strict -2 -s 640x480 " + destPath;
        String commandStr = "ffmpeg -i " + srcPath + " -s 640x480 " + destPath;
        final String[] commands = commandStr.split(" ");

        new Thread() {
            @Override
            public void run() {
                FFmpegCommand.execute(commands, new FFmpegCommand.ExecuteListener() {
                    @Override
                    public void onStart() {
                        Log.i(TAG, "changeResolution onStart.");
                    }

                    @Override
                    public void onEnd(int result) {
                        Log.i(TAG, "changeResolution onEnd result : " + result);
                    }
                });
            }
        }.start();
    }

    private void remux() {
        String srcPath = "/sdcard/a_result.mp4";
        String destPath = "/sdcard/a_remux.mp4";
//        String command = "ffmpeg -i " + srcPath + " -c:a copy -c:v copy -c:s copy " + destPath;
        String command = "ffmpeg -i " + srcPath + " -c:a copy -c:v copy " + destPath;
        final String[] commandArr = command.split(" ");

        new Thread() {
            @Override
            public void run() {
                FFmpegCommand.execute(commandArr, new FFmpegCommand.ExecuteListener() {
                    @Override
                    public void onStart() {
                        Log.i(TAG, "remux onStart.");
                    }

                    @Override
                    public void onEnd(int result) {
                        Log.i(TAG, "remux onEnd result : " + result);
                    }
                });
            }
        }.start();
    }
}
