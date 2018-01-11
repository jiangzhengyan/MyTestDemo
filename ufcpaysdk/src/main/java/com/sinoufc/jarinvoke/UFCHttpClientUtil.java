package com.sinoufc.jarinvoke;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 网络请求工具类
 */
class UFCHttpClientUtil {
	private static final String TAG = "UFCHttpClientUtil";

	// 主机地址
	// 测试微信数据http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android
//	 private static final String SERVER_HOST = "http://58.30.232.151:8081/";//测试
//	private static final String SERVER_HOST = "http://zhunzhicheng.jingcaiwang.cn/";// 准正式http://zhunzhicheng.jingcaiwang.cnhttp://172.19.1.34:8081/
//	 private static final String SERVER_HOST ="http://58.30.232.152:8080/";//正式
	 private static final String SERVER_HOST ="http://192.168.101.9:8080/";//柳州管家测试
	// 订单支付部分URL和 获取扫码信息
	private static final String PAY_URL = "jkpay/SDKOrderToSDKPay/SDKelectiveBank";
	// 快捷密码验证url
	private static final String PWD_URL = "jkpay/SDKOrderToSDKPay/SDKcheckPayPwd";
	// 快捷查询支付结果
	private static final String RESULT_URL = "jkpay/SDKOrderToSDKPay/SDKcheckOrderState";
	// 网银请求
	private static final String NETBANK_URL = "jkpay/SDKOrderToSDKPay/SDKelectiveBank";

	// 添加快捷银行卡列表的请求url
	private static final String BANKList_URL = "jkpay/ SDKOrderToSDKPay/getBankNames";
	// 快捷请求验证码的url
	private static final String VERNUMBER_URL = "jkpay/webuser/sdkSendVerMsg";
	// 确认添加快捷银行卡的url
	private static final String COMFIRMADD_URL = "jkpay/webuser/sdkSaveBack";

	// 测试模式的异步通知(通知服务端支付成功)
	private static final String NOTIFY_PAY_RESULT_SANDBOX_URL = "pay/sandbox/notify";

	/**
	 * 获取服务器根URL
	 */
	private static String getRootHost() {
		return SERVER_HOST;
	}

	/**
	 * @return 支付请求URL
	 */
	public static String getPayURL() {
		return getRootHost() + PAY_URL;
	}

	/**
	 * @return 确认密码请求URL
	 */
	public static String getPwdURL() {
		return getRootHost() + PWD_URL;
	}

	/**
	 * @return 确认支付结果请求URL
	 */
	public static String getResultURL() {
		return getRootHost() + RESULT_URL;
	}

	/**
	 * @return 网银请求URL
	 */
	public static String getNetBankURL() {
		return getRootHost() + NETBANK_URL;
	}

	/**
	 * @return 添加快捷银行卡列表的请求url
	 */
	public static String getBankList() {
		return getRootHost() + BANKList_URL;
	}

	/**
	 * @return 快捷请求验证码的url
	 */
	public static String getVerNumber() {
		return getRootHost() + VERNUMBER_URL;
	}

	/**
	 * @return 确认添加快捷银行卡的url
	 */
	public static String getComfirmAdd() {
		return getRootHost() + COMFIRMADD_URL;
	}

	public static String getNotifyPayResultSandboxUrl() {
		return getRootHost() + NOTIFY_PAY_RESULT_SANDBOX_URL;
	}

	/**
	 * http get 请求
	 * 
	 * @param url
	 *            请求uri，如有参数在原始url后面通过 ?k1=v1&k2=v2 连接
	 * @return BCHttpClientUtil.Response请求结果实例
	 */
	public static Response httpGet(String url) {

		Response response = new Response();

		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(
				UFCConstants.connectTimeout, TimeUnit.MILLISECONDS).build();

		Request request = new Request.Builder().url(url).build();

		proceedRequest(client, request, response);

		return response;
	}

	private static void proceedRequest(OkHttpClient client, Request request,
			Response response) {
		try {
			okhttp3.Response temp = client.newCall(request).execute();
			response.code = temp.code();
			ResponseBody body = temp.body();
			response.content = body.string();
		} catch (IOException e) {
			e.printStackTrace();
			Log.w(TAG, e.getMessage() == null ? " " : e.getMessage());
			response.code = -1;
			response.content = e.getMessage();
		}
	}

	/**
	 * http post 请求
	 * 
	 * @param url
	 *            请求url
	 * @param jsonStr
	 *            post参数
	 * @return BCHttpClientUtil.Response请求结果实例
	 */
	public static Response httpPost(String url, String jsonStr) {
		Response response = new Response();

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(
				UFCConstants.connectTimeout, TimeUnit.MILLISECONDS).build();

		RequestBody body = RequestBody.create(JSON, jsonStr);

		Request request = new Request.Builder().url(url).post(body).build();

		proceedRequest(client, request, response);
		return response;
	}

