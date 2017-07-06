package com.jumeng.repairmanager.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.activity.ContentActivity;
import com.jumeng.repairmanager.activity.MyMessageActivity;
import com.jumeng.repairmanager.adapter.MessageListAdapter;
import com.jumeng.repairmanager.adapter.OrderAdapter;
import com.jumeng.repairmanager.bean.MessageList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 即时订单fragemnt
 *
 */
@SuppressLint("ValidFragment")
public class SysMsgFragment extends Fragment implements OnRefreshListener{
	private PullableListView listView;
	private SharedPreferences sp;
	private int userId;
	private MyBroadcast receiver;
	private TextView tv_tips;
	private PullToRefreshLayout layout;
	private int page = 1;//加载的页数
	private int pagenum=10;
	private boolean isFirstIn = true;
	private MessageListAdapter adapter;
	private List<MessageList> list=new ArrayList<>();
	private LoadingDialog loadingDialog;
	private int type=2;//1用户端 2工人端
	private int pageNum=15;
	public SysMsgFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_sys_msg, container, false);
		tv_tips=(TextView)view.findViewById(R.id.tv_tips);

		if(sp == null){
			sp = MyApplication.getSharedPref();
		}
		userId=sp.getInt(Consts.USER_ID, -1);
		initListview(view);

		//getMessage(page);
		registerMyBroadcast();
		
		return view;
	}

	private void initListview(View view) {
		layout=(PullToRefreshLayout)view.findViewById(R.id.refresh_view);
		listView=(PullableListView)view.findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new MessageListAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/
	}

	/**
	 * 获取系统消息
	 */
	/*private void getMessage (final int page){
		loadingDialog=new LoadingDialog(getActivity(), "正在获取消息列表...");
		loadingDialog.show();
		
		RequestParams params=new RequestParams();
		params.put("type", type);
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.PUBLIC+Consts.GETINFOLIST, params, new MyJsonHttpResponseHandler(getActivity()){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						//Tools.toast(getActivity(), "获取系统给消息成功！");
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<MessageList> messageList=JsonParser.parseMessageList(ary);
						if(page==1){
							list.clear();
						}
						list.addAll(messageList);
						adapter.notifyDataSetChanged();
						
						break;
					default:
						loadingDialog.dismiss();
						//Tools.toast(getActivity(), response.getString("msg"));
						break;
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
	}*/




	private void registerMyBroadcast() {
		receiver=new MyBroadcast();
		IntentFilter filter=new IntentFilter();
		getActivity().registerReceiver(receiver, filter);
	}

	class MyBroadcast extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent intent) {

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
				//getMessage(page);
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
				//getMessage(page);
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}

}
