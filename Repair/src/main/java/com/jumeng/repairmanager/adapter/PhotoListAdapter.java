package com.jumeng.repairmanager.adapter;

import com.bumptech.glide.Glide;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.activity.ImagePagerActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PhotoListAdapter extends BaseAdapter{
	private Context context;
	private String [] imgs;

	public PhotoListAdapter(Context context, String [] imgs) {
		super();
		this.context = context;
		this.imgs = imgs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgs.length;
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return imgs[pos];
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
			convertView=View.inflate(context,R.layout.gv_photo_list_item, null);
			vh.iv_icon=(ImageView)convertView.findViewById(R.id.iv_icon);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		
		
		Glide.with(context).load(imgs[pos]).placeholder(R.mipmap.image_loader_err).into(vh.iv_icon);
		vh.iv_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(imgs!=null){
					imageBrower( pos, imgs);
				}
			}
		});
		return convertView;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 */
	protected void imageBrower(int position, String [] imgs) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		Bundle bundle=new Bundle();
		intent.putExtra("pos", position);
		bundle.putStringArray("imgs", imgs);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	

	class ViewHolder {
		private ImageView iv_icon;

	}

}
