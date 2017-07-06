package com.jumeng.repairmanager.util;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MyJsonHttpResponseHandler extends JsonHttpResponseHandler {

	private Context context;
	//public static final String ERROR_MESSAGE = "网络异常，请检查你的网络！";
	public static final String ERROR_MESSAGE = "网络繁忙，请稍后重试!";
	
	public MyJsonHttpResponseHandler(Context context) {
		super();
		this.context = context;
	}


	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseString, Throwable throwable) {
		//super.onFailure(statusCode, headers, responseString, throwable);
		//Tools.toast(context, ERROR_MESSAGE);
		Log.e("Error", "Error",throwable);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONArray errorResponse) {
		// super.onFailure(statusCode, headers, throwable, errorResponse);
		//Tools.toast(context, ERROR_MESSAGE);
		Log.e("Error", ERROR_MESSAGE, throwable);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONObject errorResponse) {
		// super.onFailure(statusCode, headers, throwable, errorResponse);
		//Tools.toast(context, ERROR_MESSAGE);
		Log.e("Error", ERROR_MESSAGE, throwable);
	}

}
