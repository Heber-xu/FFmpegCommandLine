package com.xh.command;

/**
 * Created by xuhang on 2020-11-03.
 * 非命令行的FFmpeg功能
 */
public class FFmpegNotCommand {

    static {
        System.loadLibrary("ijkffmpeg");
        System.loadLibrary("ffmpegcommand");
    }

    public native synchronized static int remux(String inputFile, String outputFile);

}
