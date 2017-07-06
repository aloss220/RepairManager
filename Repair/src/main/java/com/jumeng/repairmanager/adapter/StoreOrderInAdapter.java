package com.jumeng.repairmanager.adapter;

import java.util.List;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.StoreOrderIn;
import com.jumeng.repairmanager.bean.StoreOrderOut;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class StoreOrderInAdapter extends BaseAdapter{
	private Context context;
	private List<StoreOrderIn> list;

	public StoreOrderInAdapter(Context context, List<StoreOrderIn> list) {
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
			convertView=View.inflate(context,R.layout.lv_store_list_item_in, null);
			vh.iv_icon=(ImageView)convertView.findViewById(R.id.iv_icon);
			vh.tv_goods_info=(TextView)convertView.findViewById(R.id.tv_goods_info);
			vh.tv_count=(TextView)convertView.findViewById(R.id.tv_goods_count);
			vh.tv_money=(TextView)convertView.findViewById(R.id.tv_goods_price);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}

		vh.tv_goods_info.setText(list.get(pos).getName());
		vh.tv_count.setText("x"+list.get(pos).getCount());
		vh.tv_money.setText("￥"+list.get(pos).getPrice());
		ImageLoader.getInstance().displayImage(list.get(pos).getImg(), vh.iv_icon,ImageLoaderOptionUtil.getImageDisplayOption("Product"));
		return convertView;
	}


	class ViewHolder {
		private ImageView iv_icon;
		private TextView tv_goods_info;
		private TextView tv_count;
		private TextView tv_money;

	}

}
