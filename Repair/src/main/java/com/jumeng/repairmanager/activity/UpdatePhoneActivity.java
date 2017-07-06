package com.jumeng.repairmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Sign;
import com.jumeng.repairmanager.util.Tools;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.jumeng.repairmanager.MyApplication.getSharedPref;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

public class UpdatePhoneActivity extends BaseActivity {
	private EditText et_phone;
	private EditText et_code;
	private TextView tv_get_code,tv_code;
	private Timer timer = new Timer();
	private TimerTask task;
	private SharedPreferences sp;
	private int userId;
	private String code;
	private TimeCount timeCount;
	private long time=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_phone);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId = sp.getInt(Consts.USER_ID, -1);
		initTitleBar();
		init();
	}

	private void initTitleBar() {
		initActionBar(UpdatePhoneActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "修改手机号码", null, 0);
	}

	private void init() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_code = (EditText) findViewById(R.id.et_code);
		tv_code = (TextView) findViewById(R.id.tv_code);
		tv_get_code = (TextView) findViewById(R.id.tv_get_code);
	}

	/**
	 * 获取验证码
	 */
	public void getIdentifyCode(){
		int digital=(int)((Math.random()*9+1)*100000);
		//用户输入的电话号码
		String phone = et_phone.getText().toString().trim();
		//非空验证
		if(phone.isEmpty()){
			Tools.toast(this, "手机号不能为空");
			return;
		}
		//验证手机号的长度
		if(!Tools.isMobileNO(phone)){
			Tools.toast(this, "手机号无效!");
			return;
		}
		//隐藏输入法
		InputMethodManager inputMethodManager = (InputMethodManager)
				getSystemService(INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(et_phone.getWindowToken(), 0);
		if(timeCount==null){
			//获取验证码倒计时
			timeCount = new TimeCount(60000,1000);
		}else{
			timeCount.onTick(6000);
		}
		timeCount.start();

		RequestParams params=new RequestParams();
		params.put("code",Consts.GETCODE);
		params.put("phone", phone);
		params.put("logintype", 1);
		params.put("type", 0);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(UpdatePhoneActivity.this, "获取成功！");
							code = response.getString("TestIng");
							break;
						default:
							Tools.toast(UpdatePhoneActivity.this, response.getString("msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.tv_get_code:
				tv_get_code.setText("重新获取");
				tv_code.setVisibility(View.VISIBLE);
				if(System.currentTimeMillis()-time>12000){
					getIdentifyCode();
					time=System.currentTimeMillis();
				}else{
					Tools.toast(this, "操作过于频繁,请稍后再试");
				}
				break;
			case R.id.btn_submit:
				updatePhone();
				break;

		}
	}

	/**
	 * 修改手机号
	 */
	private void updatePhone(){

		if(!et_code.getText().toString().equals(code)){
			Tools.toast(this, "验证码错误!");
			return;
		}
		String phone=et_phone.getText().toString();
		RequestParams params=new RequestParams();
		params.put("code",Consts.UPDATETEL);
		params.put("workerid", userId);
		params.put("tel", phone);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(UpdatePhoneActivity.this, "修改成功！");
							/*Intent intent = new Intent(Sign.UPDATE_INFO);
							sendBroadcast(intent);*/
							finish();
							break;
						default:
							Tools.toast(UpdatePhoneActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//btn_code.setClickable(false);
			tv_code.setText("("+(millisUntilFinished/1000)+"s"+")");
		}

		@Override
		public void onFinish() {
			tv_get_code.setText("获取验证码");
			tv_code.setVisibility(View.GONE);
			tv_get_code.setClickable(true);
		}
	}


}
