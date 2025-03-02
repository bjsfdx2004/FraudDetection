package com.hsbc.fd.mock;

import com.hsbc.fd.entity.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class MockKafkaBroker {
    public static final LinkedBlockingQueue<Transaction> TRANSACTION_TOPIC_PARTITION_1 = new LinkedBlockingQueue<>();
    public static final LinkedBlockingQueue<Date> FRAUD_DETECTION_RULE_CHANGE_NOTIFY_TOPIC_PARTITION_1 = new LinkedBlockingQueue<>();
    private static final AtomicLong TRANSACTION_ID = new AtomicLong(1L);
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static void generateMockFraudDetectionRuleChangeNotify() {
        FRAUD_DETECTION_RULE_CHANGE_NOTIFY_TOPIC_PARTITION_1.add(new Date());
    }

    public static void generateMockTransaction(Integer count) {
        for (int i = 0; i < count; i++) {
            Transaction t = new Transaction();
            t.setId(TRANSACTION_ID.getAndIncrement());
            t.setType(generateType());

            switch (t.getType()) {
                case WITHDRAWAL:
                case DEPOSIT:
                    t.setSrcAccountID(RANDOM.nextLong(10000L));
                    break;
                case TRANSFER:
                    t.setSrcAccountID(RANDOM.nextLong(10000L));
                    t.setDestAccountID(RANDOM.nextLong(10000L) + 10000L);
                    break;
            }

            t.setAmount(new BigDecimal(RANDOM.nextDouble(0, 1000000)));
            t.setCreateTimeStamp(System.currentTimeMillis());
            TRANSACTION_TOPIC_PARTITION_1.add(t);
        }
    }

    private static Transaction.Type generateType() {
        Transaction.Type[] types = Transaction.Type.values();
        return types[RANDOM.nextInt(types.length)];
    }
}
