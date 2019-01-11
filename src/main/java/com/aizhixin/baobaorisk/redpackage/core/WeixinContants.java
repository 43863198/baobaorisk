package com.aizhixin.baobaorisk.redpackage.core;

/**
 * 微信支付及红包业务常用常量
 */
public interface WeixinContants {
    String FEE_TYPE = "CNY";//现金币种，人民币
    String TRADE_TYPE = "JSAPI";//微信付款方式
    String SIGN_TYPE = "MD5";//签名算法

    String SUCCESS = "SUCCESS";//成功状态
    String FAIL = "FAIL";//失败状态

    String BAOBAO_WX_TOKEN = "BAOBAO_WX_TOKEN";//微信token存放属性名称
}
