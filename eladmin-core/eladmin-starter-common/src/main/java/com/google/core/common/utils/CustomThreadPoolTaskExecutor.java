package com.google.core.common.utils;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 这是{@link ThreadPoolTaskExecutor}的一个简单替换，可搭配TransmittableThreadLocal实现父子线程之间的数据传递
 * @author iris
 */
public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long serialVersionUID = -1620411091519062590L;

    @Override
    public void execute(@NotNull Runnable runnable) {
        Runnable ttlRunnable = TtlRunnable.get(runnable);
        super.execute(Objects.requireNonNull(ttlRunnable));
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        Callable<T> ttlCallable = TtlCallable.get(task);
        return super.submit(Objects.requireNonNull(ttlCallable));
    }

    @NotNull
    @Override
    public Future<?> submit(@NotNull Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submit(Objects.requireNonNull(ttlRunnable));
    }

    @NotNull
    @Override
    public ListenableFuture<?> submitListenable(@NotNull Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submitListenable(Objects.requireNonNull(ttlRunnable));
    }

    @NotNull
    @Override
    public <T> ListenableFuture<T> submitListenable(@NotNull Callable<T> task) {
        Callable<T> ttlCallable = TtlCallable.get(task);
        return super.submitListenable(Objects.requireNonNull(ttlCallable));
    }
}
