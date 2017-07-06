package com.jumeng.repairmanager.activity;

import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.receiver.GlobleController.MyListener;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public  class BaseActivity extends Activity implements OnClickListener ,MyListener{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		GlobleController.getInstance().addMyListener(this);

	}



	@Override  
	public Resources getResources() {  
		Resources res = super.getResources();    
		Configuration config=new Configuration();    
		config.setToDefaults();    
		res.updateConfiguration(config,res.getDisplayMetrics() );  
		return res;  
	}  


	public void onClick(View v){

	}



	@Override
	public void newOrder() {
		// TODO Auto-generated method stub

	}



	@Override
	public void robOrder() {
		// TODO Auto-generated method stub

	}



	@Override
	public void payOrder() {
		// TODO Auto-generated method stub

	}



	@Override
	public void cancelOrder() {
		// TODO Auto-generated method stub

	}



	@Override
	public void arrive() {
		// TODO Auto-generated method stub

	}



	@Override
	public void serviceForm() {
		// TODO Auto-generated method stub

	}



	@Override
	public void systemForm() {
		// TODO Auto-generated method stub

	}



	@Override
	public void startService() {
		// TODO Auto-generated method stub

	}



	@Override
	public void endService() {
		// TODO Auto-generated method stub

	}



	@Override
	public void pass() {
		// TODO Auto-generated method stub

	}



	@Override
	public void unusualLogin() {
		// TODO Auto-generated method stub

	}

}
