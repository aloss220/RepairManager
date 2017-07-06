package com.jumeng.repairmanager.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.jumeng.repairmanager.MyApplication;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class QINiuUtils {

	//public static String accessKey="JhiflUyDaBJgrN3vTcGGrhuXBDWB6VUb8cg3B-Mq";
	//public static String secrectKey="WVc3GBbkCr4QOP_TOdfT6EZ6xe9GQCq8306qmDkR";
	//private static String  bucket ="ujiang";//空间名

	//我自己的测试账号
	public static String accessKey="qMOP35PIRAJhw6jqXN4c2pXK0x2iC6nUF_qiwYZc";
	public static String secrectKey="EpLH4J4HX7BN2fxQdRRx9rCu04DC4v_EAU1NCgEG";
	private static String  bucket ="shengyu";//空间名

	//上传到七牛后保存的文件名
	private static String key = "07.jpg";
	public static String token="JhiflUyDaBJgrN3vTcGGrhuXBDWB6VUb8cg3B-Mq:ADb-hgKXs-eQQNxbambJDTtcUFI=:eyJzY29wZSI6InVqaWFuZyIsImRlYWRsaW5lIjoxNDk4ODc0ODQwLCJ1cEhvc3RzIjpbImh0dHA6XC9cL3VwLnFpbml1LmNvbSIsImh0dHA6XC9cL3VwbG9hZC5xaW5pdS5jb20iLCItSCB1cC5xaW5pdS5jb20gaHR0cDpcL1wvMTgzLjEzNi4xMzkuMTYiXX0=";

	public static void updateQiNiu(byte[] data){
		//指定upToken, 强烈建议从服务端提供get请求获取, 这里为了掩饰直接指定key
		MyApplication.getInstance().getQiNiuManager().put(data, Tools.getTimeStamp()+".png", token, new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo info, JSONObject response) {
				boolean ok=info.isOK();
				try {
					String url=Consts.QINIUYUN+response.getString("key");
					MyApplication.getInstance().setUrl(url);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		},new UploadOptions(null, "test-type", true, null, null));

	}

	public static byte[] bitmap2Byte(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
}
