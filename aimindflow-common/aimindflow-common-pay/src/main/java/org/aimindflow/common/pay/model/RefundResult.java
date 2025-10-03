package org.aimindflow.common.pay.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 退款结果信息
 *
 * @author HezaoHezao
 */
@Data
@Accessors(chain = true)
public class RefundResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

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
     * 支付平台退款单号
     */
    private String refundNo;

    /**
     * 订单金额（元）
     */
    private BigDecimal totalAmount;

    /**
     * 退款金额（元）
     */
    private BigDecimal refundAmount;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 退款类型
     */
    private String refundType;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款用户ID
     */
    private String userId;

    /**
     * 买家支付账号
     */
    private String buyerId;

    /**
     * 卖家收款账号
     */
    private String sellerId;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 原始响应数据
     */
    private String rawData;

    /**
     * 扩展参数
     */
    private Map<String, Object> extras;

    /**
     * 创建成功结果
     *
     * @return 退款结果
     */
    public static RefundResult success() {
        return new RefundResult().setSuccess(true);
    }

    /**
     * 创建失败结果
     *
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     * @return 退款结果
     */
    public static RefundResult fail(String errorCode, String errorMsg) {
        return new RefundResult()
                .setSuccess(false)
                .setErrorCode(errorCode)
                .setErrorMsg(errorMsg);
    }
}