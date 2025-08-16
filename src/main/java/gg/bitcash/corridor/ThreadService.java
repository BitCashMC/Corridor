package gg.bitcash.corridor;

import org.bukkit.Bukkit;

import java.util.concurrent.*;

public class ThreadService {

    private final ExecutorService threadPool;

    public ThreadService(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public <T> Future<T> runAsync(Callable<T> task) {
        return threadPool.submit(task);
    }

    public Future<State> runAsync(Runnable task) {
        return threadPool.submit(task,State.SUCCESS);
    }

    public <T> Future<T> run(Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            T result = task.call();
            future.complete(result);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public Future<State> run(Runnable task) {
        CompletableFuture<State> future = new CompletableFuture<>();
        task.run();
        future.complete(State.SUCCESS);
        return future;
    }
}
