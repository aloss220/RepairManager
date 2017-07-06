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
import com.jumeng.repairmanager.adapter.MessageListAdapter;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.CommentList;
import com.jumeng.repairmanager.bean.CustomerList;
import com.jumeng.repairmanager.bean.MessageList;
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MyCommentActivity  extends BaseActivity implements OnRefreshListener {
	private static final String TAG = MyCommentActivity.class.getSimpleName();
	private int page = 1;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private CommentListAdapter adapter;
	private List<CommentList> list=new ArrayList<>();
	private SharedPreferences sp;
	private int userId;
	private int pageNum=15;
	private LoadingDialog loadingDialog;
	private TextView tv_tips;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_comment);
		MyApplication.getInstance().addActivities(this);
		sp=MyApplication.getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);
		initTitleBar() ;
		initListview( );
		getCommentList (page);
			
	}


	private void initTitleBar() {
		initActionBar(MyCommentActivity.this);

		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "我的评价", null, 0);

		tv_tips=(TextView)findViewById(R.id.tv_tips);
	}

	private void initListview( ) {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new CommentListAdapter(this, list);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/
	}
	/**
	 * 获取评论列表
	 */
	private void getCommentList (final int page){
		loadingDialog=new LoadingDialog(this, "正在获取评价列表...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETPJLIST);
		params.put("worker", userId);
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						tv_tips.setVisibility(View.GONE);
						loadingDialog.dismiss();
						String data=response.getString("data");
						JSONArray ary=new JSONArray(data);
						List<CommentList> commentList=JsonParser.parseCommentList(ary);
						if(page==1){
							list.clear();
						}
						list.addAll(commentList);
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
						//Tools.toast(MyCommentActivity.this, response.getString("state_msg"));
						break;
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
	}

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
				getCommentList (page);
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
				getCommentList (page);
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
