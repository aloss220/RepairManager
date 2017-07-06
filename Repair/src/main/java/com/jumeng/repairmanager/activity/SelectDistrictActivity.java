package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.DistrictList;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 选择城市页面
 * */
public class SelectDistrictActivity extends BaseActivity {

	private GridView gridView;
	private String province;
	private Context context;
	private List<DistrictList> cityList=new ArrayList<>();
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_district);
		context=this;
		province=MyApplication.getInstance().getProvince();
		initTitleBar();
		setViews();
		//getCity();
	}

	private void initTitleBar() {
		initActionBar(SelectDistrictActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, province,null, 0);
	}

	private void setViews() {
		gridView=(GridView)findViewById(R.id.gridView);
		adapter=new MyAdapter();
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*Tools.StartActivitytoOther(context, DGStoreActivity.class);
				MyApplication.getInstance().setDistrictId(districts.get(position).getDistrictId());*/
				
				Intent intent=new Intent(context, SearchBusinessActivity.class);
				intent.putExtra("cityId",cityList.get(position).getDistrictId());
				intent.putExtra("city",cityList.get(position).getDistrict());
				startActivity(intent);

			}
		});
	}

	/**
	 * 获取城市列表
	 */
	/*private void getCity(){
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


	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		}
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cityList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=View.inflate(context, R.layout.gv_district_item, null);
			TextView tv_lable = (TextView)convertView.findViewById(R.id.tv_lable);
			tv_lable.setText(cityList.get(pos).getDistrict());
			return convertView;
		}


	}

}
