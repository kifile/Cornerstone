package com.kifile.android.cornerstone.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步线程池，建议所有新开线程都通过这里进行execute执行，避免重复创建线程.
 * <p/>
 * Created by zhouyi on 15/5/14.
 */
public class WorkerThreadPool {

    public static final String TAG = WorkerThreadPool.class.getSimpleName();

    public static final int PRIORITY_HIGH = 10;
    public static final int PRIORITY_NORMAL = 5;

    private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors();

    private static final WorkerThreadPool sInstance = new WorkerThreadPool();

    private ExecutorService mThreadPool;

    private WorkerThreadPool() {
        mThreadPool = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>(), new WorkerThreadFactory());
    }

    public static WorkerThreadPool getInstance() {
        return sInstance;
    }

    public WorkerRunnable execute(Runnable runnable) {
        return execute(runnable, false);
    }

    public WorkerRunnable execute(Runnable runnable, boolean priority) {
        WorkerRunnable workerRunnable;
        if (!(runnable instanceof WorkerRunnable)) {
            workerRunnable = new WorkerRunnable(runnable, priority ? PRIORITY_HIGH : PRIORITY_NORMAL);
        } else {
            workerRunnable = (WorkerRunnable) runnable;
        }
        mThreadPool.execute(workerRunnable);
        return workerRunnable;
    }

    /**
     * 可取消的Runnable，用于涉及到需要取消的任务.
     */
    public static class WorkerRunnable implements Runnable, Comparable<WorkerRunnable> {

        private int mPriority;

        private boolean mCanceled;
        private boolean mFinished;

        private Runnable mRunnable;

        public WorkerRunnable(Runnable runnable, int priority) {
            mRunnable = runnable;
            mPriority = priority;
        }

        /**
         * 取消任务执行
         */
        public void cancel() {
            mCanceled = true;
        }

        public boolean isCanceled() {
            return mCanceled;
        }

        public boolean isFinished() {
            return mFinished;
        }

        @Override
        public final void run() {
            if (!isCanceled()) {
                if (mRunnable != null) {
                    mRunnable.run();
                }
            }
            mFinished = true;
        }

        @Override
        public int compareTo(WorkerRunnable another) {
            return another != null && mPriority < another.mPriority ? 1 : -1;
        }
    }

    public static class WorkerThreadFactory implements ThreadFactory {

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        WorkerThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = TAG;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + "[" + threadNumber.getAndIncrement() + "]", 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != android.os.Process.THREAD_PRIORITY_BACKGROUND) {
                t.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            }
            return t;
        }
    }
}
