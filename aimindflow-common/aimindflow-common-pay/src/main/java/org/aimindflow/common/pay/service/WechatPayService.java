package org.aimindflow.common.pay.service;

/**
 * 微信支付服务接口
 *
 * @author HezaoHezao
 */
public interface WechatPayService extends PayService {

    /**
     * 验证微信支付异步通知签名
     *
     * @param notifyData 通知数据
     * @param signature  签名
     * @param nonce      随机字符串
     * @param timestamp  时间戳
     * @return 验证结果
     */
    boolean verifyNotifySign(String notifyData, String signature, String nonce, String timestamp);

    /**
     * 获取微信支付类型
     *
     * @return 支付类型
     */
    @Override
    default String getPayType() {
        return org.aimindflow.common.pay.constant.PayConstants.PAY_TYPE_WECHAT;
    }
}