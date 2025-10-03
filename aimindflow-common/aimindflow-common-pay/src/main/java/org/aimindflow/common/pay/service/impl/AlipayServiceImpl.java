package org.aimindflow.common.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.pay.constant.PayConstants;
import org.aimindflow.common.pay.model.PayOrder;
import org.aimindflow.common.pay.model.PayResult;
import org.aimindflow.common.pay.model.RefundOrder;
import org.aimindflow.common.pay.model.RefundResult;
import org.aimindflow.common.pay.service.AlipayService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class AlipayServiceImpl extends AbstractPayServiceImpl implements AlipayService {

    /**
     * 支付宝网关地址
     */
    private final String serverUrl;

    /**
     * 应用ID
     */
    private final String appId;

    /**
     * 应用私钥
     */
    private final String privateKey;

    /**
     * 支付宝公钥
     */
    private final String alipayPublicKey;

    /**
     * 签名类型
     */
    private final String signType;

    /**
     * 字符集
     */
    private final String charset;

    /**
     * 支付宝客户端
     */
    @Getter
    private final AlipayClient alipayClient;

    /**
     * 构造方法
     *
     * @param serverUrl       支付宝网关地址
     * @param appId           应用ID
     * @param privateKey      应用私钥
     * @param alipayPublicKey 支付宝公钥
     */
    public AlipayServiceImpl(String serverUrl, String appId, String privateKey, String alipayPublicKey) {
        this(serverUrl, appId, privateKey, alipayPublicKey, PayConstants.DEFAULT_SIGN_TYPE, PayConstants.DEFAULT_CHARSET);
    }

    /**
     * 构造方法
     *
     * @param serverUrl       支付宝网关地址
     * @param appId           应用ID
     * @param privateKey      应用私钥
     * @param alipayPublicKey 支付宝公钥
     * @param signType        签名类型
     * @param charset         字符集
     */
    public AlipayServiceImpl(String serverUrl, String appId, String privateKey, String alipayPublicKey, String signType, String charset) {
        this.serverUrl = serverUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.alipayPublicKey = alipayPublicKey;
        this.signType = signType;
        this.charset = charset;
        this.alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, "json", charset, alipayPublicKey, signType);
    }

    @Override
    public PayResult createOrder(PayOrder payOrder) {
        // 参数校验和处理
        preCreateOrder(payOrder);

        try {
            // 根据支付方式创建不同的支付订单
            String payWay = payOrder.getPayWay();
            if (StrUtil.isBlank(payWay)) {
                return createFailPayResult(PayConstants.ERROR_CODE_PARAM_ERROR, "支付方式不能为空", payOrder);
            }

            PayResult payResult;
            switch (payWay) {
                case PayConstants.PAY_WAY_APP:
                    payResult = createAppPayOrder(payOrder);
                    break;
                case PayConstants.PAY_WAY_H5:
                    payResult = createH5PayOrder(payOrder);
                    break;
                case PayConstants.PAY_WAY_PC:
                    payResult = createPcPayOrder(payOrder);
                    break;
                case PayConstants.PAY_WAY_NATIVE:
                    payResult = createNativePayOrder(payOrder);
                    break;
                case PayConstants.PAY_WAY_JSAPI:
                    payResult = createJsapiPayOrder(payOrder);
                    break;
                default:
                    return createFailPayResult(PayConstants.ERROR_CODE_PARAM_ERROR, "不支持的支付方式: " + payWay, payOrder);
            }

            return payResult;
        } catch (Exception e) {
            log.error("创建支付宝支付订单异常", e);
            return createFailPayResult(PayConstants.ERROR_CODE_SYSTEM_ERROR, e.getMessage(), payOrder);
        }
    }

    /**
     * 创建APP支付订单
     *
     * @param payOrder 支付订单
     * @return 支付结果
     * @throws AlipayApiException 支付宝API异常
     */
    private PayResult createAppPayOrder(PayOrder payOrder) throws AlipayApiException {
        // 创建APP支付请求
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        // 设置异步通知地址
        if (StrUtil.isNotBlank(payOrder.getNotifyUrl())) {
            request.setNotifyUrl(payOrder.getNotifyUrl());
        }

        // 设置业务参数
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(payOrder.getOutTradeNo());
        model.setSubject(payOrder.getSubject());
        model.setTotalAmount(payOrder.getAmount().toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setTimeoutExpress(payOrder.getTimeoutMinutes() + "m");
        if (StrUtil.isNotBlank(payOrder.getAttach())) {
            model.setPassbackParams(payOrder.getAttach());
        }
        request.setBizModel(model);

        // 调用支付宝接口
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        if (response.isSuccess()) {
            // 返回支付参数
            return PayResult.success()
                    .setOutTradeNo(payOrder.getOutTradeNo())
                    .setAmount(payOrder.getAmount())
                    .setPayType(getPayType())
                    .setPayWay(payOrder.getPayWay())
                    .setPayChannel(payOrder.getPayChannel())
                    .setPayStatus(PayConstants.PAY_STATUS_WAIT)
                    .setUserId(payOrder.getUserId())
                    .setAttach(payOrder.getAttach())
                    .setPayUrl(response.getBody())
                    .setRawData(response.getBody())
                    .setExtras(payOrder.getExtras());
        } else {
            return createFailPayResult(response.getCode(), response.getMsg(), payOrder);
        }
    }

    /**
     * 创建H5支付订单
     *
     * @param payOrder 支付订单
     * @return 支付结果
     * @throws AlipayApiException 支付宝API异常
     */
    private PayResult createH5PayOrder(PayOrder payOrder) throws AlipayApiException {
        // 创建H5支付请求
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();

        // 设置异步通知地址
        if (StrUtil.isNotBlank(payOrder.getNotifyUrl())) {
            request.setNotifyUrl(payOrder.getNotifyUrl());
        }

        // 设置同步跳转地址
        if (StrUtil.isNotBlank(payOrder.getReturnUrl())) {
            request.setReturnUrl(payOrder.getReturnUrl());
        }

        // 设置业务参数
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(payOrder.getOutTradeNo());
        model.setSubject(payOrder.getSubject());
        model.setTotalAmount(payOrder.getAmount().toString());
        model.setProductCode("QUICK_WAP_WAY");
        model.setTimeoutExpress(payOrder.getTimeoutMinutes() + "m");
        if (StrUtil.isNotBlank(payOrder.getAttach())) {
            model.setPassbackParams(payOrder.getAttach());
        }
        request.setBizModel(model);

        // 调用支付宝接口
        AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            // 返回支付表单
            return PayResult.success()
                    .setOutTradeNo(payOrder.getOutTradeNo())
                    .setAmount(payOrder.getAmount())
                    .setPayType(getPayType())
                    .setPayWay(payOrder.getPayWay())
                    .setPayChannel(payOrder.getPayChannel())
                    .setPayStatus(PayConstants.PAY_STATUS_WAIT)
                    .setUserId(payOrder.getUserId())
                    .setAttach(payOrder.getAttach())
                    .setPayForm(response.getBody())
                    .setRawData(response.getBody())
                    .setExtras(payOrder.getExtras());
        } else {
            return createFailPayResult(response.getCode(), response.getMsg(), payOrder);
        }
    }

    /**
     * 创建PC支付订单
     *
     * @param payOrder 支付订单
     * @return 支付结果
     * @throws AlipayApiException 支付宝API异常
     */
    private PayResult createPcPayOrder(PayOrder payOrder) throws AlipayApiException {
        // 创建PC支付请求
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 设置异步通知地址
        if (StrUtil.isNotBlank(payOrder.getNotifyUrl())) {
            request.setNotifyUrl(payOrder.getNotifyUrl());
        }

        // 设置同步跳转地址
        if (StrUtil.isNotBlank(payOrder.getReturnUrl())) {
            request.setReturnUrl(payOrder.getReturnUrl());
        }

        // 设置业务参数
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(payOrder.getOutTradeNo());
        model.setSubject(payOrder.getSubject());
        model.setTotalAmount(payOrder.getAmount().toString());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setTimeoutExpress(payOrder.getTimeoutMinutes() + "m");
        if (StrUtil.isNotBlank(payOrder.getAttach())) {
            model.setPassbackParams(payOrder.getAttach());
        }
        request.setBizModel(model);

        // 调用支付宝接口
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            // 返回支付表单
            return PayResult.success()
                    .setOutTradeNo(payOrder.getOutTradeNo())
                    .setAmount(payOrder.getAmount())
                    .setPayType(getPayType())
                    .setPayWay(payOrder.getPayWay())
                    .setPayChannel(payOrder.getPayChannel())
                    .setPayStatus(PayConstants.PAY_STATUS_WAIT)
                    .setUserId(payOrder.getUserId())
                    .setAttach(payOrder.getAttach())
                    .setPayForm(response.getBody())
                    .setRawData(response.getBody())
                    .setExtras(payOrder.getExtras());
        } else {
            return createFailPayResult(response.getCode(), response.getMsg(), payOrder);
        }
    }

    /**
     * 创建扫码支付订单
     *
     * @param payOrder 支付订单
     * @return 支付结果
     * @throws AlipayApiException 支付宝API异常
     */
    private PayResult createNativePayOrder(PayOrder payOrder) throws AlipayApiException {
        // 创建扫码支付请求
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        // 设置异步通知地址
        if (StrUtil.isNotBlank(payOrder.getNotifyUrl())) {
            request.setNotifyUrl(payOrder.getNotifyUrl());
        }

        // 设置业务参数
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(payOrder.getOutTradeNo());
        model.setSubject(payOrder.getSubject());
        model.setTotalAmount(payOrder.getAmount().toString());
        model.setTimeoutExpress(payOrder.getTimeoutMinutes() + "m");
        if (StrUtil.isNotBlank(payOrder.getAttach())) {
            model.setPassbackParams(payOrder.getAttach());
        }
        request.setBizModel(model);

        // 调用支付宝接口
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            // 返回支付二维码
            return PayResult.success()
                    .setOutTradeNo(payOrder.getOutTradeNo())
                    .setAmount(payOrder.getAmount())
                    .setPayType(getPayType())
                    .setPayWay(payOrder.getPayWay())
                    .setPayChannel(payOrder.getPayChannel())
                    .setPayStatus(PayConstants.PAY_STATUS_WAIT)
                    .setUserId(payOrder.getUserId())
                    .setAttach(payOrder.getAttach())
                    .setPayQrCode(response.getQrCode())
                    .setRawData(response.getBody())
                    .setExtras(payOrder.getExtras());
        } else {
            return createFailPayResult(response.getCode(), response.getMsg(), payOrder);
        }
    }

    /**
     * 创建JSAPI支付订单（小程序、生活号等）
     *
     * @param payOrder 支付订单
     * @return 支付结果
     * @throws AlipayApiException 支付宝API异常
     */
    private PayResult createJsapiPayOrder(PayOrder payOrder) throws AlipayApiException {
        // 创建JSAPI支付请求
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();

        // 设置异步通知地址
        if (StrUtil.isNotBlank(payOrder.getNotifyUrl())) {
            request.setNotifyUrl(payOrder.getNotifyUrl());
        }

        // 设置业务参数
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(payOrder.getOutTradeNo());
        model.setSubject(payOrder.getSubject());
        model.setTotalAmount(payOrder.getAmount().toString());
        model.setTimeoutExpress(payOrder.getTimeoutMinutes() + "m");
        // 设置买家ID（必填）
        if (StrUtil.isNotBlank(payOrder.getBuyerId())) {
            model.setBuyerId(payOrder.getBuyerId());
        } else {
            return createFailPayResult(PayConstants.ERROR_CODE_PARAM_ERROR, "JSAPI支付必须传入买家ID", payOrder);
        }
        if (StrUtil.isNotBlank(payOrder.getAttach())) {
            model.setPassbackParams(payOrder.getAttach());
        }
        request.setBizModel(model);

        // 调用支付宝接口
        AlipayTradeCreateResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            // 返回支付交易号
            Map<String, Object> payParams = new HashMap<>();
            payParams.put("tradeNo", response.getTradeNo());

            return PayResult.success()
                    .setOutTradeNo(payOrder.getOutTradeNo())
                    .setTradeNo(response.getTradeNo())
                    .setAmount(payOrder.getAmount())
                    .setPayType(getPayType())
                    .setPayWay(payOrder.getPayWay())
                    .setPayChannel(payOrder.getPayChannel())
                    .setPayStatus(PayConstants.PAY_STATUS_WAIT)
                    .setUserId(payOrder.getUserId())
                    .setBuyerId(payOrder.getBuyerId())
                    .setAttach(payOrder.getAttach())
                    .setRawData(response.getBody())
                    .setExtras(payParams);
        } else {
            return createFailPayResult(response.getCode(), response.getMsg(), payOrder);
        }
    }

    @Override
    public PayResult queryOrder(String outTradeNo) {
        try {
            // 创建订单查询请求
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

            // 设置业务参数
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(outTradeNo);
            request.setBizModel(model);

            // 调用支付宝接口
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                // 解析支付状态
                String tradeStatus = response.getTradeStatus();
                String payStatus;
                boolean success = false;

                switch (tradeStatus) {
                    case "TRADE_SUCCESS":
                    case "TRADE_FINISHED":
                        payStatus = PayConstants.PAY_STATUS_SUCCESS;
                        success = true;
                        break;
                    case "TRADE_CLOSED":
                        payStatus = PayConstants.PAY_STATUS_CLOSED;
                        break;
                    case "WAIT_BUYER_PAY":
                    default:
                        payStatus = PayConstants.PAY_STATUS_WAIT;
                        break;
                }

                // 构建支付结果
                PayResult result = new PayResult()
                        .setSuccess(success)
                        .setOutTradeNo(outTradeNo)
                        .setTradeNo(response.getTradeNo())
                        .setAmount(new BigDecimal(response.getTotalAmount()))
                        .setPayType(getPayType())
                        .setPayStatus(payStatus)
                        .setBuyerId(response.getBuyerUserId())
                        .setSellerId(response.getSellerId())
                        .setRawData(response.getBody());

                // 设置支付时间
                if (StrUtil.isNotBlank(response.getSendPayDate())) {
                    result.setPayTime(DateUtil.parse(response.getSendPayDate()));
                }

                return result;
            } else {
                // 订单不存在
                if ("40004".equals(response.getCode()) && response.getSubCode().contains("TRADE_NOT_EXIST")) {
                    return PayResult.fail(PayConstants.ERROR_CODE_ORDER_NOT_EXIST, PayConstants.ERROR_MESSAGE_ORDER_NOT_EXIST)
                            .setOutTradeNo(outTradeNo)
                            .setPayType(getPayType());
                }
                return PayResult.fail(response.getCode(), response.getMsg())
                        .setOutTradeNo(outTradeNo)
                        .setPayType(getPayType())
                        .setRawData(response.getBody());
            }
        } catch (Exception e) {
            log.error("查询支付宝订单异常", e);
            return PayResult.fail(PayConstants.ERROR_CODE_SYSTEM_ERROR, e.getMessage())
                    .setOutTradeNo(outTradeNo)
                    .setPayType(getPayType());
        }
    }

    @Override
    public PayResult closeOrder(String outTradeNo) {
        try {
            // 创建订单关闭请求
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();

            // 设置业务参数
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            model.setOutTradeNo(outTradeNo);
            request.setBizModel(model);

            // 调用支付宝接口
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return PayResult.success()
                        .setOutTradeNo(outTradeNo)
                        .setTradeNo(response.getTradeNo())
                        .setPayType(getPayType())
                        .setPayStatus(PayConstants.PAY_STATUS_CLOSED)
                        .setRawData(response.getBody());
            } else {
                // 订单不存在或已关闭
                if ("40004".equals(response.getCode()) && response.getSubCode().contains("TRADE_NOT_EXIST")) {
                    return PayResult.success()
                            .setOutTradeNo(outTradeNo)
                            .setPayType(getPayType())
                            .setPayStatus(PayConstants.PAY_STATUS_CLOSED);
                }
                return PayResult.fail(response.getCode(), response.getMsg())
                        .setOutTradeNo(outTradeNo)
                        .setPayType(getPayType())
                        .setRawData(response.getBody());
            }
        } catch (Exception e) {
            log.error("关闭支付宝订单异常", e);
            return PayResult.fail(PayConstants.ERROR_CODE_SYSTEM_ERROR, e.getMessage())
                    .setOutTradeNo(outTradeNo)
                    .setPayType(getPayType());
        }
    }

    @Override
    public RefundResult refund(RefundOrder refundOrder) {
        // 参数校验和处理
        preRefundOrder(refundOrder);

        try {
            // 创建退款请求
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

            // 设置业务参数
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(refundOrder.getOutTradeNo());
            model.setTradeNo(refundOrder.getTradeNo());
            model.setOutRequestNo(refundOrder.getOutRefundNo());
            model.setRefundAmount(refundOrder.getRefundAmount().toString());
            model.setRefundReason(refundOrder.getRefundReason());
            request.setBizModel(model);

            // 调用支付宝接口
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return RefundResult.success()
                        .setOutTradeNo(refundOrder.getOutTradeNo())
                        .setTradeNo(refundOrder.getTradeNo())
                        .setOutRefundNo(refundOrder.getOutRefundNo())
                        .setTotalAmount(refundOrder.getTotalAmount())
                        .setRefundAmount(refundOrder.getRefundAmount())
                        .setRefundStatus(PayConstants.REFUND_STATUS_SUCCESS)
                        .setRefundTime(new Date())
                        .setRefundType(getPayType())
                        .setRefundReason(refundOrder.getRefundReason())
                        .setUserId(refundOrder.getUserId())
                        .setBuyerId(response.getBuyerUserId())
                        .setSellerId(response.getSellerId())
                        .setAttach(refundOrder.getAttach())
                        .setRawData(response.getBody())
                        .setExtras(refundOrder.getExtras());
            } else {
                return createFailRefundResult(response.getCode(), response.getMsg(), refundOrder)
                        .setRawData(response.getBody());
            }
        } catch (Exception e) {
            log.error("申请支付宝退款异常", e);
            return createFailRefundResult(PayConstants.ERROR_CODE_SYSTEM_ERROR, e.getMessage(), refundOrder);
        }
    }

    @Override
    public RefundResult queryRefund(String outTradeNo, String outRefundNo) {
        try {
            // 创建退款查询请求
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();

            // 设置业务参数
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            model.setOutTradeNo(outTradeNo);
            model.setOutRequestNo(outRefundNo);
            request.setBizModel(model);

            // 调用支付宝接口
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                // 解析退款状态
                String refundStatus = PayConstants.REFUND_STATUS_SUCCESS;

                // 构建退款结果
                RefundResult result = new RefundResult()
                        .setSuccess(true)
                        .setOutTradeNo(outTradeNo)
                        .setTradeNo(response.getTradeNo())
                        .setOutRefundNo(outRefundNo)
                        .setRefundAmount(new BigDecimal(response.getRefundAmount()))
                        .setTotalAmount(new BigDecimal(response.getTotalAmount()))
                        .setRefundStatus(refundStatus)
                        .setRefundType(getPayType())
                        .setRawData(response.getBody());

                // 设置退款时间
                if (StrUtil.isNotBlank(response.getGmtRefundPay())) {
                    result.setRefundTime(DateUtil.parse(response.getGmtRefundPay()));
                }

                return result;
            } else {
                // 退款不存在
                if ("40004".equals(response.getCode()) && response.getSubCode().contains("TRADE_NOT_EXIST")) {
                    return RefundResult.fail(PayConstants.ERROR_CODE_ORDER_NOT_EXIST, PayConstants.ERROR_MESSAGE_ORDER_NOT_EXIST)
                            .setOutTradeNo(outTradeNo)
                            .setOutRefundNo(outRefundNo);
                }
                return RefundResult.fail(response.getCode(), response.getMsg())
                        .setOutTradeNo(outTradeNo)
                        .setOutRefundNo(outRefundNo)
                        .setRawData(response.getBody());
            }
        } catch (Exception e) {
            log.error("查询支付宝退款异常", e);
            return RefundResult.fail(PayConstants.ERROR_CODE_SYSTEM_ERROR, e.getMessage())
                    .setOutTradeNo(outTradeNo)
                    .setOutRefundNo(outRefundNo);
        }
    }

    @Override
    public boolean verifyNotifySign(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV1(params, alipayPublicKey, charset, signType);
        } catch (AlipayApiException e) {
            log.error("验证支付宝异步通知签名异常", e);
            return false;
        }
    }

    @Override
    public PayResult parseNotify(Map<String, String> params) {
        try {
            // 验证签名
            if (!verifyNotifySign(params)) {
                return PayResult.fail(PayConstants.ERROR_CODE_SIGN_ERROR, PayConstants.ERROR_MESSAGE_SIGN_ERROR);
            }

            // 解析通知参数
            String outTradeNo = params.get("out_trade_no");
            String tradeNo = params.get("trade_no");
            String tradeStatus = params.get("trade_status");
            String totalAmount = params.get("total_amount");
            String buyerId = params.get("buyer_id");
            String sellerId = params.get("seller_id");
            String appId = params.get("app_id");
            String notifyTime = params.get("notify_time");

            // 验证应用ID
            if (!this.appId.equals(appId)) {
                return PayResult.fail(PayConstants.ERROR_CODE_PARAM_ERROR, "应用ID不匹配");
            }

            // 解析支付状态
            String payStatus;
            boolean success = false;

            switch (tradeStatus) {
                case "TRADE_SUCCESS":
                case "TRADE_FINISHED":
                    payStatus = PayConstants.PAY_STATUS_SUCCESS;
                    success = true;
                    break;
                case "TRADE_CLOSED":
                    payStatus = PayConstants.PAY_STATUS_CLOSED;
                    break;
                default:
                    payStatus = PayConstants.PAY_STATUS_WAIT;
                    break;
            }

            // 构建支付结果
            PayResult result = new PayResult()
                    .setSuccess(success)
                    .setOutTradeNo(outTradeNo)
                    .setTradeNo(tradeNo)
                    .setAmount(new BigDecimal(totalAmount))
                    .setPayType(getPayType())
                    .setPayStatus(payStatus)
                    .setBuyerId(buyerId)
                    .setSellerId(sellerId)
                    .setRawData(MapUtil.join(params, "=", "&"));

            // 设置支付时间
            if (StrUtil.isNotBlank(notifyTime)) {
                result.setPayTime(DateUtil.parse(notifyTime));
            }

            // 设置附加数据
            String passbackParams = params.get("passback_params");
            if (StrUtil.isNotBlank(passbackParams)) {
                result.setAttach(passbackParams);
            }

            return result;
        } catch (Exception e) {
            log.error("解析支付宝异步通知异常", e);
            return PayResult.fail(PayConstants.ERROR_CODE_SYSTEM_ERROR, e.getMessage());
        }
    }
}