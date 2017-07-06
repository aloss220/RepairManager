package com.jumeng.repairmanager.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.CancelReasonAdapter;
import com.jumeng.repairmanager.adapter.PhotoListAdapter;
import com.jumeng.repairmanager.adapter.SortAdapter;
import com.jumeng.repairmanager.bean.CancelReason;
import com.jumeng.repairmanager.bean.DistrictList;
import com.jumeng.repairmanager.bean.PhotoList;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

public class OrderDetailActivity extends BaseActivity {
	private String reason;
	private TextView tv_order_status;
	private Button btn_cancel;
	private TextView tv_patient;
	private TextView tv_relative;
	private TextView tv_type;
	private TextView tv_create_time;
	private TextView tv_order_no;
	private TextView tv_start_time;
	private TextView tv_end_time;
	private TextView tv_price;
	private TextView tv_address;
	private TextView tv_phone;
	private TextView tv_option;
	private int orderStatus;//订单状态
	private int orderId;//订单编号
	private LinearLayout ll_cancel;
	private ImageView iv_icon;
	private TextView tv_name;
	private TextView tv_age;
	private TextView tv_city;
	private TextView tv_exprience;
	private TextView tv_contact;
	private TextView tv_my_phone;
	private LinearLayout ll_nurse;
	private TextView tv_tips;
	private String icon;
	private SharedPreferences sharedPreferences;
	private int userId;
	private int nurseId;
	private List<PhotoList> list=new ArrayList<>();
	private ListView lv_cancel_reason;

	private LoadingDialog loadingDialog;
	private int type=1;//0 用户 1工人

	private MyReceiver receiver;
	//	private EditText et_reason;
	private int serviceType;
	private TextView tv_repair_item;
	private TextView tv_money;
	private GridView gridView;
	private PhotoListAdapter adapter;
	private TextView tv_service_time;
	private TextView tv_desc;
	private int status;
	private Button btn_left;
	private Button btn_right;
	private List<CancelReason> reasons=new ArrayList<>();
	private double totalFee=0;
	private SharedPreferences sp;
	private String contacts;
	private LinearLayout ll_fee;
	private LinearLayout ll_reason;
	private TextView tv_reason;
	private String phone;
	private String serviceFee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		MyApplication.getInstance().addActivities(this);
		orderId=getIntent().getIntExtra("orderId", -1);
		status=getIntent().getIntExtra("status", -1);
		sp=MyApplication.getSharedPref();

