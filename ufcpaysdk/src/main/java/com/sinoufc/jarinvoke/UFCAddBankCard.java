package com.sinoufc.jarinvoke;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sinoufc.jarinvoke.UFCHttpClientUtil.Response;
import com.sinoufc.jarinvoke.bean.BankData;
import com.sinoufc.jarinvoke.bean.Bankquick;
import com.sinoufc.jarinvoke.bean.Bankquick.Info;

/**
 * 添加快捷银行卡界面的activity
 */

public class UFCAddBankCard extends Activity {

	private String phoneNo;
	private String bankCardNo;
	private String val;
	private Type type;
	private Gson res;
	private String id;
	private String verCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(UFCMResource.getIdByName(getApplication(), "layout",
				"ufcsdk_quick_add"));

		id = getIntent().getStringExtra("userId");

		// final Spinner spinner = (Spinner) findViewById(UFCMResource
		// .getIdByName(getApplication(), "id", "spinner_bank"));
		final LinearLayout ll__add_bank = (LinearLayout) findViewById(UFCMResource
				.getIdByName(getApplication(), "id", "ll__add_bank"));
		final TextView tv_bank_title = (TextView) findViewById(UFCMResource
				.getIdByName(getApplication(), "id", "tv_bank_title"));
		final EditText etBank = (EditText) findViewById(UFCMResource
				.getIdByName(getApplication(), "id", "et_bankNumber"));
		etBank.setInputType(InputType.TYPE_CLASS_NUMBER);
		final EditText etPhone = (EditText) findViewById(UFCMResource
				.getIdByName(getApplication(), "id", "et_phoneNumber"));
		etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
		Button btSend = (Button) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "bt_sendCode"));
		final EditText etVer = (EditText) findViewById(UFCMResource
				.getIdByName(getApplication(), "id", "et_verNumber"));
		Button btComfirm = (Button) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "bt_confirm"));
		ImageView ivBack = (ImageView) findViewById(UFCMResource.getIdByName(
				getApplication(), "id", "iv_back"));

		Thread threadOne = new Thread(new Runnable() {

			@Override
			public void run() {
				res = new Gson();
				type = new TypeToken<BankData>() {
				}.getType();
				Response response = UFCHttpClientUtil.httpGet(UFCHttpClientUtil
						.getBankList());
				final BankData bankLists = res.fromJson(response.content, type);

				if (null != bankLists) {

					UFCAddBankCard.this.runOnUiThread(new Runnable() {

						public void run() {
							final String[] str = new String[bankLists
									.getBankNameArray().size()];
							for (int i = 0; i < str.length; i++) {
								str[i] = bankLists.getBankNameArray().get(i)
										.getTEXT();
							}

							if (null != str && str.length > 0) {
								tv_bank_title.setText(str[0]);
								val = bankLists.getBankNameArray().get(0).getVAL();
								System.out.println(val+"==================");
								final UFCAddBankCardCustomDialog customDialog = new UFCAddBankCardCustomDialog(
										UFCAddBankCard.this, 3);
								View add_bank_dialog = View.inflate(
										UFCAddBankCard.this,
										UFCMResource.getIdByName(
												getApplication(), "layout",
												"ufcpay_add_bank_dialog"), null);
								ListView lv_bank_list = (ListView) add_bank_dialog
										.findViewById(UFCMResource.getIdByName(
												getApplication(), "id",
												"lv_bank_list"));
								BankAdapter bankAdapter = new BankAdapter(
										UFCAddBankCard.this, str);
								lv_bank_list.setAdapter(bankAdapter);
								
								
								lv_bank_list
										.setOnItemClickListener(new AdapterView.OnItemClickListener() {
											@Override
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {

												tv_bank_title
														.setText(str[position]);
												val = bankLists.getBankNameArray().get(position).getVAL();
												customDialog.dismiss();
											}
										});
								customDialog.setContentView(add_bank_dialog);

								ll__add_bank
										.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												customDialog.show();
											}
										});

							}
						}
					});

				}

			}
		});

		threadOne.start();

		btSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				phoneNo = etPhone.getText().toString();

				if (!UFCUtil.isPhone(phoneNo)) {
					Toast.makeText(UFCAddBankCard.this, "请输入正确的手机号码",
							Toast.LENGTH_SHORT).show();
					return;
				}

				Thread threadTwo = new Thread(new Runnable() {

					@Override
					public void run() {

						Map<String, Object> map = new HashMap<String, Object>();
						map.put("validtype", "5");
						map.put("sendmobile", phoneNo);
						map.put("userId", id);

						Response resp = UFCHttpClientUtil.httpPostKeyValue(
								UFCHttpClientUtil.getVerNumber(), map);

						type = new TypeToken<Map<String, Object>>() {
						}.getType();

						final Map<String, Object> sendResult = res.fromJson(
								resp.content, type);

						UFCAddBankCard.this.runOnUiThread(new Runnable() {
							public void run() {

								if (null != sendResult) {

									if (sendResult.get("status").equals(
											"success")) {
										Toast.makeText(UFCAddBankCard.this,
												"发送成功", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(UFCAddBankCard.this,
												"发送失败", Toast.LENGTH_SHORT)
												.show();
									}
								}
							}
						});
					}
				});

				threadTwo.start();

			}
		});

		btComfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				phoneNo = etPhone.getText().toString();
				bankCardNo = etBank.getText().toString().trim();
				verCode = etVer.getText().toString().trim();
				if (TextUtils.isEmpty(bankCardNo)) {
					Toast.makeText(UFCAddBankCard.this, "请输入银行卡号",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(phoneNo)) {
					Toast.makeText(UFCAddBankCard.this, "请输入手机号",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!UFCUtil.isPhone(phoneNo)) {
					Toast.makeText(UFCAddBankCard.this, "请输入正确的手机号",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(verCode)) {
					Toast.makeText(UFCAddBankCard.this, "请输入验证码",
							Toast.LENGTH_SHORT).show();
					return;
				}

				Thread threadThree = new Thread(new Runnable() {

					@Override
					public void run() {

						HashMap<String, Object> confirmMap = new HashMap<String, Object>();
						confirmMap.put("userId", id);
						confirmMap.put("var_code", verCode);
						confirmMap.put("cardNo", bankCardNo);
						confirmMap.put("channelId", val);
						confirmMap.put("mobile", phoneNo);
						confirmMap.put("validtype", "5");

						Set<String> keySet = confirmMap.keySet();

						// 打印键值对
						for (String string : keySet) {
							System.err.println("=====" + "键 : " + string
									+ "  -值 : " + confirmMap.get(string));
							confirmMap.get(string);
						}
						Response resp = UFCHttpClientUtil.httpPostKeyValue(
								UFCHttpClientUtil.getComfirmAdd(), confirmMap);

						System.err.println("=====content: " + resp.content);
						System.err.println("=====code:" + resp.code);

						String resultString = resp.content;
						if (!TextUtils.isEmpty(resultString)) {
							try {
								JSONObject json = new JSONObject(resultString);
								String result_1 = json.getString("result");
								System.err.println("=======" + result_1);

								if ("success".equals(result_1)) {
									String value = json.getString("value");
									JSONObject json_value = new JSONObject(
											value);
									String ID = json_value.getString("ID");
									String BANKNAME = json_value
											.getString("BANKNAME");
									String BANKCODE = json_value
											.getString("BANKCODE");
									String TYPENAME = json_value
											.getString("TYPENAME");
									String CARDNO = json_value
											.getString("CARDNO");

									final Bankquick.Info in = new Bankquick.Info();
									in.setBANKCODE(BANKCODE);
									in.setBANKNAME(BANKNAME);
									in.setCARDNO(CARDNO);
									in.setID(ID);
									in.setTYPENAME(TYPENAME);

									final Bankquick b = new Bankquick();
									b.setResult(result_1);
									b.setValue(in);

									runOnUiThread(new Runnable() {
										public void run() {
											Intent intent = new Intent();
											intent.putExtra("info", in);
											setResult(4321, intent);
											Toast.makeText(UFCAddBankCard.this,
													"添加成功", Toast.LENGTH_SHORT)
													.show();
											finish();
										}
									});
								} else {
									// 添加失败

									System.err.println("======failed 失败");
									final String value_failed = json
											.getString("value");

									runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(UFCAddBankCard.this,
													value_failed,
													Toast.LENGTH_SHORT).show();
										}
									});
								}
							} catch (JSONException e) {

								System.err.println("======解析异常 失败");
								e.printStackTrace();
							}

						} else {
							// 添加失败
							runOnUiThread(new Runnable() {
								public void run() {
									System.err.println("======空 失败");
									Toast.makeText(UFCAddBankCard.this, "添加失败",
											Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
				});
				threadThree.start();

			}
		});

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		finish();
	}

	class BankAdapter extends BaseAdapter {
		private Context context;
		private String[] strData;

		public BankAdapter(Context context, String[] strData) {
			this.context = context;
			this.strData = strData;
		}

		@Override
		public int getCount() {
			return strData.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(context, UFCMResource.getIdByName(
					getApplication(), "layout", "ufcpay_item_add_bank"), null);
			TextView tv_bank = (TextView) view.findViewById(UFCMResource
					.getIdByName(getApplication(), "id", "tv_bank"));

			tv_bank.setText(strData[position]);

			return view;
		}
	}
}
