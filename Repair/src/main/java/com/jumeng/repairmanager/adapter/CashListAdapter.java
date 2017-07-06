package com.jumeng.repairmanager.adapter;

import java.util.List;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.CashList;
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

public class CashListAdapter extends BaseAdapter{
	private Context context;
	private List<CashList> list;

	public CashListAdapter(Context context, List<CashList> list) {
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
			convertView=View.inflate(context,R.layout.lv_get_cash_list_item, null);
			vh.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			vh.tv_money=(TextView)convertView.findViewById(R.id.tv_money);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.tv_time.setText(list.get(pos).getTime());
		vh.tv_money.setText("-"+list.get(pos).getMoney());

		return convertView;
	}


	class ViewHolder {
		private TextView tv_time;
		private TextView tv_money;

	}

}
