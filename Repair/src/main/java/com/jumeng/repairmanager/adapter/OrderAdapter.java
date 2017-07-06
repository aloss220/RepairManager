package com.jumeng.repairmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.activity.OrderDetailActivity;
import com.jumeng.repairmanager.bean.OrderList;
import com.jumeng.repairmanager.view.StarBarView;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
	private Context context;
	private List<OrderList>list;
	private LayoutInflater inflater;

	public OrderAdapter(Context context, List<OrderList> list) {
		super();
		this.context = context;
		this.list = list;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflater.inflate( R.layout.lv_order_list_item, null);
			holder=new ViewHolder();
			holder.starbar=(StarBarView)convertView.findViewById(R.id.starbar);
			holder.iv_icon=(ImageView)convertView.findViewById(R.id.iv_icon);
			holder.tv_item=(TextView)convertView.findViewById(R.id.tv_item);
			holder.tv_address=(TextView)convertView.findViewById(R.id.tv_address);
			holder.tv_status=(TextView)convertView.findViewById(R.id.tv_status);
			holder.tv_order_time=(TextView)convertView.findViewById(R.id.tv_order_time);
			holder.tv_server_time=(TextView)convertView.findViewById(R.id.tv_server_time);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		setOrder(position, holder);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,OrderDetailActivity.class);
				intent.putExtra("status", list.get(position).getStatus());
				intent.putExtra("orderId", list.get(position).getOrderId());
				intent.putExtra("phone", list.get(position).getContact());
				context.startActivity(intent);
			}
		});

		return convertView;
	}


	private void setOrder(final int position, ViewHolder holder) {
		switch (list.get(position).getStatus()) {//1 待服务2已到达 3  服务中 4 待评价 5已完成
			case 2:
				holder.tv_status.setText("已接单");
				holder.tv_status.setTextColor(Color.parseColor("#008EFF"));
				break;
			case 3:
				holder.tv_status.setText("已到达");
				holder.tv_status.setTextColor(Color.parseColor("#FF6D00"));
				break;
			case 4:
				holder.tv_status.setText("待支付");
				holder.tv_status.setTextColor(Color.parseColor("#2EBC73"));
				break;
			case 5:
				holder.tv_status.setText("服务中");
				holder.tv_status.setTextColor(Color.parseColor("#FF6D00"));
				break;
			case 6:
				holder.tv_status.setText("待评价");
				holder.tv_status.setTextColor(Color.parseColor("#FF6D00"));
				break;
			case 7:
				holder.tv_status.setText("已完成");
				holder.tv_status.setTextColor(Color.parseColor("#FF6D00"));
				break;
			case 8:
				holder.tv_status.setText("已取消");
				holder.tv_status.setTextColor(Color.parseColor("#7F7F7F"));
				break;
		}

		switch (list.get(position).getStatus()){
			case 7:
				holder.starbar.setVisibility(View.VISIBLE);
				holder.tv_status.setVisibility(View.GONE);
				break;
			default:
				holder.starbar.setVisibility(View.GONE);
				holder.tv_status.setVisibility(View.VISIBLE);
				break;
		}
		Glide.with(context).load(list.get(position).getIcon()).placeholder(R.mipmap.moren_fang).into(holder.iv_icon);
		holder.tv_item.setText(list.get(position).getItem());
		holder.tv_order_time.setText(list.get(position).getOrderTime());
		holder.tv_server_time.setText("预约时间:"+list.get(position).getServiceTime());
		holder.tv_address.setText("地址:"+list.get(position).getAddress());
		holder.starbar.setStarRating(list.get(position).getStar());

	}

	class ViewHolder {
		private StarBarView starbar;
		private TextView tv_item;
		private TextView tv_status;
		private TextView tv_order_time;
		private TextView tv_server_time;
		private TextView tv_address;
		private ImageView iv_icon;
	}
}
