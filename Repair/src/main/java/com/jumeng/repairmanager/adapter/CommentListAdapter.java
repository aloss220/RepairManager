package com.jumeng.repairmanager.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.CommentList;
import com.jumeng.repairmanager.bean.PhotoList;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.StarBarView;

import android.R.bool;
import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter{
	private Context context;
	private List<CommentList> list;

	public CommentListAdapter(Context context, List<CommentList> list) {
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
			convertView=View.inflate(context,R.layout.lv_comment_list_item, null);
			vh.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
			vh.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			vh.tv_user=(TextView)convertView.findViewById(R.id.tv_user);
			vh.starbar=(StarBarView)convertView.findViewById(R.id.starbar);
			vh.gridView=(GridView)convertView.findViewById(R.id.gridView);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(list.get(pos).getContent().equals("") || list.get(pos).getContent()=="" || list.get(pos).getContent()==null || list.get(pos).getContent().equals(""))
		{vh.tv_content.setVisibility(View.GONE); 
			
		}else
		{
			vh.tv_content.setText(list.get(pos).getContent()); 
		}
	
		vh.tv_time.setText(list.get(pos).getTime());
		vh.tv_user.setText(list.get(pos).getOrderNum());
		vh.starbar.setStarRating(list.get(pos).getStar());
		String[] imgs = list.get(pos).getImage().split(",");
		
		if(imgs.length>0)
		{
			if(imgs[0].equals("") || imgs[0]=="" || imgs[0]==null || imgs[0].equals(""))
			{
				vh.gridView.setVisibility(View.GONE);
			}else
			{
				vh.gridView.setAdapter(new PhotoListAdapter(context, imgs));
			}
		}
		
		return convertView;
	}

	

	class ViewHolder {
		private TextView tv_content;
		private TextView tv_time;
		private TextView tv_user;
		private StarBarView starbar;
		private GridView gridView;

	}

}
