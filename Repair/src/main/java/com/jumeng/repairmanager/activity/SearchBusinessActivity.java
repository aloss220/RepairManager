package com.jumeng.repairmanager.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.BusinessListAdapter;
import com.jumeng.repairmanager.adapter.SortAdapter;
import com.jumeng.repairmanager.adapter.SortAdapter2;
import com.jumeng.repairmanager.bean.BusinessList;
import com.jumeng.repairmanager.bean.DistrictList;
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;


public class SearchBusinessActivity  extends BaseActivity  implements TextWatcher,OnRefreshListener{
	private static final String TAG = SearchBusinessActivity.class.getSimpleName();
	private int page = 1;
	private int pageNum=10;
	private LoadingDialog mDialog;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private BusinessListAdapter adapter;
	private List<BusinessList> list=new ArrayList<>();
	private List<DistrictList> districtList=new ArrayList<>();
	private List<DistrictList> typeList=new ArrayList<>();
	private EditText et_search;
	private ImageView iv_delete;
	private PopupWindow pop;
	private LoadingDialog loadingDialog;
	private MyReceiver receiver;
	private TextView tv_tips;
	private int typeOneId=0;
	private int cityId;
	private RadioButton rb_district;
	private RadioButton rb_type;
	private Context context;
	private ListView lv_popup;
	private PopupWindow pop2;
	private ListView lv_popup2;
	private String search="";
	private String city;
	private int districtId=0;
	private Drawable drawable2;
	private Drawable drawable3;
	private Drawable drawable4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_business);
		context=this;
		MyApplication.getInstance().addActivities(this);
		cityId=getIntent().getIntExtra("cityId", -1);
		city=getIntent().getStringExtra("city");
		setViews();
		initGridview();
		//getBusinessList(search);
		popupWindow();
		popupWindow2();
		//getDistrict();
		//getProductTypeOne();
		registerMyReceiver();
	}
	private void setViews() {
		et_search=(EditText)findViewById(R.id.et_search);
		et_search.addTextChangedListener(this);
		iv_delete=(ImageView)findViewById(R.id.iv_delete);
		tv_tips=(TextView)findViewById(R.id.tv_tips);
		rb_district=(RadioButton)findViewById(R.id.rb_district);
		rb_type=(RadioButton)findViewById(R.id.rb_type);
		drawable2= getResources().getDrawable(R.mipmap.arrow_g_d);
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
		drawable3= getResources().getDrawable(R.mipmap.arrow_b_d);
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		drawable4= getResources().getDrawable(R.mipmap.arrow_b_u);
		drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());

		OnClickListener rbListener=new OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.rb_district:
					if(pop2.isShowing()){
						pop2.dismiss();
					}
					rb_type.setCompoundDrawables(null,null,drawable2,null);
					rb_district.setSelected(true);
					rb_type.setSelected(false);
					if(pop.isShowing()){
						pop.dismiss();
						rb_district.setCompoundDrawables(null,null,drawable3,null);
					}else{
						pop.showAsDropDown(rb_district, 0, 0);
						rb_district.setCompoundDrawables(null,null,drawable4,null);
					}

					break;
				case R.id.rb_type:
					//点击第二个弹窗时 如果第一个弹窗没有销毁 就把他销毁 
					if(pop.isShowing()){
						pop.dismiss();
					}
					//把第一个radiobutton的箭头设置为向下灰色
					rb_district.setCompoundDrawables(null,null,drawable2,null);
					rb_district.setSelected(false);
					rb_type.setSelected(true);
					//点击时先判断第二个弹窗是否打开  如果打开就销毁 箭头设置为向下橘色  如果没打开 就打开 并设置箭头向上橘色
					if(pop2.isShowing()){
						pop2.dismiss();
						rb_type.setCompoundDrawables(null,null,drawable3,null);
					}else{
						pop2.showAsDropDown(rb_type, 0, 0);
						rb_type.setCompoundDrawables(null,null,drawable4,null);
					}

					break;
				}
			}
		};
		rb_district.setOnClickListener(rbListener);
		rb_type.setOnClickListener(rbListener);
	}
	private void initGridview( ) {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		listView=(PullableListView)findViewById(R.id.listView);
		layout.setOnRefreshListener(this);
		adapter=new BusinessListAdapter(this, list);
		listView.setAdapter(adapter);

		/*listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(SearchBusinessActivity.this, ProductDetailActivity.class);
				//intent.putExtra("productId",list.get(position).getId());
				startActivity(intent);

			}
		});*/
	}


	/**
	 * 获取区县列表
	 */
	/*private void getDistrict(){
		districtList.clear();
		RequestParams params=new RequestParams();
		params.put("city", city);
		HttpUtil.post(Consts.WORKER+Consts.GETS_DISTRICT, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				districtList.clear();
				try {
					switch (response.getInt("state")) {
					case 1:
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						districtList.addAll(JsonParser.parseDistrictList(ary));
						SortAdapter adapter=new SortAdapter(context, districtList);
						lv_popup.setAdapter(adapter);
						break;
					case 2:
						Tools.toast(context, response.getString("msg"));
						break;
					default:
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
	 * 获取商品一级列表
	 */
	/*private void getProductTypeOne(){
		districtList.clear();
		HttpUtil.post(Consts.WORKER+Consts.GETPRODUCTTYPEONE, null, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						typeList=JsonParser.parseProductOne2(ary);
						SortAdapter2 adapter=new SortAdapter2(context, typeList);
						lv_popup2.setAdapter(adapter);

						break;
					default:
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
	 * 获取商家列表
	 */
	/*private void getBusinessList(String search){
		loadingDialog=new LoadingDialog(this, "正在获取商家列表...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("cityid", cityId);
		params.put("onetypeid", typeOneId);
		params.put("twotypeid", 0);//二级分类废弃
		params.put("districtid", districtId);
		params.put("longitude", MyApplication.getInstance().getLongitude());//经度
		params.put("latitude", MyApplication.getInstance().getLatitude());//纬度
		params.put("mess", search);//查询条件
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.WORKER+Consts.GETMERCHANTSLIST, params, new MyJsonHttpResponseHandler(this){
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
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<BusinessList> shopList=JsonParser.parseShopList(ary);
						list.addAll(shopList);

						if(shopList.size()==0){
							layout.setVisibility(View.GONE);
							tv_tips.setVisibility(View.VISIBLE);
						}else{
							layout.setVisibility(View.VISIBLE);
							tv_tips.setVisibility(View.GONE);
						}
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

	// 弹出框
	private void popupWindow() {
		View view = View.inflate(context,R.layout.popup_sort, null);
		final LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		lv_popup=(ListView)view.findViewById(R.id.lv_popup);

		pop = new PopupWindow(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//pop.setBackgroundDrawable(new BitmapDrawable());//点击外部消失
		pop.setFocusable(false);//设置为true的话  popwindow不消失就无法点击其他区域
		pop.setOutsideTouchable(false);
		pop.setContentView(view);
		//pop.showAsDropDown(findViewById(R.id.rb0), 0, 0);

		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				int height = ll_popup.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y >height) {
						pop.dismiss();
						rb_district.setCompoundDrawables(null,null,drawable3,null);
					}
				}
				return true;
			}
		});

	}
	// 弹出框
	private void popupWindow2() {
		View view = View.inflate(context,R.layout.popup_sort, null);
		final LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		lv_popup2=(ListView)view.findViewById(R.id.lv_popup);

		pop2 = new PopupWindow(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//pop2.setBackgroundDrawable(new BitmapDrawable());
		pop2.setFocusable(false);
		pop2.setOutsideTouchable(false);
		pop2.setContentView(view);
		//pop.showAsDropDown(findViewById(R.id.rb0), 0, 0);

		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				int height = ll_popup.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y >height) {
						pop2.dismiss();
						rb_type.setCompoundDrawables(null,null,drawable3,null);
					}
				}
				return true;
			}
		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_delete:
			et_search.setText(null);
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_search:
			list.clear();
			/*districtId=0;
			typeOneId=0;*/
			//getBusinessList(search);
			break;
		}

	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(TextUtils.isEmpty(et_search.getText())){
			iv_delete.setVisibility(View.GONE);
			list.clear();
			//getBusinessList("");
		}else{
			iv_delete.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		search = et_search.getText().toString();

	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {
			public void run() {
				page=1;
				//getBusinessList(search);
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

			}
		}, 500);

	}
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {
			public void run() {
				page++;
				//getBusinessList(search);
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
		IntentFilter filter=new IntentFilter();
		filter.addAction("district");
		filter.addAction("type");
		registerReceiver(receiver, filter);
	}

	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("district")){
				districtId=intent.getIntExtra("districtId", -1);
				String district = intent.getStringExtra("district");
				pop.dismiss();
				rb_district.setText(district);
				rb_district.setCompoundDrawables(null,null,drawable3,null);
			}else if(intent.getAction().equals("type")){
				typeOneId=intent.getIntExtra("districtId", -1);
				pop2.dismiss();
				String type = intent.getStringExtra("type");
				rb_type.setText(type);
				rb_type.setCompoundDrawables(null,null,drawable3,null);
			}
			//getBusinessList(search);
		}

	}

}