		if(sharedPreferences == null){
			sharedPreferences = getSharedPreferences(Consts.USER_FILE_NAME, MODE_PRIVATE);
		}
		userId=sharedPreferences.getInt(Consts.USER_ID, -1);
		initTitleBar();
		setViews();
		getOrdeDetail();
		registerMyReceiver();
		GlobleController.getInstance().addMyListener(this);

	}

	private void initTitleBar() {
		initActionBar(OrderDetailActivity.this);
		setViewShow(0, 1, 1, 0, 1);
		setViewContent(null, R.mipmap.left_arrow, "订单详情", null, R.mipmap.bohao);
	}


	private void setViews() {
		gridView=(GridView)findViewById(R.id.gridView);
		iv_icon=(ImageView)findViewById(R.id.iv_icon);
		tv_order_status=(TextView)findViewById(R.id.tv_order_status);
		tv_repair_item=(TextView)findViewById(R.id.tv_repair_item);
		tv_create_time=(TextView)findViewById(R.id.tv_create_time);
		tv_order_no=(TextView)findViewById(R.id.tv_order_no);
		tv_service_time=(TextView)findViewById(R.id.tv_service_time);
		tv_reason=(TextView)findViewById(R.id.tv_reason);
		tv_money=(TextView)findViewById(R.id.tv_money);
		tv_contact=(TextView)findViewById(R.id.tv_contact);
		tv_phone=(TextView)findViewById(R.id.tv_phone);
		tv_desc=(TextView)findViewById(R.id.tv_desc);
		tv_address=(TextView)findViewById(R.id.tv_address);
		tv_phone=(TextView)findViewById(R.id.tv_phone);
		ll_fee=(LinearLayout)findViewById(R.id.ll_fee);
		ll_reason=(LinearLayout)findViewById(R.id.ll_reason);
		btn_left=(Button)findViewById(R.id.btn_left);
		btn_right=(Button)findViewById(R.id.btn_right);

	}

	private void setStatus(int status) {
		switch (status) {
			case 2:
				btn_left.setVisibility(View.VISIBLE);
				btn_left.setText("取消订单");
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setText("到 达");
				tv_order_status.setText("已接单");
				tv_order_status.setTextColor(Color.parseColor("#008EFF"));
				ll_fee.setVisibility(View.GONE);
				ll_reason.setVisibility(View.GONE );
				break;
			case 3:
				btn_left.setVisibility(View.VISIBLE);
				btn_left.setText("取消订单");
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setText("填写报价单");
				ll_fee.setVisibility(View.GONE);
				tv_order_status.setText("已到达");
				tv_order_status.setTextColor(Color.parseColor("#008EFF"));
				ll_reason.setVisibility(View.GONE );
				break;
			case 4:
				btn_left.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
				tv_order_status.setText("待支付");
				tv_order_status.setTextColor(Color.parseColor("#2EBC73"));
				ll_fee.setVisibility(View.VISIBLE);
				ll_reason.setVisibility(View.GONE );
				break;
			case 5:
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setText("结束服务");
				btn_left.setVisibility(View.GONE);
				tv_order_status.setText("服务中");
				tv_order_status.setTextColor(Color.parseColor("#FF6D00"));
				ll_fee.setVisibility(View.VISIBLE);
				ll_reason.setVisibility(View.GONE );
				break;
			case 6:
				btn_left.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
				tv_order_status.setText("待评价");
				tv_order_status.setTextColor(Color.parseColor("#FF6D00"));
				ll_fee.setVisibility(View.VISIBLE);
				ll_reason.setVisibility(View.GONE );
				break;
			case 7:
				btn_left.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
				tv_order_status.setText("已完成");
				tv_order_status.setTextColor(Color.parseColor("#FF6D00"));
				ll_fee.setVisibility(View.VISIBLE);
				ll_reason.setVisibility(View.GONE );
				break;
			case 8:
				btn_left.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
				tv_order_status.setText("已取消");
				tv_order_status.setTextColor(Color.parseColor("#808080"));
				ll_fee.setVisibility(View.GONE );
				ll_reason.setVisibility(View.VISIBLE );

				break;

		}


	}




	/**
	 * 获取订单详情
	 */
	public void getOrdeDetail(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETORDERXIANGXI);
		params.put("orderid", orderId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							JSONObject obj=new JSONObject(response.getString("data"));
							JSONArray ary=new JSONArray(obj.getString("images"));
							if(ary.length()>0){
								String [] imgs=new String[ary.length()];
								for(int i=0;i<ary.length();i++){
									imgs[i]=(String)ary.get(i);
								}
								adapter=new PhotoListAdapter(OrderDetailActivity.this, imgs);
								gridView.setAdapter(adapter);
							}

							int customerId=obj.getInt("userid");
							getUserInfo(customerId);
							String order_no=obj.getString("order_no");
							contacts=obj.getString("contacts");
							String potion=obj.getString("potion");
							String create_time=obj.getString("create_time");
							String statustime=obj.getString("statustime");
							String adress=obj.getString("adress");
							String hejimoney=obj.getString("hejimoney");
							 serviceFee=obj.getString("myprice");
							String desc=obj.getString("Remarks");
							String reason=obj.getString("reason");
							status=obj.getInt("status");
							setStatus(status);
							tv_repair_item.setText("【"+potion+"】");
							tv_create_time.setText(create_time);
							tv_service_time.setText(statustime);
							tv_address.setText(adress);
							tv_order_no.setText("订单号:"+order_no);
							tv_money.setText("￥"+hejimoney);
							tv_desc.setText(desc);
							//tv_phone.setText(contacts);
							tv_reason.setText(reason);
							break;
						default:
							Tools.toast(OrderDetailActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


	/**
	 * 获取用户信息
	 */
	public void getUserInfo(int customerId){
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETUSER);
		params.put("user_id", customerId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							//Tools.toast(OrderDetailActivity.this, "获取成功！");
							JSONObject obj=new JSONObject(response.getString("data"));
							String icon=obj.getString("avatar");
							String nickname=obj.getString("user_nicename");
							phone = obj.getString("user_phone");
							ImageLoader.getInstance().displayImage(icon, iv_icon,ImageLoaderOptionUtil.getImageDisplayOption("moren_fang"));
							tv_phone.setText(phone);
							tv_contact.setText(nickname);

							break;
						default:
							Tools.toast(OrderDetailActivity.this, response.getString("state_ msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	/**
	 * 工人到达
	 */
	public void workerArrive(){
		final LoadingDialog dialog=new LoadingDialog(this, "请稍等...");
		dialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.WORKERGOTO);
		params.put("orderid", orderId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(OrderDetailActivity.this, "已到达！");
							GlobleController.getInstance().notifyArrive();
							dialog.dismiss();
							break;
						default:
							dialog.dismiss();
							Tools.toast(OrderDetailActivity.this, response.getString("msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}
	/**
	 * 开始服务    (已废弃)
	 */
	public void startServices(){
		final LoadingDialog dialog=new LoadingDialog(this, "请稍等...");
		dialog.show();

		RequestParams params=new RequestParams();
		params.put("code",Consts.WORKERFUWU);
		params.put("orderid", orderId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							dialog.dismiss();
							Tools.toast(OrderDetailActivity.this, "服务开始！");
							GlobleController.getInstance().notifytartService();
							break;
						default:
							dialog.dismiss();
							Tools.toast(OrderDetailActivity.this, response.getString("msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}
	/**
	 * 结束服务
	 */
	public void endServices(){
		final LoadingDialog dialog=new LoadingDialog(this, "请稍等...");
		dialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.WORKERFUWUEND);
		params.put("orderid", orderId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							dialog.dismiss();
							Tools.toast(OrderDetailActivity.this, "服务结束！");
							GlobleController.getInstance().notifyEndService();

							break;
						default:
							dialog.dismiss();
							Tools.toast(OrderDetailActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	protected void cancelDialog() {
		View view=View.inflate(this, R.layout.dialog_cancel_order_reason, null);
		lv_cancel_reason = (ListView)view.findViewById(R.id.lv_cancel_reason);
		Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
		Button btn_done = (Button)view.findViewById(R.id.btn_done);
		final Dialog  dialog=new Dialog(this, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);

		/*ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, android.R.id.text1,items);
		lv_cancel_reason.setAdapter(adapter);
		lv_cancel_reason.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
				text = items.get(pos).toString();
				Log.i("TAG", text);
			}
		});*/
		cancelReason();
		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.btn_cancel:
						dialog.dismiss();
						break;
					case R.id.btn_done:
						cancelOrder(dialog,reason);
						break;
				}
			}
		};
		btn_cancel.setOnClickListener(listener);
		btn_done.setOnClickListener(listener);

	}


	protected void isCancelDialog() {
		View view=View.inflate(this,R.layout.dialog_is_cancel_layout, null);
		Button btn_submit=(Button)view.findViewById(R.id.btn_submit);
		Button btn_cancel=(Button)view.findViewById(R.id.btn_cancel);
		final AlertDialog alertDialog=new AlertDialog.Builder(this).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(view);
		WindowManager wm=getWindowManager();
		DisplayMetrics dm=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams params=window.getAttributes();
		params.width=(int)(dm.widthPixels*0.9);
		//params.height=(int)(dm.heightPixels*0.4);
		window.setAttributes(params);
		alertDialog.setCanceledOnTouchOutside(false);
		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()){
					case R.id.btn_submit:
						cancelDialog();
						alertDialog.dismiss();
						break;
					case R.id.btn_cancel:
						alertDialog.dismiss();
						break;

				}
			}
		};
		btn_submit.setOnClickListener(listener);
		btn_cancel.setOnClickListener(listener);

	}

	/**
	 * 取消订单理由
	 */
	public void cancelReason(){
		reasons.clear();
		RequestParams params=new RequestParams();
		params.put("code",Consts.REASON);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							JSONArray ary=response.getJSONArray("data");
							List<CancelReason> list=JsonParser.parseCancelReason(ary);
							reasons.addAll(JsonParser.parseCancelReason(ary));
							CancelReasonAdapter adapter=new CancelReasonAdapter(OrderDetailActivity.this, reasons);
							lv_cancel_reason.setAdapter(adapter);
							break;
						default:
							Tools.toast(OrderDetailActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


	/**
	 * 取消订单
	 */
	public void cancelOrder(final Dialog dialog,String reason){
		if(reason==null||reason.isEmpty()){
			Tools.toast(this, "请选择取消原因！");
			return;
		}
		RequestParams params=new RequestParams();
		params.put("code",Consts.CANCELHOUORDER);
		params.put("orderid", orderId);
		params.put("why", reason);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							dialog.dismiss();
							Tools.toast(OrderDetailActivity.this, "订单已取消！");
							GlobleController.getInstance().notifycancelOrder();
							OrderDetailActivity.this.finish();
							//Tools.StartActivitytoOther(OrderDetailActivity.this, OrderActivity.class);
							break;
						default:
							dialog.dismiss();
							Tools.toast(OrderDetailActivity.this, response.getString("msg"));
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
			case R.id.btn_left:
				isCancelDialog();
				break;
			case R.id.iv_right:
				Tools.Dial(this, phone);
				break;
			case R.id.btn_right:
				switch (status) {
					case 2:
						tipDialog("确认到达后，将禁止通过电话和用户沟通");
						//workerArrive();
						break;
					case 3:
						Intent intent=new Intent(OrderDetailActivity.this, ServiceFeeActivity.class);
						intent.putExtra("orderId", orderId);
						intent.putExtra("serviceFee", serviceFee);
						startActivity(intent);
						break;
					/*case 4:
						startServices();
						break;*/
					case 5:
						endServices();
						break;
				}
				break;
		}
	}

	private void tipDialog(String tips){
		View view=View.inflate(this,R.layout.tips_dialog2, null);
		final TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
		final TextView tv_tips=(TextView)view.findViewById(R.id.tv_tips);
		final Button btn_sure=(Button) view.findViewById(R.id.btn_sure);
		final Button btn_cancel=(Button) view.findViewById(R.id.btn_cancel);

		tv_title.setText("温馨提示");
		tv_tips.setText(tips);
		final AlertDialog dialog=new AlertDialog.Builder(this).create();
		dialog.show();
		dialog.getWindow().setContentView(view);
		WindowManager wm=getWindowManager();
		Display d=wm.getDefaultDisplay();
		WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
		//params.height=(int)(d.getHeight()*0.4);
		params.width=(int)(d.getWidth()*0.8);
		dialog.getWindow().setAttributes(params);
		dialog.setCanceledOnTouchOutside(true);
		OnClickListener  listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.btn_sure:
						workerArrive();
						dialog.dismiss();
						break;
					case R.id.btn_cancel:
						dialog.dismiss();
						break;
				}

			}
		};
		btn_sure.setOnClickListener(listener);
		btn_cancel.setOnClickListener(listener);
	}

	private void registerMyReceiver() {
		IntentFilter filter=new IntentFilter();
		filter.addAction(Consts.ORDER_STATUS);
		filter.addAction("pay_success");
		filter.addAction("reason");
		receiver=new MyReceiver();
		registerReceiver(receiver, filter);
	}

	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals("reason")){
				reason=intent.getStringExtra("reason");
			}else{
				getOrdeDetail();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void payOrder() {
		getOrdeDetail();

	}

	@Override
	public void cancelOrder() {
		getOrdeDetail();

	}

	@Override
	public void startService() {
		getOrdeDetail();


	}

	@Override
	public void endService() {
		getOrdeDetail();


	}

	@Override
	public void arrive() {
		getOrdeDetail();

	}

	@Override
	public void serviceForm() {
		getOrdeDetail();

	}



}