	public static Response httpPostKeyValue(String url, Map<String, Object> para) {

		Response response = new Response();

		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(
				UFCConstants.connectTimeout, TimeUnit.MILLISECONDS).build();

		FormBody.Builder builder = new FormBody.Builder();
		for (Map.Entry<String, Object> entry : para.entrySet()) {

//			 builder.add(entry.getKey(), entry.getValue().toString());
			try {
				String value=null;
				String key = new String(entry.getKey().getBytes("utf-8") );
				if ("subject".equals(key)||"body".equals(key)) {
					  value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
				}else {
					 value =entry.getValue().toString( );
				}
//				String key = URLEncoder.encode(String.valueOf(entry.getKey()), "UTF-8");
//				String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
				System.out.println("1key : "+key+" value: "+value);
				builder.add(key, value);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) para.get("optional");
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {

//				 builder.add(entry.getKey(), entry.getValue().toString());
				try {
					String value=null;
					String key = new String(entry.getKey().getBytes("utf-8") );
					if ("subject".equals(key)||"body".equals(key)) {
						  value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
					}else {
						 value = entry.getValue().toString();
					}
//					String key = URLEncoder.encode(String.valueOf(entry.getKey()), "UTF-8");
//					String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
					System.out.println("2key : "+key+" value: "+value);
					builder.add(key, value);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Request request = new Request.Builder().url(url).post(builder.build())
				.build();

		proceedRequest(client, request, response);

		return response;
	}

	/**
	 * http delete 请求
	 * 
	 * @param url
	 *            请求uri，如有参数在原始url后面通过 ?k1=v1&k2=v2 连接
	 * @return BCHttpClientUtil.Response请求结果实例
	 */
	public static Response httpDelete(String url) {

		Response response = new Response();

		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(
				UFCConstants.connectTimeout, TimeUnit.MILLISECONDS).build();

		Request request = new Request.Builder().url(url).delete().build();

		proceedRequest(client, request, response);

		return response;
	}

	/**
	 * http post 请求
	 * 
	 * @param url
	 *            请求url
	 * @param para
	 *            post参数
	 * @return BCHttpClientUtil.Response请求结果实例
	 */
	public static Response httpPost(String url, Map<String, Object> para) {
		Gson gson = new Gson();
		String param = gson.toJson(para);

		return httpPost(url, param);
	}

	// 所有super class包含的非空字段
	static Map<String, Object> objectToMap(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();

		Class cls = object.getClass();
		while (cls != null) {
			for (Field field : cls.getDeclaredFields()) {
				field.setAccessible(true);

				Object value = null;
				try {
					value = field.get(object);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				if (value != null)
					map.put(field.getName(), value);
			}

			cls = cls.getSuperclass();
		}

		return map;
	}

	static String map2UrlQueryString(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, Object> e : map.entrySet()) {
			try {
				sb.append(e.getKey());
				sb.append('=');
				sb.append(URLEncoder.encode(String.valueOf(e.getValue()),
						"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			sb.append('&');
		}
		if (sb.length() == 0)
			return "";
		else
			return sb.substring(0, sb.length() - 1);
	}

	static UFCRestfulCommonResult setCommonResult(
			Class<? extends UFCRestfulCommonResult> className, Integer code,
			String errMsg, String detail) {
		UFCRestfulCommonResult object = null;
		try {
			Constructor<? extends UFCRestfulCommonResult> ctor = className
					.getConstructor(Integer.class, String.class, String.class);
			object = ctor.newInstance(code, errMsg, detail);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}

	// ===================== BeeCloud Rest Object CURD =====================
	private static UFCRestfulCommonResult dealWithResult(Response response,
			Class<? extends UFCRestfulCommonResult> classType) {
		if (response.content == null || response.content.length() == 0) {
			return setCommonResult(classType,
					UFCRestfulCommonResult.APP_INNER_FAIL_NUM,
					UFCRestfulCommonResult.APP_INNER_FAIL,
					"JsonSyntaxException or Network Error:" + response.code
							+ " # " + response.content);
		}

		if (response.code == 200
				|| (response.code >= 400 && response.code < 500)) {
			// 反序列化json
			Gson gson = new Gson();

			try {
				return gson.fromJson(response.content, classType);
			} catch (JsonSyntaxException ex) {
				return setCommonResult(classType,
						UFCRestfulCommonResult.APP_INNER_FAIL_NUM,
						UFCRestfulCommonResult.APP_INNER_FAIL,
						"JsonSyntaxException or Network Error:" + response.code
								+ " # " + response.content);
			}
		} else {
			return setCommonResult(classType,
					UFCRestfulCommonResult.APP_INNER_FAIL_NUM,
					UFCRestfulCommonResult.APP_INNER_FAIL, "Network Error:"
							+ response.code + " # " + response.content);
		}
	}

	static UFCRestfulCommonResult addRestObject(String url,
			Map<String, Object> reqParam,
			Class<? extends UFCRestfulCommonResult> classType,
			boolean testModeNotSupport) {
		UFCHttpClientUtil.Response response = UFCHttpClientUtil.httpPost(url,
				reqParam);

		return dealWithResult(response, classType);
	}

	static UFCRestfulCommonResult deleteRestObject(String url, String id,
			Map<String, Object> reqParam,
			Class<? extends UFCRestfulCommonResult> classType,
			boolean testModeNotSupport) {
		String reqURL = url + "/" + id + "?"
				+ UFCHttpClientUtil.map2UrlQueryString(reqParam);

		UFCHttpClientUtil.Response response = UFCHttpClientUtil
				.httpDelete(reqURL);

		return dealWithResult(response, classType);
	}

	static UFCRestfulCommonResult queryRestObjects(String url,
			Map<String, Object> reqParam,
			Class<? extends UFCRestfulCommonResult> classType,
			boolean testModeNotSupport) {
		String reqURL = url + "?"
				+ UFCHttpClientUtil.map2UrlQueryString(reqParam);
		UFCHttpClientUtil.Response response = UFCHttpClientUtil.httpGet(reqURL);

		return dealWithResult(response, classType);
	}

	public static class Response {
		public Integer code;
		public String content;
	}
}
