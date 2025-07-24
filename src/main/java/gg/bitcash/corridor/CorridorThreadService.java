package gg.bitcash.corridor;

import java.util.concurrent.ExecutorService;

public class CorridorThreadService {

    private final ExecutorService threadPool;

    public CorridorThreadService(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }
}
