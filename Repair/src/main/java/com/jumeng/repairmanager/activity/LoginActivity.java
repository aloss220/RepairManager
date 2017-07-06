package com.jumeng.repairmanager.activity;

import static android.R.attr.data;
import static com.jumeng.repairmanager.util.Consts.GETLOGIN;
import static com.jumeng.repairmanager.util.SetActionBar.border;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.rl_titlebar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_center;
import static com.loc.f.c;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igexin.sdk.PushManager;
import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.JobTypeAdapter;
import com.jumeng.repairmanager.bean.CityList;
import com.jumeng.repairmanager.bean.JobType;
import com.jumeng.repairmanager.update.UpdateManager;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.jumeng.repairmanager.view.PickerView;
import com.jumeng.repairmanager.view.PickerView2;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {

	private EditText et_phone;
	private Button btn_login;
	private PopupWindow popupWindow;
	private EditText et_code;
	private LoadingDialog loadingDialog;
	private int logintype=0;
	private SharedPreferences sp;
	private TextView tv_get_code;
	private List<JobType> list=new ArrayList<JobType>();
	private JobTypeAdapter adapter;
	private ListView lv_job_type;
	private RelativeLayout rl_job_type;
	private ImageView iv_arrow;
	private boolean isShowing=false;
	private Map<Integer, Integer> map=MyApplication.getInstance().getWorks();
	private CheckBox checkBox;
	private TextView tv_provision;
	private int isRegister=1;  //1已注册 0未注册
	private TextView tv_select;
	private TextView tv_code;
	private long time=0;
	private TimeCount timeCount;

	private String code;
	private Button btn_pcd;
	private List<CityList> provinces=new ArrayList<>();
	private List<CityList> citys=new ArrayList<>();
	private List<CityList> districts=new ArrayList<>();
	private int province_id;
	private int city_id;
	private int district_id;
	private String province="";
	private String city="";
	private String district="";
	private LinearLayout ll_popup;
	private PopupWindow popupCity;
	private PickerView2 pv_province;
	private PickerView2 pv_city;
	private PickerView2 pv_district;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		MyApplication.getInstance().addActivities(this);
		PushManager.getInstance().initialize(this.getApplicationContext());
		sp=MyApplication.getSharedPref();
		initTitleBar();
		setViews();
		showJobPopup();
		//getProvince();
		//getCity(province_id);
		//getDistrict(city_id);
		showCityPopup();
	}

	private void initTitleBar() {
		initActionBar(LoginActivity.this);
		setViewShow(0, 0, 1, 0, 0);
		setViewContent(null, 0, "登录", null, 0);
	}

	private void setViews() {
		checkBox=(CheckBox)findViewById(R.id.checkBox);
		tv_provision=(TextView)findViewById(R.id.tv_provision);
		tv_select=(TextView)findViewById(R.id.tv_select);
		tv_code=(TextView)findViewById(R.id.tv_code);
		et_phone=(EditText)findViewById(R.id.et_phone);
		et_code=(EditText)findViewById(R.id.et_code);
		tv_get_code=(TextView)findViewById(R.id.tv_get_code);
		btn_pcd=(Button)findViewById(R.id.btn_pcd);
		iv_arrow=(ImageView)findViewById(R.id.iv_arrow);
		rl_job_type=(RelativeLayout)findViewById(R.id.rl_job_type);
		rl_job_type.setOnClickListener(this);

	}

	public void onClick(View v){
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.tv_get_code:
				tv_get_code.setText("重新获取");
				tv_code.setVisibility(View.VISIBLE);
				if(System.currentTimeMillis()-time>12000){
					getIdentifyCode();
					time=System.currentTimeMillis();
				}else{
					Tools.toast(this, "操作过于频繁,请稍后再试");
				}
				break;
			case R.id.tv_provision:
				Intent intent=new Intent(this,WebActivity.class);
				intent.putExtra("flag", Consts.REGISTERPROTOCAL);
				startActivity(intent);

				break;
			case R.id.rl_job_type:
				if(isShowing){
					popupWindow.dismiss();
					tv_select.setTextColor(Color.parseColor("#2A2C38"));
					tv_select.setText("选择从事工种");
					iv_arrow.setVisibility(View.VISIBLE);
					isShowing=false;
				}else{
					popupWindow.showAsDropDown(rl_job_type, 0, 0);
					tv_select.setText("完成");
					tv_select.setTextColor(Color.RED);
					iv_arrow.setVisibility(View.GONE);
					isShowing=true;
				}
				break;
			case R.id.btn_login:
				Login();
				break;
			case R.id.btn_pcd://选择省市区
				//showCityPopup();
				popupCity.showAtLocation(ll_popup, Gravity.CENTER | Gravity.BOTTOM, 0, 0);// 设置显示位置
				break;

		}
	}



	private void showJobPopup(){
		View view=View.inflate(this, R.layout.popupwindow_job_type, null);
		lv_job_type = (ListView)view.findViewById(R.id.lv_job_type);
		popupWindow=new PopupWindow(view,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击其他区域消失
		popupWindow.setFocusable(false); // 控件可获取焦点
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setOutsideTouchable(false);
	}

	private void showCityPopup(){
		View view=View.inflate(this, R.layout.popup_city, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		popupCity = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupCity.setBackgroundDrawable(new BitmapDrawable()); // 点击其他区域消失
		popupCity.setFocusable(true); // 控件可获取焦点
		//	popupCity.showAtLocation(ll_popup, Gravity.CENTER | Gravity.BOTTOM, 0, 0);// 设置显示位置

		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
		pv_province = (PickerView2) view.findViewById(R.id.province);
		pv_city = (PickerView2) view.findViewById(R.id.city);
		pv_district = (PickerView2) view.findViewById(R.id.district);

		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupCity.dismiss();
			}
		});

		btn_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupCity.dismiss();
				btn_pcd.setText(province+city+district);
			}
		});
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = v.findViewById(R.id.ll_popup).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						popupCity.dismiss();
					}
				}
				return true;
			}
		});

		getProvince();

		pv_province.setOnSelectListener(new PickerView2.onSelectListener() {
			@Override
			public void onSelect(CityList city) {
				province_id=city.getId();
				province=city.getName();
				getCity(province_id);

			}
		});
		pv_city.setOnSelectListener(new PickerView2.onSelectListener() {
			@Override
			public void onSelect(CityList c) {
				city_id=c.getId();
				city=c.getName();
				getDistrict(city_id);

			}
		});
		pv_district.setOnSelectListener(new PickerView2.onSelectListener() {
			@Override
			public void onSelect(CityList city) {
				district=city.getName();
				district_id=city.getId();
			}
		});
	}

	/**
	 * 获取工种列表
	 */
	private void getJobList(){

		loadingDialog=new LoadingDialog(this, "正在获取工种...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETALLTYPE);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							loadingDialog.dismiss();
							String data=response.getString("data");
							JSONArray ary=new JSONArray(data);
							List<JobType> jobList=JsonParser.parseJobTypeList(ary);
						/*list.addAll(jobList);
						adapter.notifyDataSetChanged();*/

							adapter=new JobTypeAdapter(getApplicationContext(), jobList);
							lv_job_type.setAdapter(adapter);
							break;
						case 0:
							loadingDialog.dismiss();
							Tools.toast(LoginActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}
	/**
	 * 获取验证码
	 */
	public void getIdentifyCode(){
		int digital=(int)((Math.random()*9+1)*100000);
		//用户输入的电话号码
		String phone = et_phone.getText().toString().trim();
		//非空验证
		if(phone.isEmpty()){
			Tools.toast(this, "手机号不能为空");
			return;
		}
		//验证手机号的长度
		if(!Tools.isMobileNO(phone)){
			Tools.toast(this, "手机号无效!");
			return;
		}

		//隐藏输入法
		InputMethodManager inputMethodManager = (InputMethodManager)
				getSystemService(INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(et_phone.getWindowToken(), 0);
		if(timeCount==null){
			//获取验证码倒计时
			timeCount = new TimeCount(60000,1000);
		}else{
			timeCount.onTick(6000);
		}
		timeCount.start();

		RequestParams params=new RequestParams();
		params.put("code", Consts.GETCODE);
		params.put("phone", phone);
		params.put("logintype", 1);
		params.put("type", 0);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(LoginActivity.this, "获取成功！");
							code = response.getString("TestIng");
							isRegister=response.getInt("iszhuce");
							switch (isRegister) {
								case 1:
									rl_job_type.setVisibility(View.GONE);
									btn_pcd.setVisibility(View.GONE);
									break;
								case 0://未注册
									rl_job_type.setVisibility(View.VISIBLE);
									btn_pcd.setVisibility(View.VISIBLE);
									getJobList();
									break;
							}
							break;
						default:
							Tools.toast(LoginActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	/**
	 * 用户登录接口
	 * @param phone
	 * @param channelId
	 */
	private void vipLogin(String phone,String channelId){
		String typelist = "";
		if(isRegister==0){
			if(map.size()==0){
				return;
			}
			Set<Integer> keySet = map.keySet();
			String workId="";
			for(Integer key:keySet ){
				if(key!=null){
					workId += key+",";
				}
			}
			typelist=workId.substring(0, workId.length()-1);
			if(typelist.isEmpty()){
				Tools.toast(this, "请选择工种");
				return;
			}
		}

		if(!checkBox.isChecked()){
			Tools.toast(this, "请先阅读服务条款");
			return;
		}


		loadingDialog=new LoadingDialog(this, "正在登录...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETLOGIN);
		params.put("phone", phone);
		params.put("chinaid", channelId);
		params.put("typelist",typelist);
		params.put("ioschinaid", "");
		params.put("logintype", logintype);
		params.put("province", province_id);
		params.put("city", city_id);
		params.put("district", district_id);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 2:
							loadingDialog.dismiss();
							Tools.toast(LoginActivity.this, "登录成功");
							int userId=response.getInt("hgid");
							String name=response.getString("name");
							sp.edit().putInt(Consts.USER_ID, userId).putString(Consts.USER_NAME, name).commit();
							Tools.StartActivitytoOther(LoginActivity.this, MainActivity.class);
							finish();
							break;
						default:
							loadingDialog.dismiss();
							Tools.toast(LoginActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


	/**
	 * 获取省级列表
	 */
	private void getProvince(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETPROVINCE);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							String data=response.getString("data");
							JSONArray ary=new JSONArray(data);
							provinces=JsonParser.parsePCDList(ary);
							pv_province.setData(provinces);
							province=pv_province.getSelected().getName();
							province_id=pv_province.getSelected().getId();
							if(city.equals("")){
								getCity(provinces.get(0).getId());
							}
							getCity(province_id);
							break;
						case 0:
							//Tools.toast(LoginActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	/**
	 * 获取市级列表
	 */
	private void getCity(int province_id){

		RequestParams params=new RequestParams();
		params.put("code",Consts.GETCITY);
		params.put("province_id",province_id);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							citys.clear();
							String data=response.getString("data");
							JSONArray ary=new JSONArray(data);
							citys=JsonParser.parsePCDList(ary);
							pv_city.setData(citys);
							city=pv_city.getSelected().getName();
							city_id=pv_city.getSelected().getId();
							if(district.equals("")){
								getDistrict(citys.get(0).getId());
							}
							getDistrict(city_id);
							break;
						case 0:
							//Tools.toast(LoginActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}
	/**
	 * 获取区级列表
	 */
	private void getDistrict(int city_id){

		RequestParams params=new RequestParams();
		params.put("code",Consts.GETDISTRICT);
		params.put("city_id",city_id);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							districts.clear();
							String data=response.getString("data");
							JSONArray ary=new JSONArray(data);
							districts=JsonParser.parsePCDList(ary);
							pv_district.setData(districts);
							district=pv_district.getSelected().getName();
							district_id=pv_district.getSelected().getId();
							break;
						case 0:
							//Tools.toast(LoginActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}







	private void Login() {
		//用户输入的电话号码
		String phone = et_phone.getText().toString().trim();
		String myCode = et_code.getText().toString().trim();
		//String channelId="89ed3dec18bd81432e068f03e732d649";
		String channelId=sp.getString(Consts.CLIENT_ID, null);
		if(phone.isEmpty()){
			Tools.toast(this, "手机号或密码不能为空");
			return;
		}
		//验证手机号的长度
		if(!Tools.isMobileNO(phone)){
			Tools.toast(this, "手机号无效!");
			return;
		}
		if(channelId==null||channelId.equals("")||channelId.isEmpty()){
			//channelId=Tools.getMyUUID(this); 
			Tools.toast(this, "系统异常 ，请稍后重试! ");
			return;
		}
		if(code==null||code.isEmpty()||!code.equals(myCode)){
			Tools.toast(this, "验证码错误!");
			return;
		}
		if(map.size()>3){
			Tools.toast(this, "从事工种最多只能选三个!");
			return;
		}

		vipLogin( phone, channelId);

	}

	class TimeCount extends CountDownTimer{

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//btn_code.setClickable(false);
			tv_code.setText("("+(millisUntilFinished/1000)+"s"+")");
		}

		@Override
		public void onFinish() {
			tv_get_code.setText("获取验证码");
			tv_code.setVisibility(View.GONE);
			tv_get_code.setClickable(true);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		map.clear();
	}

}
