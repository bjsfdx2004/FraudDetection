package com.hsbc.fd.mock;

import com.hsbc.fd.rule.impl.chain.FraudDetectionRuleFactory;

import java.util.Date;

public class MockKafkaFraudDetectionRuleChangeNotifyConsumer extends MockKafkaConsumer {

    @Override
    public void run() {
        Date changeDate = null;

        try {
            while ((changeDate = MockKafkaBroker.FRAUD_DETECTION_RULE_CHANGE_NOTIFY_TOPIC_PARTITION_1.take()) != null) {
                FraudDetectionRuleFactory.updateFraudDetectionRuleChain(MockRedis.FRAUD_DETECTION_RULE_STR);
            }
        } catch (InterruptedException e) {

        }
    }
}
