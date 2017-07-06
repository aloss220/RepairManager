package com.jumeng.repairmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.GoodAdapter;
import com.jumeng.repairmanager.bean.GoodsList;
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

public class GoodsListActivity extends BaseActivity implements GoodAdapter.CheckInterface,GoodAdapter.ModifyCountInterface {
	private static final String TAG = GoodsListActivity.class.getSimpleName();
	private ListView listView;
	private GoodAdapter adapter;
	private List<GoodsList> list=new ArrayList<>();
	private LoadingDialog loadingDialog;
	private int totalCount;
	private double totalPrice;
	private LoadingDialog loadingDialog2;
	private int orderId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list);
		getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		MyApplication.getInstance().addActivities(this);
		orderId=getIntent().getIntExtra("orderId",-1);
		setViews();
		getGoodsList();
	}
	private void setViews( ) {
		listView=(ListView)findViewById(R.id.listView);
		adapter=new GoodAdapter(this, list);
		adapter.setCheckInterface(this);
		adapter.setModifyCountInterface(this);
		listView.setAdapter(adapter);
	}

	/**
	 * 获取配件列表
	 */
	public void getGoodsList(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.GOODS);
		params.put("orderid", orderId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							String data=response.getString("data");
							JSONArray ary=new JSONArray(data);
							List<GoodsList> orderList= JsonParser.parseGoodsList(ary);
							list.addAll(orderList);
							break;
						default:
							Tools.toast(GoodsListActivity.this, response.getString("state_msg"));
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
		String goodsId="";
		String goodsCount="";
		String price="";
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_done:
			for (int i = 0; i < list.size(); i++){
				if (list.get(i).isChoosed()){
					goodsId+=list.get(i).getId()+",";
					goodsCount+=list.get(i).getCount()+",";
					price+=list.get(i).getPrice()+",";
				}
			}
			//发送广播船值给servicefeeactivity页面
			if(!goodsId.isEmpty()&&!goodsCount.isEmpty()){
				Intent intent=new Intent(Consts.GET_GOODS_INFO);
				intent.putExtra("goodsId",goodsId.substring(0,goodsId.length()-1));
				intent.putExtra("goodsCount",goodsCount.substring(0,goodsCount.length()-1));
				intent.putExtra("totalPrice",totalPrice);
				sendBroadcast(intent);
			}
			finish();
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
	}

	@Override
	public void doIncrease(int position, View showCountView, boolean isChecked) {
		if(!list.get(position).isChoosed()){
			list.get(position).setChoosed(true);
		}
		int currentCount =list.get(position).getCount();
        currentCount++;
		list.get(position).setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");

		adapter.notifyDataSetChanged();
		calculate();

	}

	@Override
	public void doDecrease(int position, View showCountView, boolean isChecked) {
		int currentCount =list.get(position).getCount();
		if (currentCount <= 0){
			return;
		}
		if (currentCount <= 1){
			list.get(position).setChoosed(false);
		}

		currentCount--;

		list.get(position).setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");
		adapter.notifyDataSetChanged();
		calculate();

	}

	@Override
	public void checkChild(int position, boolean isChecked) {

		adapter.notifyDataSetChanged();
		calculate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}





}
