package gg.bitcash.corridor;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.*;

public class ThreadService {

    public enum Context {
        SYNC,
        ASYNC
    }

    private final ExecutorService threadPool;
    private final BukkitScheduler scheduler;
    private final Corridor instance;

    public ThreadService(Corridor instance, ExecutorService threadPool, BukkitScheduler scheduler) {
        this.threadPool = threadPool;
        this.scheduler = scheduler;
        this.instance = instance;
    }

    public <T> Future<T> runAsync(Callable<T> task) {
        return threadPool.submit(task);
    }

    public Future<State> runAsync(Runnable task) {
        return threadPool.submit(task,State.SUCCESS);
    }

    public <T> Future<T> runInline(Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            T result = task.call();
            future.complete(result);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public Future<State> runInline(Runnable task) {
        CompletableFuture<State> future = new CompletableFuture<>();
        task.run();
        future.complete(State.SUCCESS);
        return future;
    }

    public BukkitTask runBukkitTask(Runnable task) {
        return scheduler.runTask(instance,task);
    }


}
