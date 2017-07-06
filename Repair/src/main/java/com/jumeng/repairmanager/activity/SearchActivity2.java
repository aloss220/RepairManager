package com.jumeng.repairmanager.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.ProductListAdapter;
import com.jumeng.repairmanager.bean.ProductList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullToRefreshLayout.OnRefreshListener;
import com.jumeng.repairmanager.listview.PullableGridView;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class SearchActivity2  extends BaseActivity  implements TextWatcher,OnRefreshListener{
	private static final String TAG = SearchActivity2.class.getSimpleName();
	private TextView mAllDetail;
	private TextView mAllMoney;
	private int page = 1;
	private int pageNum=10;
	private int type=1;//0按上架时间倒序 1按销量 2价格从高到低 3价格从低到高
	private LoadingDialog mDialog;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private ProductListAdapter adapter;
	private List<ProductList> list=new ArrayList<>();
	private EditText et_search;
	private ImageView iv_delete;
	private LinearLayout ll_popup;
	private PopupWindow pop;
	private PullableGridView gridView;
	private LoadingDialog loadingDialog;
	private int typeId;
	private View loadmore_view;
	private int select=0;
	private int flag;
	private MyReceiver receiver;
	private TextView tv_tips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search2);
		MyApplication.getInstance().addActivities(this);
		typeId=getIntent().getIntExtra("typeId", -1);
		flag=getIntent().getIntExtra("intent", -1);
		setViews();
		initGridview( );
		//getProductList(page,type);
		registerMyReceiver();
	}



	private void setViews() {
		et_search=(EditText)findViewById(R.id.et_search);
		et_search.addTextChangedListener(this);
		iv_delete=(ImageView)findViewById(R.id.iv_delete);
		tv_tips=(TextView)findViewById(R.id.tv_tips);
	}
	private void initGridview( ) {
		layout=(PullToRefreshLayout)findViewById(R.id.refresh_view);
		loadmore_view= findViewById(R.id.loadmore_view);
		layout.setOnRefreshListener(this);
		gridView=(PullableGridView)findViewById(R.id.gridView);
		adapter=new ProductListAdapter(this, list);
		gridView.setAdapter(adapter);
		
		
		if(adapter.getCount()<6){
		loadmore_view.setVisibility(View.INVISIBLE);
	}
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(SearchActivity2.this, ProductDetailActivity.class);
				intent.putExtra("productId",list.get(position).getId());
				startActivity(intent);
				
			}
		});
	}

	
	/**
	 * 获取商品列表
	 */
	/*private void getProductList(final int page,int type){
		loadingDialog=new LoadingDialog(this, "正在获取商品列表...");
		loadingDialog.show();
		String search = et_search.getText().toString();
		RequestParams params=new RequestParams();
		if(flag==1){
			params.put("twotypeid", 0);
		}
		params.put("twotypeid", typeId);
		params.put("page", page);
		params.put("pagenum", pageNum);
		params.put("productname", search);
		params.put("type", type);
		HttpUtil.post(Consts.WORKER+Consts.GETPRODUCTLIST, params, new MyJsonHttpResponseHandler(this){
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
						List<ProductList> productList=JsonParser.parseProductList(ary);
						list.addAll(productList);
						
						if(productList.size()==0){
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




	// 排序弹出框
	public void sortWindow(int select2) {
		View view = getLayoutInflater().inflate(R.layout.layout_sort, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		RadioGroup rg=(RadioGroup)view.findViewById(R.id.rg);
		RadioButton r1 = (RadioButton)view.findViewById(R.id.r1);
		RadioButton r2 = (RadioButton)view.findViewById(R.id.r2);
		RadioButton r3 = (RadioButton)view.findViewById(R.id.r3);
		
		switch (select2) {
		case 1:
			rg.check(R.id.r1);
			break;
		case 2:
			rg.check(R.id.r2);
			break;
		case 3:
			rg.check(R.id.r3);
			break;
		}
		
		pop = new PopupWindow(this);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.MATCH_PARENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		pop.showAsDropDown(findViewById(R.id.radioGroup), 0, 0);

		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.r1:
					select=1;
					type=0;
					//getProductList(page,type);
					pop.dismiss();
					break;
				case R.id.r2:
					select=2;
					type=2;
					//getProductList(page,type);
					pop.dismiss();
					break;
				case R.id.r3:
					select=3;
					type=3;
					//getProductList(page,type);
					pop.dismiss();
					break;
				}

			}
		};
		r1.setOnClickListener(listener);
		r2.setOnClickListener(listener);
		r3.setOnClickListener(listener);
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
			//getProductList(page,type);
			break;
		case R.id.radio1:
			select=0;
			type=1;
			//getProductList(page,type);
			break;
		case R.id.radio2:
			sortWindow(select);
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
		}else{
			iv_delete.setVisibility(View.VISIBLE);
		}

	}



	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.postDelayed(new Runnable() {
			public void run() {
				page=1;
				//getProductList(page,type);
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				
			}
		}, 500);
		
	}
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		loadmore_view.setVisibility(View.VISIBLE);
		pullToRefreshLayout.postDelayed(new Runnable() {
			public void run() {
				page++;
				//getProductList(page,type);
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
