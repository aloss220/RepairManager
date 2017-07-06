package com.jumeng.repairmanager.adapter;

import java.util.List;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.GoodsList;
import com.jumeng.repairmanager.bean.ProductList;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter{
	private Context context;
	private List<ProductList> list;

	public ProductListAdapter(Context context, List<ProductList> list) {
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
			convertView=View.inflate(context,R.layout.gv_search_goods_item, null);
			vh.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			vh.iv_icon=(ImageView)convertView.findViewById(R.id.iv_icon);
			vh.tv_money=(TextView)convertView.findViewById(R.id.tv_money);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.tv_name.setText(list.get(pos).getName());
		vh.tv_money.setText("￥"+list.get(pos).getMoney());
		ImageLoader.getInstance().displayImage(list.get(pos).getImg(), vh.iv_icon,ImageLoaderOptionUtil.getImageDisplayOption("Product"));

		return convertView;
	}


	class ViewHolder {
		private TextView tv_name;
		private TextView tv_money;
		private ImageView iv_icon;

	}

}
