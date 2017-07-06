package com.jumeng.repairmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.CancelReason;
import com.jumeng.repairmanager.bean.DistrictList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelReasonAdapter extends BaseAdapter{
	private Context context;
	private List<CancelReason> list;
	private boolean isChecked=false;
	private Map<Integer, Boolean> isSelected=new HashMap<Integer, Boolean>();
	private PopupWindow pop;
	private ListView lv_sort;


	public CancelReasonAdapter(Context context, List<CancelReason> list) {
		super();
		this.context = context;
		this.list = list;

		initMap();
	}

	//初始化选择为false
	private void initMap(){
		for(int i=0;i<list.size();i++){
			isSelected.put(i, false);
		}
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
			convertView=View.inflate(context,R.layout.lv_reason_list, null);
			vh.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.checkBox.setFocusable(false);
		vh.checkBox.setClickable(false);
		vh.checkBox.setText(list.get(pos).getReason());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isChecked=!isSelected.get(pos);

				for(Integer i:isSelected.keySet()){
					isSelected.put(i, false);
				}
				//此处如果设置为 isChecked 再次点击就会取消选择   为让其选中后 再次点击不能反选  要设置成true
				isSelected.put(pos, true);
				//	isSelected.put(pos, isChecked);
				//vh.checkBox.setChecked(isChecked);
				notifyDataSetChanged();

				Intent intent=new Intent("reason");
				intent.putExtra("reason", list.get(pos).getReason());
				context.sendBroadcast(intent);

			}
		});
		vh.checkBox.setChecked(isSelected.get(pos));
		return convertView;
	}


	class ViewHolder {
		private CheckBox checkBox;

	}
}