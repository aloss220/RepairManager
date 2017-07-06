package com.jumeng.repairmanager.adapter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.bean.StoreOrderOut;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StoreOrderOutAdapter extends BaseAdapter{
	private Context context;
	private List<StoreOrderOut> list;
	private LoadingDialog loadingDialog;
	public static  Handler h=new Handler();

	public StoreOrderOutAdapter(Context context, List<StoreOrderOut> list) {
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
			convertView=View.inflate(context,R.layout.lv_store_list_item_out, null);
			vh.tv_order_no=(TextView)convertView.findViewById(R.id.tv_order_no);
			vh.tv_order_status=(TextView)convertView.findViewById(R.id.tv_order_status);
			vh.tv_count=(TextView)convertView.findViewById(R.id.tv_count);
			vh.tv_money=(TextView)convertView.findViewById(R.id.tv_money);
			vh.listView=(ListView)convertView.findViewById(R.id.listView);
			vh.btn_receiver=(Button)convertView.findViewById(R.id.btn_receiver);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		switch (list.get(pos).getStatus()) {
		case 1:
			vh.tv_order_status.setText("未发货");
			vh.btn_receiver.setVisibility(View.GONE);
			break;
		case 2:
			vh.tv_order_status.setText("已发货");
			vh.btn_receiver.setVisibility(View.VISIBLE);
			break;
		case 3:
			vh.tv_order_status.setText("已收货");
			vh.btn_receiver.setVisibility(View.GONE);
			break;

		}

		vh.tv_order_no.setText("订单号："+list.get(pos).getOrderNum());
		//vh.tv_count.setText("共"+list.get(pos).getCount()+"件商品   合计：");
		vh.tv_money.setText("合计:￥"+list.get(pos).getPrice());
		vh.listView.setAdapter(new StoreOrderInAdapter(context, list.get(pos).getStoreOrderIn()));
		setListViewHeightBasedOnChildren(vh.listView);
		
		
		vh.btn_receiver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//receiverProduct(list.get(pos).getOrderId());
			}
		});
		
		return convertView;
	}

	
	/**
	 * 确认收货
	 */
	/*private void receiverProduct(int productId){
		loadingDialog=new LoadingDialog(context, "正在提交...");
		loadingDialog.show();
		
		RequestParams params=new RequestParams();
		params.put("porderid", productId);
		HttpUtil.post(Consts.WORKER+Consts.CONFIRMGOODS, params, new MyJsonHttpResponseHandler(context){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						Tools.toast(context, "已收货！");
						h.sendEmptyMessage(1);
						
						break;
					default:
						loadingDialog.dismiss();
						//Tools.toast(MyCommentActivity.this, response.getString("msg"));
						break;
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
	}*/

	
	
	/**
	 * 根据ListView的子项目重新计算ListView的高度，然后把高度再作为LayoutParams设置给ListView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) { 
		StoreOrderInAdapter listAdapter =(StoreOrderInAdapter) listView.getAdapter(); 
		if (listAdapter == null) { 
			// pre-condition 
			return; 
		} 
		int totalHeight = 0; 
		for (int i = 0; i < listAdapter.getCount(); i++) { 
			View listItem = listAdapter.getView(i, null, listView); 
			listItem.measure(0, 0); 
			totalHeight += listItem.getMeasuredHeight(); 
		} 
		ViewGroup.LayoutParams params = listView.getLayoutParams(); 
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
		listView.setLayoutParams(params); 
	} 

	class ViewHolder {
		private TextView tv_order_no;
		private TextView tv_order_status;
		private TextView tv_count;
		private TextView tv_money;
		private ListView listView;
		private Button btn_receiver;

	}

}
