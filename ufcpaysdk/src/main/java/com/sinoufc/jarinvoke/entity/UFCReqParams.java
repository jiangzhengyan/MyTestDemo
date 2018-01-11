package com.sinoufc.jarinvoke.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sinoufc.jarinvoke.UFCException;

/**
 * 向服务端请求的基类
 * 包含请求的公用参数
 */
public class UFCReqParams {

    /**
     * 渠道类型
     * 根据不同场景选择不同的支付方式
     */
    public UFCChannelTypes channel;
    
    /** 时间戳, 毫秒数 */
	private long timestamp;

	/** 时间戳, 毫秒数 */
    public long getTimestamp() {
		return timestamp;
	}

	/**
     * 请求类型
     */
    public enum ReqType{
        //正常的APP方式支付
        PAY,
        //查询操作
        QUERY,
        }

    /**
     * 渠道支付类型
     */
    public enum UFCChannelTypes {
        /**
         * 所有渠道
         * 仅用于查询订单
         */
        ALL,
        
        /**
         * 余额原生APP支付
         */
        BL_APP,

        /**
         * 微信所有渠道
         * 仅用于查询订单
         */
        WX,

        /**
         * 微信手机原生APP支付
         */
        WX_APP,

        /**
         * 支付宝所有渠道
         * 仅用于查询订单
         */
        ALI,

        /**
         * 支付宝手机原生APP支付
         */
        ALI_APP,

        /**
         * 支付宝PC网页支付
         * 仅用于查询订单
         */
        ALI_WEB,

        /**
         * 银联所有渠道
         * 仅用于查询订单
         */
        UN,

        /**
         * 银联手机原生APP支付
         */
        UN_APP,

        /**
         * 银联PC网页支付
         * 仅用于查询订单
         */
        UN_WEB,

        /**
         * 快捷原生APP支付
         */
        QK_APP;

        /**
         * 判断是否为有效的app端支付渠道类型
         *
         * @param channel 支付渠道类型
         * @return true表示有效
         */
        public static boolean isValidAPPPaymentChannelType(UFCChannelTypes channel) {
            return channel == BL_APP ||
            	   channel == WX_APP ||
                   channel == ALI_APP ||
                   channel == UN_APP ||
                   channel == UN_WEB ||
                   channel == QK_APP;
        }

        /**
         * @param channel 支付渠道类型
         * @return 实际的渠道支付名
         */
        public static String getTranslatedChannelName(String channel) {
        	if (channel.equals(BL_APP.name()))
        		return "余额支付";
    		else if (channel.equals(WX.name()))
                return "微信支付";
            else if (channel.equals(WX_APP.name()))
                return "微信手机原生APP支付";
            else if (channel.equals(ALI.name()))
                return "支付宝支付";
            else if (channel.equals(ALI_APP.name()))
                return "支付宝手机原生APP支付";
            else if (channel.equals(ALI_WEB.name()))
                return "支付宝PC网页支付";
            else if (channel.equals(UN.name()))
                return "银联支付";
            else if (channel.equals(UN_APP.name()))
                return "银联手机原生APP支付";
            else if (channel.equals(QK_APP.name())) {
				return "快捷支付";
			}
                return "其他支付类型";
        }
    }

    /**
     * 初始化参数
     * @param channel   渠道类型
     * @param reqType   请求类型
     */
    public UFCReqParams(UFCChannelTypes channel, ReqType reqType) throws UFCException{

        if (reqType == ReqType.PAY && (channel == null ||
                !UFCChannelTypes.isValidAPPPaymentChannelType(channel)))
            throw new UFCException("非法APP支付渠道");

        this.timestamp = (new Date()).getTime();
        this.channel = channel;
    }

    /**
     * 将实例转化成符合后台请求的键值对
     * 用于以json方式post请求
     */
    public Map<String, Object> transToReqMapParams() {
        Map<String, Object> params = new HashMap<String, Object>(8);

        params.put("channel", channel.name());

        return params;
    }
}
