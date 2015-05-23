package com.izlei.shlibrary.data.executor;


import android.util.Log;

import com.izlei.shlibrary.domain.executor.ThreadExecutor;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhouzili on 2015/4/23.
 */
public class JobExecutor implements ThreadExecutor {
    public static String TAG = JobExecutor.class.getSimpleName();
    private static final int INITIAL_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 10;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final ThreadPoolExecutor threadPoolExecutor;
    private final ThreadFactory threadFactory;
    private final BlockingDeque<Runnable> workQueue;

    public JobExecutor() {
        this.threadFactory = new JobThreadFactory();
        this.workQueue = new LinkedBlockingDeque<>();
        this.threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE,MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,KEEP_ALIVE_TIME_UNIT,this.workQueue, this.threadFactory);
    }

    @Override
    public void execute(Runnable runnable) {

        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to execute cannot to be null");
        }
        this.threadPoolExecutor.execute(runnable);
    }

    public Thread getNewThread(Runnable runnable) {
        return this.threadFactory.newThread(runnable);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME = "android_";
        private int counter = 0;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, THREAD_NAME + counter);
        }
    }
}
