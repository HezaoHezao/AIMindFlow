package org.aimindflow.common.pay.service;

import com.alipay.api.AlipayClient;

/**
 * 支付宝支付服务接口
 *
 * @author HezaoHezao
 */
public interface AlipayService extends PayService {

    /**
     * 获取支付宝客户端
     *
     * @return 支付宝客户端
     */
    AlipayClient getAlipayClient();

    /**
     * 验证支付宝异步通知签名
     *
     * @param params 通知参数
     * @return 验证结果
     */
    boolean verifyNotifySign(java.util.Map<String, String> params);

    /**
     * 获取支付宝支付类型
     *
     * @return 支付类型
     */
    @Override
    default String getPayType() {
        return org.aimindflow.common.pay.constant.PayConstants.PAY_TYPE_ALIPAY;
    }
}