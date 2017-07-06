package com.jumeng.repairmanager.hxchat;

import org.apache.http.Header;
import org.json.JSONObject;

import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.jumeng.repairmanager.activity.MainActivity;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class EaseUserUtils {
	private static Context context;
	private static String nickName;
	private static String userName;
	private static String icon;
	static EaseUserProfileProvider userProvider;
	public static Handler mHandler = new Handler();
	static {
		userProvider = EaseUI.getInstance().getUserProfileProvider();
	}

	/**
	 * 根据username获取相应user
	 * 
	 * @param username
	 * @return
	 */
	public static EaseUser getUserInfo(String username) {
		if (userProvider != null)
			return userProvider.getUser(username);

		return null;
	}

	/**
	 * 设置用户头像
	 * 
	 * @param username
	 */
	/*public static void setUserAvatar(final Context context, String username,
			final ImageView imageView) {
		if (imageView != null) {
			RequestParams params = new RequestParams();
			if(username.startsWith("dgw")){
				params.put("type", 0);
			}else{
				params.put("type", 1);
			}
			params.put("hxloginname", username);
			HttpUtil.post(Consts.PUBLIC+Consts.GETHXICON,params,new MyJsonHttpResponseHandler(context) {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					try {
						switch (response.getInt("state")) {
						case 1:
						    icon =response.getString("image");
						    ImageLoader.getInstance().displayImage(icon, imageView);
							break;
						case 0:
							Tools.toast(context, response.getString("msg"));
							break;
						
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}

	}*/

	/**
	 * 设置用户昵称
	 */
	/*public static void setUserNick(String username, final TextView textView) {

		if (textView != null) {
			RequestParams params = new RequestParams();
			params.put("type", 0);
			params.put("hxloginname", username);
			HttpUtil.post(Consts.PUBLIC+Consts.GETHXICON,params,new MyJsonHttpResponseHandler(context) {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					try {
						switch (response.getInt("state")) {
						case 1:
							nickName =response.getString("name");
							textView.setText(nickName);
							break;
						case 0:
							Tools.toast(context, response.getString("msg"));
							break;
						
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}
	}*/
}
