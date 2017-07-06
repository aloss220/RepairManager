package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.MyApplication.getSharedPref;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.StoreOrderInAdapter2;
import com.jumeng.repairmanager.bean.StoreOrderIn;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateOrderActivity  extends BaseActivity  {
	private static final String TAG = CreateOrderActivity.class.getSimpleName();
	private int page = 1;
	private ListView listView;
	private ArrayList<StoreOrderIn> list=new ArrayList<>();
	private StoreOrderInAdapter2 adapter;
	private Button btn_order;
	private TextView tv_total;
	private EditText et_address;
	private LoadingDialog loadingDialog;
	private SharedPreferences sp;
	private int userId;
	private int flag;
	private String productIds;
	private String imgs;
	private String names;
	private String counts;
	private String prices;
	private TextView tv_total_money;
	private double total;
	private Integer totalCount;
	private TextView tv_count;
	private TextView tv_name;
	private TextView tv_phone;
	private LoadingDialog loadingDialog2;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_order);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);
		flag=getIntent().getIntExtra("falg", -1);
		initTitleBar() ;
		setViews();
		initListview( );
		//getInfo();
	}

	private void initTitleBar() {
		initActionBar(CreateOrderActivity.this);

		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "填写订单", null, 0);

	}


	private void setViews() {

		switch (flag) {
		case 1:
			productIds=getIntent().getStringExtra("productId");
			imgs=getIntent().getStringExtra("img");
			names=getIntent().getStringExtra("name");
			counts=getIntent().getStringExtra("count");
			prices=getIntent().getStringExtra("price");


			break;
		case 2:
			productIds=getIntent().getStringExtra("productId");
			imgs=getIntent().getStringExtra("img");
			names=getIntent().getStringExtra("name");
			counts=getIntent().getStringExtra("count");
			prices=getIntent().getStringExtra("price");

			productIds=productIds.substring(0, productIds.length()-1);
			imgs=imgs.substring(0, imgs.length()-1);
			names=names.substring(0, names.length()-1);
			counts=counts.substring(0, counts.length()-1);
			prices=prices.substring(0, prices.length()-1);
			break;
		}

		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_phone=(TextView)findViewById(R.id.tv_phone);
		btn_order=(Button)findViewById(R.id.btn_order);
		tv_total_money=(TextView)findViewById(R.id.tv_total_money);
		tv_total=(TextView)findViewById(R.id.tv_total);
		tv_count=(TextView)findViewById(R.id.tv_count);
		et_address=(EditText)findViewById(R.id.et_address);

		for(int i=0;i<counts.split(",").length;i++){
			total += Integer.valueOf(counts.split(",")[i])*Double.valueOf(prices.split(",")[i]);
		}
		tv_total_money.setText("合计：￥"+total);
		tv_total.setText("实付款：￥"+total);


	}

	private void initListview( ) {
		listView=(ListView)findViewById(R.id.listView);
		adapter=new StoreOrderInAdapter2(this, productIds.split(","),imgs.split(","),names.split(","),counts.split(","),prices.split(","));
		listView.setAdapter(adapter);
	}

	/**
	 * 获取信息
	 */
	/*private void getInfo(){
		RequestParams params=new RequestParams();
		params.put("worker", userId);
		HttpUtil.post(Consts.PUBLIC+Consts.GETWORKER, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						//Tools.toast(MyInfoActivity.this, "获取个人信息成功！");
						String phone=response.getString("phone_number");
						String name = response.getString("name");
						tv_name.setText(name);
						tv_phone.setText(phone);

						break;
					default:
						Tools.toast(CreateOrderActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}*/

	

	/**
	 * 下单
	 */
	/*private void Order(){
		String address = et_address.getText().toString();
		if(address.isEmpty()){
			Tools.toast(this, "收货地址不能为空!");
			return;
		}
		
		loadingDialog2=new LoadingDialog(this, "提交中...");
		loadingDialog2.show();
		RequestParams params=new RequestParams();
		params.put("workerid", userId);
		params.put("adress", et_address.getText().toString());
		params.put("productid", productIds);
		params.put("price", prices);
		params.put("shopnum", counts);
		params.put("money", total);
		HttpUtil.post(Consts.WORKER+Consts.GETPLACEORDER, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog2.dismiss();
						Tools.toast(CreateOrderActivity.this, "下单成功!");
						Intent intent=new Intent(CreateOrderActivity.this,OrderSuccessActivity.class);
						intent.putExtra("orderNo", response.getString("ordernum"));
						startActivity(intent);
						
						Intent i=new Intent(Consts.CREATE_ORDER);
						sendBroadcast(i);

						finish();
						break;
					default:
						loadingDialog2.dismiss();
						Tools.toast(CreateOrderActivity.this, response.getString("msg"));
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
		case R.id.btn_submit:
			//Order();
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
