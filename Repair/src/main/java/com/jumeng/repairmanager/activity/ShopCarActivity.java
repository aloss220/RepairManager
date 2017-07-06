package com.jumeng.repairmanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.ShopCarAdapter;
import com.jumeng.repairmanager.adapter.ShopCarAdapter.CheckInterface;
import com.jumeng.repairmanager.adapter.ShopCarAdapter.ModifyCountInterface;
import com.jumeng.repairmanager.bean.ShopCarList;
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

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jumeng.repairmanager.MyApplication.getSharedPref;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_right;

public class ShopCarActivity  extends BaseActivity implements OnRefreshListener,CheckInterface,ModifyCountInterface {
	private static final String TAG = ShopCarActivity.class.getSimpleName();
	private int page = 1;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private ShopCarAdapter adapter;
	private List<ShopCarList> list=new ArrayList<>();
	private Button btn_order;
	private TextView tv_total;
	private CheckBox cb_select;
	private LoadingDialog loadingDialog;
	private SharedPreferences sp;
	private int userId;
	private int pageNum=10;
	private int totalCount;
	private double totalPrice;
	private LoadingDialog loadingDialog2;
	private Button btn_delete;
	private Handler h=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				//getShopCart(page);

				tv_total.setText("合计：￥" + 0.0);
				btn_order.setText("去结算(" + 0 + ")");
				cb_select.setChecked(false);
				break;
			}

		}
    };
	private LoadingDialog loadingDialog3;
	private MyReceiver receiver;
	private LinearLayout ll_tips;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_car);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId = sp.getInt(Consts.USER_ID, -1);
		initTitleBar() ;
		setViews();
		initListview( );
		//getShopCart(page);
		registerMyReceiver();
	}

	private void initTitleBar() {
		initActionBar(ShopCarActivity.this);

		setViewShow(0, 1, 1, 1, 0);
		setViewContent(null, R.mipmap.left_arrow, "购物车", "编辑", 0);

	}


	private void setViews() {
		btn_delete=(Button)findViewById(R.id.btn_delete);
		btn_order=(Button)findViewById(R.id.btn_order);
		tv_total=(TextView)findViewById(R.id.tv_total);
		cb_select=(CheckBox)findViewById(R.id.cb_select);
		ll_tips=(LinearLayout)findViewById(R.id.ll_tips);
	}
	private void initListview( ) {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new ShopCarAdapter(this, list);
		adapter.setCheckInterface(this);
		adapter.setModifyCountInterface(this);
		listView.setAdapter(adapter);
		/*// 第一次进入自动刷新
		if (isFirstIn) {
			layout.autoRefresh();
			isFirstIn = false;
		}*/
	}

	/**
	 * 获取购物车列表
	 */
	/*private void getShopCart(final int page){

		loadingDialog=new LoadingDialog(this, "正在获取购物车商品...");
		loadingDialog.show();

		RequestParams params=new RequestParams();
		params.put("workerid", userId);
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.WORKER+Consts.GETSHOPLIST, params, new MyJsonHttpResponseHandler(this){
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
						//Tools.toast(MyCommentActivity.this, "获取评论列表成功！");
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<ShopCarList> shopCarList=JsonParser.parseShopCarList(ary);
						list.addAll(shopCarList);

						if(shopCarList.size()==0){
							layout.setVisibility(View.GONE);
							ll_tips.setVisibility(View.VISIBLE);
						}else{
							layout.setVisibility(View.VISIBLE);
							ll_tips.setVisibility(View.GONE);
						}
						break;
					case 2:
						loadingDialog.dismiss();
						if(page>1){
							ll_tips.setVisibility(View.GONE);

						}else{
							ll_tips.setVisibility(View.VISIBLE);
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
	/**
	 * 删除购物车商品
	 */
	/*private void deleteShopCart(){
		boolean isChecked=false;
		for (ShopCarList l : list)
		{
			if (l.isChoosed()){
				isChecked=true;
				break;
			}

		}
		if(!isChecked){
			Tools.toast(ShopCarActivity.this, "请选择需要删除的商品！");
			return ;

		}

		String ids="";
		for (int i = 0; i < list.size(); i++){
			if (list.get(i).isChoosed()){
				ids+=list.get(i).getShopCarId()+",";
			}
		}
		ids=ids.substring(0, ids.length()-1);
		loadingDialog2=new LoadingDialog(this, "正在删除商品...");
		loadingDialog2.show();

		RequestParams params=new RequestParams();
		params.put("shopcarid", ids);
		HttpUtil.post(Consts.WORKER+Consts.DELETESHOPCAR, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog2.dismiss();
						Tools.toast(ShopCarActivity.this, "商品已删除！");
						h.sendEmptyMessage(1);
						break;
					default:
						loadingDialog2.dismiss();
						//Tools.toast(MyCommentActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}*/
	/**
	 * 更改购物车商品数量
	 */
	/*private void changeShopCart(int type,int shopCarId){

		loadingDialog3=new LoadingDialog(this, "正在修改商品数量...");
		loadingDialog3.show();

		RequestParams params=new RequestParams();
		params.put("type", type);
		params.put("shopcarid", shopCarId);
		HttpUtil.post(Consts.WORKER+Consts.GETUPDATESHOPNUM, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog3.dismiss();
						break;
					default:
						loadingDialog3.dismiss();
						//Tools.toast(MyCommentActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}*/

	public void onClick(View v) {
		String productId="";
		String img="";
		String name="";
		String count="";
		String price="";
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.cb_select:
			doCheckAll();
			break;
		case R.id.btn_go_shop:
			Tools.StartActivitytoOther(this, DGStoreActivity.class);
			finish();
			break;
		case R.id.tv_right:
			if(tv_right.getText().toString().equals("编辑")){
				btn_delete.setVisibility(View.VISIBLE);
				btn_order.setVisibility(View.GONE);
				tv_total.setVisibility(View.INVISIBLE);
				tv_right.setText("完成");
            }else if(tv_right.getText().toString().equals("完成")){
				btn_delete.setVisibility(View.GONE);
				btn_order.setVisibility(View.VISIBLE);
				tv_total.setVisibility(View.VISIBLE);
				tv_right.setText("编辑");
			}
			break;
		case R.id.btn_order:

			if(totalCount==0||totalPrice==0 ){
				Tools.toast(this, "请选择需要结算的商品");
				return;
			}

			Intent intent=new Intent(this,CreateOrderActivity.class);
			intent.putExtra("falg", 2);
			for (int i = 0; i < list.size(); i++){
				if (list.get(i).isChoosed()){
					productId+=list.get(i).getProductId()+",";
					img+=list.get(i).getImg()+",";
					name+=list.get(i).getName()+",";
					count+=list.get(i).getCount()+",";
					price+=list.get(i).getPrice()+",";
				}
			}

			intent.putExtra("productId", productId);
			intent.putExtra("img", img);
			intent.putExtra("name", name);
			intent.putExtra("count", count);
			intent.putExtra("price", price);
			startActivity(intent);
			break;
		case R.id.btn_delete:
			//deleteShopCart();
			break;
		}

	}


	/**
	 * 统计操作<br>
	 * 1.先清空全局计数器<br>
	 * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
	 * 3.给底部的textView进行数据填充
	 */
	private void calculate(){
		totalCount = 0;
		totalPrice = 0.00;
		for (int i = 0; i < list.size(); i++){
			if (list.get(i).isChoosed())
			{
				totalCount++;
				totalPrice += list.get(i).getPrice() * list.get(i).getCount();
			}
		}
		tv_total.setText("合计：￥" + totalPrice);
		btn_order.setText("去结算(" + totalCount + ")");
	}



	@Override
	public void doIncrease(int position, View showCountView, boolean isChecked) {
		int currentCount =list.get(position).getCount();
        currentCount++;
		//product.setCount(currentCount);
		list.get(position).setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");

		//changeShopCart(1,list.get(position).getShopCarId());
		adapter.notifyDataSetChanged();
		calculate();

	}

	@Override
	public void doDecrease(int position, View showCountView, boolean isChecked) {
		int currentCount =list.get(position).getCount();
		if (currentCount <= 1){
			return;
		}
		currentCount--;

		list.get(position).setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");
		//changeShopCart(2,list.get(position).getShopCarId());
		adapter.notifyDataSetChanged();
		calculate();

	}

	@Override
	public void checkChild(int position, boolean isChecked) {

		if (isAllCheck())
			cb_select.setChecked(true);
		else
			cb_select.setChecked(false);


		adapter.notifyDataSetChanged();
		calculate();
	}

	private boolean isAllCheck()
	{
		for (ShopCarList l : list)
		{
			if (!l.isChoosed())
				return false;
		}

		return true;
	}


	/** 全选与反选 */
	private void doCheckAll(){
		for (int i = 0; i < list.size(); i++)
		{
			list.get(i).setChoosed(cb_select.isChecked());
		}
		adapter.notifyDataSetChanged();
		calculate();
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
				//getShopCart(page);
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
				//getShopCart(page);
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 500);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);

	}

	private void registerMyReceiver(){
		receiver=new MyReceiver();
		IntentFilter filter=new IntentFilter(Consts.CREATE_ORDER);
		registerReceiver(receiver, filter);
	}

	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {

			//getShopCart(page);
			tv_total.setText("合计：￥" + 0.0);
			btn_order.setText("去结算(" + 0 + ")");
			cb_select.setChecked(false);
		}

	}




}
