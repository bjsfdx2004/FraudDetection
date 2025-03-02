package com.hsbc.fd.rule;

import com.hsbc.fd.entity.Transaction;

public interface FraudDetectionRule {
    /**
     * 进行欺诈检测
     *
     * @param t 待检测的交易
     * @return 是否通过检测
     */
    boolean doFraudDetection(Transaction t);
}
