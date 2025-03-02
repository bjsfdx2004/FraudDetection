package com.hsbc.fd.mock;

import com.hsbc.fd.entity.Transaction;
import com.hsbc.fd.rule.impl.chain.FraudDetectionRuleChain;
import com.hsbc.fd.rule.impl.chain.FraudDetectionRuleFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MockKafkaTransactionConsumer extends MockKafkaConsumer {
    private static final ThreadPoolExecutor TRANSACTION_FRAUD_DETECTION_EXECUTOR;
    private static final int TRANSACTION_FRAUD_DETECTION_EXECUTOR_QUEUE_CAP_MAX = 100000;

    static {
        TRANSACTION_FRAUD_DETECTION_EXECUTOR = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(TRANSACTION_FRAUD_DETECTION_EXECUTOR_QUEUE_CAP_MAX),
                new ThreadFactory() {
                    private AtomicLong threadId = new AtomicLong(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "transaction-fraud-detection-executor-thread-" + threadId.getAndIncrement());
                        t.setDaemon(true);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Override
    public void run() {
        Transaction t = null;

        try {
            while ((t = MockKafkaBroker.TRANSACTION_TOPIC_PARTITION_1.take()) != null) {
                MockKafkaTransactionConsumer.TRANSACTION_FRAUD_DETECTION_EXECUTOR.execute(
                        new TransactionFraudDetectionRunnable(t, FraudDetectionRuleFactory.getFraudDetectionRuleChain()) {
                            @Override
                            public void run() {
                                boolean isSafe = fraudDetectionRuleChain.doFraudDetection(transaction);

                                if (!isSafe) {
                                    log.error("[Fraud Detection] detected risky transaction: {}", transaction.toString());
                                }
                            }
                        }
                );
            }
        } catch (InterruptedException e) {

        }
    }

    private static abstract class TransactionFraudDetectionRunnable implements Runnable {
        protected final Transaction transaction;
        protected final FraudDetectionRuleChain fraudDetectionRuleChain;

        public TransactionFraudDetectionRunnable(Transaction transaction, FraudDetectionRuleChain fraudDetectionRuleChain) {
            this.transaction = transaction;
            this.fraudDetectionRuleChain = fraudDetectionRuleChain;
        }
    }
}
