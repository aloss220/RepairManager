package com.jumeng.repairmanager.hxchat;

import org.apache.http.Header;
import org.json.JSONObject;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;


public class ChatActivity extends EaseBaseActivity{
	public static ChatActivity activityInstance;
	private EaseChatFragment chatFragment;
	String toChatUsername;
	private String image;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_chat_ui);
		
		activityInstance = this;
		//聊天人或群id
		toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
		//getNickName();
		chatFragment = new EaseChatFragment();
		
		//传入参数
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}
	@Override
	public void onBackPressed() {
		chatFragment.onBackPressed();
	}

	public String getToChatUsername(){
		return toChatUsername;
	}
	
	/*private void getNickName(){
		RequestParams params = new RequestParams();
		params.put("type", 0);
		params.put("hxloginname",toChatUsername);
		HttpUtil.post( Consts.PUBLIC+Consts.GETHXICON,params, new MyJsonHttpResponseHandler(ChatActivity.this) {
			

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						String name = response.getString("name");
						Bundle args = new Bundle();
						args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
						args.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);	
						args.putString("hxname", name);	
						chatFragment.setArguments(args);				
						getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
						break;
					case 101:
						Tools.toast(ChatActivity.this, "失败");
						break;
					case 102:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}*/
}
