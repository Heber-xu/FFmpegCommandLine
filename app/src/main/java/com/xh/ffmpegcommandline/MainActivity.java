package com.xh.ffmpegcommandline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xh.command.FFmpegCommand;
import com.xh.command.FFmpegCommandExecutor;

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
        findViewById(R.id.btn_multi_command).setOnClickListener(this);
        findViewById(R.id.btn_to_not_command).setOnClickListener(this);
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
            case R.id.btn_multi_command:
                multiWork();
                break;
            case R.id.btn_to_not_command:
                toNotCommand();
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
                Log.i(TAG, "contactVideo start.");
                int result = FFmpegCommand.execute(commands);
                Log.i(TAG, "contactVideo end : " + result);
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
                Log.i(TAG, "changeResolution onStart.");
                int result = FFmpegCommand.execute(commands);
                Log.i(TAG, "changeResolution onEnd result : " + result);
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
                Log.i(TAG, "remux onStart.");
                int result = FFmpegCommand.execute(commandArr);
                Log.i(TAG, "remux onEnd result : " + result);
            }
        }.start();
    }

    private void multiWork() {
        //
        String srcPath1 = "/sdcard/MyVpVideo/3bb02win_general_record_20200910145648-00-00.MP4";
        String dstPath1 = "/sdcard/MyVpVideo/a.mp4";

        String srcPath2 = "/sdcard/MyVpVideo/3bb02win_general_record_20200910151448-00-00.MP4";
        String dstPath2 = "/sdcard/MyVpVideo/b.mp4";

        //加入这个测试优先队列
        FFmpegCommandExecutor.execute(null, 0, null);
        remux(srcPath1, dstPath1, 0);
        remux(srcPath2, dstPath2, 1);
    }

    private void remux(String srcPath, final String dstPath, int priority) {
        String command = "ffmpeg -i " + srcPath + " -c:a copy -c:v copy " + dstPath;
        final String[] commandArr = command.split(" ");

        //多个线程执行会出现崩溃问题
//        new Thread() {
//            @Override
//            public void run() {
//                Log.i(TAG, "remux onStart detPath : " + dstPath);
//                int result = FFmpegCommand.execute(commandArr);
//                Log.i(TAG, "remux onEnd result : " + result + ",dstPath : " + dstPath);
//            }
//        }.start();

        FFmpegCommandExecutor.execute(commandArr, priority, new FFmpegCommandExecutor.ExecuteListener() {
            @Override
            public void onStart() {
                Log.i(TAG, "remux onStart detPath : " + dstPath);
            }

            @Override
            public void onEnd(int result) {
                Log.i(TAG, "remux onEnd result : " + result + ",dstPath : " + dstPath);
            }
        });
    }

    private void toNotCommand() {
        startActivity(new Intent(this, NotCommandActivity.class));
    }
}
