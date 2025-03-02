package com.hsbc.fd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 交易实体，负责交易过程的原子操作，包括账户的调度、状态管理
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    /**
     * 交易ID
     */
    private Long id;

    /**
     * 源账户ID
     */
    private Long srcAccountID;

    /**
     * 目的账户ID
     */
    private Long destAccountID;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易类型
     */
    private Type type;

    /**
     * 交易创建时间
     */
    private Long createTimeStamp;

    /**
     * 交易更新时间
     */
    private Long updateTimeStamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public enum Type {
        // 取款
        WITHDRAWAL,
        // 存款
        DEPOSIT,
        // 转账
        TRANSFER
    }
}
