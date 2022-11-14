package com.asatria.lock.benchmark;

public class Writer extends Executor {
    public Writer(Counter counter) {
        super(counter);
    }

    @Override
    void execute(Counter counter) {

        counter.increment();
    }
}
