package org.aimindflow.common.pay.service;

/**
 * 银联支付服务接口
 *
 * @author HezaoHezao
 */
public interface UnionPayService extends PayService {

    /**
     * 验证银联支付异步通知签名
     *
     * @param params 通知参数
     * @return 验证结果
     */
    boolean verifyNotifySign(java.util.Map<String, String> params);

    /**
     * 获取银联支付类型
     *
     * @return 支付类型
     */
    @Override
    default String getPayType() {
        return org.aimindflow.common.pay.constant.PayConstants.PAY_TYPE_UNIONPAY;
    }
}