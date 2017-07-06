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
import com.jumeng.repairmanager.adapter.GoodsListAdapter;
import com.jumeng.repairmanager.bean.ProductTypeOne;
import com.jumeng.repairmanager.bean.ProductTypeTwo;
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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class DGStoreActivity  extends BaseActivity {
	private static final String TAG = DGStoreActivity.class.getSimpleName();
	private int page = 1;
	private GridView gridView;
	private GoodsListAdapter adapter;
	private TextView tv_type;
	private RadioGroup radioGroup;
	private LoadingDialog loadingDialog;
	private int typeId;
	private String typeName;
	private List<ProductTypeOne> productListOne=new ArrayList<>();
	private List<ProductTypeTwo> productListTwo=new ArrayList<>();
	private MyReceiver receiver;
	private TextView tv_tips;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dgstore);
		MyApplication.getInstance().addActivities(this);
		initTitleBar() ;
		setViews();
		initGridview( );
		//getProductTypeOne();
		registerMyReceiver();
	}
	private void initTitleBar() {
		initActionBar(DGStoreActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "商品分类", null, 0);
	}

	private void setViews() {
		radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
		tv_type=(TextView)findViewById(R.id.tv_type);
		tv_tips=(TextView)findViewById(R.id.tv_tips);
	}




	private void initGridview( ) {
		gridView=(GridView)findViewById(R.id.gridView);
		adapter=new GoodsListAdapter(this, productListTwo);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(DGStoreActivity.this, SearchBusinessActivity.class);
				intent.putExtra("typeOneId",typeId);
				intent.putExtra("typeTwoId",productListTwo.get(position).getId());
				startActivity(intent);
			}
		});
	}

	private void addRadioButton(){
		tv_type.setText(productListOne.get(0).getName());
		for(int i=0;i<productListOne.size();i++){
			RadioButton rb=new RadioButton(this);
			rb.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));           // 设置按钮的样式    透明
			
			ColorStateList cs=getResources().getColorStateList(+R.drawable.rb_text_selector);
			rb.setTextColor(cs);//将文字selector引入代码
			
			rb.setBackgroundResource(R.drawable.rb_bg_selector2);   // 设置RadioButton的背景图片  
			rb.setTextSize(14);
			rb.setPadding(50, 40, 25,40);                 // 设置文字距离按钮四周的距离   左上右下
			rb.setText(productListOne.get(i).getName());  
			radioGroup.addView(rb, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);  
			
		}
		
		((RadioButton)radioGroup.getChildAt(0)).setChecked(true);//默认第一个选中
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int position=checkedId%productListOne.size();
				if(position==0){
					typeName=productListOne.get(productListOne.size()-1).getName();
				}else{
					typeName=productListOne.get(position-1).getName();
				}
				//getProductTypeTwo(typeId);
				tv_type.setText(typeName);
			}
		});
	}
	private void addRadioButton(final List<ProductTypeOne> list){
		for(int i=0;i<list.size();i++){
			RadioButton rb=new RadioButton(this);
			rb.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));           // 设置按钮的样式    透明

			ColorStateList cs=getResources().getColorStateList(+R.drawable.rb_text_selector);
			rb.setTextColor(cs);//将文字selector引入代码

			rb.setBackgroundResource(R.drawable.rb_bg_selector2);   // 设置RadioButton的背景图片  
			rb.setTextSize(15);
			rb.setGravity(Gravity.CENTER);
			rb.setPadding(25, 35, 25,35);                 // 设置文字距离按钮四周的距离   左上右下
			rb.setText(list.get(i).getName());  
			radioGroup.addView(rb, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);  

		}

		((RadioButton)radioGroup.getChildAt(0)).setChecked(true);//默认第一个选中
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int position=checkedId%list.size();
				if(position==0){
					typeId=list.get(list.size()-1).getId();
					typeName=list.get(list.size()-1).getName();
				}else{
					typeId=list.get(position-1).getId();
					typeName=list.get(position-1).getName();
				}
				//getProductTypeTwo(typeId);
				tv_type.setText(typeName);
			}
		});
	}

	/**
	 * 获取商品一级列表
	 */
	/*private void getProductTypeOne(){
		HttpUtil.post(Consts.WORKER+Consts.GETPRODUCTTYPEONE, null, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						//Tools.toast(MyCommentActivity.this, "获取评论列表成功！");
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<ProductTypeOne> typeOne=JsonParser.parseProductOne(ary);
						addRadioButton(typeOne);
						getProductTypeTwo(typeOne.get(0).getId());
						tv_type.setText(typeOne.get(0).getName());
						typeId=typeOne.get(0).getId();
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

	}
	*//**
	 * 获取商品二级列表
	 *//*
	private void getProductTypeTwo(int typeId){
		loadingDialog=new LoadingDialog(this, "正在获取商品列表...");
		loadingDialog.show();
		
		RequestParams params=new RequestParams();
		params.put("toptypeid", typeId);
		HttpUtil.post(Consts.WORKER+Consts.GETPRODUCTTYPETWO, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				productListTwo.clear();
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						//Tools.toast(MyCommentActivity.this, "获取评论列表成功！");
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						List<ProductTypeTwo> typeTwo=JsonParser.parseProductTwo(ary);
						productListTwo.addAll(typeTwo);
						adapter.notifyDataSetChanged();
						
						if(typeTwo.size()==0){
							tv_tips.setVisibility(View.VISIBLE);
						}else{
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
				
			}
			
		});
		
	}
*/

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
