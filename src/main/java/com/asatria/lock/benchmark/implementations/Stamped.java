package com.asatria.lock.benchmark.implementations;

import com.asatria.lock.benchmark.Counter;

import java.util.concurrent.locks.StampedLock;

public class Stamped implements Counter {

    private final StampedLock stampedlock = new StampedLock();

    private long counter;

    public long get() {
        long stamp = stampedlock.tryOptimisticRead();

        if (stampedlock.validate(stamp)) {

            return counter;
        }

        try {

            stamp = stampedlock.readLock();

            return counter;

        } finally {

            stampedlock.unlockRead(stamp);
        }
    }

    public void increment() {
        long stamp = stampedlock.writeLock();

        try {
            ++counter;
        } finally {
            stampedlock.unlockWrite(stamp);
        }
    }
}
