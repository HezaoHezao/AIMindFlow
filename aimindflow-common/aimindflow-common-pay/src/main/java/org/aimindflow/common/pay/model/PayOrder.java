package org.aimindflow.common.pay.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 支付订单信息
 *
 * @author HezaoHezao
 */
@Data
@Accessors(chain = true)
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 订单描述
     */
    private String body;

    /**
     * 订单金额（元）
     */
    private BigDecimal amount;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 支付终端
     */
    private String payTerminal;

    /**
     * 支付超时时间（分钟）
     */
    private Integer timeoutMinutes;

    /**
     * 支付成功后的跳转地址
     */
    private String returnUrl;

    /**
     * 支付结果异步通知地址
     */
    private String notifyUrl;

    /**
     * 支付用户ID
     */
    private String userId;

    /**
     * 支付用户IP
     */
    private String userIp;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 订单过期时间
     */
    private Date expireTime;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 扩展参数
     */
    private Map<String, Object> extras;
}