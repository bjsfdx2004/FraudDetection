package com.hsbc.fd.controller;

import com.hsbc.fd.config.ConstConfig;
import com.hsbc.fd.controller.response.R;
import com.hsbc.fd.controller.response.ResponseEnum;
import com.hsbc.fd.mock.MockKafkaBroker;
import com.hsbc.fd.mock.MockRedis;
import com.hsbc.fd.rule.impl.chain.FraudDetectionRuleFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mock")
@RequiredArgsConstructor
@Slf4j
public class MockController {

    @GetMapping("transaction/generate/{count}")
    public R generateTransaction(@PathVariable Integer count) {
        if (count > ConstConfig.MAX_TRANSACTION_GENERATE_COUNT) {
            return R.error().code(ResponseEnum.TRANSACTION_GENERATE_ERROR.getCode()).message(ResponseEnum.TRANSACTION_GENERATE_ERROR.getMessage());
        }

        MockKafkaBroker.generateMockTransaction(count);
        return R.ok().data("info", "produce " + count + " transactions into kafka");
    }

    @GetMapping("rule/{ruleStr}")
    public R addFraudDetectionRule(@PathVariable String ruleStr) {
        try {
            FraudDetectionRuleFactory.checkFraudDetectionRuleStr(ruleStr);
        } catch (Exception e) {
            return R.error().code(ResponseEnum.FRAUD_DETECTION_RULE_STR_ERROR.getCode()).message(ResponseEnum.FRAUD_DETECTION_RULE_STR_ERROR.getMessage());
        }

        MockRedis.FRAUD_DETECTION_RULE_STR = ruleStr;
        MockKafkaBroker.generateMockFraudDetectionRuleChangeNotify();
        return R.ok().data("info", "rule change successful");
    }
}
