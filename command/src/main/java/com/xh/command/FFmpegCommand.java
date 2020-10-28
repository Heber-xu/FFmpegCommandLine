package com.xh.command;

import androidx.annotation.NonNull;

/**
 * Created by xuhang on 2020-09-08.
 */
public class FFmpegCommand {

    static {
        System.loadLibrary("ijkffmpeg");
        System.loadLibrary("ffmpegcommand");
    }

    /**
     * 多个线程执行会出现崩溃问题，所以需要做成线程安全的
     * todo 需要研究几个问题：
     * 1、如果这里不是使用static方法，而是对象方法，那么需要做线程安全吗？
     * 2、对于FFmpeg底层命令行相关代码（相关结构体）是否是针对进程全局的？
     * 3、java native 类方法和对象方法对于底层C/C++代码有什么影响吗？
     *
     * @param commands
     * @return
     */
    public static synchronized native int execute(String[] commands);

}
