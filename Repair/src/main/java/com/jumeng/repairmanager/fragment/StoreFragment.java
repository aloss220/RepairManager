package com.jumeng.repairmanager.fragment;

import static com.jumeng.repairmanager.MyApplication.getSharedPref;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.OrderAdapter;
import com.jumeng.repairmanager.adapter.StoreOrderOutAdapter;
import com.jumeng.repairmanager.bean.OrderList;
import com.jumeng.repairmanager.bean.ProductTypeTwo;
import com.jumeng.repairmanager.bean.StoreOrderIn;
import com.jumeng.repairmanager.bean.StoreOrderOut;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 即时订单fragemnt
 *
 */
@SuppressLint("ValidFragment")
public class StoreFragment extends Fragment implements OnRefreshListener{
	private PullableListView listView;
	private int orderStatus;//0 全部订单 1已取消 2待服务3服务中 4待评价
	private SharedPreferences sharedPreferences;
	private int userId;
	private MyBroadcast receiver;
	private TextView tv_tips;
	private PullToRefreshLayout layout;
	private int page = 1;//加载的页数
	private int pageNum=10;
	private boolean isFirstIn = true;
	private ArrayList<StoreOrderOut> list=new ArrayList<>();
	private ArrayList<StoreOrderIn> list2=new ArrayList<>();
	private StoreOrderOutAdapter adapter;
	private LoadingDialog loadingDialog;
	private SharedPreferences sp;

	public StoreFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_store, container, false);
		tv_tips=(TextView)view.findViewById(R.id.tv_tips);
		sp=getSharedPref();
		userId = sp.getInt(Consts.USER_ID, -1);
		if(sharedPreferences == null){
			sharedPreferences = getActivity().getSharedPreferences(Consts.USER_FILE_NAME, getActivity().MODE_PRIVATE);
		}
		userId=sharedPreferences.getInt(Consts.USER_ID, -1);
		initListview(view);

		//getStoreList(page);
		registerMyBroadcast();

		StoreOrderOutAdapter.h=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				switch (msg.what) {
				case 1:
					//getStoreList(page);
					break;
				}
			}
		};

		return view;
	}

	private void initListview(View view) {
		layout=(PullToRefreshLayout)view.findViewById(R.id.refresh_view);
		listView=(PullableListView)view.findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new StoreOrderOutAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/
	}


	/**
	 * 获取商城订单
	 */
	/*private void getStoreList(final int page){
		loadingDialog=new LoadingDialog(getActivity(), "正在获取订单列表...");
		loadingDialog.show();

		RequestParams params=new RequestParams();
		params.put("workerid", userId);
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.WORKER+Consts.GETPRODUCTORDERLIST, params, new MyJsonHttpResponseHandler(getActivity()){
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
						loadingDialog.dismiss();
						//Tools.toast(MyCommentActivity.this, "获取评论列表成功！");
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<StoreOrderOut> orderOut=JsonParser.parseStoreOutList(ary);
						list.addAll(orderOut);

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
						//Tools.toast(MyCommentActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}

		});

	}*/

	private void registerMyBroadcast() {
		receiver=new MyBroadcast();
		IntentFilter filter=new IntentFilter(Consts.PRODUCT_SEND);
		getActivity().registerReceiver(receiver, filter);
	}

	class MyBroadcast extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent intent) {
			//getStoreList(page);
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
				//getStoreList(page);
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
				//getStoreList(page);
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}
}
