package com.jumeng.repairmanager.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.AlertDialog;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

public class ServiceFeeActivity extends BaseActivity implements OnClickListener,TextWatcher{



	private int orderId;
	private EditText et_fee1;
	private TextView tv_fee2;
	private EditText et_fee3;
	private EditText et_fee4;
	private EditText et_fee5;
	private EditText et_time;
	private double  totalFee=0.0;
	private TextView tv_total_fee;
	private SharedPreferences sp;
	private String total;
	private DecimalFormat df;
	private String fee1,fee2,fee3,fee4,fee5;
	private MyReceiver receiver;
	private String goodsId="";
	private String goodsCount="";
	private double totalPrice;
	private String serviceFee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_fee);
		MyApplication.getInstance().addActivities(this);
		sp=MyApplication.getSharedPref();
		orderId=getIntent().getIntExtra("orderId", -1);
		serviceFee=getIntent().getStringExtra("serviceFee");
		initTitleBar();
		setViews();
		registerMyReceiver();

	}


	private void initTitleBar() {
		initActionBar(ServiceFeeActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "填写报价单", null, 0);
	}


	private void setViews() {
		et_fee1=(EditText)findViewById(R.id.et_fee1);
		tv_fee2=(TextView) findViewById(R.id.tv_fee2);
		et_fee3=(EditText)findViewById(R.id.et_fee3);
		et_fee4=(EditText)findViewById(R.id.et_fee4);
		et_fee5=(EditText)findViewById(R.id.et_fee5);
		et_time=(EditText)findViewById(R.id.et_time);
		tv_total_fee=(TextView)findViewById(R.id.tv_total_fee);

		et_fee1.addTextChangedListener(this);
		et_fee3.addTextChangedListener(this);
		et_fee4.addTextChangedListener(this);
		et_fee5.addTextChangedListener(this);

		df= new DecimalFormat("##0.00");
		if(!serviceFee.equals("")&&Double.parseDouble(serviceFee)>0.0){
			et_fee1.setText(serviceFee);
		}else{
			et_fee1.setText("");
		}
		et_fee1.setSelection(et_fee1.length());

	}

	/**
	 * 提交表单
	 */
	public void submitForm(){
		String time=et_time.getText().toString();
		if(time.isEmpty()){
			time="0";
		}
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETBAOJIA);
		params.put("orderid", orderId);
		params.put("myprice", Double.parseDouble(fee1));
		params.put("guide_price", Double.parseDouble(fee2));
		params.put("weixian", Double.parseDouble(fee3));
		params.put("othemoney", Double.parseDouble(fee4));
		params.put("yiwaibaoxian", Double.parseDouble(fee5));
		params.put("hejimoney", totalFee);
		params.put("timelong", Double.parseDouble(time));
		params.put("goods_id", goodsId);
		params.put("goods_num", goodsCount);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(ServiceFeeActivity.this, "提交成功！");
							GlobleController.getInstance().notifyServiceForm();
							finish();

							break;
						default:
							Tools.toast(ServiceFeeActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


	public void onClick(View v){
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.tv_fee2:
				Intent intent=new Intent(this, GoodsListActivity.class);
				intent.putExtra("orderId", orderId);
				startActivity(intent);
				break;
			case R.id.btn_sbumit:
				veryfiInput();
				if(totalFee==0||totalFee==0.0){
					Tools.toast(this, "总金额不能为0");
					return;
				}
				makeSure();
				break;

		}
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	private void veryfiInput(){
		fee1=et_fee1.getText().toString();
		fee2=tv_fee2.getText().toString();
		fee3=et_fee3.getText().toString();
		fee4=et_fee4.getText().toString();
		fee5=et_fee5.getText().toString();

		if(fee1.isEmpty()){
			fee1="0";
		}
		if(fee2.isEmpty()){
			fee2="0";
		}
		if(fee3.isEmpty()){
			fee3="0";
		}
		if(fee4.isEmpty()){
			fee4="0";
		}
		if(fee5.isEmpty()){
			fee5="0";
		}

		totalFee=Double.valueOf(fee1)+totalPrice+Double.valueOf(fee3)+Double.valueOf(fee4)+Double.valueOf(fee5);
		tv_total_fee.setText("￥"+totalFee+"");
	}


	@Override
	public void afterTextChanged(Editable s) {
		veryfiInput();
	}


	private void makeSure(){
		new AlertDialog(this).builder().setTitle("是否确认提交?")
				.setPositiveButton("马上提交",new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						submitForm();


					}

				}).setNegativeButton("重新填写", new OnClickListener() {

			@Override
			public void onClick(View arg0) {


			}
		}).show();



	}



	private void registerMyReceiver() {
		IntentFilter filter=new IntentFilter();
		filter.addAction(Consts.GET_GOODS_INFO);
		receiver=new MyReceiver();
		registerReceiver(receiver, filter);
	}

	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			goodsId=intent.getStringExtra("goodsId");
			goodsCount=intent.getStringExtra("goodsCount");
			totalPrice=intent.getDoubleExtra("totalPrice",-1.0);
			tv_fee2.setText(totalPrice+"");
			veryfiInput();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}



}
