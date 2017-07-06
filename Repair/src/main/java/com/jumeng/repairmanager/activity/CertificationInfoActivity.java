package com.jumeng.repairmanager.activity;

import static com.igexin.push.core.a.i;
import static com.jumeng.repairmanager.R.id.iv_userImg;
import static com.jumeng.repairmanager.util.SetActionBar.border;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.rl_titlebar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;
import static com.jumeng.repairmanager.util.SetActionBar.tv_center;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.receiver.GlobleController.MyListener;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.ImageTools;
import com.jumeng.repairmanager.util.ImageUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadOptions;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CertificationInfoActivity extends BaseActivity {
	private LoadingDialog loadingDialog;
	private SharedPreferences sp;
	private int userId;
	private ImageView imageView[] = new ImageView[3];
	private ImageView imageTips[] = new ImageView[3];
	private int index;


	//版本比较：是否是4.4及以上版本
	final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	//保存图片本地路径
	public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
			+ "/account/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
	private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;
	private static final String IMAGE_FILE_NAME = "faceImage.jpeg";
	private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";

	//常量定义
	public static final int TAKE_A_PICTURE = 10;
	public static final int SELECT_A_PICTURE = 20;
	public static final int SET_PICTURE = 30;
	public static final int SET_ALBUM_PICTURE_KITKAT = 40;
	public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;
	private String mAlbumPicturePath = null;

	File fileone = null;
	File filetwo = null;

	private PopupWindow pop_photo;
	private LinearLayout ll_popup_photo;
	private RelativeLayout parent_photo;
	private Button bt_carema;
	private Button bt_photo;
	private Button bt_cancel_photo;
	private String IFront="";
	private String Iback="";
	private String Icert="";
	private Button btn_certification;
	private int verifyStatus;
	private String cardFront;
	private String cardBack;
	private String certificate;
	private SharedPreferences sharedPreferences;
	private int isPass;
	private String reason;
	private String carecard="";
	private TextView tv_reason;
	private MyBroadcastReceiver receiver;
	private List<Bitmap> bitmapList=new ArrayList<>();
	private List<String> qiniuUrl=new ArrayList<>();
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 5:
					certification();
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_certification_info);
		MyApplication.getInstance().addActivities(this);
		GlobleController.getInstance().addMyListener(this);
		sharedPreferences=getSharedPreferences(Consts.USER_FILE_NAME, MODE_PRIVATE);
		if(sp == null){
			sp = MyApplication.getSharedPref();
			userId=sp.getInt(Consts.USER_ID, -1);
		}
		checkSDCard();
		registerBoradcastReceiver();
		initTitleBar() ;
		setViews();
		getInfo();
	}

	private void initTitleBar() {
		initActionBar(CertificationInfoActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "认证信息", null, 0);

	}
	private void setViews() {
		btn_certification=(Button)findViewById(R.id.btn_certification);
		tv_reason=(TextView)findViewById(R.id.tv_reason);
		imageView[0]=(ImageView)findViewById(R.id.identity_front);
		imageView[1]=(ImageView)findViewById(R.id.identity_back);
		imageView[2]=(ImageView)findViewById(R.id.identity_zige);

		imageTips[0]=(ImageView)findViewById(R.id.iv_tip1);
		imageTips[1]=(ImageView)findViewById(R.id.iv_tip2);
		imageTips[2]=(ImageView)findViewById(R.id.iv_tip3);

		//showWhat();
	}


	private void showWhat(){
		if(cardFront.length()>0&&isPass>0){
			Glide.with(this).load(cardFront).into(imageView[0]);
		}else{
			imageView[0].setImageResource(R.mipmap.uploadpicturesmall);
		}

		if(cardBack.length()>0&&isPass>0){
			Glide.with(this).load(cardBack).into(imageView[1]);
		}else{
			imageView[1].setImageResource(R.mipmap.uploadpicturesmall);
		}

		if(certificate.length()>0&&isPass>0){
			Glide.with(this).load(certificate).into(imageView[2]);
		}else{
			imageView[2].setImageResource(R.mipmap.certificate);
		}

		if(cardFront.equals("")&&cardBack.equals("")){
			btn_certification.setVisibility(View.VISIBLE);
			for(int i=0;i<imageTips.length;i++){
				imageTips[i].setVisibility(View.GONE);
				imageView[i].setClickable(true);
			}
		}else{
			btn_certification.setVisibility(View.GONE);
			switch (verifyStatus) {
				case 1:
				case 2:
					switch (isPass){
						case 0:
							for(int i=0;i<imageTips.length;i++){
								imageTips[i].setVisibility(View.GONE);
								imageView[i].setClickable(true);
							}
							btn_certification.setVisibility(View.VISIBLE);
							tv_reason.setVisibility(View.GONE);
							break;
						case 1:
							for(int i=0;i<imageTips.length;i++){
								imageTips[i].setVisibility(View.VISIBLE);
								imageTips[i].setImageResource(R.mipmap.not_pass);
								imageView[i].setClickable(true);
							}
							btn_certification.setVisibility(View.VISIBLE);
							tv_reason.setVisibility(View.VISIBLE);
							tv_reason.setText(reason);
							break;
						case 2:
							for(int i=0;i<imageTips.length;i++){
								imageTips[i].setVisibility(View.VISIBLE);
								imageTips[i].setImageResource(R.mipmap.pass);
								imageView[i].setClickable(false);
							}
							btn_certification.setVisibility(View.GONE);
							tv_reason.setVisibility(View.GONE);
							break;
						case 3:
							for(int i=0;i<imageTips.length;i++){
								imageTips[i].setVisibility(View.VISIBLE);
								imageTips[i].setImageResource(R.mipmap.sh);
								imageView[i].setClickable(false);
							}
							btn_certification.setVisibility(View.GONE);
							tv_reason.setVisibility(View.GONE);
							break;
					}

					break;
				case 3:
					for(int i=0;i<imageTips.length;i++){
						imageTips[i].setVisibility(View.VISIBLE);
						imageTips[i].setImageResource(R.mipmap.not_pass);
						imageView[i].setClickable(false);
					}
					btn_certification.setVisibility(View.GONE);
					break;
			}

		}

	}
	/**
	 * 实名认证
	 */
	public void certification(){
		if(IFront.isEmpty()){
			Tools.toast(this, "请上传身份证正面照!");
			return;
		}
		if(Iback.isEmpty()){
			Tools.toast(this, "请上传身份证反面照!");
			return;
		}

		loadingDialog=new LoadingDialog(this, "正在上传资料...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.UPDATESMRZ);
		params.put("nursing", userId);
		params.put("Idcardpositive", qiniuUrl.get(0));//身份证正面
		params.put("Idcardreverse", qiniuUrl.get(1));//身份证反面
		if(qiniuUrl.size()>2){
			carecard=qiniuUrl.get(2);
		}else{
			carecard="";
		}
		params.put("carecard", carecard);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(CertificationInfoActivity.this, "上传成功,等待管理员审核！");
							String Idcardpositive=response.getString("Idcardpositive");
							String Idcardreverse=response.getString("Idcardreverse");
							String carecard=response.getString("carecard");

							if(Idcardpositive.length()>0){
								ImageLoader.getInstance().displayImage(Idcardpositive, imageView[0]);
							}else{
								imageView[0].setImageResource(R.mipmap.uploadpicturesmall);
							}

							if(Idcardreverse.length()>0){
								ImageLoader.getInstance().displayImage(Idcardreverse, imageView[1]);
							}else{
								imageView[1].setImageResource(R.mipmap.uploadpicturesmall);
							}

							if(carecard.length()>0){
								ImageLoader.getInstance().displayImage(carecard, imageView[2]);
							}else{
								imageView[2].setImageResource(R.mipmap.certificate);
							}

							for(int i=0;i<imageTips.length;i++){
								imageTips[i].setVisibility(View.VISIBLE);
								imageTips[i].setImageResource(R.mipmap.sh);
								imageView[i].setClickable(false);
							}
							btn_certification.setVisibility(View.GONE);
							loadingDialog.dismiss();
							break;
						default:
							loadingDialog.dismiss();
							Tools.toast(CertificationInfoActivity.this, response.getString("state_msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	/**
	 * 获取信息
	 */
	public void getInfo(){
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
							isPass=obj.getInt("audit");
							reason=obj.getString("mess");
							verifyStatus=obj.getInt("status");
							cardFront=obj.getString("Idcardpositive");
							cardBack=obj.getString("Idcardreverse");
							certificate=obj.getString("carecard");
							showWhat();
							break;
						default:
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.identity_front:
				imageTips[0].setVisibility(View.GONE);
				tv_reason.setVisibility(View.GONE);
				InitPhoto();
				index=0;
				break;
			case R.id.identity_back:
				imageTips[1].setVisibility(View.GONE);
				InitPhoto();
				index=1;
				break;
			case R.id.identity_zige:
				imageTips[2].setVisibility(View.GONE);
				InitPhoto();
				index=2;
				break;
			case R.id.btn_certification:
				uploadToken();
				break;
		}
	}

	/**
	 * 获取七牛云上传凭证
	 */
	public void uploadToken(){
		RequestParams params=new RequestParams();
		params.put("code",Consts.UPLOAD_TOKEN);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					String token=response.getString("token");
					for(int i=0;i<bitmapList.size();i++){
						qiNiuYunUpload( bitmapList.get(i),token);
					}
					//qiNiuYunUpload(bitmap,token);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}

	private void qiNiuYunUpload(Bitmap bitmap,String token) {
		MyApplication.getInstance().getQiNiuManager().put(Tools.bitmap2Byte(bitmap),
				Tools.getTimeStamp()+".jpg", token,
				new UpCompletionHandler() {
					@Override
					public void complete(String key, ResponseInfo info, JSONObject response) {
						try {
							boolean ok=info.isOK();
							//Tools.toast(CertificationInfoActivity.this,"是否上传成功:"+ok);
							//Tools.toast(CertificationInfoActivity.this,"response="+response.toString());
							String url= Consts.QINIUYUN+response.getString("key");
							qiniuUrl.add(url);
							if(qiniuUrl.size()==bitmapList.size()){
								handler.sendEmptyMessage(5);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},new UploadOptions(null, "test-type", true, null, null));
	}

/*************************************图像上传开始*************************************/
	/**
	 * 上传照片
	 */
	public void InitPhoto() {
		pop_photo = new PopupWindow(CertificationInfoActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		ll_popup_photo = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop_photo.setWidth(LayoutParams.MATCH_PARENT);
		pop_photo.setHeight(LayoutParams.WRAP_CONTENT);
		pop_photo.setBackgroundDrawable(new BitmapDrawable());
		pop_photo.setFocusable(true);
		pop_photo.setOutsideTouchable(true);
		pop_photo.setContentView(view);

		parent_photo = (RelativeLayout) view.findViewById(R.id.parent);
		bt_carema = (Button) view.findViewById(R.id.item_popupwindows_camera);
		bt_photo = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		bt_cancel_photo = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		ll_popup_photo.startAnimation(AnimationUtils
				.loadAnimation(CertificationInfoActivity.this,R.anim.activity_translate_in));
		pop_photo.showAtLocation(ll_popup_photo, Gravity.BOTTOM, 0, 0);

		parent_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop_photo.dismiss();
				ll_popup_photo.clearAnimation();
			}
		});
		// 点击相机拍照功能启动照相机界面
		bt_carema.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startCapture();
				pop_photo.dismiss();
				ll_popup_photo.clearAnimation();
			}

		});
		// 点击相机相册功能启动相册界面
		bt_photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mIsKitKat) {
					selectImageUriAfterKikat();
				} else {
					cropImageUri();
				}
				pop_photo.dismiss();
				ll_popup_photo.clearAnimation();
			}
		});
		bt_cancel_photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop_photo.dismiss();
				ll_popup_photo.clearAnimation();
			}
		});
	}

	private void checkSDCard(){
		File directory = new File(ACCOUNT_DIR);
		File imagepath = new File(IMGPATH);
		if (!directory.exists()) {
			Log.i("zou", "directory.mkdir()");
			directory.mkdir();
		}
		if (!imagepath.exists()) {
			Log.i("zou", "imagepath.mkdir()");
			imagepath.mkdir();
		}

		fileone = new File(IMGPATH, IMAGE_FILE_NAME);
		filetwo = new File(IMGPATH, TMP_IMAGE_FILE_NAME);

		try {
			if (!fileone.exists() && !filetwo.exists()) {
				fileone.createNewFile();
				filetwo.createNewFile();
			}
		} catch (Exception e) {
		}

	}


	/** <br>功能简述:拍照
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void startCapture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
		startActivityForResult(intent, TAKE_A_PICTURE);
	}

	/** <br>功能简述:裁剪图片方法实现---------------------- 相册
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void cropImageUri() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, SELECT_A_PICTURE);
	}


	/**
	 *  <br>功能简述:4.4以上裁剪图片方法实现---------------------- 相册
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void selectImageUriAfterKikat() {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		/*Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");*/
		startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
	}

	/**
	 * <br>功能简述:裁剪图片方法实现----------------------相机
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param uri
	 */
	private void cameraCropImageUri(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		//		if (mIsKitKat) {
		//			intent.putExtra("return-data", true);
		//			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		//		} else {
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		//		}
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, SET_PICTURE);
	}

	/**
	 * <br>功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param uri
	 */
	private void cropImageUriAfterKikat(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		//		intent.putExtra("return-data", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
	}

	/**
	 * <br>功能简述:将Uri转为bitmap
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param uri
	 * @return
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 结果处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bitmap photo=null;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SELECT_A_PICTURE://android4.4以下版本（相册）
					photo = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH,TMP_IMAGE_FILE_NAME)));
					imageView[index].setImageBitmap(photo);
					if(index==0){
						bitmapList.add(photo);
						IFront=Tools.BitmapToString(photo);
					}else if(index==1){
						bitmapList.add(photo);
						Iback=Tools.BitmapToString(photo);
					}else if(index==2){
						bitmapList.add(photo);
						Icert=Tools.BitmapToString(photo);
					}
					break;
				case SELECET_A_PICTURE_AFTER_KIKAT://android4.4以上版本 裁剪图片（相册）
					mAlbumPicturePath = ImageUtil.getPath(getApplicationContext(), data.getData());
					cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
					break;
				case SET_ALBUM_PICTURE_KITKAT://android4.4以上版本 裁剪图片（相机）
					photo = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
					imageView[index].setImageBitmap(photo);
					if(index==0){
						bitmapList.add(photo);
						IFront=Tools.BitmapToString(photo);
					}else if(index==1){
						bitmapList.add(photo);
						Iback=Tools.BitmapToString(photo);
					}else if(index==2){
						bitmapList.add(photo);
						Icert=Tools.BitmapToString(photo);
					}
					break;
				case TAKE_A_PICTURE://拍照
					cameraCropImageUri(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
					break;
				case SET_PICTURE://相机裁剪图片
					photo = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
					imageView[index].setImageBitmap(photo);
					if(index==0){
						bitmapList.add(photo);
						IFront=Tools.BitmapToString(photo);
					}else if(index==1){
						bitmapList.add(photo);
						Iback=Tools.BitmapToString(photo);
					}else if(index==2){
						bitmapList.add(photo);
						Icert=Tools.BitmapToString(photo);
					}
					break;
			}
		}else if(resultCode==RESULT_CANCELED){
			Tools.toast(this, "取消设置图像");
		}
	}
/***********************************图像上传结束***************************************/


	/**
	 * 创建一个广播接收参数
	 * */
	class  MyBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			getInfo();
		}

	}


	/**
	 * 注册广播
	 * */
	private void registerBoradcastReceiver() {
		receiver=new MyBroadcastReceiver();
		IntentFilter myIntentFilter1 = new IntentFilter();
		myIntentFilter1.addAction(Consts.NO_PASS);
		myIntentFilter1.addAction(Consts.PASS);
		registerReceiver(receiver, myIntentFilter1);
	}

	/**
	 * 销毁广播
	 * */
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
