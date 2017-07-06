package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.border;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.rl_titlebar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_center;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.view.ProgressWebView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends BaseActivity {

	private ProgressWebView webView;
	private String url;
	private int flag;
	private int id;
	private int tag;
	private int itemId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		MyApplication.getInstance().addActivities(this);
		flag=getIntent().getIntExtra("flag", -1);
		tag=getIntent().getIntExtra("tag", -1);
		id=getIntent().getIntExtra("id", -1);
		initTitleBar();
		setViews();

	}


	private void initTitleBar() {
		initActionBar(WebActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		String title="";
		switch (flag) {
		case Consts.GUIDEPAGE:
			title="用户指导";
			url=Consts.GUIDE_URL;
			break;
		case Consts.VERSIONINFO:
			title="版本信息";
			url=Consts.VERSION_URL;
			break;
		case Consts.REGISTERPROTOCAL:
			title="注册协议";
			url=Consts.UJIANG_PROTOCOL;
			break;
		case -1:
			switch (tag) {
			case 1:
				itemId=getIntent().getIntExtra("itemId", -1);
				url=Consts.BASE+"WebAPP/Appweb.aspx?infoid="+itemId;
				title="系统消息";
				break;
			case 2:
				itemId=getIntent().getIntExtra("itemId", -1);
				url=Consts.BASE+"WebAPP/Appweb.aspx?infoid="+itemId;
				title="权益福利";
				break;
			}
			
			break;
		}
		setViewContent(null, R.mipmap.left_arrow, title, null, 0);


	}
	private void setViews() {
		webView = (ProgressWebView)findViewById(R.id.webView1);
		WebSettings webSettings = webView.getSettings();  
		webSettings.setJavaScriptEnabled(true);  
		webView.loadUrl(url);  
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		}

	}

}
