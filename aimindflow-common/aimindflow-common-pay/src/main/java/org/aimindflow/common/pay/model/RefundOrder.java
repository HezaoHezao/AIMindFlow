package org.aimindflow.common.pay.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 退款订单信息
 *
 * @author HezaoHezao
 */
@Data
@Accessors(chain = true)
public class RefundOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 支付平台交易号
     */
    private String tradeNo;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 订单金额（元）
     */
    private BigDecimal totalAmount;

    /**
     * 退款金额（元）
     */
    private BigDecimal refundAmount;

    /**
     * 退款类型
     */
    private String refundType;

    /**
     * 退款结果异步通知地址
     */
    private String notifyUrl;

    /**
     * 退款申请时间
     */
    private Date createTime;

    /**
     * 退款用户ID
     */
    private String userId;

    /**
     * 退款用户IP
     */
    private String userIp;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 扩展参数
     */
    private Map<String, Object> extras;
}