package com.jumeng.repairmanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.jumeng.repairmanager.util.Consts;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyApplication extends Application implements AMapLocationListener{
	private static SharedPreferences sp;
	private static UploadManager uploadManager;
	private static MyApplication mInstance=null;
	private MyApplication appContext;

	private String province;
	private String city;
	private String discrict;
	private int orderCount;
	private int isOrder;
	private double balance;
	private double totalIncome;
	private double totalFee;

	private int verifyStatus;
	private int isPass;
	private String reason;
	private String cardFront;
	private String cardBack;
	private String certificate;

	private String versionName;
	private String downloadUrl;
	private String updateInfo;
	private int versionCode;
	private String selectJob;
	private double longitude;
	private double latitude;
	private int districtId;
	private String url;



	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	private Map<Integer, Integer> works=new HashMap<>();

	private Map<Integer, Integer> goods=new HashMap<>();

	private static LinkedList<Activity> activities=new LinkedList<>();

	@Override
	public void onCreate() {
		super.onCreate();
		//PushManager.getInstance().initialize(this.getApplicationContext());

		initImageLoader(getApplicationContext());
		activate();
		initHxChat();
		if(sp==null){
			sp=getSharedPreferences(Consts.USER_FILE_NAME, MODE_PRIVATE);
		}


		//七牛云相关配置
		//Zone.zone0:华东
		//Zone.zone1:华北
		//Zone.zone2:华南
		//———http上传，自动识别上传区域——
		//Zone.httpAutoZone
		//———https上传，自动识别上传区域——
		Configuration config = new Configuration.Builder()
				.chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
				.putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
				.connectTimeout(10) // 链接超时。默认 10秒
				.responseTimeout(60) // 服务器响应超时。默认 60秒
				.zone(Zone.httpAutoZone)
				.build();

		// 重用uploadManager。一般地，只需要创建一个uploadManager对象
		if(uploadManager==null){
			uploadManager=new UploadManager(config);
		}
	}

	/**
	 * 获取MyApplication实例
	 */
	public static MyApplication getInstance() {
		if(mInstance==null){
			mInstance=new MyApplication();
		}
		return mInstance;
	}

	/**
	 * 获取SharedPreferences实例
	 */
	public static SharedPreferences getSharedPref(){
		return sp;
	}
	/**
	 * 获取七牛云管理对象
	 */
	public static UploadManager getQiNiuManager(){
		return uploadManager;
	}

	@SuppressWarnings("deprecation")
	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)//加载图片的线程数
				.denyCacheImageMultipleSizesInMemory() //解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
				.memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))
				.memoryCacheSize(2*1024*1024)
				.discCacheSize(50*1024*1024)
				.discCacheFileCount(100).discCache(new UnlimitedDiskCache(new File(Environment.getExternalStorageDirectory()+"/Ujiang/imgCache")))
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//设置磁盘缓存文件名称
				.tasksProcessingOrder(QueueProcessingType.LIFO)//设置加载显示图片队列进程
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}



	public void addActivities(Activity activity){
		if(activities.contains(activity)){
			return;
		}
		activities.add(activity);
	}

	public void finishActivities(){
		if(activities!=null&&activities.size()>0){
			for(Activity activity:activities){
				activity.finish();
			}
			//System.exit(0);
		}
	}

	private void activate() {
		mlocationClient = new AMapLocationClient(getApplicationContext());
		mLocationOption = new AMapLocationClientOption();
		//设置定位监听
		mlocationClient.setLocationListener(this);
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式  Hight_Accuracy  高精度模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		//设置定位参数
		mlocationClient.setLocationOption(mLocationOption);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		mlocationClient.startLocation();

	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		String province=aMapLocation.getProvince();
		String city = aMapLocation.getCity();
		// 区域  例如：雁塔区
		String discrict=aMapLocation.getDistrict();
		double longitude=aMapLocation.getLongitude();
		double latitude=aMapLocation.getLatitude();
		MyApplication.getInstance().setCity(city);
		MyApplication.getInstance().setDiscrict(discrict);
		MyApplication.getInstance().setLongitude(longitude);
		MyApplication.getInstance().setLatitude(latitude);
		MyApplication.getInstance().setProvince(province);

	}


	private void initHxChat() {
		EMOptions options=new EMOptions();
		options.setAcceptInvitationAlways(false);
		EMClient.getInstance().init(getApplicationContext(), options);
		EMClient.getInstance().setDebugMode(true);

		appContext = this;
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果APP启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

		if (processAppName == null ||!processAppName.equalsIgnoreCase(appContext.getPackageName())) {
			Log.e("TAG", "enter the service process!");

			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}

	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}










	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	public String getDiscrict() {
		return discrict;
	}

	public void setDiscrict(String discrict) {
		this.discrict = discrict;
	}


	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}



	public double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Map<Integer, Integer> getWorks() {
		return works;
	}

	public void setWorks(Map<Integer, Integer> works) {
		this.works = works;
	}

	public double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	public Map<Integer, Integer> getGoods() {
		return goods;
	}

	public void setGoods(Map<Integer, Integer> goods) {
		this.goods = goods;
	}

	public int getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(int verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public int getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}

	public String getCardFront() {
		return cardFront;
	}

	public void setCardFront(String cardFront) {
		this.cardFront = cardFront;
	}

	public String getCardBack() {
		return cardBack;
	}

	public void setCardBack(String cardBack) {
		this.cardBack = cardBack;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getIsPass() {
		return isPass;
	}

	public void setIsPass(int isPass) {
		this.isPass = isPass;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSelectJob() {
		return selectJob;
	}

	public void setSelectJob(String selectJob) {
		this.selectJob = selectJob;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
