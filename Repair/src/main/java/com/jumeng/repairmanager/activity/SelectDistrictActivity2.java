package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.CityListAdapter;
import com.jumeng.repairmanager.adapter.CityListAdapter2;
import com.jumeng.repairmanager.bean.DistrictList;
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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 选择区页面
 * */
public class SelectDistrictActivity2 extends BaseActivity{

	private static final String TAG = DGStoreActivity.class.getSimpleName();
	private int page = 1;
	private GridView gridView;
	private CityListAdapter adapter;
	private CityListAdapter2 adapter2;
	private TextView tv_type;
	private LoadingDialog loadingDialog;
	private int cityId;
	private String cityName;
	private List<DistrictList> cityList=new ArrayList<>();
	private List<DistrictList> districtList=new ArrayList<>();
	private MyReceiver receiver;
	private TextView tv_tips;
	private Context context;
	private ListView listView;
	private Map<Integer,Boolean> isSelected = new HashMap<Integer,Boolean>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_district2);
		context=this;
		MyApplication.getInstance().addActivities(this);
		initTitleBar() ;
		setViews();
		initGridview( );
		//getCity();
		registerMyReceiver();
	}
	private void initTitleBar() {
		initActionBar(SelectDistrictActivity2.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "选择地区", null, 0);
	}

	private void setViews() {
		listView=(ListView)findViewById(R.id.listView);
		tv_type=(TextView)findViewById(R.id.tv_type);
		tv_tips=(TextView)findViewById(R.id.tv_tips);
		adapter2=new CityListAdapter2(this, cityList,isSelected);
		listView.setAdapter(adapter2);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//getDistrict(cityList.get(position).getDistrict());
				setCheckMap(cityList.get(position).getDistrictId());
				tv_type.setText(cityList.get(position).getDistrict());
			}
		});

	}

	private void initGridview( ) {
		gridView=(GridView)findViewById(R.id.gridView);
		adapter=new CityListAdapter(this, districtList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tools.StartActivitytoOther(context, DGStoreActivity.class);
				MyApplication.getInstance().setDistrictId(districtList.get(position).getDistrictId());
			}
		});
	}

	private void initCheckMap(List<DistrictList> list ){
		isSelected.put(list.get(0).getDistrictId(), true);
		for(int i=1;i<list.size();i++){
			isSelected.put(list.get(i).getDistrictId(), false);
		}
	}

	private void setCheckMap(int cityId){
		Set<Integer> keySet = isSelected.keySet();
		for(int id : keySet){
			isSelected.put(id, false);
		}
		isSelected.put(cityId, true);
		adapter2.notifyDataSetChanged();
	}



	/**
	 * 获取城市列表
	 */
	/*private void getCity(){
		cityList.clear();
		RequestParams params=new RequestParams();
		params.put("province", MyApplication.getInstance().getProvince());
		HttpUtil.post(Consts.WORKER+Consts.GETS_CITY, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						cityList.addAll(JsonParser.parseCityList(ary));
						getDistrict(cityList.get(0).getDistrict());
						tv_type.setText(cityList.get(0).getDistrict());
						cityId=cityList.get(0).getDistrictId();
						initCheckMap(cityList);
						break;
					default:
						//Tools.toast(MyCommentActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter2.notifyDataSetChanged();
			}

		});

	}*/
	/**
	 * 获取区县列表
	 */
	/*private void getDistrict(String city){
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

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.iv_right:
			Intent intent=new Intent(this, SearchBusinessActivity.class);
			intent.putExtra("flag", 1);
			startActivity(intent);
			break;
		}

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

			finish();
		}

	}

}
