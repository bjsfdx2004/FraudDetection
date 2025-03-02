package com.hsbc.fd.controller.response;

import com.hsbc.fd.config.ConstConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum ResponseEnum {
    SUCCESS(0, "成功"),
    ERROR(-1, "服务器内部错误"),
    TRANSACTION_GENERATE_ERROR(-100, "超过交易生成的最大数量: " + ConstConfig.MAX_TRANSACTION_GENERATE_COUNT),
    FRAUD_DETECTION_RULE_STR_ERROR(-101, "欺诈检测规则字符串，格式错误，正确格式: 1_666.66-2_1_100_600_1500_10000");


    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;
}
