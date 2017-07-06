package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.MyApplication.getInstance;
import static com.jumeng.repairmanager.util.SetActionBar.border;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.rl_titlebar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_center;
import static com.jumeng.repairmanager.util.SetActionBar.tv_right;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.BalanceListAdapter;
import com.jumeng.repairmanager.bean.BalanceList;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * 我的钱包
 */
public class MyBalanceActivity  extends BaseActivity implements OnRefreshListener {
	private static final String TAG = MyBalanceActivity.class.getSimpleName();
	private TextView mAllDetail;
	private TextView mAllMoney;
	private LoadingDialog mDialog;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private BalanceListAdapter adapter;
	private List<BalanceList> list=new ArrayList<>();
	private TextView tv_1;
	private TextView tv_2;
	private TextView tv_yue;
	private TextView tv_balance;
	private SharedPreferences sp;
	private int userId;

	private int page=1;
	private int pageNum=20;
	private TextView tv_tips;
	private TextView tv_3;
	private TextView tv_order_num;
	private double maxCash;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_balance);
		MyApplication.getInstance().addActivities(this);
		sp=MyApplication.getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);
		initTitleBar() ;
		setViews();
		initListview( );
		//getIncome();
		getCanCash ();
		//getIOList(page);
		registerBoradcastReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getIOList(page);
		getIncome();
	}

	private void initTitleBar() {
		initActionBar(MyBalanceActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "我的收入",null, 0);
	}

	private void setViews() {
		tv_1=(TextView)findViewById(R.id.tv_1);
		tv_2=(TextView)findViewById(R.id.tv_2);
		tv_3=(TextView)findViewById(R.id.tv_3);
		tv_order_num=(TextView)findViewById(R.id.tv_order_num);
		tv_balance=(TextView)findViewById(R.id.tv_balance);
		//tv_balance.setText(""+getInstance().getBalance());
		tv_yue=(TextView)findViewById(R.id.textview_yue);
		tv_tips=(TextView)findViewById(R.id.tv_tips);
		tv_1.setText("*您在U匠App共接了 单,收入总额共 元");
		tv_2.setText("*您在U匠App平台共提现 元,待审核共 元");
		tv_order_num.setText(getInstance().getOrderCount()+"单");

	}

	private void initListview( ) {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView_balance);
		layout.setOnRefreshListener(this);
		adapter=new BalanceListAdapter(this, list);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/
	}

	/**
	 *查看累计提现，待审核金额
	 */
	public void getTX(){

		RequestParams params=new RequestParams();
		params.put("code",Consts.GETTX);
		params.put("nursing", userId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							double totalCash=response.getDouble("accumulative");
							double waitVeify=response.getDouble("Notaudit");
							tv_1.setText("*您在U匠App共接了");
							Spannable spannable1 = new SpannableString(getInstance().getOrderCount()+"");
							spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7906")), 0,spannable1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							tv_1.append(spannable1);
							tv_1.append("单,收入总额共");
							Spannable spannable2 = new SpannableString(getInstance().getTotalIncome()+"");
							spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7906")), 0,spannable2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							tv_1.append(spannable2);
							tv_1.append("元");

							tv_2.setText("*您在U匠App平台共提现");
							Spannable spannable3 = new SpannableString(totalCash+"");
							spannable3.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7906")), 0,spannable3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							tv_2.append(spannable3);

							tv_2.append("元,待审核共");
							Spannable spannable4 = new SpannableString(waitVeify+"");
							spannable4.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7906")), 0,spannable4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							tv_2.append(spannable4);
							tv_2.append("元");

							tv_order_num.setText(spannable1);
							tv_order_num.append("单");



							//tv_1.setText("*您在U匠App共接了"+getInstance().getOrderCount()+"单,收入总额共"+getInstance().getTotalIncome()+"元");
							//+tv_2.setText("*您在U匠App平台共提现"+totalCash+"元,待审核共"+waitVeify+"元");

							break;
						default:
							//Tools.toast(MyBalanceActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});


	}
	/**
	 * 获取累计收入
	 */
	public void getIncome(){

		RequestParams params=new RequestParams();
		params.put("code",Consts.GETSHOURU);
		params.put("nursing", userId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							//tv_3.setText("*累计服务收入");
							MyApplication.getInstance().setTotalIncome(response.getDouble("money"));
							getTX();
							/*Spannable spannable1 = new SpannableString(response.getDouble("money")+"");
							spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7906")), 0,spannable1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							tv_3.append(spannable1);
							tv_3.append("元");*/
							break;
						default:
							Tools.toast(MyBalanceActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});
	}

	/**
	 * 获取最大可提现金额
	 */
	private void getCanCash (){

		RequestParams params=new RequestParams();
		params.put("code",Consts.GETKOUCHUCAILIAO);
		params.put("nursing", userId);
		HttpUtil.post( params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							maxCash=response.getDouble("ktnum");
							tv_balance.setText(maxCash+"");

							break;
						default:
							Tools.toast(MyBalanceActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


	/**
	 * 获取收支记录
	 */
	public void getIOList(final int page){

		RequestParams params=new RequestParams();
		params.put("code",Consts.GETCZJL);
		params.put("nursing", userId);
		params.put("page", page);
		params.put("pagenum", pageNum);
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
							tv_tips.setVisibility(View.GONE);
							String data=response.getString("data");
							JSONArray ary=new JSONArray(data);
							List<BalanceList> balanceList=JsonParser.parseIOList(ary);

							list.addAll(balanceList);
							break;
						case 2:
							if(page>1){
								tv_tips.setVisibility(View.GONE);
							}else{
								tv_tips.setVisibility(View.VISIBLE);
							}
							break;
						default:
							Tools.toast(MyBalanceActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}

		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.tv_right:
				//Tools.StartActivitytoOther(this, GetCashActivity.class);
				break;
			case R.id.btn_get_money:
				Intent intent=new Intent(this, CashActivity.class);
				intent.putExtra("maxCash",maxCash);
				startActivity(intent);
				break;
		}

	}




	/**
	 * 创建一个广播接收参数
	 * */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Consts.REFESH)) {
				getTX();
				getCanCash();
			}
		}
	};

	/**
	 * 注册广播
	 * */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter1 = new IntentFilter();
		myIntentFilter1.addAction(Consts.REFESH);
		this.registerReceiver(mBroadcastReceiver, myIntentFilter1);
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
				getIOList(page);
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
				getIOList(page);
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mBroadcastReceiver);
	}
}
