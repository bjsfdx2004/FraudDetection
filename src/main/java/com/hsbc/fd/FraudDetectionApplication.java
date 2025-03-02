package com.hsbc.fd;

import com.hsbc.fd.mock.MockKafkaFraudDetectionRuleChangeNotifyConsumer;
import com.hsbc.fd.mock.MockKafkaTransactionConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FraudDetectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FraudDetectionApplication.class, args);
        new MockKafkaTransactionConsumer().start();
        new MockKafkaFraudDetectionRuleChangeNotifyConsumer().start();
    }

}
