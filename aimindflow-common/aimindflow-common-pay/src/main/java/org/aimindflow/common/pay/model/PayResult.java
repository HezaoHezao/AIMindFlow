package org.aimindflow.common.pay.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 支付结果信息
 *
 * @author HezaoHezao
 */
@Data
@Accessors(chain = true)
public class PayResult implements Serializable {

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
     * 订单金额（元）
     */
    private BigDecimal amount;

    /**
     * 实付金额（元）
     */
    private BigDecimal actualAmount;

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
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付用户ID
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
     * 支付链接（用于网页支付、二维码支付等）
     */
    private String payUrl;

    /**
     * 支付表单（用于表单提交支付）
     */
    private String payForm;

    /**
     * 支付二维码内容（用于扫码支付）
     */
    private String payQrCode;

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
     * @return 支付结果
     */
    public static PayResult success() {
        return new PayResult().setSuccess(true);
    }

    /**
     * 创建失败结果
     *
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     * @return 支付结果
     */
    public static PayResult fail(String errorCode, String errorMsg) {
        return new PayResult()
                .setSuccess(false)
                .setErrorCode(errorCode)
                .setErrorMsg(errorMsg);
    }
}