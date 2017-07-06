package com.jumeng.repairmanager.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.ImageUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Sign;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadOptions;
import com.tarena.utils.ImageCircleView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import static com.jumeng.repairmanager.MyApplication.getInstance;
import static com.jumeng.repairmanager.MyApplication.getSharedPref;
import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

/**
 * 用户信息页面
 */

public class MyInfoActivity extends BaseActivity {
	private ImageView back;
	private RelativeLayout rl1,rl2,rl3,rl5,rl7,rl8,rl9;
	private PopupWindow pop_photo = null;
	private LinearLayout ll_popup_photo;
	private RelativeLayout parent_photo;
	private Button bt_carema;
	private Button bt_photo;
	private Button bt_cancel_photo;
	private PopupWindow pop_photo2 = null;
	private LinearLayout ll_popup_photo2;
	private RelativeLayout parent_photo2;
	private Button bt_man;
	private Button bt_women;
	private Button bt_cancel;

	private ImageCircleView iv_userImg;
	private TextView edit_name;
	private TextView edit_phone;
	private TextView edit_sex;
	private TextView edit_startWork;
	private TextView edit_nagive;
	private String name;
	private String tel;
	private String startWork;
	private String nagive;
	private SharedPreferences sp;
	private int userId;
	private TextView user_code;
	private ImageView level;
	private TextView ide_info;
	private MyBroadcastReceiver receiver;
	private LoadingDialog loadingDialog;
	private RelativeLayout rl11;
	private String type="";
	private int verifyStatus;
	private String cardFront;
	private String cardBack;
	private String certificate;

	private String accountName;
	private String bank;
	private String bankCard;

