package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.GoodsListAdapter2;
import com.jumeng.repairmanager.bean.ADInfo;
import com.jumeng.repairmanager.bean.ProductTypeTwo;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyImageCycleViewListener;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.ImageCycleView;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.jumeng.repairmanager.view.MyGridView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BusinessDetailActivity extends BaseActivity {
	private RelativeLayout rl_phone;
	private ImageCycleView imageCycleView;
	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
	private MyGridView gridView;
	private Context context;
	private List<ProductTypeTwo> list=new ArrayList<>();
	private int shopId;
	private TextView tv_shopName;
	private TextView tv_distance;
	private TextView tv_address;
	private TextView tv_introduce;
	private int page = 1;
	private int pageNum=100;
	private String distance;
	private String phone;
	private String address;
	private String longitude;
	private String latitude;
	private String shopName;
	private TextView tv_tips;
	private GoodsListAdapter2 adapter;
	private LoadingDialog loadingDialog;
	private String[] imgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_detail);
		context=this;
		shopId=getIntent().getIntExtra("shopId", -1);
		distance=getIntent().getStringExtra("distance");
		initTitleBar();
		setViews();
		//getShopDetail();
		setGridView();
		//getMainProduct();


	}


	private void initTitleBar() {
		initActionBar(BusinessDetailActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "店铺详情",null, 0);
	}


	private void setViews() {
		rl_phone=(RelativeLayout)findViewById(R.id.rl_phone);
		rl_phone.setOnClickListener(this);
		imageCycleView=(ImageCycleView)findViewById(R.id.imageCycleView);

		tv_shopName=(TextView)findViewById(R.id.tv_shopName);
		tv_distance=(TextView)findViewById(R.id.tv_distance);
		tv_address=(TextView)findViewById(R.id.tv_address);
		tv_introduce=(TextView)findViewById(R.id.tv_introduce);
		tv_tips=(TextView)findViewById(R.id.tv_tips);
		tv_distance.setText(distance+"km");
	}

	private void setGridView(){
		gridView=(MyGridView)findViewById(R.id.gridView);
		 adapter=new GoodsListAdapter2(context, list);
		gridView.setAdapter(adapter);
		gridView.setFocusable(false);
	}
	private void initialize(String [] imgs) {
		for(int i=0;i < imgs.length; i ++){
			ADInfo info = new ADInfo();
			//info.setResID(items[i]);
			info.setUrl(imgs[i]);
			info.setContent("top-->" + i);
			infos.add(info);
		}
		//第三个参数为true表示图片来自网络 
		imageCycleView.setImageResources(infos, mAdCycleViewListener,true);
	}

	private MyImageCycleViewListener mAdCycleViewListener = new MyImageCycleViewListener() {

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			// TODO Auto-generated method stub
			ImageLoader.getInstance().displayImage(imageURL, imageView,ImageLoaderOptionUtil.getImageDisplayOption("lunbo"));
		}
		/*@Override
		public void displayImageSD(int id, ImageView imageView) {
			imageView.setBackgroundResource(id);
		}*/
		/*轮播图点击事件**/
		public void onImageClick(ADInfo info, int postion, View imageView) {
			//Tools.toast(context,""+postion);
			if(imgs!=null){
				imageBrower(postion,imgs);
			}

		}

    };

	
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 */
	protected void imageBrower(int position, String [] imgs) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		Bundle bundle=new Bundle();
		intent.putExtra("pos", position);
		bundle.putStringArray("imgs", imgs);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 获取店铺详情
	 */
	/*private void getShopDetail(){
		RequestParams params=new RequestParams();
		params.put("merchantsid", shopId);
		HttpUtil.post(Consts.WORKER+Consts.GETMERCHANTS, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						//Tools.toast(MyCommentActivity.this, "获取评论列表成功！");
						shopName=response.getString("merchantsName");
						phone=response.getString("tel");
						address=response.getString("adress");
						longitude=response.getString("longitude");
						latitude=response.getString("latitude");
						String introduce=response.getString("mainproducts");
						String images=response.getString("images");//顶部轮播图
						if(images!=null&&images.length()!=0&&images!=""){
							imgs = images.split(",");
							initialize(imgs);
						}

						tv_shopName.setText(shopName);
						tv_address.setText(address);
						tv_introduce.setText(introduce);
						break;
					case 2:
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
	 * 主营商品
	 */
	/*private void getMainProduct(){
		RequestParams params=new RequestParams();
		params.put("merchantsid", shopId);
		params.put("page", page);
		params.put("pagenum", pageNum);
		HttpUtil.post(Consts.WORKER+Consts.GETMERCHANTSPRODUCT, params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						tv_tips.setVisibility(View.GONE);
						String data=response.getString("List");
						JSONArray ary=new JSONArray(data);
						list.addAll(JsonParser.parseMainProductList(ary));
						break;
					case 2:
						tv_tips.setVisibility(View.VISIBLE);
						tv_tips.setText(response.getString("msg"));
						//Tools.toast(context, response.getString("msg"));
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

	private void goToMap(){
		if(Tools.isInstall("com.autonavi.minimap")){
			try {
				Intent i = new Intent();
				i.setAction("android.intent.action.VIEW");
				i.addCategory("android.intent.category.DEFAULT");
				i.setPackage("com.autonavi.minimap");
				/*i.setData(Uri
						.parse("androidamap://viewMap?sourceApplication=洗车联盟&poiname="+marker.getSnippet().split(",")[0]+"&lat="+marker.getPosition().latitude+"&lon="+marker.getPosition().longitude+"&dev=0")); // softname，开发程序的名称
				 */				
				i.setData(Uri
						.parse("androidamap://viewMap?sourceApplication=U匠&poiname="+address+"&lat="+Double.parseDouble(latitude)+"&lon="+Double.parseDouble(longitude)+"&dev=0")); // softname，开发程序的名称
				startActivity(i);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}else{
			if(Tools.isInstall("com.baidu.BaiduMap")){
				try {
					//Intent intent = Intent.getIntent("intent://map/marker?location="+marker.getPosition().latitude+","+marker.getPosition().longitude+"&title="+marker.getSnippet().split(",")[0]+"&content="+marker.getTitle()+"&src=聚盟科技|洗车联盟#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
					Intent intent = Intent.getIntent("intent://map/marker?location="+Double.parseDouble(latitude)+","+Double.parseDouble(longitude)+"&title="+shopName+"&content="+address+"&src=诺诚四海|U匠#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
					startActivity(intent); //启动调用
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}  
			}else{
				Tools.toast(context, "安装高德或百度地图后可使用导航功能");
			}

		}

	}

	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.rl_phone:
			loadingDialog=new LoadingDialog(this,"");
			loadingDialog.show();
			Tools.Dial2(this, phone,loadingDialog);
			break;
		case R.id.iv_location:
			goToMap();
			break;

		}

	}

}
