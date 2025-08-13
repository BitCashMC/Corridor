package gg.bitcash.corridor;

import org.bukkit.Bukkit;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ThreadService {

    private final ExecutorService threadPool;
    private fin
    private static final String state = "COMPLETE";

    public ThreadService(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public <T> Future<T> runAsync(Callable<T> task) {
        return threadPool.submit(task);
    }

    public Future<String> runAsync(Runnable task) {
        Bukkit.getScheduler()
        return threadPool.submit(task,state);
    }
}
