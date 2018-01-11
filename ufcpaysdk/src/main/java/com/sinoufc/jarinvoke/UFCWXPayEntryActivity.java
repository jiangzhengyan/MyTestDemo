package com.sinoufc.jarinvoke;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class UFCWXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(UFCMResource.getIdByName(getApplication(), "layout", "ufcsdk_wx_pay_result"));
        
    	api = WXAPIFactory.createWXAPI(this, UFCConstants.WXAPP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			 int code = resp.errCode;

		        switch (code){
		            case 0://支付成功后的界面
		            	
		                break;
		            case -1:
		            //    Toast.makeText(UFCWXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
		                break;
		            case -2://用户取消支付后的界面
		                break;
		        }
		        finish();
		  
		}
	}
};