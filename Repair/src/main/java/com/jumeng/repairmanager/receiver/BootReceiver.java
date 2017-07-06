package com.jumeng.repairmanager.receiver;

import com.jumeng.repairmanager.service.LocationService;
import com.jumeng.repairmanager.util.Consts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Consts.RESTART_SERVICE)) {
			//TODO
			//在这里写重新启动service的相关操作
			Intent intent1=new Intent();
			intent1.setClass(context, LocationService.class);
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(intent1);	
		}

	}

}
