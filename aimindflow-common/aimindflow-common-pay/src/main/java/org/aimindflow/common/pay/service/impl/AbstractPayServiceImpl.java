package org.aimindflow.common.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.aimindflow.common.pay.constant.PayConstants;
import org.aimindflow.common.pay.model.PayOrder;
import org.aimindflow.common.pay.model.PayResult;
import org.aimindflow.common.pay.model.RefundOrder;
import org.aimindflow.common.pay.model.RefundResult;
import org.aimindflow.common.pay.service.PayService;

import java.util.Date;

/**
 * 抽象支付服务实现
 *
 * @author HezaoHezao
 */
public abstract class AbstractPayServiceImpl implements PayService {

    /**
     * 创建支付订单前的参数校验和处理
     *
     * @param payOrder 支付订单
     */
    protected void preCreateOrder(PayOrder payOrder) {
        // 检查订单号
        if (StrUtil.isBlank(payOrder.getOutTradeNo())) {
            payOrder.setOutTradeNo(IdUtil.fastSimpleUUID());
        }

        // 检查订单标题
        if (StrUtil.isBlank(payOrder.getSubject())) {
            payOrder.setSubject("订单支付");
        }

        // 检查支付类型
        if (StrUtil.isBlank(payOrder.getPayType())) {
            payOrder.setPayType(getPayType());
        }

        // 检查创建时间
        if (payOrder.getCreateTime() == null) {
            payOrder.setCreateTime(new Date());
        }

        // 检查过期时间
        if (payOrder.getExpireTime() == null && payOrder.getTimeoutMinutes() > 0) {
            payOrder.setExpireTime(DateUtil.offsetMinute(payOrder.getCreateTime(), payOrder.getTimeoutMinutes()));
        }

        // 检查超时时间（分钟）
        if (payOrder.getTimeoutMinutes() <= 0) {
            payOrder.setTimeoutMinutes(PayConstants.DEFAULT_TIMEOUT_MINUTES);
        }
    }

    /**
     * 创建退款订单前的参数校验和处理
     *
     * @param refundOrder 退款订单
     */
    protected void preRefundOrder(RefundOrder refundOrder) {
        // 检查退款单号
        if (StrUtil.isBlank(refundOrder.getOutRefundNo())) {
            refundOrder.setOutRefundNo(IdUtil.fastSimpleUUID());
        }

        // 检查退款原因
        if (StrUtil.isBlank(refundOrder.getRefundReason())) {
            refundOrder.setRefundReason("用户退款");
        }

        // 检查退款类型
        if (StrUtil.isBlank(refundOrder.getRefundType())) {
            refundOrder.setRefundType(getPayType());
        }

        // 检查创建时间
        if (refundOrder.getCreateTime() == null) {
            refundOrder.setCreateTime(new Date());
        }
    }

    /**
     * 创建支付失败结果
     *
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     * @param payOrder  支付订单
     * @return 支付结果
     */
    protected PayResult createFailPayResult(String errorCode, String errorMsg, PayOrder payOrder) {
        PayResult result = PayResult.fail(errorCode, errorMsg);
        if (payOrder != null) {
            result.setOutTradeNo(payOrder.getOutTradeNo())
                    .setAmount(payOrder.getAmount())
                    .setPayType(payOrder.getPayType())
                    .setPayWay(payOrder.getPayWay())
                    .setPayChannel(payOrder.getPayChannel())
                    .setUserId(payOrder.getUserId())
                    .setAttach(payOrder.getAttach())
                    .setExtras(payOrder.getExtras());
        }
        return result;
    }

    /**
     * 创建退款失败结果
     *
     * @param errorCode   错误码
     * @param errorMsg    错误信息
     * @param refundOrder 退款订单
     * @return 退款结果
     */
    protected RefundResult createFailRefundResult(String errorCode, String errorMsg, RefundOrder refundOrder) {
        RefundResult result = RefundResult.fail(errorCode, errorMsg);
        if (refundOrder != null) {
            result.setOutTradeNo(refundOrder.getOutTradeNo())
                    .setTradeNo(refundOrder.getTradeNo())
                    .setOutRefundNo(refundOrder.getOutRefundNo())
                    .setTotalAmount(refundOrder.getTotalAmount())
                    .setRefundAmount(refundOrder.getRefundAmount())
                    .setRefundType(refundOrder.getRefundType())
                    .setRefundReason(refundOrder.getRefundReason())
                    .setUserId(refundOrder.getUserId())
                    .setAttach(refundOrder.getAttach())
                    .setExtras(refundOrder.getExtras());
        }
        return result;
    }
}