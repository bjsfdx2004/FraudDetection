package com.hsbc.fd.mock;

import java.util.concurrent.atomic.AtomicLong;

public abstract class MockKafkaConsumer implements Runnable {
    private static final AtomicLong CONSUMER_ID = new AtomicLong(1L);
    private final Thread consumerThread;

    public MockKafkaConsumer() {
        consumerThread = new Thread(
                this,
                "consumer thread " + CONSUMER_ID.getAndIncrement()
        );

        consumerThread.setDaemon(true);
    }

    public void start() {
        consumerThread.start();
    }

    public void stop() {
        consumerThread.interrupt();
    }
}
