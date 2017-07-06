package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.border;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.rl_titlebar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_center;

import java.util.ArrayList;
import java.util.List;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.BalanceListAdapter;
import com.jumeng.repairmanager.adapter.MessageListAdapter;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.MessageList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContentActivity  extends BaseActivity  {
	private static final String TAG = ContentActivity.class.getSimpleName();
	private int page = 1;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private MessageListAdapter adapter;
	private List<MessageList> list=new ArrayList<>();
	private String title;
	private String time;
	private String content;
	private TextView tv_time;
	private TextView tv_content;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_content);
		MyApplication.getInstance().addActivities(this);
		 title=getIntent().getStringExtra("title");
		 time=getIntent().getStringExtra("time");
		 content=getIntent().getStringExtra("content");
		initTitleBar() ;
	}


	private void initTitleBar() {
		initActionBar(ContentActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow_g, title, null, 0);
		rl_titlebar.setBackgroundColor(Color.parseColor("#F8F8F8"));
		tv_center.setTextColor(Color.parseColor("#333333"));
		border.setVisibility(View.VISIBLE);


		tv_time=(TextView)findViewById(R.id.tv_time);
		tv_content=(TextView)findViewById(R.id.tv_content);
		tv_time.setText(time);
		tv_content.setText(content);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
