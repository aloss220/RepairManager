package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.adapter.JobTypeAdapter2;
import com.jumeng.repairmanager.adapter.JobTypeAdapter2.checkInterface;
import com.jumeng.repairmanager.bean.JobType;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.JsonParser;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class SelectWorkActivity extends BaseActivity implements checkInterface{

	private GridView gridView;
	private List<JobType> list=new ArrayList<>();
	private LoadingDialog loadingDialog;
	private JobTypeAdapter2 adapter;
	private String jobIds="";
	private SharedPreferences sp;
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_work);
		if(sp == null){
			sp = MyApplication.getSharedPref();
			userId=sp.getInt(Consts.USER_ID, -1);
		}
		initTitleBar();
		setViews();
		getJobList();

	}




	private void initTitleBar() {
		initActionBar(SelectWorkActivity.this);
		setViewShow(0, 1, 1, 1, 0);
		setViewContent(null, R.mipmap.left_arrow, "选择工种","确定", 0);
	}

	private void setViews() {
		gridView=(GridView)findViewById(R.id.gridView);
		adapter=new JobTypeAdapter2(this, list);
		gridView.setAdapter(adapter);
		adapter.setCheckInterface(this);
	}



	/**
	 * 获取工种列表
	 */
	private void getJobList(){

		loadingDialog=new LoadingDialog(this, "正在获取工种...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETALLTYPE);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						loadingDialog.dismiss();
						String data=response.getString("data");
						JSONArray ary=new JSONArray(data);
						List<JobType> jobList=JsonParser.parseJobTypeList(ary);
						list.addAll(jobList);
						break;
					case 0:
						loadingDialog.dismiss();
						Tools.toast(SelectWorkActivity.this, response.getString("msg"));
						break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}

		});

	}
	/**
	 * 修改工种
	 */
	private void changeJobList(String reason){
		if(reason.isEmpty()){
			Tools.toast(this, "请填写修改原因！");
			return;
		}
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETUPDATESERVICE);
		params.put("workerid", userId);
		params.put("servicelist", jobIds);
		params.put("why", reason);
		HttpUtil.post( params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
					case 1:
						Tools.toast(SelectWorkActivity.this, "提交成功！请耐心等待后台审核");
						finish();
						break;
					default:
						Tools.toast(SelectWorkActivity.this, response.getString("msg"));
						break;
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}


	private void changeDialog(){
		View view=View.inflate(this,R.layout.dialog_select_job, null);
		final EditText et_score=(EditText) view.findViewById(R.id.et_score);
		Button btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
		final Button btn_sure=(Button) view.findViewById(R.id.btn_sure);
		final AlertDialog dialog=new AlertDialog.Builder(this).create();
		//alertDialog中点击edittext无法弹出输入法  在show方法之前调用此方法
		dialog.setView(getLayoutInflater().inflate(R.layout.dialog_select_job, null));
		dialog.show();
		dialog.getWindow().setContentView(view);
		WindowManager wm=getWindowManager();
		Display d=wm.getDefaultDisplay();
		WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
		//params.height=(int)(d.getHeight()*0.4);
		params.width= d.getWidth();
		dialog.getWindow().setAttributes(params);
		dialog.setCanceledOnTouchOutside(true);
		OnClickListener  listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_cancel:
					dialog.dismiss();
					break;
				case R.id.btn_sure:
					changeJobList(et_score.getText().toString());
					break;
				}

			}
		};
		btn_cancel.setOnClickListener(listener);
		btn_sure.setOnClickListener(listener);
	}
	




	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.tv_right:
			if(jobIds==null||jobIds.isEmpty()){
				Tools.toast(this, "至少选择一个工种");
				return;
			}
			if(jobIds.split(",").length>3){
				Tools.toast(this, "最多只能选择三个工种");
				return;
			}
			changeDialog();
			break;
		}
	}




	@Override
	public void check() {
		adapter.notifyDataSetChanged();
		jobIds="";
		for(int i=0;i<list.size();i++){
			if(list.get(i).isChoose()){
				jobIds+=list.get(i).getJobId()+",";
			}
			
		}
		if(jobIds!=null&&!jobIds.isEmpty()){
			jobIds=jobIds.substring(0,jobIds.length()-1);
		}
		
		MyApplication.getInstance().setSelectJob(jobIds);
	}



}
