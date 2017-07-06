package com.jumeng.repairmanager.service;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.activity.LoginActivity;
import com.jumeng.repairmanager.activity.SettingsActivity;
import com.jumeng.repairmanager.util.Tools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyPushService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MyApplication.getInstance().finishActivities();
		Tools.StartActivitytoOther(this, LoginActivity.class);
	}

}
