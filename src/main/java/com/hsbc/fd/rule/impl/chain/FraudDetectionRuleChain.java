package com.hsbc.fd.rule.impl.chain;

import com.hsbc.fd.entity.Transaction;
import com.hsbc.fd.rule.FraudDetectionRule;

import java.util.LinkedList;
import java.util.List;

/**
 * 把多个欺诈检测规则，组装成链
 */
public class FraudDetectionRuleChain implements FraudDetectionRule {
    private final List<FraudDetectionRule> ruleChain = new LinkedList<>();

    public FraudDetectionRuleChain addRule(FraudDetectionRule rule) {
        ruleChain.add(rule);
        return this;
    }

    public FraudDetectionRuleChain removeRule(FraudDetectionRule rule) {
        ruleChain.remove(rule);
        return this;
    }

    @Override
    public boolean doFraudDetection(Transaction t) {
        if (ruleChain.isEmpty()) {
            return true;
        }

        for (FraudDetectionRule rule : ruleChain) {
            if (!rule.doFraudDetection(t)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FraudDetectionRuleChain{");
        sb.append("ruleChain=").append(ruleChain);
        sb.append('}');
        return sb.toString();
    }
}
