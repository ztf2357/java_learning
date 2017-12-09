package com.beyondhost.exam.util;

import java.util.concurrent.*;

public class CrawlThreadPool {


    private ThreadPoolExecutor pool = null;

    public  CrawlThreadPool() {
        pool = new ThreadPoolExecutor(
                0,
                10,
                30,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(50),
                new CrawlRejectedExecutionHandler());
    }


    public void destory() {
        if(pool != null) {
            pool.shutdownNow();
        }
    }

    public ExecutorService  getThreadPoolExecutor() {
        return this.pool;
    }


    private class CrawlRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
