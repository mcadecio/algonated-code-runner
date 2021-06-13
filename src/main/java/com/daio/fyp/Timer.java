package com.daio.fyp;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Timer {
    private static final Logger logger = LoggerFactory.getLogger(Timer.class);
    private static final String TOOK_MS_S = "{} took -> {}ms | {}s";

    public static void runTimedTask(Task task, String name) {
        Stopwatch timer = Stopwatch.createStarted();
        task.run();
        timer.stop();
        Duration duration = timer.elapsed();
        logger.info(TOOK_MS_S, name, duration.toMillis(), duration.toSeconds());
    }

    public static void runTimedTaskWithException(TaskWithException task, String name) {
        Stopwatch timer = Stopwatch.createStarted();
        try {
            task.run();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        timer.stop();
        Duration duration = timer.elapsed();
        logger.info(TOOK_MS_S, name, duration.toMillis(), duration.toSeconds());
    }

    public static <T> T runTimedTask(TaskWithResult<T> task, String name) {
        Stopwatch timer = Stopwatch.createStarted();
        T result = task.run();
        timer.stop();
        Duration duration = timer.elapsed();
        logger.info(TOOK_MS_S, name, duration.toMillis(), duration.toSeconds());
        return result;
    }

    public static <T> T runTimedTaskWithException(TaskWithResultWithException<T> task, String name, T defaultValue) {
        Stopwatch timer = Stopwatch.createStarted();
        T result;
        try {
            result = task.run();
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = defaultValue;
        }
        timer.stop();
        Duration duration = timer.elapsed();
        logger.info(TOOK_MS_S, name, duration.toMillis(), duration.toSeconds());
        return result;
    }

    public interface Task {
        void run();
    }

    public interface TaskWithException {
        void run() throws Exception;
    }

    public interface TaskWithResult<T> {
        T run();
    }

    public interface TaskWithResultWithException<T> {
        T run() throws Exception;
    }
}
