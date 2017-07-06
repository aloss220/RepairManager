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
import com.jumeng.repairmanager.adapter.CashListAdapter;
import com.jumeng.repairmanager.bean.CashList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.view.LoadingDialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class GetCashActivity extends BaseActivity implements OnRefreshListener{

	private PullableListView listView;
	private PullToRefreshLayout refresh_view;
	private int page=1;
	private boolean isFirstIn = true;//是否自动刷新
	private CashListAdapter adapter;
	private SharedPreferences sharedPreferences;
	private LoadingDialog loadingDialog;
	private int userId;
	private PullToRefreshLayout layout;
	private List<CashList> list=new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_cash);
		MyApplication.getInstance().addActivities(this);
		if(sharedPreferences == null){
			sharedPreferences = MyApplication.getSharedPref();
		}
		userId=sharedPreferences.getInt(Consts.USER_ID, -1);
		initTitleBar();
		initListview( );
		setViews();
		initListview();
	}



	private void initTitleBar() {
		initActionBar(GetCashActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow_g, "提现记录", null, 0);
		rl_titlebar.setBackgroundColor(Color.parseColor("#F8F8F8"));
		tv_center.setTextColor(Color.parseColor("#333333"));
		border.setVisibility(View.VISIBLE);

	}
	
	private void setViews() {

	}

	private void initListview() {
		refresh_view=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView);
		refresh_view.setOnRefreshListener(this);
		adapter=new CashListAdapter(this, getData());
		listView.setAdapter(adapter);
		/*	if(isFirstIn){
			refresh_view.autoRefresh();
			isFirstIn=false;
		}*/


	}



	private List<CashList> getData() {
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		return list;
	}
	private void moreData(){
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
		list.add(new CashList("2016-02-12 12:14:02", 205.00));
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;

		}

	}


	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {
			public void run() {
				page=1;
				moreData();
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);


	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {
			public void run() {
				page++;
				moreData();
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

			}
		}, 500);

	}
}
