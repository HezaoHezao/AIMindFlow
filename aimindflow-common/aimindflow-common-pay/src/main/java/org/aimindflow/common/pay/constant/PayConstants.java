package org.aimindflow.common.pay.constant;

/**
 * 支付常量
 *
 * @author HezaoHezao
 */
public class PayConstants {

    /**
     * 支付类型
     */
    public static final String PAY_TYPE_ALIPAY = "alipay";  // 支付宝
    public static final String PAY_TYPE_WECHAT = "wechat";  // 微信支付
    public static final String PAY_TYPE_UNIONPAY = "unionpay";  // 银联支付

    /**
     * 支付方式
     */
    public static final String PAY_WAY_APP = "app";  // APP支付
    public static final String PAY_WAY_H5 = "h5";  // H5支付
    public static final String PAY_WAY_JSAPI = "jsapi";  // JSAPI支付（公众号、小程序）
    public static final String PAY_WAY_NATIVE = "native";  // 原生扫码支付
    public static final String PAY_WAY_PC = "pc";  // PC网页支付
    public static final String PAY_WAY_FACE = "face";  // 刷脸支付
    public static final String PAY_WAY_BARCODE = "barcode";  // 条码支付（付款码）

    /**
     * 支付状态
     */
    public static final String PAY_STATUS_WAIT = "wait";  // 等待支付
    public static final String PAY_STATUS_SUCCESS = "success";  // 支付成功
    public static final String PAY_STATUS_CLOSED = "closed";  // 已关闭
    public static final String PAY_STATUS_REFUND = "refund";  // 已退款
    public static final String PAY_STATUS_FAILED = "failed";  // 支付失败

    /**
     * 退款状态
     */
    public static final String REFUND_STATUS_PROCESSING = "processing";  // 退款处理中
    public static final String REFUND_STATUS_SUCCESS = "success";  // 退款成功
    public static final String REFUND_STATUS_FAILED = "failed";  // 退款失败

    /**
     * 错误码
     */
    public static final String ERROR_CODE_PARAM_ERROR = "PARAM_ERROR";  // 参数错误
    public static final String ERROR_CODE_CONFIG_ERROR = "CONFIG_ERROR";  // 配置错误
    public static final String ERROR_CODE_SIGN_ERROR = "SIGN_ERROR";  // 签名错误
    public static final String ERROR_CODE_NETWORK_ERROR = "NETWORK_ERROR";  // 网络错误
    public static final String ERROR_CODE_SYSTEM_ERROR = "SYSTEM_ERROR";  // 系统错误
    public static final String ERROR_CODE_ORDER_NOT_EXIST = "ORDER_NOT_EXIST";  // 订单不存在
    public static final String ERROR_CODE_ORDER_CLOSED = "ORDER_CLOSED";  // 订单已关闭
    public static final String ERROR_CODE_ORDER_PAID = "ORDER_PAID";  // 订单已支付
    public static final String ERROR_CODE_AMOUNT_ERROR = "AMOUNT_ERROR";  // 金额错误
    public static final String ERROR_CODE_REFUND_ERROR = "REFUND_ERROR";  // 退款错误

    /**
     * 错误信息
     */
    public static final String ERROR_MESSAGE_PARAM_ERROR = "参数错误";  // 参数错误
    public static final String ERROR_MESSAGE_CONFIG_ERROR = "配置错误";  // 配置错误
    public static final String ERROR_MESSAGE_SIGN_ERROR = "签名错误";  // 签名错误
    public static final String ERROR_MESSAGE_NETWORK_ERROR = "网络错误";  // 网络错误
    public static final String ERROR_MESSAGE_SYSTEM_ERROR = "系统错误";  // 系统错误
    public static final String ERROR_MESSAGE_ORDER_NOT_EXIST = "订单不存在";  // 订单不存在
    public static final String ERROR_MESSAGE_ORDER_CLOSED = "订单已关闭";  // 订单已关闭
    public static final String ERROR_MESSAGE_ORDER_PAID = "订单已支付";  // 订单已支付
    public static final String ERROR_MESSAGE_AMOUNT_ERROR = "金额错误";  // 金额错误
    public static final String ERROR_MESSAGE_REFUND_ERROR = "退款错误";  // 退款错误

    /**
     * 默认超时时间（分钟）
     */
    public static final int DEFAULT_TIMEOUT_MINUTES = 30;

    /**
     * 默认字符集
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 默认签名类型
     */
    public static final String DEFAULT_SIGN_TYPE = "RSA2";
}