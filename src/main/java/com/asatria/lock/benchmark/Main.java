package com.asatria.lock.benchmark;

import com.asatria.lock.benchmark.implementations.RWLock;
import com.asatria.lock.benchmark.implementations.Stamped;
import com.asatria.lock.benchmark.implementations.Synchronized;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static int EXECUTION_PERIOD_IN_SEC = 60;
    public static int R_THREADS = 5;
    public static int W_THREADS = 5;
    public static int ROUNDS = 10;
    private static String COUNTER = Counters.SYNCHRONIZED.toString();

    private static ExecutorService es;

    private static int round;

    private static Executor[][] rounds;

    private enum Counters {
        SYNCHRONIZED,
        RWLOCK,
        STAMPED
    }

    public static void main(String[] args) {
        COUNTER = Counters.SYNCHRONIZED.toString();

        if (args.length > 0) {
            COUNTER = args[0];
        }

        if (args.length > 1) {
            R_THREADS = Integer.valueOf(args[1]);
        }

        if (args.length > 2) {
            W_THREADS = Integer.valueOf(args[2]);
        }

        if (args.length > 3) {
            ROUNDS = Integer.valueOf(args[3]);
        }

        if (args.length > 4) {
            EXECUTION_PERIOD_IN_SEC = Integer.valueOf(args[4]);
        }

        rounds = new Executor[ROUNDS][R_THREADS + W_THREADS];

        System.out.println("Using " + COUNTER + ". read threads: " + R_THREADS + ". write threads: " + W_THREADS + ". rounds: " + ROUNDS +
                ". execution period (sec): " + EXECUTION_PERIOD_IN_SEC);

        for (round = 0; round < ROUNDS; round++) {

            Counter counter = getCounter();

            es = Executors.newFixedThreadPool(R_THREADS + W_THREADS);

            AtomicInteger readers = new AtomicInteger(R_THREADS);
            AtomicInteger writers = new AtomicInteger(W_THREADS);

            int i = 0;

            while (readers.get() > 0 || writers.get() > 0) {
                if (readers.decrementAndGet() >= 0) {

                    Reader reader = new Reader(counter);

                    rounds[round][i] = reader;

                    es.execute(reader);

                    i++;
                }

                if (writers.decrementAndGet() >= 0) {

                    Writer writer = new Writer(counter);

                    rounds[round][i] = writer;

                    es.execute(writer);

                    i++;
                }
            }

            try {
                Thread.sleep(EXECUTION_PERIOD_IN_SEC * 1000L);

                es.shutdownNow();

                es.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Arrays.stream(rounds[round]).map(Executor::getExecutionCount).reduce(Long::sum).get() / EXECUTION_PERIOD_IN_SEC);
        }

        System.out.println("Average: " + (Arrays.stream(rounds).flatMap(Arrays::stream).map(Executor::getExecutionCount).reduce(Long::sum).get() / EXECUTION_PERIOD_IN_SEC) / ROUNDS);
    }

    public static Counter getCounter() {
        Counters counterTypeEnum = Counters.valueOf(COUNTER);

        switch (counterTypeEnum) {
            case RWLOCK:
                return new RWLock();
            case SYNCHRONIZED:
                return new Synchronized();
            case STAMPED:
                return new Stamped();
        }

        return null;
    }

}
