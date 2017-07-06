package com.jumeng.repairmanager.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.hxchat.ChatListActivity;
import com.jumeng.repairmanager.service.LocationService;
import com.jumeng.repairmanager.update.UpdateManager;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.NetworkUtil;
import com.jumeng.repairmanager.util.Sign;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.AlertDialog;
import com.jumeng.repairmanager.view.CircleImageView;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jumeng.repairmanager.MyApplication.getInstance;
import static com.jumeng.repairmanager.MyApplication.getSharedPref;
import static com.jumeng.repairmanager.R.id.district;
import static com.jumeng.repairmanager.util.SetActionBar.tv_left;

public class MainActivity extends BaseActivity {

	private RelativeLayout rl_order;
	private RelativeLayout rl_income;
	private RelativeLayout rl_comment;
	private RelativeLayout rl_customer;
	private RelativeLayout rl_store;
	private RelativeLayout rl_shop_car;
	private RelativeLayout rl_message;
	private RelativeLayout rl_right;
	private RelativeLayout rl_snap;
	private RelativeLayout rl_my_Info;
	private RelativeLayout rl_chat;
	private int userId;
	private TextView tv_userName;
	private ImageView chat_new;
	private TextView tv_number;
	private CircleImageView circleImageView;
	private LoadingDialog loadingDialog;
	private TextView tv_times;
	private TextView tv_level;
	private TextView tv_work;
	private SharedPreferences sp;
	private SharedPreferences sharePreferences;
	private String name;
	private MyReceiver receiver;
	private int type=1;
	private boolean isWork=false;
	private TextView tv_tips;
	private Button btn_rob;
	private Button btn_switch;
	private String token="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivities(this);
		NetworkUtil.checkNetworkState(this);
		sp=getSharedPref();
		userId = sp.getInt(Consts.USER_ID, -1);
		isWork=sp.getBoolean("isWork", false);
		startService(new Intent(MainActivity.this, LocationService.class));
		setViews();
		checkLogin();
		getInfo();
		registerReceiver();
	}
	@Override
	protected void onResume() {
		super.onResume();
		EMClient.getInstance().chatManager().addMessageListener(msgListener);
		//getInfo();
	}
	private void setViews() {
		tv_left=(TextView)findViewById(R.id.tv_left);

		btn_rob=(Button)findViewById(R.id.btn_rob);
		btn_switch=(Button)findViewById(R.id.btn_switch);
		tv_tips=(TextView)findViewById(R.id.tv_tips);

		if(sp.getBoolean("isWork",false)){//如果处于上班状态  点击后下班  传1是下班
			setButtonStyle(0);
		}else{//如果处于下班状态  点击后上班   传0是上班
			setButtonStyle(1);
		}

		tv_level=(TextView)findViewById(R.id.tv_level);
		tv_times=(TextView)findViewById(R.id.tv_times);
		tv_work=(TextView)findViewById(R.id.tv_work);
		tv_userName=(TextView)findViewById(R.id.tv_userName);
		tv_number=(TextView)findViewById(R.id.tv_number);
		chat_new = (ImageView) findViewById(R.id.chat_new);
		circleImageView=(CircleImageView)findViewById(R.id.circleImageView);
		rl_order=(RelativeLayout)findViewById(R.id.rl_order);
		rl_income=(RelativeLayout)findViewById(R.id.rl_income);
		rl_comment=(RelativeLayout)findViewById(R.id.rl_comment);
		rl_customer=(RelativeLayout)findViewById(R.id.rl_customer);
		rl_store=(RelativeLayout)findViewById(R.id.rl_store);
		rl_shop_car=(RelativeLayout)findViewById(R.id.rl_shop_car);
		rl_message=(RelativeLayout)findViewById(R.id.rl_message);
		rl_right=(RelativeLayout)findViewById(R.id.rl_right);
		rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
		rl_order.setOnClickListener(this);
		rl_income.setOnClickListener(this);
		rl_comment.setOnClickListener(this);
		rl_customer.setOnClickListener(this);
		rl_store.setOnClickListener(this);
		rl_shop_car.setOnClickListener(this);
		rl_message.setOnClickListener(this);
		rl_right.setOnClickListener(this);
		rl_chat.setOnClickListener(this);

		rl_my_Info = (RelativeLayout) findViewById(R.id.rl_my_Info);
		rl_my_Info.setOnClickListener(this);

	}

	public void onClick(View v){
		switch (v.getId()) {
			case R.id.tv_left:
				break;
			case R.id.iv_right:
				Tools.StartActivitytoOther(this, SettingsActivity.class);
				break;
			case R.id.rl_order:
				Tools.StartActivitytoOther(this, OrderActivity.class);
				break;
			case R.id.rl_income:
				Tools.StartActivitytoOther(this, MyBalanceActivity.class);
				break;
			case R.id.rl_comment:
				Tools.StartActivitytoOther(this, MyCommentActivity.class);
				break;
			case R.id.rl_customer:
				Tools.toast(this, "正在努力赶工中，敬请期待");
				//Tools.StartActivitytoOther(this, MyCustomerActivity.class);
				break;
			case R.id.rl_store:
				Tools.toast(this, "正在努力赶工中，敬请期待");
				//Tools.StartActivitytoOther(this, SelectDistrictActivity.class);
				break;
			case R.id.rl_shop_car:
				Tools.toast(this, "正在努力赶工中，敬请期待");
				//Tools.StartActivitytoOther(this, ShopCarActivity.class);
				break;
			case R.id.rl_message:
				Tools.toast(this, "正在努力赶工中，敬请期待");
				//Tools.StartActivitytoOther(this, MyMessageActivity.class);
				break;
			case R.id.rl_right:
				Tools.toast(this, "正在努力赶工中，敬请期待");
				//Tools.StartActivitytoOther(this, MyRightActivity.class);
				break;
			case R.id.btn_switch:
				if(!sp.getBoolean("isWork",false)){//如果处于上班状态  点击后下班  传1是下班
					isGetOrder(0);
				}else{//如果处于下班状态  点击后上班   传0是上班
					isGetOrder(1);
				}
				break;
			case R.id.btn_rob:
				if(sp.getBoolean("isWork",false)){
					Tools.StartActivitytoOther(this, MySnapActivity.class);
				}else{
					Tools.toast(this, "上班后才能接单!");
				}
				break;
			case R.id.rl_my_Info:
				Tools.StartActivitytoOther(this, MyInfoActivity.class);
				break;
			case R.id.rl_chat:
				Tools.toast(this, "正在努力赶工中，敬请期待");
				/*chat_new.setImageResource(R.mipmap.chat_w);
				Tools.StartActivitytoOther(this, ChatListActivity.class);*/
				break;
		}

	}

	/*
	* 0 上班状态   1下班状态
	* */
	private void setButtonStyle(int n){
		switch (n) {
			case 0:
				//设置上下班按钮样式
				btn_switch.setText("点击下线休息");
				btn_switch.setBackgroundDrawable(getResources().getDrawable(R.drawable.submit_bg));
				//设置抢单按钮样式
				btn_rob.setBackgroundDrawable(getResources().getDrawable(R.drawable.submit_bg3));
				break;
			case 1:
				//设置上下班按钮样式
				btn_switch.setText("点击上线赚钱");
				btn_switch.setBackgroundDrawable(getResources().getDrawable(R.drawable.submit_bg2));
				//设置抢单按钮样式
				btn_rob.setBackgroundDrawable(getResources().getDrawable(R.drawable.submit_bg4));
				break;
		}

	}

	/**
	 * @param n
	 */
	private void setIsOnline(int n){
		Intent intent=null;
		switch (n){
			case 0://上班
				//将iswork置为true
				sp.edit().putBoolean("isWork", true).commit();
				getInstance().setIsOrder(0);
				//发送广播通知上班 高德地图开始上传位置
				intent = new Intent(Consts.START_WORK);
				sendBroadcast(intent);
				break;
			case 1://下班
				//将iswork置为false
				sp.edit().putBoolean("isWork", false).commit();
				getInstance().setIsOrder(1);
				//发送广播通知下班 高德地图清除位置信息
				intent = new Intent(Consts.END_WORK);
				sendBroadcast(intent);

				break;

		}
	}


	private void registerReceiver (){
		receiver=new MyReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Sign.MSG);
		filter.addAction(Consts.START_WORK);
		filter.addAction(Consts.END_WORK);
		filter.addAction(Sign.UPDATE_PHOTO);
		filter.addAction(Sign.UPDATE_STARTWORK);
		filter.addAction(Sign.UPDATE_NAME);
		registerReceiver(receiver, filter);

	}


	public class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Sign.MSG)){
				chat_new.setImageResource(R.mipmap.xiaoxi_new);
			}
			else if(intent.getAction().equals(Consts.START_WORK)){
				setButtonStyle(0);
			}else if(intent.getAction().equals(Consts.END_WORK)){
				setButtonStyle(1);
			}else if(intent.getAction().equals(Sign.UPDATE_PHOTO)){
				String icon=intent.getStringExtra("image_url");
				ImageLoader.getInstance().displayImage(icon, circleImageView,
						ImageLoaderOptionUtil.getImageDisplayOption("moren_yuan"));
			}else if(intent.getAction().equals(Sign.UPDATE_STARTWORK)){
				String worknum=intent.getStringExtra("worktime");
				tv_work.setText(worknum+"年");
			}else if(intent.getAction().equals(Sign.UPDATE_NAME)){
				String name=intent.getStringExtra("name");
				tv_userName.setText(name);
			}
		}

	}

	/**
	 * 获取信息
	 */
	private void getInfo(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETINFO);
		params.put("userid", userId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							JSONObject obj=new JSONObject(response.getString("data"));
							String icon=obj.getString("icon");
							int userId=obj.getInt("id");
							int status = obj.getInt("status");
							int audit=obj.getInt("audit");
							//每次打开软件先判断是否认证通过
							if(audit==0){
								isRenzheng();
							}
							if(status==3){
								Tools.toast(MainActivity.this, "您的账号在不同设备登录过,请重新登录！");
								MyApplication.getSharedPref().edit().clear().commit();
								Tools.StartActivitytoOther(MainActivity.this, LoginActivity.class);
								MyApplication.getInstance().finishActivities();
							}
							String grade=obj.getString("grade");
							String phone=obj.getString("phone_number");
							//double totalIncome=obj.getDouble("Totalrevenue");//累计收入
							String canCash=obj.getString("ktmoney");//可提现余额
							int isOrder = obj.getInt("isorder");

							name=obj.getString("name");

							String nickname=obj.getString("nickname");
							String nativeplace=obj.getString("nativeplace");//籍贯
							String cardFront=obj.getString("Idcardpositive");
							String cardBack=obj.getString("Idcardreverse");
							String certificate=obj.getString("carecard");
							String orderCount=obj.getString("totalorder");
							String worknum=obj.getString("worknum");
							String province=obj.getString("province_name");
							String city=obj.getString("city_name");
							String district=obj.getString("district_name");
							tv_left.setText(district);
							ImageLoader.getInstance().displayImage(icon, circleImageView,
									ImageLoaderOptionUtil.getImageDisplayOption("moren_yuan"));
							tv_userName.setText(name);
							tv_number.setText("工号："+nickname);
							if(String.valueOf(orderCount).isEmpty()){
								tv_times.setText("— —");
							}else{
								tv_times.setText(orderCount+"次");
							}
							if(String.valueOf(worknum).isEmpty()){
								tv_work.setText("— —");
							}else{
								tv_work.setText(worknum+"年");
							}
							if(String.valueOf(grade).isEmpty()){
								tv_level.setText("— —");
							}else{
								tv_level.setText(grade+"级");

							}


							getInstance().setCardFront(cardFront);
							getInstance().setCardBack(cardBack);
							getInstance().setCertificate(certificate);


							getInstance().setOrderCount(Integer.valueOf(orderCount));
							//getInstance().setTotalIncome(totalIncome);
							getInstance().setVerifyStatus(status);
							getInstance().setIsOrder(isOrder);
							break;
						default:
							Tools.toast(MainActivity.this, response.getString("status_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	/**
	 * 是否能够接单
	 */
	public void isGetOrder(final int isorder ){
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETISORDER);
		params.put("isorder", isorder);
		params.put("workerid", userId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 0:
							Tools.toast(MainActivity.this, response.getString("state_msg"));
							break;
						case 1:
							Tools.toast(MainActivity.this, response.getString("state_msg"));
							setIsOnline(0);
							setButtonStyle(0);

							break;
						case 2:
							Tools.toast(MainActivity.this, response.getString("state_msg"));
							setIsOnline(1);
							setButtonStyle(1);
							break;
						case 3:
							Tools.toast(MainActivity.this, response.getString("state_msg"));
							setIsOnline(1);
							setButtonStyle(1);
							break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}



	/**
	 * 退出登录
	 */
	public void Logout(){

		RequestParams params=new RequestParams();
		params.put("code",Consts.LOGGEDOUT);
		params.put("type", 1);
		params.put("userid", userId);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Intent intent=new Intent(Consts.END_WORK);
							sendBroadcast(intent);
							break;
						default:
							Tools.toast(MainActivity.this, response.getString("msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}
	/**
	 * 检查是否异地登录
	 */
	private void checkLogin(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.ANOTHER);
		params.put("workerid", userId);
		params.put("logintype", 0);
		params.put("ioschinaid", "");
		params.put("chinaid", sp.getString(Consts.CLIENT_ID, null));
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 2:
							//发送广播通知高德地图清除位置信息
							Intent intent=new Intent(Consts.END_WORK);
							sendBroadcast(intent);

							Tools.toast(MainActivity.this, "您的账号在不同设备登录过,请重新登录！");
							MyApplication.getSharedPref().edit().clear().commit();
							Tools.StartActivitytoOther(MainActivity.this, LoginActivity.class);
							MyApplication.getInstance().finishActivities();
							EMClient.getInstance().logout(true);



							break;
						default:
							//Tools.toast(MainActivity.this, response.getString("state_ msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


	//用来检测所守护的Service是否还活着
	public static boolean isServiceWorked(Context context, String serviceName) {
		ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}



	private long exitTime=0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			if(System.currentTimeMillis()-exitTime>2000){
				Tools.toast(this, "再按一次退出程序");
				exitTime=System.currentTimeMillis();
			}else{
				MyApplication.getInstance().finishActivities();
			}

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		//startService(new Intent(MainActivity.this, GuardService.class));
		EMClient.getInstance().chatManager().removeMessageListener(msgListener);
	}
	EMMessageListener msgListener = new EMMessageListener() {

		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			for (EMMessage message : messages) {
				String username = null;
				// 群组消息
				if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
					username = message.getTo();
				} else {
					// 单聊消息
					username = message.getFrom();
				}
				//				EaseUI.getInstance().getNotifier().onNewMsg(message);
				Play();
				Intent intent = new Intent(Sign.MSG);
				sendBroadcast(intent);

			}
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages) {
			//收到透传消息
		}

		@Override
		public void onMessageReadAckReceived(List<EMMessage> messages) {
			//收到已读回执
		}

		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> message) {
			//收到已送达回执
		}

		@Override
		public void onMessageChanged(EMMessage message, Object change) {
			//消息状态变动
		}
	};



	private void Play(){
		//通过create方法获取mediaPlayer实例 的话在start()之前 不用调prepare()；
		MediaPlayer player=MediaPlayer.create(this,R.raw.message_voice);
		player.start();

	}

	@Override
	public void pass() {
		getInfo();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		//startService(new Intent(MainActivity.this, GuardService.class));
	}


	private void isRenzheng(){
		new AlertDialog(this).builder().setTitle("认证通过后才可以接单哦！您还没有认证，赶紧先去提交认证信息吧!")
				.setPositiveButton("前往认证",new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Tools.StartActivitytoOther(MainActivity.this,CertificationInfoActivity.class);

					}

				}).setNegativeButton("暂不认证", new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {


			}
		}).show();



	}



	/**
	 * 程序是否在前台运行 
	 *
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the  
		// device  

		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.  
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
}
