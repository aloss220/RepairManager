package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.MyApplication.getSharedPref;
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
import com.jumeng.repairmanager.activity.SearchBusinessActivity.MyReceiver;
import com.jumeng.repairmanager.bean.ADInfo;
import com.jumeng.repairmanager.bean.ProductList;
import com.jumeng.repairmanager.bean.ProductTypeOne;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.ImageCycleView;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.jumeng.repairmanager.view.ImageCycleView.ImageCycleViewListener;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 商品详情页面
 * @author he
 *
 */
public class ProductDetailActivity extends BaseActivity {
	private TextView tv_title_exchange;
	private TextView tv_total_points_exchange;
	private TextView tv_number_exchange;
	private WebView wv_product_detail;
	private int id;
	private String name;
	private ImageCycleView ad_view_exchange;
	private ImageView iv_exchange;
	private String url;
	private SharedPreferences sharedPreferences;
	private int userId;
	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
	private String icon;
	private ImageCycleView cycleView;
	private TextView tv_info;
	private TextView tv_money;
	private TextView tv_scale;
	private TextView tv_count;
	private LoadingDialog loadingDialog;
	private int count=1;
	private int prodId;
	private double price;
	private String img;

	private int [] imgId={R.mipmap.test04,R.mipmap.test05,R.mipmap.test04,R.mipmap.test04};
	private WebView webView;
	private int productId;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);
		productId=getIntent().getIntExtra("productId", -1);
		initTitleBar();
		setViews();
		setWebView();
		registerMyReceiver();

	}
	private void initTitleBar() {
		initActionBar(ProductDetailActivity.this);
		setViewShow(0, 1, 1, 0, 1);
		setViewContent(null, R.mipmap.left_arrow, "商品详情", null, R.mipmap.shop_car_w);

	}
	private void setViews() {
		cycleView=(ImageCycleView)findViewById(R.id.cycleView);
		tv_info=(TextView)findViewById(R.id.tv_info);
		tv_money=(TextView)findViewById(R.id.tv_money);
		tv_scale=(TextView)findViewById(R.id.tv_scale);
		tv_count=(TextView)findViewById(R.id.tv_count);


		//getProductDetail();

	}
	private void setWebView(){

		webView = (WebView)findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();  
		webSettings.setJavaScriptEnabled(true);  


		webView.loadUrl(url);  
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

		});

	}

	private void initialize(String [] imgs) {
		for(int i=0;i < imgs.length; i++){
			ADInfo info = new ADInfo();
			info.setUrl(imgs[i]);
			info.setContent("top-->" + i);
			infos.add(info);
		}
		cycleView.setImageResources(infos, mAdCycleViewListener,true);
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {

		}
		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView,ImageLoaderOptionUtil.getImageDisplayOption("ProductDetail"));
		}
		@Override
		public void displayImageSD(int id, ImageView imageView) {
			// TODO Auto-generated method stub

		}
	};
	private MyReceiver receiver;


	/**
	 * 获取商品详情
	 */
	/*private void getProductDetail(){
		loadingDialog=new LoadingDialog(this, "正在获取商品详情...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("productid", productId);

		HttpUtil.post(Consts.WORKER+Consts.GETPRODUCTDETAILED, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						prodId=response.getInt("id");
						 img=response.getString("images");
						String [] imgs=img.split(",");
						initialize(imgs);
						 name=response.getString("productname");
						 price=response.getDouble("price");
						double scale=response.getDouble("rebate");

						tv_info.setText(name);
						tv_money.setText("￥"+price);
						tv_scale.setText("提成比例"+scale+"%");
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

	}*/


	/**
	 * 加入购物车
	 */
	/*private void addShopCart(){
		if(count==0){
			Tools.toast(this, "商品数量必须大于0");
			return;
		}
		RequestParams params=new RequestParams();
		params.put("productid", productId);
		params.put("workerid", userId);
		params.put("shopnum", count);
		HttpUtil.post(Consts.WORKER+Consts.ADDSHOP, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						Tools.toast(ProductDetailActivity.this, "已加入购物车");
						break;
					default:
						Tools.toast(ProductDetailActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}*/

	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.iv_right:	
			Intent i=new Intent(this, ShopCarActivity.class);
			startActivity(i);
			break;
		case R.id.btn_left:	
			if(count>0){
				count--;
			}
			tv_count.setText(count+"");
			break;
		case R.id.btn_right:	
			count++;
			tv_count.setText(count+"");
			break;
		case R.id.btn_shop_car:	
			//addShopCart();
			break;
		case R.id.btn_order:
			Intent intent=new Intent(this, CreateOrderActivity.class);
			intent.putExtra("falg", 1);
			intent.putExtra("productId", prodId+"");
			intent.putExtra("img", img);
			intent.putExtra("name", name);
			intent.putExtra("count", count+"");
			intent.putExtra("price", price+"");
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
