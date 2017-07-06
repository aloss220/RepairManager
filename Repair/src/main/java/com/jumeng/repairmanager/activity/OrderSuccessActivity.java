package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.util.ArrayList;
import java.util.List;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.CashListAdapter;
import com.jumeng.repairmanager.bean.CashList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OrderSuccessActivity extends BaseActivity {

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
	private TextView tv_order_no;
	private String orderNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_success);
		MyApplication.getInstance().addActivities(this);
		if(sharedPreferences == null){
			sharedPreferences = MyApplication.getSharedPref();
		}
		userId=sharedPreferences.getInt(Consts.USER_ID, -1);
		orderNo=getIntent().getStringExtra("orderNo");
		initTitleBar();
		setViews();
	}



	private void initTitleBar() {
		initActionBar(OrderSuccessActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "下单成功", null, 0);

	}
	
	private void setViews() {
		tv_order_no=(TextView)findViewById(R.id.tv_order_no);
		tv_order_no.setText(orderNo);
	}

	

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.tv_look:
			Tools.StartActivitytoOther(this, OrderActivity.class);
			finish();
			break;

		}

	}


	
}
