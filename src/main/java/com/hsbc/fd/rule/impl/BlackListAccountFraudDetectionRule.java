package com.hsbc.fd.rule.impl;

import com.hsbc.fd.entity.Transaction;
import com.hsbc.fd.rule.FraudDetectionRule;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 检测来自可疑账户的交易
 */
public class BlackListAccountFraudDetectionRule implements FraudDetectionRule {
    private final Set<Long> blackListAccountSet;

    public BlackListAccountFraudDetectionRule(String ruleStr) {
        blackListAccountSet = new CopyOnWriteArraySet<>();
        String[] typeAndParamArray = ruleStr.split("_");

        for (int i = 1, len = typeAndParamArray.length; i < len; i++) {
            blackListAccountSet.add(Long.parseLong(typeAndParamArray[i]));
        }
    }

    @Override
    public boolean doFraudDetection(Transaction t) {
        if (t.getSrcAccountID() != null && blackListAccountSet.contains(t.getSrcAccountID())) {
            return false;
        }

        if (t.getDestAccountID() != null && blackListAccountSet.contains(t.getDestAccountID())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BlackListAccountFraudDetectionRule{");
        sb.append("blackListAccountSet=").append(blackListAccountSet);
        sb.append('}');
        return sb.toString();
    }
}
