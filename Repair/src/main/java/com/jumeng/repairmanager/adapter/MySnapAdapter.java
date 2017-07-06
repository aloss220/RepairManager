package com.jumeng.repairmanager.adapter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.activity.OrderDetailActivity;
import com.jumeng.repairmanager.activity.OrderDetailActivity2;
import com.jumeng.repairmanager.bean.OrderList;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MySnapAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<OrderList> list;

	public MySnapAdapter(Context mContext,List<OrderList> list) {
		this.mContext = mContext;
		this.list = list;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.lv_my_snap_item, null);
			viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.createTime = (TextView) convertView.findViewById(R.id.createTime);
			viewHolder.serviceTime = (TextView) convertView.findViewById(R.id.serviceTime);
			viewHolder.btn_right=(Button)convertView.findViewById(R.id.btn_right);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_title.setText(list.get(position).getItem());
		viewHolder.createTime.setText("下单时间:"+list.get(position).getOrderTime());
		viewHolder.serviceTime.setText("预约时间:"+list.get(position).getServiceTime());

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,OrderDetailActivity2.class);
				intent.putExtra("orderId", list.get(position).getOrderId());
				mContext.startActivity(intent);
				
			}
		});
		viewHolder.btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//isCanReceiveOrder(list.get(position).getOrderId(),viewHolder.btn_right);
				robOrder(list.get(position).getOrderId());
			}
		});
		
		return convertView;
	}
	
	
	/**
	 * 判断工人是否可以接收新订单
	 * 
	 * @throws JSONException
	 */
	private void isCanReceiveOrder(final int orderId,final Button btn){
		RequestParams params = new RequestParams();
		params.put("code",Consts.GETISJIEDAN);
		params.put("workerid", MyApplication.getSharedPref().getInt(Consts.USER_ID, -1));
		HttpUtil.post(params, new MyJsonHttpResponseHandler(mContext) {
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						tipDialog("还有三个单子没做完呢，做完再来抢新单吧！");
						break;
					case 2:
						robOrder(orderId);
						break;
					}
					
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				
			}
		});
	}
	
	private void tipDialog(String tips){
		View view=View.inflate(mContext,R.layout.tips_dialog, null);
		final TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
		final TextView tv_tips=(TextView)view.findViewById(R.id.tv_tips);
		tv_title.setText("温馨提示");
		tv_tips.setText(tips);
		final Button btn_sure=(Button) view.findViewById(R.id.btn_ok);
		final AlertDialog dialog=new AlertDialog.Builder(mContext).create();
		dialog.show();
		dialog.getWindow().setContentView(view);
		WindowManager wm=((Activity)mContext).getWindowManager();
		Display d=wm.getDefaultDisplay();
		WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
		//params.height=(int)(d.getHeight()*0.4);
		params.width=(int)(d.getWidth()*0.8);
		dialog.getWindow().setAttributes(params);
		dialog.setCanceledOnTouchOutside(true);
		OnClickListener  listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_ok:
					dialog.dismiss();
					break;
				}

			}
		};
		btn_sure.setOnClickListener(listener);
	}
	
	/**
	 * 抢单
	 */
	public void robOrder(final int orderId){

		RequestParams params=new RequestParams();
		params.put("code",Consts.WORKERJIEDAN);
		params.put("orderid", orderId);
		params.put("workerid", MyApplication.getSharedPref().getInt(Consts.USER_ID, -1));
		HttpUtil.post(params, new MyJsonHttpResponseHandler(mContext){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						//loadingDialog.dismiss();
						Tools.toast(mContext, "抢单成功!");
						
						GlobleController.getInstance().notifyRobOrder();
						
						Intent intent=new Intent(mContext,OrderDetailActivity.class);
						intent.putExtra("orderId", orderId);
						intent.putExtra("status", 2);
						mContext.startActivity(intent);
						break;
					default:
						//loadingDialog.dismiss();
						Tools.toast(mContext, response.getString("state_msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	
	
	class ViewHolder{
		TextView tv_title;
		TextView createTime;
		TextView serviceTime;
		Button btn_right;
	}

	
	
	
	
}
