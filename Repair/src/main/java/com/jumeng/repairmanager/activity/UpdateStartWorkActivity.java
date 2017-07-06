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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UpdateStartWorkActivity extends BaseActivity{
	private EditText et_work_time;
	private Button saveBtn;
	private SharedPreferences sp;
	private int userId;
	private String worktime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_startwork);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId = sp.getInt(Consts.USER_ID, -1);
		worktime=getIntent().getStringExtra("worktime");
		initTitleBar();
		init();
	}
	
	private void initTitleBar() {
		initActionBar(UpdateStartWorkActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "从业时间", null, 0);
		
	}

	private void init() {
		et_work_time = (EditText) findViewById(R.id.startWork);

		saveBtn = (Button) findViewById(R.id.save);
		saveBtn.setOnClickListener(this);
		if(et_work_time==null||et_work_time.equals("null")){
			et_work_time.setText("");
		}else{
			et_work_time.setText(worktime);
			et_work_time.setSelection(et_work_time.getText().length());
		}
	}
	
	/**
	 * 修改籍贯
	 */
	private void updateWorkTime(){
		final String timelong=et_work_time.getText().toString();
		if(timelong.isEmpty()){
			Tools.toast(this, "从业时间不能为空！");
			return;
		}
		RequestParams params=new RequestParams();
		params.put("code",Consts.UODATETIMELONG);
		params.put("workerid", userId);
		params.put("timelong", timelong);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						Tools.toast(UpdateStartWorkActivity.this, "修改成功！");
						Intent intent = new Intent(Sign.UPDATE_STARTWORK);
						intent.putExtra("worktime",timelong);
						sendBroadcast(intent);
						finish();
						break;
					default:
						Tools.toast(UpdateStartWorkActivity.this, response.getString("msg"));
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
			updateWorkTime();
			break;
		}
		
	}

}
