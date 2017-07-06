package com.jumeng.repairmanager.adapter;

import java.util.List;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.activity.BusinessDetailActivity;
import com.jumeng.repairmanager.activity.ContentActivity;
import com.jumeng.repairmanager.activity.WebActivity;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.BusinessList;
import com.jumeng.repairmanager.bean.MessageList;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.bool;
import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BusinessListAdapter extends BaseAdapter{
	private Context context;
	private List<BusinessList> list;

	public BusinessListAdapter(Context context, List<BusinessList> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=View.inflate(context,R.layout.lv_business_list_item, null);
			vh.iv_image=(ImageView)convertView.findViewById(R.id.iv_image);
			vh.tv_shopName=(TextView)convertView.findViewById(R.id.tv_shopName);
			vh.tv_distance=(TextView)convertView.findViewById(R.id.tv_distance);
			vh.tv_address=(TextView)convertView.findViewById(R.id.tv_address);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(pos).getIcon(), vh.iv_image,ImageLoaderOptionUtil.getImageDisplayOption("lunbo"));
		vh.tv_shopName.setText(list.get(pos).getShopName());
		vh.tv_distance.setText(list.get(pos).getDistance()+"km");
		vh.tv_address.setText("店铺地址："+list.get(pos).getAddress());
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,BusinessDetailActivity.class);
				intent.putExtra("shopId", list.get(pos).getShopId());
				intent.putExtra("distance", list.get(pos).getDistance());
				context.startActivity(intent);
				
			}
		});

		return convertView;
	}


	class ViewHolder {
		private ImageView iv_image;
		private TextView tv_shopName;
		private TextView tv_distance;
		private TextView tv_address;

	}

}
