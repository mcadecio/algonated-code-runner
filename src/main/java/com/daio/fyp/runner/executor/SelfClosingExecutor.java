package com.daio.fyp.runner.executor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class SelfClosingExecutor implements AutoCloseable {

    private final ExecutorService executorService;

    public SelfClosingExecutor(int poolSize) {
        this.executorService = new ThreadPoolExecutor(
                poolSize,
                poolSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(tasks);
    }

    @Override
    public void close() {
        executorService.shutdown();
    }
}
