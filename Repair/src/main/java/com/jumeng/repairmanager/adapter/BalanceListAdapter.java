package com.jumeng.repairmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.BalanceList;

import java.util.List;

import static com.jumeng.repairmanager.R.id.tv_status;


public class BalanceListAdapter extends BaseAdapter{
	private Context context;
	private List<BalanceList> list;

	public BalanceListAdapter(Context context, List<BalanceList> list) {
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
			convertView=View.inflate(context,R.layout.lv_balance_list_item, null);
			vh.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			vh.tv_typename=(TextView)convertView.findViewById(R.id.tv_time_name);
			vh.tv_money=(TextView)convertView.findViewById(R.id.tv_money);
			vh.tv_status=(TextView)convertView.findViewById(tv_status);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.tv_time.setText(list.get(pos).getTime());

		switch (list.get(pos).getType()){
			case 1:
				vh.tv_typename.setText("提现");
				vh.tv_money.setText("-"+list.get(pos).getMoney());
				if(list.get(pos).getStatus()==0){
					vh.tv_status.setVisibility(View.VISIBLE);
					vh.tv_status.setText("(审核中)");
				}else{
					vh.tv_status.setVisibility(View.GONE);
				}
				break;
			case 2:
				vh.tv_typename.setText("收入");
				vh.tv_money.setText("+"+list.get(pos).getMoney());
				break;
		}

		return convertView;
	}


	class ViewHolder {
		private TextView tv_time;
		private TextView tv_money;
		private TextView tv_typename;
		private TextView tv_status;

	}

}
