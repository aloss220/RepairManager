package com.jumeng.repairmanager.util;

import com.jumeng.repairmanager.view.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * 检查网络连接
 * @author Administrator
 *
 */
public class NetworkUtil {

	public static void checkNetworkState(final Context context)
	{
		try {
			//判断有没有网
			ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
			if (activeNetworkInfo==null){
				new AlertDialog(context).builder().setTitle("当前无网络"+"\n")
				.setPositiveButton("打开网络",new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						int version=Build.VERSION.SDK_INT;
						if (version>10)
						{
							Intent intent=new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							context.startActivity(intent);
						}
					}
					
				}).setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						
					}
				}).show();
			

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
