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
import com.jumeng.repairmanager.adapter.MessageListAdapter;
import com.jumeng.repairmanager.bean.MessageList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MyMessageActivity  extends BaseActivity implements OnRefreshListener{
	private static final String TAG = MyMessageActivity.class.getSimpleName();
	private int page = 1;
	private List<MessageList> list=new ArrayList<>();
	private int type=2;//1用户端 2工人端
	private int pageNum=15;
	private LoadingDialog loadingDialog;
	private RadioButton rb0;
	private RadioButton rb1;
	private RadioGroup radioGroup;
	private ViewPager viewpager;
	private List<Fragment> fragments=new ArrayList<>();
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private MessageListAdapter adapter;
	private TextView tv_tips;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_message);
		MyApplication.getInstance().addActivities(this);
		initTitleBar() ;
		setViews();
		initListview() ;
		//getMessage(page);
	}


	private void initTitleBar() {
		initActionBar(MyMessageActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "我的消息", null, 0);
	}

	private void setViews() {
		tv_tips=(TextView)findViewById(R.id.tv_tips);
	}

	private void initListview() {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new MessageListAdapter(this, list);
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
		loadingDialog=new LoadingDialog(this, "正在获取消息列表...");
		loadingDialog.show();

		RequestParams params=new RequestParams();
		params.put("type", type);
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.PUBLIC+Consts.GETINFOLIST, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						tv_tips.setVisibility(View.GONE);
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
						//Tools.toast(getActivity(), response.getString("msg"));
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
				page=1;
				//getMessage(page);
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
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}



}