	private RelativeLayout rl12;


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
	private String nickname;
	private String nativeplace;
	private String worknum;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_info);
		MyApplication.getInstance().addActivities(this);
		sp=getSharedPref();
		userId=sp.getInt(Consts.USER_ID, -1);

		GlobleController.getInstance().addMyListener(this);
		checkSDCard();
		initTitleBar();
		setViews();

		registerBoradcastReceiver();
		InitPhoto();
		InitPhoto2();
		addListener();
		addListener2();


	}

	@Override
	protected void onResume() {
		super.onResume();
		getInfo();


	}

	private void initTitleBar() {
		initActionBar(MyInfoActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "个人资料", null, 0);


	}
	private void setViews() {
		iv_userImg = (ImageCircleView) findViewById(R.id.iv_userImg);
		level = (ImageView) findViewById(R.id.level);
		edit_name = (TextView) findViewById(R.id.edit_name);
		edit_phone = (TextView) findViewById(R.id.edit_phone);
		user_code = (TextView) findViewById(R.id.user_code);
		edit_startWork = (TextView) findViewById(R.id.edit_startWork);
		edit_sex = (TextView) findViewById(R.id.edit_sex);
		ide_info = (TextView) findViewById(R.id.ide_info);
		edit_nagive = (TextView) findViewById(R.id.edit_nagive);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl1.setOnClickListener(this);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl2.setOnClickListener(this);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl3.setOnClickListener(this);
		rl5 = (RelativeLayout) findViewById(R.id.rl5);
		rl5.setOnClickListener(this);
		rl7 = (RelativeLayout) findViewById(R.id.rl7);
		rl7.setOnClickListener(this);
		rl8 = (RelativeLayout) findViewById(R.id.rl8);
		rl8.setOnClickListener(this);
		rl9 = (RelativeLayout) findViewById(R.id.rl9);
		rl9.setOnClickListener(this);
		rl11= (RelativeLayout) findViewById(R.id.rl11);
		rl11.setOnClickListener(this);
		rl12= (RelativeLayout) findViewById(R.id.rl12);
		rl12.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.rl1:
				ll_popup_photo.startAnimation(AnimationUtils.loadAnimation(MyInfoActivity.this, R.anim.activity_translate_in));
				pop_photo.showAtLocation(ll_popup_photo, Gravity.BOTTOM, 0, 0);
				break;
			case R.id.rl2:
				intent=new Intent(this, UpdateNameActivity.class);
				intent.putExtra("name",name);
				startActivity(intent);
				break;
			case R.id.rl3:
				Tools.StartActivitytoOther(this, UpdatePhoneActivity.class);
				break;
			case R.id.rl5:
				ll_popup_photo2.startAnimation(AnimationUtils.loadAnimation(MyInfoActivity.this, R.anim.activity_translate_in));
				pop_photo2.showAtLocation(ll_popup_photo2, Gravity.BOTTOM, 0, 0);
				break;
			case R.id.rl7:
				intent=new Intent(this, UpdateStartWorkActivity.class);
				intent.putExtra("worktime",worknum);
				startActivity(intent);
				break;
			case R.id.rl8:
				intent=new Intent(this, UpdateNativeActivity.class);
				intent.putExtra("nativeplace",nativeplace);
				startActivity(intent);
				break;
			case R.id.rl9:
				intent=new Intent(this, CertificationInfoActivity.class);
				startActivity(intent);
				break;
			case R.id.rl11:
				intent=new Intent(this, LableActivity.class);
				intent.putExtra("type", type);
				startActivity(intent);
				break;
			case R.id.rl12:
				intent=new Intent(this, BankCardActivity.class);
				intent.putExtra("accountName", accountName);
				intent.putExtra("bank", bank);
				intent.putExtra("bankCard", bankCard);
				startActivity(intent);
				break;


		}

	}

	/**
	 * 头像修改点击头像时弹出PopupWindow
	 */
	public void InitPhoto() {
		pop_photo = new PopupWindow(MyInfoActivity.this);
		View view = getLayoutInflater().inflate(R.layout.photo_popupwindow,
				null);
		ll_popup_photo = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop_photo.setWidth(LayoutParams.MATCH_PARENT);
		pop_photo.setHeight(LayoutParams.WRAP_CONTENT);
		pop_photo.setBackgroundDrawable(new BitmapDrawable());
		pop_photo.setFocusable(true);
		pop_photo.setOutsideTouchable(true);
		pop_photo.setContentView(view);

		parent_photo = (RelativeLayout) view.findViewById(R.id.parent);
		bt_carema = (Button) view.findViewById(R.id.btn_camera);
		bt_photo = (Button) view.findViewById(R.id.btn_album);
		bt_cancel_photo = (Button) view.findViewById(R.id.btn_cancel);
	}

	/**
	 * 修改性别时弹出PopupWindow
	 */
	public void InitPhoto2() {
		pop_photo2 = new PopupWindow(MyInfoActivity.this);
		View view = getLayoutInflater().inflate(R.layout.sex_popupwindow,
				null);
		ll_popup_photo2 = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop_photo2.setWidth(LayoutParams.MATCH_PARENT);
		pop_photo2.setHeight(LayoutParams.WRAP_CONTENT);
		pop_photo2.setBackgroundDrawable(new BitmapDrawable());
		pop_photo2.setFocusable(true);
		pop_photo2.setOutsideTouchable(true);
		pop_photo2.setContentView(view);

		parent_photo2 = (RelativeLayout) view.findViewById(R.id.parent);
		bt_man = (Button) view.findViewById(R.id.man);
		bt_women = (Button) view.findViewById(R.id.women);
		bt_cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
	}
	private void addListener() {
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
	private void addListener2() {
		parent_photo2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop_photo2.dismiss();
				ll_popup_photo2.clearAnimation();
			}
		});

		bt_man.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				edit_sex.setText("男");
				pop_photo2.dismiss();
				ll_popup_photo2.clearAnimation();
				updateSex("男");
			}

		});

		bt_women.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				edit_sex.setText("女");
				pop_photo2.dismiss();
				ll_popup_photo2.clearAnimation();
				updateSex("女");
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop_photo2.dismiss();
				ll_popup_photo2.clearAnimation();
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
	 * <br>功能简述:
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
		Bitmap bitmap=null;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SELECT_A_PICTURE://android4.4以下版本（相册）
					bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH,TMP_IMAGE_FILE_NAME)));
					iv_userImg.setImageBitmap(bitmap);
					uploadToken(bitmap);
					break;
				case SELECET_A_PICTURE_AFTER_KIKAT://android4.4以上版本 裁剪图片（相册）
					mAlbumPicturePath = ImageUtil.getPath(getApplicationContext(), data.getData());
					cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
					break;
				case SET_ALBUM_PICTURE_KITKAT://android4.4以上版本 裁剪图片（相机）
					bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
					iv_userImg.setImageBitmap(bitmap);
					uploadToken(bitmap);
					break;
				case TAKE_A_PICTURE://拍照
					cameraCropImageUri(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
					break;
				case SET_PICTURE://相机裁剪图片
					bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
					iv_userImg.setImageBitmap(bitmap);
					uploadToken(bitmap);
					break;
			}
		}else if(resultCode==RESULT_CANCELED){
			Tools.toast(this, "取消设置图像");
		}
	}

	/**
	 * 获取七牛云上传凭证
	 */
	public void uploadToken(final Bitmap bitmap){
		RequestParams params=new RequestParams();
		params.put("code",Consts.UPLOAD_TOKEN);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					String token=response.getString("token");
					qiNiuYunUpload(bitmap,token);
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
							//Tools.toast(MyInfoActivity.this,"是否上传成功:"+ok);
							//Tools.toast(MyInfoActivity.this,"response="+response.toString());
							String url= Consts.QINIUYUN+response.getString("key");
							updateIcon (url);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},new UploadOptions(null, "test-type", true, null, null));
	}


	/**
	 * 修改性别
	 */
	public void updateSex(String s){
		int sex=s.equals("男")?0:1;
		RequestParams params=new RequestParams();
		params.put("code",Consts.UODATESEX);
		params.put("workerid", userId);
		params.put("sex", sex);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(MyInfoActivity.this, "修改成功！");
							break;
						default:
							Tools.toast(MyInfoActivity.this, response.getString("state_msg"));
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
							String icon=obj.getString("icon");
							int userId=obj.getInt("id");
							int gender=obj.getInt("gender");
							int balance=obj.getInt("balance");
							int isPass=obj.getInt("audit");
							verifyStatus=obj.getInt("status");
							String reason=obj.getString("mess");
							int grade=obj.getInt("grade");
							type=obj.getString("type");
							String phone=obj.getString("phone_number");
							double totalIncome=obj.getDouble("totalrevenue");//累计收入
							int canCash=obj.getInt("ktmoney");//可提现余额

							 name=obj.getString("name");
							 nickname=obj.getString("nickname");
							 nativeplace=obj.getString("nativeplace");//籍贯
							cardFront=obj.getString("Idcardpositive");
							cardBack=obj.getString("Idcardreverse");
							certificate=obj.getString("carecard");
							int orderCount=obj.getInt("totalorder");
							 worknum=obj.getString("worknum");
							accountName=obj.getString("kaihuname");
							bank=obj.getString("kaihuhank");
							bankCard=obj.getString("kahao");
							ImageLoader.getInstance().displayImage(icon, iv_userImg,
									ImageLoaderOptionUtil.getImageDisplayOption("moren_yuan"));
							edit_name.setText(name);
							user_code.setText(nickname);
							edit_phone.setText(phone);
							edit_startWork.setText(worknum+"年");
							edit_nagive.setText(nativeplace);
							edit_sex.setText(gender==0?"男":"女");
							switch (grade) {
								case 1:
									level.setImageResource(R.mipmap.v11);
									break;
								case 2:
									level.setImageResource(R.mipmap.v22);
									break;
								case 3:
									level.setImageResource(R.mipmap.v33);
									break;
								case 4:
									level.setImageResource(R.mipmap.v44);
									break;

							}
							switch (verifyStatus) {
								case 1:
									switch (isPass){
										case 0:
											ide_info.setText("未审核");
											break;
										case 1:
											ide_info.setText("审核未通过");
											break;
										case 2:
											ide_info.setText("审核通过");
											break;
										case 3:
											ide_info.setText("审核中");
											break;
									}
									break;
								case 2:
									ide_info.setText("开启");
									break;
								case 3:
									ide_info.setText("禁用");
									break;
							}

							getInstance().setCardFront(cardFront);
							getInstance().setCardBack(cardBack);
							getInstance().setCertificate(certificate);
							getInstance().setVerifyStatus(verifyStatus);
							getInstance().setReason(reason);
							getInstance().setIsPass(isPass);

							break;
						default:
							//	loadingDialog.dismiss();
							Tools.toast(MyInfoActivity.this, response.getString("msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	/**
	 * 修改图像
	 */
	public void updateIcon (String url){
		RequestParams params=new RequestParams();
		params.put("code",Consts.UODATEICON);
		params.put("workerid", userId);
		params.put("icon", url);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							Tools.toast(MyInfoActivity.this, "修改成功！");
							String icon=response.getString("icon");
							ImageLoader.getInstance().displayImage(icon, iv_userImg);

							Intent intent=new Intent(Sign.UPDATE_PHOTO);
							intent.putExtra("image_url",icon);
							sendBroadcast(intent);
							break;
						default:
							Tools.toast(MyInfoActivity.this, response.getString("msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}


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
		myIntentFilter1.addAction(Sign.UPDATE_INFO);
		myIntentFilter1.addAction(Consts.NO_PASS);
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


	@Override
	public void pass() {
		getInfo();

	}




}
