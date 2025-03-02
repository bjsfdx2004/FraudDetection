package com.hsbc.fd.rule.impl.chain;

import com.hsbc.fd.rule.FraudDetectionRule;
import com.hsbc.fd.rule.impl.BlackListAccountFraudDetectionRule;
import com.hsbc.fd.rule.impl.MaxAmountFraudDetectionRule;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class FraudDetectionRuleFactory {
    private static final Map<Integer, Class<? extends FraudDetectionRule>> FRAUD_DETECTION_RULE_MAP;
    private static volatile FraudDetectionRuleChain LATEST_FRAUD_DETECTION_RULE_CHAIN = new FraudDetectionRuleChain();

    static {
        FRAUD_DETECTION_RULE_MAP = new HashMap<>();
        FRAUD_DETECTION_RULE_MAP.put(1, MaxAmountFraudDetectionRule.class);
        FRAUD_DETECTION_RULE_MAP.put(2, BlackListAccountFraudDetectionRule.class);
    }

    private FraudDetectionRuleFactory() {

    }

    public static FraudDetectionRuleChain getFraudDetectionRuleChain() {
        return LATEST_FRAUD_DETECTION_RULE_CHAIN;
    }

    public static void updateFraudDetectionRuleChain(String rulesStr) {
        LATEST_FRAUD_DETECTION_RULE_CHAIN = checkFraudDetectionRuleStr(rulesStr);
    }

    /**
     * 通过解析包含欺诈检测规则的字符串，得到欺诈检测规则链
     * 1_666.66-2_1_100_600_1500_10000
     *
     * @param rulesStr 待解析的，包含欺诈检测规则的字符串
     * @return 欺诈检测规则链
     */
    public static FraudDetectionRuleChain checkFraudDetectionRuleStr(String rulesStr) {
        String[] ruleStrArray = rulesStr.split("-");
        FraudDetectionRuleChain ruleChain = new FraudDetectionRuleChain();

        for (String ruleStr : ruleStrArray) {
            String[] ruleAndParamArray = ruleStr.split("_");

            try {
                int ruleType = Integer.parseInt(ruleAndParamArray[0]);
                Class<? extends FraudDetectionRule> ruleTypeClass = FRAUD_DETECTION_RULE_MAP.get(ruleType);
                Constructor<? extends FraudDetectionRule> constructor = ruleTypeClass.getDeclaredConstructor(String.class);
                FraudDetectionRule rule = constructor.newInstance(ruleStr);
                ruleChain.addRule(rule);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException(e);
            }
        }

        return ruleChain;
    }
}
