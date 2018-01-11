package com.sinoufc.jarinvoke;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;


/**
 * 网银界面调用H5
 */
public class UFCNetBankPay extends Activity {
	private WebView webview;

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		webview = new WebView(this);  
		//设置WebView属性，能够执行Javascript脚本  
        webview.getSettings().setJavaScriptEnabled(true);  
        
        Bundle data = getIntent().getExtras();
        Map<String, String> map = new HashMap<String, String>();
		map.put("orderNo", data.getString("orderNo"));
		map.put("comId", data.getString("comId"));
		map.put("username", data.getString("username"));
		map.put("payCardTypeHidden", "1");
		map.put("payBankHidden", "spdb");
		map.put("payTypeHidden", "net-bank");
		map.put("payMoney",data.getString("payMoney"));
		
		String web = UFCHttpClientUtil.getNetBankURL();
		int i = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (i == 0) {
				web += "?" + entry.getKey() + "=" + entry.getValue();
			} else {
				web += "&" + entry.getKey() + "=" + entry.getValue();
			}
			i++;
		}
		
        //加载需要显示的网页  
        webview.loadUrl(web);  
        //设置Web视图  
        setContentView(webview);  
       
    }  
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		Intent intent = new Intent();
		intent.putExtra("mark", "mark");
		setResult(54321, intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(webview != null){
			webview.destroy();
			
		}
		
	}

}
