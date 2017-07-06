package com.jumeng.repairmanager.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.DistrictList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

public class CityListAdapter2 extends BaseAdapter{
	private Context context;
	private List<DistrictList> list;
	private Map<Integer, Boolean> isSelected=new HashMap<Integer, Boolean>();

	public CityListAdapter2(Context context, List<DistrictList> list,Map<Integer, Boolean> isSelected) {
		this.context = context;
		this.list = list;
		this.isSelected = isSelected;
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
			convertView=View.inflate(context, R.layout.lv_city_item, null);
			vh.radioButton = (RadioButton)convertView.findViewById(R.id.radioButton);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.radioButton.setFocusable(false);//无此句点击item无响应的  
		vh.radioButton.setClickable(false);
		vh.radioButton.setText(list.get(pos).getDistrict());
		vh.radioButton.setChecked(isSelected.get(list.get(pos).getDistrictId()));

		return convertView;
	}


	class ViewHolder {
		private RadioButton radioButton;

	}


}
