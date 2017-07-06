package com.jumeng.repairmanager.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import static com.jumeng.repairmanager.MyApplication.getSharedPref;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

public class UpdateNativeActivity extends BaseActivity {
	private EditText et_nagive;
	private Button saveBtn;
	private SharedPreferences sp;
	private int userId;
	private String nativeplace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_nagive);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId = sp.getInt(Consts.USER_ID, -1);
		nativeplace=getIntent().getStringExtra("nativeplace");
		initTitleBar() ;
		init();
	}

	private void initTitleBar() {
		initActionBar(UpdateNativeActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "籍贯", null, 0);
		
	}
	
	private void init() {
		et_nagive = (EditText) findViewById(R.id.nagive);
		saveBtn = (Button) findViewById(R.id.save);
		saveBtn.setOnClickListener(this);

		if(et_nagive==null||et_nagive.equals("null")){
			et_nagive.setText("");
		}else{
			et_nagive.setText(nativeplace);
			et_nagive.setSelection(et_nagive.getText().length());
		}
	}

	/**
	 * 修改籍贯
	 */
	private void updateNative(){
		String nagive=et_nagive.getText().toString();
		if(nagive.isEmpty()){
			Tools.toast(this, "籍贯不能为空！");
			return;
		}
		RequestParams params=new RequestParams();
		params.put("code",Consts.UPDATEJG);
		params.put("nursing", userId);
		params.put("nativeplace", nagive);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						Tools.toast(UpdateNativeActivity.this, "修改成功！");
						/*Intent intent = new Intent(Sign.UPDATE_INFO);
						sendBroadcast(intent);*/
						finish();
						break;
					default:
						Tools.toast(UpdateNativeActivity.this, response.getString("msg"));
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
			updateNative();
			break;
		}
		
	}

}
