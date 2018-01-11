package com.sinoufc.jarinvoke.entity;

import java.util.HashMap;
import java.util.Map;

import com.sinoufc.jarinvoke.UFCException;

/**
 * 支付参数类
 * 继承于UFCReqParams
 * @see cn.beecloud.entity.BCReqParams
 */
public class UFCPayReqParams extends UFCReqParams {

    /**
     * 订单总金额
     * 须是正整数, 单位为分
     */
    public Integer totalFee;

    /**
     * 商户订单号
     * 32个字符内, 数字或字母组合, 请自行确保在商户系统中唯一 ,同一订单号不可重复提交, 否则会造成订单重复
     */
    public String billNum;

    /**
     * 订单描述
     * UTF8编码格式, 32个字节内, 最长支持16个汉字
     */
    public String title;

    /**
     * 授权码
     */
    public String authCode;

    /**
     * 机具终端编号, 支付宝条码(ALI_SCAN)的选填参数,
     * 若机具商接入, terminalId(机具终端编号)必填, storeId(商户门店编号)选填
     */
    public String terminalId;

    /**
     * 商户门店编号, 支付宝条码(ALI_SCAN)的选填参数,
     * 若系统商接入, storeId(商户的门店编号)必填, terminalId(机具终端编号)选填
     */
    public String storeId;

    /**
     * 订单超时时间，以秒为单位，选填
     */
    public Integer billTimeout;
    
    /**
     * 异步回调地址
     */
    public String notifyUrl;

    /**
     * 附加数据
     * 用户自定义的参数, 将会在webhook通知中原样返回, 该字段主要用于商户携带订单的自定义数据
     */
    public Map<String, String> optional;

    /**
     * 附加数据
     * 用于后期统计，目前只支持key为category的分类统计
     */
    public Map<String, String> analysis;

    /**
     * 构造函数
     * @param channel       支付渠道类型
     * @throws BCException  父类构造有可能抛出异常
     */
    public UFCPayReqParams(UFCChannelTypes channel) throws UFCException {
        super(channel, ReqType.PAY);
    }

    /**
     * 构造函数
     * @param channel       支付渠道类型
     * @param reqType       请求类型
     * @throws BCException  父类构造有可能抛出异常
     */
    public UFCPayReqParams(UFCChannelTypes channel, ReqType reqType) throws UFCException {
        super(channel, reqType);
    }

    /**
     * 将实例转化成符合后台请求的键值对
     * 用于以json方式post请求
     */
    public Map<String, Object> transToBillReqMapParams(){
        Map<String, Object> params = new HashMap<String, Object>(8);

        params.put("timestamp", getTimestamp());
        params.put("channel", channel.name());
        params.put("payMoney", totalFee);
        params.put("billNum", billNum);
        params.put("body", title);
        
        if (billTimeout != null) {
			params.put("bill_timeout", billTimeout);
		}
        if (notifyUrl != null)
            params.put("notify_url", notifyUrl);

        if (optional !=null && optional.size() != 0)
            params.put("optional", optional);

        if (analysis !=null && analysis.size() != 0)
            params.put("analysis", analysis);

        if (authCode!=null && authCode.length() != 0)
            params.put("auth_code", authCode);

        if (terminalId!=null && terminalId.length() != 0)
            params.put("terminal_id", terminalId);

        if (storeId!=null && storeId.length() != 0)
            params.put("store_id", storeId);

        return params;
    }
}
