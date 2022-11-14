package com.asatria.lock.benchmark.implementations;

import com.asatria.lock.benchmark.Counter;

import java.util.concurrent.locks.StampedLock;

public class Stamped implements Counter {

    private final StampedLock rwlock = new StampedLock();

    private long counter;

    public long get() {
        long stamp = rwlock.tryOptimisticRead();

        if (rwlock.validate(stamp)) {

            return counter;
        }

        try {

            stamp = rwlock.readLock();

            return counter;

        } finally {

            rwlock.unlockRead(stamp);
        }
    }

    public void increment() {
        long stamp = rwlock.writeLock();

        try {
            ++counter;
        } finally {
            rwlock.unlockWrite(stamp);
        }
    }
}
