package com.xh.command;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuhang on 2020-10-27.
 * FFmpeg命令行执行者
 */
public class FFmpegCommandExecutor {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final long KEEP_ALIVE_TIME = 10L;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    static {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new LinkedBlockingQueue());
        //优先队列默认使用是最小堆？
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new PriorityBlockingQueue());
//        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }

    public static void execute(final String[] commands, int priority, final ExecuteListener listener) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (listener != null) {
//                    listener.onStart();
//                }
//                int result = FFmpegCommand.execute(commands);
//                if (listener != null) {
//                    listener.onEnd(result);
//                }
//            }
//        };
        //todo 需要移出测试的sleep等代码。。。
        Runnable runnable = new PriorityRunnable(priority) {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onStart();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = -1;
                if(commands == null){
                    result = -1;
                }else{
                    result  = FFmpegCommand.execute(commands);
                }
                if (listener != null) {
                    listener.onEnd(result);
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(runnable);
    }

    public interface ExecuteListener {

        void onStart();

        void onEnd(int result);
    }

}
