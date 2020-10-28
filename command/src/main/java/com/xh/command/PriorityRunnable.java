package com.xh.command;

/**
 * Created by xuhang on 2020-10-27.
 */
public abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {

    private int mPriority;

    public PriorityRunnable(int priority) {
        this.mPriority = priority;
    }

    @Override
    public int compareTo(PriorityRunnable o) {
//            return this.mPriority - o.mPriority;
        return o.mPriority - this.mPriority;
    }

}
