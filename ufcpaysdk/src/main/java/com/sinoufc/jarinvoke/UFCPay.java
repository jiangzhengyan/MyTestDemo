package com.sinoufc.jarinvoke;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sinoufc.jarinvoke.UFCHttpClientUtil.Response;
import com.sinoufc.jarinvoke.async.UFCCallback;
import com.sinoufc.jarinvoke.bean.PayInfo;
import com.sinoufc.jarinvoke.entity.UFCPayReqParams;
import com.sinoufc.jarinvoke.entity.UFCPayResult;
import com.sinoufc.jarinvoke.entity.UFCReqParams;
import com.sinoufc.jarinvoke.entity.UFCReqParams.UFCChannelTypes;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 支付类
 * 单例模式
 */
public class UFCPay {
	
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			 if(msg.what == 1){
				 
		     }
		};
	};

	private static final String TAG = "UFCPay";
	
	/**
     * 保留callback实例
     */
    static UFCCallback payCallback;
    
	static Activity mContexActivity;
	// IWXAPI 是第三方app和微信通信的openapi接口
    static IWXAPI wxAPI = null;
    /**
     * 微信App Id
     */
    public String wxAppId;
	
	private static UFCPay instance;

	public UFCPay() {
		
	}
	
	/**
     * 唯一获取UFCPay实例的入口
     * @param context   留存context
     * @return UFCPay实例
     */
	public static UFCPay getInstance(Context context) {
		if (instance == null) {
			instance = new UFCPay();
			payCallback = null;
		}
		if (context != null) {
			mContexActivity = (Activity) context;
		}
		return instance;
	}

	/**
	 * 释放UFCPay占据的Contex,callback引用
	 */
	public static void clear(){
		mContexActivity = null;
		payCallback = null;
	}
	
	public static String initWechatPay(Context context, String wechatAppID) {
		String errMsg = null;
		if (null == context) {
			errMsg = "Error: initWechatPay中，context参数不能为空.";
			Log.e(TAG, errMsg);
			return errMsg;
		}
		
		if (null == wechatAppID || wechatAppID.length() == 0) {
			errMsg = "Error: initWechatPay中，wechatAppID参数不能为空.";
			Log.e(TAG, errMsg);
			return errMsg;
		}
		
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		wxAPI = WXAPIFactory.createWXAPI(context, null);
		UFCConstants.WXAPP_ID = wechatAppID;
		
		try {
			if (isWechatPaySupported()) {
				// 将该app注册到微信
				wxAPI.registerApp(wechatAppID);
			} else {
				errMsg = "Error: 所安装的微信不支持支付.";
				Log.e(TAG, errMsg);
			}
		} catch (Exception e) {
			errMsg = "Error: 无法注册到微信 " + wechatAppID + ". Exception: " + e.getMessage();
			Log.e(TAG, errMsg);
		}
		
		return errMsg;
	}
	
	/**
	 * 释放微信接口占用
	 */
	public static void detatchWechat() {
		if (null != wxAPI) {
			wxAPI.detach();
			wxAPI = null;
		}
	}
	
	/**
	 * 判断微信是否支付支付
	 * @return true 表示支付
	 */
	public static boolean isWechatPaySupported(){
		boolean isPaySupported = false;
		if (null != wxAPI) {
			isPaySupported = wxAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		}
		return isPaySupported;
	}
	
	/**
	 * 判断微信应用是否安装并支持
	 * @return 
	 */
	public static boolean isWechatInstalledAndSupported() {
		boolean isWechatInstalledAndSupported = false;
		if (null != wxAPI) {
			isWechatInstalledAndSupported = wxAPI.isWXAppInstalled() && wxAPI.isWXAppSupportAPI();
		}
		return isWechatInstalledAndSupported;
	}
	
	/**
     * 支付调用总接口
     *
     * @param payParam        支付参数
     * @param callback        支付完成后的回调函数
     */
	public void reqPaymentAsync(final PayParams payParam, final UFCCallback callback) {
		if (null == payParam.channelType)
			return;
		reqPaymentAsync(payParam.channelType, 
						payParam.billTitle,
		                payParam.billTotalFee,
		                payParam.billNum,
		                payParam.billTimeout,
		                payParam.notifyUrl,
		                payParam.optional,
		                payParam.analysis,
		                callback);
	}
	
	/**
     * 支付调用总接口2
     *
     * @param channelType     支付类型  对于支付手机APP端目前只支持 BL_APP, WX_APP, ALI_APP, UN_APP, QK_APP
     * @param billTitle       商品描述, 32个字节内, 汉字以2个字节计
     * @param billTotalFee    支付金额，以分为单位，必须是正整数
     * @param billNum         商户自定义订单号
     * @param billTimeout     订单超时时间，以秒为单位，可以为null
     * @param optional        为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
     * @param analysis        为扩展参数，用于后期的分析，当前仅支持以category为key的分类分析
     * @param callback        支付完成后的回调函数
     */
	private void reqPaymentAsync(final UFCChannelTypes channelType, 
								 final String billTitle,
								 final Integer billTotalFee, 
								 final String billNum, 
								 final Integer billTimeout,
								 final String notifyUrl, 
								 final Map<String, String> optional,
								 final Map<String, String> analysis, 
								 final UFCCallback callback) {
		if (null == callback) {
			Log.w(TAG, "请初始化回调函数callback");
			return;
		}
		
		payCallback = callback;
		
		//这个里面去做一些后台需要处理的工作，将订单加签也放在这个里面 
		Runnable requestTask = new Runnable() {
			
			@Override
			public void run() {
				//校验并准备公用参数
				UFCPayReqParams payReqParams;
				try {
					payReqParams = new UFCPayReqParams(channelType);
				} catch (UFCException e) {
					callback.done(new UFCPayResult( UFCPayResult.RESULT_FAIL, 
													UFCPayResult.APP_INTERNAL_EXCEPTION_ERR_CODE, 
													UFCPayResult.FAIL_EXCEPTION, 
													e.getMessage()));
					return;
				}
				
				//开始检验bill参数
				String paramValidResult = UFCValidationUtil.prepareParametersForPay(billTitle, billTotalFee, billNum, optional, payReqParams);
				if (paramValidResult != null) {
                    callback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL,
                    							   UFCPayResult.APP_INTERNAL_PARAMS_ERR_CODE,
                    							   UFCPayResult.FAIL_INVALID_PARAMS,
                    							   paramValidResult));
                    return;
                }
				
				payReqParams.billTimeout = billTimeout;
				payReqParams.notifyUrl = notifyUrl;
				payReqParams.analysis = analysis;
				
				UFCHttpClientUtil.Response response = null ;
				String payUrl =  UFCHttpClientUtil.getPayURL();
				response = UFCHttpClientUtil.httpPostKeyValue(payUrl, payReqParams.transToBillReqMapParams());
			    
				if (response.code == 200 || (response.code >= 400 && response.code < 500)) {
					String ret = response.content;
					//反序列化Json
					Gson res = new Gson();
					Type type = new TypeToken<Map<String, Object>>(){}.getType();
					Map<String, Object> responseMap;
					try {
						responseMap = res.fromJson(ret, type);
					} catch (JsonSyntaxException e) {
						callback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL,
								UFCPayResult.APP_INTERNAL_EXCEPTION_ERR_CODE,
								UFCPayResult.FAIL_EXCEPTION,
                                "JsonSyntaxException or Network Error:" + response.code + " # " + response.content));
                        return;
					}
					//对后台返回结果进行解析，后台返回的数据里面需要有这个result_code，0表示正常返回
 				   Integer resultCode = Integer.parseInt((String) responseMap.get("result_code"));
 				   // Integer resultCode = 0;
					if (resultCode == 0) {
						if (null != mContexActivity) {
							switch (channelType) {
							case BL_APP:
								reqQuickPaymentViaAPP(responseMap.get("orderNo").toString(),1000);
								break;
							case WX_APP:
								reqWXPaymentViaAPP(responseMap,optional.get("orderNo"));
								break;
							case ALI_APP:
								reqAliPaymentViaAPP(responseMap);
								break;
							case UN_APP:
								break;
							case QK_APP:
								reqQuickPaymentViaAPP(responseMap.get("orderNo").toString(),1000);
								
								break;
							default:
								callback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL, 
										UFCPayResult.APP_INTERNAL_PARAMS_ERR_CODE, 
										UFCPayResult.FAIL_INVALID_PARAMS, 
										"channelType参数不合法"));
								break;
							}
						} else {
							callback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL, 
									UFCPayResult.APP_INTERNAL_EXCEPTION_ERR_CODE, 
									UFCPayResult.FAIL_EXCEPTION, 
									"Context-Activity NP-Exception"));
						}
					} else {
						 //返回后端传回的错误信息
                        String serverMsg = String.valueOf(responseMap.get("result_msg"));
                        String serverDetail = String.valueOf(responseMap.get("err_detail"));
                        callback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL, resultCode, serverMsg, serverDetail));
					}
				} else {
					callback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL,
												   UFCPayResult.APP_INTERNAL_NETWORK_ERR_CODE,
												   UFCPayResult.FAIL_NETWORK_ISSUE,
						                           "Network Error:" + response.code + " # " + response.content));
				}
			}


			
		};
		
		Thread requestThread = new Thread(requestTask);
		requestThread.start();
	}
	
	/**
	 * 微信支付接口3
	 * 与服务器交互过之后，进行下一步调用微信APP支付
	 * @param responseMap
	 */
	protected void reqWXPaymentViaAPP(Map<String, Object> responseMap,String orderNo) {
		PayReq req = new PayReq();
		req.appId			= String.valueOf(responseMap.get("appid"));
		req.partnerId		= String.valueOf(responseMap.get("partnerId"));
		req.prepayId		= String.valueOf(responseMap.get("prepayId"));
		req.nonceStr		= String.valueOf(responseMap.get("nonceStr"));
		req.timeStamp		= String.valueOf(responseMap.get("timeStamp"));
		req.packageValue	= String.valueOf(responseMap.get("packageValue"));
		req.sign			= String.valueOf(responseMap.get("sign"));
		req.extData			= "app data"; // optional
		if (null != wxAPI) {
			Log.d(TAG, "--->>reqWXPaymentViaAPP--->>支付前");
			wxAPI.sendReq(req);
			Log.d(TAG, "--->>reqWXPaymentViaAPP--->>支付完成后");
			
			reqQuickPaymentViaAPP(orderNo,2000);
			
		} else {
			payCallback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL, 
					UFCPayResult.APP_INTERNAL_EXCEPTION_ERR_CODE, 
					UFCPayResult.FAIL_EXCEPTION, 
					"Error: 微信API为空, 请确认已经在需要调起微信支付的Activity中[成功]调用了UFCPay.initWechatPay"));
		}
	}
	
	/**
	 * 支付宝支付接口3
	 * 与服务器交互过之后，进行下一步调用支付宝APP支付
	 * @param responseMap
	 */
	protected void reqAliPaymentViaAPP(Map<String, Object> responseMap) {
		String orderInfo = (String) responseMap.get("order_string");
		PayTask aliPay = new PayTask(mContexActivity);
		String aliResult = aliPay.pay(orderInfo, true);
		Log.d(TAG, "--->>aliResult:" + aliResult);
        //解析ali返回结果
        Pattern pattern = Pattern.compile("resultStatus=\\{(\\d+?)\\}");
        Matcher matcher = pattern.matcher(aliResult);
        String resCode = "";
        if (matcher.find())
            resCode = matcher.group(1);

        String result;
        int errCode;
        String errMsg;
        String errDetail;
        
        //9000-订单支付成功, 8000-正在处理中, 4000-订单支付失败, 6001-用户中途取消, 6002-网络连接出错
        if (resCode.equals("9000")) {
            result = UFCPayResult.RESULT_SUCCESS;
            errCode = UFCPayResult.APP_PAY_SUCC_CODE;
            errMsg = UFCPayResult.RESULT_SUCCESS;
            errDetail = errMsg;
        } else if (resCode.equals("6001")) {
            result = UFCPayResult.RESULT_CANCEL;
            errCode = UFCPayResult.APP_PAY_CANCEL_CODE;
            errMsg = UFCPayResult.RESULT_CANCEL;
            errDetail = errMsg;
        } else if (resCode.equals("8000")) {
            result = UFCPayResult.RESULT_UNKNOWN;
            errCode = UFCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE;
            errMsg = UFCPayResult.RESULT_PAYING_UNCONFIRMED;
            errDetail = "订单正在处理中，无法获取成功确认信息";
        } else {
            result = UFCPayResult.RESULT_FAIL;
            errCode = UFCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE;
            errMsg = UFCPayResult.FAIL_ERR_FROM_CHANNEL;

            if (resCode.equals("4000"))
                errDetail = "订单支付失败";
            else
                errDetail = "网络连接出错";
        }

        payCallback.done(new UFCPayResult(result, errCode, errMsg, errDetail));
	}
	
	
	/**
	 * 微信，余额，快捷查询结果
	 * @param responseMap
	 */
	protected void reqQuickPaymentViaAPP(String orderNo,int time) {
		
		for (int i = 0; i < 15; i++) {
			SystemClock.sleep(time);
			
			String resultUrl =  UFCHttpClientUtil.getResultURL();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", orderNo);
			//查询结果需要等待，否则可能会失败
			
			Response response = UFCHttpClientUtil.httpPostKeyValue(resultUrl,map);
			
			if(response.code == 200 || (response.code >= 400 && response.code < 500)){
				String result = response.content;
				Gson res = new Gson();
				Type type = new TypeToken<Map<String, Object>>(){}.getType();
				Map<String, Object> resCode = res.fromJson(result, type);
				String resultCode = (String) resCode.get("result");
				
				if (resultCode.equals("success")) {
					payCallback.done(new UFCPayResult(UFCPayResult.RESULT_SUCCESS,  UFCPayResult.APP_PAY_SUCC_CODE,
							UFCPayResult.RESULT_SUCCESS, UFCPayResult.RESULT_SUCCESS));
					break;
					
				} else {
					
					if (i == 14) {
						payCallback.done(new UFCPayResult(UFCPayResult.RESULT_FAIL,  UFCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE,
								UFCPayResult.FAIL_ERR_FROM_CHANNEL, "订单支付失败"));
					} else {
						continue;
					}		
				} 
			}
		}	
	}
	
	/**
	 * 网银支付,查看结果
	 * @param responseMap
	 */
	protected void reqNetBankPaymentViaAPP(UFCCallback callback,final String orderNo) {
		payCallback = callback;
		new Thread(new Runnable() {

			@Override
			public void run() {

				String resultUrl =  UFCHttpClientUtil.getResultURL();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderId", orderNo);
				
				Response response = UFCHttpClientUtil.httpPostKeyValue(resultUrl,map);
				
				if(response.code == 200 || (response.code >= 400 && response.code < 500)){
					String result = response.content;
					Gson res = new Gson();
					Type type = new TypeToken<Map<String, Object>>(){}.getType();
					Map<String, Object> resCode = res.fromJson(result, type);
					String resultCode = (String) resCode.get("result");
					
					if (resultCode.equals("success")) {
						payCallback.done(new UFCPayResult(UFCPayResult.RESULT_SUCCESS,  UFCPayResult.APP_PAY_SUCC_CODE,
								UFCPayResult.RESULT_SUCCESS, UFCPayResult.RESULT_SUCCESS));
					}else {
						payCallback.done(new UFCPayResult( UFCPayResult.RESULT_FAIL,  UFCPayResult.APP_INTERNAL_THIRD_CHANNEL_ERR_CODE,
								UFCPayResult.FAIL_ERR_FROM_CHANNEL, "订单支付失败"));
						
					}
				}
			}
		}).start();
		
	}

	/**
     * 微信支付调用接口
     * 如果您申请的是新版本(V3)的微信支付，请使用此接口发起微信支付.
     *
     * @param billTitle    商品描述, 32个字节内, 汉字以2个字节计
     * @param billTotalFee 支付金额，以分为单位，必须是正整数
     * @param billNum      商户自定义订单号
     * @param optional     为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
     * @param callback     支付完成后的回调函数
     */
	public void reqWXPaymentAsync(String billTitle, 
								  int billTotalFee, 
								  String billNum,
								  Map<String, String> optional, 
								  UFCCallback callback) {
		reqPaymentAsync(UFCReqParams.UFCChannelTypes.WX_APP, 
						billTitle,
						billTotalFee,
						billNum,
		                null,
		                null,
		                optional,
		                null,
		                callback);
	}
	
	/**
     * 支付宝支付调用接口
     *
     * @param billTitle    商品描述, 32个字节内, 汉字以2个字节计
     * @param billTotalFee 支付金额，以分为单位，必须是正整数
     * @param billNum      商户自定义订单号
     * @param optional     为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
     * @param callback     支付完成后的回调函数
     */
	public void reqAliPaymentAsync(String billTitle, 
								  int billTotalFee, 
								  String billNum,
								  Map<String, String> optional, 
								  UFCCallback callback) {
		reqPaymentAsync(UFCReqParams.UFCChannelTypes.ALI_APP, 
						billTitle,
						billTotalFee,
						billNum,
		                null,
		                null,
		                optional,
		                null,
		                callback);
	}
	/**
	 * 快捷支付调用接口
	 *
	 * @param billTitle    商品描述, 32个字节内, 汉字以2个字节计
	 * @param billTotalFee 支付金额，以分为单位，必须是正整数
	 * @param billNum      商户自定义订单号
	 * @param optional     为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @param callback     支付完成后的回调函数
	 */
	public void reqQuickPaymentAsync(String billTitle, 
			int billTotalFee, 
			String billNum,
			Map<String, String> optional, 
			UFCCallback callback) {
		reqPaymentAsync(UFCReqParams.UFCChannelTypes.QK_APP, 
				billTitle,
				billTotalFee,
				billNum,
				null,
				null,
				optional,
				null,
				callback);
	}
	/**
	 * 余额支付调用接口
	 *
	 * @param billTitle    商品描述, 32个字节内, 汉字以2个字节计
	 * @param billTotalFee 支付金额，以分为单位，必须是正整数
	 * @param billNum      商户自定义订单号
	 * @param optional     为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @param callback     支付完成后的回调函数
	 */
	public void reqBalancePaymentAsync(String billTitle, 
			int billTotalFee, 
			String billNum,
			Map<String, String> optional, 
			UFCCallback callback) {
		reqPaymentAsync(UFCReqParams.UFCChannelTypes.BL_APP, 
				billTitle,
				billTotalFee,
				billNum,
				null,
				null,
				optional,
				null,
				callback);
	}

	/**
     * 外部支付参数实例
     */
    public static class PayParams {
    	
        /**
         *  只允许
         *  UFCReqParams.UFCChannelTypes.BL_APP
         *  UFCReqParams.UFCChannelTypes.WX_APP，
         *  UFCReqParams.UFCChannelTypes.ALI_APP，
         *  UFCReqParams.UFCChannelTypes.UN_APP，
         *  UFCReqParams.UFCChannelTypes.QK_APP
         *
         */
        public UFCReqParams.UFCChannelTypes channelType;

        /**
         * 商品描述, 32个字节内, 汉字以2个字节计
         */
        public String billTitle;

        /**
         * 支付金额，以分为单位，必须是正整数
         */
        public Integer billTotalFee;

        /**
         * 商户自定义订单号，PayPal不需要该参数
         */
        public String billNum;

        /**
         * 支付货币，如CNY、USD，目前仅PayPal用到
         */
        public String currency;

        /**
         * 订单超时时间，以秒为单位，建议不小于360, 可以为null
         */
        public Integer billTimeout;

        /**
         * 异步回调地址
         */
        public String notifyUrl;

        /**
         * 扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求，可以为null，
         * 对于PayPal请以HashMap实例化
         */
        public Map<String, String> optional;

        /**
         * 扩展参数，用于分析，可以为null
         * 目前key只有是"category"时才会进行分析
         */
        public Map<String, String> analysis;
    }

	

}
