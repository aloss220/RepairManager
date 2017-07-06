package com.jumeng.repairmanager.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.JobType;
import com.jumeng.repairmanager.util.LogUtil;
import com.jumeng.repairmanager.util.Tools;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class JobTypeAdapter extends BaseAdapter{
	private Context context;
	private List<JobType> list;
	private boolean isChecked=false;
	private LogUtil log=LogUtil.rLog();
	private Map<Integer, Boolean> isSelected=new HashMap<>();
	private Map<Integer, Integer> works=MyApplication.getInstance().getWorks();

	public JobTypeAdapter(Context context, List<JobType> list) {
		this.context = context;
		this.list = list;

		initMap();
	}

	//初始化选择为false
	private void initMap(){
		for(int i=0;i<list.size();i++){
			isSelected.put(list.get(i).getJobId(), false);
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
			convertView=View.inflate(context,R.layout.lv_job_type_list_item, null);
			vh.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.checkBox.setFocusable(false);
		vh.checkBox.setClickable(false);
		vh.checkBox.setText(list.get(pos).getName());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean isChecked=!isSelected.get(list.get(pos).getJobId());
				isSelected.put(list.get(pos).getJobId(), isChecked);
				//vh.checkBox.setChecked(isChecked);
				notifyDataSetChanged();

				/*for(int i=0;i<list.size();i++){
					if(isSelected.get(pos)){
						works.put(list.get(pos).getJobId(), pos);
					}
				}*/
				if(isSelected.get(list.get(pos).getJobId())){
					works.put(list.get(pos).getJobId(), pos);
					//log.i(works);
				}else{
					works.remove(list.get(pos).getJobId());
				}
				log.i(works);
				MyApplication.getInstance().setWorks(works);
				
			}
		});
		vh.checkBox.setChecked(isSelected.get(list.get(pos).getJobId()));
		return convertView;
	}


	class ViewHolder {
		private CheckBox checkBox;

	}

}
