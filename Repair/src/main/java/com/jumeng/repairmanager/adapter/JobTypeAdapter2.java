package com.jumeng.repairmanager.adapter;

import java.util.List;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.JobType;
import com.jumeng.repairmanager.util.Tools;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class JobTypeAdapter2 extends BaseAdapter{
	private Context context;
	private List<JobType> list;
	private checkInterface checkInterface;

	public JobTypeAdapter2(Context context, List<JobType> list) {
		super();
		this.context = context;
		this.list = list;
	}
	public void setCheckInterface(checkInterface checkInterface){
		this.checkInterface=checkInterface;
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
			convertView=View.inflate(context,R.layout.gv_lable_item2, null);
			vh.cb_lable=(CheckBox)convertView.findViewById(R.id.cb_lable);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.cb_lable.setText(list.get(pos).getName());
		
		
		vh.cb_lable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				list.get(pos).setChoose(((CheckBox) v).isChecked());
				vh.cb_lable.setChecked(((CheckBox) v).isChecked());
				checkInterface.check();// 暴露子选接口
				/*String jobIds=MyApplication.getInstance().getSelectJob();
				if(jobIds!=null){
					if(jobIds.split(",").length>2){
						Tools.toast(context, "最多只能选择三个！");
					}else{
						list.get(pos).setChoose(((CheckBox) v).isChecked());
						vh.cb_lable.setChecked(((CheckBox) v).isChecked());
						//checkInterface.check();// 暴露子选接口
					}
				}else{
					list.get(pos).setChoose(((CheckBox) v).isChecked());
					vh.cb_lable.setChecked(((CheckBox) v).isChecked());
				}
					
				checkInterface.check();// 暴露子选接口
*/			}
		});
		return convertView;
	}


	class ViewHolder {
		private CheckBox cb_lable;

	}
	
	
	
	
	public interface checkInterface{
		
		void check();
	}

}
