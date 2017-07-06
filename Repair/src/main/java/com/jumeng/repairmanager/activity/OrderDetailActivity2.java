package com.jumeng.repairmanager.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.PhotoListAdapter;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

public class OrderDetailActivity2 extends BaseActivity{
	private TextView tv_create_time;
	private TextView tv_order_no;
	private TextView tv_address;
	private TextView tv_phone;
	private int orderId;//订单编号
	private TextView tv_contact;
	private SharedPreferences sharedPreferences;

	private int type=1;//0 用户 1工人

	private String [] imgs={"http://img4.imgtn.bdimg.com/it/u=4236942158,2307642402&fm=21&gp=0.jpg","http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg","http://pic41.nipic.com/20140509/4746986_145156378323_2.jpg"};
	private TextView tv_repair_item;
	private GridView gridView;
	private PhotoListAdapter adapter;
	private TextView tv_service_time;
	private TextView tv_desc;
	private Button btn_left;
	private ImageView iv_icon;
	private int userId;
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail2);
		MyApplication.getInstance().addActivities(this);
		orderId=getIntent().getIntExtra("orderId", -1);
		initTitleBar();
		setViews();
		if(sharedPreferences == null){
			sharedPreferences = getSharedPreferences(Consts.USER_FILE_NAME, MODE_PRIVATE);
		}
		userId=sharedPreferences.getInt(Consts.USER_ID, -1);
		getOrdeDetail();

	}

	private void initTitleBar() {
		initActionBar(OrderDetailActivity2.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "订单详情", null, 0);
	}


	private void setViews() {
		gridView=(GridView)findViewById(R.id.gridView);
		iv_icon=(ImageView)findViewById(R.id.iv_icon);
		tv_repair_item=(TextView)findViewById(R.id.tv_repair_item);
		tv_create_time=(TextView)findViewById(R.id.tv_create_time);
		tv_order_no=(TextView)findViewById(R.id.tv_order_no);
		tv_service_time=(TextView)findViewById(R.id.tv_service_time);
		tv_contact=(TextView)findViewById(R.id.tv_contact);
		tv_phone=(TextView)findViewById(R.id.tv_phone);
		tv_desc=(TextView)findViewById(R.id.tv_desc);
		tv_address=(TextView)findViewById(R.id.tv_address);
		btn_left=(Button)findViewById(R.id.btn_left);

	}




	/**
	 * 获取订单详情
	 */
	public void getOrdeDetail(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETORDERXIANGXI);
		params.put("orderid", orderId);
		HttpUtil.post( params, new MyJsonHttpResponseHandler(this){
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
								adapter=new PhotoListAdapter(OrderDetailActivity2.this, imgs);
								gridView.setAdapter(adapter);
							}
							int customerId=obj.getInt("userid");
							getUserInfo(customerId);
							String order_no=obj.getString("order_no");
							String potion=obj.getString("potion");
							String create_time=obj.getString("create_time");
							String statustime=obj.getString("statustime");
							String adress=obj.getString("adress");
							String hejimoney=obj.getString("hejimoney");
							String desc=obj.getString("Remarks");
							String reason=obj.getString("reason");
							int status = obj.getInt("status");

							tv_repair_item.setText("【"+potion+"】");
							tv_create_time.setText(create_time);
							tv_service_time.setText(statustime);
							tv_address.setText(adress);
							tv_order_no.setText("订单号:"+order_no);
							tv_desc.setText(desc);
							break;
						default:
							Tools.toast(OrderDetailActivity2.this, response.getString("state_msg"));
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
							JSONObject obj=new JSONObject(response.getString("data"));
							String icon=obj.getString("avatar");
							String nickname=obj.getString("user_nicename");
							phone = obj.getString("user_phone");
							ImageLoader.getInstance().displayImage(icon, iv_icon,ImageLoaderOptionUtil.getImageDisplayOption("moren_fang"));
							tv_phone.setText(phone);
							tv_contact.setText(nickname);

							break;
						default:
							Tools.toast(OrderDetailActivity2.this, response.getString("state_ msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	/**
	 * 判断工人是否可以接收新订单
	 *
	 * @throws JSONException
	 */
	private void isCanReceiveOrder(){
		RequestParams params = new RequestParams();
		params.put("code",Consts.GETISJIEDAN);
		params.put("workerid", MyApplication.getSharedPref().getInt(Consts.USER_ID, -1));
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this) {
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							tipDialog("还有三个单子没做完呢，做完再来抢新单吧！");
							break;
						case 2:
							robOrder();
							break;
					}

				} catch (JSONException e) {
					// e.printStackTrace();
				}

			}
		});
	}

	private void tipDialog(String tips){
		View view=View.inflate(this,R.layout.tips_dialog, null);
		final TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
		final TextView tv_tips=(TextView)view.findViewById(R.id.tv_tips);
		tv_title.setText("温馨提示");
		tv_tips.setText(tips);
		final Button btn_sure=(Button) view.findViewById(R.id.btn_ok);
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
					case R.id.btn_ok:
						dialog.dismiss();
						break;
				}

			}
		};
		btn_sure.setOnClickListener(listener);
	}


	/**
	 * 抢单
	 */
	public void robOrder(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.WORKERJIEDAN);
		params.put("orderid", orderId);
		params.put("workerid", MyApplication.getSharedPref().getInt(Consts.USER_ID, -1));
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(OrderDetailActivity2.this, "抢单成功!");

							GlobleController.getInstance().notifyRobOrder();
							Intent intent=new Intent(OrderDetailActivity2.this,OrderDetailActivity.class);
							intent.putExtra("orderId", orderId);
							startActivity(intent);
							finish();
							break;
						default:
							Tools.toast(OrderDetailActivity2.this, response.getString("state_msg"));
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
				//isCanReceiveOrder();
				robOrder();
				break;
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}




}
