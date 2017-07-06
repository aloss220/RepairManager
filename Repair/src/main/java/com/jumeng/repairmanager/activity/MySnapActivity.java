package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.MySnapAdapter;
import com.jumeng.repairmanager.bean.OrderList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
import com.jumeng.repairmanager.listview.PullableGridView;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.receiver.GlobleController.MyListener;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MySnapActivity extends BaseActivity implements OnRefreshListener{
	private PullableListView listView;
	private MySnapAdapter adapter;
	private SharedPreferences sp;
	private int userId;
	private int page = 1;//加载的页数
	private int pagenum=10;
	private List<OrderList> list=new ArrayList<>();

	private PullToRefreshLayout layout;
	private TextView tv_tips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_snap);
		MyApplication.getInstance().addActivities(this);
		if(sp == null){
			sp = MyApplication.getSharedPref();
		}
		userId=sp.getInt(Consts.USER_ID, -1);
		initTitleBar();
		initListview();
		getPushOrderList(page);

		GlobleController.getInstance().addMyListener(this);
	}
	private void initTitleBar() {
		initActionBar(MySnapActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "我要抢单", null, 0);
		
		tv_tips=(TextView)findViewById(R.id.tv_tips);
	}
	private void initListview() {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		layout.setOnRefreshListener(this);
		listView = (PullableListView) findViewById(R.id.listView);
		adapter = new MySnapAdapter(this,list);
		listView.setAdapter(adapter);

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}


	/**
	 * 获取推送订单列表
	 */
	public void getPushOrderList(final int page){
		
		RequestParams params=new RequestParams();
		params.put("code",Consts.TUISONGORDERLIST);
		params.put("nursing", userId);
		params.put("page", page);
		params.put("pagenum", pagenum);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if(page==1){
					list.clear();
				}
				try {
					switch (response.getInt("state")) {
					case 1:
						//Tools.toast(MySnapActivity.this, "获取成功！");
						String data=response.getString("data");
						JSONArray ary=new JSONArray(data);
						List<OrderList> orderList=JsonParser.parsePushOrderList(ary);

						list.addAll(orderList);
						
						if(orderList.size()==0){
							layout.setVisibility(View.GONE);
							tv_tips.setVisibility(View.VISIBLE);
						}else{
							layout.setVisibility(View.VISIBLE);
							tv_tips.setVisibility(View.GONE);
						}

						break;
					case 2:
						if(page>1){
							tv_tips.setVisibility(View.GONE);
						}else{
							tv_tips.setVisibility(View.VISIBLE);
						}
						
						break;
					default:
						//Tools.toast(MySnapActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
		});
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		}

	}
	@Override
	public void newOrder() {
		getPushOrderList(page);

	}
	@Override
	public void robOrder() {

		//getPushOrderList(page);
		finish();
	}
	
	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				page=1;
				getPushOrderList(page);
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				page++;
				getPushOrderList(page);
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
	
	
	
}
