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

import com.hyphenate.easeui.EaseConstant;
import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.CustomerListAdapter;
import com.jumeng.repairmanager.bean.CustomerList;
import com.jumeng.repairmanager.hxchat.ChatActivity;
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

public class MyCustomerActivity  extends BaseActivity implements OnRefreshListener {
	private static final String TAG = MyCustomerActivity.class.getSimpleName();
	private int page = 1;
	private int pageNum=15;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private CustomerListAdapter adapter;
	private List<CustomerList> list=new ArrayList<>();
	private SharedPreferences sp;
	private int userId;
	private LoadingDialog loadingDialog;
	private String im;
	private TextView tv_tips;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_customer);
		MyApplication.getInstance().addActivities(this);
		sp=MyApplication.getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);
		/*if(sharePreferences == null){
			sharePreferences = getSharedPreferences(Consts.IM,Context.MODE_PRIVATE);
		}*/
		im = sp.getString(Consts.IM, null);
		initTitleBar() ;
		initListview( );
		//getCstomers(page);
	}


	private void initTitleBar() {
		initActionBar(MyCustomerActivity.this);

		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "服务客户", null, 0);

		tv_tips=(TextView)findViewById(R.id.tv_tips);
	}

	private void initListview( ) {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new CustomerListAdapter(this, list);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/


		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent();
				intent.setClass(MyCustomerActivity.this, ChatActivity.class);
				intent.putExtra(EaseConstant.EXTRA_USER_ID,  list.get(position).getHxloginname());
				startActivity(intent);
			}
		});
	}


	/**
	 * 获取服务客户
	 */
	/*public void getCstomers (final int page){
		loadingDialog=new LoadingDialog(this, "正在获取客户列表...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("nursing", userId);
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.WORKER+Consts.GETFWUSERLIST, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if(page==1){
					list.clear();
				}
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						//Tools.toast(MyCustomerActivity.this, "获取服务客户成功！");
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<CustomerList> customerList=JsonParser.parseCustomerList(ary);
						list.addAll(customerList);
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
						Tools.toast(MyCustomerActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();

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
				page=1;
				//getCstomers(page);
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
				page++;
				//getCstomers(page);
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
