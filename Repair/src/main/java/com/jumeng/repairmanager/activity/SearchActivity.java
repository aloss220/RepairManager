package com.jumeng.repairmanager.activity;

import java.util.ArrayList;
import java.util.List;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.BalanceListAdapter;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.listview.PullToRefreshLayout;
import com.jumeng.repairmanager.listview.PullableListView;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class SearchActivity  extends BaseActivity  implements TextWatcher{
	private static final String TAG = SearchActivity.class.getSimpleName();
	private TextView mAllDetail;
	private TextView mAllMoney;
	private int page = 1;
	private LoadingDialog mDialog;
	private PullToRefreshLayout layout;
	private PullableListView listView;
	private BalanceListAdapter adapter;
	private List<BalanceList> list=new ArrayList<>();
	private EditText et_search;
	private ImageView iv_delete;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		MyApplication.getInstance().addActivities(this);
		setViews();
	}



	private void setViews() {
		et_search=(EditText)findViewById(R.id.et_search);
		et_search.addTextChangedListener(this);
		iv_delete=(ImageView)findViewById(R.id.iv_delete);
	}





	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_delete:
			et_search.setText(null);
			break;
		case R.id.btn_search:
			Tools.StartActivitytoOther(this, SearchBusinessActivity.class);
			break;
		}

	}



	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(TextUtils.isEmpty(et_search.getText())){
			iv_delete.setVisibility(View.GONE);
		}else{
			iv_delete.setVisibility(View.VISIBLE);
		}
		
	}



	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}



}
