package org.aimindflow.common.pay.service;

import org.aimindflow.common.pay.model.PayOrder;
import org.aimindflow.common.pay.model.PayResult;
import org.aimindflow.common.pay.model.RefundOrder;
import org.aimindflow.common.pay.model.RefundResult;

/**
 * 支付服务接口
 *
 * @author HezaoHezao
 */
public interface PayService {

    /**
     * 创建支付订单
     *
     * @param payOrder 支付订单信息
     * @return 支付结果
     */
    PayResult createOrder(PayOrder payOrder);

    /**
     * 查询支付订单
     *
     * @param outTradeNo 商户订单号
     * @return 支付结果
     */
    PayResult queryOrder(String outTradeNo);

    /**
     * 关闭支付订单
     *
     * @param outTradeNo 商户订单号
     * @return 是否成功
     */
    boolean closeOrder(String outTradeNo);

    /**
     * 申请退款
     *
     * @param refundOrder 退款订单信息
     * @return 退款结果
     */
    RefundResult refund(RefundOrder refundOrder);

    /**
     * 查询退款
     *
     * @param outRefundNo 商户退款单号
     * @return 退款结果
     */
    RefundResult queryRefund(String outRefundNo);

    /**
     * 验证支付回调通知
     *
     * @param notifyData 回调通知数据
     * @return 支付结果
     */
    PayResult verifyNotify(String notifyData);

    /**
     * 获取支付类型
     *
     * @return 支付类型
     */
    String getPayType();
}