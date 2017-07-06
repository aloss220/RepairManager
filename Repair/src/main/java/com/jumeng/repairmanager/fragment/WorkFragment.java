package com.jumeng.repairmanager.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.OrderAdapter;
import com.jumeng.repairmanager.bean.OrderList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
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

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

/**
 * 即时订单fragemnt
 *
 */
@SuppressLint("ValidFragment")
public class WorkFragment extends Fragment implements OnRefreshListener,MyListener{
	private PullableListView listView;
	private int orderStatus;//0 全部订单 1已取消 2待服务3服务中 4待评价
	private SharedPreferences sp;
	private int userId;
	private MyBroadcast receiver;
	private TextView tv_tips;
	private PullToRefreshLayout layout;
	private int page = 1;//加载的页数
	private int pagenum=10;
	private boolean isFirstIn = true;
	private OrderAdapter adapter;
	private List<OrderList> list=new ArrayList<>();
	private int searchType=0;//0全部 1已取消2已完成
	public WorkFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_work, container, false);
		tv_tips=(TextView)view.findViewById(R.id.tv_tips);

		if(sp == null){
			sp = MyApplication.getSharedPref();
		}
		userId=sp.getInt(Consts.USER_ID, -1);
		initListview(view);

		getOrderList(page,searchType);
		registerMyBroadcast();
		GlobleController.getInstance().addMyListener(this);
		return view;
	}

	private void initListview(View view) {
		tv_tips=(TextView)view.findViewById(R.id.tv_tips);
		layout=(PullToRefreshLayout)view.findViewById(R.id.refresh_view);
		listView=(PullableListView)view.findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new OrderAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/

	}

	/**
	 * 获取订单列表
	 */
	public void getOrderList(final int page,int searchType){
		RequestParams params=new RequestParams();
		params.put("code",Consts.WORKERORDERLIST);
		params.put("workerid", userId);
		params.put("page", page);
		params.put("pagenum", pagenum);
		params.put("sousuotype", searchType);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(getActivity()){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
					if(page==1){
						list.clear();
					}
					
				try {
					switch (response.getInt("state")) {
					case 1:
						tv_tips.setVisibility(View.GONE);
						String data=response.getString("data");
						JSONArray ary=new JSONArray(data);
						List<OrderList> orderList=JsonParser.parseOrderList(ary);
						
						list.addAll(orderList);
						
						break;
					case 2:
						if(page==1){
							list.clear();
						}
						if(page>1){
							tv_tips.setVisibility(View.GONE);
						}else{
							tv_tips.setVisibility(View.VISIBLE);
						}
						break;
					default:
						
						Tools.toast(getActivity(), response.getString("state_msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}

		});

	}





	private void registerMyBroadcast() {
		receiver=new MyBroadcast();
		IntentFilter filter=new IntentFilter(Consts.SEARCH_TYPE);
		getActivity().registerReceiver(receiver, filter);
	}

	class MyBroadcast extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent intent) {
			searchType=intent.getIntExtra("searchType", -1);
			page=intent.getIntExtra("page", -1);
			getOrderList(page,searchType);
		}
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();

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
				getOrderList(page,searchType);
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
				getOrderList(page,searchType);
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}

	@Override
	public void newOrder() {
		// TODO Auto-generated method stub

	}

	@Override
	public void robOrder() {
		getOrderList(page,searchType);

	}

	@Override
	public void payOrder() {
		getOrderList(page,searchType);

	}

	@Override
	public void cancelOrder() {
		getOrderList(page,searchType);

	}

	@Override
	public void startService() {
		getOrderList(page,searchType);

	}

	@Override
	public void endService() {
		getOrderList(page,searchType);

	}

	@Override
	public void arrive() {
		getOrderList(page,searchType);

	}

	@Override
	public void serviceForm() {
		getOrderList(page,searchType);

	}

	@Override
	public void systemForm() {
		getOrderList(page,searchType);

	}

	@Override
	public void pass() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unusualLogin() {
		// TODO Auto-generated method stub

	}
}
