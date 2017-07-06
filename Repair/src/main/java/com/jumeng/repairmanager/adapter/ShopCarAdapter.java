package com.jumeng.repairmanager.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.ShopCarList;
import com.jumeng.repairmanager.util.ImageLoaderOptionUtil;
import com.jumeng.repairmanager.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopCarAdapter extends BaseAdapter{
	private Context context;
	private List<ShopCarList> list;
	private boolean mIsShowSelectImageView;
	private Map<Integer, Boolean> isSelected=new HashMap<>();
	private boolean selectAll;
	private int count;
	private LogUtil log=LogUtil.rLog();
	private Map<Integer, Integer> goods=MyApplication.getInstance().getGoods();
	
	
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;
	

	public ShopCarAdapter(Context context, List<ShopCarList> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public void setCheckInterface(CheckInterface checkInterface)
	{
		this.checkInterface = checkInterface;
	}

	public void setModifyCountInterface(ModifyCountInterface modifyCountInterface)
	{
		this.modifyCountInterface = modifyCountInterface;
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
			convertView=View.inflate(context,R.layout.lv_shop_car_list_item, null);
			vh.tv_info=(TextView)convertView.findViewById(R.id.tv_info);
			vh.tv_count=(TextView)convertView.findViewById(R.id.tv_count);
			vh.iv_icon=(ImageView)convertView.findViewById(R.id.iv_icon);
			vh.check_box=(CheckBox)convertView.findViewById(R.id.cb_select);
			vh.tv_other=(TextView)convertView.findViewById(R.id.tv_other);
			vh.tv_money=(TextView)convertView.findViewById(R.id.tv_money);
			vh.btn_left=(Button)convertView.findViewById(R.id.btn_left);
			vh.btn_right=(Button)convertView.findViewById(R.id.btn_right);
			
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		
		vh.tv_count.setText(list.get(pos).getCount()+"");
		vh.tv_info.setText(list.get(pos).getName());
		//vh.tv_other.setText(list.get(pos).getOther());
		vh.tv_money.setText("￥"+list.get(pos).getPrice());
		ImageLoader.getInstance().displayImage(list.get(pos).getImg(), vh.iv_icon,ImageLoaderOptionUtil.getImageDisplayOption("Product"));
		vh.check_box.setChecked(list.get(pos).isChoosed());
		vh.check_box.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				list.get(pos).setChoosed(((CheckBox) v).isChecked());
				vh.check_box.setChecked(((CheckBox) v).isChecked());
				checkInterface.checkChild(pos, ((CheckBox) v).isChecked());// 暴露子选接口
			}
		});
		
		vh.btn_right.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				modifyCountInterface.doIncrease(pos, vh.tv_count, vh.check_box.isChecked());// 暴露增加接口
			}
		});
		vh.btn_left.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				modifyCountInterface.doDecrease(pos, vh.tv_count, vh.check_box.isChecked());// 暴露删减接口
			}
		});

		
		
		return convertView;
	}


	class ViewHolder {
		private TextView tv_info;
		private TextView tv_count;
		private TextView tv_other;
		private TextView tv_money;
		private ImageView iv_icon;
		private CheckBox check_box;
		private Button btn_left;
		private Button btn_right;

	}
	
	
	 /**
		 * 复选框接口
		 * 
		 * 
		 */
		public interface CheckInterface
		{

			/**
			 * 选框状态改变时触发的事件
			 * 
			 * @param isChecked 元素选中与否 
			 *           
			 */
            void checkChild(int position, boolean isChecked);
		}

		/**
		 * 改变数量的接口
		 * 
		 * 
		 */
		public interface ModifyCountInterface
		{
			/**
			 * 增加操作
			 * @param showCountView
			 *            用于展示变化后数量的View
			 * @param isChecked
			 *            子元素选中与否
			 */
            void doIncrease(int position, View showCountView, boolean isChecked);

			/**
			 * 删减操作
			 * 
			 *            元素位置
			 * @param showCountView
			 *            用于展示变化后数量的View
			 * @param isChecked
			 *            子元素选中与否
			 */
            void doDecrease(int position, View showCountView, boolean isChecked);
		}

}
