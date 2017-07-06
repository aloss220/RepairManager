package com.jumeng.repairmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import static com.jumeng.repairmanager.util.SetActionBar.border;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.rl_titlebar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_center;

public class CashActivity extends BaseActivity {

	private EditText et_money;
	private SharedPreferences sp;
	private int userId;
	private LoadingDialog loadingDialog;
	private double maxCash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash);
		MyApplication.getInstance().addActivities(this);
		
		sp=MyApplication.getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);
		maxCash=getIntent().getDoubleExtra("maxCash",0.0);
		initTitleBar();
		setViews();
	}
	private void initTitleBar() {
		initActionBar(CashActivity.this);

		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "申请提现", null, 0);
	}
	
	private void setViews() {
		et_money=(EditText)findViewById(R.id.et_money);
		et_money.setHint("您的最大可提现金额为"+maxCash+"元");
	}
	
	
	/**
	 * 申请提现
	 */
	private void getCash (){
		String money=et_money.getText().toString();
		if(money.isEmpty()){
			Tools.toast(this, "请输入提现金额");
			return;
		}

		if(Double.parseDouble(money)>maxCash){
			Tools.toast(this, "超出最大可提现金额，请重新填写！");
			return;
		}
		loadingDialog=new LoadingDialog(this, "提现中...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.ADDWITHDRAWAL);
		params.put("nursing", userId);
		params.put("money", money);
		params.put("ktnum", maxCash);
		HttpUtil.post( params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						Tools.toast(CashActivity.this, "申请成功，等待管理员审核处理 ！");
						finish();
						 Intent	intent1 = new Intent(Consts.REFESH);
						 
						CashActivity.this.sendBroadcast(intent1);
						break;
					default:
						loadingDialog.dismiss();
						Tools.toast(CashActivity.this, response.getString("state_msg"));
						break;
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
	}

	
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.btn_cash:
			getCash ();
			break;

		}
	}

}
