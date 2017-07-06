package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LableActivity extends BaseActivity {

	private String type;
	private GridView gridView;
	private String[] lables;
	private Button btn_save;
	private TextView tv_right;
	private GridView gridView2;
	private RelativeLayout rl_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lable);
		type=getIntent().getStringExtra("type");
		lables = type.split(",");
		initTitleBar();
		setViews();

	}




	private void initTitleBar() {
		initActionBar(LableActivity.this);
		setViewShow(0, 1, 1, 1, 0);
		setViewContent(null, R.mipmap.left_arrow, "从事工种","编辑", 0);
	}

	private void setViews() {
		gridView=(GridView)findViewById(R.id.gridView);
		if(type.equals("")){
			gridView.setVisibility(View.GONE);
		}else{
			gridView.setAdapter(new MyAdapter());
		}
	}

	public void onClick(View v){
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.tv_right:
				if(!lables.equals("")){
					Tools.StartActivitytoOther(this, SelectWorkActivity.class);
				}
				break;
		}
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lables.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=View.inflate(LableActivity.this, R.layout.gv_lable_item, null);
			TextView tv_lable = (TextView)convertView.findViewById(R.id.tv_lable);
			tv_lable.setText(lables[pos]);
			return convertView;
		}


	}

}
