package com.zzb.warranty.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/2/10.
 */
public class UserCouponExecutorService implements ExecutorService {

    private static UserCouponExecutorService userCouponExecutorService;

    private ExecutorService pool;

    private UserCouponExecutorService() {
        pool = Executors.newFixedThreadPool(10);
    }

    public static UserCouponExecutorService getInstance() {
        if (userCouponExecutorService == null) {
            userCouponExecutorService = new UserCouponExecutorService();
        }
        return userCouponExecutorService;
    }

    public ExecutorService getPool() {
        return pool;
    }

    public void shutDown() {
        pool.shutdown();
    }

    public void execute(Runnable r) {
        pool.execute(r);
    }


    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
