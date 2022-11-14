package com.asatria.lock.benchmark;

public class Reader extends Executor {
    public Reader(Counter counter) {
        super(counter);
    }

    @Override
    void execute(Counter counter) {

        counter.get();
    }

}
