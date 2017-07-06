package com.jumeng.repairmanager.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.CustomerList;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.CircleImageView;

import android.R.bool;
import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class CustomerListAdapter extends BaseAdapter{
	private Context context;
	private List<CustomerList> list;

	public CustomerListAdapter(Context context, List<CustomerList> list) {
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
			convertView=View.inflate(context,R.layout.lv_customer_list_item, null);
			vh.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			vh.tv_account=(TextView)convertView.findViewById(R.id.tv_account);
			vh.circleImageView=(ImageView)convertView.findViewById(R.id.circleImageView);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.tv_name.setText(list.get(pos).getName());
		vh.tv_account.setText(list.get(pos).getAccount());
		Glide.with(context).load(list.get(pos).getIcon()).placeholder(R.mipmap.moren_fang).into(vh.circleImageView);
		return convertView;
	}


	class ViewHolder {
		private TextView tv_name;
		private TextView tv_account;
		private ImageView circleImageView;

	}

}
