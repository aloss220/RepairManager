package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.hyphenate.chat.EMClient;
import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.update.UpdateManager;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.AlertDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {

	private RelativeLayout rl_version_update;
	private TextView tv_version;
	private RelativeLayout rl_version_info;
	private RelativeLayout rl_guide;
	private SharedPreferences sp;
	private int userId;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		context=this;
		MyApplication.getInstance().addActivities(this);
		if(sp == null){
			sp = MyApplication.getSharedPref();
		}
		userId=sp.getInt(Consts.USER_ID, -1);
		initTitleBar();
		setViews();
	}
	private void initTitleBar() {
		initActionBar(SettingsActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "设置", null, 0);

	}

	private void setViews() {
		rl_version_update=(RelativeLayout)findViewById(R.id.rl_version_update);
		rl_version_info=(RelativeLayout)findViewById(R.id.rl_version_info);
		rl_guide=(RelativeLayout)findViewById(R.id.rl_guide);

		rl_version_update.setOnClickListener(this);
		rl_version_info.setOnClickListener(this);
		rl_guide.setOnClickListener(this);

		tv_version=(TextView)findViewById(R.id.tv_version);
		tv_version.setText("V"+Tools.getVersionName(this));
	}

	@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.rl_version_update:
			Tools.toast(context,"已是最新版本");
			break;
		case R.id.rl_version_info:

			intent.setClass(this, WebActivity.class);
			intent.putExtra("flag",Consts.VERSIONINFO );
			startActivity(intent);
			break;
		case R.id.rl_guide:
			Tools.toast(this, "正在努力赶工中，敬请期待");
			/*intent.setClass(this, WebActivity.class);
			intent.putExtra("flag", Consts.GUIDEPAGE);
			startActivity(intent);*/
			break;
		case R.id.btn_logoff:
			cancel();
			break;
		}

	}
	


	/**
	 * 退出登录
	 */
	public void Logout(){

		RequestParams params=new RequestParams();
		params.put("code",Consts.LOGGEDOUT);
		params.put("type", 1);
		params.put("userid", userId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1: {
						Intent intent = new Intent();
						intent.setAction(Consts.END_WORK);
						sendBroadcast(intent);
						MyApplication.getSharedPref().edit().clear().commit();
						Tools.StartActivitytoOther(SettingsActivity.this, LoginActivity.class);
						EMClient.getInstance().logout(true);
						MyApplication.getInstance().finishActivities();
					}
						break;
					default:
						Tools.toast(SettingsActivity.this, response.getString("state_msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}
	private void cancel(){
		new AlertDialog(this).builder().setTitle("确认退出?")
		.setPositiveButton("确定",new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Logout();

				
			}

		}).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(View arg0) {


			}
		}).show();



	}


}
