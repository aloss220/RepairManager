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
import com.jumeng.repairmanager.adapter.BalanceListAdapter;
import com.jumeng.repairmanager.adapter.CommentListAdapter;
import com.jumeng.repairmanager.adapter.CustomerListAdapter;
import com.jumeng.repairmanager.adapter.MessageListAdapter;
import com.jumeng.repairmanager.adapter.RightListAdapter;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.CommentList;
import com.jumeng.repairmanager.bean.CustomerList;
import com.jumeng.repairmanager.bean.MessageList;
import com.jumeng.repairmanager.bean.RightList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MyRightActivity  extends BaseActivity implements OnRefreshListener {
	private static final String TAG = MyRightActivity.class.getSimpleName();
	private int page = 1;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private RightListAdapter adapter;
	private List<RightList> list=new ArrayList<>();
	private SharedPreferences sp;
	private int userId;
	private int pageNum;
	private LoadingDialog loadingDialog;
	private TextView tv_tips;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_right);
		MyApplication.getInstance().addActivities(this);
		sp=MyApplication.getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);
		initTitleBar() ;
		initListview( );
		//getRightList ();
	}


	private void initTitleBar() {
		initActionBar(MyRightActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "权益福利", null, 0);

		tv_tips=(TextView)findViewById(R.id.tv_tips);
	}

	private void initListview( ) {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new RightListAdapter(this, list);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(MyRightActivity.this, WebActivity.class);
				intent.putExtra("itemId", list.get(position).getItemId());
				intent.putExtra("tag", 2);
				startActivity(intent);
				
			}
		});
	}
	
	/**
	 * 获取权益福利列表
	 */
	/*public void getRightList (){
		loadingDialog=new LoadingDialog(this, "正在获取权益福利列表...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.WORKER+Consts.GETFULI, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						//Tools.toast(MyRightActivity.this, "获取权益福利成功！");
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<RightList> rightList=JsonParser.parseRightList(ary);
						list.addAll(rightList);
						adapter.notifyDataSetChanged();
						
						if(rightList.size()==0){
							layout.setVisibility(View.GONE);
							tv_tips.setVisibility(View.VISIBLE);
						}else{
							layout.setVisibility(View.VISIBLE);
							tv_tips.setVisibility(View.GONE);
						}
						
						break;
					case 2:
						loadingDialog.dismiss();
						if(page>1){
							tv_tips.setVisibility(View.GONE);
						}else{
							tv_tips.setVisibility(View.VISIBLE);
						}
						break;
					default:
						loadingDialog.dismiss();
						Tools.toast(MyRightActivity.this, response.getString("msg"));
						break;
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
	}*/



	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		}

	}







	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				adapter.notifyDataSetChanged();
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
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
