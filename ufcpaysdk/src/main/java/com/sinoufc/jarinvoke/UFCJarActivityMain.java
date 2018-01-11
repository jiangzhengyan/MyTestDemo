package com.sinoufc.jarinvoke;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinoufc.jarinvoke.R;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sinoufc.jarinvoke.UFCHttpClientUtil.Response;
import com.sinoufc.jarinvoke.async.UFCCallback;
import com.sinoufc.jarinvoke.async.UFCResult;
import com.sinoufc.jarinvoke.bean.BankInfo;
import com.sinoufc.jarinvoke.bean.Bankquick;
import com.sinoufc.jarinvoke.bean.Bankquick.Info;
import com.sinoufc.jarinvoke.bean.Data;
import com.sinoufc.jarinvoke.bean.PayInfo;
import com.sinoufc.jarinvoke.bean.QuickInfo;
import com.sinoufc.jarinvoke.entity.UFCPayResult;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class UFCJarActivityMain extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "JarActivityMain";

	String msg = "我是来自Jar中的Activity";

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	private ExpandableListView listView;
	private List<Data> mData;

	private TextView tv_sdk_paymoney, tv_sdk_orderinfo;
	private Button btn_sdk_pay, btn_sdk_add;
	// record the current checked radio number
	private int checkedIndex = -1;

	// 正在加载的弹出框进度条
	private ProgressDialog loadingDialog;

	private MyAdapter myAdapter;

	public Map<String, String> mapOptional = new HashMap<String, String>();

	private PayInfo info;

	// 快捷卡信息
	private List<QuickInfo> quickInfo;
	// 网银信息
	private List<BankInfo> bankInfo;

	// 区分不同快捷卡
	private String bondId;

	// 快捷列表点击标识
	private int index = -1;

	//支付标识,为了处理回调不一致的问题
	private int payFlag=0;//1,微信
	// 支付结果返回入口
	UFCCallback ufcCallback = new UFCCallback() {

		@Override
		public void done(UFCResult ufcResult) {
			// 先将loadingDialog框关闭
			loadingDialog.dismiss();
			final UFCPayResult payResult = (UFCPayResult) ufcResult;

			// 根据需求处理支付结果
			// 需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
			UFCJarActivityMain.this.runOnUiThread(new Runnable() {
				public void run() {
					String result = payResult.getResult();
					// && result.equals(UFCPayResult.RESULT_SUCCESS
					if (null != result) {
						if (result.equals(UFCPayResult.RESULT_SUCCESS)) {

							Intent intent = new Intent();
							intent.putExtra("result", result);

							setResult(654321, intent);
							finish();

						} else {
							//不是微信支付的时候弹吐司
							if (payFlag!=1) {
								Toast.makeText(UFCJarActivityMain.this, "支付失败",
										Toast.LENGTH_SHORT).show();
							}
							
						}

					} else {

					}
				}
			});
		}

	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String pwd = (String) msg.obj;
			if (TextUtils.equals(pwd, "success")) {
				int payMoney = (int) (Double.parseDouble(info.getPayMoney()) * 100.0);
				switch (msg.what) {

				// 快捷支付
				case 123:
					mapOptional.put("payQuickHidden", "spdb");
					quickAndBalancepay("quick", "spdb");
					UFCPay.getInstance(UFCJarActivityMain.this)
							.reqQuickPaymentAsync(info.getBody(), payMoney,
									UFCUtil.genBillNum(), mapOptional,
									ufcCallback);
					break;

				// 余额支付
				case 456:
					quickAndBalancepay("balance", "");
					UFCPay.getInstance(UFCJarActivityMain.this)
							.reqBalancePaymentAsync(info.getBody(), payMoney,
									UFCUtil.genBillNum(), mapOptional,
									ufcCallback);
					break;
				default:
					break;
				}
			} else {
				Toast.makeText(UFCJarActivityMain.this, "密码错误",
						Toast.LENGTH_SHORT).show();
			}

		}

		private void quickAndBalancepay(String payTypeHidden,
				String payQuickHidden) {
			loadingDialog.show();
			mapOptional = new HashMap<String, String>();
			mapOptional.put("orderNo", info.getOrderNo());
			mapOptional.put("userName", info.getUserName());
			mapOptional.put("comId", info.getComId());
			mapOptional.put("coreUserId", info.getCoreUserId());
			mapOptional.put("receiveUrl", info.getReceiveUrl());
			mapOptional.put("payTypeHidden", payTypeHidden);
			mapOptional.put("payQuickHidden", payQuickHidden);
			if (null != bondId) {
				mapOptional.put("bondId", bondId);
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(UFCMResource.getIdByName(getApplication(), "layout",
				"ufcsdk_activity_jar_invoke"));

		init();

		tv_sdk_paymoney = (TextView) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "tv_sdk_paymoney"));
		tv_sdk_orderinfo = (TextView) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "tv_sdk_orderinfo"));
		ImageView iv_back = (ImageView) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "iv_back"));

		tv_sdk_paymoney.setText("¥" + info.getPayMoney());
		tv_sdk_orderinfo.setText("订单号:" + info.getShowOrderNo());

		btn_sdk_pay = (Button) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "btn_sdk_pay"));
		btn_sdk_add = (Button) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "bt_sdk_add"));

		btn_sdk_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UFCJarActivityMain.this,
						UFCAddBankCard.class);
				intent.putExtra("userId", info.getUserName());
				startActivityForResult(intent, 1234);
			}
		});

		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_sdk_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 根据checkedIndex去mData里面找到对应的支付方式
				int mimg = (Integer) mData.get(checkedIndex).img1;
				// Toast.makeText(UFCJarActivityMain.this, (CharSequence)
				// mData.get(checkedIndex).tag, Toast.LENGTH_SHORT).show();

				payFlag=0;
				// 微信
				if (mimg == R.drawable.ufcsdk_pay_weixin) {
					// loadingDialog.show();
					// 对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
					// 这个是微信自身存在的问题
					// 添加测试扩展参数

					payFlag=1;
					mapOptional = new HashMap<String, String>();
					mapOptional.put("appid", info.getWxappId());
					mapOptional.put("spbill_create_ip", "192.168.40.154");
					mapOptional.put("orderNo", info.getOrderNo());
					mapOptional.put("payTypeHidden", "othpay");
					mapOptional.put("payOthHidden", "webchat");
					mapOptional.put("comId", info.getComId());
					mapOptional.put("userName", info.getUserName());

					if (UFCPay.isWechatInstalledAndSupported()
							&& UFCPay.isWechatPaySupported()) {
						UFCPay.getInstance(UFCJarActivityMain.this)
								.reqWXPaymentAsync(
										info.getBody(), // 商品描述
										(int) (Double.parseDouble(info
												.getPayMoney()) * 100.0), // 订单金额(分)
										UFCUtil.genBillNum(), // 订单流水号
										mapOptional, // 扩展参数(可以null)
										ufcCallback); // 支付完成后回调入口
					} else {
						Toast.makeText(UFCJarActivityMain.this,
								"您尚未安装微信或者安装的微信版本不支持.", Toast.LENGTH_SHORT)
								.show();
						loadingDialog.dismiss();
					}
				} else if (mimg == R.drawable.ufcsdk_pay_ali) {
					// 支付宝

					// loadingDialog.show();

					mapOptional = new HashMap<String, String>();

					mapOptional.put("subject", info.getSubject());
					mapOptional.put("orderNo", info.getOrderNo());
					mapOptional.put("payTypeHidden", "othpay");
					mapOptional.put("payOthHidden", "alipay");
					mapOptional.put("comId", info.getComId());
					mapOptional.put("userName", info.getUserName());

					UFCPay.getInstance(UFCJarActivityMain.this)
							.reqAliPaymentAsync(
									info.getBody(),
									(int) (Double.parseDouble(info
											.getPayMoney()) * 100.0),
									UFCUtil.genBillNum(), mapOptional,
									ufcCallback);

				} else if (mimg == R.drawable.ufcsdk_pay_wangyin) {
					// 网银

					loadingDialog.show();

					Intent intent = new Intent(UFCJarActivityMain.this,
							UFCNetBankPay.class);
					Bundle bundle = new Bundle();

					bundle.putString("orderNo", info.getOrderNo());
					bundle.putString("comId", info.getComId());
					bundle.putString("userName", info.getUserName());
					bundle.putString(
							"payMoney",
							(int) (Double.parseDouble(info.getPayMoney()) * 100.0)
									+ "");

					intent.putExtras(bundle);

					startActivityForResult(intent, 12345);

				} else if (mimg == R.drawable.ufcsdk_pay_qianbao) {
					// 余额

					confirmPay(456);
				} else if (mimg == R.drawable.ufcsdk_pay_kuaijie) {
					// 快捷

					if (null != quickInfo) {
						if (null != bondId) {
							confirmPay(123);
						} else {
							Toast.makeText(UFCJarActivityMain.this, "请选择快捷银行卡",
									Toast.LENGTH_SHORT).show();
						}
					} else {

						Toast.makeText(UFCJarActivityMain.this, "请绑定快捷银行卡",
								Toast.LENGTH_SHORT).show();
					}

				}

			}

			private void confirmPay2(final int what) {
				// R.style.Theme_Light_Dialog_Light_Dialog_Cus
				final Dialog dialog = new Dialog(UFCJarActivityMain.this,
						R.style.Theme_Light_Dialog_Light_Dialog_Cus);

				View view = View.inflate(UFCJarActivityMain.this, UFCMResource
						.getIdByName(UFCJarActivityMain.this, "layout",
								"ufcpay_dialog_inputpsd"), null);

				final EditText inputServer = (EditText) view
						.findViewById(UFCMResource.getIdByName(
								UFCJarActivityMain.this, "id", "et_psd"));
				inputServer
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());
				inputServer.setFocusable(true);
				
				view.findViewById(
						UFCMResource.getIdByName(UFCJarActivityMain.this, "id",
								"ufcpay_inputpwd_tv_cancel"))
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								// 取消
								dialog.dismiss();
							}
						});
				dialog.setContentView(view);

				dialog.setCancelable(false);

				dialog.show();
			}

			// 快捷和余额确认支付
			private void confirmPay1(final int what) {
				final EditText inputServer = new EditText(
						UFCJarActivityMain.this);
				inputServer
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());
				inputServer.setFocusable(true);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						UFCJarActivityMain.this);

				builder.setTitle("请输入密码1：")
						.setView(inputServer)
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}

								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											int which) {

										final String inputName = inputServer
												.getText().toString();
										Thread thread = new Thread(
												new Runnable() {
													@Override
													public void run() {

														final String pwdUrl = UFCHttpClientUtil
																.getPwdURL();
														final Map<String, Object> map = new HashMap<String, Object>();
														map.put("userId", info
																.getUserName());
														String pwd = UFCDEndecryptUtil
																.Base64Encode(inputName);
														map.put("pwd", pwd);

														Response response = UFCHttpClientUtil
																.httpPostKeyValue(
																		pwdUrl,
																		map);

														String result = response.content;
														Gson res = new Gson();
														Type type = new TypeToken<Map<String, Object>>() {
														}.getType();
														Map<String, Object> responseMap = res
																.fromJson(
																		result,
																		type);

														Message message = Message
																.obtain();
														message.obj = responseMap
																.get("result");
														message.what = what;
														handler.sendMessage(message);
														dialog.dismiss();
													}
												});
										thread.start();
									}
								}).show();

			}
		});

		mData = getPayData();

		listView = (ExpandableListView) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "pay_listview"));
		listView.setGroupIndicator(null);
		myAdapter = new MyAdapter(this);
		listView.setAdapter(myAdapter);

		// 实现点击整个item时 都会触发单选框的 动作
		listView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				int childId = checkedIndex - parent.getFirstVisiblePosition();
				if (childId >= 0) {
					// 如果checked =true的radio在显示的窗口内，改变其状态为false
					View item = parent.getChildAt(childId);
					if (item != null) {
						RadioButton rb = (RadioButton) item
								.findViewById(checkedIndex);
						if (rb != null)
							rb.setChecked(false);
					}
					checkedIndex = groupPosition;
				}

				return false;
			}
		});

		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				if (groupPosition == 3) {
					bondId = quickInfo.get(childPosition).getID();
					index = childPosition;
					myAdapter.notifyDataSetChanged();
				}

				return true;
			}

		});

	}

	
	/**
	 * 被调用生成界面的时候初始化传入过来的参数，并相应保存入库
	 */
	private void init() {
		Bundle data = getIntent().getExtras();
		if (null != data && "0".equals(data.getString("result_code"))) {
			info = new PayInfo();
			info.setNeedClose(data.getString("needClose"));
			info.setWxappId(data.getString("app_id"));
			info.setSubject(data.getString("subject"));
			info.setBody(data.getString("body"));
			info.setShowOrderNo(data.getString("showOrderNo"));
			info.setOrderNo(data.getString("orderNo"));
			info.setUserName(data.getString("userName"));
			info.setOrderId(data.getString("order_id"));
			info.setCoreUserId(data.getString("coreUserId"));
			info.setPayMoney(data.getString("payMoney"));
			info.setDismoney(data.getString("disMoney"));
			info.setBalance(data.getString("balance"));
			info.setUserType(data.getString("userType"));
			info.setComId(data.getString("comId"));
			info.setQuickArray(data.getString("quickArray"));
			info.setBankArray(data.getString("bankArray"));
			info.setOrderType(data.getString("OrderType"));
			info.setRealType(data.getString("realType"));
			info.setReturnUrl(data.getString("returnUrl"));
			info.setReceiveUrl(data.getString("receiveUrl"));
		}

		Gson res = new Gson();

		Type type = new TypeToken<ArrayList<QuickInfo>>() {
		}.getType();

		if (info.getQuickArray().length() > 3) {

			quickInfo = res.fromJson(info.getQuickArray(), type);
		}

		type = new TypeToken<ArrayList<BankInfo>>() {
		}.getType();

		if (info.getBankArray().length() > 3) {

			bankInfo = res.fromJson(info.getBankArray(), type);
		}

		UFCConstants.WXAPP_ID = info.getWxappId();

		if (UFCConstants.WXAPP_ID.length() > 0) {

			initWXchatPay(UFCConstants.WXAPP_ID);
		}

		// 如果调起支付太慢, 可以在这里开启动画, 以progressdialog为例
		loadingDialog = new ProgressDialog(UFCJarActivityMain.this);
		loadingDialog.setMessage("处理中，请稍候...");
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(true);
	}

	private void confirmPay(final int what) {
		//判断余额是否足够
		if (what== 456) {
			//余额支付
			if (null!=info) {
				String balance = info.getBalance();
				String payMoney = info.getPayMoney();
				if (!TextUtils.isEmpty(balance)&&!TextUtils.isEmpty(payMoney)) {
					if ( Double.parseDouble(payMoney)>Double.parseDouble(balance)) {
						Toast.makeText(this, "余额不足,请选择其他支付方式", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
		}
		// R.style.Theme_Light_Dialog_Light_Dialog_Cus
		final Dialog dialog = new Dialog(UFCJarActivityMain.this,
				UFCMResource.getIdByName(UFCJarActivityMain.this, "style",
						"Theme_Light_Dialog_Light_Dialog_Cus"));

		View view = View.inflate(UFCJarActivityMain.this, UFCMResource
				.getIdByName(UFCJarActivityMain.this, "layout",
						"ufcpay_dialog_inputpsd"), null);

		final EditText inputServer = (EditText) view.findViewById(UFCMResource
				.getIdByName(UFCJarActivityMain.this, "id", "et_psd"));
		inputServer.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		inputServer.setFocusable(true);
		view.findViewById(
				UFCMResource.getIdByName(UFCJarActivityMain.this, "id",
						"tv_sure")).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// 确认
						final String inputName = inputServer.getText()
								.toString();
						if (TextUtils.isEmpty(inputName)) {
							Toast.makeText(UFCJarActivityMain.this, "密码不能为空",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {

								final String pwdUrl = UFCHttpClientUtil
										.getPwdURL();
								final Map<String, Object> map = new HashMap<String, Object>();
								map.put("userId", info.getUserName());
								String pwd = UFCDEndecryptUtil
										.Base64Encode(inputName);
								map.put("pwd", pwd);

								Response response = UFCHttpClientUtil
										.httpPostKeyValue(pwdUrl, map);

								String result = response.content;
								Gson res = new Gson();
								Type type = new TypeToken<Map<String, Object>>() {
								}.getType();
								Map<String, Object> responseMap = res.fromJson(
										result, type);

								Message message = Message.obtain();
								message.obj = responseMap.get("result");
								message.what = what;
								handler.sendMessage(message);
								dialog.dismiss();
							}
						});
						thread.start();

					}
				});
		// ufcpay_inputpwd_tv_cancel
		view.findViewById(
				UFCMResource.getIdByName(getApplicationContext(), "id",
						"tv_cancel")).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// 取消
						dialog.dismiss();
					}
				});
		dialog.setContentView(view);

		dialog.setCancelable(false);

		dialog.show();
	}

	/**
	 * 初始化微信支付
	 * 
	 * @param wXAPP_ID
	 *            微信平台申请到的APPID
	 */
	private void initWXchatPay(String wXAPP_ID) {
		String initInfo = UFCPay.initWechatPay(UFCJarActivityMain.this,
				wXAPP_ID);
//		if (initInfo != null) {
//			Toast.makeText(this, "微信初始化失败：" + initInfo, Toast.LENGTH_LONG)
//					.show();
//		}
		api = WXAPIFactory.createWXAPI(UFCJarActivityMain.this, wXAPP_ID);
		api.registerApp(wXAPP_ID);
		api.handleIntent(getIntent(), this);
	}

	/** 获取在list中显示的数据 */
	private List<Data> getPayData() {
		List<Data> list = new ArrayList<Data>();
		Data data = new Data();

		if (null != info.getOrderType() && "0".equals(info.getOrderType())
				&& null != info.getRealType() && "1".equals(info.getRealType())) {

			data.title = "余额支付";
			data.img1 = R.drawable.ufcsdk_pay_qianbao;
			data.img2 = UFCMResource.getIdByName(getApplication(), "drawable",
					"ufcsdk_pay_qianbao");
			data.tag = "pay_qianbao";
			if (null == info.getBalance()) {
				data.money = "余额:0";
			} else {
				data.money = "余额:" + info.getBalance();
			}
			//隐藏余额支付
			if (!TextUtils.equals(info.getNeedClose(), "1")) {				
				list.add(data);
			}
		}

		data = new Data();
		data.title = "微信支付";
		data.img1 = R.drawable.ufcsdk_pay_weixin;
		data.img2 = UFCMResource.getIdByName(getApplication(), "drawable",
				"ufcsdk_pay_weixin");
		data.tag = "pay_weixin";
		//隐藏微信支付
		if (!TextUtils.equals(info.getNeedClose(), "2")) {				
			list.add(data);
		}
		 

		data = new Data();
		data.title = "支付宝支付";
		data.img1 = R.drawable.ufcsdk_pay_ali;
		data.img2 = UFCMResource.getIdByName(getApplication(), "drawable",
				"ufcsdk_pay_ali");
		data.tag = "pay_ali";
		//隐藏支付宝支付
		 if (!TextUtils.equals(info.getNeedClose(), "3")) {				
					list.add(data);
		 }

		if (null != info.getUserType() && "1".equals(info.getUserType())
				&& null != info.getRealType() && "1".equals(info.getRealType())) {

			data = new Data();
			data.title = "快捷支付";
			data.img1 = R.drawable.ufcsdk_pay_kuaijie;
			data.img2 = UFCMResource.getIdByName(getApplication(), "drawable",
					"ufcsdk_pay_kuaijie");
			data.icon = UFCMResource.getIdByName(getApplication(), "drawable",
					"ufcsdk_pay_spdb_icon");
			data.tag = "pay_kuaijie";
			//隐藏快捷支付
			if (!TextUtils.equals(info.getNeedClose(), "4")) {				
				list.add(data);
			}
		}

		data = new Data();
		data.title = "网银支付";
		data.img1 = R.drawable.ufcsdk_pay_wangyin;
		data.img2 = UFCMResource.getIdByName(getApplication(), "drawable",
				"ufcsdk_pay_wangyin");
		data.icon = UFCMResource.getIdByName(getApplication(), "drawable",
				"ufcsdk_pay_spdb_icon");
		data.tag = "pay_wangyin";
		//隐藏网银支付
		 if (!TextUtils.equals(info.getNeedClose(), "5")) {				
					list.add(data);
		 }

		return list;
	}

	// 存放listview中的数据项
	public static class ViewHolder {
		public ImageView img;
		public TextView title;
		public RadioButton viewBtn;
		public TextView money;
		public TextView desc;
	}

	class MyAdapter extends BaseExpandableListAdapter {

		private LayoutInflater mInflater;
		private Data data;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getGroupCount() {
			return mData.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			int size = 0;
			if (groupPosition == 3) {
				if (null != quickInfo) {
					size = quickInfo.size();
				} else {
					size = 1;
				}
			} else if (groupPosition == 4) {
				size = bankInfo.size();
			}
			return size;
		}

		@Override
		public Object getGroup(int groupPosition) {

			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {

			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			data = mData.get(groupPosition);
			if (convertView == null) {
				// convertView 存放item的
				holder = new ViewHolder();
				convertView = mInflater.inflate(UFCMResource.getIdByName(
						getApplication(), "layout",
						"ufcsdk_activity_mylistviewwithradio"), null);
				holder.img = (ImageView) convertView.findViewById(UFCMResource
						.getIdByName(getApplication(), "id", "img2"));
				holder.title = (TextView) convertView.findViewById(UFCMResource
						.getIdByName(getApplication(), "id", "title2"));
				holder.money = (TextView) convertView.findViewById(UFCMResource
						.getIdByName(getApplication(), "id", "money"));
				holder.viewBtn = (RadioButton) convertView
						.findViewById(UFCMResource
								.getIdByName(getApplication(), "id",
										"listview2_radiobutton"));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackgroundResource(data.img2);
			holder.title.setText(data.title);
			holder.money.setText(data.money);

			// 让子控件button失去焦点 这样不会覆盖掉item的焦点 否则点击item 不会触发响应即onItemClick失效
			holder.viewBtn.setFocusable(false);// 无此句点击item无响应的
			holder.viewBtn.setId(groupPosition);
			holder.viewBtn.setChecked(groupPosition == checkedIndex);
			holder.viewBtn
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								// set pre radio button
								if (checkedIndex != -1) {
									int childId = checkedIndex
											- listView
													.getFirstVisiblePosition();
									if (childId >= 0) {
										View item = listView
												.getChildAt(childId);
										if (item != null) {
											RadioButton rb = (RadioButton) item
													.findViewById(checkedIndex);
											if (rb != null)
												rb.setChecked(false);
										}
									}
								}
								// set cur radio button
								checkedIndex = buttonView.getId();
							}

						}
					});
			if (checkedIndex == -1 && groupPosition == 0) {
				holder.viewBtn.setChecked(true);
			} else if (groupPosition == checkedIndex) {
				holder.viewBtn.setChecked(true);// 将本次点击的RadioButton设置为选中状态
			} else {
				holder.viewBtn.setChecked(false);
			}

			return convertView;

		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(UFCMResource
						.getIdByName(getApplication(), "layout",
								"ufcsdk_activity_childitem"), null);
				holder.desc = (TextView) convertView.findViewById(UFCMResource
						.getIdByName(getApplication(), "id", "desc"));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (groupPosition == 3) {
				if (null != quickInfo) {
					holder.desc.setText(quickInfo.get(childPosition)
							.getCARDNO());
					initDrawable(holder.desc, data.icon);

					if (index == childPosition) {
						holder.desc.setTextColor(Color.RED);
						holder.desc.setTextSize(20);
					} else {
						holder.desc.setTextColor(Color.BLACK);
						holder.desc.setTextSize(18);
					}
				} else {
					holder.desc.setVisibility(View.GONE);
				}
			} else if (groupPosition == 4) {
				holder.desc.setText(bankInfo.get(childPosition).getNAME());
				initDrawable(holder.desc, data.icon);
				holder.desc.setTextColor(Color.BLACK);
				holder.desc.setTextSize(18);
			}

			return convertView;
		}

		private void initDrawable(TextView desc, int icon) {
			Drawable drawable = getResources().getDrawable(data.icon);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(10, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			desc.setCompoundDrawables(drawable, null, null, null);
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		init();
		System.err.println("返回到Main");
		if (null != data) {
			if (requestCode == 1234 && resultCode == 4321) {
				if (null != data.getSerializableExtra("info")) {
					Bankquick.Info info = (Info) data
							.getSerializableExtra("info");
					if (null != info) {
						QuickInfo quick = new QuickInfo();
						quick.setID(info.getID());
						quick.setBANKNAME(info.getBANKNAME());
						quick.setBANKCODE(info.getBANKCODE());
						quick.setTYPENAME(info.getTYPENAME());
						quick.setCARDNO(info.getCARDNO());
						if (null != quickInfo) {
							quickInfo.add(quick);
						}
						if (null != myAdapter) {
							listView.setAdapter(myAdapter);
							myAdapter.notifyDataSetChanged();
						}
					}
				}
			} else if (requestCode == 12345 && resultCode == 54321) {
				String mark = data.getStringExtra("mark");
				if ("mark".equals(mark)) {
					UFCPay.getInstance(UFCJarActivityMain.this)
							.reqNetBankPaymentViaAPP(ufcCallback,
									info.getOrderNo());
				}
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		finish();
	}

}
