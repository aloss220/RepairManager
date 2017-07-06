package com.jumeng.repairmanager.service;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.activity.MainActivity;
import com.jumeng.repairmanager.activity.MainActivity.MyReceiver;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Sign;
import com.jumeng.repairmanager.util.Tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements AMapLocationListener{

	private AMapLocationClient mLocationClient;
	private AMapLocationClientOption mLocationOption;
	private int spacing =2000;//间隔时间   每多少毫秒定位和上传一次
	private MyReceiver receiver;
	private int userId;



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		getLocation();

		registerReceiver();

	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (Tools.isApplicationBroughtToBackground(getApplicationContext())) {
			/*Notification notification = new Notification(R.drawable.logo,
					"U匠工人已切换至后台运行",
	                System.currentTimeMillis());
	        pintent = PendingIntent.getService(this, 0, intent, 0);
            *//*API Level 11中，setLatestEventInfo该函数已经被替代*//*
	        notification.setLatestEventInfo(this, "U匠工人",
	                "U匠工人已切换至后台运行", pintent);

	        //让该service前台运行，避免手机休眠时系统自动杀掉该服务
	        //如果 id 为 0 ，那么状态栏的 notification 将不会显示。
	        startForeground(1, notification);*/
			NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			Notification.Builder builder = new Notification.Builder(this);
			builder.setSmallIcon(R.mipmap.logo);
			builder.setTicker("");
			builder.setContentTitle("U匠工人");
			builder.setContentText("U匠工人已切换至后台运行");
			builder.setWhen(System.currentTimeMillis()); //发送时间
			//builder.setDefaults(Notification.DEFAULT_ALL);// 设置通知的音乐、振动、LED等
			Notification notification = builder.build();
			manager.notify(1, notification);
			startForeground(1, notification);
			Log.i("TAG", "U匠运行中");
		}

		return Service.START_REDELIVER_INTENT;
	}

	/**
	 * 先进行定位操作
	 */
	private void getLocation() {
		mLocationClient = new AMapLocationClient(this);
		mLocationOption = new AMapLocationClientOption();
		//设置定位监听
		mLocationClient.setLocationListener(this);
		//设置为高精度定位模式
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(spacing);
		//设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		mLocationClient.startLocation();
	}
	/**
	 * 连续上传位置信息
	 */
	private void UploadLocation(final LatLonPoint lp) {
		NearbySearch mNearbySearch = NearbySearch.getInstance(getApplicationContext());
		mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
			@Override
			public UploadInfo OnUploadInfoCallback() {
				UploadInfo loadInfo = new UploadInfo();
				if(userId!=-1&&MyApplication.getInstance().getVerifyStatus()==2&&MyApplication.getInstance().getIsOrder()==0){
					loadInfo.setCoordType(NearbySearch.AMAP);
					loadInfo.setPoint(lp);//经纬度信息
					userId=MyApplication.getSharedPref().getInt(Consts.USER_ID, -1);
					loadInfo.setUserID(""+userId);
				}
				//Log.i("TAG", "上传");
				return loadInfo;

			}
		}, spacing);
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if(aMapLocation!=null&&aMapLocation.getErrorCode()==0){
			double latitude = aMapLocation.getLatitude();//获取维度
			double longitude = aMapLocation.getLongitude();//获取精度
			LatLonPoint lp = new LatLonPoint(latitude, longitude);
			//Log.i("TAG", "定位");
			UploadLocation(lp);
		}else{
			String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
		}

	}
	/**
	 * 清除地图上的id
	 */

	public void cleanDate() {
		NearbySearch.getInstance(getApplicationContext()).stopUploadNearbyInfoAuto();
		//获取附近实例，并设置要清楚用户的id
		NearbySearch.getInstance(getApplicationContext()).setUserID(""+userId);
		//调用异步清除用户接口
		NearbySearch.getInstance(getApplicationContext())
		.clearUserInfoAsyn();
		mLocationClient.stopLocation();
		mLocationClient.onDestroy();
		//调用销毁功能，在应用的合适生命周期需要销毁附近功能
		NearbySearch.destroy();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cleanDate() ;
		/*服务被杀死时发送广播重启服务   com.jumeng.repairmanager.receiver.BootReceiver*/
		Intent intent=new Intent(Consts.RESTART_SERVICE);
		sendBroadcast(intent);
		stopForeground(true);
	}
	private void registerReceiver (){
		IntentFilter filter=new IntentFilter();
		filter.addAction(Consts.START_WORK);
		filter.addAction(Consts.END_WORK);
		receiver=new MyReceiver();
		registerReceiver(receiver, filter);

	}
	public class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Consts.START_WORK)){
				getLocation();
			}else if(intent.getAction().equals(Consts.END_WORK)){
				cleanDate() ;
			}
		}

	}

}
