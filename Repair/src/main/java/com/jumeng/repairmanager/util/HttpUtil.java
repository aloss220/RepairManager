package com.jumeng.repairmanager.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 服务器 链接请求工具类
 * @author he
 *
 */
public class HttpUtil {
	
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
	static {
		client.setTimeout(10000); // 设置链接超时，如果不设置，默认为10s
	}

	public static void get(String interfaceMethod, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
	{
		client.get(getRequestUrlString(interfaceMethod), res);
	}


	public static void get(RequestParams params,JsonHttpResponseHandler res) // url里面带参数
	{
		client.get(getRequestUrlString(), params, res);
	}

	public static void post(String interfaceMethod, RequestParams params,
			AsyncHttpResponseHandler res) // url里面带参数
	{
		client.post(getRequestUrlString(interfaceMethod), params, res);
	}


	public static void get(String url, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
	{
		client.get(url, res);
	}

	public static void post(RequestParams params,
			JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
	{
		client.post(getRequestUrlString(), params, res);
	}

	public static void get(String interfaceMethod, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(getRequestUrlString(interfaceMethod), bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
	/**
	 * 获取请求的服务器url
	 * @param interfaceMethod 请求的接口方法
	 */
	public static String getRequestUrlString(String interfaceMethod){
		return interfaceMethod;
	}
	public static String getRequestUrlString(){
		return Consts.BASE;
	}
}
