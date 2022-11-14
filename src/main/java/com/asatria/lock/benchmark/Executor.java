package com.asatria.lock.benchmark;

public abstract class Executor implements Runnable {
    private final Counter counter;
    private long executionCount;

    public Executor(Counter counter) {
        this.counter = counter;
    }

    abstract void execute(Counter counter);

    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }

            execute(counter);

            ++executionCount;
        }
    }

    public long getExecutionCount() {

        return executionCount;
    }
}
