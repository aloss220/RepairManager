package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.MyApplication.getSharedPref;
import static com.jumeng.repairmanager.util.SetActionBar.border;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.rl_titlebar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_center;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Sign;
import com.jumeng.repairmanager.util.Tools;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 更新名字页面
 */
public class UpdateNameActivity extends BaseActivity {
	private EditText et_name;
	private Button saveBtn;
	private SharedPreferences sp;
	private int userId;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_name);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId = sp.getInt(Consts.USER_ID, -1);
		name=getIntent().getStringExtra("name");
		
		initTitleBar();
		init();
	}
	private void initTitleBar() {
		initActionBar(UpdateNameActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "修改姓名", null, 0);
	}
			

	private void init() {
		et_name = (EditText) findViewById(R.id.name);
		saveBtn = (Button) findViewById(R.id.save);
		saveBtn.setOnClickListener(this);

		if(name==null||name.equals("null")){
			et_name.setText("");
		}else{
			et_name.setText(name);
			et_name.setSelection(et_name.getText().length());
		}
	}

	/**
	 * 修改姓名
	 */
	public void updateName(){
		String name=et_name.getText().toString();
		if(name.isEmpty()){
			Tools.toast(this, "姓名不能为空！");
			return;
		}
		RequestParams params=new RequestParams();
		params.put("code",Consts.UODATENAME);
		params.put("workerid", userId);
		params.put("name", name);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						Tools.toast(UpdateNameActivity.this, "修改成功！");
						String name=response.getString("name");
						sp.edit().putString(Consts.USER_NAME, name).commit();
						Intent intent = new Intent(Sign.UPDATE_NAME);
						intent.putExtra("name",name);
						sendBroadcast(intent);
						finish();
						break;
					default:
						Tools.toast(UpdateNameActivity.this, response.getString("state_msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.save:
			Log.e("----","保存名字");
			updateName();
			break;
		}
		
	}

}
