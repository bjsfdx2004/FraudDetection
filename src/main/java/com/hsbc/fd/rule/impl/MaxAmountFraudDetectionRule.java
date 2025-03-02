package com.hsbc.fd.rule.impl;

import com.hsbc.fd.entity.Transaction;
import com.hsbc.fd.rule.FraudDetectionRule;

import java.math.BigDecimal;

/**
 * 检测超过一定金额的交易
 */
public class MaxAmountFraudDetectionRule implements FraudDetectionRule {
    private final BigDecimal maxAmount;

    public MaxAmountFraudDetectionRule(String ruleStr) {
        String[] typeAndParamArray = ruleStr.split("_");
        this.maxAmount = new BigDecimal(typeAndParamArray[1]);
    }

    @Override
    public boolean doFraudDetection(Transaction t) {
        if (maxAmount.compareTo(t.getAmount()) < 0) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MaxAmountFraudDetectionRule{");
        sb.append("maxAmount=").append(maxAmount);
        sb.append('}');
        return sb.toString();
    }
}
