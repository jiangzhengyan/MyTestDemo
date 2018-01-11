package com.sinoufc.jarinvoke;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.sinoufc.jarinvoke.entity.UFCPayReqParams;

/**
 * 用于校验所传参数的类
 * @author Administrator
 *
 */
public class UFCValidationUtil {

	private static final String TAG = "UFCValidationUtil";
	
	/**
	 * 判断字符串是否为空
	 * @param str 待校验字符串
	 * @return
	 */
	private static boolean isValidString(String str){
		return str != null && str.length() != 0;
	}
	
	/**
     * 判断string是否只包含数字和字母, 且长度8-32
     */
	private static boolean isValidBillNum(String str) {
		return str != null && str.length() >= 8 && str.length() <= 32 && str.matches("[A-Za-z0-9]+");
	}
	
	/**
     * 判断字符串是否为有效的正整数
     * @param str 被校验字符串
     */
	static boolean isStringValidPositiveInt(String str) {
		boolean result = false;
		if (isValidString(str)) {
			int len = str.length();
			int i = 0;
			for (; i < len; i++) {
				char c = str.charAt(i);
				if (c <= '/' || c >= ':') {
					break;
				}
			}
			if (i == len) {
				result = true;
			}
		}
		return result;
	}
	
	/**
     * 判断字符串是否为url
     * @param str 被校验字符串
     */
    static boolean isStringValidURL(String str) {
    	return isValidString(str) && (str.toLowerCase().startsWith("http://") || str.toLowerCase().startsWith("https://"));
    }
    
    /**
     * 判断订单长度是否在有效长度以内, 汉字以2个字节计, 总长度最大为32
     * @param str   订单标题
     * @return true代表长度有效
     */
    private static boolean isValidBillTitleLength(String str) {
        if (!isValidString(str))
            return false;

        boolean res = false;

        try {
            byte[] bytes = str.getBytes("GBK");

            if (bytes.length <= 32)
                res = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return res;
    }
    
    /**
     * 校验bill参数
     * 设置公用参数
     *
     * @param billTitle       商品描述, 32个字节内, 汉字以2个字节计
     * @param billTotalFee    支付金额，以分为单位，必须是正整数
     * @param billNum         商户自定义订单号
     * @param parameters      用于存储公用信息
     * @param optional        为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
     * @return 返回校验失败信息, 为null则表明校验通过
     */
    static String prepareParametersForPay(final String billTitle, 
    									  final Integer billTotalFee,
                                          final String billNum, 
                                          final Map<String, String> optional,
                                          UFCPayReqParams parameters) {

        if (!UFCValidationUtil.isValidBillTitleLength(billTitle)) {
            return "parameters: 不合法的参数-订单标题长度不合法, 32个字节内, 汉字以2个字节计";
        }

        if (billTotalFee == null || billTotalFee <= 0) {
            return "parameters: billTotalFee " + billTotalFee +
                    " 格式不正确, 必须是以分为单位的正整数, 比如100表示1元";
        }

        if (!UFCValidationUtil.isValidBillNum(billNum))
            return "parameters: 订单号必须是长度8~32位字母和/或数字组合成的字符串";

        parameters.title = billTitle;
        parameters.totalFee = billTotalFee;
        parameters.billNum = billNum;
        parameters.optional = optional;

        return null;
    }

}
