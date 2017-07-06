package com.jumeng.repairmanager.receiver;



import org.json.JSONException;
import org.json.JSONObject;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.activity.CertificationInfoActivity;
import com.jumeng.repairmanager.activity.LableActivity;
import com.jumeng.repairmanager.activity.LoginActivity;
import com.jumeng.repairmanager.activity.MainActivity;
import com.jumeng.repairmanager.activity.MySnapActivity;
import com.jumeng.repairmanager.activity.OrderDetailActivity;
import com.jumeng.repairmanager.activity.SettingsActivity;
import com.jumeng.repairmanager.service.MyPushService;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Tools;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

public class PushDemoReceiver extends BroadcastReceiver {

	/**
	 * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
	 */
	public static StringBuilder payloadData = new StringBuilder();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

		//
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
			case PushConsts.GET_MSG_DATA:
				// 获取透传数据
				byte[] payload = bundle.getByteArray("payload");

				String taskid = bundle.getString("taskid");
				String messageid = bundle.getString("messageid");

				// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
				boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
				System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

				if (payload != null) {
					String data = new String(payload);
					try {
						Tools.lightUpScreen(context);
						JSONObject obj=new JSONObject(data);
						String type=obj.getString("MsgType");
						int orderId=obj.getInt("OrderID");
						Intent i=null;
						if(type.equals("NewsOrder")){//新订单透传
							GlobleController.getInstance().notifyNewOrder();
							Tools.toast(context, context.getResources().getString(R.string.new_order));
						}else if(type.equals("paysuccessful")){
							GlobleController.getInstance().notifyPayOrder();
							Tools.toast(context, context.getResources().getString(R.string.pay_order));
						}else if(type.equals("quxiaoorder")){
							GlobleController.getInstance().notifycancelOrder();
							Tools.toast(context, context.getResources().getString(R.string.cancel_order));
						} else if(type.equals("Systemassignorder")){//系统指派订单信息
							GlobleController.getInstance().notifysystemForm();
							Tools.toast(context, context.getResources().getString(R.string.systemorder));
						}else if(type.equals("refreshorder")){//刷新我要接单界面
							GlobleController.getInstance().notifyNewOrder();
						}else if(type.equals("senddelivery")){// 商城订单发货成功
							Tools.toast(context, context.getResources().getString(R.string.storeOrderStart));
							Intent in=new Intent(Consts.PRODUCT_SEND);
							context.sendBroadcast(in);
						}else if(type.equals("Customerservice")){// 后台下单
							GlobleController.getInstance().notifysystemForm();
						}else if(type.equals("approvedsuccessful")){// 审核通过透传
							Tools.toast(context, context.getResources().getString(R.string.pass));
							i=new Intent(Consts.PASS);
							context.sendBroadcast(i);
							GlobleController.getInstance().notifyPass();
						}else if(type.equals("Notifihome")){// 审核通过通知
							if(!isTopActivity(context, MainActivity.class)){
								i=new Intent(context,MainActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
								context.startActivity(i);
							}
						}else if(type.equals("anotherplace")){// 异地登录
							Tools.toast(context, context.getResources().getString(R.string.unusualLogin));
							MyApplication.getSharedPref().edit().clear().commit();
							MyApplication.getInstance().finishActivities();
							if(!isTopActivity(context, LoginActivity.class)){
								i=new Intent(context,LoginActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(i);
							}
							//发送广播通知高德地图清除位置信息
							i = new Intent(Consts.END_WORK);
							context.sendBroadcast(i);
						}else if(type.equals("NotifiNewsOrder")){// 通知新订单
							//Tools.toast(context, context.getResources().getString(R.string.new_order));
							if(!isTopActivity(context, MySnapActivity.class)){
								i=new Intent(context,MySnapActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);
							}
						}else if(type.equals("Notifiorderxiangxi")){// 支付宝支付成功通知
							//Tools.toast(context, context.getResources().getString(R.string.new_order));
							if(!isTopActivity(context, OrderDetailActivity.class)){
								i=new Intent(context,OrderDetailActivity.class);
								i.putExtra("orderId", orderId);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);

								Intent i2=new Intent("pay_success");
								context.sendBroadcast(i2);
							}
						}else if(type.equals("Notifipaysuccessful")){// 微信支付成功通知
							//Tools.toast(context, context.getResources().getString(R.string.new_order));
							if(!isTopActivity(context, OrderDetailActivity.class)){
								i=new Intent(context,OrderDetailActivity.class);
								i.putExtra("orderId", orderId);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);

								Intent i2=new Intent("pay_success");
								context.sendBroadcast(i2);
							}
						}else if(type.equals("startorder")){// 后台强制开始接单
							Tools.toast(context, context.getResources().getString(R.string.start_work));
							MyApplication.getSharedPref().edit().putBoolean("isWork", true).commit();
							i=new Intent(Consts.START_WORK);
							context.sendBroadcast(i);
						}else if(type.equals("endorder")){// 后台强制结束接单
							Tools.toast(context, context.getResources().getString(R.string.end_work));
							MyApplication.getSharedPref().edit().putBoolean("isWork", false).commit();
							i=new Intent(Consts.END_WORK);
							context.sendBroadcast(i);
						}else if(type.equals("Notthrough")){// 身份审核未通过
							Tools.toast(context, context.getResources().getString(R.string.no_pass));
							i=new Intent(Consts.NO_PASS);
							context.sendBroadcast(i);
						}else if(type.equals("NotifiNotthrough")){// 身份审核未通过消息+透传
							if(!isTopActivity(context, CertificationInfoActivity.class)){
								i=new Intent(context,CertificationInfoActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
								context.startActivity(i);
							}
						}else if(type.equals("Notifiupdateservice")){// 修改工种审核通过
							Tools.toast(context, context.getResources().getString(R.string.job_pass));
							if(!isTopActivity(context, LableActivity.class)){
								i=new Intent(context,LableActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);
							}
						}else if(type.equals("Notifiworkerdeleteorder")){// 接单后取消订单通知
							if(!isTopActivity(context, OrderDetailActivity.class)){
								i=new Intent(context,OrderDetailActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);
							}

						}else if(type.equals("workerquxiao")){// 后台同意工人取消订单申请透传（两个端公用）
							if(!isTopActivity(context, OrderDetailActivity.class)){
								i=new Intent(context,OrderDetailActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);
							}

						}else if(type.equals("Notifiworkerquxiao")){// 后台同意工人取消订单申请通知透传（两个端公用）
							Tools.toast(context, context.getResources().getString(R.string.cancel_pass));
							if(!isTopActivity(context, OrderDetailActivity.class)){
								i=new Intent(context,OrderDetailActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);
							}
						}else if(type.equals("NotifiNOworkerquxiao")){// 后台拒绝工人取消订单申请通知透传
							Tools.toast(context, context.getResources().getString(R.string.cancel_nopass));
							if(!isTopActivity(context, OrderDetailActivity.class)){
								i=new Intent(context,OrderDetailActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(i);
							}
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//String type=new StringBuilder().substring(13,29);
					//String MsgType=data.getBytes("MsgType");
					Log.d("GetuiSdkDemo", "receiver payload : " + data);
					//  Subject.getInstance().notifyRefushUI(1);//1代表及时订单


					payloadData.append(data);
					payloadData.append("\n");

				}
				break;

			case PushConsts.GET_CLIENTID:
				// 获取ClientID(CID)
				// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
				String cid = bundle.getString("clientid");
				SharedPreferences sp=context.getSharedPreferences(Consts.USER_FILE_NAME, Context.MODE_PRIVATE);
				sp.edit().putString(Consts.CLIENT_ID, cid).commit();
				break;

			case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
			 * String result = bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
			 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
			 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
				break;

			default:
				break;
		}
	}


	private boolean isTopActivity(Context context,Class<?> cls){
		boolean isTop = false;
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		Log.d("TAG", "isTopActivity = " + cn.getClassName());
		if (cn.getClassName().contains(cls.getName())){
			isTop = true;
		}
		Log.d("TAG", "isTop = " + isTop);
		return isTop;
	}
}
