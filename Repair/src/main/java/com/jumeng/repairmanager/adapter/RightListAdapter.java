package com.jumeng.repairmanager.adapter;

import java.util.List;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.MessageList;
import com.jumeng.repairmanager.bean.RightList;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Tools;

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

public class RightListAdapter extends BaseAdapter{
	private Context context;
	private List<RightList> list;

	public RightListAdapter(Context context, List<RightList> list) {
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
			convertView=View.inflate(context,R.layout.lv_right_list_item, null);
			vh.tv_title=(TextView)convertView.findViewById(R.id.tv_title);
			vh.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			vh.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.tv_title.setText(list.get(pos).getTitle());
		vh.tv_time.setText(list.get(pos).getTime());
		vh.tv_content.setText(list.get(pos).getIntro());

		return convertView;
	}


	class ViewHolder {
		private TextView tv_title;
		private TextView tv_time;
		private TextView tv_content;

	}

}
